package com.markaldrich.sigma.demo;

import com.markaldrich.sigma.framework.elements.*;

/**
 * A test of the SigmaFramework.
 * @author maste
 *
 */
public class SigmaFrameworkHelloWorld {
	public static void main(String[] args) {
		SigmaScript script = new SigmaScript();
		
		SigmaClass main = new SigmaClass();
		main.name = "Main";
		main.access = SigmaAccessModifier.PUBLIC;
		
		SigmaGlobalVariable helloWorldString = new SigmaGlobalVariable();
		helloWorldString.name = "helloWorldString";
		helloWorldString.type = "String";
		helloWorldString.data = "\"Hello world\"";
		helloWorldString.access = SigmaAccessModifier.PUBLIC;
		helloWorldString.isStatic = true;
		main.globalVariables.add(helloWorldString);
		
		SigmaMethod mainMethod = new SigmaMethod();
		mainMethod.access = SigmaAccessModifier.PUBLIC;
		mainMethod.returnType = "void";
		mainMethod.isStatic = true;
		mainMethod.parameters.put("args", "String[]");
		mainMethod.name = "main";
		
		SigmaMethodCall s1 = new SigmaMethodCall();
		s1.method = "System.out.println";
		s1.parameters.add(helloWorldString);
		mainMethod.statements.add(s1);
		
		main.methods.add(mainMethod);
		
		script.mainClass = main;
		
		System.out.println(script.getSource());
		
	}
}
