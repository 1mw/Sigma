package com.markaldrich.sigma.framework.elements;

import java.util.ArrayList;

/**
 * SigmaMethodCall is an abstraction of a Java method call in a SigmaScript.
 * @author maste
 *
 */
public class SigmaMethodCall extends SigmaElement implements SigmaStatement {
	public String method;
	public ArrayList<String> parameters = new ArrayList<>();
	
	@Override
	public String toString() {
		if(method == null) System.err.println("Method call has no method to call!");
		String toReturn = "";
		
		toReturn += method + "(";
		
		for(String o : parameters) {
			toReturn += o;
			if(parameters.indexOf(o) == parameters.size() - 1) {
			} else {
				toReturn += ",";
			}
		}
		
		toReturn += ");";
		
		return toReturn;
	}
	
	public String assignmentCall() {
		if(method == null) System.err.println("Method call has no method to call!");
		String toReturn = "";
		
		toReturn += method + "(";
		
		for(String o : parameters) {
			toReturn += o;
			if(parameters.indexOf(o) == parameters.size() - 1) {
			} else {
				toReturn += ",";
			}
		}
		
		toReturn += ")";
		
		return toReturn;
	}
}
