/**
 *  EnumSetField.java
 *  Created on Apr 1, 2014 1:32:22 PM for project jgdbe
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import net.wombatrpgs.mgns.core.Schema;
import net.wombatrpgs.mgnse.Global;

/**
 * Represents a set of enums as an array. To the user it's checkboxes.
 */
public class EnumSetField extends FieldPanel {
	
	private static final long serialVersionUID = -8982149974112983109L;
	
	protected JPanel boxPanel;
	protected List<JCheckBox> boxes;

	public EnumSetField(EditorPanel parent, Object data, Field field) {
		super(parent, field);
		boxes = new ArrayList<JCheckBox>();
		boxPanel = new JPanel();
		boxPanel.setLayout(new GridBagLayout());
		Enum<?>[] preselected = (Enum[]) data;
		int x = 0;
		int y = 0;
		for (Object value : field.getType().getComponentType().getEnumConstants()) {
			JCheckBox box = new JCheckBox(value.toString());
			if (preselected != null) {
				for (Enum<?> e : preselected) {
					if (e.toString().equals(value.toString())) {
						box.setSelected(true);
					}
				}
			}
			boxes.add(box);
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.gridx = x;
			c.gridy = y;
			c.ipadx = 50;
			boxPanel.add(box, c);
			box.addActionListener(this);
			x += 1;
			if (x == 3) {
				x = 0;
				y += 1;
			}
		}
		addConstrained(boxPanel);
	}

	@Override
	protected void copyTo(Schema s) {
		List<Enum<?>> checked = new ArrayList<Enum<?>>();
		for (JCheckBox box : boxes) {
			if (box.isSelected()) {
				Object corresponding = null;
				for (Object value : source.getType().getComponentType().getEnumConstants()) {
					if (value.toString().equals(box.getText())) {
						corresponding = value;
						break;
					}
				}
				if (corresponding == null) {
					Global.instance().warn("Orphan button " + box.getText());
				} else {
					checked.add((Enum<?>) corresponding);
				}
			}
		}
		Object array = Array.newInstance(source.getType().getComponentType(), checked.size());
		for (int i = 0; i < checked.size(); i += 1) {
			Array.set(array, i, checked.get(i));
		}
		try {
			source.set(s, array);
		} catch (Exception e) {
			Global.instance().err("Reflection fail'd", e);
		}
	}

}
