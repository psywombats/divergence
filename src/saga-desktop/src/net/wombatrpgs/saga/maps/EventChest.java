/**
 *  EventChest.java
 *  Created on Sep 12, 2014 1:34:04 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.graphics.FacesAnimationFactory;
import net.wombatrpgs.mgne.maps.TiledMapObject;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.sagaschema.rpg.abil.CombatItemMDO;

/**
 * Because scripting them is just... *effort*.
 */
public class EventChest extends MapEvent {
	
	protected static String PROPERTY_ITEM_KEY = "item";
	protected static String PROPERTY_KEY_ITEM = "keyItem";
	protected static String KEY_ANIM_CLOSED = "anim_chest_closed";
	protected static String KEY_ANIM_OPEN = "anim_chest_open";
	
	protected FacesAnimation openSprite, closedSprite;
	protected String switchName;
	protected CombatItem item;
	protected boolean keyItem;
	
	/**
	 * Creates a new event from a Tiled object.
	 * @param	object			The object to create from
	 */
	public EventChest(TiledMapObject object) {
		super(object.generateMDO(EventMDO.class));
		openSprite = FacesAnimationFactory.create(KEY_ANIM_OPEN);
		closedSprite = FacesAnimationFactory.create(KEY_ANIM_CLOSED);
		assets.add(openSprite);
		assets.add(closedSprite);
		
		String itemKey = object.getString(PROPERTY_ITEM_KEY);
		if (itemKey != null) {
			item = new CombatItem(MGlobal.data.getEntryFor(itemKey, CombatItemMDO.class));
			assets.add(item);
		}
		
		switchName = "chest_" + object.getLevel().getKeyName();
		switchName += "(" + object.getTileX() + "," + object.getTileY() + ")";
		
		keyItem = object.propertyExists(PROPERTY_KEY_ITEM);
		
		setAppearance();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onInteract()
	 */
	@Override
	public boolean onInteract() {
		if (!chestOpened()) {
			if (item != null) {
				if (SGlobal.heroes.getInventory().isFull()) {
					MGlobal.ui.getBlockingBox().blockText(
							MGlobal.levelManager.getScreen(), "No more room for items!");
				} else {
					if (keyItem) {
						MGlobal.ui.getBlockingBox().blockText(
								MGlobal.levelManager.getScreen(),
								"Retrieved the " + item.getName() + ".");
					} else {
						MGlobal.ui.getBlockingBox().blockText(
								MGlobal.levelManager.getScreen(),
								"Retrieved a " + item.getName() + ".");
					}
					MGlobal.sfx.play(SConstants.SFX_GET);
					SGlobal.heroes.getInventory().add(item);
					MGlobal.memory.setSwitch(switchName);
					setAppearance();
				}
			} else {
				MGlobal.ui.getBlockingBox().blockText(
						MGlobal.levelManager.getScreen(), "Empty.");
				MGlobal.memory.setSwitch(switchName);
				setAppearance();
			}
		}
		return true;
	}

	/**
	 * Sets appearance based on switch status.
	 */
	protected void setAppearance() {
		if (MGlobal.memory.getSwitch(switchName)) {
			setAppearance(openSprite);
		} else {
			setAppearance(closedSprite);
		}
	}
	
	/**
	 * Checks if this chest should appear open or closed.
	 * @return
	 */
	protected boolean chestOpened() {
		if (keyItem) {
			return SGlobal.heroes.isCarryingItemType(item);
		} else {
			return MGlobal.memory.getSwitch(switchName);
		}
	}
}
