package com.markaldrich.sigma.framework.elements;

import java.util.ArrayList;

/**
 * SigmaMethod is an abstraction of a Java method in a Sigma script.
 * @author maste
 *
 */
public class SigmaMethod extends SigmaElement {
	
	public String name;
	public SigmaAccessModifier access;
	public String returnType;
	public ArrayList<SigmaStatement> statements;
	public ArrayList<String> parameterTypesToTake;
	
	@Override
	public String toString() {
		String toReturn = (access == SigmaAccessModifier.NONE) ? "" : (access.toString().toLowerCase() + " ") + returnType + " " + name + "(";
		
		for(String s : parameterTypesToTake) {
			toReturn += s + ",";
		}
		
		toReturn += ") {";
		
		for(SigmaStatement s : statements) {
			toReturn += s.toString();
		}
		
		toReturn += "}";
		
		return toReturn;
	}
}
