package com.markaldrich.sigma.gui;

import java.awt.EventQueue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.markaldrich.sigma.framework.elements.*;


public class MainWindow implements TreeSelectionListener {

	private static JFrame frame;
	protected static JTree tree;
	protected static DefaultMutableTreeNode top;
	
	public static SigmaScript script = new SigmaScript();
	static {
		SigmaClass main = new SigmaClass();
		main.name = "Main";
		main.access = SigmaAccessModifier.PUBLIC;
		script.mainClass = main;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		top = new DefaultMutableTreeNode("Program");
		tree = new JTree(top);
		tree.setBounds(0, 0, 391, 569);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
		
		frame.getContentPane().add(tree);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		menuBar.add(file);
		
		JMenuItem newItem = new JMenuItem("New...");
		newItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new NewWindow();
			}
		});
		file.add(newItem);
		
		JMenuItem printToConsoleItem = new JMenuItem("Dump source to console");
		printToConsoleItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(script.getSource());
			}
		});
		file.add(printToConsoleItem);
		
		frame.setJMenuBar(menuBar);
		
		updateInterface();
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		
		if(node == null) {
			return;
		}
		System.out.println(node);
	}
	
	public static void updateInterface() {
		top.removeAllChildren();
		for(SigmaGlobalVariable gv : script.mainClass.globalVariables) {
			DefaultMutableTreeNode n = new DefaultMutableTreeNode(gv.name);
			top.add(n);
		}
		for(SigmaMethod m : script.mainClass.methods) {
			String signature = (m.access == SigmaAccessModifier.NONE) ? "" : (m.access.toString().toLowerCase() + " ") + m.name + "(";
			{
				int i = 0;
				int size = m.parameters.size();
				for(String type : m.parameters.values()) {
					if(i == size - 1) {
						signature += " " + type;
					} else {
						signature += " " + type + ",";
					}
					i++;
				}
			}
			DefaultMutableTreeNode n = new DefaultMutableTreeNode(signature);
			top.add(n);
		}
	}
}
