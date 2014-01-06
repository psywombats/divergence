/**
 *  FourDirDialog.java
 *  Created on May 27, 2013 12:49:09 PM for project MGNDBE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.wizards;

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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
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
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.wombatrpgs.mgnse.MainFrame;
import net.wombatrpgs.mrogueschema.graphics.AnimationMDO;
import net.wombatrpgs.mrogueschema.graphics.FourDirMDO;
import net.wombatrpgs.mrogueschema.graphics.data.AnimationType;
import net.wombatrpgs.mrogueschema.graphics.data.DynamicBoxMDO;

/**
 * Dialog attachment for fourdir wizard.
 * 
 * This code's a pile of shit. It works, but if you're looking at this in, say,
 * 2014+, I'm so so sorry.
 */
public class FourDirDialog extends JDialog implements	ActionListener,
														DocumentListener,
														MouseListener,
														MouseMotionListener {
	
	private static final long serialVersionUID = 148284705740583304L;
	private static final int WINDOW_WIDTH = 900;
	private static final int WINDOW_HEIGHT = 600;
	private static final String DEFAULT_ITEM = "Select a sprite";
	
	private static final int LEFT_DIR = 2;
	private static final int RIGHT_DIR = 0;
	private static final int DOWN_DIR = 3;
	private static final int UP_DIR = 1;
	
	private static final String TOOL_EXPORT = "Export";
	private static final String TOOL_MIRROR_HORIZ = "Mirror left hitbox data to right data";
	private static final String TOOL_MIRROR_VERT = "Mirror downwards hitbox data to up data";
	
	private static final String MODE_BOUNDING = "Draw Bounding Boxes";
	private static final String MODE_ATTACK = "Draw Attack Boxes";
	
	protected MainFrame frame;
	protected JComboBox<String> fileSelect;
	protected Image image;
	protected JFormattedTextField frameWidthField, frameHeightField, frameCountField;
	protected JFormattedTextField fpsField;
	protected List<JFormattedTextField> ax1Fields;
	protected List<JFormattedTextField> ax2Fields;
	protected List<JFormattedTextField> ay1Fields;
	protected List<JFormattedTextField> ay2Fields;
	protected JMenu menuTools, menuMode;
	protected JMenuBar bar;
	protected JMenuItem toolExport, toolCopyHoriz, toolCopyVert;
	protected ButtonGroup groupMode;
	protected JRadioButtonMenuItem radioBoundingMode, radioAttackMode;
	
	// indexed by hitboxCoords.get(frameno)[dir][x1/y1/x2/y2]
	protected List<int[][]> hitboxCoords;
	
	protected boolean dragging;
	protected int dragStartX, dragStartY;
	protected int dragDir, dragFrame;
	protected int dragToX, dragToY;
	
	private int gridYIndex = 0;
	
	public FourDirDialog(MainFrame frame) {
		super(frame, "Four-Dir Sprite Wizard");
		this.frame = frame;
		dragging = false;
		hitboxCoords = new ArrayList<int[][]>();
		init();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (e.getSource() == fileSelect) {
			ImageIcon ii = new ImageIcon("sprites/" + fileSelect.getSelectedItem().toString());
			image = ii.getImage();
		} else if (cmd.equals(TOOL_EXPORT)) {
			export();
		} else if (cmd.equals(TOOL_MIRROR_HORIZ)) {
			mirrorHoriz();
		} else if (cmd.equals(TOOL_MIRROR_VERT)) {
			mirrorVert();
		}
		try {
			for (JFormattedTextField field : ax1Fields) field.commitEdit();
			for (JFormattedTextField field : ax2Fields) field.commitEdit();
			for (JFormattedTextField field : ay1Fields) field.commitEdit();
			for (JFormattedTextField field : ay2Fields) field.commitEdit();
		} catch (ParseException e2) {
			System.err.println("parse exception");
		}
		ensureSize();
		repaint();
	}
	
	private void init() {
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		
		int treeHorizPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int treeVertPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		
		toolExport = new JMenuItem(TOOL_EXPORT);
		toolCopyHoriz = new JMenuItem(TOOL_MIRROR_HORIZ);
		toolCopyVert = new JMenuItem(TOOL_MIRROR_VERT);
		toolExport.addActionListener(this);
		toolCopyHoriz.addActionListener(this);
		toolCopyVert.addActionListener(this);
		
		menuTools = new JMenu("Tools");
		menuTools.add(toolExport);
		menuTools.add(toolCopyHoriz);
		menuTools.add(toolCopyVert);
		
		menuMode = new JMenu("Draw Mode");
		groupMode = new ButtonGroup();
		radioBoundingMode = new JRadioButtonMenuItem(MODE_BOUNDING);
		radioAttackMode = new JRadioButtonMenuItem(MODE_ATTACK);
		groupMode.add(radioBoundingMode);
		groupMode.add(radioAttackMode);
		menuMode.add(radioBoundingMode);
		menuMode.add(radioAttackMode);
		radioBoundingMode.addActionListener(this);
		radioAttackMode.addActionListener(this);
		radioBoundingMode.setSelected(true);
		
		bar = new JMenuBar();
		bar.add(menuTools);
		bar.add(menuMode);
		setJMenuBar(bar);
		
		JPanel optionsPane = new JPanel();
		optionsPane.setLayout(new GridBagLayout());
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
		
		JPanel picPane = new JPanel() {
			private static final long serialVersionUID = -6878299544591473409L;
			@Override public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (!imageSelected()) return;
				int frames = getFrameCount();
				int width = getFrameWidth();
				int height = getFrameHeight();
				for (int dir = 0; dir < 4; dir++) {
					for (int frame = 0; frame < frames; frame++) {
						int x = frame * width;
						int y = dir * height;
						if ((frame + dir) % 2 == 0) {
							g.setColor(new Color(1, .9f, .8f));
						} else {
							g.setColor(new Color(.8f, .9f, 1));
						}
						g.fillRect(x, y, width, height);
					}
				}
				g.drawImage(image, 0, 0, this);
				ensureSize();
				for (int dir = 0; dir < 4; dir++) {
					for (int frame = 0; frame < frames; frame++) {
						int x = frame * width;
						int y = dir * height;
						int fromX, toX, fromY, toY;
						
						// bounding boxes
						g.setColor(new Color(.0f, .3f, .9f, .5f));
						if (dragging && dir == dragDir && !attackModeEnabled()) {
							fromX = (dragToX > dragStartX) ? dragStartX : dragToX;
							toX = (dragToX > dragStartX) ? dragToX : dragStartX;
							fromY = (dragToY > dragStartY) ? dragStartY : dragToY;
							toY = (dragToY > dragStartY) ? dragToY : dragStartY;
						} else {
							fromX = getFieldVal(ax1Fields.get(dir));
							toX = getFieldVal(ax2Fields.get(dir));
							fromY = getFieldVal(ay1Fields.get(dir));
							toY = getFieldVal(ay2Fields.get(dir));
						}
						g.fillRect(	x + fromX,
								y + fromY,
								toX - fromX,
								toY - fromY);
						
						// attack boxes
						g.setColor(new Color(.9f, .3f, .0f, .5f));
						if (dragging && dir == dragDir && attackModeEnabled() && frame == dragFrame) {
							fromX = (dragToX > dragStartX) ? dragStartX : dragToX;
							toX = (dragToX > dragStartX) ? dragToX : dragStartX;
							fromY = (dragToY > dragStartY) ? dragStartY : dragToY;
							toY = (dragToY > dragStartY) ? dragToY : dragStartY;
						} else {
							int[][] attackData = hitboxCoords.get(frame);
							fromX = attackData[dir][0];
							toX = attackData[dir][2];
							fromY = attackData[dir][1];
							toY = attackData[dir][3];
						}
						g.fillRect(	x + fromX,
								y + fromY,
								toX - fromX,
								toY - fromY);
					}
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
		
		addLabel(optionsPane, "Sprite file");
		fileSelect = new JComboBox<String>();
		File dir = frame.getLogic().loadFile("sprites");
		for (File f : dir.listFiles()) {
			if (f.isFile()) fileSelect.addItem(f.getName());
		}
		fileSelect.setEditable(true);
		fileSelect.setSelectedItem(DEFAULT_ITEM);
		fileSelect.setEditable(false);
		fileSelect.addActionListener(this);
		optionsPane.add(fileSelect, generateConstraints());
		
		addLabel(optionsPane, "Frame count");
		frameCountField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		frameCountField.setValue(4);
		frameCountField.setColumns(8);
		frameCountField.getDocument().addDocumentListener(this);
		optionsPane.add(frameCountField, generateConstraints());
		
		addLabel(optionsPane, "Frame width");
		frameWidthField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		frameWidthField.setValue(32);
		frameWidthField.setColumns(12);
		frameWidthField.getDocument().addDocumentListener(this);
		optionsPane.add(frameWidthField, generateConstraints());
		
		addLabel(optionsPane, "Frame height");
		frameHeightField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		frameHeightField.setValue(32);
		frameHeightField.setColumns(12);
		frameHeightField.getDocument().addDocumentListener(this);
		optionsPane.add(frameHeightField, generateConstraints());
		
		addLabel(optionsPane, "Animation speed (fps)");
		fpsField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		fpsField.setValue(4);
		fpsField.setColumns(8);
		fpsField.getDocument().addDocumentListener(this);
		optionsPane.add(fpsField, generateConstraints());
		
		ax1Fields = new ArrayList<JFormattedTextField>();
		ax2Fields = new ArrayList<JFormattedTextField>();
		ay1Fields = new ArrayList<JFormattedTextField>();
		ay2Fields = new ArrayList<JFormattedTextField>();
		for (int i = 0; i < 4; i++) {
			String dirString = "NULL";
			if (i == LEFT_DIR) dirString = "Left";
			if (i == RIGHT_DIR) dirString = "Right";
			if (i == DOWN_DIR) dirString = "Down";
			if (i == UP_DIR) dirString = "Up";
			for (int j = 0; j < 4; j++) {
				String coordString = "NULL";
				if (j == 0) coordString = "x1";
				if (j == 1) coordString = "y1";
				if (j == 2) coordString = "x2";
				if (j == 3) coordString = "y2";
				addLabel(optionsPane, dirString + " hitbox " + coordString);
				JFormattedTextField field = new JFormattedTextField(NumberFormat.getIntegerInstance());
				field.setColumns(12);
				field.setValue(0);
				field.getDocument().addDocumentListener(this);
				optionsPane.add(field, generateConstraints());
				if (j == 0) ax1Fields.add(field);
				if (j == 1) ay1Fields.add(field);
				if (j == 2) ax2Fields.add(field);
				if (j == 3) ay2Fields.add(field);
			}
		}
		
		c = new GridBagConstraints();
		c.weighty = 99;
		c.gridy = gridYIndex;
		optionsPane.add(new JPanel(), c);
		
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
		c.ipady = 1;
		c.ipadx = 1;
		c.insets = new Insets(0, 10, 10, 10);
		gridYIndex += 1;
		return c;
	}
	
	private void addLabel(JPanel panel, String str) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = gridYIndex;
		//c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.ipady = 1;
		c.ipadx = 1;
		c.insets = new Insets(6, 10, 2, 10);
		gridYIndex += 1;
		JLabel label = new JLabel(str);
		panel.add(label, c);
	}
	
	private boolean imageSelected() {
		return image != null;
	}
	
	private int getFrameCount() { return Integer.valueOf(frameCountField.getValue().toString()); }
	private int getFrameWidth() { return Integer.valueOf(frameWidthField.getValue().toString()); }
	private int getFrameHeight() { return Integer.valueOf(frameHeightField.getValue().toString()); }
	private int getFieldVal(JFormattedTextField field) { return Integer.valueOf(field.getValue().toString()); }

	@Override public void insertUpdate(DocumentEvent e) { repaint(); }
	@Override public void removeUpdate(DocumentEvent e) { repaint(); }
	@Override public void changedUpdate(DocumentEvent e) { repaint(); }
	
	@Override public void mouseMoved(MouseEvent e) { }
	@Override public void mouseEntered(MouseEvent e) { }
	@Override public void mouseExited(MouseEvent e) { }
	@Override public void mouseClicked(MouseEvent e) { }

	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragging) {
			dragToX = e.getX() - dragFrame * getFrameWidth();
			dragToY = e.getY() - dragDir * getFrameHeight();
			repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int frame = (int) Math.floor((float) e.getX() / (float) getFrameWidth());
		int dir = (int) Math.floor((float) e.getY() / (float) getFrameHeight());
		if (frame >= 0 && frame < getFrameCount() && dir >= 0 && dir < 4) {
			dragging = true;
			dragDir = dir;
			dragFrame = frame;
			dragStartX = e.getX() - dragFrame * getFrameWidth();
			dragStartY = e.getY() - dragDir * getFrameHeight();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		dragging = false;
		int x1 = (dragToX > dragStartX) ? dragStartX : dragToX;
		int x2 = (dragToX < dragStartX) ? dragStartX : dragToX;
		int y1 = (dragToY > dragStartY) ? dragStartY : dragToY;
		int y2 = (dragToY < dragStartY) ? dragStartY : dragToY;
		if (!attackModeEnabled()) {
			ax1Fields.get(dragDir).setValue(x1);
			ax2Fields.get(dragDir).setValue(x2);
			ay1Fields.get(dragDir).setValue(y1);
			ay2Fields.get(dragDir).setValue(y2);
		} else {
			int[][] attackData = hitboxCoords.get(dragFrame);
			attackData[dragDir][0] = x1;
			attackData[dragDir][1] = y1;
			attackData[dragDir][2] = x2;
			attackData[dragDir][3] = y2;
		}
		repaint();
	}
	
	private void export() {
		String name = (String) JOptionPane.showInputDialog(
				this,
				"Enter the name of this sprite (eg \"talyssa_run\", \"enemy_hunter\")",
				"Export sprite...",
				JOptionPane.PLAIN_MESSAGE,
				null, null, null);
		String subfolder = (String) JOptionPane.showInputDialog(
				this,
				"Enter the subfolder/type of this sprite (eg \"charas/talyssa\", \"enemies\")",
				"Export sprite...",
				JOptionPane.PLAIN_MESSAGE,
				null, null, null);
		// TODO: check for conflicts in naming ?
		
		// check for cancel
		if (name == "" || name == null || subfolder == null) {
			return;
		}
		
		boolean writeAttacks = false;
		for (int[][] attackData : hitboxCoords) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (attackData[i][j] != 0) {
						writeAttacks = true;
						break;
					}
				}
			}
		}
		
		List<AnimationMDO> animMDOs = new ArrayList<AnimationMDO>();
		for (int dir = 0; dir < 4; dir++) {
			String dirString = "NULL";
			if (dir == LEFT_DIR) dirString = "left";
			if (dir == UP_DIR) dirString = "up";
			if (dir == RIGHT_DIR) dirString = "right";
			if (dir == DOWN_DIR) dirString = "down";
			AnimationMDO mdo = new AnimationMDO();
			mdo.key = "anim_" + name + "_" + dirString;
			mdo.description = "Autogenerated by four dir sprite wizard";
			mdo.subfolder = subfolder;
			mdo.animSpeed = getFieldVal(fpsField);
			mdo.file = fileSelect.getSelectedItem().toString();
			mdo.frameCount = getFieldVal(frameCountField);
			mdo.frameWidth = getFieldVal(frameWidthField);
			mdo.frameHeight = getFieldVal(frameHeightField);
			mdo.hit1x = getFieldVal(ax1Fields.get(dir));
			mdo.hit1y = getFieldVal(ay1Fields.get(dir));
			mdo.hit2x = getFieldVal(ax2Fields.get(dir));
			mdo.hit2y = getFieldVal(ay2Fields.get(dir));
			mdo.mode = AnimationType.REPEAT;
			mdo.offX = 0;
			mdo.offY = dir * mdo.frameHeight;
			
			if (writeAttacks) {
				mdo.attackBoxes = new DynamicBoxMDO[mdo.frameCount];
				for (int frame = 0; frame < mdo.frameCount; frame++) {
					int[][] attackData = hitboxCoords.get(frame);
					DynamicBoxMDO box = new DynamicBoxMDO();
					box.x1 = attackData[dir][0];
					box.y1 = attackData[dir][1];
					box.x2 = attackData[dir][2];
					box.y2 = attackData[dir][3];
					mdo.attackBoxes[frame] = box;
				}
			}
			
			animMDOs.add(mdo);
			frame.getLogic().getOut().writeNewSchema(mdo);
		}
		
		FourDirMDO mdo = new FourDirMDO();
		mdo.description = "Autogenerated by four dir sprite wizard";
		mdo.subfolder = subfolder;
		mdo.key = "4dir_" + name;
		mdo.leftAnim = "anim_" + name + "_left";
		mdo.rightAnim = "anim_" + name + "_right";
		mdo.upAnim = "anim_" + name + "_up";
		mdo.downAnim = "anim_" + name + "_down";		
		frame.getLogic().getOut().writeNewSchema(mdo);
		
		JOptionPane.showMessageDialog(this,
				"Export complete.\n",
				"Export Complete",
				JOptionPane.INFORMATION_MESSAGE);
		setVisible(false);
	}
	
	private String mirror(String old) {
		int oldNum = Integer.valueOf(old);
		int newNum = mirror2(oldNum);
		return (new Integer(newNum)).toString();
	}
	
	private int mirror2(int oldNum) {
		int width = Integer.valueOf(frameWidthField.getText());
		return width - oldNum;
	}
	
	private boolean attackModeEnabled() {
		return radioAttackMode.isSelected();
	}
	
	private void mirrorHoriz() {
		ax1Fields.get(RIGHT_DIR).setText(mirror(ax2Fields.get(LEFT_DIR).getText()));
		ax2Fields.get(RIGHT_DIR).setText(mirror(ax1Fields.get(LEFT_DIR).getText()));
		ay1Fields.get(RIGHT_DIR).setText(ay1Fields.get(LEFT_DIR).getText());
		ay2Fields.get(RIGHT_DIR).setText(ay2Fields.get(LEFT_DIR).getText());
		for (int[][] attackData : hitboxCoords) {
			attackData[RIGHT_DIR][0] = mirror2(attackData[LEFT_DIR][2]);
			attackData[RIGHT_DIR][1] = attackData[LEFT_DIR][1];
			attackData[RIGHT_DIR][2] = mirror2(attackData[LEFT_DIR][0]);
			attackData[RIGHT_DIR][3] = attackData[LEFT_DIR][3];
		}
	}
	
	private void mirrorVert() {
		ax1Fields.get(UP_DIR).setText(ax1Fields.get(DOWN_DIR).getText());
		ax2Fields.get(UP_DIR).setText(ax2Fields.get(DOWN_DIR).getText());
		ay1Fields.get(UP_DIR).setText(ay1Fields.get(DOWN_DIR).getText());
		ay2Fields.get(UP_DIR).setText(ay2Fields.get(DOWN_DIR).getText());
		for (int[][] attackData : hitboxCoords) {
			attackData[UP_DIR][0] = attackData[DOWN_DIR][0];
			attackData[UP_DIR][1] = attackData[DOWN_DIR][1];
			attackData[UP_DIR][2] = attackData[DOWN_DIR][2];
			attackData[UP_DIR][3] = attackData[DOWN_DIR][3];
		}
	}
	
	private void ensureSize() {
		int newFrames = getFieldVal(frameCountField);
		while (hitboxCoords.size() < newFrames) {
			int temp[][] = new int[4][4];
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					temp[i][j] = 0;
				}
			}
			hitboxCoords.add(temp);
		}
	}

}
