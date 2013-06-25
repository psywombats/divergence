/**
 *  IntentAct.java
 *  Created on Jan 29, 2013 11:09:14 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.core.Queueable;

/**
 * An interface for um a very plan method. Should be subclasses, supplied with
 * an actor, and then perform that action on the actor at runtime. Unlike the
 * moveset actions, intent actions are created new for each intelligence rather
 * than shared between a set of actors. Again, maybe this should've been an
 * interface. The intent is called once per update loop usually.
 */
public abstract class IntentAct implements Queueable {
	
	protected CharacterEvent actor;
	protected BehaviorList parent;
	
	/**
	 * Creates a new intent action for the specified actor.
	 * @param 	actor			The actor the act is being generated for
	 */
	public IntentAct(BehaviorList parent, CharacterEvent actor) {
		this.actor = actor;
		this.parent = parent;
	}
	
	/**
	 * Actually do the goshdarned action.
	 */
	public abstract void act();

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		// default does nothing
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		// default does nothing
	}
	
	

}
