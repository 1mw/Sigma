package com.markaldrich.sigma.framework.elements;

import java.util.ArrayList;

public class SigmaScript {
	public SigmaClass mainClass;
	
	public String getSource() {
		if(mainClass == null) System.err.println("Script has no main class!");
		return mainClass.toString();
	}
}
