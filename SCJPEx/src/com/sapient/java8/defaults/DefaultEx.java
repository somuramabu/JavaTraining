package com.sapient.java8.defaults;

interface Vehicle {
   default void print(){
      System.out.println("I am a vehicle!");
   }
   
   static void blowHorn(){
	      System.out.println("Blowing horn!!!");
   }
}

interface FourWheeler{
   default void print(){
      System.out.println("I am a four wheeler!");
   }
}

public class DefaultEx implements Vehicle,FourWheeler{
	 public void print(){
	      //System.out.println("I am a four wheeler!!!!!");
	      Vehicle.super.print();
	 }
	 public static void main(String[] a){
		 DefaultEx ex  = new DefaultEx();
		 ex.print();
		 Vehicle.blowHorn();
	 }
}
