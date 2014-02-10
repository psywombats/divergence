/**
 *  MainFrame.java
 *  Created on Aug 5, 2012 11:29:59 PM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.swing.*;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgnse.tree.SchemaTree;
import net.wombatrpgs.mgnse.wizard.Wizard;


/**
 * Main frame of the MGNSE application.
 */
public class MainFrame extends JFrame {
	
	public static int APP_WIDTH = 960;
	public static int APP_HEIGHT = 640;

	/** some autogen shit */
	private static final long serialVersionUID = -6777603645539166111L;
	
	private SchemaTree tree;
	private JPanel editor;
	private Logic logic;
	private Listener in;
	private JMenu file, edit, wizard;
	private JMenuItem saveItem, saveAllItem, revertItem, revertAllItem, deleteItem, cloneItem;
	private JScrollPane treeScroll, editorScroll;
	private List<Wizard> allWizards;
	private JMenuBar bar;
	
	/**
	 * Set up the application... in practice all this does is set window title.
	 */
	public MainFrame() {
		super("JG Database Editor");
	}
	
	/**
	 * Sets up and launches this frame.
	 */
	public void launchGUI() {
		
		initLookAndFeel();
		initLogic();
		initFrame();
		initInput();
		initMenu();
		initFinish();
	}
	
	/** @param enabled True to enable save menu option, false otherwise */
	public void setSaveEnable(boolean enabled) {
		saveItem.setEnabled(enabled);
		revertItem.setEnabled(enabled);
	}
	
	/** @param enabled True to enable save all menu option, false otherwise */
	public void setSaveAllEnable(boolean enabled) {
		saveAllItem.setEnabled(enabled);
		revertAllItem.setEnabled(enabled);
	}
	
	/** @param enabled True if the delete menu should be enabled, else false */
	public void setDeleteEnable(boolean enabled) {
		deleteItem.setEnabled(enabled);
	}
	
	/** @return All associated wizards for database */
	public List<? extends Wizard> getWizards() {
		return allWizards;
	}
	
	/**
	 * Sets up the actual application area with UI elements.
	 */
	private void initFrame() {
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		
		int treeHorizPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int treeVertPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		
		treeScroll = new JScrollPane(tree, treeHorizPolicy, treeVertPolicy);
		treeScroll.setMinimumSize(new Dimension(APP_WIDTH / 4, APP_HEIGHT));
		treeScroll.setPreferredSize(new Dimension(APP_WIDTH / 4, APP_HEIGHT));
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.VERTICAL;
		add(treeScroll, c);
		
		int editorHorizPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int editorVertPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		editorScroll = new JScrollPane(editor, editorHorizPolicy, editorVertPolicy);
		editorScroll.setPreferredSize(new Dimension(APP_WIDTH * 3 /4, APP_HEIGHT));
		editorScroll.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		editorScroll.setAlignmentY(JComponent.TOP_ALIGNMENT);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weighty = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.ipady = 10;
		add(editorScroll, c);
	}
	
	/**
	 * Sets up the menu bar and hooks them into the input.
	 */
	private void initMenu() {
		
		bar = new JMenuBar();
		
		file = new JMenu("File");
		file.add(createMenuItem(Action.OPEN_PROJECT));
		file.add(createMenuItem(Action.CLOSE_PROJECT));
		file.addSeparator();
		saveItem = createMenuItem(Action.SAVE);
		file.add(saveItem);
		saveAllItem = createMenuItem(Action.SAVE_ALL);
		file.add(saveAllItem);
		file.addSeparator();
		file.add(createMenuItem(Action.QUIT_APP));
		bar.add(file);
		
		edit = new JMenu("Edit");
		edit.add(createMenuItem(Action.NEW_ENTRY));
		revertItem = createMenuItem(Action.REVERT);
		edit.add(revertItem);
		revertAllItem = createMenuItem(Action.REVERT_ALL);
		edit.add(revertAllItem);
		deleteItem = createMenuItem(Action.DELETE_ENTRY);
		edit.add(deleteItem);
		cloneItem = createMenuItem(Action.CLONE_ENTRY);
		edit.add(cloneItem);
		bar.add(edit);
		
		// TODO: dbe: do this each time a project is loaded
		loadAllWizards();
		
		setSaveAllEnable(false);
		setSaveEnable(false);
		setDeleteEnable(false);
		
		setJMenuBar(bar);
	}
	
	/**
	 * Sets the appropriate look and feel of the frame.
	 */
	private void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Completes setup and displays the frame.
	 */
	private void initFinish() {
		pack();
		setVisible(true);
	}
	
	/**
	 * Initializes all input listeners.
	 */
	private void initInput() {
		in = new Listener(this, logic);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				in.onWindowClose(we);
			}
		});
		tree.addTreeSelectionListener(in);
	}
	
	/**
	 * Starts the logic and tree.
	 */
	private void initLogic() {
		tree = new SchemaTree();
		editor = new JPanel();
		logic = new Logic(this, tree, editor);
	}
	
	/**
	 * Creates a new menu item for use in this guy's menus. It's like a mini-
	 * factory!
	 * @param a The action that this menu item triggers
	 * @return A new menu item!
	 */
	private JMenuItem createMenuItem(Action a) {
		JMenuItem item = new JMenuItem(a.toString());
		if (a.shortcutExists()) {
			KeyStroke key;
			if (a.maskExists()) {
				key = KeyStroke.getKeyStroke(a.getVK(), ActionEvent.CTRL_MASK + a.getMask());
			} else {
				key = KeyStroke.getKeyStroke(a.getVK(), ActionEvent.CTRL_MASK);
			}
			item.setAccelerator(key);
			item.setMnemonic(a.getVK());
		}
		item.addActionListener(in);
		return item;
	}
	
	private JMenuItem createMenuItem(Wizard w) {
		JMenuItem item = new JMenuItem(w.getName());
		item.addActionListener(in);
		return item;
	}
	
	/**
	 * Asks the user if they REALLY WANT TO QUIT. Returns the result of their
	 * choice with a default message box.
	 * @return User's selection: 0 is yes, 1 is no, 2 is cancel
	 */
	public int promptUnsaved() {
		Object[] options = {"Yes", "Don't Save", "Cancel"};
		return JOptionPane.showOptionDialog(this,
				"There are database entries with unsaved changes.", 
				"Really quit?",
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null, options, options[0]);
	}
	
	/**
	 * Prompts the user to pick from a list of schema.
	 * @param itemList All the schema classes to choose from, in choice format
	 * for sorting reasons and stuff
	 * @param defaultChoice The choice selected by default
	 * @return Which one the user chose, null if canceled
	 */
	public SchemaChoice promptChooseSchema(ArrayList<SchemaChoice> itemList,
			SchemaChoice defaultChoice) {
		if (defaultChoice == null) {
			defaultChoice = itemList.get(0);
		}
		SchemaChoice choice = (SchemaChoice) JOptionPane.showInputDialog(
				this,
				"Select a schema for the new database entry:",
                "New Entry...",
                JOptionPane.PLAIN_MESSAGE,
                null,
                itemList.toArray(),
                defaultChoice);
		return choice;
	}
	
	/**
	 * Prompts the user to enter a key for their new entry.
	 * @return The key the user enetered, null if canceled
	 */
	public String promptKey() {
		String choice = (String) JOptionPane.showInputDialog(
				this,
				"Enter the unique key of this entry",
				"New Entry...",
				JOptionPane.PLAIN_MESSAGE,
				null, null, null);
		return choice;
	}
	
	/**
	 * Prompts the user to enter a key for their new entry.
	 * @return The subdir the user entered, null if nothing
	 */
	public String promptSubdir() {
		String choice = (String) JOptionPane.showInputDialog(
				this,
				"Enter a subdirectory, or just press OK\n" +
				"(a subdirectory sorts entries, like \"bosses/\" for instance",
				"New Entry...",
				JOptionPane.PLAIN_MESSAGE,
				null, null, null);
		return choice;
	}
	
	/**
	 * Simple popup factory. Creates an alert dialog with the specified string.
	 * @param msg The message to display in the popup
	 */
	public void alert(String msg) {
		JOptionPane.showMessageDialog(this,
				msg,
				"Error",
				JOptionPane.ERROR_MESSAGE);
	}
	
	public Logic getLogic() {
		return logic;
	}
	
	public void setLogic(Logic logic) {
		this.logic = logic;
	}
	
	public void loadAllWizards() {
		allWizards = new ArrayList<Wizard>();
		
		for (String filename : getLogic().getConfig().schema) {
			loadWizards(filename);
		}
	}
	
	public void loadWizards(String filename) {
		
		// get the jar
		JarInputStream stream = null;
		File f = getLogic().loadFile(filename);
		try {
			stream = new JarInputStream(new FileInputStream(f));
		} catch (Exception e) {
			Global.instance().err("Couldn't find plugin jar " + f, e);
		}
		
		// init the classloader
		URL pluginURL = null;
		ClassLoader cl = null;
		try {
			pluginURL = f.toURI().toURL();
			cl = new URLClassLoader(new URL[] { pluginURL }, MainSchema.class.getClassLoader());
		} catch (MalformedURLException e1) {
			Global.instance().err("Malformed url " + pluginURL, e1);
		}
		
		// load the wizards
		while (true) {
			JarEntry entry = null;
			try {
				entry = stream.getNextJarEntry();
			} catch (IOException e) {
				Global.instance().err("Error reading a plugin class", e);
			}
			if (entry == null) break;
			if (entry.getName().endsWith(".class")) {
				String className = SchemaTree.fileUrlToBinaryName(entry.getName());
				Global.instance().debug("Loading a plugin " + className);
				try {
					Class<?> rawClass = cl.loadClass(className);
					if (!Wizard.class.isAssignableFrom(rawClass)) {
						Global.instance().debug("Class doesn't extend plugin: " + rawClass);
					} else {
						Wizard w = (Wizard) rawClass.newInstance();
						w.setFrame(this);
						allWizards.add(w);
					}
				} catch (ClassNotFoundException e) {
					Global.instance().err("Couldn't find a class?" + className, e);
				} catch (Exception e) {
					Global.instance().err("Bad instantiation " + className, e);
				}
			} else {
				Global.instance().debug("Found a non-schema file: " + entry.getName());
			}
		}
		
		if (wizard != null) bar.remove(wizard);
		wizard = new JMenu("Wizards");
		for (Wizard w : allWizards) {
			JMenuItem item = createMenuItem(w);
			wizard.add(item);
		}
		bar.add(wizard);
		repaint();
	}

}
