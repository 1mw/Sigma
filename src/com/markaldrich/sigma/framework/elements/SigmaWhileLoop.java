package com.markaldrich.sigma.framework.elements;

import java.util.ArrayList;

/**
 * SigmaWhileLoop is an abstraction of a Java while loop in Sigma.
 */
public class SigmaWhileLoop implements SigmaStatement {
	public String condition;
	public ArrayList<SigmaStatement> statements = new ArrayList<>();
	
	@Override
	public String toString() {
		String toReturn = "while(" + condition + ") {\n";
		
		for(SigmaStatement s : statements) {
			toReturn += s.toString();
		}
		
		toReturn += "}\n";
		return toReturn;
	}
}
