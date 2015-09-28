package com.markaldrich.sigma.framework.elements;

public class SigmaGlobalVariable extends SigmaObject {
	public SigmaAccessModifier access;
	public boolean isStatic;
	
	public String declarationToString() {
		if(access == null) System.err.println("Class has null access modifier!");
		return (access == SigmaAccessModifier.NONE) ? "" : 
			(access.toString().toLowerCase() + " ") + 
			((isStatic) ? "static " : "") + ((isFinal == true) ? 
					"final " : "") + type + " " + name + " = " + data + ";\n";
	}
}
