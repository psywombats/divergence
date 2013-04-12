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
import net.wombatrpgs.rainfall.graphics.Graphic;
import net.wombatrpgs.rainfall.io.audio.SoundObject;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.audio.SoundMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.data.MoveMDO;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.data.MoveMobility;
import net.wombatrpgs.rainfallschema.graphics.FourDirMDO;
import net.wombatrpgs.rainfallschema.graphics.GraphicMDO;

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
	protected Graphic icon;
	protected SoundObject sfx;
	
	/**
	 * Constructs a moveset act from data.
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
		if (mdo.graphic != null && !mdo.graphic.equals(Constants.NULL_MDO)) {
			GraphicMDO iconMDO= RGlobal.data.getEntryFor(mdo.graphic, GraphicMDO.class);
			this.icon = new Graphic(iconMDO);
		}
		if (mdo.sound != null && !mdo.sound.equals(Constants.NULL_MDO)) {
			SoundMDO soundMDO = RGlobal.data.getEntryFor(mdo.sound, SoundMDO.class);
			this.sfx = new SoundObject(soundMDO, actor);
		}
		this.actor = actor;
	}
	
	/** @return The idle animation associated with this move */
	public FacesAnimation getIdleAppearance() { return idleAppearance; }
	
	/** @return The walking animation associated with this move */
	public FacesAnimation getWalkingAppearance() { return walkingAppearance; }
	
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
		if (!actor.canAct()) return;
		if (!actor.isMoveActive(this)) coreAct(map, actor);
		this.map = map;
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
		if (icon != null) {
			icon.queueRequiredAssets(manager);
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
		if (walkingAppearance != null) {
			walkingAppearance.postProcessing(manager, pass);
		}
		if (idleAppearance != null) {
			idleAppearance.postProcessing(manager, pass);
		}
		if (icon != null) {
			icon.postProcessing(manager, pass);
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
	 * Gets the icon displayed for this icon. To be displayed when this move is
	 * bound to a command. Can be null
	 * @return					The icon of this move, or null if none
	 */
	public Graphic getIcon() {
		return icon;
	}
	
	/**
	 * Determines if this move should stop the hero from moving.
	 */
	public boolean disablesMovement() {
		return mdo.mobility == MoveMobility.IMMOBILE;
	}
	
	/**
	 * Shortcut for subclasses to act themselves.
	 * @param 	map				The map the action takes place on
	 */
	protected final void act(Level map) {
		act(map, this.actor);
	}

}
