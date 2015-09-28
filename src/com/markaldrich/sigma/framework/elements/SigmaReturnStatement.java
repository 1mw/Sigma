package com.markaldrich.sigma.framework.elements;

public class SigmaReturnStatement implements SigmaStatement {
	public String objectReturning;
	
	public String toString() {
		return "return " + objectReturning + ";\n";
	}
}
