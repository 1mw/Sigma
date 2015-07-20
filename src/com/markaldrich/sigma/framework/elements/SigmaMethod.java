package com.markaldrich.sigma.framework.elements;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * SigmaMethod is an abstraction of a Java method in a Sigma script.
 * @author maste
 *
 */
public class SigmaMethod extends SigmaElement {
	
	public String name;
	public SigmaAccessModifier access;
	public boolean isStatic;
	public String returnType;
	public ArrayList<SigmaStatement> statements = new ArrayList<>();
	
	// Keys are names, values are types
	public HashMap<String, String> parameters = new HashMap<>();
	
	@Override
	public String toString() {
		if(name == null) System.err.println("Method has null name!");
		if(access == null) System.err.println("Method has null access modifier!");
		if(returnType == null) System.err.println("Method has no return type!");
		
		String toReturn = ((access == SigmaAccessModifier.NONE) ? "" : (access.toString().toLowerCase() + " ")) + ((isStatic == true) ? "static " : "")  + returnType + " " + name + "(";
		
		{
			int i = 0;
			for(String s : parameters.keySet()) {
				if(i == parameters.size() - 1) {
					toReturn += parameters.get(s) + " " + s;
				} else {
					toReturn += parameters.get(s) + " " + s + ",";
				}
			}
		}
		
		toReturn += ") {";
		
		for(SigmaStatement s : statements) {
			if(s instanceof SigmaObject) {
				toReturn += ((SigmaObject) s).declarationToString();
			} else {
				toReturn += s.toString();
			}
		}
		
		toReturn += "}";
		
		return toReturn;
	}
}
