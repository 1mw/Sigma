package com.markaldrich.sigma.framework.elements;

public class SigmaReturnStatement implements SigmaStatement {
	public String nameOfObjectReturning;
	
	public String toString() {
		return "return " + nameOfObjectReturning + ";";
	}
}
