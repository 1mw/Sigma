package com.markaldrich.sigma.framework.elements;

public class SigmaIfElseStatement implements SigmaStatement, SigmaElement {
	public String condition;
	
	public SigmaIfBlock ifTrue = new SigmaIfBlock();
	public SigmaElseBlock ifFalse = new SigmaElseBlock();
	
	@Override
	public String toString() {
		String toReturn = "if(" + condition + ") {\n";
		
		for(SigmaStatement s : ifTrue.statements) {
			if(s instanceof SigmaObject) {
				toReturn += ((SigmaObject) s).declarationToString();
			} else {
				toReturn += s.toString();
			}
		}
		
		toReturn += "} else {\n";
		
		for(SigmaStatement s : ifFalse.statements) {
			if(s instanceof SigmaObject) {
				toReturn += ((SigmaObject) s).declarationToString();
			} else {
				toReturn += s.toString();
			}
		}
		
		toReturn += "}\n";
		
		return toReturn;
	}
}
