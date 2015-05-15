///This class represents a touch event.
public class Touch implements Comparable<Touch>{
	private char key;
	private double pressure;
	private long timestamp;

	private List<Double> probability; //TODO move parallel arraw to better data structure
	private List<Window> predecessor_window;


	public Touch(int keycode, double pressure, long timestamp){
		this.key=keycode;
		this.pressure=pressure;
		this.timestamp=timestamp;

		probability=new ArrayList<Double>();
		predecessor_window=new ArrayList<Window>();
	}


	///copy constructor
	public Touch(Touch t){
		this.key = t.key;
		this.pressure = t.pressure;
		this.timestamp = t.timestamp;

		this.probability = new ArrayList<Double>(t.probability);
		this.predecessor_window = new ArrayList<Window>(t.predecessor_window);
	}


	///sets the probability that this touch succeeds a given sequence. Reccord the sequence and the probability
	public void set_probability(Window preceeding_window, double p){
		//TODO check for correctness
		//add them at their respective locations
		//if predecessor_window already exists, update the pressure value. If predecessor_window does not exist, then add both predecessor_window and pressure
		int index = 0;

		//search for window in predecessor_window
		for(index=0;index<predecessor_window.size();index++){
			//does this window equal the current window?
			if(predecessor_window.get(i).compareTo(preceeding_window)==0){
				break;
			}
		}

		if(index < predecessor_window.size()){
			//predecessor_window already exists, updateing probability
			probability.set(index, p);
		}else{
			predecessor_window.add(preceeding_window);
			probability.add(p);
		}
	}


	///returns the probability of the touch occurring after a given window w. If the window does not exist return (TODO) currently returning 0
	public double get_probability(Window preceeding_window){
		//TODO check for correctness
		//take in a window and return the probability of this touch coming after that window
		//search for window in predecessor_window
		for(index=0;index<predecessor_window.size();index++){
			//does this window equal the current window?
			if(predecessor_window.get(i).compareTo(preceeding_window)==0){
				break;
			}
		}

		if(index < predecessor_window.size()){
			//predecessor_window already exists, updateing probability
			return probability(index);
		}else{
			//window does not exist in predecessor window
			//TODO determine what to do. is this correct?
			return 0;
		}
	}


	public double get_pressure(){
		return pressure;
	}


	public double get_key(){
		return key;
	}


	public long get_timestamp(){
		return timestamp;
	}


	///implement hash function for the touch class
	@Override
	public int hashCode(){
		//TODO make this better
		return pressure;
	}


	///compare touches to one another. return negative if this touch is less than other_touch
	@Override
	public int compareTo(Touch other_touch){
		//TODO check for correctness
		int differance = 0;

		if(this.key != other_touch.key){
			differance++;
		}

		if(this.timestamp != other_touch.timestamp){
			differance++;
		}

		if(!are_lists_equal(this, other_touch)){
			differance++;
		}

		//do this check last so if they are indeed unequal, the differance in the pressure value will be returned (most likly)
		if(this.pressure != other_touch.pressure){
			differance = this.pressure - other_touch.pressure;
		}

		return differance;
	}


	///compare the lists contained within two touches for equality. return true if they are equal. Cannot compare predecessor_window here because it will create an infinite loop in calling the compareTo functions back and forth.
	private boolean are_lists_equal(Touch t1, Touch t2){
			//TODO check for correctness
			int differance = 0;			

			Iterator<Window> this_iterator = t1.probability.iterator();
			Iterator<Window> other_iterator = t2.probability.iterator();

			while(this_iterator.hasNext()){
				Touch this_touch = this_iterator.next();
				Touch other_touch = other_iterator.next();

				//compare the touches
				if(this_touch.compareTo(other_touch)!=0){
					//incrementing differance indicates they are not equal
					differance++;
					break;
				}
			}

			return differance=0;
	}
}
