/**
 *  InventoryScreen.java
 *  Created on Apr 13, 2014 1:22:06 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.CombatItem;
import net.wombatrpgs.saga.rpg.Inventory;
import net.wombatrpgs.saga.ui.ItemSelector;
import net.wombatrpgs.saga.ui.ItemSelector.SlotListener;

/**
 * Items display!
 */
public class InventoryScreen extends Screen {
	
	protected static final int ITEMS_WIDTH = 112;
	protected static final int ITEMS_HEIGHT = 130;
	protected static final int ITEMS_EDGE_PADDING = 12;
	protected static final int ITEMS_LIST_PADDING = 3;
	
	protected Inventory inventory;
	protected ItemSelector items;
	protected int marked;
	protected Nineslice bg;
	protected float globalX, globalY;
	
	/**
	 * Creates a new inventory screen for the party's inventory.
	 */
	public InventoryScreen() {
		this.inventory = SGlobal.heroes.getInventory();
		pushCommandContext(new CMapMenu());
		
		bg = new Nineslice(ITEMS_WIDTH, ITEMS_HEIGHT);
		assets.add(bg);
		items = new ItemSelector(inventory, inventory.slotCount(),
				ITEMS_WIDTH - ITEMS_EDGE_PADDING * 2, ITEMS_LIST_PADDING, false);
		assets.add(items);
		
		globalX = (getWidth() - ITEMS_WIDTH) / 2;
		globalY = (getHeight() - ITEMS_HEIGHT) / 2;
		
		items.setX(globalX + (ITEMS_WIDTH - items.getWidth()) / 2);
		items.setY(globalY + (ITEMS_HEIGHT - items.getHeight()) / 2 - 7);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		bg.renderAt(batch, globalX, globalY);
		items.render(batch);
		super.render(batch);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		marked = -1;
		items.awaitSelection(new SlotListener() {
			@Override public boolean onSelection(int selected) {
				if (marked == -1) {
					if (selected == -1) {
						MGlobal.screens.pop();
						return true;
					} else {
						marked = selected;
						items.setIndent();
					}
				} else {
					items.clearIndent();
					if (selected == -1) {
						items.setSelected(marked);
					} else {
						if (marked != selected) {
							inventory.swap(marked, selected);
						} else {
							CombatItem item = inventory.get(selected);
							if (item != null) {
								items.useOrDiscard();
							}
						}
					}
					marked = -1;
				}
				return false;
			}
		}, true);
	}
}
