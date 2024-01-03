package TrabalhoPratico1;

//class that handle the output to the queue
//as the queue explicitly needs the 3 values in 1 object
//but this can be done separately 
public class OutputSpec_v2 {

	private double value;
	private String type;
	private int producer_id;
	private int consumer_id;
	private long consumerExecutionTime;
	private long producerExecutionTime;

	public OutputSpec_v2(double value, String type, int producer_id , int consumer_id,long consumerExecutionTime,long producerExecutionTime) {
		this.value = value;
		this.type = type;
		this.producer_id = producer_id;
		this.consumerExecutionTime=consumerExecutionTime;
		this.consumerExecutionTime=producerExecutionTime;
		this.consumer_id = consumer_id;
	}


	public double getValue() {
		return value;
	}
	
	

	public long getConsumerExecutionTime() {
		return consumerExecutionTime;
	}


	public void setConsumerExecutionTime(long consumerExecutionTime) {
		this.consumerExecutionTime = consumerExecutionTime;
	}


	public long getProducerExecutionTime() {
		return producerExecutionTime;
	}


	public void setProducerExecutionTime(long producerExecutionTime) {
		this.producerExecutionTime = producerExecutionTime;
	}


	public long getExecutionTime() {
		return consumerExecutionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.consumerExecutionTime = executionTime;
	}

	public int getConsumer_id() {
		return consumer_id;
	}


	public void setConsumer_id(int consumer_id) {
		this.consumer_id = consumer_id;
	}


	public int getProducer_id() {
		return producer_id;
	}


	public void setProducer_id(int producer_id) {
		this.producer_id = producer_id;
	}

	public void setValue(double value) {
		this.value = value;
	}

	
	public int getproducer_id() {
		return producer_id;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	@Override
	public String toString() {
		return "OutputSpec_v2 [value=" + value + ", type=" + type + ", producer_id=" + producer_id + ", consumer_id="
				+ consumer_id + ", executionTime=" + consumerExecutionTime + "]";
	}

	

}
