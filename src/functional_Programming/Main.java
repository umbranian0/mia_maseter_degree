/**
 * 
 */
package functional_Programming;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;


/**
 * 
 */
public class Main {
	// functional interface to make Square
	@FunctionalInterface
	interface sqrtInterface {
		double calculateSquareRoot(int i);
	}

	// functional interface to create EmpregadoFactory
	@FunctionalInterface
	interface EmpregadoFactory {
		Empregado create();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// exemplo s expressoes lambda devolvem VOID
		sqrtInterface sqrtFunc = i -> Math.sqrt(i);
		int i = 132;
		double sqrtRes = sqrtFunc.calculateSquareRoot(i);
		System.out.println(sqrtRes);

		// comparator comparator 2 Strings
		String st1 = "andre";
		String st2 = "bruno";
		Comparator<String> cscomp = (s1, s2) -> Integer.compare(s1.length(), s2.length());
		int diffLength = cscomp.compare(st1, st2);
		// use predicate function to check if :
		// Rest of cscomp is 0 ?
		// yes : same length
		// no : different length
		Predicate<?> isSameLength = (b) -> b.equals(0);
		boolean isSame = isSameLength.equals(diffLength);
		System.out.println("is the same length ? : " + isSame);

		// comparator 2 Objects variables
		EmpregadoFactory empregadoFactory1 = () -> new Empregado(1, "Vasile", true, 1521.70);
		EmpregadoFactory empregadoFactory2 = () -> new Empregado(2, "Joao", false, 1802.1);
		EmpregadoFactory empregadoFactory3 = () -> new Empregado(3, "Joao", false, 1202.1);

		Empregado emp1 = empregadoFactory1.create();
		Empregado emp2 = empregadoFactory2.create();
		Empregado emp3 = empregadoFactory3.create();
		System.out.println("emp1 =" + emp1.toString() + "\n" + "emp2 = " + emp2.toString());
		// use comparator to check the operation
		// is the same name?
		Comparator<Empregado> cemp = (e1, e2) -> e1.getName().compareTo(e2.getName());
		boolean nestedFunction = isSameLength.equals(cemp.compare(emp1, emp2));// expected false
		System.out.println(
				"is the same name?" + nestedFunction + "objects : " + emp1.toString() + "objects : " + emp2.toString());
		//
		boolean nestedFunction2 = isSameLength.equals(cemp.compare(emp2, emp3));// expected true
		System.out.println("is the same name?" + nestedFunction2 + "objects : " + emp2.toString() + "objects : "
				+ emp3.toString());

		// sort the array by salary
		List<Empregado> emps = new ArrayList<>();
		emps.add(emp1);
		emps.add(emp2);
		emps.add(emp3);
		Collections.sort(emps, (e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()));
		System.out.println("comparator" + emps.toString());

		// example runnable lambda instance
		Runnable r1 = () -> System.out.println("Start running");
		r1.run();

		// another example of runnable
		Runnable r = new Runnable() {
			@Override
			public void run() {
				System.out.println("Start r running");
			}
		};
		r.run();

	}

}
