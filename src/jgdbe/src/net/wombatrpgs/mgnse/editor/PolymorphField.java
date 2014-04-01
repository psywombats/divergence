/**
 *  PolymorphPanel.java
 *  Created on Mar 31, 2014 10:11:24 PM for project jgdbe
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import net.wombatrpgs.mgns.core.Annotations.InlinePolymorphic;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.PolymorphicSchema;
import net.wombatrpgs.mgns.core.Schema;

/**
 * Panel for selecting one of many subclasses.
 */
public class PolymorphField extends FieldPanel {

	private static final long serialVersionUID = 4266235764147704065L;
	
	protected JComboBox<String> selector;
	protected RemovablePanel subpanel;
	protected EditorPanel contents;
	
	protected Class<? extends PolymorphicSchema> superC;
	protected Class<? extends PolymorphicSchema> selected;
	protected List<Class<? extends PolymorphicSchema>> subs;

	/**
	 * Creates a new subpanel for selecting a subclass.
	 * @param	parent			The editor to create for
	 * @param	defaultData		The existing subclass that's there, or null
	 * @param	annotation		The annotation containing the poly class
	 * @param	field			The field to wrap around
	 */
	public PolymorphField(EditorPanel parent, PolymorphicSchema defaultData,
			InlinePolymorphic annotation, Field field) {
		super(parent, field);
		
		selector = new JComboBox<String>();
		if (field.isAnnotationPresent(Nullable.class)) {
			selector.addItem("None");
		}
		superC = field.getAnnotation(InlinePolymorphic.class).value();
		subs = parent.getTree().getSubclasses(superC);
		for (Class<? extends PolymorphicSchema> clazz : subs) {
			selector.addItem(clazz.getSimpleName());
		}
		addConstrained(selector);
		selector.addActionListener(this);
		
		if (defaultData != null) {
			selector.setSelectedItem(defaultData.getClass());
			updateSubpanel();
		}
	}

	/**
	 * @see net.wombatrpgs.mgnse.editor.FieldPanel#copyTo
	 * (net.wombatrpgs.mgns.core.Schema)
	 */
	@Override
	protected void copyTo(Schema s) {
		try {
			if (selected != null) {
				Schema result = selected.newInstance();
				contents.copyTo(result);
				source.set(s, result);
			} else {
				source.set(s, null);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see net.wombatrpgs.mgnse.editor.FieldPanel#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		updateSubpanel();
	}
	
	protected void updateSubpanel() {
		if (subpanel != null) {
			remove(subpanel);
		}
		selected = null;
		for (Class<? extends PolymorphicSchema> subC : subs) {
			if (subC.getSimpleName().equals(selector.getSelectedItem())) {
				selected = subC;
			}
		}
		if (selected != null) {
			try {
				contents = new EditorPanel(selected.newInstance(), null, parent.getLogic());
				subpanel = new RemovablePanel(
						this,
						new RemovalListener() {
							@Override public void onRemoved(JComponent contents) {
								selector.setSelectedItem("None");
								updateSubpanel();
							}
						},
						contents,
						false);
				addConstrained(subpanel);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		revalidate();
		parent.revalidate();
	}

}
