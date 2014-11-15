/**
 *  ArrayField.java
 *  Created on Oct 16, 2012 3:50:19 PM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;

import net.wombatrpgs.mgnse.Global;

/**
 * Superclass of all array-based fields. It's useful for the common removable
 * panel and new button functionality.
 * @param <T> The underlying type of the array
 */
public abstract class ArrayField<T extends JComponent> 	extends FieldPanel 
														implements RemovalListener {
	
	private static final long serialVersionUID = -8155278891013826258L;
	protected ArrayList<T> inputs;
	protected JButton newButton;
	protected boolean isInline;

	public ArrayField(EditorPanel parent, Field field, boolean isInline) {
		super(parent, field);
		
		inputs = new ArrayList<T>();
		this.isInline = isInline;
		
		newButton = new JButton("Add New Entry");
		newButton.setActionCommand("addnew");
		newButton.addActionListener(this);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = gridYIndex;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(0, 32, 0, 0);
		gridYIndex++;
		
		add(newButton, c);
		checkMute(newButton);
	}
	
	@Override
	public void onRemoved(JComponent contents) {
		JComponent toRemove = null;
		for (JComponent input : inputs) {
			if (input.equals(contents)) {
				toRemove = input;
				break;
			}
		}
		if (toRemove == null) {
			Global.instance().warn("Couldn't find a removable for " + contents);
		} else {
			inputs.remove(toRemove);
			this.remove(toRemove);
			parent.revalidate();
			revalidate();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		addInput(genInput());
		revalidate();
		super.actionPerformed(e);
	}
	
	protected abstract T genInput();
	
	/**
	 * Adds an input to the world.
	 * @param input		The input to add
	 */
	protected void addInput(T input) {
		inputs.add(input);
		addConstrained(new RemovablePanel(this, this, input, isInline));
		checkMute(input);
	}

}
