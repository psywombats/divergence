/**
 *  StringFrame.java
 *  Created on Aug 12, 2012 4:50:04 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.lang.reflect.Field;

import javax.swing.JTextField;

import net.wombatrpgs.mgns.core.Schema;
import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgnse.Global;

/**
 * An editor field for strings.
 */
public class StringField extends FieldPanel {

	private static final long serialVersionUID = -779438681951462976L;
	private JTextField input;
	
	/**
	 * Creates and initializes a new string field editor.
	 * @param defaultData The string this field should contain by default
	 * @param field The field to check for annotations and shit
	 * @param schema The data object to read/write values from
	 */
	public StringField(EditorPanel parent, String defaultData, Field field, Schema schema) {
		super(parent, field);
		//TODO: alter this so that the editor correctly consumes screen space
		input = new JTextField(75);
		if (defaultData == null && source.isAnnotationPresent(DefaultValue.class)) {
			defaultData = source.getAnnotation(DefaultValue.class).value();
		}
		input.setText(defaultData);
		input.getDocument().addDocumentListener(this);
		addConstrained(input);
		checkMute(input);
	}
	
	@Override
	protected void copyTo(Schema s) {
		try {
			source.set(s, input.getText());
		} catch (IllegalArgumentException e) {
			Global.instance().err("Bad argument: " + input.getText(), e);
		} catch (IllegalAccessException e) {
			Global.instance().err("Bad access: " + source, e);
		}
	}

}
