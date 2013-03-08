/**
 *  Block.java
 *  Created on Dec 29, 2012 1:13:08 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.AnimationStrip;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.Positionable;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.physics.RectHitbox;
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
		upperBox = new RectHitbox(new Positionable() {
			@Override public int getX() { return (int) x; }
			@Override public int getY() { return (int) (y + Level.PIXELS_PER_Y); }
		}, 	-1, -1, 
			getHitbox().getWidth()+1, getHitbox().getHeight()+1);
	}
	
	/** @param summoning True if summoning is in progress */
	public void setSummoning(boolean summoning) { this.summonInProgress = summoning; }
	
	/** @return True if summoning is in progress */
	public boolean isSummoning() { return this.summonInProgress; }
	
	/** @return The hitbox of the block's upper half */
	public Hitbox getUpperBox() { return this.upperBox; }

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#onCollide
	 * (net.wombatrpgs.rainfall.maps.MapEvent, net.wombatrpgs.rainfall.physics.CollisionResult)
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
		if (parent == null) {
			RGlobal.reporter.warn("That dumb block-z bug showed up again");
			return;
		}
		map.removePassabilityOverride(upperBox, map.getZ(this) + 1);
		List<MapEvent> doomedIdiots = new ArrayList<MapEvent>();
		for (MapEvent event : map.getEvents()) {
			if (map.getZ(event) == map.getZ(this) + 1 &&
				event.getHitbox().isColliding(upperBox).isColliding) {
				doomedIdiots.add(event);
			}
		}
		for (MapEvent event : doomedIdiots) {
			event.onSupportPulled();
		}
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
		if (parent != null) parent.removeEvent(this);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#setHidden(boolean)
	 */
	@Override
	public void setHidden(boolean hidden) {
		if (hidden == true) {
			parent.removeEvent(this);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#getName()
	 */
	@Override
	public String getName() {
		return "block";
	}

	/**
	 * Changes the map the block is currently located on. Call every time the
	 * block is summoned.
	 * @param	map				The new level for the block to be on
	 */
	public void changeMap(Level map) {
		if (this.parent != null) {
			parent.removeEvent(this);
		}
		map.addEvent(this, getX(), getY());
	}
	
}
