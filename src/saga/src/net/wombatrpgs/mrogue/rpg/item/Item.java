/**
 *  Item.java
 *  Created on Oct 20, 2013 6:41:32 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.item;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.rpg.GameUnit;
import net.wombatrpgs.mrogue.rpg.act.Action;
import net.wombatrpgs.mrogueschema.items.data.ItemMDO;

/**
 * Woahhh this game has items now! These are things on the ground that you can
 * pick up and add to your inventory. Rather, this is the in-world
 * representation of that item. There's a separation between the logical item
 * that an RPG character uses and an ItemEvent that dies when the item is
 * picked up and spawned when dropped. This is a rather general superclass, and
 * it's abstract because all items will be things like potions, book, weapons,
 * etc.<br><br>
 * 
 * It extends action as it can be used as an action during a character's turn.
 */
public abstract class Item extends Action implements Queueable {
	
	protected ItemMDO mdo;
	protected GameUnit owner;
	protected ItemEvent parent;
	protected Graphic icon;
	
	protected List<Queueable> assets;
	
	/**
	 * Creates a new item from data.
	 * @param	mdo				The data to generate from
	 */
	public Item(ItemMDO mdo) {
		this.mdo = mdo;
		assets = new ArrayList<Queueable>();
		icon = new Graphic(Constants.ITEMS_DIR, mdo.icon);
		assets.add(icon);
	}
	
	/** @return The data this item generated from */
	public ItemMDO getMDO() { return mdo; }
	
	/** @return The chara that has this item in their inventory */
	public GameUnit getOwner() { return owner; }
	
	/** @return The in-map representation of this item */
	public ItemEvent getEvent() { return parent; }
	
	/** @return The visual for this item */
	public Graphic getIcon() { return icon; }
	
	/** @param event The new in-map representation of this item */
	public void setParent(ItemEvent event) { this.parent = event; }
	
	/** @return The in-game name of this object */
	public String getName() { return mdo.name; }
	
	/** @return The in-game description of this object */
	public String getDescription() { return mdo.gameDesc; }
	
	/** @return How often this item will be discarded */
	public float getRarity() { return mdo.rarity; }
	
	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.act.Action#act()
	 */
	@Override
	public void act() {
		use();
	}

	/**
	 * This should be called to simulate a character picking up this item. Is
	 * called exlusively from its ItemEvent parent.
	 * @param	unit			The chara picking us up
	 */
	public void onPickup(GameUnit unit) {
		this.owner = unit;
		parent = null;
		setActor(unit.getParent());
		unit.pickUp(this);
	}
	
	/**
	 * Call this when the character opts to spend their turn and use the item.
	 */
	public void use() {
		owner.getInventory().removeItem(this);
		internalUse();
		owner = null;
		parent = null;
		assets.clear();
	}
	
	/**
	 * Called by the item when it's time to apply whatever this item's effect
	 * is. So do your main thing. Inventory is taken care of elsewhere.
	 */
	protected abstract void internalUse();

}
