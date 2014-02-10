/**
 *  RemovablePanel.java
 *  Created on Oct 12, 2012 12:59:29 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A multi-purpose panel for any element a user can remove from an array.
 */
public class RemovablePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -6300077133684767396L;
	private JComponent contents;
	private RemovalListener listener;
	private JPanel parent;
	
	/**
	 * Creates and initializes a new removable panel with the specified
	 * payload. The payload item is treated pretty genericly so set up the
	 * listeners before you pass it in.
	 * @param parent		The panel this panel is a part of
	 * @param listener		The dude to notify when shit goes down
	 * @param contents		The payload item
	 * @param isInline		True for horizontal remove layout, false for vert
	 */
	public RemovablePanel(JPanel parent, RemovalListener listener, 
			JComponent contents, boolean isInline) {
		setLayout(new GridBagLayout());
		this.contents = contents;
		this.parent = parent;
		this.listener = listener;
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(0, 32, 0, 0);
		add(contents, c);
		
		JButton yesno = new JButton("Remove");
		yesno.setActionCommand("remove");
		yesno.addActionListener(this);
		
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = isInline ? 1 : 0;
		c2.gridy = isInline ? 0 : 1;
		c2.fill = GridBagConstraints.NONE;
		c2.weightx = 0;
		c2.anchor = GridBagConstraints.LAST_LINE_START;
		c2.ipady = 2;
		c2.ipadx = 2;
		c2.insets = new Insets(0, 32, 0, 0);
		add(yesno, c2);
		
		invalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		listener.onRemoved(contents);
		parent.remove(this);
	}

}
