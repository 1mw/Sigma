package com.markaldrich.sigma.framework.elements;

/**
 * SigmaObject is an abstraction of a Java object in a Sigma script.
 * @author maste
 *
 */
public class SigmaObject {
	public String name;
	public String type;
	public String dataName;
	public int declarationLine;
	
	public String declarationToString() {
		return name + " = " + dataName + ";";
	}
	
	@Override
	public String toString() {
		System.err.println(name + " of type " + type + " just got toString() called on it.");
		return null;
	}
}
