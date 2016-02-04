package com.sapient.java8.lamda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@FunctionalInterface
interface MathOperation {
    int operation(int a, int b);
}

@FunctionalInterface
interface AdvancedMathOperation {
    int operation(int a);
}

@FunctionalInterface
interface VerifyEmptyORNull<T> {
    boolean isNotNullOrEmpty(T a);
}



public class SortingByLamda {
	int methodLevelVariable = 20;
	SortingByLamda(){
		methodLevelVariable = 40;
	}
	
	private void testLamdaExpressions(int i, int j){
		
		//with type declaration and return statement along with curly braces
		MathOperation obj = (int num1, int num2) -> {return num1+num2;};
		System.out.println("Addition......"+ obj.operation(i, j));
		//with type declaration
		obj = (int num1, int num2) -> num1/num2;
		System.out.println("Division......"+ obj.operation(i, j));
		//without type declaration
		obj = (num1, num2) -> num1*num2;
		System.out.println("Multiplication......"+ obj.operation(i, j));
			
		
		//with type declaration and return statement along with curly braces
		AdvancedMathOperation obj1 = (int num1) -> {System.out.println("********"+methodLevelVariable);return num1*num1;};
		System.out.println("Power(N,2)......"+ obj1.operation(i));
		//without type declaration and with return statement along with curly braces 
		obj1 = (num1) -> {return num1*num1*num1;};
		System.out.println("Power(N,3)......"+ obj1.operation(i));
		//without type declaration 
		obj1 = num1 -> num1*num1*num1*num1;
		System.out.println("Power(N,4)......"+ obj1.operation(i));
	}
	
	
	private  void toString(List<String> names, VerifyEmptyORNull<String> verifier){
		String str = "";
		for(String name: names){
			if(verifier.isNotNullOrEmpty(name)){
				str = str.concat(name);
			}
		}
		System.out.println("toString........."+str);
	}
	
	
	private void testStaticMethodReferences(List<String> names){
		Collections.sort(names, SortingByLamda::compare);
		names.forEach(System.out::println);
	}
	
	private void testInstanceMethodReferences(List<String> names){
		Collections.sort(names, this::compareTo);
		names.forEach(System.out::println);
	}
	
	private void testArbitraryInstanceTypeMethodReferences(List<String> names){
		Collections.sort(names, String::compareToIgnoreCase);
		names.forEach(System.out::println);
	}
	
	
	/*
	 * Sorting by Lamda expressions
	 */
	private void sortUsingJava8(List<String> names){   
		Collections.sort(names, (String str1, String str2) -> {return str1.compareTo(str2);});
	    //Collections.sort(names, (str1,str2) -> str1.compareTo(str2));
		//Collections.sort(names, (String str1,String  str2) -> str1.compareTo(str2));
	}
	
	private void sortUsingJava7(List<String> names){   
	      Collections.sort(names, new Comparator<String>() {
	         @Override
	         public int compare(String s1, String s2) {
	            return s1.compareTo(s2);
	         }
	      });
	}
	
	public int compareTo(String s1, String s2) {
         return s1.compareTo(s2);
    }
	
	public static int compare(String s1, String s2) {
        return s1.compareTo(s2);
   }
	
	public static void main(String[] args) {
			  SortingByLamda tester = new SortingByLamda();
		      tester.testLamdaExpressions(20, 4);
		    
		      List<String> names1 = new ArrayList<String>();
		      names1.add("Mahesh ");
		      names1.add("Suresh ");
		      names1.add("Ramesh ");
		      names1.add("Naresh ");
		      names1.add("Kalpesh ");
		      
		      System.out.println("Sort using Java 7 syntax: ");
			  tester.sortUsingJava7(names1);
		      System.out.println(names1);
		    
		      names1.add(2,"Rajesh ");
		      System.out.println("Sort using Java 8 syntax: ");
			  tester.sortUsingJava8(names1);
		      System.out.println(names1);
		      
		      names1.add(2,"Somesh ");
		      System.out.println("By Static method reference: ");
			  tester.testStaticMethodReferences(names1);
			  
			  //names1.add(2,null);
			  names1.add(2,"");
			  names1.add(2,"     ");
			  System.out.println("Filtering Empty or Null Objects");
			  tester.toString(names1,obj -> obj != null && !obj.trim().equals(""));
			  
			 
		   }

	}

