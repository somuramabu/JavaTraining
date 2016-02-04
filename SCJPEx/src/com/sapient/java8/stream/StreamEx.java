package com.sapient.java8.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamEx {
	
	public static List<Employee> getEmployees()	{
		List<Employee> list = new ArrayList<Employee>();
		Department d1 = new Department();
		d1.setId(1);
		d1.setName("dept1");
		
		Department d2 = new Department();
		d2.setId(1);
		d2.setName("dept2");
		
		Employee e = new Employee();
		e.setId(1);
		e.setName("Emp1");
		e.setDept(d1);
		e.setSal(1000.00);
		list.add(e);
		e = new Employee();
		e.setId(2);
		e.setName("Emp2");
		e.setDept(d1);
		e.setSal(2000.00);
		list.add(e);
		e = new Employee();
		e.setId(3);
		e.setName("Emp3");
		e.setDept(d1);
		e.setSal(3000.00);
		list.add(e);
		e = new Employee();
		e.setId(4);
		e.setName("Emp4");
		e.setDept(d1);
		e.setSal(4000.00);
		list.add(e);
		
		e = new Employee();
		e.setId(1);
		e.setName("Emp1");
		e.setDept(d1);
		e.setSal(100.00);
		list.add(e);
		e = new Employee();
		e.setId(2);
		e.setName("Emp2");
		e.setDept(d2);
		e.setSal(2000.00);
		list.add(e);
		e = new Employee();
		e.setId(3);
		e.setName("Emp3");
		e.setDept(d2);
		e.setSal(3000.00);
		list.add(e);
		e = new Employee();
		e.setId(4);
		e.setName("Emp4");
		e.setDept(d2);
		e.setSal(5000.00);
		list.add(e);
		//list.add(null);
		return list;
	}
	public static void main(String[] args) {
		
		List<Employee> list = StreamEx.getEmployees();
		
		String delimitedNamesStr = list.stream().map(emp->emp.getName()).collect(Collectors.joining(",") );
		System.out.println("All employee names with comma separated"+delimitedNamesStr);
		
		String sortedDelimitedNamesStr = list.stream().map(emp->emp.getName()).sorted().collect(Collectors.joining(",") );
		System.out.println("Sorted employee names with comma separated"+sortedDelimitedNamesStr);
		
		String uniqueDelimitedNamesStr = list.stream().map(emp->emp.getName()).sorted().distinct().collect(Collectors.joining(",") );
		System.out.println("Sorted and unique employee names with comma separated"+uniqueDelimitedNamesStr);
		
		Optional<Double> topSal =list.stream().map(emp->emp.getSal()).sorted().collect(Collectors.maxBy((sal1,sal2)->new Double(sal1).compareTo(new Double(sal2))));
		System.out.println("Employee top sal: "+topSal.get());
		
		Optional<Employee> employee =list.stream().collect(Collectors.maxBy((emp1,emp2)->new Double(emp1.getSal()).compareTo(new Double(emp2.getSal()))));
		System.out.println("Top salaried employee  name: "+employee.get().getName()+ "  Sal:  "+employee.get().getSal());
		
		employee =list.stream().collect(Collectors.minBy((emp1,emp2)->new Double(emp1.getSal()).compareTo(new Double(emp2.getSal()))));
		System.out.println("Least salaried employee  name: "+employee.get().getName()+ "  Sal:  "+employee.get().getSal());
		
		Map<String, List<Employee>> groupByNameList =list.stream().collect(Collectors.groupingBy(Employee::getName));
		groupByNameList.forEach((name,empList) -> System.out.println("Name: "+name+" average sal:"+empList.stream().map(emp->emp.getSal()).collect(Collectors.averagingDouble(sal->sal))));
		
		Map<Department, List<Employee>> groupByDeptList =list.stream().collect(Collectors.groupingBy(Employee::getDept));
		groupByDeptList.forEach((dept,empList) -> System.out.println("Dept Name: "+dept.getName()+" average sal:"+empList.stream().map(emp->emp.getSal()).collect(Collectors.averagingDouble(sal->sal))));
		
		Map<Department, List<Employee>> filteredGroupByDeptList =list.stream().filter(emp->emp.getSal()<3000).collect(Collectors.groupingBy(Employee::getDept));
		filteredGroupByDeptList.forEach((dept,empList) -> System.out.println("Dept Name: "+dept.getName()+" average sal:"+empList.stream().map(emp->emp.getSal()).collect(Collectors.averagingDouble(sal->sal))));
	}

}
