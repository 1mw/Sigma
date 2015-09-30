package com.markaldrich.sigma.demo;

import com.markaldrich.sigma.framework.elements.*;

public class SigmaFrameworkControlFlowDemo {
	public static void main(String[] args) {
		SigmaScript script = new SigmaScript();
		{
			SigmaClass main = new SigmaClass();
			main.name = "Main";
			main.access = SigmaAccessModifier.PUBLIC;
			{
				SigmaGlobalVariable number = new SigmaGlobalVariable();
				number.name = "number";
				number.type = "int";
				number.access = SigmaAccessModifier.PRIVATE;
				number.isStatic = true;
				number.isFinal = true;
				number.data = "10";
				main.globalVariables.add(number);
				
				SigmaMethod mainMethod = new SigmaMethod();
				mainMethod.name = "main";
				mainMethod.access = SigmaAccessModifier.PUBLIC;
				mainMethod.parameters.put("args", "String[]");
				mainMethod.isStatic = true;
				mainMethod.returnType = "void";
				{
					SigmaIfElseStatement isNumberEven = new SigmaIfElseStatement();
					isNumberEven.condition = "number % 2 == 0";
					{
						SigmaIfBlock ifBlock = new SigmaIfBlock();
						{
						    SigmaObject object = new SigmaObject();
						    object.data = "2";
						    object.name = "dag";
						    object.type = "int";
						    ifBlock.statements.add(object);
						    
						    
							SigmaMethodCall print = new SigmaMethodCall();
							print.method = "System.out.println";
							print.parameters.add("\"The number is even!\"");
							ifBlock.statements.add(print);
						}
						isNumberEven.ifTrue = ifBlock;
						
						SigmaElseBlock ifFalse = new SigmaElseBlock();
						{
							SigmaMethodCall print = new SigmaMethodCall();
							print.method = "System.out.println";
							print.parameters.add("\"The number is odd!\"");
							ifFalse.statements.add(print);
						}
						isNumberEven.ifFalse = ifFalse;
					}
					mainMethod.statements.add(isNumberEven);
				}
				main.methods.add(mainMethod);
			}
			script.mainClass = main;
		}
		System.out.println(script.getSource());
	}
}
