/**
 *  FileField.java
 *  Created on Oct 10, 2012 9:18:47 PM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.io.File;
import java.lang.reflect.Field;

import javax.swing.JComboBox;

import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Schema;
import net.wombatrpgs.mgnse.Global;

/**
 * A panel that allows the user to select a file from a subdirectory of the
 * project file. The directory is specified by an annotation on the field and is
 * local to the project directory.
 */
public class FileField extends FieldPanel {

	private static final long serialVersionUID = -6709112266354782638L;
	private JComboBox<String> input;
	
	/**
	 * Creates and initializes a new reference field editor.
	 * @param parent 		The editor panel this panel is a part of
	 * @param defaultData 	The key that this field contained by default
	 * @param field 		The field to check for annotations and shit
	 */
	public FileField(EditorPanel parent, String defaultData, Field field) {
		super(parent, field);
		String directoryName = field.getAnnotation(FileLink.class).value();
		File dir = parent.loadFile(directoryName);
		if (dir == null || dir.isFile()) {
			Global.instance().err(dir + " is not a directory", new Exception());
		}
		input = new JComboBox<String>();
		if (source.isAnnotationPresent(Nullable.class)) {
			input.addItem("None");
		}
		for (File f : dir.listFiles()) {
			if (f.isFile()) {
				input.addItem(f.getName());
			}
		}
		if (defaultData != null && !defaultData.equals("")) {
			if (!selectString(defaultData)) {
				Global.instance().warn("Default data " + defaultData + " not found on file");
			}
		} else {
			if (!selectString("None")) {
				Global.instance().warn("No default data, but field is not nullable");
			}
		}
		input.addActionListener(this);
		addConstrained(input);
		checkMute(input);
	}

	@Override
	protected void copyTo(Schema s) {
		try {
			source.set(s, input.getSelectedItem());
		} catch (Exception e) {
			Global.instance().err("There was some reflection fuckup??", e);
		}
	}
	
	/**
	 * Selects a string in the drop-down list.
	 * @param s		The string to select
	 * @return		True if selected, false otherwise
	 */
	protected boolean selectString(String s) {
		for (int i = 0; i < input.getItemCount(); i++) {
			if (s.equals(input.getItemAt(i))) {
				input.setSelectedIndex(i);
				return true;
			}
		}
		return false;
	}

}
