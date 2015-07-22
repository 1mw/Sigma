package com.markaldrich.sigma.framework.elements;

public class SigmaIfElseStatement implements SigmaStatement, SigmaElement {
	public String condition;
	
	public SigmaIfBlock ifTrue = new SigmaIfBlock();
	public SigmaElseBlock ifFalse = new SigmaElseBlock();
	
	@Override
	public String toString() {
		String toReturn = "if(" + condition + ") {";
		
		for(SigmaStatement s : ifTrue.statements) {
			if(s instanceof SigmaObject) {
				toReturn += ((SigmaObject) s).declarationToString();
			} else {
				toReturn += s.toString();
			}
		}
		
		toReturn += "} else {";
		
		for(SigmaStatement s : ifFalse.statements) {
			if(s instanceof SigmaObject) {
				toReturn += ((SigmaObject) s).declarationToString();
			} else {
				toReturn += s.toString();
			}
		}
		
		toReturn += "}";
		
		return toReturn;
	}
}
