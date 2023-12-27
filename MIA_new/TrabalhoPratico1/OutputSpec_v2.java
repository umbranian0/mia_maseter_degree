package TrabalhoPratico1;

import javax.swing.Spring;

//class that handle the output to the queue
//as the queue explicitly needs the 3 values in 1 object
//but this can be done separately 
public class OutputSpec_v2{
	
	public double value;
	public String type;
	
	
	public OutputSpec_v2(double value,String type ) {
		this.value = value;
		this.type = type;
	}

	
	

	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "OutputSpec_v2 [value=" + value + ", type=" + type + "]";
	}

	
	

}
