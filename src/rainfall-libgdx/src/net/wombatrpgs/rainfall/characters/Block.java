/**
 *  Block.java
 *  Created on Dec 29, 2012 1:13:08 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.collisions.CollisionResult;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.AnimationStrip;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.Positionable;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfallschema.characters.CharacterEventMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.SummonMDO;

/**
 * That big blocky thing that falls from the sky.
 */
public class Block extends CharacterEvent {
	
	protected SummonMDO mdo;
	protected AnimationStrip anim;
	protected Hitbox upperBox;
	protected boolean summonInProgress;
	
	/**
	 * Creates the block. Must be updated with the map each time the hero
	 * makes a transition.
	 * @param	mdo			The MDO that spawned this abomination
	 */
	public Block(SummonMDO mdo) {
		super(RGlobal.data.getEntryFor(mdo.blockEvent, CharacterEventMDO.class));
		setCollisionsEnabled(true);
		summonInProgress = false;
		upperBox = getHitbox().duplicate();
		upperBox.setParent(new Positionable() {
			@Override public int getX() { return (int) x; }
			@Override public int getY() { return (int) (y + Level.PIXELS_PER_Y); }	
		});
	}
	
	/** @param summoning True if summoning is in progress */
	public void setSummoning(boolean summoning) { this.summonInProgress = summoning; }
	
	/** @return True if summoning is in progress */
	public boolean isSummoning() { return this.summonInProgress; }
	
	/** @return The hitbox of the block's upper half */
	public Hitbox getUpperBox() { return this.upperBox; }

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#onCollide
	 * (net.wombatrpgs.rainfall.maps.MapEvent, net.wombatrpgs.rainfall.collisions.CollisionResult)
	 */
	@Override
	public boolean onCollide(MapEvent other, CollisionResult result) {
		if (other == RGlobal.hero) {
			return true;
		} else {
			return super.onCollide(other, result);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		appearance.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		appearance.postProcessing(manager, pass);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#onAddedToMap
	 * (net.wombatrpgs.rainfall.maps.Level)
	 */
	@Override
	public void onAddedToMap(Level map) {
		super.onAddedToMap(map);
		map.addPassabilityOverride(upperBox, map.getZ(this) + 1);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#onRemovedFromMap
	 * (net.wombatrpgs.rainfall.maps.Level)
	 */
	@Override
	public void onRemovedFromMap(Level map) {
		super.onRemovedFromMap(map);
		map.removePassabilityOverride(upperBox, map.getZ(this) + 1);
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#changeZ(int)
	 */
	@Override
	public void changeZ(int newZ) {
		parent.removePassabilityOverride(upperBox, parent.getZ(this) + 1);
		super.changeZ(newZ);
		parent.addPassabilityOverride(upperBox, parent.getZ(this) + 1);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#reset()
	 */
	@Override
	public void reset() {
		//getLevel().removeEvent(this);
	}

	/**
	 * Changes the map the block is currently located on. Call every time the
	 * block is summoned.
	 * @param	map				The new level for the block to be on
	 */
	public void changeMap(Level map) {
		if (this.parent != null) {
			parent.teleportOff();
		}
		map.addEvent(this, getX(), getY());
	}
	
}
