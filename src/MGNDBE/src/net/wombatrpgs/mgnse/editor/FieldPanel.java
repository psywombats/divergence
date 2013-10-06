/**

 *  FieldPanel.java
 *  Created on Aug 13, 2012 11:54:36 PM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.wombatrpgs.mgns.core.Annotations;
import net.wombatrpgs.mgns.core.Schema;

/**
 * Base of all editor panel field editors. Provides some cool methods for them
 * to use related to constraints.
 */
public abstract class FieldPanel extends JPanel implements 	DocumentListener, 
															ActionListener,
															ItemListener,
															PropertyChangeListener {
	
	private static final long serialVersionUID = -2777827947566521485L;
	
	protected EditorPanel parent;
	protected Field source;
	protected int gridYIndex;
	
	/**
	 * Creates and initializes a new panel. Checks for header status, starts
	 * layout stuff.
	 * @param field The source field this is being generated from
	 */
	public FieldPanel(EditorPanel parent, Field field) {
		this.source = field;
		this.parent = parent;
		setLayout(new GridBagLayout());
		gridYIndex = 0;
		checkHeader();
		describe();
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) { parent.setDirty(true); }
	@Override
	public void removeUpdate(DocumentEvent e) { parent.setDirty(true); }
	@Override
	public void changedUpdate(DocumentEvent e) { parent.setDirty(true); }
	@Override
	public void actionPerformed(ActionEvent e) { parent.setDirty(true); }
	@Override
	public void itemStateChanged(ItemEvent e) { parent.setDirty(true); }
	@Override
	public void propertyChange(PropertyChangeEvent e) { parent.setDirty(true); }

	/**
	 * Selects a string in a drop-down list.
	 * @param s			The string to select
	 * @param input		The dropdown list to run through
	 * @return			True if it was there, false if it wasn't
	 */
	public static boolean selectString(JComboBox<String> input, String s) {
		for (int i = 0; i < input.getItemCount(); i++) {
			if (s.equals(input.getItemAt(i))) {
				input.setSelectedIndex(i);
				return true;
			}
		}
		return false;
	}
	
	/** @return True if this field is part of the header */
	protected boolean isHeader() {
		return (source.isAnnotationPresent(Annotations.Header.class) &&
				source.getAnnotation(Annotations.Header.class).value());
	}

	/**
	 * Gets a new set of default constraints. These constraints increment a
	 * hidden field so that it's always in the right order, as well as setting
	 * a few basic fields
	 * @return A new constraint ready for use
	 */
	protected GridBagConstraints generateConstraints() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = gridYIndex;
		//c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.ipady = 2;
		c.ipadx = 2;
		gridYIndex += 1;
		return c;
	}
	
	/**
	 * Adds a child using generated constraints.
	 * @param input The child component to add
	 */
	protected void addConstrained(JComponent input) {
		this.add(input, generateConstraints());
	}
	
	/**
	 * Generates a new JLabel or something appropriate for labeling. In here so
	 * that the look and feel of all labels can change rapidly.
	 * @param defaultData The text value for the label
	 * @return The component that is the label
	 */
	protected JComponent newLabel(String defaultData) {
		JLabel label = new JLabel(defaultData);
		return label;
	}
	
	/**
	 * Disables input to the component if the field is annotated as Immutable.
	 * @param component The component to potentially disable
	 */
	protected void checkMute(JComponent component) {
		if (source.isAnnotationPresent(Annotations.Immutable.class)) {
			component.setEnabled(!source.getAnnotation(Annotations.Immutable.class).value());
		}
	}
	
	/**
	 * Copy bound data to the appropriate field in the new schema object. It's
	 * guaranteed to be the same type as the creator schema.
	 * @param s The new schema to write to
	 */
	protected abstract void copyTo(Schema s);
	
	/**
	 * Labels a field if it has a Desc tag attached. Does this by added a new
	 * JTextLabel or something.
	 */
	private void describe() {
		if (source.isAnnotationPresent(Annotations.Desc.class)) {
			addConstrained(newLabel(source.getAnnotation(Annotations.Desc.class).value()));
		}
	}
	
	/**
	 * Checks if this field is a header and if so, updates the component
	 * accordingly.
	 */
	private void checkHeader() {
		if (isHeader()) {
			//setBackground(Color.CYAN);
			//HERPHERPHREPHREPEHRP
		}
	}

}
