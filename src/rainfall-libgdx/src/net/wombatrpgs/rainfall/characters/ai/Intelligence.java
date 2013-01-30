/**
 *  Intelligence.java
 *  Created on Jan 30, 2013 12:11:50 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai;

import java.util.PriorityQueue;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.IntelligenceMDO;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.intent.IntentMDO;

// TODO: this class and set of classes could be implemented per-enemyclass
// right now it's per enemy instance
// this would require all data about enemies to be stored in hash tables I think
/**
 * A construct of intents that controls an AI. Sequentially goes through its
 * list of potential actions, and if one is valid, performs it. A very straight-
 * forward piece of AI meant to control enemies. However, should some behavior
 * be too complex to be represented as intents, it can instead just have a
 * custom intelligence and override this thing's methods. A new intelligence is
 * created for each individual enemy.
 */
public class Intelligence {
	
	protected IntelligenceMDO mdo;
	protected PriorityQueue<Intent> intents;
	protected CharacterEvent actor;
	
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
			intents.add(new Intent(intentMDO, actor));
		}
	}
	
	/**
	 * Go through all the intents and perform the best one~!
	 */
	public void act() {
		for (Intent intent : intents) {
			if (intent.checkAndAct()) break;
		}
	}

}
