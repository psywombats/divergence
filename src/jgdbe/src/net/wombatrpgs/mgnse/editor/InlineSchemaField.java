/**
 *  InlineSchemaField.java
 *  Created on Aug 20, 2013 7:39:30 PM for project MGNDBE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.lang.reflect.Field;

import net.wombatrpgs.mgns.core.Schema;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgnse.Global;

/**
 * An array of in-line schema. Example use case: monster attacks. They have a
 * priority and condition etc but they're never made separate from a monster. So
 * you need one of these to link to them.
 */
public class InlineSchemaField extends FieldPanel {
	
	private static final long serialVersionUID = 1772154060584558239L;
	
	protected EditorPanel input;

	public InlineSchemaField(EditorPanel parent, Schema defaultData, Field field) {
		super(parent, field);
		if (defaultData != null) {
			input = new EditorPanel(defaultData, null, parent.getLogic(), false);
		} else {
			try {
				Schema newSchema = (Schema) field.getType().newInstance();
				input = new EditorPanel(newSchema, null, parent.getLogic(), false);
			} catch (Exception e) {
				Global.instance().err("Bad field", e);
			}
		}
		input.setDirtyListener(this);
		checkMute(input);
		addConstrained(input);
	}

	@Override
	protected void copyTo(Schema s) {
		Class<? extends Schema> clazz = source.getAnnotation(InlineSchema.class).value();
		Schema schema;
		try {
			schema = clazz.newInstance();
			input.copyTo(schema);
			try {
				source.set(s, schema);
			} catch (Exception e) {
				Global.instance().err("Reflection fail'd", e);
			}
		} catch (Exception e) {
			Global.instance().err("Instantiation exception inline", e);
		}
	}

}
