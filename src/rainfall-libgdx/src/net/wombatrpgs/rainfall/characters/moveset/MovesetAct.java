/**
 *  MovesetAct.java
 *  Created on Jan 20, 2013 11:55:42 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.moveset;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.core.Constants;
import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.core.Updateable;
import net.wombatrpgs.rainfall.graphics.FacesAnimation;
import net.wombatrpgs.rainfall.graphics.FourDir;
import net.wombatrpgs.rainfall.io.audio.SoundObject;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.objects.TimerListener;
import net.wombatrpgs.rainfall.maps.objects.TimerObject;
import net.wombatrpgs.rainfallschema.audio.SoundMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.data.MoveMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.data.MoveMobility;
import net.wombatrpgs.rainfallschema.graphics.FourDirMDO;

/**
 * A superclass for all moves from the movesets that involve character animation
 * swapping and other stuff. A MovesetAct should really only be used by a single
 * actor.
 */
public abstract class MovesetAct implements Actionable, 
											Queueable,
											Updateable {
	
	protected MoveMDO mdo;
	protected CharacterEvent actor;
	protected Level map;
	protected FacesAnimation idleAppearance, walkingAppearance;
	protected SoundObject sfx;
	protected int maxRange;
	protected boolean coolingDown;
	
	/**
	 * Constructs a moveset act from data.
	 * @param	actor			The actor to create the move for
	 * @param 	mdo				The MDO with data to construct from
	 */
	public MovesetAct(CharacterEvent actor, MoveMDO mdo) {
		this.mdo = mdo;
		if (mdo.staticAnimation != null && !mdo.staticAnimation.equals(Constants.NULL_MDO)) {
			FourDirMDO animMDO = RGlobal.data.getEntryFor(mdo.staticAnimation, FourDirMDO.class);
			this.idleAppearance = new FourDir(animMDO, actor);
		}
		if (mdo.movingAnimation != null && !mdo.movingAnimation.equals(Constants.NULL_MDO)) {
			FourDirMDO animMDO = RGlobal.data.getEntryFor(mdo.movingAnimation, FourDirMDO.class);
			this.walkingAppearance = new FourDir(animMDO, actor);
		}
		if (mdo.sound != null && !mdo.sound.equals(Constants.NULL_MDO)) {
			SoundMDO soundMDO = RGlobal.data.getEntryFor(mdo.sound, SoundMDO.class);
			this.sfx = new SoundObject(soundMDO);
		}
		this.actor = actor;
	}
	
	/** @return The idle animation associated with this move */
	public FacesAnimation getIdleAppearance() { return idleAppearance; }
	
	/** @return The walking animation associated with this move */
	public FacesAnimation getWalkingAppearance() { return walkingAppearance; }
	
	/** @return The character executing this move */
	public CharacterEvent getActor() { return this.actor; }
	
	/** @return The range that this move attacks, 0 if none */
	public int getRange() { return maxRange; }
	
	/**
	 * @see net.wombatrpgs.rainfall.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		// default does nothing
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.Actionable#act
	 * (net.wombatrpgs.rainfall.maps.Level, net.wombatrpgs.rainfall.characters.CharacterEvent)
	 */
	@Override
	public final void act(Level map, CharacterEvent actor) {
		if (coolingDown) return;
		if (!actor.canAct()) return;
		if (!actor.isMoveActive(this)) coreAct(map, actor);
		this.map = map;
		coolingDown = true;
		if (mdo.mobility == MoveMobility.IMMOBILE) {
			actor.halt();
		}
		if (idleAppearance != null) {
			idleAppearance.reset();
			idleAppearance.startMoving();
		}
		if (walkingAppearance != null) {
			walkingAppearance.reset();
			walkingAppearance.startMoving();
		}
		final MovesetAct parent = this;
		new TimerObject(mdo.cooldown, actor, new TimerListener() {
			@Override public void onTimerZero(TimerObject source) {
				parent.coolingDown = false;
			}
		});
	}
	
	/**
	 * Stops the move, if such a thing is necessary.
	 * @param 	map				The map the action takes place on
	 * @param 	actor			The chara performing the action
	 */
	public void stop(Level map, CharacterEvent actor) {
		if (actor.isMoveActive(this)) coreRelease(map, actor);
		this.map = map;	
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		if (walkingAppearance != null) {
			walkingAppearance.queueRequiredAssets(manager);
		}
		if (idleAppearance != null) {
			idleAppearance.queueRequiredAssets(manager);
		}
		if (sfx != null) {
			sfx.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		maxRange = 0;
		if (walkingAppearance != null) {
			walkingAppearance.postProcessing(manager, pass);
			if (walkingAppearance.getMaxRange() > maxRange) {
				maxRange = walkingAppearance.getMaxRange();
			}
		}
		if (idleAppearance != null) {
			idleAppearance.postProcessing(manager, pass);
			if (idleAppearance.getMaxRange() > maxRange) {
				maxRange = idleAppearance.getMaxRange();
			}
		}
		if (sfx != null) {
			sfx.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.Actionable#act
	 * (net.wombatrpgs.rainfall.maps.Level, net.wombatrpgs.rainfall.characters.CharacterEvent)
	 * Same as that thing 
	 */
	public abstract void coreAct(Level map, CharacterEvent actor);
	
	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.Actionable#act
	 * (net.wombatrpgs.rainfall.maps.Level, net.wombatrpgs.rainfall.characters.CharacterEvent)
	 * Same as that thing but when the button is released. Default does nothing.
	 */
	public void coreRelease(Level map, CharacterEvent actor) {
		
	}
	
	/**
	 * Called when the event is pre-emptively interrupted by the hero. This only
	 * really applies to events that are active for a period of time.
	 */
	public void cancel() {
		actor.stopAction(this);
	}
	
	/**
	 * Determines if this move should stop the hero from moving.
	 */
	public boolean disablesMovement() {
		return mdo.mobility == MoveMobility.IMMOBILE || mdo.mobility == MoveMobility.STRAIGHT_LINE_ONLY;
	}
	
	/**
	 * Determines what type of move this is. Default is other, because we don't
	 * know.
	 * @return					The type of this move
	 */
	public MoveType getType() {
		return MoveType.OTHER;
	}
	
	/**
	 * Shortcut for subclasses to act themselves.
	 * @param 	map				The map the action takes place on
	 */
	protected final void act(Level map) {
		act(map, this.actor);
	}

}
