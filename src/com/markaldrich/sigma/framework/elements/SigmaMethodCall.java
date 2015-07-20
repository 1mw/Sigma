package com.markaldrich.sigma.framework.elements;

import java.util.ArrayList;

/**
 * SigmaMethodCall is an abstraction of a Java method call in a SigmaScript.
 * @author maste
 *
 */
public class SigmaMethodCall extends SigmaElement implements SigmaStatement {
	public String method;
	public ArrayList<SigmaObject> parameters = new ArrayList<>();
	
	@Override
	public String toString() {
		String toReturn = "";
		
		toReturn += method + "(";
		
		for(SigmaObject o : parameters) {
			toReturn += o.name;
			if(parameters.indexOf(o) == parameters.size() - 1) {
			} else {
				toReturn += ",";
			}
		}
		
		toReturn += ");";
		
		return toReturn;
	}
}
