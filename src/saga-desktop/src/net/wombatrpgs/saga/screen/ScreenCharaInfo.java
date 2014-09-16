/**
 *  CharaInfoScreen.java
 *  Created on Apr 11, 2014 6:38:33 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextFormat;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.ui.CharaSelector;
import net.wombatrpgs.saga.ui.CharaSelector.SelectionListener;
import net.wombatrpgs.saga.ui.ItemSelector;
import net.wombatrpgs.saga.ui.ItemSelector.SlotListener;
import net.wombatrpgs.saga.ui.CharaInsert;
import net.wombatrpgs.saga.ui.CharaInsertFull;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Displays detailed stats on a character.
 */
public class ScreenCharaInfo extends SagaScreen implements TargetSelectable {
	
	protected static final int HEADER_WIDTH = 160;
	protected static final int HEADER_HEIGHT = 48;
	protected static final int STATS_WIDTH = 48;
	protected static final int STATS_HEIGHT = 108;
	protected static final int STATS_PADDING = 8;
	protected static final int ABILS_WIDTH = 112;
	protected static final int ABILS_HEIGHT = 108;
	protected static final int ABILS_EDGE_PADDING = 12;
	protected static final int ABILS_LIST_PADDING = 3;
	protected static final int INSERTS_PADDING = 3;
	
	protected Chara chara;
	
	protected Nineslice headerBG, statsBG, abilsBG;
	protected CharaInsert header;
	protected TextFormat labelFormat, statFormat;
	protected List<Stat> statDisplay;
	protected ItemSelector abils;
	protected CharaSelector inserts;
	protected boolean showInserts;
	protected int headerX, headerY;
	protected int globalX, globalY;
	
	/**
	 * Default constructor, sets up the display.
	 * @param	chara			The character to display info on
	 */
	public ScreenCharaInfo(Chara chara) {
		this.chara = chara;
		pushCommandContext(new CMapMenu());
		
		headerBG = new Nineslice(HEADER_WIDTH, HEADER_HEIGHT);
		assets.add(headerBG);
		statsBG = new Nineslice(STATS_WIDTH,
				STATS_HEIGHT + headerBG.getBorderHeight());
		assets.add(statsBG);
		abilsBG = new Nineslice(ABILS_WIDTH + statsBG.getBorderWidth(),
				ABILS_HEIGHT + headerBG.getBorderHeight());
		assets.add(abilsBG);
		header = new CharaInsertFull(chara, false);
		assets.add(header);
		addUChild(header);
		abils = new ItemSelector(chara.getInventory(), chara.getInventory().slotCount(),
				ABILS_WIDTH - ABILS_EDGE_PADDING * 2, ABILS_LIST_PADDING,
				false, false);
		assets.add(abils);
		inserts = new CharaSelector(SGlobal.heroes, false, false, true, INSERTS_PADDING);
		assets.add(inserts);
		addUChild(inserts);
		
		globalX = (getWidth() - HEADER_WIDTH) / 2;
		globalY = -(getHeight() - (HEADER_HEIGHT + STATS_HEIGHT - 
				statsBG.getBorderHeight())) / 2;
		
		statDisplay = new ArrayList<Stat>();
		statDisplay.add(Stat.STR);
		statDisplay.add(Stat.AGI);
		statDisplay.add(Stat.DEF);
		statDisplay.add(Stat.MANA);
		
		showInserts = false;
		createDisplay();
	}
	
	/**
	 * @see net.wombatrpgs.saga.screen.TargetSelectable#getUser()
	 */
	@Override
	public Chara getUser() {
		return chara;
	}

	/**
	 * @see net.wombatrpgs.saga.screen.TargetSelectable#awaitSelection
	 * (net.wombatrpgs.saga.ui.CharaSelector.SelectionListener)
	 */
	@Override
	public void awaitSelection(final SelectionListener listener) {
		inserts.awaitSelection(new SelectionListener() {
			@Override public boolean onSelection(Chara selected) {
				if (selected == null) {
					showInserts = false;
				}
				return listener.onSelection(selected);
			}
		}, true);
		showInserts = true;
	}

	/**
	 * @see net.wombatrpgs.saga.screen.TargetSelectable#refresh()
	 */
	@Override
	public void refresh() {
		inserts.refresh();
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#postProcessing
	 * (net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		inserts.setX(globalX);
		inserts.setY(globalY + getHeight() - HEADER_HEIGHT);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		abilsBG.renderAt(batch, globalX + STATS_WIDTH - statsBG.getBorderWidth(),
				globalY + getHeight() - HEADER_HEIGHT - STATS_HEIGHT);
		statsBG.renderAt(batch, globalX, globalY + getHeight() - HEADER_HEIGHT -
				STATS_HEIGHT);
		headerBG.renderAt(batch, globalX, globalY + getHeight() - HEADER_HEIGHT);
		header.render(batch);
		if (showInserts) {
			inserts.render(batch);
		}
		
		FontHolder font = MGlobal.ui.getFont();
		for (int i = 0; i < statDisplay.size(); i += 1) {
			Stat stat = statDisplay.get(i);
			int offY = (int) (-i * (font.getLineHeight()*2 + STATS_PADDING));
			font.draw(batch, labelFormat, stat.getLabel(), offY);
			font.draw(batch, statFormat, (int) chara.get(stat) + "", offY);
		}
		
		abils.render(batch);
		
		super.render(batch);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		final ScreenCharaInfo parent = this;
		abils.awaitSelection(new SlotListener() {
			@Override public boolean onSelection(int selected) {
				if (selected == -1) {
					MGlobal.screens.pop();
					return true;
				} else {
					CombatItem item = chara.getInventory().get(selected);
					if (item == null || !item.isMapUsable()) {
						MGlobal.sfx.play(SConstants.SFX_FAIL);
						return false;
					}
					MGlobal.sfx.play(SConstants.SFX_CURE);
					item.onMapUse(parent);
				}
				return false;
			}
		}, true);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (showInserts && !inserts.isActive()) {
			showInserts = false;
			return true;
		} else {
			return super.onCommand(command);
		}
	}

	/**
	 * Creates the header/stats display.
	 */
	protected void createDisplay() {
		FontHolder font = MGlobal.ui.getFont();
		headerX = (HEADER_WIDTH - header.getWidth()) / 2;
		headerY = getHeight() - HEADER_HEIGHT + (HEADER_HEIGHT - header.getHeight()) / 2;
		header.refresh();
		header.setX(globalX + headerX);
		header.setY(globalY + headerY);
		
		labelFormat = new TextFormat();
		labelFormat.align = HAlignment.LEFT;
		labelFormat.width = STATS_WIDTH - statsBG.getBorderWidth()*2;
		labelFormat.height = 80;
		labelFormat.x = globalX + statsBG.getBorderWidth();
		labelFormat.y = globalY + getHeight() - (HEADER_HEIGHT + statsBG.getBorderHeight() / 2);
		
		statFormat = new TextFormat();
		statFormat.align = HAlignment.RIGHT;
		statFormat.width = STATS_WIDTH - statsBG.getBorderWidth()*5/2;
		statFormat.height = 80;
		statFormat.x = globalX + statsBG.getBorderWidth();
		statFormat.y = globalY + (int) (getHeight() - (HEADER_HEIGHT +
				statsBG.getBorderHeight() / 2 + font.getLineHeight()));
		
		abils.setX(globalX + STATS_WIDTH - statsBG.getBorderWidth()/2 +
				(ABILS_WIDTH - abils.getWidth()) / 2);
		abils.setY(globalY + getHeight() - HEADER_HEIGHT - ABILS_EDGE_PADDING -
				abils.getHeight() - headerBG.getBorderHeight());
	}

}
