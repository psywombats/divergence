/**
 *  EventItem.java
 *  Created on Jan 27, 2015 2:25:58 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.maps.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.bacon01.core.BGlobal;
import net.wombatrpgs.baconschema.rpg.ItemMDO;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.physics.CollisionResult;
import net.wombatrpgs.mgne.physics.Hitbox;
import net.wombatrpgs.mgne.physics.RectHitbox;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgneschema.maps.EventMDO;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;

/**
 * bacon item
 */
public class EventItem extends MapEvent {
	
	protected Graphic appearance;
	protected ItemMDO itemMDO;
	protected Hitbox box;

	public EventItem(ItemMDO mdo) {
		super(generateEventMDO(mdo));
		this.itemMDO = mdo;
		appearance = new Graphic("res/sprites/", itemMDO.icon);
		assets.add(appearance);
		box = new RectHitbox(this, 0, 0, 16, 16);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#isPassable()
	 */
	@Override
	public boolean isPassable() {
		return isHidden();
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		if (isHidden()) return;
		appearance.renderAt(batch, x + appearance.getWidth()/2, y + appearance.getHeight()/2);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#onCollide
	 * (net.wombatrpgs.mgne.maps.events.MapEvent, net.wombatrpgs.mgne.physics.CollisionResult)
	 */
	@Override
	public boolean onCollide(MapEvent event, CollisionResult result) {
		if (event != MGlobal.getHero()) {
			return false;
		}
		if (!isHidden()) {
			BGlobal.items.pickUp(itemMDO);
			MGlobal.memory.setSwitch("collect_" + itemMDO.key);
			MGlobal.audio.playSFX("item_get");
		}
		return super.onCollide(event, result);
	}

	/**
	 * @see net.wombatrpgs.mgne.maps.events.MapEvent#getHitbox()
	 */
	@Override
	public Hitbox getHitbox() {
		return box;
	}

	private static EventMDO generateEventMDO(ItemMDO itemMDO) {
		EventMDO mdo = new EventMDO();
		mdo.key = itemMDO.key + "_event";
		mdo.description = "generated";
		mdo.face = OrthoDir.SOUTH;
		mdo.width = 1f;
		mdo.height = 1f;
		// TODO: support multiple items of same key
		mdo.hide = "return getSwitch('collect_" + itemMDO.key + "')";
		return mdo;
	}
}
