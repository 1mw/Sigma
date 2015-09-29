package com.markaldrich.sigma.demo;

import com.markaldrich.sigma.framework.elements.*;

public class SigmaFrameworkSubclassDemo {
	public static void main(String[] args) {
		SigmaScript script = new SigmaScript();
		
		SigmaClass mainClass = new SigmaClass();
		mainClass.access = SigmaAccessModifier.PUBLIC;
		mainClass.name = "Main";
		{
			SigmaMethod mainMethod = new SigmaMethod();
			mainMethod.name = "main";
			mainMethod.access = SigmaAccessModifier.PUBLIC;
			mainMethod.isStatic = true;
			mainMethod.returnType = "void";
			{
				
			}
			mainClass.methods.add(mainMethod);
			
			SigmaSubclass subclass = new SigmaSubclass();
			subclass.access = SigmaAccessModifier.PUBLIC;
			subclass.isStatic = true;
			subclass.name = "Subclass";
			{
				
			}
			mainClass.subclasses.add(subclass);
		}
		script.mainClass = mainClass;
		
		System.out.println(script.getSource());
	}
}
