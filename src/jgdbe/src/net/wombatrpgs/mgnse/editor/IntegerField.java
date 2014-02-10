/**
 *  IntegerPanel.java
 *  Created on Aug 14, 2012 1:33:25 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.lang.reflect.Field;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;

import net.wombatrpgs.mgns.core.Schema;
import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgnse.Global;

/**
 * An editor for standard integer objects.
 */
public class IntegerField extends FieldPanel {

	private static final long serialVersionUID = -4255466662658576049L;
	private JFormattedTextField input;

	/**
	 * Creates and initializes a new integer field editor.
	 * @param defaultData 	The integer this field should contain by default
	 * @param field 		The field to check for annotations and shit
	 * @param schema 		The data object to read/write values from
	 */
	public IntegerField(EditorPanel parent, Integer defaultData, Field field, Schema schema) {
		super(parent, field);
		input = new JFormattedTextField(NumberFormat.getIntegerInstance());
		if (defaultData != null) {
			input.setText(defaultData.toString());
		} else {
			if (source.isAnnotationPresent(DefaultValue.class)) {
				input.setText(source.getAnnotation(DefaultValue.class).value());
			}
		}
		input.setColumns(10);
		input.addPropertyChangeListener("value", this);
		checkMute(input);
		addConstrained(input);
	}

	@Override
	protected void copyTo(Schema s) {
		if (input.getText() == null || input.getText().equals("")) {
			// TODO: if we're doing validation on this field, do it here.
			input.setText("0");
		}
		try {
			source.set(s, Integer.valueOf(input.getText().replace(",","")));
		} catch (NumberFormatException e) {
			String ew = input.getText();
			System.out.println(ew);
			Global.instance().err("Our number formatting failed on " + input.getText(), e);
		} catch (Exception e) {
			Global.instance().err("There was some reflection fuckup??", e);
		}
	}

}
