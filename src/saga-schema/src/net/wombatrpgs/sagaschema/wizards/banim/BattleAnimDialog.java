/**
 *  BattleAnimDialog.java
 *  Created on May 22, 2014 10:30:31 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.wizards.banim;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.wombatrpgs.mgnse.MainFrame;
import net.wombatrpgs.sagaschema.graphics.banim.BattleAnimStripMDO;
import net.wombatrpgs.sagaschema.graphics.banim.data.BattleStepMDO;
import net.wombatrpgs.sagaschema.graphics.banim.data.RotationType;

/**
 * Dialog for battle animations.
 */
public class BattleAnimDialog extends JDialog implements	ActionListener,
															MouseListener,
															MouseMotionListener,
															DocumentListener {
	
	private static final long serialVersionUID = 392469649966986235L;
	private static final int WINDOW_WIDTH = 900;
	private static final int WINDOW_HEIGHT = 600;
	private static final int PORTRAIT_SIZE = 48;
	
	private static final String TOOL_EXPORT = "Export";
	
	private static final String ROTATION_ENABLED = RotationType.ROTATION_ENABLED.toString();
	private static final String ROTATION_DISABLED = RotationType.ROTATION_DISABLED.toString();
	
	protected MainFrame frame;
	
	protected JMenuItem toolExport;
	protected JMenu menuTools;
	protected JMenuBar bar;
	protected JComboBox<String> stepSelect, spriteSelect, rotationSelect;
	protected JButton newStep;
	protected JFormattedTextField fieldX, fieldY;
	protected JFormattedTextField fieldStart, fieldDuration;
	protected int gridYIndex = 0;
	
	protected boolean dragging;
	protected int dragStartX, dragStartY;
	protected boolean locked;
	
	protected Image sandbag, sprite;
	protected List<BattleStepMDO> steps;
	protected BattleStepMDO selectedStep;
	
	public BattleAnimDialog(MainFrame frame) {
		super(frame, "Battle Animation Wizard");
		this.frame = frame;
		init();
		steps = new ArrayList<BattleStepMDO>();
	}
	
	private void init() {
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		
		int treeHorizPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int treeVertPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		
		toolExport = new JMenuItem(TOOL_EXPORT);
		toolExport.addActionListener(this);
		
		menuTools = new JMenu("Tools");
		menuTools.add(toolExport);
		
		bar = new JMenuBar();
		bar.add(menuTools);
		setJMenuBar(bar);
		
		JPanel optionsPane = new JPanel();
		optionsPane.setLayout(new GridBagLayout());
		optionsPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		optionsPane.setAlignmentY(JComponent.TOP_ALIGNMENT);
		
		JScrollPane settingScroll = new JScrollPane(optionsPane, treeHorizPolicy, treeVertPolicy);
		settingScroll.setMinimumSize(new Dimension(WINDOW_WIDTH / 3, WINDOW_HEIGHT));
		settingScroll.setPreferredSize(new Dimension(WINDOW_WIDTH / 3, WINDOW_HEIGHT));
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
		
		JPanel picPane = new JPanel() {
			private static final long serialVersionUID = -6878299544591473409L;
			@Override public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(sandbag, PORTRAIT_SIZE, PORTRAIT_SIZE, this);
				g.setColor(Color.GRAY);
				g.drawLine(PORTRAIT_SIZE*3/2, 0, PORTRAIT_SIZE*3/2, PORTRAIT_SIZE*3);
				g.drawLine(0, PORTRAIT_SIZE*3/2, PORTRAIT_SIZE*3, PORTRAIT_SIZE*3/2);
				if (selectedStep != null && selectedStep.sprite != null && sprite != null) {
					g.drawImage(sprite,
							(int) (PORTRAIT_SIZE*3/2 - sprite.getWidth(null)/2 + selectedStep.x),
							(int) (PORTRAIT_SIZE*3/2 - sprite.getHeight(null)/2 + selectedStep.y),
							null);
				}
			}
		};
		picPane.addMouseListener(this);
		picPane.addMouseMotionListener(this);
		
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
		
		ImageIcon ii = new ImageIcon("sprites/battle_portraits/sandbag.png");
		sandbag = ii.getImage();
		
		newStep = new JButton("New step");
		newStep.addActionListener(this);
		optionsPane.add(newStep, generateConstraints());
		
		addLabel(optionsPane, "Step select");
		stepSelect = new JComboBox<String>();
		stepSelect.addActionListener(this);
		optionsPane.add(stepSelect, generateConstraints());
		
		addLabel(optionsPane, "Sprite select");
		spriteSelect = new JComboBox<String>();
		spriteSelect.addActionListener(this);
		File dir = frame.getLogic().loadFile("sprites/battle_anim");
		for (File f : dir.listFiles()) {
			spriteSelect.addItem(f.getName());
		}
		optionsPane.add(spriteSelect, generateConstraints());
		
		addLabel(optionsPane, "x offset");
		fieldX = new JFormattedTextField(NumberFormat.getIntegerInstance());
		fieldX.setValue(0);
		fieldX.setColumns(8);
		fieldX.getDocument().addDocumentListener(this);
		optionsPane.add(fieldX, generateConstraints());
		
		addLabel(optionsPane, "y offset");
		fieldY = new JFormattedTextField(NumberFormat.getIntegerInstance());
		fieldY.setValue(0);
		fieldY.setColumns(8);
		fieldY.getDocument().addDocumentListener(this);
		optionsPane.add(fieldY, generateConstraints());
		
		addLabel(optionsPane, "start");
		fieldStart = new JFormattedTextField(NumberFormat.getNumberInstance());
		fieldStart.setValue(0);
		fieldStart.setColumns(8);
		fieldStart.getDocument().addDocumentListener(this);
		optionsPane.add(fieldStart, generateConstraints());
		
		addLabel(optionsPane, "duration");
		fieldDuration = new JFormattedTextField(NumberFormat.getNumberInstance());
		fieldDuration.setValue(0);
		fieldDuration.setColumns(8);
		fieldDuration.getDocument().addDocumentListener(this);
		optionsPane.add(fieldDuration, generateConstraints());
		
		addLabel(optionsPane, "Rotation?");
		rotationSelect = new JComboBox<String>();
		rotationSelect.addActionListener(this);
		rotationSelect.addItem(ROTATION_DISABLED);
		rotationSelect.addItem(ROTATION_ENABLED);
		optionsPane.add(rotationSelect, generateConstraints());
		
		c = new GridBagConstraints();
		c.weighty = 99;
		c.gridy = gridYIndex;
		optionsPane.add(new JPanel(), c);
		
		pack();
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (e.getSource() == newStep) {
			BattleStepMDO mdo = new BattleStepMDO();
			mdo.start = 0f;
			mdo.x = 0f;
			mdo.y = 0f;
			mdo.duration = 0f;
			steps.add(mdo);
			stepSelect.addItem("Step " + steps.size());
			selectStep(steps.size() - 1);
		} else if (e.getSource() == stepSelect) {
			copyMDOToFields();
			selectStep(stepSelect.getSelectedIndex());
		} else if (e.getSource() == spriteSelect) {
			if (selectedStep == null) return;
			copyFieldsToMDO();
		} else if (cmd.equals(TOOL_EXPORT)) {
			export();
		}
		repaint();
	}
	
	private void export() {
		String name = (String) JOptionPane.showInputDialog(
				this,
				"Enter the name of this anim (eg \"slash\", \"shoot_arrow\")",
				"Export animation...",
				JOptionPane.PLAIN_MESSAGE,
				null, null, null);
		String subfolder = (String) JOptionPane.showInputDialog(
				this,
				"Enter the subfolder/type of this anim (eg \"weapons/spellbooks\", \"abilities\")",
				"Export animation...",
				JOptionPane.PLAIN_MESSAGE,
				null, null, null);
		// TODO: dbe: check for conflicts in naming ?
		
		// check for cancel
		if (name == "" || name == null || subfolder == null) {
			return;
		}
		
		copyFieldsToMDO();
		
		BattleAnimStripMDO mdo = new BattleAnimStripMDO();
		mdo.description = "Autogenerated by battle anim wizard";
		mdo.subfolder = subfolder;
		mdo.key = "banim_" + name;
		mdo.steps = new BattleStepMDO[steps.size()];
		for (int i = 0; i < steps.size(); i += 1) {
			mdo.steps[i] = steps.get(i);
			mdo.steps[i].sprite = "battle_anim/" + mdo.steps[i].sprite;
		}
		frame.getLogic().getOut().writeNewSchema(mdo);
		
		JOptionPane.showMessageDialog(this,
				"Export complete.\n",
				"Export Complete",
				JOptionPane.INFORMATION_MESSAGE);
		setVisible(false);
	}
	
	private void addLabel(JPanel panel, String str) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = gridYIndex;
		c.weightx = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.ipady = 1;
		c.ipadx = 1;
		c.insets = new Insets(6, 10, 2, 10);
		gridYIndex += 1;
		JLabel label = new JLabel(str);
		panel.add(label, c);
	}
	
	@Override public void insertUpdate(DocumentEvent e) { copyFieldsToMDO(); }
	@Override public void removeUpdate(DocumentEvent e) { copyFieldsToMDO(); }
	@Override public void changedUpdate(DocumentEvent e) { copyFieldsToMDO(); }

	@Override public void mouseMoved(MouseEvent e) { }
	@Override public void mouseEntered(MouseEvent e) { }
	@Override public void mouseExited(MouseEvent e) { }
	@Override public void mouseClicked(MouseEvent e) { }

	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragging && selectedStep != null) {
			selectedStep.x = (float) (((e.getX() - dragStartX) / 8) * 8);
			selectedStep.y = (float) (((e.getY() - dragStartY) / 8) * 8);
			copyMDOToFields();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!dragging) {
			dragging = true;
			dragStartX = e.getX();
			dragStartY = e.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		dragging = false;
	}
	
	private GridBagConstraints generateConstraints() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = gridYIndex;
		c.weightx = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.ipady = 1;
		c.ipadx = 1;
		c.insets = new Insets(0, 10, 10, 10);
		gridYIndex += 1;
		return c;
	}
	
	private void selectStep(int index) {
		stepSelect.setSelectedIndex(index);
		selectedStep = steps.get(index);
		copyMDOToFields();
	}
	
	private void copyFieldsToMDO() {
		if (selectedStep != null && !locked) {
			locked = true;
			selectedStep.x = Float.valueOf(fieldX.getValue().toString());
			selectedStep.y = Float.valueOf(fieldY.getValue().toString());
			selectedStep.sprite = spriteSelect.getSelectedItem().toString();
			ImageIcon ii = new ImageIcon("sprites/battle_anim/" + selectedStep.sprite);
			sprite = ii.getImage();
			selectedStep.start = Float.valueOf(fieldStart.getValue().toString());
			selectedStep.duration = Float.valueOf(fieldDuration.getValue().toString());
			if (rotationSelect.getSelectedItem().equals(ROTATION_ENABLED)) {
				selectedStep.rotation = RotationType.ROTATION_ENABLED;
			} else {
				selectedStep.rotation = RotationType.ROTATION_DISABLED;
			}
			locked = false;
		}
		repaint();
	}
	
	private void copyMDOToFields() {
		if (selectedStep != null  && !locked) {
			locked = true;
			fieldX.setValue(selectedStep.x);
			fieldY.setValue(selectedStep.y);
			fieldStart.setValue(selectedStep.start);
			fieldDuration.setValue(selectedStep.duration);
			
			spriteSelect.setSelectedItem(selectedStep.sprite);
			rotationSelect.setSelectedItem(selectedStep.rotation);
			ImageIcon ii = new ImageIcon("sprites/battle_anim/" + selectedStep.sprite);
			sprite = ii.getImage();
			locked = false;
		}
		repaint();
	}

}
