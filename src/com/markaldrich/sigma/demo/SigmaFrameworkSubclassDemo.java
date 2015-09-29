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
			mainMethod.parameters.put("args", "String[]");
			mainMethod.returnType = "void";
			{
				SigmaObject instance = new SigmaObject();
				instance.data = "new Subclass()";
				instance.name = "sub";
				instance.type = "Subclass";
				mainMethod.statements.add(instance);
				
				SigmaMethodCall callPrint = new SigmaMethodCall();
				callPrint.method = "sub.print";
				mainMethod.statements.add(callPrint);
			}
			mainClass.methods.add(mainMethod);
			
			SigmaSubclass subclass = new SigmaSubclass();
			subclass.access = SigmaAccessModifier.PUBLIC;
			subclass.isStatic = true;
			subclass.name = "Subclass";
			{
				SigmaMethod test = new SigmaMethod();
				test.name = "print";
				test.access = SigmaAccessModifier.PUBLIC;
				test.returnType = "void";
				{
					SigmaMethodCall print = new SigmaMethodCall();
					print.method = "System.out.println";
					{
						print.parameters.add("\"Hello world!\"");
					}
					test.statements.add(print);
				}
				subclass.methods.add(test);
			}
			mainClass.subclasses.add(subclass);
		}
		script.mainClass = mainClass;
		
		System.out.println(script.getSource());
	}
}
