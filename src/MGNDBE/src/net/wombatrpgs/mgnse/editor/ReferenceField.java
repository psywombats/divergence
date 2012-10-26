/**
 *  ReferencePanel.java
 *  Created on Aug 18, 2012 9:18:47 PM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.lang.reflect.Field;

import javax.swing.JComboBox;

import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Schema;
import net.wombatrpgs.mgnse.Global;
import net.wombatrpgs.mgnse.tree.SchemaNode;

/**
 * A panel that allows the user to select another data entry.
 */
public class ReferenceField extends FieldPanel {

	private static final long serialVersionUID = 4140236210558060162L;
	private JComboBox<String> input;
	
	/**
	 * Creates and initializes a new reference field editor.
	 * @param parent 		The editor panel this panel is a part of
	 * @param defaultData 	The key that this field contained by default
	 * @param field 		The field to check for annotations and shit
	 */
	public ReferenceField(EditorPanel parent, String defaultData, Field field) {
		super(parent, field);
		Class<? extends MainSchema> schema = field.getAnnotation(SchemaLink.class).value();
		SchemaNode node = Global.instance().getSchemaMap().get(schema);
		input = new JComboBox<String>();
		if (source.isAnnotationPresent(Nullable.class)) {
			input.addItem("None");
		}
		for (int i = 0; i < node.getChildCount(); i++) {
			input.addItem(((SchemaNode) node.getChildAt(i)).getObjectName());
		}
		if (defaultData != null && !defaultData.equals("")) {
			// TODO: verify it exists
			selectString(input, defaultData);
		} else {
			selectString(input, "None");
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

}
