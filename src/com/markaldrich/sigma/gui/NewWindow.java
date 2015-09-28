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
import javax.swing.JSeparator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NewWindow {

	private JFrame frmNewClassLevel;

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
	private JTextField txtNameMethod;
	private JTextField txtParameterTypeMethod;
	private JTextField txtParameterNameMethod;
	private JTextField importName;
	
	/**
	 * Create the application.
	 */
	public NewWindow() {
		initialize();
		frmNewClassLevel.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNewClassLevel = new JFrame();
		frmNewClassLevel.setIconImage(Toolkit.getDefaultToolkit().getImage(NewWindow.class.getResource("/res/logo.png")));
		frmNewClassLevel.setTitle("New Class Level Item");
		frmNewClassLevel.setBounds(100, 100, 450, 300);
		frmNewClassLevel.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frmNewClassLevel.requestFocus();
		frmNewClassLevel.setResizable(false);
		frmNewClassLevel.setAlwaysOnTop(true);
		frmNewClassLevel.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 444, 271);
		
		
		
		frmNewClassLevel.getContentPane().add(tabbedPane);
		
		JPanel newMethod = new JPanel();
		tabbedPane.addTab("Method", null, newMethod, null);
		newMethod.setLayout(null);
		
		JButton btnCancel_1 = new JButton("Cancel");
		btnCancel_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmNewClassLevel.dispose();
			}
		});
		btnCancel_1.setBounds(219, 205, 98, 26);
		newMethod.add(btnCancel_1);
		
		JLabel lblName_1 = new JLabel("Name:");
		lblName_1.setBounds(12, 12, 125, 16);
		newMethod.add(lblName_1);
		
		txtNameMethod = new JTextField();
		txtNameMethod.setBounds(155, 12, 190, 20);
		newMethod.add(txtNameMethod);
		txtNameMethod.setColumns(10);
		
		JLabel lblReturnType = new JLabel("Return type:");
		lblReturnType.setBounds(12, 40, 125, 16);
		newMethod.add(lblReturnType);
		
		JComboBox comboReturn = new JComboBox(defaultTypes);
		comboReturn.setEditable(true);
		comboReturn.setBounds(155, 40, 190, 20);
		newMethod.add(comboReturn);
		
		JCheckBox checkStaticMethod = new JCheckBox("Static");
		checkStaticMethod.setBounds(12, 68, 125, 24);
		newMethod.add(checkStaticMethod);
		
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
		
		JCheckBox checkStaticGlobal = new JCheckBox("Static");
		checkStaticGlobal.setBounds(201, 38, 112, 24);
		newGlobalVariable.add(checkStaticGlobal);
		

		String[] types = new String[SigmaAccessModifier.values().length];
		{
			int i = 0;
			for(SigmaAccessModifier a : SigmaAccessModifier.values()) {
				types[i] = a.toString().toLowerCase();
				i++;
			}
		}
		JComboBox accessModifierBoxGlobal = new JComboBox(types);
		accessModifierBoxGlobal.setBounds(205, 11, 222, 20);
		newGlobalVariable.add(accessModifierBoxGlobal);
		
		JComboBox accessModifierBoxMethod = new JComboBox(types);
		accessModifierBoxMethod.setBounds(155, 70, 190, 20);
		newMethod.add(accessModifierBoxMethod);
		
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
				switch(accessModifierBoxGlobal.getSelectedItem().toString()) {
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
				g.isStatic = checkStaticGlobal.isSelected();
				g.isFinal = checkFinal.isSelected();
				
				MainWindow.script.mainClass.globalVariables.add(g);
				
				frmNewClassLevel.dispose();
				
				MainWindow.updateInterface();
			}
		});
		btnOk.setBounds(329, 205, 98, 26);
		newGlobalVariable.add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmNewClassLevel.dispose();
			}
		});
		btnCancel.setBounds(219, 205, 98, 26);
		newGlobalVariable.add(btnCancel);
		
		JButton btnOk_1 = new JButton("OK");
		btnOk_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SigmaMethod m = new SigmaMethod();
				m.name = txtNameMethod.getText();
				m.returnType = comboReturn.getSelectedItem().toString();
				m.isStatic = checkStaticMethod.isSelected();
				if(txtNameMethod.getText().equals("") || txtNameMethod.getText() == null || 
						comboReturn.getSelectedItem().toString().equals("") || comboReturn.getSelectedItem() == null) {
					Toolkit.getDefaultToolkit().beep();
					return;
				}
				
				if(txtParameterTypeMethod.getText().contains("Leave blank for no parameter")) {} else {
					if(txtParameterTypeMethod.getText().contains(" ")) {
						Toolkit.getDefaultToolkit().beep();
						System.err.println("txtParameterTypeMethod contains space");
						return;
					} else {
						m.parameters.put(txtParameterNameMethod.getText(), txtParameterTypeMethod.getText());						
					}
				}
				
				switch(accessModifierBoxMethod.getSelectedItem().toString()) {
				case "public":
					m.access = SigmaAccessModifier.PUBLIC;
					break;
				case "private":
					m.access = SigmaAccessModifier.PRIVATE;
					break;
				case "protected":
					m.access = SigmaAccessModifier.PROTECTED;
					break;
				case "none":
					m.access = SigmaAccessModifier.NONE;
					break;
				default:
					m.access = SigmaAccessModifier.NONE;
					break;
				}
				MainWindow.script.mainClass.methods.add(m);
				frmNewClassLevel.dispose();
				
				MainWindow.updateInterface();
			}
		});
		btnOk_1.setBounds(329, 205, 98, 26);
		newMethod.add(btnOk_1);
		
		JLabel lblParameterType = new JLabel("Parameter type:");
		lblParameterType.setBounds(12, 102, 125, 16);
		newMethod.add(lblParameterType);
		
		txtParameterTypeMethod = new JTextField();
		txtParameterTypeMethod.setBounds(155, 100, 190, 20);
		new GhostText(txtParameterTypeMethod, "Leave blank for no parameter");
		newMethod.add(txtParameterTypeMethod);
		txtParameterTypeMethod.setColumns(10);
		
		JLabel lblParameterName = new JLabel("Parameter name:");
		lblParameterName.setBounds(12, 130, 125, 16);
		newMethod.add(lblParameterName);
		
		txtParameterNameMethod = new JTextField();
		txtParameterNameMethod.setBounds(155, 128, 190, 20);
		newMethod.add(txtParameterNameMethod);
		txtParameterNameMethod.setColumns(10);
		
		JPanel importPanel = new JPanel();
		tabbedPane.addTab("Import", null, importPanel, null);
		importPanel.setLayout(null);
		
		JLabel lblFully = new JLabel("Fully qualified name:");
		lblFully.setBounds(12, 12, 150, 16);
		importPanel.add(lblFully);
		
		importName = new JTextField();
		importName.setBounds(180, 10, 247, 20);
		importPanel.add(importName);
		importName.setColumns(10);
		
		JButton btnOk_2 = new JButton("OK");
		btnOk_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(importName.getText().equals("")) {
					Toolkit.getDefaultToolkit().beep();
					return;
				}
				MainWindow.script.mainClass.imports.add(importName.getText());
				frmNewClassLevel.dispose();
				MainWindow.updateInterface();
			}
		});
		btnOk_2.setBounds(329, 205, 98, 26);
		importPanel.add(btnOk_2);
		
		JButton btnCancel_2 = new JButton("Cancel");
		btnCancel_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmNewClassLevel.dispose();
			}
		});
		btnCancel_2.setBounds(219, 205, 98, 26);
		importPanel.add(btnCancel_2);
		
	}
	
    public static class GhostText implements FocusListener, DocumentListener, PropertyChangeListener {
        private final JTextField textfield;
        private boolean isEmpty;
        private Color ghostColor;
        private Color foregroundColor;
        private final String ghostText;

        protected GhostText(final JTextField textfield, String ghostText) {
            super();
            this.textfield = textfield;
            this.ghostText = ghostText;
            this.ghostColor = Color.LIGHT_GRAY;
            textfield.addFocusListener(this);
            registerListeners();
            updateState();
            if (!this.textfield.hasFocus()) {
                focusLost(null);
            }
        }

        public void delete() {
            unregisterListeners();
            textfield.removeFocusListener(this);
        }

        private void registerListeners() {
            textfield.getDocument().addDocumentListener(this);
            textfield.addPropertyChangeListener("foreground", this);
        }

        private void unregisterListeners() {
            textfield.getDocument().removeDocumentListener(this);
            textfield.removePropertyChangeListener("foreground", this);
        }

        public Color getGhostColor() {
            return ghostColor;
        }

        public void setGhostColor(Color ghostColor) {
            this.ghostColor = ghostColor;
        }

        private void updateState() {
            isEmpty = textfield.getText().length() == 0;
            foregroundColor = textfield.getForeground();
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (isEmpty) {
                unregisterListeners();
                try {
                    textfield.setText("");
                    textfield.setForeground(foregroundColor);
                } finally {
                    registerListeners();
                }
            }

        }

        @Override
        public void focusLost(FocusEvent e) {
            if (isEmpty) {
                unregisterListeners();
                try {
                    textfield.setText(ghostText);
                    textfield.setForeground(ghostColor);
                } finally {
                    registerListeners();
                }
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateState();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateState();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateState();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateState();
        }

    }
}
