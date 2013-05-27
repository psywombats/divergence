/**
 *  FourDirDialog.java
 *  Created on May 27, 2013 12:49:09 PM for project MGNDBE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.wizard;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import net.wombatrpgs.mgnse.MainFrame;

/**
 * Dialog attachment for fourdir wizard.
 */
public class FourDirDialog extends JDialog {
	
	private static final long serialVersionUID = 148284705740583304L;
	private static final int WINDOW_WIDTH = 900;
	private static final int WINDOW_HEIGHT = 600;
	private static final String DEFAULT_ITEM = "Select a sprite";
	
	protected MainFrame frame;
	protected JComboBox<String> fileSelect;
	protected ImageIcon image;
	
	private int gridYIndex = 0;
	
	public FourDirDialog(MainFrame frame) {
		super(frame, "Four-Dir Sprite Wizard");
		this.frame = frame;
		init();
	}
	
	private void init() {
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		
		int treeHorizPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int treeVertPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		
		JPanel optionsPane = new JPanel();
		optionsPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		optionsPane.setAlignmentY(JComponent.TOP_ALIGNMENT);
		
		JScrollPane settingScroll = new JScrollPane(optionsPane, treeHorizPolicy, treeVertPolicy);
		settingScroll.setMinimumSize(new Dimension(WINDOW_WIDTH / 4, WINDOW_HEIGHT));
		settingScroll.setPreferredSize(new Dimension(WINDOW_WIDTH / 4, WINDOW_HEIGHT));
		settingScroll.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		settingScroll.setAlignmentY(JComponent.TOP_ALIGNMENT);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.VERTICAL;
		c.ipadx = 10;
		c.ipady = 10;
		add(settingScroll, c);
		
		JPanel picPane = new JPanel();
		
		JScrollPane picScroll = new JScrollPane(picPane, treeHorizPolicy, treeVertPolicy);
		picScroll.setPreferredSize(new Dimension(WINDOW_WIDTH * 3 / 4, WINDOW_HEIGHT));
		picScroll.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		picScroll.setAlignmentY(JComponent.CENTER_ALIGNMENT);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weighty = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.BOTH;
		add(picScroll, c);
		
		fileSelect = new JComboBox<String>();
		File dir = frame.getLogic().loadFile("sprites");
		for (File f : dir.listFiles()) {
			if (f.isFile()) fileSelect.addItem(f.getName());
		}
		fileSelect.setEditable(true);
		fileSelect.setSelectedItem(DEFAULT_ITEM);
		fileSelect.setEditable(false);
		optionsPane.add(fileSelect, generateConstraints());
		
		image = new ImageIcon("sprites/block.png");
		
		pack();
		setVisible(true);
	}
	
	private GridBagConstraints generateConstraints() {
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

}
