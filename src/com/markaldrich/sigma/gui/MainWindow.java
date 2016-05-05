package com.markaldrich.sigma.gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.markaldrich.sigma.framework.elements.SigmaAccessModifier;
import com.markaldrich.sigma.framework.elements.SigmaAssignment;
import com.markaldrich.sigma.framework.elements.SigmaClass;
import com.markaldrich.sigma.framework.elements.SigmaElement;
import com.markaldrich.sigma.framework.elements.SigmaElementType;
import com.markaldrich.sigma.framework.elements.SigmaElseBlock;
import com.markaldrich.sigma.framework.elements.SigmaGlobalVariable;
import com.markaldrich.sigma.framework.elements.SigmaIfBlock;
import com.markaldrich.sigma.framework.elements.SigmaIfElseStatement;
import com.markaldrich.sigma.framework.elements.SigmaMethod;
import com.markaldrich.sigma.framework.elements.SigmaObject;
import com.markaldrich.sigma.framework.elements.SigmaScript;
import com.markaldrich.sigma.framework.elements.SigmaStatement;
import com.markaldrich.sigma.framework.elements.SigmaWhileLoop;

public class MainWindow implements TreeSelectionListener {
	/**
	 * The JFrame that is the main window.
	 */
	private static JFrame frmSigma;
	
	/**
	 * The tree that holds the program structure.
	 */
	protected static JTree tree;
	
	/**
	 * The model for the tree.
	 */
	protected static DefaultTreeModel model;
	
	/**
	 * The top of the tree. Should be the "Program" node.
	 */
	protected static DefaultMutableTreeNode top;
	
	/**
	 * The JTextPane that holds info about the currently
	 * selected node. This should be updated whenever the
	 * user clicks something. It is updated in this class.
	 */
	private static JTextPane selectedItemPane;
	
	/**
	 * The JTextPane that shows the source for the currently
	 * selected objects. This should be updated whenever the
	 * user clicks something. It is updated in this class,
	 * but the source is generated from the 
	 * com.markaldrich.sigma.framework.elements.* objects.
	 */
	private static JTextPane sourcePane;
	
	/**
	 * The HashMap that maps each node to its SigmaElement counterpart.
	 */
	private static HashMap<DefaultMutableTreeNode, SigmaElement> map = new HashMap<>();

	/**
	 * The SigmaScript that represents the program that the 
	 * user is currently working on. Currently, this program
	 * is generated before the window even appears and you
	 * cannot save it and or load it later.
	 */
	public static SigmaScript script = new SigmaScript();

	/**
	 * More static initialization of the SigmaScript,
	 * as you must manually initialize these things after
	 * creating the SigmaScript.
	 */
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
		System.setProperty("apple.awt.application.name", "Sigma");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainWindow();
					frmSigma.setVisible(true);
				} catch(Exception e) {
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
		frmSigma = new JFrame();
		frmSigma.setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/res/logo.png")));
		frmSigma.setTitle("Sigma");
		frmSigma.setBounds(100, 100, 800, 600);
		frmSigma.setResizable(false);
		frmSigma.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSigma.getContentPane().setLayout(null);
		
		// Initialization
		top = new DefaultMutableTreeNode("Program");
		tree = new JTree(top);
		tree.setFont(new Font("Lucida Console", Font.PLAIN, 11));
		tree.setBounds(0, 0, 391, 548);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) {
					// Try to find the element that the user clicked on
					final TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					Rectangle pathBounds = tree.getUI().getPathBounds(tree, path);
					if(pathBounds != null && pathBounds.contains(e.getX(), e.getY())) {
						final DefaultMutableTreeNode selectedItem = (DefaultMutableTreeNode) path
								.getLastPathComponent();
						final SigmaElement element = map.get(selectedItem);
						final SigmaElementType type = (element instanceof SigmaGlobalVariable)
								? SigmaElementType.GLOBAL_VARIABLE
								: (element instanceof SigmaScript) ? SigmaElementType.SCRIPT
								: (element instanceof SigmaMethod) ? SigmaElementType.METHOD
								: (element instanceof SigmaIfElseStatement) ? SigmaElementType.IF_ELSE
								: (element instanceof SigmaIfBlock) ? SigmaElementType.IF
								: (element instanceof SigmaElseBlock) ? SigmaElementType.ELSE
								: (element instanceof SigmaWhileLoop) ? SigmaElementType.WHILE_LOOP
								: (element instanceof SigmaStatement) ? SigmaElementType.STATEMENT
								: SigmaElementType.UNKNOWN;

						System.out.println();
						
						// This is the menu that will pop up
						// after right clicking on an element.
						JPopupMenu menu = new JPopupMenu();
						
						// Determine the appropriate menu items
						// to show according to the type of element
						// clicked on.
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
						a: if(type == SigmaElementType.SCRIPT) {
							// Exit if the main method already exists
							for(SigmaMethod m : script.mainClass.methods) {
								if(m.name.equals("main")) {
									break a;
								}
							}
							JMenuItem addEntryPoint = new JMenuItem("Add entry point to program");
							addEntryPoint.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									SigmaMethod mainMethod = new SigmaMethod();
									mainMethod.name = "main";
									mainMethod.isStatic = true;
									mainMethod.returnType = "void";
									mainMethod.access = SigmaAccessModifier.PUBLIC;
									mainMethod.parameters.put("args", "String[]");
									script.mainClass.methods.add(mainMethod);
									updateInterface();
								}
							});
							menu.add(addEntryPoint);
						}
						if(type == SigmaElementType.IF) {
							JMenuItem addStatement = new JMenuItem("Add statement");
							addStatement.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									TreePath parentMethodPath = path.getParentPath();
									for(int i = 0; i < path.getPathCount(); i++) {
										if(map.get(parentMethodPath.getLastPathComponent()) instanceof SigmaMethod) {
											new NewStatementWindow(element,
													((SigmaMethod) map.get(parentMethodPath.getLastPathComponent())));
											break;
										}
										parentMethodPath = parentMethodPath.getParentPath();
									}
								}
							});
							menu.add(addStatement);
						}
						if(type == SigmaElementType.ELSE) {
							System.out.println("e");
							JMenuItem addStatement = new JMenuItem("Add statement");
							addStatement.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									TreePath parentMethodPath = path.getParentPath();
									for(int i = 0; i < path.getPathCount(); i++) {
										if(map.get(parentMethodPath.getLastPathComponent()) instanceof SigmaMethod) {
											new NewStatementWindow(element,
													((SigmaMethod) map.get(parentMethodPath.getLastPathComponent())));
											break;
										}
										parentMethodPath = parentMethodPath.getParentPath();
									}
								}
							});
							menu.add(addStatement);
						}

						{
							if(type != SigmaElementType.SCRIPT
									&& map.get(selectedItem.getParent()) instanceof SigmaMethod) {
								JMenuItem addStatementBelow = new JMenuItem("Add statement below");
								addStatementBelow.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										new NewStatementWindow((SigmaMethod) map.get(selectedItem.getParent()),
												selectedItem.getParent().getIndex(selectedItem) + 1);
									}
								});
								menu.add(addStatementBelow);

								JMenuItem addStatementHere = new JMenuItem("Add statement here");
								addStatementHere.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										new NewStatementWindow((SigmaMethod) map.get(selectedItem.getParent()),
												selectedItem.getParent().getIndex(selectedItem));
									}
								});
								menu.add(addStatementHere);

								JMenuItem moveUp = new JMenuItem("Move statement up");
								moveUp.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										if(selectedItem.getParent().getIndex(selectedItem) == 0) {
											Toolkit.getDefaultToolkit().beep();
											return;
										}
										SigmaMethod parent = (SigmaMethod) map.get(selectedItem.getParent());
										Collections.swap(parent.statements,
												selectedItem.getParent().getIndex(selectedItem),
												selectedItem.getParent().getIndex(selectedItem) - 1);
										updateInterface();
									}
								});
								menu.add(moveUp);

								JMenuItem moveDown = new JMenuItem("Move statement down");
								moveDown.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										if(selectedItem.getParent().getIndex(
												selectedItem) == selectedItem.getParent().getChildCount() - 1) {
											Toolkit.getDefaultToolkit().beep();
											return;
										}
										SigmaMethod parent = (SigmaMethod) map.get(selectedItem.getParent());
										Collections.swap(parent.statements,
												selectedItem.getParent().getIndex(selectedItem),
												selectedItem.getParent().getIndex(selectedItem) + 1);
										updateInterface();
									}
								});
								menu.add(moveDown);
							}
						}

						if(type != SigmaElementType.IF || type != SigmaElementType.ELSE) {
							JMenuItem delete = new JMenuItem(
									"Delete " + type.toString().toLowerCase().replace('_', ' '));
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
										script.mainClass.methods.get(script.mainClass.methods
												.indexOf(map.get(selectedItem.getParent()))).statements
														.remove((SigmaStatement) element);
										break;
									case WHILE_LOOP:
										script.mainClass.methods.get(script.mainClass.methods
												.indexOf(map.get(selectedItem.getParent()))).statements
														.remove((SigmaWhileLoop) element);
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
		frmSigma.getContentPane().add(tree);

		selectedItemPane = new JTextPane();
		selectedItemPane.setEditable(false);
		selectedItemPane.setBounds(401, 26, 383, 276);
		JScrollPane sipScroll = new JScrollPane(selectedItemPane);
		sipScroll.setBounds(selectedItemPane.getBounds());
		frmSigma.getContentPane().add(sipScroll);

		JLabel lblInfo = new JLabel("Information");
		lblInfo.setBounds(401, 1, 383, 14);
		frmSigma.getContentPane().add(lblInfo);

		JLabel lblSource = new JLabel("Source");
		lblSource.setBounds(401, 313, 383, 14);
		frmSigma.getContentPane().add(lblSource);

		sourcePane = new JTextPane();
		sourcePane.setEditable(false);
		sourcePane.setBounds(401, 338, 383, 201);
		JScrollPane spScroll = new JScrollPane(sourcePane);
		spScroll.setBounds(sourcePane.getBounds());
		frmSigma.getContentPane().add(spScroll);
		
		// The menu bar at the top of the window
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

		JMenuItem mntmSaveSourceAs = new JMenuItem("Save source as...");
		mntmSaveSourceAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter("Java source files", "java"));
				JOptionPane.showMessageDialog(frmSigma, "The main class in your program is titled \""
						+ script.mainClass.name
						+ "\".\nFor your program to compile correctly, you must save your file with that name.\nTo compile your program, run \"javac filename.java\" from the command line.\n(Note: you must have the JDK installed to compile Java programs. If you are running Windows, you must also set your PATH environment variable to include \"javac.exe\".)\nTo run your program, run \"java -cp . filename\"\n(Note: You must have the JRE or JDK installed to run Java programs. Also note that you do not type any file extension while running.)");
				int response = fc.showOpenDialog(frmSigma);
				if(response == JFileChooser.APPROVE_OPTION) {
					File file = new File(fc.getSelectedFile().getAbsolutePath());
					PrintWriter writer;
					try {
						writer = new PrintWriter(file);
					} catch(FileNotFoundException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(frmSigma, "There was an error saving the file.");
						return;
					}
					writer.println(script.getSource());
					writer.close();
					JOptionPane.showMessageDialog(frmSigma,
							"Successfully saved file at " + fc.getSelectedFile().getAbsolutePath());
				}
			}
		});

		JMenuItem mntmManageImports = new JMenuItem("Manage imports...");
		mntmManageImports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ImportsWindow();
			}
		});
		file.add(mntmManageImports);
		file.add(mntmSaveSourceAs);

		JMenuItem mntmShowSource = new JMenuItem("Show source");
		mntmShowSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ShowSourceWindow(script.getSource());
			}
		});
		file.add(mntmShowSource);
		file.add(printToConsoleItem);

		frmSigma.setJMenuBar(menuBar);

		updateInterface();
	}

	/**
	 * Updates the selectItemPane and sourcePane
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		if(node == null) {
			return;
		}

		System.out.println(node);

		String s = "";
		SigmaElement element = map.get(node);

		System.out.println(element);
		System.out.println(element.getClass().getName());

		final SigmaElementType type = (element instanceof SigmaGlobalVariable) ? SigmaElementType.GLOBAL_VARIABLE
				: (element instanceof SigmaScript) ? SigmaElementType.SCRIPT
						: (element instanceof SigmaMethod) ? SigmaElementType.METHOD
								: (element instanceof SigmaIfElseStatement) ? SigmaElementType.IF_ELSE
										: (element instanceof SigmaIfBlock) ? SigmaElementType.IF
												: (element instanceof SigmaElseBlock) ? SigmaElementType.ELSE
														: (element instanceof SigmaObject) ? SigmaElementType.OBJECT
																: (element instanceof SigmaStatement)
																		? SigmaElementType.STATEMENT
																		: SigmaElementType.UNKNOWN;

		sourcePane.setText("");

		switch(type) {
		case GLOBAL_VARIABLE:
			s += "Global Variable";
			SigmaGlobalVariable gv = (SigmaGlobalVariable) element;
			s += " " + gv.name;

			sourcePane.setText(gv.declarationToString());
			break;
		case SCRIPT:
			s += "Script";
			SigmaScript sc = (SigmaScript) element;

			sourcePane.setText(sc.getSource());
			break;
		case METHOD:
			s += "Method";
			SigmaMethod m = (SigmaMethod) element;
			s += " " + m.name;

			s += "\n";

			s += "Parameters:\n";

			for(String p : m.parameters.keySet()) {
				s += "	Name = " + p + "; Type = " + m.parameters.get(p) + "\n";
			}

			sourcePane.setText(m.toString());
			break;
		case IF_ELSE:
			s += "If/Else Statement\n";
			SigmaIfElseStatement ifs = (SigmaIfElseStatement) element;
			s += "Condition: " + ifs.condition + "\n";

			sourcePane.setText(ifs.toString());
			break;
		case IF:
			s += "If Block";
			SigmaIfBlock ib = (SigmaIfBlock) element;

			String source = "";
			for(SigmaStatement ss : ib.statements) {
				if(ss instanceof SigmaGlobalVariable) {
					source += ((SigmaGlobalVariable) ss).declarationToString();
				} else if(ss instanceof SigmaObject) {
					source += ((SigmaObject) ss).declarationToString();
				} else {
					source += ss.toString();
				}
			}

			sourcePane.setText(source);
			break;
		case ELSE:
			s += "Else Block";
			SigmaElseBlock eb = (SigmaElseBlock) element;

			String source2 = "";
			for(SigmaStatement ss : eb.statements) {
				if(ss instanceof SigmaGlobalVariable) {
					source2 += ((SigmaGlobalVariable) ss).declarationToString();
				} else if(ss instanceof SigmaObject) {
					source2 += ((SigmaObject) ss).declarationToString();
				} else {
					source2 += ss.toString();
				}
			}

			sourcePane.setText(source2);
			break;
		case STATEMENT:
			SigmaStatement st = (SigmaStatement) element;
			if(st instanceof SigmaAssignment) {
				SigmaAssignment sa = (SigmaAssignment) st;
				s += "Assignment\n";
				s += sa.object + " -> " + sa.dataToAssign;

				sourcePane.setText(sa.toString());
			}

			// TODO: Add more types here
			break;
		case UNKNOWN:
			throw new RuntimeException("Unknown type!");
		case CLASS:
			break;
		case OBJECT:
			s += "Variable";
			SigmaObject o = (SigmaObject) element;
			s += " " + o.name;

			s += "\n";

			s += "Name -> " + o.name + "\n";
			s += "Type -> " + o.type + "\n";
			s += "Data -> " + o.data + "\n";

			sourcePane.setText(o.declarationToString());
			break;
		default:
			break;
		}

		selectedItemPane.setText("");
		selectedItemPane.setText(s);
	}

	/**
	 * Updates the tree interface.
	 */
	public static void updateInterface() {
		// Clear the tree.
		top.removeAllChildren();
		
		// Clear the bindings between the SigmaElements and
		// the tree nodes.
		map.clear();

		// Map the "Program" node to the SigmaScript
		// itself, which happens to be a SigmaElement,
		// so it fits in this HashMap.
		map.put(top, script);

		// Add all of the global variables to the tree.
		for(SigmaGlobalVariable gv : script.mainClass.globalVariables) {
			// Construct the node, add it to the "Program"
			// node, and map it to the SigmaGlobalVariable that
			// it corresponds to.
			DefaultMutableTreeNode n = new DefaultMutableTreeNode(gv.name);
			top.add(n);
			map.put(n, gv);
		}
		
		// Add all of the methods to the tree.
		for(SigmaMethod m : script.mainClass.methods) {
			// Construct the node, add it to the "Program"
			// node, and map it to the SigmaGlobalVariable that
			// it corresponds to.
			DefaultMutableTreeNode methodNode = updateMethod(m);
			top.add(methodNode);
			map.put(methodNode, m);
		}

		// Reload the model. If we do not relaod it,
		// the changes would not occur. This is
		// intentionally done after we do all of the
		// changes, as if we updated it in any other place,
		// the model would flicker. This way, the updating is
		// smooth and the user would not notice anything.
		model.reload();
		
		// Expand all of the nodes in the entire tree,
		// as by default, they are closed, and it is
		// seriously annoying to have to expand every
		// single node any time you make a change. The
		// nodes do not keep their status, as the entire
		// tree is cleared on every update.
		expandAllNodes();
	}

	/**
	 * Generates a DefaultMutableTreeNode for a
	 * SigmaMethod.
	 * @param m The SigmaMethod we are generating the node for.
	 * @return The DefaultMutableTreeNode that represents m.
	 */
	public static DefaultMutableTreeNode updateMethod(SigmaMethod m) {
		String signature = ((m.access == SigmaAccessModifier.NONE) ? ""
				: (m.access.toString().toLowerCase() + " ")) + ((m.isStatic) ? "static " : "") + m.returnType + " "
						+ m.name + "(";
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

	/**
	 * Generates a DefaultMutableTreeNode for a
	 * SigmaStatement and adds it to the tree.
	 * @param s The SigmaStatement that we are generating the node for.
	 * @param parent The node that the statement's new node should be added under.
	 */
	public static void updateStatement(SigmaStatement s, DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode n = parent;
		if(s instanceof SigmaAssignment) {
			DefaultMutableTreeNode statement = new DefaultMutableTreeNode(
					((SigmaAssignment) s).object + " -> " + ((SigmaAssignment) s).dataToAssign);
			n.add(statement);
			map.put(statement, (SigmaAssignment) s);
		} else if(s instanceof SigmaObject) {
			DefaultMutableTreeNode statement = new DefaultMutableTreeNode(
					"New: " + ((SigmaObject) s).name + " -> " + ((SigmaObject) s).data);
			n.add(statement);
			map.put(statement, (SigmaElement) s);
		} else if(s instanceof SigmaIfElseStatement) {
			DefaultMutableTreeNode statement = new DefaultMutableTreeNode(
					"If " + ((SigmaIfElseStatement) s).condition.split(" == ")[0] + " is "
							+ ((SigmaIfElseStatement) s).condition.split(" == ")[1]);
			DefaultMutableTreeNode thenBlock = new DefaultMutableTreeNode("Then");
			for(SigmaStatement st : ((SigmaIfElseStatement) s).ifTrue.statements) {
				updateStatement(st, thenBlock);
			}
			DefaultMutableTreeNode elseBlock = new DefaultMutableTreeNode("Else");
			for(SigmaStatement st : ((SigmaIfElseStatement) s).ifFalse.statements) {
				updateStatement(st, elseBlock);
			}
			map.put(statement, (SigmaIfElseStatement) s);
			map.put(thenBlock, ((SigmaIfElseStatement) s).ifTrue);
			map.put(elseBlock, ((SigmaIfElseStatement) s).ifFalse);
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
	 * Simple method to expand all of the nodes in
	 * the tree.
	 */
	private static void expandAllNodes() {
		int rc = tree.getRowCount();
		for(int i = 0; i < rc; i++) {
			tree.expandRow(i);
		}
		
		if(tree.getRowCount() != rc) {
			// Expand all of the nodes again if more were opened.
			// expandAllNodes() will keep executing recursively
			// until all of the nodes are expanded.
			expandAllNodes();
		}
	}
}
