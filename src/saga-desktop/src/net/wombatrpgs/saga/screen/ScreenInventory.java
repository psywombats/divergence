/**
 *  InventoryScreen.java
 *  Created on Apr 13, 2014 1:22:06 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.rpg.items.Inventory;
import net.wombatrpgs.saga.ui.CharaSelector;
import net.wombatrpgs.saga.ui.CharaSelector.SelectionListener;
import net.wombatrpgs.saga.ui.ItemSelector;
import net.wombatrpgs.saga.ui.ItemSelector.SlotListener;

/**
 * Items display!
 */
public class ScreenInventory extends SagaScreen implements TargetSelectable {
	
	protected static final int ITEMS_WIDTH = 112;
	protected static final int ITEMS_HEIGHT = 130;
	protected static final int ITEMS_EDGE_PADDING = 12;
	protected static final int ITEMS_LIST_PADDING = 3;
	protected static final int INSERTS_PADDING = 3;
	
	protected Inventory inventory;
	protected ItemSelector items;
	protected CharaSelector inserts;
	protected Nineslice bg;
	protected int marked;
	protected float globalX, globalY;
	
	/**
	 * Creates a new inventory screen for the party's inventory.
	 */
	public ScreenInventory() {
		this.inventory = SGlobal.heroes.getInventory();
		pushCommandContext(new CMapMenu());
		
		bg = new Nineslice(ITEMS_WIDTH, ITEMS_HEIGHT);
		assets.add(bg);
		items = new ItemSelector(inventory, inventory.slotCount(),
				ITEMS_WIDTH - ITEMS_EDGE_PADDING * 2, ITEMS_LIST_PADDING,
				false, false);
		inserts = new CharaSelector(SGlobal.heroes, false, false, true, INSERTS_PADDING);
		assets.add(inserts);
		assets.add(items);
		addUChild(inserts);
		
		globalX = (getWidth() - (ITEMS_WIDTH + inserts.getWidth())) / 2;
		globalY = (getHeight() - ITEMS_HEIGHT) / 2;
		
		items.setX(globalX + (ITEMS_WIDTH - items.getWidth()) / 2);
		items.setY(globalY + (ITEMS_HEIGHT - items.getHeight()) / 2 - 7);
	}

	/**
	 * @see net.wombatrpgs.saga.screen.TargetSelectable#getUser()
	 */
	@Override
	public Chara getUser() {
		return null;
	}

	/**
	 * @see net.wombatrpgs.saga.screen.TargetSelectable#awaitSelection
	 * (net.wombatrpgs.saga.ui.CharaSelector.SelectionListener)
	 */
	@Override
	public void awaitSelection(SelectionListener listener) {
		inserts.awaitSelection(listener, true);
	}

	/**
	 * @see net.wombatrpgs.saga.screen.TargetSelectable#refresh()
	 */
	@Override
	public void refresh() {
		inserts.refresh();
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		if (inserts.isActive()) {
			bg.renderAt(batch, globalX, globalY);
			inserts.render(batch);
		} else {
			inserts.render(batch);
			bg.renderAt(batch, globalX, globalY);
		}
		items.render(batch);
		super.render(batch);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		final ScreenInventory parent = this;
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
								items.useOrDiscard(parent);
							}
						}
					}
					marked = -1;
				}
				return false;
			}
		}, true);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#postProcessing
	 * (net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		
		inserts.setX(globalX + ITEMS_WIDTH - bg.getBorderWidth());
		inserts.setY(globalY + ITEMS_HEIGHT - inserts.getHeight());
	}
	
}
