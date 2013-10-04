/**
 *  Intelligence.java
 *  Created on Jun 22, 2013 12:21:13 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters.ai;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.characters.EnemyEvent;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogueschema.characters.ai.data.IntelligenceMDO;

/**
 * A thing that can tell enemies what to do. Replaces the old Blockbound style
 * behavior list exclusive control. Behavior lists (old intelligences) now
 * extend this.
 */
public abstract class Intelligence implements Queueable {
	
	protected IntelligenceMDO mdo;
	protected EnemyEvent actor;
	protected List<Queueable> assets;
	
	/**
	 * Creates a new intelligence to control a specific enemy.
	 * @param 	actor			The enemy to control
	 */
	public Intelligence(IntelligenceMDO mdo, EnemyEvent actor) {
		this.actor = actor;
		this.assets = new ArrayList<Queueable>();
		this.mdo = mdo;
	}
	
	/**
	 * This is what the intelligence decides to do. It should analyze the game
	 * state and its internal whatevers and come up with the best course of
	 * action for the actor, then tell the actor to do it. Called every tick of
	 * the update loop.
	 */
	public abstract void act();

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}

}
