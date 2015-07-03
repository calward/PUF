package data_analysis;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


///The purpose of this class is to test out the model compare process on data that has been collected
///The data to used will be contained in the data_sets folder
/// input: 	data_sets folder
/// output: model_compare_output.txt
public class Model_compare {
	//setting this to true will print out the probability of each test instead of the min,max,average
	final static boolean PRINT_ALL_PROBABILITY = false;
	
	//specify the input, output locations
	private final static String output_file_name = "model_compare_output.txt";
	private final static String input_folder_name = "data_sets";
	
	//specify different model sizes and 
	private final static int[] window_sizes = {3};
	private final static int[] token_sizes = {7};
	private final static int[] thresholds = {5000};
	private final static int[] user_model_sizes = {5000};
	private final static int[] auth_model_sizes = {4000};
	
	
	public static void main(String[] args){
		ArrayList<Model_compare_thread> test_models = new ArrayList<Model_compare_thread>();
		ArrayList<Thread> threads = new ArrayList<Thread>();
		ArrayList<String> data_sets = new ArrayList<String>();
		
		//read in all data set paths into data_sets
		File[] files = new File(input_folder_name).listFiles();
		for(File file : files){
		  if(file.isFile()){
		    data_sets.add(file.getAbsolutePath());
		  }
		}
		
		///create a number of tests with different parameters
		for(int a=0;a<window_sizes.length;a++){
			for(int b=0;b<token_sizes.length;b++){
				for(int c=0;c<thresholds.length;c++){
					for(int d=0;d<user_model_sizes.length;d++){
						for(int e=0;e<auth_model_sizes.length;e++){
							for(int f=0;f<data_sets.size();f++){
								for(int g=0;g<data_sets.size();g++){
									test_models.add(
											new Model_compare_thread(
													data_sets.get(f),
													data_sets.get(g),
													user_model_sizes[d],
													auth_model_sizes[e],
													window_sizes[a],
													token_sizes[b],
													thresholds[c]));
								}
							}
						}
					}
				}
			}
		}
		
		//run all threads
		for(int i=0;i<test_models.size();i++){
			threads.add(new Thread(test_models.get(i)));
			threads.get(i).start();
		}
		
		// join all threads
		for(int i=0;i<threads.size();i++){
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			print_progress(i*(100.0/threads.size()));
		}
		
		
		//print the results to a txt file
		print_results(test_models);
	}
	
	
	//prints out a progress bar
	private static void print_progress(double percent){
		char character='=';
		
		//make sure percent is between 0 and 100
		percent = (percent>100)?(100):(percent);
		percent = (percent<0)?(0):(percent);
		
		System.out.print("progress: 0:");
		
		for(int i=0;i<10;i++){
			System.out.print((percent>=i*10)?(character):(' '));
		}
		
		System.out.println(":100");
	}
	
	
	///output the results to a text file
	private static void print_results(List<Model_compare_thread> test_models){
		//do things based on PRINT_ALL_PROBABILITY
		//things I want to print
		//1) probability with which the data sets authenticated [min, max, average]
		//2) base data set used
		//3) auth data set used
		//4) size of base model
		//5) size of auth model
		PrintWriter output=null;
		
		try {
			output = new PrintWriter(output_file_name, "UTF-8");
			
			if(!PRINT_ALL_PROBABILITY){
				output.println("base_data_set\t"
						+ "auth_data_set\t"
						+ "window_size\t"
						+ "token_size\t"
						+ "threshold\t"
						+ "base_model_size\t"
						+ "auth_model_size\t"
						+ "min\t"
						+ "max\t"
						+ "average");
				for(int i=0;i<test_models.size();i++){	
					String auth_data_name = test_models.get(i).get_auth_data_path();
					String base_data_name = test_models.get(i).get_base_data_path();
					
					//put the data paths in a more read-able format
					String[] split_auth_string = auth_data_name.split("/");
					String[] split_base_string = base_data_name.split("/");
					
					auth_data_name = split_auth_string[split_auth_string.length-1];
					base_data_name = split_base_string[split_base_string.length-1];
					
					//output.print("-");
					output.println(base_data_name + "\t"
							+ auth_data_name + "\t"
							+ test_models.get(i).get_window_size() + "\t"
							+ test_models.get(i).get_token_size() + "\t"
							+ test_models.get(i).get_threshold() + "\t"
							+ test_models.get(i).get_base_model_size() + "\t"
							+ test_models.get(i).get_auth_model_size() + "\t"
							+ test_models.get(i).min_authentication_probability + "\t"
							+ test_models.get(i).max_authentication_probability + "\t"
							+ test_models.get(i).average_authentication_probability);
				}
			}else{
				//TODO print out the probability for each individual compairason
				output.println("base_data_set\t"
						+ "auth_data_set\t"
						+ "window_size\t"
						+ "token_size\t"
						+ "threshold\t"
						+ "base_model_size\t"
						+ "auth_model_size\t"
						+ "probability");
				for(int i=0;i<test_models.size();i++){	
					String auth_data_name = test_models.get(i).get_auth_data_path();
					String base_data_name = test_models.get(i).get_base_data_path();
					
					//put the data paths in a more read-able format
					String[] split_auth_string = auth_data_name.split("/");
					String[] split_base_string = base_data_name.split("/");
					
					auth_data_name = split_auth_string[split_auth_string.length-1];
					base_data_name = split_base_string[split_base_string.length-1];
					
					//output.print("-");
					for(int k=0;k<test_models.get(i).get_auth_probability_list().size();k++){
						output.println(base_data_name + "\t"
								+ auth_data_name + "\t"
								+ test_models.get(i).get_window_size() + "\t"
								+ test_models.get(i).get_token_size() + "\t"
								+ test_models.get(i).get_threshold() + "\t"
								+ test_models.get(i).get_base_model_size() + "\t"
								+ test_models.get(i).get_auth_model_size() + "\t"
								+ test_models.get(i).get_auth_probability_list().get(k));
					}
				}
			}
			
			output.close();
		} catch (Exception e) {
			System.out.println("Failed to open output file");
			e.printStackTrace();
		}
	}
}
