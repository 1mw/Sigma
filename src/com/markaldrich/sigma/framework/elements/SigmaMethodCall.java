package com.markaldrich.sigma.framework.elements;

import java.util.ArrayList;

/**
 * SigmaMethodCall is an abstraction of a Java method call in a SigmaScript.
 * @author maste
 *
 */
public class SigmaMethodCall implements SigmaStatement {
	public SigmaMethod method;
	public ArrayList<SigmaObject> parameters;
	
	@Override
	public String toString() {
		String toReturn = "";
		
		toReturn += method + "(";
		
		for(SigmaObject o : parameters) {
			toReturn += o.name + ",";
		}
		
		toReturn += ");";
		
		return toReturn;
	}
}
