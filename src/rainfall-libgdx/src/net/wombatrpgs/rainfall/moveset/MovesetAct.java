/**
 *  MovesetAct.java
 *  Created on Jan 20, 2013 11:55:42 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.moveset;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.FacesAnimation;
import net.wombatrpgs.rainfall.graphics.FourDir;
import net.wombatrpgs.rainfall.graphics.Queueable;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.MoveMDO;
import net.wombatrpgs.rainfallschema.graphics.FourDirMDO;

/**
 * A superclass for all moves from the movesets that involve character animation
 * swapping and other stuff. A MovesetAct should really only be used by a single
 * actor.
 */
public abstract class MovesetAct implements Actionable, Queueable {
	
	protected CharacterEvent actor;
	protected Level map;
	protected FacesAnimation oldAppearance, newAppearance;
	protected FacesAnimation animFromMDO;
	
	/**
	 * Constructs a moveset act from data.
	 * @param 	mdo				The MDO with data to construct from
	 */
	public MovesetAct(CharacterEvent actor, MoveMDO mdo) {
		if (mdo.animation != null && !"".equals(mdo.animation)) {
			FourDirMDO animMDO = RGlobal.data.getEntryFor(mdo.animation, FourDirMDO.class);
			this.animFromMDO = new FourDir(animMDO, actor);
		}
		this.actor = actor;
	}

	/**
	 * @see net.wombatrpgs.rainfall.moveset.Actionable#act
	 * (net.wombatrpgs.rainfall.maps.Level, net.wombatrpgs.rainfall.characters.CharacterEvent)
	 */
	@Override
	public final void act(Level map, CharacterEvent actor) {
		coreAct(map, actor);
		this.map = map;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.graphics.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		if (animFromMDO != null) {
			animFromMDO.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void postProcessing(AssetManager manager) {
		if (animFromMDO != null) {
			animFromMDO.postProcessing(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.moveset.Actionable#act
	 * (net.wombatrpgs.rainfall.maps.Level, net.wombatrpgs.rainfall.characters.CharacterEvent)
	 * Same as that thing 
	 */
	public abstract void coreAct(Level map, CharacterEvent actor);
	
	/**
	 * Shortcut for subclasses to act themselves.
	 * @param 	map				The map the action takes place on
	 */
	protected final void act(Level map) {
		act(map, this.actor);
	}
	
	/**
	 * Remembers the actor's appearance for future use. Useful if the act 
	 * requires changing the appearance of the actor temporarily.
	 */
	protected void saveAppearance() {
		oldAppearance = actor.getAppearance();
	}
	
	/**
	 * Changes the actor's appearance to the supplied animation. Also remembers
	 * the old action for future restoration.
	 * @param	animFromMDO2		The new appearance for the actor
	 */
	protected void setAppearance(FacesAnimation animFromMDO2) {
		if (actor.getAppearance() != animFromMDO2) {
			saveAppearance();
			actor.setAppearance(animFromMDO2);
			this.newAppearance = animFromMDO2;
			this.newAppearance.startMoving();
		}
	}
	
	/**
	 * Returns the actor to its memorized animation if appropriate. Here, this
	 * means if the animation is the animation that was set by this move.
	 */
	protected void restoreAppearance() {
		// FIXME: there's a bug here
		if (newAppearance == RGlobal.hero.getAppearance()) {
			actor.setAppearance(oldAppearance);
		}
	}

}
