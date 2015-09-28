package com.markaldrich.sigma.framework.elements;

/**
 * SigmaAssignment is an abstraction of a Java assignment in a Sigma script.
 * @author maste
 * 
 */
public class SigmaAssignment implements SigmaElement, SigmaStatement {
	public String object;
	public String dataToAssign;
	
	@Override
	public String toString() {
		return object + " = " + dataToAssign + ";\n";
	}
}
