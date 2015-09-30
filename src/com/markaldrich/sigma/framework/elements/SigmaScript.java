package com.markaldrich.sigma.framework.elements;

import java.util.ArrayList;

public class SigmaScript implements SigmaElement {
	public SigmaClass mainClass;
	public ArrayList<SigmaImport> imports = new ArrayList<>();
	
	public String getSource() {
		if(mainClass == null) System.err.println("Script has no main class!");
		String toReturn = "";
		
		for(SigmaImport s : imports) {
			toReturn += "import " + s.toImport + ";\n";
		}
		
		toReturn += mainClass.toString();
		
		return toReturn;
	}
	
	@Override
	public String toString() {
		return getSource();
	}
}
