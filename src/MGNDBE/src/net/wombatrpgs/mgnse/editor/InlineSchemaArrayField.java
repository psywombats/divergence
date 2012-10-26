/**
 *  InlineSchemaArrayField.java
 *  Created on Oct 16, 2012 5:39:34 PM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.mgns.core.Schema;
import net.wombatrpgs.mgnse.Global;

/**
 * An array of in-line schema. Example use case: monster attacks. They have a
 * priority and condition etc but they're never made separate from a monster. So
 * you need one of these to link to them.
 */
public class InlineSchemaArrayField extends ArrayField<EditorPanel> {

	private static final long serialVersionUID = -1273141915845389992L;

	public InlineSchemaArrayField(EditorPanel parent, Schema[] defaultData, Field field) {
		super(parent, field, false);
		
		if (defaultData != null) {
			for (Schema s : defaultData) {
				EditorPanel input = genInput(s);
				addInput(input);
			}
		}
	}

	@Override
	protected EditorPanel genInput() {
		try {
			HeadlessSchema s = source.getAnnotation(InlineSchema.class).value().newInstance();
			return genInput(s);
		} catch (InstantiationException e) {
			Global.instance().err("Coulnd't make a new schema for field " + source, e);
		} catch (IllegalAccessException e) {
			Global.instance().err("Bad access for new schema for field " + source, e);
		}
		return null;
	}
	
	/**
	 * Generates an editor panel to edit the a schema of the specified type.
	 * @param s		The type of schema to work with
	 * @return		A new editor panel to work with it
	 */
	protected EditorPanel genInput(Schema s) {
		EditorPanel subpanel = new EditorPanel(s, null, parent.getLogic());
		return subpanel;
	}

	@Override
	protected void copyTo(Schema s) {
		Class<? extends Schema> clazz = source.getAnnotation(InlineSchema.class).value();
		Object schema = Array.newInstance(clazz, inputs.size());
		for (int i = 0; i < inputs.size(); i++) {
			Array.set(schema, i, clazz.cast(inputs.get(i).getSchema()));
			inputs.get(i).copyTo(clazz.cast(Array.get(schema, i)));
		}
		try {
			source.set(s, schema);
		} catch (Exception e) {
			Global.instance().err("Reflection fail'd", e);
		}
	}

}
