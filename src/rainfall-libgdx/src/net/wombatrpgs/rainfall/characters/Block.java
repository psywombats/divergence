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
import net.wombatrpgs.rainfall.graphics.particles.Emitter;
import net.wombatrpgs.rainfall.graphics.particles.GibParticleSet;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.Positionable;
import net.wombatrpgs.rainfall.maps.events.AnimationPlayer;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.maps.objects.TimerListener;
import net.wombatrpgs.rainfall.maps.objects.TimerObject;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.physics.RectHitbox;
import net.wombatrpgs.rainfallschema.characters.CharacterEventMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.SummonMDO;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.graphics.EmitterMDO;
import net.wombatrpgs.rainfallschema.graphics.GibsetMDO;

/**
 * That big blocky thing that falls from the sky.
 */
public class Block extends CharacterEvent {
	
	protected SummonMDO mdo;
	protected AnimationStrip anim;
	protected Hitbox upperBox;
	protected AnimationPlayer badPlayer;
	protected Emitter emitter;
	protected boolean summonInProgress;
	
	/**
	 * Creates the block. Must be updated with the map each time the hero
	 * makes a transition.
	 * @param	mdo			The MDO that spawned this abomination
	 */
	public Block(SummonMDO mdo) {
		super(RGlobal.data.getEntryFor(mdo.blockEvent, CharacterEventMDO.class));
		this.mdo = mdo;
		setCollisionsEnabled(true);
		summonInProgress = false;
		upperBox = new RectHitbox(new Positionable() {
			@Override public int getX() { return (int) x; }
			@Override public int getY() { return (int) (y + Level.PIXELS_PER_Y); }
		}, 	-1, -1, 
			getHitbox().getWidth()+1, getHitbox().getHeight()+1);
		AnimationMDO badMDO = RGlobal.data.getEntryFor(mdo.failAnimation, AnimationMDO.class);
		this.badPlayer = new AnimationPlayer(badMDO);
		if (mdo.emitter != null && mdo.gibs != null) {
			GibsetMDO gibsetMDO = RGlobal.data.getEntryFor(mdo.gibs, GibsetMDO.class);
			EmitterMDO emitterMDO = RGlobal.data.getEntryFor(mdo.emitter, EmitterMDO.class);
			GibParticleSet gibs = new GibParticleSet(gibsetMDO);
			emitter = new Emitter(emitterMDO, gibs);
		}
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
		badPlayer.queueRequiredAssets(manager);
		if (emitter != null) {
			emitter.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		appearance.postProcessing(manager, pass);
		badPlayer.postProcessing(manager, pass);
		if (emitter != null) {
			emitter.postProcessing(manager, pass);
		}
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
	
	/**
	 * Nicely animates the block landing (and killing) itself at the location.
	 * @param	map				The map to drop the block on
	 * @param 	targetTileX		The x-coord to land at (in tiles)
	 * @param 	targetTileY		The y-coord to land at (in tiles)
	 * @param	z				The z-layer to land at (in z-depth layer)
	 */
	public void selfDestructAt(Level map, final int targetTileX, final int targetTileY, int z) {
		if (RGlobal.block != null && RGlobal.block.getLevel() != null) {
			RGlobal.block.getLevel().removeEvent(RGlobal.block);
		}
		map.addEvent(badPlayer, targetTileX, targetTileY, z+1);
		badPlayer.start();
		new TimerObject(mdo.duration, this, new TimerListener() {
			@Override
			public void onTimerZero(TimerObject source) {
//				if (emitter != null) {
//					RGlobal.hero.getLevel().addEvent(emitter, 
//							targetTileX, targetTileY,
//							RGlobal.hero.getLevel().getZ(RGlobal.hero));
//					emitter.fire(0, 0);
//				}
			}
		});
	}
	
}
