package TrabalhoPratico1;

//class that handle the output to the queue
//as the queue explicitly needs the 3 values in 1 object
//but this can be done separately 
public class OutputSpec{
	
	public double cpu;
	public double ram;
	public double diskSpace;
	
	public OutputSpec() {
		
	}
	public OutputSpec(double cpu,double ram,double diskSpace ) {
		this.cpu = cpu;
		this.ram = ram;
		this.diskSpace = diskSpace;
	}

	public double getCpu() {
		return cpu;
	}

	

	public void setCpu(double cpu) {
		this.cpu = cpu;
	}
	public void setRam(double ram) {
		this.ram = ram;
	}
	public void setDiskSpace(double diskSpace) {
		this.diskSpace = diskSpace;
	}
	public double getRam() {
		return ram;
	}


	public double getDiskSpace() {
		return diskSpace;
	}

	@Override
	public String toString() {
		return "OutputSpec [cpu=" + cpu + ", ram=" + ram + ", diskSpace=" + diskSpace + "]";
	}

	
	

}
