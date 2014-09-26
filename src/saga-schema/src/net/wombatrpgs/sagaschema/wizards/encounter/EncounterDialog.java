/**
 *  EncounterDialog.java
 *  Created on Sep 7, 2014 1:50:06 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.wizards.encounter;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import net.wombatrpgs.mgnse.Global;
import net.wombatrpgs.mgnse.MainFrame;
import net.wombatrpgs.mgnse.tree.SchemaNode;
import net.wombatrpgs.sagaschema.rpg.chara.CharaMDO;
import net.wombatrpgs.sagaschema.rpg.encounter.EncounterMDO;
import net.wombatrpgs.sagaschema.rpg.encounter.EncounterSetMDO;
import net.wombatrpgs.sagaschema.rpg.encounter.data.EncounterMemberMDO;
import net.wombatrpgs.sagaschema.rpg.encounter.data.EncounterSetMemberMDO;

/**
 * Swing dialog for the encounter wizard.
 */
public class EncounterDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private static final int MONSTER_MAX = 14;
	private static final String NO_MONSTER = "[no monster]";
	
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 670;
	
	private int gridYIndex;
	
	private MainFrame frame;
	private JFormattedTextField levelField, mcountField, setcountField;
	private JButton levelButton, exportButton;
	
	private ArrayList<LevelBox> boxes;
	private Map<String, EncounterInfo> infoMap;
	private float monsterCount;
	private int level;
	private int loadedMons;
	private int sets;

	public EncounterDialog(MainFrame frame) {
		super(frame, "Encounter Set Wizard");
		this.frame = frame;
		init();
	}
	
	public void init() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		
		int treeHorizPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int treeVertPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		pane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		pane.setAlignmentY(JComponent.TOP_ALIGNMENT);
		
		JScrollPane scroll = new JScrollPane(pane, treeHorizPolicy, treeVertPolicy);
		scroll.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		scroll.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		scroll.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		scroll.setAlignmentY(JComponent.TOP_ALIGNMENT);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.VERTICAL;
		c.ipadx = 32;
		c.ipady = 32;
		add(scroll, c);
		
		addLabel(pane, "Encounter level (meat transform level)");
		levelField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		levelField.setValue(0);
		levelField.setColumns(8);
		pane.add(levelField, generateConstraints(false));
		
		levelButton = new JButton("Enter Level");
		levelButton.addActionListener(this);
		pane.add(levelButton, generateConstraints(false));
		
		boxes = new ArrayList<LevelBox>();
		for (int i = -1; i < MONSTER_MAX; i += 1) {
			
			GridBagConstraints c1 = generateConstraints(true);
			c1.gridwidth = 1;
			c1.gridx = 0;
			c1.weightx = 0;
			gridYIndex -= 1;
			GridBagConstraints c2 = generateConstraints(true);
			c2.gridwidth = 1;
			c2.gridx = 1;
			c2.weightx = 1;
			c2.fill = GridBagConstraints.NONE;
			
			if (i >= 0) {
				LevelBox box = new LevelBox();
				boxes.add(box);
				box.label = new JLabel(NO_MONSTER);
				box.field = new JFormattedTextField(NumberFormat.getIntegerInstance());
				box.field.setColumns(8);
				box.field.setMinimumSize(new Dimension(100, 0));
				box.field.addActionListener(this);
				pane.add(box.label, c1);
				pane.add(box.field, c2);
			} else {
				JLabel label1 = new JLabel("Monster name");
				JLabel label2 = new JLabel("Danger level");
				pane.add(label1, c1);
				pane.add(label2, c2);
			}
		}
		
		addLabel(pane, "Average monsters per encounter target");
		mcountField = new JFormattedTextField(NumberFormat.getNumberInstance());
		mcountField.setValue(0);
		mcountField.setColumns(8);
		c = generateConstraints(false);
		c.insets.bottom += 12;
		pane.add(mcountField, c);
		
		addLabel(pane, "Encounter sets desired");
		setcountField = new JFormattedTextField(NumberFormat.getNumberInstance());
		setcountField.setValue(0);
		setcountField.setColumns(8);
		c = generateConstraints(false);
		c.insets.bottom += 12;
		pane.add(setcountField, c);
		
		exportButton = new JButton("Export and finish");
		exportButton.addActionListener(this);
		pane.add(exportButton, generateConstraints(false));
		
		pack();
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == levelButton) {
			for (LevelBox box : boxes) {
				box.label.setText(NO_MONSTER);
				box.field.setValue(0);
				box.mdo = null;
			}
			level = Integer.valueOf(levelField.getValue().toString());
			loadedMons = 0;
			recursivelyAdd(Global.instance().getNode(CharaMDO.class));
		} else if (e.getSource() == exportButton) {
			execute();
		}
		repaint();
	}
	
	private void addLabel(JPanel panel, String str) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = gridYIndex;
		c.weightx = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.ipady = 1;
		c.ipadx = 1;
		c.gridwidth = 2;
		c.insets = new Insets(16, 16, 0, 0);
		gridYIndex += 1;
		JLabel label = new JLabel(str);
		panel.add(label, c);
	}
	
	private GridBagConstraints generateConstraints(boolean small) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = gridYIndex;
		c.weightx = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.ipady = 1;
		c.ipadx = 1;
		c.gridwidth = 2;
		if (small) {
			c.insets = new Insets(1, 32, 1, 32);
		} else {
			c.insets = new Insets(12, 32, 12, 32);
		}
		gridYIndex += 1;
		return c;
	}
	
	private void recursivelyAdd(SchemaNode node) {
		if (node.isLeaf()) {
			CharaMDO mdo = (CharaMDO) frame.getLogic().getIn().instantiateData(
					CharaMDO.class,
					node.getFile());
			if (mdo.meatTargetLevel != null && mdo.meatTargetLevel == level && mdo.gp > 0) {
				LevelBox box = boxes.get(loadedMons);
				box.label.setText(mdo.name);
				box.field.setValue(1);
				box.mdo = mdo;
				loadedMons += 1;
			}
		} else {
			for (int i = 0; i < node.getChildCount(); i++) {
				recursivelyAdd((SchemaNode) node.getChildAt(i));
			}
		}
	}
	
	private int danger(int monsterIndex) {
		return Integer.valueOf(boxes.get(monsterIndex).field.getValue().toString());
	}
	
	private void nameEncounter(EncounterMDO mdo) {
		String levelString = String.valueOf(level);
		while (levelString.length() < 2) levelString = "0" + levelString;
		mdo.key = "encounter_lvl" + levelString;
		for (EncounterMemberMDO member : mdo.members) {
			mdo.key += "_";
			int index = member.enemy.lastIndexOf('_');
			mdo.key += member.enemy.substring(index + 1);
			if (!member.amount.equals("1-1")) {
				mdo.key += member.amount;
			}
		}
	}
	
	private EncounterSetMDO cloneAndSwap(EncounterSetMDO mdo, int index1, int index2) {
		EncounterSetMDO clone = new EncounterSetMDO();
		clone.key = mdo.key;
		clone.subfolder = mdo.subfolder;
		int length = mdo.encounters.length;
		clone.encounters = new EncounterSetMemberMDO[length];
		
		for (int i = 0; i < length; i += 1) {
			clone.encounters[i] = new EncounterSetMemberMDO();
			clone.encounters[i].encounter = mdo.encounters[i].encounter;
			clone.encounters[i].weight = mdo.encounters[i].weight;
		}
		clone.encounters[index1].encounter = mdo.encounters[index2].encounter;
		clone.encounters[index2].encounter = mdo.encounters[index1].encounter;
		
		return clone;
	}
	
	private float error(EncounterSetMDO mdo, float danger, float monsters) {
		float weight = 0;
		float thisMonsters = 0;
		float thisDanger = 0;
		for (EncounterSetMemberMDO member : mdo.encounters) {
			EncounterInfo info = infoMap.get(member.encounter);
			weight += member.weight;
			thisMonsters += info.count * member.weight;
			thisDanger += info.danger * member.weight;
		}
		thisMonsters /= weight;
		thisDanger /= weight;
		return Math.abs(monsters - thisMonsters) + Math.abs(danger - thisDanger);
	}
	
	private void execute() {
		monsterCount = Float.valueOf(mcountField.getValue().toString());
		sets = Integer.valueOf(setcountField.getValue().toString());
		Random r = new Random();
		
		String levelString = String.valueOf(level);
		while (levelString.length() < 2) levelString = "0" + levelString;
		
		List<EncounterInfo> allEncounters = new ArrayList<EncounterInfo>();
		List<String> unusedEncounters = new ArrayList<String>();
		
		int maxPerGroup = Math.round(Math.max(Math.min(monsterCount * 2.8f, 8), 4));
		int homoGroups = Math.round(Math.min(monsterCount * 2f + 1, 5));
		int doubleGroups = Math.round(Math.min(monsterCount * 1.5f + 2, 5));
		int tripleGroups = Math.round(Math.min(monsterCount * 1f + 2, 5));
		int totalGroups = loadedMons + homoGroups + doubleGroups + tripleGroups;
		
		for (int i = 0; i < loadedMons; i += 1) {
			EncounterInfo info = new EncounterInfo();
			allEncounters.add(info);
			info.count = 1;
			info.danger = Integer.valueOf(boxes.get(i).field.getValue().toString());
			CharaMDO monster = boxes.get(i).mdo;
			EncounterMemberMDO member = new EncounterMemberMDO();
			member.amount = "1-1";
			member.enemy = monster.key;
			info.mdo.members = new EncounterMemberMDO[] { member };
			nameEncounter(info.mdo);
		}
		
		for (int i = 0; i < homoGroups; i += 1) {
			int low = (maxPerGroup-2);
			int hi = (maxPerGroup);
			EncounterInfo info = new EncounterInfo();
			allEncounters.add(info);
			info.count = (float) (low + hi) / 2f;
			info.danger = danger(i);
			CharaMDO monster = boxes.get(i).mdo;
			EncounterMemberMDO member = new EncounterMemberMDO();
			member.amount = low + "-" + hi;
			member.enemy = monster.key;
			info.mdo.members = new EncounterMemberMDO[] { member };
			nameEncounter(info.mdo);
		}
		
		for (int i = 0; i < doubleGroups; i += 1) {
			int low = 2;
			int hi = (int) Math.floor(maxPerGroup/2f) + 1;
			EncounterInfo info = new EncounterInfo();
			allEncounters.add(info);
			info.count = (float) (low + hi) / 2f;
			info.count += (float) (0 + hi-2) / 2f;
			int index1 = r.nextInt(loadedMons);
			int index2;
			do {
				index2 = r.nextInt(loadedMons);
			} while (index1 == index2);
			info.danger = (danger(index1) + danger(index2)) / 2;
			CharaMDO monster1 = boxes.get(index1).mdo;
			CharaMDO monster2 = boxes.get(index2).mdo;
			EncounterMemberMDO member1 = new EncounterMemberMDO();
			EncounterMemberMDO member2 = new EncounterMemberMDO();
			member1.amount = low + "-" + hi;
			member2.amount = 0 + "-" + (hi-2);
			member1.enemy = monster1.key;
			member2.enemy = monster2.key;
			info.mdo.members = new EncounterMemberMDO[] { member1, member2 };
			nameEncounter(info.mdo);
		}
		
		for (int i = 0; i < tripleGroups; i += 1) {
			int low = 2;
			int hi = (int) Math.floor(maxPerGroup/2f) + 1;
			EncounterInfo info = new EncounterInfo();
			allEncounters.add(info);
			info.count = (float) (low + hi) / 2f;
			info.count += (float) (0 + hi-2) / 2f;
			info.count += (float) (0 + hi-2) / 2f;
			int index1 = r.nextInt(loadedMons);
			int index2, index3;
			do {
				index2 = r.nextInt(loadedMons);
			} while (index1 == index2);
			do {
				index3 = r.nextInt(loadedMons);
			} while (index1 == index3 || index2 == index3);
			info.danger = (danger(index1) + danger(index2) + danger(index3)) / 3;
			CharaMDO monster1 = boxes.get(index1).mdo;
			CharaMDO monster2 = boxes.get(index2).mdo;
			CharaMDO monster3 = boxes.get(index3).mdo;
			EncounterMemberMDO member1 = new EncounterMemberMDO();
			EncounterMemberMDO member2 = new EncounterMemberMDO();
			EncounterMemberMDO member3 = new EncounterMemberMDO();
			member1.amount = low + "-" + hi;
			member2.amount = 0 + "-" + (hi-2);
			member3.amount = 0 + "-" + (hi-2);
			member1.enemy = monster1.key;
			member2.enemy = monster2.key;
			member3.enemy = monster3.key;
			info.mdo.members = new EncounterMemberMDO[] { member1, member2, member3 };
			nameEncounter(info.mdo);
		}
		
		infoMap = new HashMap<String, EncounterInfo>();
		for (EncounterInfo info : allEncounters) {
			infoMap.put(info.mdo.key, info);
			unusedEncounters.add(info.mdo.key);
		}
		
		List<EncounterSetMDO> results = new ArrayList<EncounterSetMDO>();
		
		for (int i = 0; i < sets; i += 1) {
			Collections.shuffle(allEncounters);
			float targetDanger = 1f;
			float targetMonsters = (float) monsterCount;
			if (sets > 1) {
				targetDanger += (float) i / (float) (sets-1);
				targetMonsters += (float) i / (float) (sets-1);
			}
			EncounterSetMDO mdo = new EncounterSetMDO();
			String dangerString = String.valueOf(i);
			while (dangerString.length() < 2) dangerString = "0" + dangerString;
			mdo.key = "encounterset_level" + levelString + "_danger" + dangerString;
			mdo.subfolder = "level" + levelString;
			
			mdo.encounters = new EncounterSetMemberMDO[totalGroups];
			for (int j = 0; j < totalGroups; j += 1) {
				mdo.encounters[j] = new EncounterSetMemberMDO();
				mdo.encounters[j].encounter = allEncounters.get(j).mdo.key;
				switch (j) {
				case 0:		mdo.encounters[j].weight = 48;		break;
				case 1:		mdo.encounters[j].weight = 48;		break;
				case 2:		mdo.encounters[j].weight = 32;		break;
				case 3:		mdo.encounters[j].weight = 32;		break;
				case 4:		mdo.encounters[j].weight = 32;		break;
				case 5:		mdo.encounters[j].weight = 32;		break;
				case 6:		mdo.encounters[j].weight = 16;		break;
				case 7:		mdo.encounters[j].weight = 8;		break;
				case 8:		mdo.encounters[j].weight = 4;		break;
				case 9:		mdo.encounters[j].weight = 2;		break;
				case 10:	mdo.encounters[j].weight = 2;		break;
				default:	mdo.encounters[j].weight = 0;		break;
				}
			}
			
			EncounterSetMDO best = mdo;
			float bestError = error(mdo, targetDanger, targetMonsters);
			for (int attempt = 0; attempt < 100; attempt += 1) {
				for (int i1 = 0; i1 < totalGroups; i1 += 1) {
					for (int i2 = i1+1; i2 < totalGroups; i2 += 1) {
						EncounterSetMDO candidate = cloneAndSwap(best, i1, i2);
						float error = error(candidate, targetDanger, targetMonsters);
						if (error < bestError) {
							best = candidate;
							bestError = error;
						}
					}
				}
			}
			
			for (EncounterSetMemberMDO member : best.encounters) {
				unusedEncounters.remove(member.encounter);
			}
			best.encounters = Arrays.copyOf(best.encounters, 11);
			results.add(best);
		}
		
		for (EncounterInfo info : allEncounters) {
			if (unusedEncounters.contains(info.mdo.key)) continue;
			info.mdo.subfolder = "level" + levelString;
			info.mdo.description = "Autogenerated by encounter wizard";
			info.mdo.name = info.mdo.key;
			frame.getLogic().getOut().writeNewSchema(info.mdo);
		}
		for (EncounterSetMDO mdo : results) {
			mdo.steps = 42;
			mdo.name = mdo.key;
			mdo.description = "Autogenerated by encounter wizard";
			frame.getLogic().getOut().writeNewSchema(mdo);
		}
		
		JOptionPane.showMessageDialog(this,
				"Generation complete.\n",
				"Generation Complete",
				JOptionPane.INFORMATION_MESSAGE);
		setVisible(false);
	}
	
	private class LevelBox {
		public JFormattedTextField field;
		public JLabel label;
		public CharaMDO mdo;
	}
	
	private class EncounterInfo {
		public EncounterMDO mdo;
		public float count;
		public int danger;
		public EncounterInfo() {
			mdo = new EncounterMDO();
		}
	}
	
}
