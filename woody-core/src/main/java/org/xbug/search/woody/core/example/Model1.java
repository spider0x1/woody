package org.xbug.search.woody.core.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xbug.search.woody.core.model.Model;

public class Model1 extends Model {

	@Override
	public boolean isValid() {
		return true;
	}
	
	public String name;

	public int age;

	public boolean gender;

	public String[] arr1;

	public int[] arr2;

	public List<String> list1;

	public Set<Integer> set2;

	public Map<String, Integer> map1;


	public Model1() {
		name = "name";
		age = 10;
		gender = true;
		arr1 = new String[] { "1", "2" };
		arr2 = new int[] { 1, 2 };
		list1 = new ArrayList<String>();
		list1.add("L1");
		list1.add("L2");
		
		set2 = new HashSet<Integer>();
		set2.add(1);
		set2.add(2);
		
		map1 = new HashMap<String, Integer>();
		map1.put("One", 1);
		map1.put("Two", 2);
	}
	
	

	
	public static void main(String[] args) {
		setFormat(true);
		System.out.println(new Model1().toJson());
	}
}
