package data;

import java.util.ArrayList;

public class Stack<E> {
	
	int maximum = -1;
	public int top = 0;
	ArrayList<E> stack = new ArrayList<E>();
	
	public Stack() {};
	
	public Stack(int maximum) {
		this.maximum = maximum;
	}
	
	public E peek() {
		E e = null;
		if (top > 0) {
			e = stack.get(top-1);
		}
		return e;
	}
	
	public void push(E e) {
		if (top < maximum || maximum == -1) {
			stack.add(e);
			top++;
		}
	}
	
	public E pop() {
		E e = null;
		if (top > 0) {
			top--;
			e = stack.get(top);
			stack.remove(top);
		}
		return e;
	}
	
	public String toString() {
		String output = "";
		
		for (int i = 0; i < top; i++) {
			
			output += stack.get(i).toString();
			if (i+1 != top) {
				output += "\r\n";
			}
		}
		return output;
	}
	
	public String[] toStringArray() {
		String[] output = new String[top];
		
		for (int i = 0; i < top; i++) {
			output[i] = stack.get(i).toString();
		}
		return output;
	}

}
