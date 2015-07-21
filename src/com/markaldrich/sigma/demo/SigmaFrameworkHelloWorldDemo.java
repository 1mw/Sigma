package com.markaldrich.sigma.demo;

import com.markaldrich.sigma.framework.elements.*;

/**
 * A demo of the SigmaFramework.
 * @author maste
 *
 */
public class SigmaFrameworkHelloWorldDemo {
	public static void main(String[] args) {
		// A group of brackets denotes that anything done inside them belongs to the object above them.
		// This is done to maintain neat-ish indentation.
		
		// Create a new SigmaScript.
		SigmaScript script = new SigmaScript();
		{
			// Create a new method, call it main, set its attributes
			SigmaClass main = new SigmaClass();
			main.name = "Main";
			main.access = SigmaAccessModifier.PUBLIC;
			{
				// Create a new global variable, set its name, type, etc. and add it to the main method
				SigmaGlobalVariable helloWorldString = new SigmaGlobalVariable();
				helloWorldString.name = "helloWorldString";
				helloWorldString.type = "String";
				helloWorldString.data = "\"Hello world\"";
				helloWorldString.access = SigmaAccessModifier.PUBLIC;
				helloWorldString.isStatic = true;
				main.globalVariables.add(helloWorldString);
				
				// Create a new method
				SigmaMethod mainMethod = new SigmaMethod();
				mainMethod.access = SigmaAccessModifier.PUBLIC;
				mainMethod.returnType = "void";
				mainMethod.isStatic = true;
				mainMethod.parameters.put("args", "String[]");
				mainMethod.name = "main";
				{
					// Call a method
					SigmaMethodCall s1 = new SigmaMethodCall();
					s1.method = "System.out.println";
					s1.parameters.add("helloWorldString");
					mainMethod.statements.add(s1);
				}
				// Add mainMethod to the class
				main.methods.add(mainMethod);
			}
			// Set the "Main" class as the main class in the script
			script.mainClass = main;
		}
		// Print the generated source code
		System.out.println(script.getSource());
		
	}
}
