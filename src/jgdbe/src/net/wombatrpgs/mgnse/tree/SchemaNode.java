/**
 *  FileNode.java
 *  Created on Aug 12, 2012 12:33:30 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.tree;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

import net.wombatrpgs.mgns.core.MainSchema;

/**
 * A tree node with a file user object. Simple enough, right?
 */
public class SchemaNode extends DefaultMutableTreeNode {
	
	private File object;
	private String name;
	private Class<? extends MainSchema> schema;

	/** some generated shit */
	private static final long serialVersionUID = 1605159624632252852L;
	
	/**
	 * Creates a new file node that represents a data object
	 * @param file The file this node represents
	 * @param schema The class that this file fills
	 * tree system.
	 */
	public SchemaNode(File file, Class<? extends MainSchema> schema) {
		this.object = file;
		this.schema = schema;
		setAllowsChildren(false);
	}
	
	/**
	 * Creates a pseudo-node that instead of representing a file, just prints
	 * out a string
	 * @param name The string to print out
	 */
	public SchemaNode(String name) {
		this.name = name;
		setAllowsChildren(true);
	}
	
	/** @return The file this object represents */
	public File getFile() { return object; }
	
	/** @return The schema class of the file */
	public Class<? extends MainSchema> getSchema() { return schema; }
	
	/** @param schema The new schema for the node */
	public void setSchema(Class<? extends MainSchema> schema) { this.schema = schema; }
	
	@Override
	public boolean isLeaf() {
		return !getAllowsChildren();
	}
	
	/** @return The name of the file or key of the node */
	public String getName() {
		if (object != null) {
			return object.getName();
		} else {
			return name;
		}
	}
	
	/** @return The human-name of the represented object */
	public String getObjectName() {
		// TODO: dbe: get this working correctly for fuck's sake
		// right now it just returns the filename minus the .json
		if (getName().contains(".")) {
			return getName().substring(0, getName().indexOf('.'));
		} else {
			return getName();
		}
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
