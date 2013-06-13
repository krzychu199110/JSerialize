package tests;

import java.util.HashSet;
import java.util.Set;

public class ObjectWithHashSet {
	Set set = new HashSet<String>();
	
	public ObjectWithHashSet(){
		set.add("Karol");
	}
}