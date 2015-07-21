package com.markaldrich.sigma.gui;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.WindowConstants;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.markaldrich.sigma.framework.elements.*;
import javax.swing.JCheckBox;

public class NewWindow {

	private JFrame frame;

	public static final String[] types = {
		"Variable",
		"Method",
		"Statement",
	};
	public static final String[] defaultTypes = {
		"int",
		"String",
		"double",
		"float",
		"long",
		"char",
	};
	
	private JTextField txtGlobalVariableName;
	private JTextField txtGlobalVariableValue;
	
	/**
	 * Create the application.
	 */
	public NewWindow() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.requestFocus();
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 444, 271);
		
		
		
		frame.getContentPane().add(tabbedPane);
		
		JPanel newVariable = new JPanel();
		tabbedPane.addTab("Variable", null, newVariable, null);
		newVariable.setLayout(null);
		
		JPanel newMethod = new JPanel();
		tabbedPane.addTab("Method", null, newMethod, null);
		newMethod.setLayout(null);
		
		JPanel newGlobalVariable = new JPanel();
		tabbedPane.addTab("Global Variable", null, newGlobalVariable, null);
		newGlobalVariable.setLayout(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(12, 13, 55, 16);
		newGlobalVariable.add(lblName);
		
		txtGlobalVariableName = new JTextField();
		txtGlobalVariableName.setBounds(79, 11, 114, 20);
		newGlobalVariable.add(txtGlobalVariableName);
		txtGlobalVariableName.setColumns(10);
		
		JLabel lblValue = new JLabel("Value:");
		lblValue.setBounds(12, 42, 55, 16);
		newGlobalVariable.add(lblValue);
		
		txtGlobalVariableValue = new JTextField();
		txtGlobalVariableValue.setBounds(79, 40, 114, 20);
		newGlobalVariable.add(txtGlobalVariableValue);
		txtGlobalVariableValue.setColumns(10);
		
		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(12, 70, 55, 16);
		newGlobalVariable.add(lblType);
		
		JCheckBox checkStatic = new JCheckBox("Static");
		checkStatic.setBounds(201, 38, 112, 24);
		newGlobalVariable.add(checkStatic);
		

		String[] types = new String[SigmaAccessModifier.values().length];
		{
			int i = 0;
			for(SigmaAccessModifier a : SigmaAccessModifier.values()) {
				types[i] = a.toString().toLowerCase();
				i++;
			}
		}
		JComboBox accessModifierBox = new JComboBox(types);
		accessModifierBox.setBounds(205, 11, 222, 20);
		newGlobalVariable.add(accessModifierBox);
		
		
		JComboBox comboType = new JComboBox(defaultTypes);
		comboType.setEditable(true);
		comboType.setBounds(79, 70, 114, 20);
		newGlobalVariable.add(comboType);

		JCheckBox checkFinal = new JCheckBox("Final");
		checkFinal.setBounds(201, 66, 112, 24);
		newGlobalVariable.add(checkFinal);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SigmaGlobalVariable g = new SigmaGlobalVariable();
				g.name = txtGlobalVariableName.getText();
				g.type = comboType.getSelectedItem().toString();
				g.data = txtGlobalVariableValue.getText();
				switch(accessModifierBox.getSelectedItem().toString()) {
				case "public":
					g.access = SigmaAccessModifier.PUBLIC;
					break;
				case "private":
					g.access = SigmaAccessModifier.PRIVATE;
					break;
				case "protected":
					g.access = SigmaAccessModifier.PROTECTED;
					break;
				case "none":
					g.access = SigmaAccessModifier.NONE;
					break;
				default:
					g.access = SigmaAccessModifier.NONE;
					break;
				}
				g.isStatic = checkStatic.isSelected();
				g.isFinal = checkFinal.isSelected();
				
				MainWindow.script.mainClass.globalVariables.add(g);
				
				frame.dispose();
				
				MainWindow.updateInterface();
			}
		});
		btnOk.setBounds(329, 204, 98, 26);
		newGlobalVariable.add(btnOk);
		
	}
}
