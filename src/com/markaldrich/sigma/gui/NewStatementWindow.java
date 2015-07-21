package com.markaldrich.sigma.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.markaldrich.sigma.framework.elements.SigmaAssignment;
import com.markaldrich.sigma.framework.elements.SigmaIfElseStatement;
import com.markaldrich.sigma.framework.elements.SigmaMethod;
import com.markaldrich.sigma.framework.elements.SigmaMethodCall;
import com.markaldrich.sigma.framework.elements.SigmaObject;
import com.markaldrich.sigma.framework.elements.SigmaReturnStatement;

public class NewStatementWindow {

	private JFrame frame;
	public SigmaMethod methodToAddTo;
	public int index = -1;
	private JTextField assignVariable;
	private JTextField assignValue;
	private JTextField returnValue;
	private JTextField variableName;
	private JTextField variableValue;
	private JTextField methodName;
	private JTextField methodArguments;
	private JTextField ifThenElseIf;
	private JTextField ifThenElseIs;

	/**
	 * Create the application.
	 */
	/**
	 * @wbp.parser.constructor
	 */
	public NewStatementWindow(SigmaMethod methodToAddTo, int index) {
		this.methodToAddTo = methodToAddTo;
		this.index = index;
		initialize();
		frame.setVisible(true);
	}
	
	public NewStatementWindow(SigmaMethod methodToAddTo) {
		this.methodToAddTo = methodToAddTo;
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel variablePanel = new JPanel();
		tabbedPane.addTab("Variable", null, variablePanel, null);
		variablePanel.setLayout(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(12, 12, 55, 16);
		variablePanel.add(lblName);
		
		JLabel lblValue_1 = new JLabel("Value:");
		lblValue_1.setBounds(12, 40, 55, 16);
		variablePanel.add(lblValue_1);
		
		JCheckBox variableFinal = new JCheckBox("Final");
		variableFinal.setBounds(85, 96, 112, 24);
		variablePanel.add(variableFinal);
		
		variableName = new JTextField();
		variableName.setBounds(85, 10, 114, 20);
		variablePanel.add(variableName);
		variableName.setColumns(10);
		
		variableValue = new JTextField();
		variableValue.setBounds(85, 38, 114, 20);
		variablePanel.add(variableValue);
		variableValue.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnNewButton_1.setBounds(219, 205, 98, 26);
		variablePanel.add(btnNewButton_1);
		
		JPanel ifThenElsePanel = new JPanel();
		tabbedPane.addTab("If, Then, Else", null, ifThenElsePanel, null);
		ifThenElsePanel.setLayout(null);
		
		JLabel lblIf = new JLabel("If");
		lblIf.setBounds(12, 12, 20, 16);
		ifThenElsePanel.add(lblIf);
		
		ifThenElseIf = new JTextField();
		ifThenElseIf.setBounds(45, 10, 114, 20);
		ifThenElsePanel.add(ifThenElseIf);
		ifThenElseIf.setColumns(10);
		
		JLabel lblIs = new JLabel("is");
		lblIs.setBounds(177, 12, 20, 16);
		ifThenElsePanel.add(lblIs);
		
		ifThenElseIs = new JTextField();
		ifThenElseIs.setBounds(215, 10, 114, 20);
		ifThenElsePanel.add(ifThenElseIs);
		ifThenElseIs.setColumns(10);
		
		JButton btnOk_3 = new JButton("OK");
		btnOk_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SigmaIfElseStatement s = new SigmaIfElseStatement();
				s.condition = ifThenElseIf.getText() + " == " + ifThenElseIs.getText();
				
				int indexOfMethod = MainWindow.script.mainClass.methods.indexOf(methodToAddTo);
				SigmaMethod temp = MainWindow.script.mainClass.methods.get(indexOfMethod);
				if(index == -1) {
					temp.statements.add(s);
				} else {
					temp.statements.add(index, s);
				}
				MainWindow.script.mainClass.methods.set(indexOfMethod, temp);
				frame.dispose();
				MainWindow.updateInterface();
			}
		});
		btnOk_3.setBounds(329, 205, 98, 26);
		ifThenElsePanel.add(btnOk_3);
		
		JButton btnCancel_3 = new JButton("Cancel");
		btnCancel_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnCancel_3.setBounds(219, 205, 98, 26);
		ifThenElsePanel.add(btnCancel_3);
		
		JPanel methodCallPanel = new JPanel();
		tabbedPane.addTab("Method Call", null, methodCallPanel, null);
		methodCallPanel.setLayout(null);
		
		JLabel lblMethod = new JLabel("Method:");
		lblMethod.setBounds(12, 12, 79, 16);
		methodCallPanel.add(lblMethod);
		
		methodName = new JTextField();
		methodName.setBounds(109, 10, 114, 20);
		methodCallPanel.add(methodName);
		methodName.setColumns(10);
		
		JLabel lblArgument = new JLabel("Arguments:");
		lblArgument.setBounds(12, 40, 79, 16);
		methodCallPanel.add(lblArgument);
		
		methodArguments = new JTextField();
		methodArguments.setBounds(109, 38, 318, 20);
		methodCallPanel.add(methodArguments);
		new NewWindow.GhostText(methodArguments, "Ex: 5,\"String\",true");
		methodArguments.setColumns(10);
		
		JButton btnOk_2 = new JButton("OK");
		btnOk_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SigmaMethodCall c = new SigmaMethodCall();
				c.method = methodName.getText();
				if(methodArguments.getText().contains("Ex: 5,\"String\",true")) {} else {
					for(String s : methodArguments.getText().split(",")) {
						c.parameters.add(s);
					}
				}
				
				int indexOfMethod = MainWindow.script.mainClass.methods.indexOf(methodToAddTo);
				SigmaMethod temp = MainWindow.script.mainClass.methods.get(indexOfMethod);
				if(index == -1) {
					temp.statements.add(c);
				} else {
					temp.statements.add(index, c);
				}
				MainWindow.script.mainClass.methods.set(indexOfMethod, temp);
				frame.dispose();
				MainWindow.updateInterface();
				
			}
		});
		btnOk_2.setBounds(329, 205, 98, 26);
		methodCallPanel.add(btnOk_2);
		
		JButton btnCancel_2 = new JButton("Cancel");
		btnCancel_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnCancel_2.setBounds(219, 205, 98, 26);
		methodCallPanel.add(btnCancel_2);
		
		JPanel assignmentPanel = new JPanel();
		tabbedPane.addTab("Assignment", null, assignmentPanel, null);
		assignmentPanel.setLayout(null);
		
		JLabel lblVariable = new JLabel("Variable:");
		lblVariable.setBounds(10, 11, 76, 14);
		assignmentPanel.add(lblVariable);
		
		JLabel lblValue = new JLabel("Value:");
		lblValue.setBounds(10, 39, 76, 14);
		assignmentPanel.add(lblValue);
		
		assignVariable = new JTextField();
		assignVariable.setBounds(96, 11, 138, 20);
		assignmentPanel.add(assignVariable);
		assignVariable.setColumns(10);
		
		assignValue = new JTextField();
		assignValue.setBounds(96, 36, 138, 20);
		assignmentPanel.add(assignValue);
		assignValue.setColumns(10);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnCancel.setBounds(219, 205, 98, 26);
		assignmentPanel.add(btnCancel);
		
		JPanel returnPanel = new JPanel();
		tabbedPane.addTab("Return", null, returnPanel, null);
		returnPanel.setLayout(null);
		
		JLabel lblValueToReturn = new JLabel("Value to return:");
		lblValueToReturn.setBounds(12, 12, 119, 16);
		returnPanel.add(lblValueToReturn);
		returnValue = new JTextField();
		returnValue.setBounds(149, 10, 114, 20);
		returnPanel.add(returnValue);
		returnValue.setColumns(10);
		
		JButton btnOk_1 = new JButton("OK");
		btnOk_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SigmaReturnStatement r = new SigmaReturnStatement();
				r.objectReturning = returnValue.getText();
				
				int indexOfMethod = MainWindow.script.mainClass.methods.indexOf(methodToAddTo);
				SigmaMethod temp = MainWindow.script.mainClass.methods.get(indexOfMethod);
				if(index == -1) {
					temp.statements.add(r);
				} else {
					temp.statements.add(index, r);
				}
				MainWindow.script.mainClass.methods.set(indexOfMethod, temp);
				frame.dispose();
				MainWindow.updateInterface();
			}
		});
		btnOk_1.setBounds(329, 205, 98, 26);
		returnPanel.add(btnOk_1);
		
		JButton btnCancel_1 = new JButton("Cancel");
		btnCancel_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnCancel_1.setBounds(219, 205, 98, 26);
		returnPanel.add(btnCancel_1);

		JComboBox variableType = new JComboBox(NewWindow.defaultTypes);
		variableType.setEditable(true);
		variableType.setBounds(85, 68, 114, 20);
		variablePanel.add(variableType);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SigmaAssignment a = new SigmaAssignment();
				a.object = assignVariable.getText();
				a.dataToAssign = assignValue.getText();
				
				int indexOfMethod = MainWindow.script.mainClass.methods.indexOf(methodToAddTo);
				SigmaMethod temp = MainWindow.script.mainClass.methods.get(indexOfMethod);
				if(index == -1) {
					temp.statements.add(a);
				} else {
					temp.statements.add(index, a);
				}
				MainWindow.script.mainClass.methods.set(indexOfMethod, temp);
				frame.dispose();
				MainWindow.updateInterface();
			}
		});
		btnOk.setBounds(329, 205, 98, 26);
		assignmentPanel.add(btnOk);

		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SigmaObject v = new SigmaObject();
				v.name = variableName.getText();
				v.type = variableType.getSelectedItem().toString();
				v.data = variableValue.getText();
				v.isFinal = variableFinal.isSelected();
				
				int indexOfMethod = MainWindow.script.mainClass.methods.indexOf(methodToAddTo);
				SigmaMethod temp = MainWindow.script.mainClass.methods.get(indexOfMethod);
				if(index == -1) {
					temp.statements.add(v);
				} else {
					temp.statements.add(index, v);
				}
				MainWindow.script.mainClass.methods.set(indexOfMethod, temp);
				frame.dispose();
				MainWindow.updateInterface();
			}
		});
		btnNewButton.setBounds(329, 205, 98, 26);
		variablePanel.add(btnNewButton);
	}
}
