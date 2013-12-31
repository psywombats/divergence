/**
 *  ItemEvent.java
 *  Created on Oct 20, 2013 6:59:55 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.item;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;
import net.wombatrpgs.mrogueschema.items.data.ItemMDO;

/**
 * The representation of an item lying on the ground.
 */
public class ItemEvent extends MapEvent {
	
	protected ItemMDO mdo;
	protected Item item;
	
	/**
	 * Creates a new item icon for a given level, item.
	 * @param	parent			The parent level
	 * @param	me				The item game unit
	 * @param	tileX			The x-coord of the tile we're at, in tiles
	 * @param	tileY			The y-coord of the tile we're at, in tiles
	 */
	public ItemEvent(Level parent, Item me, int tileX, int tileY) {
		super(parent);
		this.item = me;
		this.mdo = me.getMDO();
		this.setTileX(tileX);
		this.setTileY(tileY);
		me.setParent(this);
	}
	
	/** @return The item that we're holding */
	public Item getItem() { return item; }

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#getRegion()
	 */
	@Override
	public TextureRegion getRegion() {
		return item.getIcon().getGraphic();
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		if (!MGlobal.graphics.isShaderEnabled() && !MGlobal.hero.inLoS(this)) {
			return;
		}
		setX(getTileX() * parent.getTileWidth());
		setY(getTileY() * parent.getTileHeight());
		parent.getBatch().draw(item.getIcon().getGraphic(), x, y);
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#collideWith
	 * (net.wombatrpgs.mrogue.rpg.CharacterEvent)
	 */
	@Override
	public void collideWith(CharacterEvent character) {
		if (character != MGlobal.hero) {
			return;
		}
		pickUpBy(character);
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#mouseoverMessage()
	 */
	@Override
	public String mouseoverMessage() {
		return item.getName() + " is lying here.";
	}

	/**
	 * Simulates being picked up by a character event. This should be called
	 * when someone stumbles over this item.
	 * @param character
	 */
	public void pickUpBy(CharacterEvent character) {
		item.onPickup(character.getUnit());
		parent.removeEvent(this);
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#getZ()
	 */
	@Override
	protected float getZ() {
		return super.getZ() + parent.getTileHeight();
	}

}
