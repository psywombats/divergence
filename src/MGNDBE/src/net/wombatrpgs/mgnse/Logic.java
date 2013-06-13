/**
 *  Logic.java
 *  Created on Aug 6, 2012 12:10:40 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;

import net.wombatrpgs.mgns.core.Annotations;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgnse.editor.EditorPanel;
import net.wombatrpgs.mgnse.io.ContextualFileLoader;
import net.wombatrpgs.mgnse.io.InputHandler;
import net.wombatrpgs.mgnse.io.OutputHandler;
import net.wombatrpgs.mgnse.schema.AppConfig;
import net.wombatrpgs.mgnse.schema.ProjectConfig;
import net.wombatrpgs.mgnse.tree.SchemaNode;
import net.wombatrpgs.mgnse.tree.SchemaTree;

/**
 * Handles all editor actions. Should be one per application.
 */
public class Logic {
	
	private HashMap<File, EditorPanel> dirtyEditors;
	private MainFrame parent;
	private EditorPanel currentEditor;
	private ProjectConfig projectConfig;
	private AppConfig appConfig;
	private SchemaTree tree;
	private JPanel editorPane;
	private InputHandler in;
	private OutputHandler out;
	private ContextualFileLoader cfl;
	
	/**
	 * Creates a new Logic object. It needs a parent to perform some basic i/o.
	 * @param parent The parent frame
	 * @param tree The tree this logic should work on
	 */
	public Logic(MainFrame parent, SchemaTree tree, JPanel contentPane) {
		this.in = new InputHandler(parent);
		this.out = new OutputHandler(parent);
		this.tree = tree;
		this.editorPane = contentPane;
		this.parent = parent;
		parent.setLogic(this);
		this.cfl = new ContextualFileLoader() {
			@Override
			public File getFile(String name) {
				File parentDir = out.getProjectConfigFile().getParentFile();
				return in.getFile(parentDir, name);
			}
		};
		initialize();
	}
	
	/**
	 * The contextual correct way to load a file!
	 * @param fileName		Name of the file or directory to load
	 * @return				That local file or directory
	 */
	public File loadFile(String fileName) {
		return cfl.getFile(fileName);
	}
	
	/**
	 * Called whenever the user indicates they want to quit or the close button
	 * is hit.
	 */
	public void onApplicationQuit() {
		if (hasUnsavedChanges()) {
			switch (parent.promptUnsaved()) {
			case 0:
				saveAll();
				break;
			case 1:
				break;
			case 2:
				return;
			}
		}
		out.writeAppConfig(appConfig);
		System.exit(0);
	}
	
	/**
	 * Called whenever the user wants to open a project. Delegate work!
	 */
	public void requestOpenProject() {
		out.setProjectConfigFile(in.requestProjectFile(out.getProjectConfigFile()));
		if (out.getProjectConfigFile() != null) {
			appConfig.projectFile = out.getProjectConfigFile().getAbsolutePath();
		} else {
			appConfig.projectFile = "";
		}
		loadProject();
	}
	
	/**
	 * Clears all the project~
	 */
	public void closeProject() {
		appConfig.projectFile = "";
		tree.clear();
	}
	
	/**
	 * Loads the last selected element in the tree into the editor.
	 */
	public void loadSelectedElement() {
		SchemaNode node = tree.getSelectedNode();
		if (node == null) {
			parent.setDeleteEnable(false);
			return;
		}
		parent.setDeleteEnable(true);
		EditorPanel newEditor;
		if (dirtyEditors.containsKey(node.getFile())) {
			newEditor = dirtyEditors.get(node.getFile());
		} else {
			MainSchema schema = in.instantiateData(node.getSchema(), node.getFile());
			newEditor = new EditorPanel(schema, node.getFile(), this);
			editorPane.add(newEditor, newEditor.genConstraints());
			dirtyEditors.put(node.getFile(), newEditor);
		}
		if (newEditor != currentEditor) {
			if (currentEditor != null) {
				currentEditor.setVisible(false);
				if (!currentEditor.isDirty()) {
					dirtyEditors.remove(currentEditor.getFile());
				}
			}
			newEditor.setVisible(true);
			currentEditor = newEditor;
			notifyDirty(currentEditor);
		}
		editorPane.revalidate();
	}
	
	/**
	 * Saves the currently active schema.
	 */
	public void save() {
		currentEditor.saveData(out);
	}
	
	/**
	 * Saves all edited schema.
	 */
	public void saveAll() {
		for (EditorPanel editor : dirtyEditors.values()) {
			if (editor.isDirty()) {
				editor.saveData(out);
			}
		}
		for (EditorPanel editor : dirtyEditors.values()) {
			if (editor != currentEditor) {
				dirtyEditors.remove(editor);
			}
		}
	}
	
	/**
	 * Reverts the currently selected schema.
	 */
	public void revert() {
		currentEditor.loadData();
		editorPane.revalidate();
	}
	
	/**
	 * Reverts all changed schema.
	 */
	public void revertAll() {
		for (EditorPanel panel : dirtyEditors.values()) {
			panel.loadData();
		}
		notifyDirty(currentEditor);
		editorPane.revalidate();
	}
	
	/**
	 * Called when any editor declares itself dirty or not dirty.
	 * @param notifier The panel that triggered the update
	 */
	public void notifyDirty(EditorPanel notifier) {
		if (currentEditor != notifier) return;
		parent.setSaveAllEnable(hasUnsavedChanges());
		parent.setSaveEnable(currentEditor.isDirty());
	}
	
	/**
	 * Prompts the user to create a new database entry of a type of their
	 * selection.
	 */
	public void newEntry() {
		ArrayList<SchemaChoice> itemList = new ArrayList<SchemaChoice>();
		SchemaChoice defaultChoice = null;
		for (Class<? extends MainSchema> schemaClass : tree.getSchema()) {
			String name = schemaClass.getSimpleName();
			if (schemaClass.isAnnotationPresent(Annotations.Path.class)) {
				name = schemaClass.getAnnotation(Annotations.Path.class).value() + name;
			}
			SchemaChoice choice = new SchemaChoice(schemaClass, name);
			if (tree.getSelectedClass() == schemaClass) {
				defaultChoice = choice;
			}
			itemList.add(choice);
		}
		SchemaChoice choice = parent.promptChooseSchema(itemList, defaultChoice);
		if (choice != null) {
			newEntry(choice.schema, null);
		}
	}
	
	/**
	 * Creates a new schema of the specified type.
	 * @param T the type of schema to create
	 * @param schema The type of schema to create
	 * @param source Starter data for the schema, null if none
	 */
	public void newEntry(Class<? extends MainSchema> schema, MainSchema source) {
		String key = parent.promptKey();
		if (key == null) return;
		if (key.equals("")) {
			parent.alert("Enter a non-empty key next time, okay?");
			newEntry(schema, source);
		}
		String subdir = parent.promptSubdir();
		if (subdir == null) return;
		MainSchema instance = null;
		if (source == null) {
			try {
				instance = schema.newInstance();
			} catch (InstantiationException e) {
				Global.instance().err("Couldn't instantiate " + schema, e);
			} catch (IllegalAccessException e) {
				Global.instance().err("Bad permissions for " + schema, e);
			}
		} else {
			instance = source;
		}
		String path = in.getFile(out.getProjectConfigFile().getParentFile(), 
				projectConfig.data).getAbsolutePath() + "\\";
		path += schema.getName().replace('.', '\\') + "\\";
		path += key+".json";
		instance.key = key;
		instance.subfolder = subdir;
		File file = new File(path);
		if (file.exists()) {
			// this should ensure unique key
			parent.alert("A similarly-named file already exists for that schema.");
			newEntry(schema, source);
			return;
		}
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} catch (IOException e) {
			Global.instance().err("Error creating " + file, e);
		}
		out.writeSchema(instance, file);
		SchemaNode node = tree.addDataEntry(file);
		tree.refresh();
		tree.selectNode(node);
		parent.setDeleteEnable(true);
	}
	
	/**
	 * Deletes the currently selected schema. Also clears it from the file
	 * system.
	 */
	public void deleteEntry() {
		SchemaNode node = tree.getSelectedNode();
		if (node == null) return;
		out.delete(node.getFile());
		SchemaNode parent = (SchemaNode) node.getParent();
		tree.deleteDataEntry(node);
		dirtyEditors.remove(currentEditor);
		currentEditor.setVisible(false);
		editorPane.revalidate();
		currentEditor = null;
		tree.selectNode(parent);
	}
	
	public ProjectConfig getConfig() {
		return projectConfig;
	}
	
	/**
	 * Makes a duplicate of the selected entry.
	 */
	public void cloneEntry() {
		SchemaNode selected = tree.getSelectedNode();
		Class<? extends MainSchema> clazz = tree.getSelectedClass();
		MainSchema source = in.instantiateData(selected.getSchema(), selected.getFile());
		MainSchema target = null;
		try {
			target = clazz.newInstance();
		} catch (InstantiationException e) {
			Global.instance().err("Bad clone", e);
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			Global.instance().err("Bad clone 2", e);
			e.printStackTrace();
			return;
		}
		for (Field f : clazz.getFields()) {
			try {
				f.set(target, f.get(source));
			} catch (Exception e) {
				e.printStackTrace();
				Global.instance().err("Cloned a bad field", e);
			}
		}
		newEntry(clazz, target);
	}
	
	public OutputHandler getOut() {
		return out;
	}
	
	public InputHandler getIn() {
		return in;
	}
	
	/**
	 * Sets up the logic to do its thing.
	 */
	private void initialize() {
		appConfig = in.parseAppConfig(out.getAppConfigFile());
		if (appConfig.projectFile.equals("")) {
			requestOpenProject();
		} else {
			out.setProjectConfigFile(new File(appConfig.projectFile));
			loadProject();
		}
	}
	
	/**
	 * Sets the schema tree to reflect current project configuration.
	 */
	private void refreshTree() {
		if (projectConfig == null) {
			tree.clear();
		} else {
			tree.setDataDir(cfl.getFile(projectConfig.data));
			tree.setSchemaJar(cfl.getFile(projectConfig.schema));
			tree.refreshSchema();
		}
	}
	
	/**
	 * Actually does the work behind loading the project from config file.
	 */
	private void loadProject() {
		dirtyEditors = new HashMap<File, EditorPanel>();
		projectConfig = in.parseProjectConfig(out.getProjectConfigFile());
		refreshTree();
	}
	
	/**
	 * Determines if the user made changes since last save.
	 * @return True if things have changed, false otherwise
	 */
	private boolean hasUnsavedChanges() {
		for (EditorPanel panel : dirtyEditors.values()) {
			if (panel.isDirty()) return true;
		}
		return false;
	}

}
