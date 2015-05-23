package runtime;
import components.Chain;

//TODO cause something that makes sence to happen when the user fails the authentication
///This thread will call the compare method of chain class. The goal is to compare user chain and auth chain and make the result, pass/fail known. Or do something based on pass/fail such as cause the phone to lock.

public class CompareChains implements Runnable{
	final double AUTHENTICATION_THRESHOLD = .7; //TODO find a reasonable / justifiable value for the authentication threshold	
	
	private volatile boolean is_authentic;
	private volatile boolean complete; //indicates is_authentic contains the result 
	
	private Chain user_chain;
	private Chain auth_chain;
	
	/// will need to make copies of the chains passed in so they do not get updated by something else during the comparason
	public CompareChains(Chain user_chain, Chain auth_chain){
		this.user_chain = new Chain(user_chain);
		this.auth_chain = new Chain(auth_chain);
		
		is_authentic = false;
		complete = false;
	}

	
	///compare user_chain and auth_chain and choose what to do with the result
	@Override
	public void run(){
		double differance;

		Distribution_thread distribution_thread = new Distribution_thread();
		Key_distribution_thread key_distribution_thread = new Key_distribution_thread();
		Touch_probability_thread touch_probability_thread = new Touch_probability_thread();

		Thread dt = new Thread(distribution_thread);
		Thread kdt = new Thread(key_distribution_thread);
		Thread tpt = new Thread(touch_probability_thread);

		// start a separate thread for each computation
		dt.start();
		kdt.start();
		tpt.start();
		
		// wait for each of the computation threads to finish
		//TODO handle InterruptedException
		try {
			dt.join();
			kdt.join();
			tpt.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		///preform the comparason now that the values are cached in the Chain's	
		differance = user_chain.compare_to(auth_chain);
		
		if(!is_user_authentic(differance)){
			//TODO determine what to do when the user fails the authentication
			//user fails the authentication
			//possibly cause the lock screen to come up
			is_authentic = false;
			System.out.println("User fails to authenticate");
		}else{
			is_authentic = true;
		}
		complete=true;
	}
	
	
	///returns the result of the authentication. This method does not provide any guarentees that the compairason has finsihed yet. If the compairason has not yet finished it will return false;
	public boolean get_auth_result(){
		return is_authentic;
	}
	
	
	//returns true if the authentication has completed and is_authentic holds the result.
	public boolean get_auth_complete(){
		return complete;
	}
	
	
	///based on the value passed in, determine whether the user should be authenticaed or not. This is split out into a method because it will probably be more complex than this.
	private boolean is_user_authentic(double differance){
		return differance > AUTHENTICATION_THRESHOLD;
	}

	
	/// These computation threads work because the results are cached in chain
	///preforms the distributation computation
	private class Distribution_thread implements Runnable{
		@Override
		public void run(){
			user_chain.get_distribution();
			auth_chain.get_distribution();
		}
	}

	///preforms the key_distribution computation
	private class Key_distribution_thread implements Runnable{
		@Override
		public void run(){
			user_chain.get_key_distribution();
			auth_chain.get_key_distribution();
		}
	}


	///preforms the probability computation
	private class Touch_probability_thread implements Runnable{
		@Override
		public void run(){
			user_chain.get_touch_probability(null, 0);
			auth_chain.get_touch_probability(null, 0);
		}
	}
}
