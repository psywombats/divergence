/**
 *  PolymorphPanel.java
 *  Created on Mar 31, 2014 10:11:24 PM for project jgdbe
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import net.wombatrpgs.mgns.core.Annotations.InlinePolymorphic;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.PolymorphicLink;
import net.wombatrpgs.mgns.core.PolymorphicSchema;
import net.wombatrpgs.mgns.core.Schema;
import net.wombatrpgs.mgnse.Global;

/**
 * Panel for selecting one of many subclasses.
 */
public class PolymorphField extends FieldPanel {

	private static final long serialVersionUID = 4266235764147704065L;
	
	protected JComboBox<String> selector;
	protected RemovablePanel subpanel;
	protected EditorPanel contents;
	
	protected Class<? extends PolymorphicSchema> superC;
	protected PolymorphicLink link;
	protected List<Class<? extends PolymorphicSchema>> subs;
	
	protected boolean initializing;

	/**
	 * Creates a new subpanel for selecting a subclass.
	 * @param	parent			The editor to create for
	 * @param	data			Not entirely sure anymore
	 * @param	field			The field to wrap around
	 */
	public PolymorphField(EditorPanel parent, PolymorphicLink data, Field field) {
		super(parent, field);
		
		initializing = true;
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
		
		if (data != null) {
			link = data;
			updateSubpanel();
		}
		initializing = false;
	}

	/**
	 * @see net.wombatrpgs.mgnse.editor.FieldPanel#copyTo
	 * (net.wombatrpgs.mgns.core.Schema)
	 */
	@Override
	protected void copyTo(Schema s) {
		if (contents == null) return;
		try {
			source.set(s, link);
			if (contents.getFile() == null || !contents.getFile().exists()) {
				contents.getFile().getParentFile().mkdirs();
				contents.getFile().createNewFile();
			}
			contents.saveData(parent.getLogic().getOut());
		} catch (Exception e) {
			Global.instance().err("Reflection fail", e);
		}
	}

	/**
	 * @see net.wombatrpgs.mgnse.editor.FieldPanel#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!initializing) {
			super.actionPerformed(e);
			updateSubpanel();
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void updateSubpanel() {
		if (subpanel != null) {
			remove(subpanel);
			if (contents.getFile().exists()) {
				contents.getFile().delete();
			}
			link = null;
		}
		Class<? extends PolymorphicSchema> selected = null;
		for (Class<? extends PolymorphicSchema> subC : subs) {
			if (subC.getSimpleName().equals(selector.getSelectedItem())) {
				selected = subC;
			}
		}
		if (link != null) {
			selected = (Class<? extends PolymorphicSchema>) parent.getTree().getSchemaByName(link.clazz);
			if (selected != null) {
				selector.setSelectedItem(selected.getSimpleName());
			}
		}
		if (selected != null) {
			try {
				Random r = new Random();
				MainSchema schema;
				File f;
				if (link == null) {
					link = new PolymorphicLink();
					link.key = "anon_" + parent.getSchema().getClass().getSimpleName() + "_" + r.nextInt();
					link.clazz = selected.getCanonicalName();
					f = new File(parent.getLogic().pathForSchema(selected, link.key));
					schema = selected.newInstance();
					schema.key = link.key;
					schema.subfolder = "";
					schema.description = "auto-generated anonymous polymorph";
					schema.name = schema.key;
				} else {
					f = new File(parent.getLogic().pathForSchema(selected, link.key));
					if (f.exists()) {
						schema = parent.getLogic().getIn().instantiateData(
								parent.getTree().getSchemaByFile(f),
								f);
					} else {
						return;
					}
				}
				contents = new EditorPanel(schema, f, parent.getLogic(), false);
				contents.setDirtyListener(this);
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
