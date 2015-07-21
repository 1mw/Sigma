package com.markaldrich.sigma.framework.elements;

import java.util.ArrayList;

public class SigmaIfElseStatement extends SigmaElement implements SigmaStatement {
	public String condition;
	
	public ArrayList<SigmaStatement> ifTrue = new ArrayList<>();
	public ArrayList<SigmaStatement> ifFalse = new ArrayList<>();
	
	@Override
	public String toString() {
		String toReturn = "if(" + condition + ") {";
		
		for(SigmaStatement s : ifTrue) {
			toReturn += s.toString();
		}
		
		toReturn += "} else {";
		
		for(SigmaStatement s : ifFalse) {
			toReturn += s.toString();
		}
		
		toReturn += "}";
		
		return toReturn;
	}
}
