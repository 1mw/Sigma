package com.markaldrich.sigma.framework.elements;

import java.util.ArrayList;

/**
 * SigmaClass is an abstraction of a Java class in a Sigma script.
 * @author maste
 *
 */
public class SigmaClass {
	
	public String name;
	public SigmaAccessModifier access;
	public ArrayList<SigmaGlobalVariable> globalVariables;
	public ArrayList<SigmaMethod> methods;
	
	@Override
	public String toString() {
		String toReturn = "";
		toReturn += (access == SigmaAccessModifier.NONE) ? "" : (access.toString().toLowerCase() + " ");
		toReturn += "class " + name + " {";
		
		for(SigmaGlobalVariable g : globalVariables) {
			toReturn += g.toString();
		}
		
		for(SigmaMethod m : methods) {
			toReturn += m.toString();
		}
		
		toReturn += "}";
		
		return toReturn;
	}
}
