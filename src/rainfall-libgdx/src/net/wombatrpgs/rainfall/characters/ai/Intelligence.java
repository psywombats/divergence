/**
 *  Intelligence.java
 *  Created on Jan 30, 2013 12:11:50 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai;

import java.util.PriorityQueue;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.IntelligenceMDO;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.intent.IntentMDO;

/**
 * A construct of intents that controls an AI. Sequentially goes through its
 * list of potential actions, and if one is valid, performs it. A very straight-
 * forward piece of AI meant to control enemies. However, should some behavior
 * be too complex to be represented as intents, it can instead just have a
 * custom intelligence and override this thing's methods. A new intelligence is
 * created for each individual enemy. The reason this is set as a per-enemy
 * thing rather than a per-enemy-type thing is that intelligecces can change
 * based on actions of an individual enemy.
 */
public class Intelligence implements Queueable {
	
	protected IntelligenceMDO mdo;
	protected PriorityQueue<Intent> intents;
	protected CharacterEvent actor;
	protected IntentAct busy;
	
	/**
	 * Creates a new intelligence for an actor based on data.
	 * @param 	mdo				The data to create the intelligence from
	 * @param 	actor			The actor to create the intelligence for
	 */
	public Intelligence(IntelligenceMDO mdo, CharacterEvent actor) {
		this.mdo = mdo;
		this.actor = actor;
		intents = new PriorityQueue<Intent>();
		for (IntentMDO intentMDO : mdo.intents) {
			intents.add(new Intent(this, intentMDO, actor));
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Intent intent : intents) {
			intent.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Intent intent : intents) {
			intent.postProcessing(manager, pass);
		}
	}
	
	/**
	 * Go through all the intents and perform the best one~!
	 */
	public void act() {
		if (busy == null) {
			for (Intent intent : intents) {
				if (intent.checkAndAct()) break;
			}
		} else {
			busy.act();
		}
	}
	
	/**
	 * Sets the delaying action on this intelligence. This act will override
	 * the intelligence and always act until it unsets itself as the busy
	 * event.
	 * @param act
	 */
	public void setBusy(IntentAct act) {
		this.busy = act;
	}
	
}
