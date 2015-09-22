package com.markaldrich.sigma.gui;

import java.awt.EventQueue;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
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
import java.awt.Font;

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
		tree.setFont(new Font("Lucida Console", Font.PLAIN, 11));
		tree.setBounds(0, 0, 391, 548);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					Rectangle pathBounds = tree.getUI().getPathBounds(tree,
							path);
					if (pathBounds != null
							&& pathBounds.contains(e.getX(), e.getY())) {
						DefaultMutableTreeNode selectedItem = (DefaultMutableTreeNode) path
								.getLastPathComponent();
						SigmaElement element = map.get(selectedItem);
						final SigmaElementType type = (element instanceof SigmaGlobalVariable) ? SigmaElementType.GLOBAL_VARIABLE
								: (element instanceof SigmaScript) ? SigmaElementType.SCRIPT
										: (element instanceof SigmaMethod) ? SigmaElementType.METHOD
												: (element instanceof SigmaIfElseStatement) ? SigmaElementType.IF_ELSE
														: (element instanceof SigmaIfBlock) ? SigmaElementType.IF
																: (element instanceof SigmaElseBlock) ? SigmaElementType.ELSE
																		: (element instanceof SigmaStatement) ? SigmaElementType.STATEMENT
																				: SigmaElementType.UNKNOWN;

						System.out.println();
						JPopupMenu menu = new JPopupMenu();
						if (type == SigmaElementType.METHOD) {
							JMenuItem addStatement = new JMenuItem(
									"Add statement");
							addStatement
									.addActionListener(new ActionListener() {
										@Override
										public void actionPerformed(
												ActionEvent arg0) {
											new NewStatementWindow(
													(SigmaMethod) element);
										}
									});
							menu.add(addStatement);
						}
						a: if (type == SigmaElementType.SCRIPT) {
							for (SigmaMethod m : script.mainClass.methods) {
								if (m.name.equals("main")) {
									break a;
								}
							}
							JMenuItem addEntryPoint = new JMenuItem(
									"Add entry point to program");
							addEntryPoint
									.addActionListener(new ActionListener() {
										@Override
										public void actionPerformed(
												ActionEvent arg0) {
											SigmaMethod mainMethod = new SigmaMethod();
											mainMethod.name = "main";
											mainMethod.isStatic = true;
											mainMethod.returnType = "void";
											mainMethod.access = SigmaAccessModifier.PUBLIC;
											mainMethod.parameters.put("args",
													"String[]");
											script.mainClass.methods
													.add(mainMethod);
											updateInterface();
										}
									});
							menu.add(addEntryPoint);
						}
						if (type == SigmaElementType.IF) {
							JMenuItem addStatement = new JMenuItem(
									"Add statement");
							addStatement
									.addActionListener(new ActionListener() {
										@Override
										public void actionPerformed(
												ActionEvent arg0) {
											TreePath parentMethodPath = path
													.getParentPath();
											for (int i = 0; i < path
													.getPathCount(); i++) {
												if (map.get(parentMethodPath
														.getLastPathComponent()) instanceof SigmaMethod) {
													new NewStatementWindow(
															element,
															((SigmaMethod) map
																	.get(parentMethodPath
																			.getLastPathComponent())));
													break;
												}
												parentMethodPath = parentMethodPath
														.getParentPath();
											}
										}
									});
							menu.add(addStatement);
						}
						if (type == SigmaElementType.ELSE) {
							System.out.println("e");
							JMenuItem addStatement = new JMenuItem(
									"Add statement");
							addStatement
									.addActionListener(new ActionListener() {
										@Override
										public void actionPerformed(
												ActionEvent arg0) {
											TreePath parentMethodPath = path
													.getParentPath();
											for (int i = 0; i < path
													.getPathCount(); i++) {
												if (map.get(parentMethodPath
														.getLastPathComponent()) instanceof SigmaMethod) {
													new NewStatementWindow(
															element,
															((SigmaMethod) map
																	.get(parentMethodPath
																			.getLastPathComponent())));
													break;
												}
												parentMethodPath = parentMethodPath
														.getParentPath();
											}
										}
									});
							menu.add(addStatement);
						}

						{
							if (type != SigmaElementType.SCRIPT
									&& map.get(selectedItem.getParent()) instanceof SigmaMethod) {
								JMenuItem addStatementBelow = new JMenuItem(
										"Add statement below");
								addStatementBelow
										.addActionListener(new ActionListener() {
											@Override
											public void actionPerformed(
													ActionEvent arg0) {
												new NewStatementWindow(
														(SigmaMethod) map
																.get(selectedItem
																		.getParent()),
														selectedItem
																.getParent()
																.getIndex(
																		selectedItem) + 1);
											}
										});
								menu.add(addStatementBelow);

								JMenuItem addStatementHere = new JMenuItem(
										"Add statement here");
								addStatementHere
										.addActionListener(new ActionListener() {
											@Override
											public void actionPerformed(
													ActionEvent arg0) {
												new NewStatementWindow(
														(SigmaMethod) map
																.get(selectedItem
																		.getParent()),
														selectedItem
																.getParent()
																.getIndex(
																		selectedItem));
											}
										});
								menu.add(addStatementHere);

								JMenuItem moveUp = new JMenuItem(
										"Move statement up");
								moveUp.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										if (selectedItem.getParent().getIndex(
												selectedItem) == 0) {
											Toolkit.getDefaultToolkit().beep();
											return;
										}
										SigmaMethod parent = (SigmaMethod) map
												.get(selectedItem.getParent());
										Collections
												.swap(parent.statements,
														selectedItem
																.getParent()
																.getIndex(
																		selectedItem),
														selectedItem
																.getParent()
																.getIndex(
																		selectedItem) - 1);
										updateInterface();
									}
								});
								menu.add(moveUp);

								JMenuItem moveDown = new JMenuItem(
										"Move statement down");
								moveDown.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent arg0) {
										if (selectedItem.getParent().getIndex(
												selectedItem) == selectedItem
												.getParent().getChildCount() - 1) {
											Toolkit.getDefaultToolkit().beep();
											return;
										}
										SigmaMethod parent = (SigmaMethod) map
												.get(selectedItem.getParent());
										Collections
												.swap(parent.statements,
														selectedItem
																.getParent()
																.getIndex(
																		selectedItem),
														selectedItem
																.getParent()
																.getIndex(
																		selectedItem) + 1);
										updateInterface();
									}
								});
								menu.add(moveDown);
							}
						}

						if (type != SigmaElementType.IF
								|| type != SigmaElementType.ELSE) {
							JMenuItem delete = new JMenuItem("Delete "
									+ type.toString().toLowerCase()
											.replace('_', ' '));
							delete.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									map.remove(selectedItem);
									switch (type) {
									case GLOBAL_VARIABLE:
										script.mainClass.globalVariables
												.remove(element);
										break;
									case METHOD:
										script.mainClass.methods
												.remove(element);
										break;
									case STATEMENT:
										script.mainClass.methods.get(script.mainClass.methods
												.indexOf(map.get(selectedItem
														.getParent()))).statements
												.remove((SigmaStatement) element);
										break;
									default:
										System.err
												.println("Couldn't assume type.");
									}
									updateInterface();
								}
							});
							menu.add(delete);
						}
						menu.show(tree, pathBounds.x, pathBounds.y
								+ pathBounds.height);
					}
				}
			}
		});
		tree.addTreeSelectionListener(this);
		model = (DefaultTreeModel) tree.getModel();
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

		JMenuItem mntmSaveSourceAs = new JMenuItem("Save source as...");
		mntmSaveSourceAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter(
						"Java source files", "java"));
				JOptionPane
						.showMessageDialog(
								frame,
								"The main class in your program is titled \""
										+ script.mainClass.name
										+ "\".\nFor your program to compile correctly, you must save your file with that name.\nTo compile your program, run \"javac filename.java\" from the command line.\n(Note: you must have the JDK installed to compile Java programs. If you are running Windows, you must also set your PATH environment variable to include \"javac.exe\".)\nTo run your program, run \"java -cp . filename\"\n(Note: You must have the JRE or JDK installed to run Java programs. Also note that you do not type any file extension while running.)");
				int response = fc.showOpenDialog(frame);
				if (response == JFileChooser.APPROVE_OPTION) {
					File file = new File(fc.getSelectedFile().getAbsolutePath());
					PrintWriter writer;
					try {
						writer = new PrintWriter(file);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(frame,
								"There was an error saving the file.");
						return;
					}
					writer.println(script.getSource());
					writer.close();
					JOptionPane.showMessageDialog(frame,
							"Successfully saved file at "
									+ fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		file.add(mntmSaveSourceAs);
		file.add(printToConsoleItem);

		frame.setJMenuBar(menuBar);

		updateInterface();
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();

		if (node == null) {
			return;
		}

		System.out.println(node);
	}

	public static void updateInterface() {
		top.removeAllChildren();
		map.clear();

		map.put(top, script);

		for (SigmaGlobalVariable gv : script.mainClass.globalVariables) {
			DefaultMutableTreeNode n = new DefaultMutableTreeNode(gv.name);
			top.add(n);
			map.put(n, gv);
		}

		for (SigmaMethod m : script.mainClass.methods) {
			DefaultMutableTreeNode methodNode = updateMethod(m);
			top.add(methodNode);
			map.put(methodNode, m);
		}

		model.reload();
	}

	public static DefaultMutableTreeNode updateMethod(SigmaMethod m) {
		String signature = (m.access == SigmaAccessModifier.NONE) ? ""
				: (m.access.toString().toLowerCase() + " ")
						+ ((m.isStatic) ? "static " : "") + m.returnType + " "
						+ m.name + "(";
		{
			int i = 0;
			int size = m.parameters.size();
			for (String type : m.parameters.values()) {
				if (i == size - 1) {
					signature += type;
				} else {
					signature += type + ", ";
				}
				i++;
			}
		}
		signature += ")";
		DefaultMutableTreeNode methodNode = new DefaultMutableTreeNode(
				signature);

		for (SigmaStatement s : m.statements) {
			updateStatement(s, methodNode);
		}

		return methodNode;
	}

	public static void updateStatement(SigmaStatement s,
			DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode n = parent;
		if (s instanceof SigmaAssignment) {
			DefaultMutableTreeNode statement = new DefaultMutableTreeNode(
					((SigmaAssignment) s).object + " -> "
							+ ((SigmaAssignment) s).dataToAssign);
			n.add(statement);
			map.put(statement, (SigmaAssignment) s);
		} else if (s instanceof SigmaObject) {
			DefaultMutableTreeNode statement = new DefaultMutableTreeNode(
					"New: " + ((SigmaObject) s).name + " -> "
							+ ((SigmaObject) s).data);
			n.add(statement);
			map.put(statement, (SigmaElement) s);
		} else if (s instanceof SigmaIfElseStatement) {
			DefaultMutableTreeNode statement = new DefaultMutableTreeNode("If "
					+ ((SigmaIfElseStatement) s).condition.split(" == ")[0]
					+ " is "
					+ ((SigmaIfElseStatement) s).condition.split(" == ")[1]);
			DefaultMutableTreeNode thenBlock = new DefaultMutableTreeNode(
					"Then");
			for (SigmaStatement st : ((SigmaIfElseStatement) s).ifTrue.statements) {
				updateStatement(st, thenBlock);
			}
			DefaultMutableTreeNode elseBlock = new DefaultMutableTreeNode(
					"Else");
			for (SigmaStatement st : ((SigmaIfElseStatement) s).ifFalse.statements) {
				updateStatement(st, elseBlock);
			}
			map.put(thenBlock, ((SigmaIfElseStatement) s).ifTrue);
			map.put(elseBlock, ((SigmaIfElseStatement) s).ifFalse);
			statement.add(thenBlock);
			statement.add(elseBlock);
			n.add(statement);
		} else {
			DefaultMutableTreeNode statement = new DefaultMutableTreeNode(
					s.toString());
			n.add(statement);
			map.put(statement, (SigmaElement) s);
		}
	}
}
