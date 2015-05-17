///TODO generate a csv file for testing
///This class is used to test that the model is being built correctly. Also tested is the model compairason. and various classes used in model creating. The idea is to print out the tests which fail.
///This class should have to do no actual work if the program is designed well.
public class Main{
	final String TOUCH_TEST_FILE_NAME = "test_touch.csv";
	final int TOUCH_TEST_FILE_LENGTH = 1000;
	
	///enumerate the differant types of tests which may be run
	private enum TestTypes{
		SPEED ("Speed, output execution time of operations",0),
		CORRECTNESS ("Correctness, output pass / fail indicators of correctness",1);

		private final String description;
		private final int identifier;

		public String toString(){
			return description;
		}
		
		public int get_identifier(){
			return identifier;
		}
	}

	///enumerate the different types of test files which may be built. Some inner enumerations are defined. The idea is that the test file may be any combination of these inner enumerations.
	private enum TestFiles{
		//TODO um.... check if this works... yeah
		PREEXISTING ("Preexisting file in working directory named: "+TOUCH_TEST_FILE_NAME, 0),
		SELECT ("Build a test file", 1);

		private final String description;
		private final int identifier;

		public String toString(){
			return description;
		}
		
		public int get_identifier(){
			return identifier;
		}
		
		//enumerate properties of test files
		//the amount of pressure, this will determine the median pressure
		public enum PressureAmount{
			HIGH ("High pressure, 0.75", 0, .75),
			MEDIUM ("Medium Pressure, 0.5", 1, .5),
			LOW ("Low Pressure, 0.25", 2, .25);

			private final String description;
			private final int identifier;
			private final double value;
			
			public String toString(){
				return description;
			}
		
			public int get_identifier(){
				return identifier;
			}

			public double get_value(){
				return value;
			}
		}

		//Defines what type of distribution will be written to the test file
		//TODO decide the value
		public enum Distribution{
			NORMAL ("Normal, centered about pressure median", 0, null),
			ABNORMAL ("Abnormal, centered about pressure median, but inverted", 1, null),
			RANDOM ("Random, completly and utterly random", 2, null);

			private final String description;
			private final int identifier;
			private final double value;
			
			public String toString(){
				return description;
			}
		
			public int get_identifier(){
				return identifier;
			}
		
			public double get_value(){
				return value;
			}
		}

		//this determines the width of the distribution. high concentration implies low standard deviation.
		//TODO decide the value
		public enum Concentration{
			HIGH ("High, [std deviation]", 0, null),
			MEDIUM ("Medium, [std deviation]",1, null),
			LOW ("Low, [std deviation]",2, null);

			private final String description;
			private final int identifier;
			private final double value;
			
			public String toString(){
				return description;
			}
		
			public int get_identifier(){
				return identifier;
			}
		
			public double get_value(){
				return value;
			}
		}
	}

	//all methods return true if they pass
	public static void Main(String args[]){
		// allow the user to select between tests for correctness and tests for speed
		TestTypes type = get_user_input_test_type();

		//construct the test file
		build_touch_test_file(TOUCH_TEST_FILE_NAME);

		switch(type){
			case CORRECTNESS:
				correctness_test();
				break;
			case SPEED:
				speed_test();
				break;
		}
	}


	///TODO builds a test csv file containing touches. This will be read in to build the marcov model. Make this easy to modify. The goal is to be able to test how different types of data will affect the model.
	private static void build_touch_test_file(String file_name){
		//TODO prompt the user, asking which type of test file they would like to generate.
		Scanner input = new Scanner(System.in);
		boolean invalid = true;
		TestFiles type;
		
		while(invalid){
			prompt_test_file_type();

			type = input.nextLine();
			for(TestFiles t_f : TestFiles.values(){
				if(t_f.get_identifier()==type){
					invalid = false;
					break;
				}
			}
		}

		switch(type){
			case PREEXISTING:
				break;
			case SELECT:
				//ask for differant properties of the file
				double file_distribution = get_file_distribution();
				double file_concentration = get_file_concentration();
				double file_pressure_amount = get_file_pressure_amount();

				//construct the file based on these values
				Scanner output = new Scanner(new File(file_name));
				
				for(int i=0;i<TOUCH_TEST_FILE_LENGTH;i++){
					//TODO generate touch information
					//TODO write touch information to output
				}

				break;
		}
	}


	// get user input from std in
	private static TestTypes get_user_input_test_type(){
		Scanner input = new Scanner(System.in);
		boolean invalid = true;
		TestTypes type;
		
		while(invalid){
			prompt_test_type();

			type = input.nextLine();
			for(TestTypes t_t : TestTypes.values(){
				if(t_t.get_identifier()==type){
					invalid = false;
					break;
				}
			}
		}

		return type;
	}


	// display user options for types of tests
	private static void prompt_test_type(){
		System.out.println("");		
		System.out.println("Which type of test would you like to run:");

		//for all enum values		
		for (TestTypes t_t : TestTypes.values()) {
 			System.out.println(t_t.get_identifier()+") "+t_t.toString());			
		}
	}

	
	// display user options for types of files
	private static TestFiles prompt_test_file_type(){
		System.out.println("");		
		System.out.println("Which type of test file would you like to use:");

		//for all enum values		
		for (TestFiles t_f : TestFiles.values()) {
 			System.out.println(t_f.get_identifier()+") "+t_f.toString());			
		}
	}


	// display user options for file distribution, return the value associated with the selection
	private static double get_file_distribution(){
		Scanner input = new Scanner(System.in);
		boolean invalid = true;
		TestFiles.Distribution type;
		
		while(invalid){
			display_file_distribution_options();

			type = input.nextLine();
			for(TestFiles.Distribution t_f : TestFiles.Distribution.values(){
				if(t_f.get_identifier()==type){
					invalid = false;
					break;
				}
			}
		}

		return type.get_value();
	}


	// display user options for file concentration, return the value associated with the selection
	private static double get_file_concentration(){
		Scanner input = new Scanner(System.in);
		boolean invalid = true;
		TestFiles.Concentration type;
		
		while(invalid){
			display_file_concentration_options();

			type = input.nextLine();
			for(TestFiles.Concentration t_f : TestFiles.Concentration.values(){
				if(t_f.get_identifier()==type){
					invalid = false;
					break;
				}
			}
		}

		return type.get_value();
	}


	// display user options for file pressure amount, return the value associated with the selection
	private static double get_file_pressure_amount(){
		Scanner input = new Scanner(System.in);
		boolean invalid = true;
		TestFiles.PressureAmount type;
		
		while(invalid){
			display_file_pressure_amount_options();

			type = input.nextLine();
			for(TestFiles.PressureAmount t_f : TestFiles.PressureAmount.values(){
				if(t_f.get_identifier()==type){
					invalid = false;
					break;
				}
			}
		}

		return type.get_value();
	}

	// display distribution options
	private static void display_file_distribution_options(){
		System.out.println("");		
		System.out.println("What should be the file distribution:");

		//for all enum values		
		for (TestFiles.Distribution t_f : TestFiles.Distribution.values()) {
 			System.out.println(t_f.get_identifier()+") "+t_f.toString());			
		}
	}


	// display concentration options
	private static void display_file_concentration_options(){
		System.out.println("");		
		System.out.println("What should be the file distribution:");

		//for all enum values		
		for (TestFiles.Concentration t_f : TestFiles.Concentration.values()) {
 			System.out.println(t_f.get_identifier()+") "+t_f.toString());			
		}
	}


	// display pressure amount options
	private static void display_file_pressure_amount_options(){
		System.out.println("");		
		System.out.println("What should be the file distribution:");

		//for all enum values		
		for (TestFiles.PressureAmount t_f : TestFiles.PressureAmount.values()) {
 			System.out.println(t_f.get_identifier()+") "+t_f.toString());			
		}
	}
	

	//##### BEGIN TEST CALLS #####
	//TODO call tests for speed
	private static boolean speed_test(){
		//make calls to all of the speed test methods which return a long indicating the amount of time the action took. These actions will allow me to isolate slow points in processing and attempt to fix them.
		long total_time;

		//output the results
		System.out.println("");
		System.out.println("total time taken: " + total_time);
	}


	//call tests for correctness
	private static boolean correctness_test(){
		//make calls to all of the classes to be tested
		boolean test_model_compare_success = test_model_compare();
		boolean test_distribution_success = test_distribution();
		boolean test_model_building_success = test_model_building();
		boolean test_chain_success = test_chain();

		//print out errors in a readable format
		System.out.println("program overview error log:");

		if(test_model_compare_success &&
			test_distribution_success &&
			test_model_building_success &&
			test_chain_success)
		{
			System.out.println("\tall tests successful");
			return true;
		}else{
			if(!test_model_compare_success){
				System.out.println("\tmodel_compare fails");
			}
		
			if(!test_distribution_success){
				System.out.println("\tdistribution fails");
			}
	
			if(!test_model_building_success){
				System.out.println("\tmodel_building fails");
			}

			if(!test_chain_success){
				System.out.println("\tchain fails");
			}
			return false;
		}
	}


	//#### Tests for correctness ####
	//### classes to be tested ###
	private static boolean test_model_compare(){
		//TODO test the model_compare.java file for correctness
	}
	

	private static boolean test_distribution(){
		boolean correct=true;

		//test functions in the distribution class
		boolean test_calc_min_success = test_calc_min();
		boolean test_calc_max_success = test_calc_max();
		boolean test_calc_average_success = test_calc_average();
		boolean test_calc_standard_deviation_success = test_calc_standard_deviation();
		
		//print out any errors in a readable way
		System.out.println("distribution error log:");

		if(!test_calc_min_success){
			System.out.println("\tcalc_min fails");
			correct=false;
		}
		
		if(!test_calc_max_success){
			System.out.println("\tcalc_max fails");
			correct=false;
		}

		if(!test_calc_average_success){
			System.out.println("\tcalc_average fails");
			correct=false;
		}

		if(!test_calc_standard_deviation_success){
			System.out.println("\tcalc_standard_deviation fails");
			correct=false;
		}
		
		return correct;
	}


	private static boolean test_model_building(){
		//TODO test different aspects of building the model for correctness
	}

	
	///calls methods to test the chain class
	private static boolean test_chain(){
		//TODO call methods to test the functionality of the chain class
	}


	//### methods to be tested ###
	//# Distribution class #
	private static boolean test_calc_min(){
		boolean correct = false;	
		ArrayList<Touch> touches = new ArrayList<Touch>();
		Distribution dist;

		//create a List of touches with a minimum value
		touches.add(new Touch('a',.1);
		touches.add(new Touch('a',.2);
		touches.add(new Touch('a',.3);
		touches.add(new Touch('a',.4);
		touches.add(new Touch('a',.5);
		touches.add(new Touch('a',.6);
		touches.add(new Touch('a',.7);
		touches.add(new Touch('a',.8);
		touches.add(new Touch('a',.9);

		
		//test to see that calc_min finds this value correctly
		dist = new Distribution(touches);

		if(dist.get_min()==.1){
			correct=true;
		}
		
		return correct;
	}


	private static boolean test_calc_max(){
		boolean correct = false;	
		ArrayList<Touch> touches = new ArrayList<Touch>();
		Distribution dist;

		//create a List of touches with a minimum value
		touches.add(new Touch('a',.1);
		touches.add(new Touch('a',.2);
		touches.add(new Touch('a',.3);
		touches.add(new Touch('a',.4);
		touches.add(new Touch('a',.5);
		touches.add(new Touch('a',.6);
		touches.add(new Touch('a',.7);
		touches.add(new Touch('a',.8);
		touches.add(new Touch('a',.9);

		
		//test to see that calc_min finds this value correctly
		dist = new Distribution(touches);

		if(dist.get_max()==.9){
			correct=true;
		}
		
		return correct;
	}


	private static boolean test_calc_average(){
		boolean correct = false;	
		ArrayList<Touch> touches = new ArrayList<Touch>();
		Distribution dist;

		//create a List of touches with a minimum value
		touches.add(new Touch('a',.1);
		touches.add(new Touch('a',.2);
		touches.add(new Touch('a',.3);
		touches.add(new Touch('a',.4);
		touches.add(new Touch('a',.5);
		touches.add(new Touch('a',.6);
		touches.add(new Touch('a',.7);
		touches.add(new Touch('a',.8);
		touches.add(new Touch('a',.9);

		
		//test to see that calc_min finds this value correctly
		dist = new Distribution(touches);

		if(dist.get_average()==.5){
			correct=true;
		}
		
		return correct;
	}


	private static boolean test_calc_standard_deviation(){
		boolean correct = false;	
		ArrayList<Touch> touches = new ArrayList<Touch>();
		Distribution dist;

		//create a List of touches with a minimum value
		touches.add(new Touch('a',.1);
		touches.add(new Touch('a',.2);
		touches.add(new Touch('a',.3);
		touches.add(new Touch('a',.4);
		touches.add(new Touch('a',.5);
		touches.add(new Touch('a',.6);
		touches.add(new Touch('a',.7);
		touches.add(new Touch('a',.8);
		touches.add(new Touch('a',.9);

		
		//test to see that calc_min finds this value correctly
		dist = new Distribution(touches);

		//TODO make sure this is correct
		//actual value should be around .2582
		if(dist.get_standard_deviation()>(.258)&&dist.get_standard_deviation()<(.2585)){
			correct=true;
		}
		
		return correct;
	}
	

	//TODO
	//# Model Compare #
	

	//TODO
	//# Model Building #


	//TODO
	//# Chain #

	
	//TODO
	//# Window #


	//TODO
	//# 

	
	//TODO
	//#### Tests for speed ####
	private static long test_model_build_time(){
		//initialize things
		ChainBuilder chain_builder = new ChainBuilder();

		//initialize
		chain_builder

		long start_time = System.currentTimeMillis();
		//do the method
		long end_time = System.currentTimeMillis();

		return end_time-start_time;
	}
}
