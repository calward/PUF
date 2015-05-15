///This class represents a touch event.
public class Touch{
	private char key;
	private double pressure;
	private double probability;
	private long timestamp;

	public Touch(int keycode, double pressure, long timestamp){
		this.keycode=keycode;
		this.pressure=pressure;
		probability=0;
	}

	///copy constructor
	public Touch(Touch t){
		this.key = t.get_probability();
		this.pressure = t.get_pressure();
		this.probability = t.get_key();
		this.timestamp = t.get_timestamp();
	}

	public void set_probability(double p){
		probability=p;
	}

	///returns the probability which has been assigned to this touch
	public double get_probability(){
		return probability;
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
}
