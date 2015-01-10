/**
 *  CharaInfoScreen.java
 *  Created on Apr 11, 2014 6:38:33 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.ui.CharaSelector;
import net.wombatrpgs.saga.ui.CharaSelector.SelectionListener;
import net.wombatrpgs.saga.ui.DescriptionBox;
import net.wombatrpgs.saga.ui.ItemSelector;
import net.wombatrpgs.saga.ui.CharaInsert;
import net.wombatrpgs.saga.ui.CharaInsertFull;
import net.wombatrpgs.saga.ui.SlotListener;
import net.wombatrpgs.saga.ui.StatsBar;

/**
 * Displays detailed stats on a character.
 */
public class ScreenCharaInfo extends SagaScreen implements TargetSelectable {
	
	protected static final int HEADER_WIDTH = 178;
	protected static final int HEADER_HEIGHT = 52;
	protected static final int ABILS_WIDTH = 130;
	protected static final int ABILS_HEIGHT = 108;
	protected static final int ABILS_EDGE_PADDING = 12;
	protected static final int ABILS_LIST_PADDING = 4;
	protected static final int INSERTS_PADDING = 3;
	protected static final int DESCRIPTION_HEIGHT = 36;
	
	protected Chara chara;
	
	protected Nineslice headerBG, abilsBG;
	protected StatsBar stats;
	protected DescriptionBox description;
	protected CharaInsert header;
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
		
		stats = new StatsBar(chara);
		assets.add(stats);
		
		headerBG = new Nineslice(HEADER_WIDTH, HEADER_HEIGHT);
		assets.add(headerBG);
		abilsBG = new Nineslice(ABILS_WIDTH + stats.getBorderWidth(),
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
		
		description = new DescriptionBox(HEADER_WIDTH, DESCRIPTION_HEIGHT);
		assets.add(description);
		
		globalX = (getWidth() - HEADER_WIDTH) / 2;
		globalY = -(getHeight() - (DESCRIPTION_HEIGHT + HEADER_HEIGHT + stats.getHeight())) / 2;
		
		stats.setX(globalX);
		stats.setY(globalY + getHeight() - HEADER_HEIGHT -
				stats.getHeight() + headerBG.getBorderHeight());
		
		description.setX(globalX);
		description.setY(stats.getY() - (description.getHeight() - headerBG.getBorderHeight()));
		
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
		description.render(batch);
		abilsBG.renderAt(batch, globalX + stats.getWidth() - stats.getBorderWidth(),
				globalY + getHeight() - HEADER_HEIGHT - stats.getHeight() +
				stats.getBorderHeight());
		stats.render(batch);
		headerBG.renderAt(batch, globalX, globalY + getHeight() - HEADER_HEIGHT);
		header.render(batch);
		if (showInserts) {
			inserts.render(batch);
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
						MGlobal.audio.playSFX(SConstants.SFX_FAIL);
						return false;
					}
					MGlobal.audio.playSFX(SConstants.SFX_CURE);
					item.onMapUse(parent);
				}
				return false;
			}
		}, true);
		abils.attachHoverListener(new SlotListener() {
			@Override public boolean onSelection(int selected) {
				CombatItem selectedItem = chara.getInventory().get(selected);
				description.describe(selectedItem);
				return false;
			}
		});
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
		headerX = (HEADER_WIDTH - header.getWidth()) / 2;
		headerY = getHeight() - HEADER_HEIGHT + (HEADER_HEIGHT - header.getHeight()) / 2;
		header.refresh();
		header.setX(globalX + headerX);
		header.setY(globalY + headerY);
		
		abils.setX(globalX + stats.getWidth() - stats.getBorderWidth()/2 +
				(ABILS_WIDTH - abils.getWidth()) / 2);
		abils.setY(globalY + getHeight() - HEADER_HEIGHT - ABILS_EDGE_PADDING/2 -
				abils.getHeight() - headerBG.getBorderHeight());
	}

}
