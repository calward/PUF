package data_analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

//TODO fix the methods which maximize and minimize things.... or atleast verify that they work. I think there may be some bugs with them.

/** Generates statistics on results generated by model_compare.java.
 * NOTE: there are bugs in the maximize, minimize functions for authentication accuracy.
 * I did min/max authentication accuracy analysis in another program after the fact.
 * The results on authentication accuracy, false positive, and false negative may be trusted though.
 */
public class Statistics {
	final static String OUTPUT_FILE_NAME = "results/statistics.txt";
	final static String AUTHENTICATION_ACCURACY_OUTPUT_FILE_NAME = "results/statistics_authentication_accuracy.txt";
	final static String INPUT_FILE_NAME = "results/model_compare_output.txt"; // comes from model_compare.java.... must rename model_compare_output.txt
	
	//TODO parses data that has already been generated by Model_compare.java
	// this is useful because Model_compare is time consuming and doesn't have to be run again
	public static void main(String args[]){
		ArrayList<Double> should_authenticate = new ArrayList<Double>();
		ArrayList<Integer> should_authenticate_base_model_size = new ArrayList<Integer>();
		ArrayList<Integer> should_authenticate_auth_model_size = new ArrayList<Integer>();
		
		ArrayList<Double> should_not_authenticate = new ArrayList<Double>();
		ArrayList<Integer> should_not_authenticate_base_model_size = new ArrayList<Integer>();
		ArrayList<Integer> should_not_authenticate_auth_model_size = new ArrayList<Integer>();
		
		//TODO read in the percentages lists, sorted based on whether they should authenticate or not
		Scanner input = null;
		
		try {
			input = new Scanner(new File(INPUT_FILE_NAME));
			String line = "";
			String[] split_line;
			
			String base_data_path = "";
			String auth_data_path = "";
			
			int base_model_size=0;
			int auth_model_size=0;
			double authentication_percentage = 0;
			
			//kill the first line(header)
			input.nextLine();
			
			while(input.hasNextLine()){
				line = input.nextLine();
				
				//parse the line
				split_line = line.split("\t");
				
				base_data_path=split_line[0];
				auth_data_path=split_line[1];
				base_model_size=Integer.valueOf(split_line[5]);
				auth_model_size=Integer.valueOf(split_line[6]);
				authentication_percentage=Double.valueOf(split_line[7]);
				
				if(base_data_path.equals(auth_data_path)){
					//put it in the should authenticate list
					should_authenticate.add(authentication_percentage);
					
					//keep parallel arrays of the base and auth model sizes
					should_authenticate_base_model_size.add(base_model_size);
					should_authenticate_auth_model_size.add(auth_model_size);
				}else{
					//put it in the should not authenticate list
					should_not_authenticate.add(authentication_percentage);
					
					//keep parallel arrays of the base and auth model sizes
					should_not_authenticate_base_model_size.add(base_model_size);
					should_not_authenticate_auth_model_size.add(auth_model_size);
				}
			}
			//System.out.println(should_authenticate.get(0));
			//System.out.println(should_not_authenticate.get(0));
			
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("input file is on mars or somethin'");
			e.printStackTrace();
		}

		//TODO TODO TODO uncomment 3 lines
		//TODO call methods to retrieve statistics on the lists
		//double best_threshold = best_authentication_percentage(should_authenticate, should_not_authenticate);
		//double minimized_false_positive_threshold = minimize_false_positive_authentication_percentage(should_authenticate, should_not_authenticate);
		//double equalized_false_positive_negative_threshold = equal_false_positive_negative_authentication_percentage(should_authenticate, should_not_authenticate);
		
		//TODO print out the results to output_file
		PrintWriter output = null;
		
//		try {
//			output = new PrintWriter(OUTPUT_FILE_NAME, "UTF-8");
//
//			// print out the probability for each individual compairason
//			output.println("best_threshold: "+best_threshold+"\n"
//					+ "false_positive_percentage: "+false_positive_percentage(best_threshold, should_authenticate, should_not_authenticate)+"\n"
//					+ "false_negative_percentage: "+false_negative_percentage(best_threshold, should_authenticate, should_not_authenticate)+"\n"
//					+ "number_of_tests_conducted: "+(should_authenticate.size()+should_not_authenticate.size()+"\n"));
//
//			output.println("minimized_false_positive_threshold: "+minimized_false_positive_threshold+"\n"
//					+ "false_positive_percentage: "+false_positive_percentage(minimized_false_positive_threshold, should_authenticate, should_not_authenticate)+"\n"
//					+ "false_negative_percentage: "+false_negative_percentage(minimized_false_positive_threshold, should_authenticate, should_not_authenticate)+"\n"
//					+ "number_of_tests_conducted: "+(should_authenticate.size()+should_not_authenticate.size()+"\n"));
//
//			output.println("equalized_false_positive_negative_threshold: "+equalized_false_positive_negative_threshold+"\n"
//					+ "false_positive_percentage: "+false_positive_percentage(equalized_false_positive_negative_threshold, should_authenticate, should_not_authenticate)+"\n"
//					+ "false_negative_percentage: "+false_negative_percentage(equalized_false_positive_negative_threshold, should_authenticate, should_not_authenticate)+"\n"
//					+ "number_of_tests_conducted: "+(should_authenticate.size()+should_not_authenticate.size()+"\n"));
//
//			//now print out all the results at each potential authentication threshold
//			output.println("authentication_threshold\t"
//					+"false_positive_percentage\t"
//					+"false_negative_percentage\t"
//					+"authentication_accuracy");
//
//			//try a range of authentication thresholds
//			int step_number = 100000;
//			double beginning = 0.0;
//			// end must be between beginning and 1.0
//			double end = 0.001;
//			for(int i=0;i<step_number;i++){
//				//go from .01 to 100, in steps of .0001
//				double current_threshold = beginning+(end/((double)step_number))*i;
//
//				//System.out.println(String.format("%f", false_positive_percentage(current_threshold, should_authenticate, should_not_authenticate)));
//
//				output.println(current_threshold+"\t"
//						+false_positive_percentage(current_threshold, should_authenticate, should_not_authenticate)+"\t"
//						+false_negative_percentage(current_threshold, should_authenticate, should_not_authenticate)+"\t"
//						+authentication_accuracy(current_threshold, should_authenticate, should_not_authenticate));
//			}
//
//			output.close();
//		} catch (Exception e) {
//			System.out.println("Failed to open output file");
//			e.printStackTrace();
//		}
		
		//TODO need to determine how authentication accurracy depents on model size
		try{
			output = new PrintWriter(AUTHENTICATION_ACCURACY_OUTPUT_FILE_NAME, "UTF-8");
			
			//TODO populate these two arrays with average auth accuracy and model size associated with that auth accuracy
			ArrayList<Model_combination> model_combination_list = new ArrayList<Model_combination>();
			
			//TODO create a list of the model sizes for both user model and auth model
			ArrayList<Integer> base_model_dummy = new ArrayList<Integer>(should_authenticate_base_model_size);
			base_model_dummy.addAll(should_not_authenticate_base_model_size);
			
			ArrayList<Integer> auth_model_dummy = new ArrayList<Integer>(should_authenticate_auth_model_size);
			auth_model_dummy.addAll(should_not_authenticate_auth_model_size);
			
			Set<Integer> user_model_size_list = new HashSet<Integer>(base_model_dummy);
			Set<Integer> auth_model_size_list = new HashSet<Integer>(auth_model_dummy);
			
			// create the list of model combinations
			for(Integer current_user_size : user_model_size_list){
				for(Integer current_auth_size : auth_model_size_list){
					model_combination_list.add(new Model_combination(current_user_size, current_auth_size));
				}
			}
			
			//TODO for each model combination determine the authentication accuracy
			//things needed to determine this:
			//1) authentication percentage
			//2) list of should authenticate percentages
			//3) list of should not authenticate percentages
			double auth_threshold = best_authentication_percentage(should_authenticate, should_not_authenticate); //(1)
			
			ArrayList<Double> current_should_authenticate_list; //(2)
			ArrayList<Double> current_should_not_authenticate_list; //(3)
			
			for(Model_combination model_combo : model_combination_list){
				current_should_authenticate_list = new ArrayList<Double>();
				current_should_not_authenticate_list = new ArrayList<Double>();
				
				//TODO generate the lists and find the authentication accuracy
				for(int i=0;i<should_authenticate_base_model_size.size();i++){
					if((model_combo.user_model_size == should_authenticate_base_model_size.get(i)) && (model_combo.auth_model_size == should_authenticate_auth_model_size.get(i))){
						current_should_authenticate_list.add(should_authenticate.get(i));
					}
				}
				
				for(int i=0;i<should_not_authenticate_base_model_size.size();i++){
					if((model_combo.user_model_size == should_not_authenticate_base_model_size.get(i)) && (model_combo.auth_model_size == should_not_authenticate_auth_model_size.get(i))){
						current_should_not_authenticate_list.add(should_not_authenticate.get(i));
					}
				}
				
				//find the authentication accuracy
				model_combo.authentication_accuracy=authentication_accuracy(auth_threshold, current_should_authenticate_list, current_should_not_authenticate_list);
			}
			
			//NOW each model combination object has the authentication accuracy max_value
			// print out average authentication accuracy vs. model size
			output.println("base_model_size\t"
					+"auth_model_size\t"
					+"authentication_accuracy");
			
			for(Model_combination model_combo : model_combination_list){
				output.println(model_combo.user_model_size+"\t"+ model_combo.auth_model_size+"\t"+model_combo.authentication_accuracy);
			}
			
			output.close();
		} catch (Exception e) {
			System.out.println("Failed to open output file");
			e.printStackTrace();
		}
	}
	
	
	//this class is used to store information about the user model, auth model, and their authentication accuracy
	private static class Model_combination{
		public int user_model_size;
		public int auth_model_size;
		public double authentication_accuracy;
		
		public Model_combination(int user_model_size, int auth_model_size){
			this.user_model_size=user_model_size;
			this.auth_model_size=auth_model_size;
			authentication_accuracy=0;
		}
	}
	
	
	//defined as the percentage of should not authenticate percentages which did authenticate
	public static double false_positive_percentage(double authentication_percentage, List<Double> should_authenticate_percentages, List<Double> should_not_authenticate_percentages){
		double count=0;
		
		for(double percentage : should_not_authenticate_percentages){
			if(percentage >= authentication_percentage){
				count++;
			}
		}
		
		return count/should_not_authenticate_percentages.size();
	}
	
	
	//defined as the percentage of should authenticate percentages which did not authenticate
	public static double false_negative_percentage(double authentication_percentage, List<Double> should_authenticate_percentages, List<Double> should_not_authenticate_percentages){
		double count=0;
		
		for(double percentage : should_authenticate_percentages){
			if(percentage < authentication_percentage){
				count++;
			}
		}
		
		return count/should_authenticate_percentages.size();
	}
	
	
	//TODO remove override
	// returns the authenticaion_percentage where false_positive_percentage + false_negative_percentage is the smallest
	public static double best_authentication_percentage(List<Double> should_authenticate_percentages, List<Double> should_not_authenticate_percentages){
		double best_percentage=2;
		double current_false_positive_percentage=1;
		double current_false_negative_percentage=1;
		
		ArrayList<Double> possible_percentages = new ArrayList<Double>();
		
		//create a list of percentages to try
		//add a null as a dummy max_value for the first test
		possible_percentages.add(null);
		
		for(int i=0;i<200;i++){
			//go every half percent
			possible_percentages.add(i*.05);
		}
		
		//try the percentages removing the ones that doesn't work
		while(possible_percentages.size()>1){
			//find the relevent percentages for the next thing in the list
			current_false_positive_percentage = false_positive_percentage(possible_percentages.get(1), should_authenticate_percentages, should_not_authenticate_percentages);
			current_false_negative_percentage = false_negative_percentage(possible_percentages.get(1), should_authenticate_percentages, should_not_authenticate_percentages);
			
			if((current_false_positive_percentage+current_false_negative_percentage)<best_percentage){
				//the current best percentage will always be first in the possible_percentages list
				//this percentage is no longer the best, so remove the first possible percentage
				possible_percentages.remove(0);
				
				
				best_percentage=current_false_positive_percentage+current_false_negative_percentage;
			}else{
				//the current best percentage is still the best, remove the second item in the list
				possible_percentages.remove(1);
			}
		}

		//TODO returns a rediculious number
		//TODO so future tim will know that this method needs to be fixed
		//return possible_percentages.get(0);
		return -10;
	}
	
	
	//minimizes the false positive percentage
	public static double minimize_false_positive_authentication_percentage(List<Double> should_authenticate_percentages, List<Double> should_not_authenticate_percentages){
		double best_percentage=2;
		double current_false_positive_percentage=1;
		
		ArrayList<Double> possible_percentages = new ArrayList<Double>();
		
		//create a list of percentages to try
		//add a null as a dummy max_value for the first test
		possible_percentages.add(null);
		
		for(int i=0;i<200;i++){
			//go every half percent
			possible_percentages.add(i*.05);
		}
		
		//try the percentages removing the ones that doesn't work
		while(possible_percentages.size()>1){
			//find the relevent percentages for the next thing in the list
			current_false_positive_percentage = false_positive_percentage(possible_percentages.get(1), should_authenticate_percentages, should_not_authenticate_percentages);
			
			if(current_false_positive_percentage<best_percentage){
				//the current best percentage will always be first in the possible_percentages list
				//this percentage is no longer the best, so remove the first possible percentage
				possible_percentages.remove(0);
				
				
				best_percentage=current_false_positive_percentage;
			}else{
				//the current best percentage is still the best, remove the second item in the list
				possible_percentages.remove(1);
			}
		}
		
		return possible_percentages.get(0);
	}
	
	
	// returns the authentication percentage where false negative percentage and false positive percentage are the closest
	public static double equal_false_positive_negative_authentication_percentage(List<Double> should_authenticate_percentages, List<Double> should_not_authenticate_percentages){
		double best_percentage=2;
		double current_false_positive_percentage=1;
		double current_false_negative_percentage=1;
		
		ArrayList<Double> possible_percentages = new ArrayList<Double>();
		
		//create a list of percentages to try
		//add a null as a dummy max_value for the first test
		possible_percentages.add(null);
		
		for(int i=0;i<200;i++){
			//go every half percent
			possible_percentages.add(i*.05);
		}
		
		//try the percentages removing the ones that doesn't work
		while(possible_percentages.size()>1){
			//find the relevent percentages for the next thing in the list
			current_false_positive_percentage = false_positive_percentage(possible_percentages.get(1), should_authenticate_percentages, should_not_authenticate_percentages);
			current_false_negative_percentage = false_negative_percentage(possible_percentages.get(1), should_authenticate_percentages, should_not_authenticate_percentages);
			
			if(Math.abs(current_false_positive_percentage-current_false_negative_percentage)<best_percentage){
				//the current best percentage will always be first in the possible_percentages list
				//this percentage is no longer the best, so remove the first possible percentage
				possible_percentages.remove(0);
				
				
				best_percentage=Math.abs(current_false_positive_percentage-current_false_negative_percentage);
			}else{
				//the current best percentage is still the best, remove the second item in the list
				possible_percentages.remove(1);
			}
		}
		
		return possible_percentages.get(0);
	}


	// returns the authentication accuracy. defined as [number correct authentications / total authentications]... this is weighted by the number of things in each list
	public static double authentication_accuracy(double authentication_percentage, List<Double> should_authenticate_percentages, List<Double> should_not_authenticate_percentages){
		int correct_should_authenticate_count = 0;
		int correct_should_not_authenticate_count = 0;
		
		for(Double percentage : should_authenticate_percentages){
			if(percentage >= authentication_percentage){
				correct_should_authenticate_count++;
			}
		}
		
		for(Double percentage : should_not_authenticate_percentages){
			if(percentage < authentication_percentage){
				correct_should_not_authenticate_count++;
			}
		}
		
		//TODO should these be weighted?
		//calculate this as a weighted average
		//double should_authenticate_weight = should_authenticate_percentages.size()/((double)(should_authenticate_percentages.size()+should_not_authenticate_percentages.size()));
		//double should_not_authenticate_weight = should_not_authenticate_percentages.size()/((double)(should_authenticate_percentages.size()+should_not_authenticate_percentages.size()));
		
		return (correct_should_authenticate_count+correct_should_not_authenticate_count)/((double)(should_authenticate_percentages.size()+should_not_authenticate_percentages.size()));
		//return ((double)(correct_should_authenticate_count*should_authenticate_weight+correct_should_not_authenticate_count*should_not_authenticate_weight))/((double)(should_authenticate_percentages.size()+should_not_authenticate_percentages.size()));
	}
}