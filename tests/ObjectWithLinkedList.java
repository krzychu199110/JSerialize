package tests;

import java.util.LinkedList;

public class ObjectWithLinkedList {
	LinkedList<Integer> link = new LinkedList<Integer>();
	
	public ObjectWithLinkedList(){
	int num1 = 2, num2 = 11, num3 = 20;
	link.add(num1);
	link.add(num2);
	link.add(num3);
	}
}
