package com.markaldrich.sigma.gui;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.markaldrich.sigma.framework.elements.SigmaImport;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class ImportsWindow {

	private JFrame frame;

	private JTextArea txtImports;
	private JTextField txtName;

	/**
	 * Create the application.
	 */
	public ImportsWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ImportsWindow.class.getResource("/res/logo.png")));
		frame.setResizable(false);
		frame.setBounds(100, 100, 600, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		txtImports = new JTextArea();
		txtImports.setEditable(false);
		txtImports.setText("Imports");
		txtImports.setBounds(10, 45, 574, 312);
		JScrollPane tiScroll = new JScrollPane(txtImports);
		tiScroll.setBounds(txtImports.getBounds());
		frame.getContentPane().add(tiScroll);

		JButton btnAddImport = new JButton("Add import");
		btnAddImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String im = txtName.getText();

				for(SigmaImport i : MainWindow.script.imports) {
					if(i.toImport.equals(im)) {
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(frame, "Cannot add import because import already exists.",
								"Error", JOptionPane.WARNING_MESSAGE, new ImageIcon(Toolkit.getDefaultToolkit()
										.getImage(MainWindow.class.getResource("/res/logo.png"))));
						return;
					}
				}

				SigmaImport newImport = new SigmaImport();
				newImport.toImport = im;
				MainWindow.script.imports.add(newImport);

				updateInterface();
			}
		});
		btnAddImport.setBounds(10, 437, 204, 23);
		frame.getContentPane().add(btnAddImport);

		JButton btnRemoveImport = new JButton("Remove import");
		btnRemoveImport.setBounds(380, 437, 204, 23);
		btnRemoveImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String im = txtName.getText();
				SigmaImport toRemove = null;
				for(SigmaImport i : MainWindow.script.imports) {
					if(i.toImport.equals(im)) {
						toRemove = i;
					}
				}

				if(!MainWindow.script.imports.remove(toRemove)) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(frame, "Cannot remove import because it does not exist.", "Error",
							JOptionPane.WARNING_MESSAGE, new ImageIcon(Toolkit.getDefaultToolkit()
									.getImage(MainWindow.class.getResource("/res/logo.png"))));
				}

				updateInterface();
			}
		});
		frame.getContentPane().add(btnRemoveImport);

		JLabel lblCurrentImports = new JLabel("Current Imports");
		lblCurrentImports.setBounds(10, 11, 574, 23);
		frame.getContentPane().add(lblCurrentImports);

		txtName = new JTextField();
		txtName.setBounds(10, 406, 574, 20);
		frame.getContentPane().add(txtName);
		txtName.setColumns(10);

		JLabel lblFullyQualifiedName = new JLabel("Fully qualified name");
		lblFullyQualifiedName.setBounds(10, 381, 574, 14);
		frame.getContentPane().add(lblFullyQualifiedName);

		updateInterface();

		frame.setVisible(true);
	}

	private void updateInterface() {
		String importsList = "";

		for(SigmaImport im : MainWindow.script.imports) {
			importsList += im.toImport + "\n";
		}

		txtImports.setText(importsList);
	}
}
