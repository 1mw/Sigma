package com.markaldrich.sigma.demo;

import com.markaldrich.sigma.framework.elements.*;

public class SigmaFrameworkSquareRootDemo {
	public static void main(String[] args) {
		
		SigmaScript script = new SigmaScript();
		{
			SigmaClass main = new SigmaClass();
			main.name = "Main";
			main.access = SigmaAccessModifier.PUBLIC;
			{
				SigmaGlobalVariable numberToSquare = new SigmaGlobalVariable();
				numberToSquare.name = "numberToSquare";
				numberToSquare.access = SigmaAccessModifier.PRIVATE;
				numberToSquare.isStatic = true;
				numberToSquare.type = "int";
				numberToSquare.data = "5";
				main.globalVariables.add(numberToSquare);
				
				
				SigmaMethod square = new SigmaMethod();
				square.returnType = "int";
				square.name = "square";
				square.parameters.put("i", "int");
				square.access = SigmaAccessModifier.PRIVATE;
				square.isStatic = true;
				{
					SigmaReturnStatement returnStatement = new SigmaReturnStatement();
					returnStatement.objectReturning = "i*i";
					square.statements.add(returnStatement);
				}
				main.methods.add(square);
				
				SigmaMethod mainMethod = new SigmaMethod();
				mainMethod.name = "main";
				mainMethod.access = SigmaAccessModifier.PUBLIC;
				mainMethod.isStatic = true;
				mainMethod.returnType = "void";
				mainMethod.parameters.put("args", "String[]");
				{
					SigmaMethodCall getSquaredNumber = new SigmaMethodCall();
					getSquaredNumber.method = "square";
					getSquaredNumber.parameters.add(numberToSquare);
					// Purposely not added to main method
					
					SigmaObject squaredNumber = new SigmaObject();
					squaredNumber.name = "squaredNumber";
					squaredNumber.type = "int";
					// Gets source to call the method in an assignment context
					squaredNumber.data = getSquaredNumber.assignmentCall();
					mainMethod.statements.add(squaredNumber);
					
					SigmaMethodCall printNumber = new SigmaMethodCall();
					printNumber.method = "System.out.println";
					printNumber.parameters.add(squaredNumber);
					mainMethod.statements.add(printNumber);
				}
				main.methods.add(mainMethod);
			}
			script.mainClass = main;
		}
		System.out.println(script.getSource());
		
	}
}
