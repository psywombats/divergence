/**
 *  FileField.java
 *  Created on Oct 10, 2012 9:18:47 PM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.lang.reflect.Field;

import javax.swing.JComboBox;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Schema;
import net.wombatrpgs.mgnse.Global;

/**
 * A panel that allows the user to select an enumerated value from a set list.
 * Enums don't have to be stored anywhere specific, but ideally they're in the
 * giant jar that makes up the rest of the schema.
 */
public class EnumField extends FieldPanel {

	private static final long serialVersionUID = -6709112266354782638L;
	private JComboBox<String> input;
	
	/**
	 * Creates and initializes a new enum field editor.
	 * @param parent 		The editor panel this panel is a part of
	 * @param defaultData 	The key that this field contained by default
	 * @param field 		The field to check for annotations and shit
	 */
	public EnumField(EditorPanel parent, Object defaultData, Field field) {
		super(parent, field);
		input = new JComboBox<String>();
		if (field.isAnnotationPresent(Nullable.class)) {
			input.addItem("None");
		}
		for (Object value : field.getType().getEnumConstants()) {
			input.addItem(value.toString());
		}
		if (defaultData == null || defaultData.equals("")) {
			if (field.isAnnotationPresent(DefaultValue.class)) {
				defaultData = field.getAnnotation(DefaultValue.class).value();
			}
		}
		if (defaultData != null && !defaultData.equals("")) {
			if (!selectString(input, defaultData.toString())) {
				Global.instance().warn("Default value " + defaultData + " not found on enum");
			}
		}
		input.addActionListener(this);
		input.addItemListener(this);
		input.addPropertyChangeListener("value", this);
		addConstrained(input);
		checkMute(input);
	}

	@Override
	protected void copyTo(Schema s) {
		try {
			source.set(s, null);
			for (Object value : source.getType().getEnumConstants()) {
				if (value.toString().equals(input.getSelectedItem().toString())) {
					source.set(s, value);
				}
			}
		} catch (Exception e) {
			Global.instance().err("There was some reflection fuckup??", e);
		}
	}

}
