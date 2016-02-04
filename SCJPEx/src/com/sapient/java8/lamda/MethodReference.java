package com.sapient.java8.lamda;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@FunctionalInterface
interface StringOperation {
    String toString(char[] charArray);
}

@FunctionalInterface
interface DynamicArray<T> {
    T[] createArray(int size);
}

public class MethodReference {
	
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
	
	private String testConstructorReferences(){
		//You can assign a constructor reference to any functional interface which has a method compatible with the constructor. 
		StringOperation strOperation = String::new;
		char[] charArray = {'R','A','M'};
		return strOperation.toString(charArray);
	}
	
	private void testConstructorReferencesArray(){

		DynamicArray<String> dynamicStrArray = String[]::new;
		String[] stringArray = dynamicStrArray.createArray(10);
		System.out.println("Length :"+stringArray.length+"  1st element: "+stringArray[1]);
		
		
		DynamicArray<Integer> dynamicIntrArray = Integer[]::new;
		Integer[] intArray = dynamicIntrArray.createArray(20);
		System.out.println("Length :"+intArray.length+"  1st element: "+intArray[1]);
		
	}

	public int compareTo(String s1, String s2) {
         return s1.compareTo(s2);
    }
	
	public static int compare(String s1, String s2) {
        return s1.compareTo(s2);
    }
	
	
	public static List<String>	toList(String namesString,List<String> names) {
        String[] namesArray = namesString.split(",");
        for (String name : namesArray) {
        	names.add(name);
        }
        return names;
	}
	
	public static void main(String[] args) {
			  MethodReference tester = new MethodReference();
		      List<String> names1 = new ArrayList<String>();
		      names1.add("Mahesh ");
		      names1.add("Suresh ");
		      names1.add("Ramesh ");
		      names1.add("Naresh ");
		      names1.add("Kalpesh ");
		      
		      System.out.println("By Static method reference: ");
			  tester.testStaticMethodReferences(names1);
			  
			  names1.add(2,"Rajesh ");
			  System.out.println("By Static method reference: ");
			  tester.testInstanceMethodReferences(names1);
			  
			  names1.add(2,"Somesh ");
			  System.out.println("By Static method reference: ");
			  tester.testArbitraryInstanceTypeMethodReferences(names1);
			 
			  names1.add(5,"Hamesh ");
			  System.out.println("By Constructor Reference : "+tester.testConstructorReferences());
			  
			  System.out.println("By Constructor Reference using array: ");
			  tester.testConstructorReferencesArray();
		   }

	}

