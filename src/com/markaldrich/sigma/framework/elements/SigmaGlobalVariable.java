package com.markaldrich.sigma.framework.elements;

public class SigmaGlobalVariable extends SigmaObject {
	public SigmaAccessModifier access;
	public boolean isStatic;
	
	public String declarationToString() {
		return (access == SigmaAccessModifier.NONE) ? "" : (access.toString().toLowerCase() + " ") + ((isStatic) ? "static " : "") + type + " " + name + " = " + data + ";";
	}
}
