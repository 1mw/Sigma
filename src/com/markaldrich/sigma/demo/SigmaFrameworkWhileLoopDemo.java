package com.markaldrich.sigma.demo;

import com.markaldrich.sigma.framework.elements.*;

public class SigmaFrameworkWhileLoopDemo {
	public static void main(String[] args) {
		SigmaScript script = new SigmaScript();
		{
			SigmaClass main = new SigmaClass();
			main.name = "Main";
			main.access = SigmaAccessModifier.PUBLIC;
			{
				SigmaMethod mainMethod = new SigmaMethod();
				mainMethod.name = "main";
				mainMethod.access = SigmaAccessModifier.PUBLIC;
				mainMethod.parameters.put("args", "String[]");
				mainMethod.isStatic = true;
				mainMethod.returnType = "void";
				{
					SigmaObject number = new SigmaObject();
					number.name = "number";
					number.type = "int";
					number.data = "0";
					mainMethod.statements.add(number);
					
					SigmaWhileLoop whileLoop = new SigmaWhileLoop();
					whileLoop.condition = "number < 10";
					{
						SigmaMethodCall print = new SigmaMethodCall();
						print.method = "System.out.println";
						print.parameters.add("number");
						whileLoop.statements.add(print);
						
						SigmaAssignment assign = new SigmaAssignment();
						assign.object = "number";
						assign.dataToAssign = "number + 1";
						whileLoop.statements.add(assign);
					}
					mainMethod.statements.add(whileLoop);
				}
				main.methods.add(mainMethod);
			}
			script.mainClass = main;
		}
		
		System.out.println(script.getSource());
	}
}
