/**
 *  EditorPanel.java
 *  Created on Aug 12, 2012 3:15:53 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.JPanel;

import net.wombatrpgs.mgns.core.Annotations;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.InlinePolymorphic;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.PolymorphicLink;
import net.wombatrpgs.mgns.core.Schema;
import net.wombatrpgs.mgnse.Global;
import net.wombatrpgs.mgnse.Logic;
import net.wombatrpgs.mgnse.io.OutputHandler;
import net.wombatrpgs.mgnse.tree.SchemaTree;

/**
 * This panel has a bunch of widgets and gadgets for editing database entries.
 */
public class EditorPanel extends JPanel {

	private static final long serialVersionUID = 6210685766673563887L;
	
	private static final Insets HEADER_INSETS = new Insets(2, 10, 2, 10);
	private static final Insets STANDARD_INSETS = new Insets(10, 10, 10, 10);
	
	private ArrayList<FieldPanel> boundPanels;
	private Schema source;
	private File file;
	private Logic logic;
	private DirtyListener dirtyListener;
	private int gridYIndex;
	private boolean dirty;
	private boolean showHeader;
	
	/**
	 * Creates a new editor panel. Data is loaded from the provided schema.
	 * @param schema 		The schema this pane will edit
	 * @param file 			The file that this schema should (eventually) be written
	 * @param logic 		The controlling logic for the editor
	 * @param showHeader	True to show header items (ie, not inline editor)
	 */
	public EditorPanel(Schema schema, File file, Logic logic, boolean showHeader) {
		this.source = schema;
		this.file = file;
		this.logic = logic;
		this.showHeader = showHeader;
		loadData();
	}
	
	/** @return True if this panel has unsaved changes */
	public boolean isDirty() { return this.dirty; }
	/** @return The file this schema writes to */
	public File getFile() { return this.file; }
	/** @return The controlling logic for this panel */
	public Logic getLogic() { return this.logic; }
	/** @return The underlying schema for this panel */
	public Schema getSchema() { return this.source; }
	/** @return The tree of all schemers */
	public SchemaTree getTree() { return this.logic.getSchemaTree(); }
	/** @param listener The new dirty listener for this panel */
	public void setDirtyListener(DirtyListener listener) { this.dirtyListener = listener; }
	
	/** @param dirty True if this panel has unsaved changes */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		logic.notifyDirty(this);
		if (dirtyListener != null && dirty) {
			dirtyListener.onDirty();
		}
	}

	/**
	 * The contextual way to load a local file.
	 * @param fileName		The name of the file or directory
	 * @return				The named file or directory, local to project file
	 */
	public File loadFile(String fileName) { return logic.loadFile(fileName); }
	
	/**
	 * Writes all our fun data to a file!
	 * @param out The output handler to use to write schema
	 */
	public void saveData(OutputHandler out) {
		if (file == null) {
			Global.instance().err("Somehow tried to dump an inner editor?", new Exception());
			return;
		}
		try {
			Schema s = source.getClass().newInstance();
			copyTo(s);
			out.writeSchema(s, file);
		} catch (Exception e) {
			Global.instance().err("Screwup trying to write schema " + source, e);
		}
		setDirty(false);
	}
	
	/**
	 * Copies all the data from the field panels to an unsuspecting schema.
	 * @param s		The schema to mutate/write to
	 */
	public void copyTo(Schema s) {
		for (FieldPanel panel : boundPanels) {
			panel.copyTo(s);
		}
	}
	
	/**
	 * Reads schema from file and builds editor for it.
	 */
	public void loadData() {
		this.setLayout(new GridBagLayout());
		generateAllFields();
		dirty = false;
	}
	
	/**
	 * Primes the editor for modifying a data object.
	 */
	public void generateAllFields() {
		gridYIndex = 0;
		boundPanels = new ArrayList<FieldPanel>();
		removeAll();
		Boolean[] added = new Boolean[source.getClass().getFields().length];
		for (int i = 0; i < added.length; i++) {
			added[i] = false;
		}
		Field f = null;
		try {
			for (int i = 0; i < source.getClass().getFields().length; i++) {
				f = source.getClass().getFields()[i];
				if (f.isAnnotationPresent(Annotations.Header.class) &&
						f.getAnnotation(Annotations.Header.class).value()) {
					generateEditorFieldFromData(f, f.get(source), source, showHeader);
					added[i] = true;
				}
			}
			addPadding();
			for (int i =0; i < source.getClass().getFields().length; i++) {
				f = source.getClass().getFields()[i];
				if (!added[i]) {
					generateEditorFieldFromData(f, f.get(source), source, true);
				}
			}
		} catch (Exception e) {
			Global.instance().err("Failed reading field " + f, e);
		}

		GridBagConstraints c = new GridBagConstraints();
		c.gridy = gridYIndex;
		c.weighty = 1;
		add(new JPanel(), c);
	}
	
	/**
	 * Generates the grid bag constraints necessary to place this panel in its
	 * parent editor frame. Keeps display logic out of Logic.
	 * @return The bag constraints to add this object
	 */
	public GridBagConstraints genConstraints() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		return c;
	}
	
	/**
	 * Adds an editor field to this editor frame based on the schema field and
	 * provided default data from the existing entry.
	 * @param field 	The field that this segment will edit
	 * @param data 		The default value that this field will have
	 * @param schema 	The schema to map to
	 * @param display	True to actually display this field
	 */
	private void generateEditorFieldFromData(Field field, Object data, Schema schema, boolean display) {
		
		FieldPanel panel = null;
		if (field.getType().isArray()) {
			if (field.isAnnotationPresent(SchemaLink.class)) {
				// this is strings, all representing links to other schema
				panel = new ReferenceArrayField(this, (String[]) data, field);
			} else if (field.isAnnotationPresent(InlineSchema.class)) {
				// this is a list of uh... other stuff
				panel = new InlineSchemaArrayField(this, (Schema[]) data, field);
			} else if (Enum.class.isAssignableFrom(field.getType().getComponentType())) {
				// array of enums means a set
				panel = new EnumSetField(this, data, field);
			}
		} else if (field.isAnnotationPresent(InlineSchema.class)) {
			panel = new InlineSchemaField(this, (Schema) data, field);
		} else if (Enum.class.isAssignableFrom(field.getType())) {
			panel = new EnumField(this, data, field);
		} else if (field.isAnnotationPresent(FileLink.class)) {
			panel = new FileField(this, (String) data, field);
		} else if (field.isAnnotationPresent(SchemaLink.class)) {
			panel = new ReferenceField(this, (String) data, field);
		} else if (field.isAnnotationPresent(InlinePolymorphic.class)) {
			panel = new PolymorphField(this, (PolymorphicLink) data, field);
		} else if (String.class.isAssignableFrom(field.getType())) {
			panel = new StringField(this, (String) data, field, schema);
		} else if (Integer.class.isAssignableFrom(field.getType())) {
			panel = new IntegerField(this, (Integer) data, field, schema);
		} else if (Float.class.isAssignableFrom(field.getType())) {
			panel = new FloatField(this, (Float) data, field, schema);
		} else {
			Global.instance().warn("Couldn't find a valid panel for: " + field.getType());
			return;
		}
		
		boundPanels.add(panel);
		if (display) {
			Global.instance().debug("Generated a panel " + panel.getClass().getCanonicalName());
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = gridYIndex;
			//c.insets = new Insets(10, 20, 20, 10);
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.weightx = 1;
			c.weighty = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			if (panel.isHeader()) {
				c.insets = HEADER_INSETS;
			} else {
				c.insets = STANDARD_INSETS;
			}
			add(panel, c);
			gridYIndex++;
		}
	}
	
	/**
	 * Shitty little helper I relegated to over here.
	 */
	private void addPadding() {
		JPanel pad = new JPanel();
		pad.setPreferredSize(new Dimension(0, 24));
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = gridYIndex;
		gridYIndex += 1;
		add(pad, c);
	}

}
