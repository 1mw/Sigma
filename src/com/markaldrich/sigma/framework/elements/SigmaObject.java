package com.markaldrich.sigma.framework.elements;

/**
 * SigmaObject is an abstraction of a Java object in a Sigma script.
 * @author maste
 *
 */
public class SigmaObject extends SigmaElement implements SigmaStatement {
	public String name;
	public String type;
	public String data;
	
	public String declarationToString() {
		if(name == null) System.err.println("Object has null name!");
		if(type == null) System.err.println("Object has null type!");
		if(data == null) System.err.println("Object has null data!");
		return type + " " + name + " = " + data + ";";
	}
	
	@Override
	public String toString() {
		if(name == null) System.err.println("Object has null name!");
		if(type == null) System.err.println("Object has null type!");
		if(data == null) System.err.println("Object has null data!");
		System.err.println(name + " of type " + type + " just got toString() called on it.");
		return null;
	}
}
