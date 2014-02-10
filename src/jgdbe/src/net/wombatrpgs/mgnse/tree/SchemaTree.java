/**
 *  SchemaTree.java
 *  Created on Aug 8, 2012 4:34:08 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.tree;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;

import net.wombatrpgs.mgns.core.Annotations;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgnse.Global;
import net.wombatrpgs.mgnse.exception.DatabaseEntrySchemaException;
import net.wombatrpgs.mgnse.exception.MisplacedDatabaseEntryException;

/**
 * A panel for reprsenting schema and database entries. Extends JTree so you can
 * just embed it wherever provided you give it the right shit.
 */
public class SchemaTree extends JTree {
	
	/** true to to make the meta-subdirs show up in the filesystem */
	private static boolean filesystemReflectsSubdirs = false;
	/** some generated shit */
	private static final long serialVersionUID = -7357798094174051382L;
	
	/** The .jar containing the schema */
	private File schemaJar;
	/** The directory containing the database entries */
	private File dataDir;
	/** All the schema class files */
	private ArrayList<Class<? extends MainSchema>> schema;
	/** Maps schema to their respective nodes */
	private HashMap<Class<? extends MainSchema>, SchemaNode> map;
	/** The providing data structure */
	private SchemaNode tree;
	/** load dem classes */
	private URLClassLoader cl;
	/** Model setup for the tree */
	private DefaultTreeModel model;
	
	/** @param dataDir The new location of the database entries */
	public void setDataDir(File dataDir) { this.dataDir = dataDir; }
	/** @param schemaFile The new (jar) schema file */
	public void setSchemaJar(File schemaFile) { this.schemaJar = schemaFile; }
	/** @return All known schema types */
	public ArrayList<Class<? extends MainSchema>> getSchema() { return schema; }
	
	/**
	 * Creates a new empty tree.
	 */
	public SchemaTree() {
		super();
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.setCellRenderer(new DefaultTreeCellRenderer() {
			private static final long serialVersionUID = 6136933935626442828L;
			@Override
			public Component getTreeCellRendererComponent( JTree tree, Object value, boolean sel,
					boolean expanded, boolean leaf, int row, boolean hasFocus) {
			    return super.getTreeCellRendererComponent(tree, value.toString(), sel, expanded,
			    		leaf, row, hasFocus);
			}

		});
	}
	
	/**
	 * Gets the locally displayed user-facing path of a schema from its
	 * annotations
	 * @param schemaClass The schema class
	 * @return The path to where the schema should be displayed
	 */
	public static String getSchemaDisplayPath(Class<? extends MainSchema> schemaClass) {
		String path = "";
		if (schemaClass.isAnnotationPresent(Annotations.Path.class)) {
			path += schemaClass.getAnnotation(Annotations.Path.class).value();
		}
		path += schemaClass.getSimpleName();
		return path;
	}
	
	/**
	 * Updates the contents of the tree to reflect the file system.
	 */
	@SuppressWarnings("unchecked")
	public void refreshSchema() {
		
		// get the jar
		JarInputStream stream = null;
		try {
			stream = new JarInputStream(new FileInputStream(schemaJar));
		} catch (FileNotFoundException e) {
			Global.instance().err("Couldn't find schema jar " + schemaJar, e);
		} catch (IOException e) {
			Global.instance().err("Couldn't read schema jar " + schemaJar, e);
		}
		
		// init the classloader
		URL schemaURL = null;
		try {
			schemaURL = schemaJar.toURI().toURL();
			cl = new URLClassLoader(new URL[] { schemaURL }, MainSchema.class.getClassLoader());
		} catch (MalformedURLException e1) {
			Global.instance().err("Malformed url " + schemaURL, e1);
		}
		
		// get all the classes
		schema = new ArrayList<Class<? extends MainSchema>>();
		while (true) {
			JarEntry entry = null;
			try {
				entry = stream.getNextJarEntry();
			} catch (IOException e) {
				Global.instance().err("Error reading a schema class", e);
			}
			if (entry == null) break;
			if (entry.getName().endsWith(".class")) {
				String className = fileUrlToBinaryName(entry.getName());
				Global.instance().debug("Loading a " + className);
				try {
					Class<?> rawClass = cl.loadClass(className);
					if (!MainSchema.class.isAssignableFrom(rawClass)) {
						Global.instance().debug("Class doesn't extend main schema: " + rawClass);
					} else {
						schema.add((Class<? extends MainSchema>) rawClass);
					}
				} catch (ClassNotFoundException e) {
					Global.instance().err("Couldn't find a class?" + className, e);
				}
			} else {
				Global.instance().debug("Found a non-schema file: " + entry.getName());
			}
		}
		Global.instance().setSchema(schema);
		
		// set up the tree
		tree = new SchemaNode("Schema");
		this.setModel(new DefaultTreeModel(tree));
		map = new HashMap<Class<? extends MainSchema>, SchemaNode>();
		for (Class<? extends MainSchema> schemaClass : schema) {
			if (schemaClass.isAnnotationPresent(ExcludeFromTree.class)){
				Global.instance().debug("Ignored schema file due to exclude " + schemaClass);
			} else {
				map.put(schemaClass, getNodeByPath(tree,
						getSchemaDisplayPath(schemaClass), schemaClass));
			}
		}
		this.expandPath(new TreePath(tree.getPath()));
		Global.instance().setSchemaMap(map);
		
		// populate
		refreshData();
	}
	
	/**
	 * Loads in all the database entries from the database!
	 */
	public void refreshData() {
		loadData(dataDir);
	}
	
	/**
	 * Clears the data model for empty projects.
	 */
	public void clear() {
		setModel(new DefaultTreeModel(new SchemaNode("No Project")));
	}
	
	/**
	 * Returns the selected data node. This will return null if nothing or
	 * schema is selected.
	 * @return The last selected data, null if none
	 */
	public SchemaNode getSelectedNode() {
		SchemaNode node = (SchemaNode) getLastSelectedPathComponent();
		if (node == null) return null;
		if (node.getFile() == null) return null;
		return node;
	}
	
	/**
	 * Returns the selected class. The selection could be either an individual
	 * schema or a lot of schema but this returns their class either way. Could
	 * return null if a bigger folder is selected.
	 * @return The currently selected class in the tree
	 */
	public Class<? extends MainSchema> getSelectedClass() {
		SchemaNode node = (SchemaNode) getLastSelectedPathComponent();
		if (node == null) return null;
		return node.getSchema();
	}
	
	/**
	 * Adds a single data object. Does not auto-validate.
	 * @param data The data file to add from
	 * @return The node that was created
	 */
	public SchemaNode addDataEntry(File data) {
		Class<? extends MainSchema> schema = getSchemaByFile(data);
		SchemaNode parent = getNodeByPath(tree, getSchemaDisplayPath(schema), null);
		if (parent == null) {
			Global.instance().err("Couldn't find schema for " + data.getName(),
					new DatabaseEntrySchemaException(data.getName()));
		}
		parent = getNodeByPath(parent, getDataSubdir(data), schema);
		SchemaNode node = new SchemaNode(data, schema);
		if (!schema.isAnnotationPresent(ExcludeFromTree.class)) {
			parent.add(node);
		} else {
			Global.instance().debug("Ignored due to annotation: " + schema);
		}
		Global.instance().debug("Loaded object " + data.getName() + " as a " + schema.getName());
		return node;
	}
	
	/**
	 * Deletes a schema node from the tree. Auto-revalidates.
	 * @param node The node to delete
	 */
	public void deleteDataEntry(SchemaNode node) {
		SchemaNode parent = (SchemaNode) node.getParent();
		parent.remove(node);
		refresh();
	}
	
	/**
	 * Selects the node in the tree. Assumes the node already exists in the
	 * tree.
	 * @param node The node to select
	 */
	public void selectNode(TreeNode node) {
		List<TreeNode> list = new ArrayList<TreeNode>();
	    while (node != null) {
	    	list.add(node);
	        node = node.getParent();
	    }
	    Collections.reverse(list); 
	    this.setSelectionPath(new TreePath(list.toArray()));
	}
	
	/**
	 * Call whenever nodes added/deleted.
	 */
	public void refresh() {
		model.reload();
	}
	
	/**
	 * Converts a file URL to java class name.
	 * @param fileURL The URL to the file
	 * @return The qualified class name of the file
	 */
	public static String fileUrlToBinaryName(String fileURL) {
		String url = new String(fileURL);
		if (url.endsWith(".class")) {
			url = url.substring(0, url.length() - 6);
		} else {
			Global.instance().warn("Bad class conversion? " + fileURL);
		}
		url = url.replace("/", ".");
		return url;
	}
	
	/**
	 * Gets a schema node from the internal tree.
	 * @param base Base node to look from
	 * @param path The path to the required element (display, not file/class)
	 * @param creationClass The class to use to create new nodes, null if no new
	 * nodes should be created
	 */
	private SchemaNode getNodeByPath(SchemaNode base, String path, 
			Class<? extends MainSchema> creationClass) {
		if (path.equals("")) return base;
		String[] pathParts = path.split("/");
		for (String part : pathParts) {
			SchemaNode child = null;
			for (int j = 0; j < base.getChildCount(); j++) {
				SchemaNode testChild = (SchemaNode) base.getChildAt(j);
				if (testChild.getName().equals(part)) {
					child = testChild;
					break;
				}
			}
			if (child == null) {
				if (creationClass != null) {
					child = new SchemaNode(part);
					child.setSchema(creationClass);
					base.add(child);
				} else {
					return null;
				}
			}
			base = child;
		}
		return base;
	}
	
	/**
	 * Gets the actual schema for a data object based on its file location.
	 * @param dataFile The data file to get the schema for
	 * @return The class of schema of the data
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends MainSchema> getSchemaByFile(File dataFile) {
		String rootPath = dataDir.getAbsolutePath();
		String ourPath = getMainSchemaDirectory(dataFile).getAbsolutePath();
		String relativePath = ourPath.substring(rootPath.length(), ourPath.length());
		String className = relativePath.replace('\\', '.');
		className = className.replace('/', '.');
		if (className.startsWith(".")) className = className.substring(1);
		try {
			Class<?> rawClass = cl.loadClass(className);
			if (!MainSchema.class.isAssignableFrom(rawClass)) {
				Global.instance().warn("Loaded class " + rawClass + " didn't extend base schema");
			}
			return (Class<? extends MainSchema>) rawClass;
		} catch (ClassNotFoundException e) {
			Global.instance().err("Couldn't find a class " + className, e);
			return null;
		}
	}
	
	/**
	 * Gets the directory corresponding to this file's schema. For instance, if
	 * this data file was a monster (even Path("/monster/bosses")), this would
	 * return net/wombatrpgs/schema/monster directory.
	 * @param dataFile The file to get the schema directory of
	 * @return The root schema directory for the data file
	 */
	private File getMainSchemaDirectory(File dataFile) {
		String subdir = getDataSubdir(dataFile);
		if (subdir.equals("")) {
			return dataFile.getParentFile();
		} else {
			String path = dataFile.getParentFile().getAbsolutePath();
			if (filesystemReflectsSubdirs) {
				String[] subdirParts = subdir.split("/");
				for (int i = 0; i < subdirParts.length; i++) {
					String part = subdirParts[subdirParts.length - i - 1];
					int index = path.indexOf(part);
					if (index == -1) {
						String msg = "Bad subdir " + subdir;
						Global.instance().err(subdir, new MisplacedDatabaseEntryException(msg));
					} else {
						path = path.substring(0, index);
					}
				}
			}
			return new File(path);
		}
	}
	
	/**
	 * Get the subdirectory this data entry is located at within its parent
	 * schema directory.
	 * @param dataFile The file with the data to read
	 * @return The path this file should be in local to the schema
	 */
	private String getDataSubdir(File dataFile) {
		try {
			Global.instance().mapper().configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			MainSchema base = Global.instance().mapper().readValue(dataFile, MainSchema.class);
			Global.instance().mapper().configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			return base.subfolder;
		} catch (JsonParseException e) {
			Global.instance().err("Malformed base schema " + dataFile, e);
		} catch (JsonMappingException e) {
			Global.instance().err("Doesn't match base schema " + dataFile, e);
		} catch (IOException e) {
			Global.instance().err("Can't read base " + dataFile, e);
		}
		return null;
	}
	
	/**
	 * Loads all database entries from the directory. Recursive.
	 * @param dir The directory to start loading from
	 */
	private void loadData(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadData(file);
			} else {
				addDataEntry(file);
			}
		}
		model = new DefaultTreeModel(tree);
		this.setModel(model);
	}

}
