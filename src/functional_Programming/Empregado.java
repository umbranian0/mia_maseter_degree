package functional_Programming;

public class Empregado {
		
	//attributes
	private int id;
	private String name;
	private boolean isActive;
	private double salary;
	
	public Empregado() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public Empregado(int id, String name, boolean isActive) {
		super();
		this.id = id;
		this.name = name;
		this.isActive = isActive;
	}

	public Empregado(int id, String name, boolean isActive, double salary) {
		super();
		this.id = id;
		this.name = name;
		this.isActive = isActive;
		this.salary = salary;
	}
	//acessors

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	

	public double getSalary() {
		return salary;
	}



	public void setSalary(double salary) {
		this.salary = salary;
	}



	@Override
	public String toString() {
		return "Empregado [id=" + id + ", name=" + name + ", isActive=" + isActive + ", salary=" + salary + "]";
	}





}
