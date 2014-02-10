/**
 *  RefereceArrayField.java
 *  Created on Oct 11, 2012 11:38:25 PM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.JComboBox;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.Schema;
import net.wombatrpgs.mgnse.Global;
import net.wombatrpgs.mgnse.tree.SchemaNode;

/**
 * A field for editting an array of references only. It's not as generic as it
 * should be but it should handle its job. There are other versions for generic
 * in-line stuff and primitives.
 */
public class ReferenceArrayField extends ArrayField<JComboBox<String>> {

	private static final long serialVersionUID = -2192741043903713950L;
	
	protected JComboBox<String> input;

	public ReferenceArrayField(EditorPanel parent, String[] defaultData, Field field) {
		super(parent, field, true);
		
		if (defaultData != null) {
			for (String s : defaultData) {
				JComboBox<String> input = genInput();
				if (!selectString(input, s)) {
					Global.instance().warn("No object with key " + s + " for reference array");
				}
				addInput(input);
			}
		}
	}
	
	@Override
	protected void copyTo(Schema s) {
		String[] keys = new String[inputs.size()];
		for (int i = 0; i < inputs.size(); i++) {
			keys[i] = (String) inputs.get(i).getSelectedItem();
		}
		try {
			source.set(s, keys);
		} catch (Exception e) {
			Global.instance().err("There was some reflection fuckup??", e);
		}
	}
	
	/**
	 * Generates an input field.
	 * @return			The generated input
	 */
	@Override
	protected JComboBox<String> genInput() {
		Class<? extends MainSchema> schema = source.getAnnotation(SchemaLink.class).value();
		ArrayList<SchemaNode> nodes = Global.instance().getImplementers(schema);
		input = new JComboBox<String>();
		for (SchemaNode node : nodes) {
			for (int i = 0; i < node.getChildCount(); i++) {
				recursivelyAdd((SchemaNode) node.getChildAt(i));
			}
		}
		return input;
	}
	
	protected void recursivelyAdd(SchemaNode node) {
		if (node.isLeaf()) {
			input.addItem(node.getObjectName());
		} else {
			for (int i = 0; i < node.getChildCount(); i++) {
				recursivelyAdd((SchemaNode) node.getChildAt(i));
			}
		}
	}

}
