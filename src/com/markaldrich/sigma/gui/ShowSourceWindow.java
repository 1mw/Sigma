package com.markaldrich.sigma.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;

public class ShowSourceWindow extends JDialog implements ClipboardOwner {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5081814857929301168L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public ShowSourceWindow(final String source) {
		setBounds(MainWindow.frmSigma.getX(), MainWindow.frmSigma.getY(), 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JTextPane textPane = new JTextPane();
			textPane.setEditable(false);
			textPane.setText(source);
			contentPanel.add(textPane);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnCopyToClipboard = new JButton("Copy to clipboard");
				btnCopyToClipboard.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						StringSelection ss = new StringSelection(source);
						Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ShowSourceWindow.this);
					}
				});
				buttonPane.add(btnCopyToClipboard);
			}
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		
		setVisible(true);
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub
		
	}

}
