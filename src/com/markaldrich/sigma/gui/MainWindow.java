package com.markaldrich.sigma.gui;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.markaldrich.sigma.framework.elements.SigmaAccessModifier;
import com.markaldrich.sigma.framework.elements.SigmaAssignment;
import com.markaldrich.sigma.framework.elements.SigmaClass;
import com.markaldrich.sigma.framework.elements.SigmaElement;
import com.markaldrich.sigma.framework.elements.SigmaElseBlock;
import com.markaldrich.sigma.framework.elements.SigmaGlobalVariable;
import com.markaldrich.sigma.framework.elements.SigmaIfBlock;
import com.markaldrich.sigma.framework.elements.SigmaIfElseStatement;
import com.markaldrich.sigma.framework.elements.SigmaMethod;
import com.markaldrich.sigma.framework.elements.SigmaObject;
import com.markaldrich.sigma.framework.elements.SigmaScript;
import com.markaldrich.sigma.framework.elements.SigmaStatement;
import com.markaldrich.sigma.framework.elements.SigmaElementType;


public class MainWindow implements TreeSelectionListener {

	private static JFrame frame;
	protected static JTree tree;
	protected static DefaultTreeModel model;
	protected static DefaultMutableTreeNode top;
	private static HashMap<DefaultMutableTreeNode, SigmaElement> map = new HashMap<>();
	
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
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) {
					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					Rectangle pathBounds = tree.getUI().getPathBounds(tree, path);
					if(pathBounds != null && pathBounds.contains(e.getX(), e.getY())) {
						DefaultMutableTreeNode selectedItem = (DefaultMutableTreeNode) path.getLastPathComponent();
						SigmaElement element = map.get(selectedItem);
						final SigmaElementType type = (element instanceof SigmaGlobalVariable) ? SigmaElementType.GLOBAL_VARIABLE : 
							(element instanceof SigmaMethod) ? SigmaElementType.METHOD : 
								(element instanceof SigmaStatement) ? SigmaElementType.STATEMENT : 
									(element instanceof SigmaIfElseStatement) ? SigmaElementType.IF_ELSE : 
										(element instanceof SigmaIfBlock) ? SigmaElementType.IF : 
											(element instanceof SigmaElseBlock) ? SigmaElementType.ELSE : SigmaElementType.UNKNOWN;
						
						JPopupMenu menu = new JPopupMenu();
						if(type == SigmaElementType.METHOD) {
							JMenuItem addStatement = new JMenuItem("Add statement");
							addStatement.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									new NewStatementWindow((SigmaMethod) element);
								}
							});
							menu.add(addStatement);
						}
						if(type == SigmaElementType.IF) {
							JMenuItem addStatement = new JMenuItem("Add statement");
							addStatement.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									new NewStatementWindow((SigmaMethod) element);
								}
							});
							menu.add(addStatement);
						}
						
						if(type != SigmaElementType.IF || type != SigmaElementType.ELSE) {
							JMenuItem delete = new JMenuItem("Delete " + type.toString().toLowerCase().replace('_', ' '));
							delete.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									map.remove(selectedItem);
									switch(type) {
									case GLOBAL_VARIABLE:
										script.mainClass.globalVariables.remove(element);
										break;
									case METHOD:
										script.mainClass.methods.remove(element);
										break;
									case STATEMENT:
										script.mainClass.methods.get(script.mainClass.methods.indexOf(map.get(selectedItem.getParent()))).statements.remove((SigmaStatement) element);
										break;									
									default:
										System.err.println("Couldn't assume type.");
									}
									updateInterface();
								}
							});
							menu.add(delete);
						}
						menu.show(tree, pathBounds.x, pathBounds.y + pathBounds.height);
					}
				}
			}
		});
		tree.addTreeSelectionListener(this);
		model = (DefaultTreeModel) tree.getModel();
		frame.getContentPane().add(tree);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateInterface();
			}
		});
		btnUpdate.setBounds(491, 218, 89, 23);
		frame.getContentPane().add(btnUpdate);
		
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
		map.clear();
		
		for(SigmaGlobalVariable gv : script.mainClass.globalVariables) {
			DefaultMutableTreeNode n = new DefaultMutableTreeNode(gv.name);
			top.add(n);
			map.put(n, gv);
		}
		
		for(SigmaMethod m : script.mainClass.methods) {
			DefaultMutableTreeNode methodNode = updateMethod(m);
			top.add(methodNode);
			map.put(methodNode, m);
		}
		
		model.reload();
	}
	
	public static DefaultMutableTreeNode updateMethod(SigmaMethod m) {
		String signature = (m.access == SigmaAccessModifier.NONE) ? "" : (m.access.toString().toLowerCase() + " ")
				+ ((m.isStatic) ? "static " : "") + m.returnType + " " + m.name + "(";
		{
			int i = 0;
			int size = m.parameters.size();
			for(String type : m.parameters.values()) {
				if(i == size - 1) {
					signature += type;
				} else {
					signature += type + ", ";
				}
				i++;
			}
		}
		signature += ")";
		DefaultMutableTreeNode methodNode = new DefaultMutableTreeNode(signature);
		
		for(SigmaStatement s : m.statements) {
			updateStatement(s, methodNode);
		}
		
		return methodNode;
	}
	
	public static void updateStatement(SigmaStatement s, DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode n = parent;
		if(s instanceof SigmaAssignment) {
			DefaultMutableTreeNode statement = new DefaultMutableTreeNode(((SigmaAssignment) s).object + 
					" equals " + ((SigmaAssignment) s).dataToAssign);
			n.add(statement);
			map.put(statement, (SigmaAssignment) s);
		} else if(s instanceof SigmaObject) {
			DefaultMutableTreeNode statement = new DefaultMutableTreeNode("New: " + ((SigmaObject) s).name + " equals " + ((SigmaObject) s).data);
			n.add(statement);
			map.put(statement, (SigmaElement) s);
		} else if(s instanceof SigmaIfElseStatement) {
			DefaultMutableTreeNode statement = new DefaultMutableTreeNode("If " + ((SigmaIfElseStatement) s).condition.split(" == ")[0] + 
					" is " + ((SigmaIfElseStatement) s).condition.split(" == ")[1]);
			DefaultMutableTreeNode thenBlock = new DefaultMutableTreeNode("Then");
			for(SigmaStatement st : ((SigmaIfElseStatement) s).ifTrue.statements) {
				updateStatement(st, thenBlock);
			}
			DefaultMutableTreeNode elseBlock = new DefaultMutableTreeNode("Else");
			for(SigmaStatement st : ((SigmaIfElseStatement) s).ifFalse.statements) {
				updateStatement(st, elseBlock);
			}
			statement.add(thenBlock);
			statement.add(elseBlock);
			n.add(statement);
		} else {
			DefaultMutableTreeNode statement = new DefaultMutableTreeNode(s.toString());
			n.add(statement);
			map.put(statement, (SigmaElement) s);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	
	
	
	
	
	
	
	
	
	
	
	public static synchronized void updateInterface() {
		top.removeAllChildren();
		map.clear();
		
		for(SigmaGlobalVariable gv : script.mainClass.globalVariables) {
			DefaultMutableTreeNode n = new DefaultMutableTreeNode(gv.name);
			top.add(n);
			map.put(n, gv);
		}
		for(SigmaMethod m : script.mainClass.methods) {
			DefaultMutableTreeNode n = updateStatementsInMethod(m);
			
		}
		model.reload();
	}
	
	public static DefaultMutableTreeNode updateStatementsInMethod(SigmaMethod m) {
		String signature = (m.access == SigmaAccessModifier.NONE) ? "" : (m.access.toString().toLowerCase() + " ")
				+ ((m.isStatic) ? "static " : "") + m.returnType + " " + m.name + "(";
		{
			int i = 0;
			int size = m.parameters.size();
			for(String type : m.parameters.values()) {
				if(i == size - 1) {
					signature += type + " ";
				} else {
					signature += type + ", ";
				}
				i++;
			}
		}
		signature += ")";
		DefaultMutableTreeNode n = new DefaultMutableTreeNode(signature);
		
		for(SigmaStatement s : m.statements) {
			if(s instanceof SigmaAssignment) {
				DefaultMutableTreeNode statement = new DefaultMutableTreeNode(((SigmaAssignment) s).object + 
						" equals " + ((SigmaAssignment) s).dataToAssign);
				n.add(statement);
				map.put(statement, (SigmaAssignment) s);
			} else if(s instanceof SigmaObject) {
				DefaultMutableTreeNode statement = new DefaultMutableTreeNode("New: " + ((SigmaObject) s).name + " equals " + ((SigmaObject) s).data);
				n.add(statement);
				map.put(statement, (SigmaElement) s);
			} else if(s instanceof SigmaIfElseStatement) {
				updateStatementsInIfElseStatement((SigmaIfElseStatement) s);
			} else {
				DefaultMutableTreeNode statement = new DefaultMutableTreeNode(s.toString());
				n.add(statement);
				map.put(statement, (SigmaElement) s);
			}
		}
		
		top.add(n);
		map.put(n, m);
	}
	
	public static DefaultMutableTreeNode updateStatementsInIfElseStatement(SigmaIfElseStatement ifElseStatement) {
		for(SigmaStatement s : ifElseStatement.ifTrue) {
			if(s instanceof SigmaAssignment) {
				DefaultMutableTreeNode statement = new DefaultMutableTreeNode(((SigmaAssignment) s).object + 
						" equals " + ((SigmaAssignment) s).dataToAssign);
				n.add(statement);
				map.put(statement, (SigmaAssignment) s);
			} else if(s instanceof SigmaObject) {
				DefaultMutableTreeNode statement = new DefaultMutableTreeNode("New: " + ((SigmaObject) s).name + " equals " + ((SigmaObject) s).data);
				n.add(statement);
				map.put(statement, (SigmaElement) s);
			} else if(s instanceof SigmaIfElseStatement) {
				 SigmaIfElseStatement ie = (SigmaIfElseStatement) s;
				 DefaultMutableTreeNode ifElse = new DefaultMutableTreeNode("If " + ie.condition.split(" == ")[0] + " is " + ie.condition.split(" == ")[0]);
				 DefaultMutableTreeNode thenBlock = new DefaultMutableTreeNode("Then");
				 
				 
				 
				 DefaultMutableTreeNode elseBlock = new DefaultMutableTreeNode("Else");
				 
				 
				 
				 ifElse.add(thenBlock);
				 ifElse.add(elseBlock);
				 n.add(ifElse);
				 map.put(ifElse, ie);
			} else {
				DefaultMutableTreeNode statement = new DefaultMutableTreeNode(s.toString());
				n.add(statement);
				map.put(statement, (SigmaElement) s);
			}
		}
	}*/
}
