package com.markaldrich.sigma.framework.elements;

import java.util.ArrayList;

public class SigmaScript {
	public SigmaClass mainClass;
	
	public String getSource() {
		return mainClass.toString();
	}
}
