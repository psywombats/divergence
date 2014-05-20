/**
 *  EquipScreen.java
 *  Created on Apr 13, 2014 4:28:53 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.items.CharaInventory;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.rpg.items.PartyInventory;
import net.wombatrpgs.saga.ui.CharaInsert;
import net.wombatrpgs.saga.ui.CharaInsertFull;
import net.wombatrpgs.saga.ui.ItemSelector;
import net.wombatrpgs.saga.ui.ItemSelector.SlotListener;

/**
 * Equip items to heroes and strip them of their weapons to sell for ill-gotten
 * shopkeeper gold. Or something like that.
 */
public class ScreenEquip extends SagaScreen {
	
	protected static final int HEADER_WIDTH = 112;
	protected static final int HEADER_HEIGHT = 48;
	protected static final int ABILS_WIDTH = 112;
	protected static final int ABILS_HEIGHT = 100;
	protected static final int ITEMS_WIDTH = 112;
	protected static final int ITEMS_HEIGHT = 122;
	protected static final int ITEMS_EDGE_PADDING = 12;
	protected static final int ITEMS_LIST_PADDING = 3;
	
	protected Chara chara;
	protected PartyInventory inventory;
	protected CharaInventory equipped;
	
	protected Nineslice headerBG, abilsBG, itemsBG;
	protected CharaInsert header;
	protected ItemSelector abils, items;
	protected int globalX, globalY;
	protected int marked, lastRight;
	
	/**
	 * Creates a screen to equip a character.
	 * @param	chara			The character to equip
	 */
	public ScreenEquip(Chara chara) {
		this.chara = chara;
		equipped = chara.getInventory();
		inventory = SGlobal.heroes.getInventory();
		pushCommandContext(new CMapMenu());
		
		headerBG = new Nineslice(HEADER_WIDTH, HEADER_HEIGHT);
		abilsBG = new Nineslice(ABILS_WIDTH, ABILS_HEIGHT + headerBG.getBorderHeight());
		itemsBG = new Nineslice(ITEMS_WIDTH, ITEMS_HEIGHT + headerBG.getBorderHeight());
		assets.add(headerBG);
		assets.add(abilsBG);
		assets.add(itemsBG);
		
		globalX = (getWidth() - (ITEMS_WIDTH + ABILS_WIDTH - abilsBG.getBorderWidth())) / 2;
		globalY = (getHeight() - (ABILS_HEIGHT + HEADER_HEIGHT)) / 2;
		
		header = new CharaInsertFull(chara, false);
		header.setX(globalX + (HEADER_WIDTH - header.getWidth()) / 2);
		header.setY(globalY + ABILS_HEIGHT + (HEADER_HEIGHT - header.getHeight()) / 2);
		assets.add(header);
		addUChild(header);
		
		abils = new ItemSelector(equipped, equipped.slotCount(),
				ABILS_WIDTH - ITEMS_EDGE_PADDING * 2, ITEMS_LIST_PADDING,
				false, false);
		items = new ItemSelector(inventory, inventory.slotCount(),
				ITEMS_WIDTH - ITEMS_EDGE_PADDING * 2, ITEMS_LIST_PADDING,
				false, false);
		abils.setX(globalX + (ABILS_WIDTH - abils.getWidth()) / 2);
		abils.setY(globalY + (ABILS_HEIGHT - abils.getHeight()) / 2 - 5);
		items.setX(globalX + ABILS_WIDTH - abilsBG.getBorderWidth() + (ITEMS_WIDTH - items.getWidth()) / 2);
		items.setY(globalY + (ITEMS_HEIGHT - items.getHeight()) / 2 - 5);
		assets.add(abils);
		assets.add(items);
		
		lastRight = 0;
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		itemsBG.renderAt(batch, globalX + ABILS_WIDTH - abilsBG.getBorderWidth(), globalY);
		abilsBG.renderAt(batch, globalX, globalY);
		headerBG.renderAt(batch, globalX, globalY + ABILS_HEIGHT);
		header.render(batch);
		items.render(batch);
		abils.render(batch);
		super.render(batch);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		focusAbils();
	}
	
	/**
	 * Called when the player selects an equipment slot to equip to/from.
	 * @param	selected		The slot the played selected
	 * @return					True to unfocus the abils screen, else false
	 */
	protected boolean onEquipSlotSelect(int selected) {
		if (equipped.equippableAt(selected)) {
			abils.setIndent();
			marked = selected;
			items.awaitSelection(new SlotListener() {
				@Override public boolean onSelection(int selected) {
					if (selected != -1) {
						CombatItem left = equipped.get(marked);
						CombatItem right = inventory.get(selected);
						equipped.drop(left);
						inventory.drop(right);
						equipped.set(marked, right);
						inventory.set(selected, left);
						lastRight = selected;
					}
					focusAbils();
					abils.setSelected(marked);
					return true;
				}
			}, true);
			items.setSelected(lastRight);
			return true;
		} else {
			// TODO: sfx: failure sound
			return false;
		}
	}
	
	/**
	 * Goes back to the abilities await.
	 */
	protected void focusAbils() {
		abils.clearIndent();
		abils.awaitSelection(new SlotListener() {
			@Override public boolean onSelection(int selected) {
				if (selected == -1) {
					MGlobal.screens.pop();
					return true;
				} else {
					return onEquipSlotSelect(selected);
				}
			}
		}, true);
	}

}
