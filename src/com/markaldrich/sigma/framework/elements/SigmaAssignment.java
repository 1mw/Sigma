package com.markaldrich.sigma.framework.elements;

/**
 * SigmaAssignment is an abstraction of a Java assignment in a Sigma script.
 * @author maste
 * 
 */
public class SigmaAssignment extends SigmaElement implements SigmaStatement {
	public SigmaObject object;
	public String dataToAssign;
	
	@Override
	public String toString() {
		return object.name + " = " + dataToAssign + ";";
	}
}
