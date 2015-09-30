package com.markaldrich.sigma.gui;

import javax.swing.JFrame;

import com.markaldrich.sigma.framework.elements.SigmaElseBlock;
import com.markaldrich.sigma.framework.elements.SigmaIfBlock;
import com.markaldrich.sigma.framework.elements.SigmaMethod;

public class DataChooserWindow {

	private JFrame frame;
	private SigmaMethod methodContext;
	private SigmaIfBlock ifBlockContext;
	private SigmaElseBlock elseBlockContext;

	/**
	 * Create the application.
	 */
	public DataChooserWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

}
