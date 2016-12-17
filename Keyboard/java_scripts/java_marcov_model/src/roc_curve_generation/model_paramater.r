#
# generate graph for how the 'performance'
# of the system depends on each of the model
# parameters
#
# general idea:
#   1. fix a threshold value given a desired FPR
#   2. evaluate how FNR preforms when a given model parameter is changed
#   3. plot one line for each model parameter
#   3.1 plot is model parameter value normalized to
#       [0,1] based on [min, max] for that parameter vs.
#       FNR
#
# i'm trying to say something about how a given
# model parameter affects the compare values
source("utility.r")

library(tools)

# enumerate the model parameters
model_parameter_list <- c("window_size", "token_size",
    "threshold", "user_model_size", "auth_model_size")

#TODO set n_threshold <- 20000
n_threshold <- 2000
data_directory <- "parameter_compare"

# maximum number of files which will be handled
max_files <- 100000

# fixed value for FPR
#TODO fix minimize_FNR function!!!!!
fixed_FPR <- 0.01

# read all the csv files in parameter compare folder
files <- list.files(path = data_directory, pattern = ".csv",
    all.files = FALSE, full.names=FALSE)

# split the file name into usable quantities
# stored in file_data by the same name as model_parameter_list
file_data <- data.frame(file_name=files)
for(i in 1:nrow(file_data)){
    # associate model parameters
    name <- read.csv(text=as.character(file_path_sans_ext(file_data$file_name[[i]])),
        header=FALSE, col.names=model_parameter_list)

    for(j in 1:length(model_parameter_list)){
        file_data[i, model_parameter_list[[j]]] <- name[1, model_parameter_list[[j]]]
    }

    # read in the data
    file_data$data[[i]] <- read.csv(paste0(data_directory, "/",
        as.character(file_data$file_name[[i]])))
}

# figure out which files belong with a given model parameter
# that is, what files vary the model parameter while keeping
# the other parameters constant
#
# for each model parameter in this list,
# create a list of corresponding data files
#
# a file can be added to multiple lists if multiple parameters vary
model_parameter_to_file_map <- data.frame(row.names=model_parameter_list)

# for each model parameter
# create a list of files
list_of_list <- vector("list", length(model_parameter_list))
for(j in 1:length(model_parameter_list)){
    # create a list of size max_files
    list <- vector("list", max_files)

    # decide whether each file should be added to this list
    index <- 1
    for(i in 1:nrow(file_data)){
        #TODO based on what should I add a file to each of the lists
        #TODO right now all files parameters have all files
        if(j != 0){
            list[[index]] <- file_data[i, "file_name"]
            index <- index + 1
        }
    }

    # remove all nulls from the list
    list <- list[!sapply(list, is.null)]

    # add the list to the list of lists
    list_of_list[[j]] <- list
}

# add the lists to the map
model_parameter_to_file_map$file_list <- I(list_of_list)

# now, all the data sets in
# model_parameter_to_file_map$file_list contain
# the data files with statistics for a given
# model parameter.
# for each model parameter
#   for each of these file in file_list,
#       I want to compute the rate data (FNR, FPR)
#
# I will add the rate data as a column in file_data
rate_data_list <- vector("list", nrow(file_data))
for(i in 1:nrow(file_data)){
    rate_data_list[[i]] <- generate_threshold_data(file_data$data[[i]], n_threshold)
}

# add the rate data to the file_data data frame
file_data$rate_data <- rate_data_list

# then I can fix FPR for each parameter
# then I can compute FNR corresponding to that FPR
# add this value as a column to the model_parameter_file_map
FNR_list <- vector("list", nrow(file_data))
for(i in 1:nrow(file_data)){
    FNR_list[[i]] <- minimize_FNR(file_data$rate_data[[i]], as.numeric(fixed_FPR))
}

# add the rate data to the file_data data frame
file_data$minimum_FNR <- FNR_list

# then I can scale all the model parameters
# scale to the interval [0,1] given [min,max] for each parameter
#
# for each model parameter
for(j in 1:length(model_parameter_list)){
    # get a list of all values of this one parameter
    x <- file_data[, model_parameter_list[[j]]]

    # standardize the data between [0,1]
    standardized_list <- (x-min(x))/(max(x)-min(x))

    # remove NaN's from the list,
    # replace them with 0
    standardized_list[is.nan(standardized_list)] <- 0

    # REPLACE the actual parameter values in file_data
    file_data[, model_parameter_list[[j]]] <- standardized_list
}

# change the row names of file_data to be the file names
rownames(file_data) <- file_data[, "file_name"]

# then I can plot FNR vs. scaled model parameter value
#
# the model_parameter_to_file_map will be used
# to determine which [parameter value, minimum_FNR_value]
# should be plotted for each parameter
#
# for each model parameter
#   create a list of parameter value and minimum_pnr value
parameter_value_list_list <- vector("list", length(model_parameter_list))
minimum_FNR_list_list <- vector("list", length(model_parameter_list))
for(i in 1:length(model_parameter_list)){
    file_list <- unlist(model_parameter_to_file_map[model_parameter_list[[i]], "file_list"][[1]])

    # for each file in the list,
    # add the corresponding: parameter value and
    # minimum_FNR_value to plot_series
    parameter_value_list <- vector("list",  length(file_list))
    minimum_FNR_list <- vector("list",  length(file_list))
    for(j in 1:length(file_list)){
        # grab the values associated with the file
        # from file_data
        parameter_value_list[[j]] <- file_data[file_list[[j]],
            model_parameter_list[[i]]]
        minimum_FNR_list[[j]] <- unlist(file_data[file_list[[j]],
            "minimum_FNR"])
    }

    # place the lists into the data frame
    parameter_value_list_list[[i]] <- parameter_value_list
    minimum_FNR_list_list[[i]] <- minimum_FNR_list
}

# place the data into a data frame
plot_series <- data.frame(parameter_value=I(parameter_value_list_list),
    minimum_FNR=I(minimum_FNR_list_list), row.names=model_parameter_list)

#
# model_parameters vs FNR
# given a fixed FPR
# describes how each of the model parameters affects the authentication accuracy
#
# plot one line for each model parameter
# the values to plot for this line are stored in plot_series
#
pdf("output/model_parameter.pdf")

# create colors, linetypes, for 4 situations
colors <- rainbow(6, start=0.4)
# determines the type of line
linetype <- c(1,2,4,5)
# determines the symbol used for the line
plotchar <- c(18:22)

# set up the plot ( this doesn't plot anything anyway,
# so the arguments doen't matter. )
plot(c(0.0), c(0.0),
    xlab="Parameters", ylab="FNR", main="Parameters vs. FNR",
    xlim=c(0,1), ylim=c(0,1), type="n")

unlist(plot_series["token_size", "parameter_value"])
unlist(plot_series["token_size", "minimum_FNR"])

unlist(plot_series["window_size", "parameter_value"])
unlist(plot_series["window_size", "minimum_FNR"])

#TODO token size doesn't seem to be being plotted

# plot each of the parameters
for(i in 1:length(model_parameter_list)){
    lines(unlist(plot_series[model_parameter_list[[i]], "parameter_value"]),
        unlist(plot_series[model_parameter_list[[i]], "minimum_FNR"]),
        type="l", lwd=1.5, lty=linetype[i-1], col=colors[i-1], pch=plotchar[i-1])
}

# explain that the FPR is fixed
text(.50, .99, labels=paste("FPR =", fixed_FPR))

# make a legend
legend(0.60, 1.0, model_parameter_list, cex=1.0, col=colors,
    lty=linetype, title="Parameter") #pch=plotchar,

dev.off()