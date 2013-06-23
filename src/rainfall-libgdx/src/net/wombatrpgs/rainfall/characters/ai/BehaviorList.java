/**
 *  Intelligence.java
 *  Created on Jan 30, 2013 12:11:50 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai;

import java.util.PriorityQueue;

import net.wombatrpgs.rainfall.characters.enemies.EnemyEvent;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.BehaviorListMDO;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.intent.IntentMDO;

/**
 * A construct of intents that controls an AI. Sequentially goes through its
 * list of potential actions, and if one is valid, performs it. A very straight-
 * forward piece of AI meant to control enemies. However, should some behavior
 * be too complex to be represented as intents, it can instead just have a
 * custom intelligence and override this thing's methods. A new intelligence is
 * created for each individual enemy. The reason this is set as a per-enemy
 * thing rather than a per-enemy-type thing is that intelligeces can change
 * based on actions of an individual enemy.
 */
public class BehaviorList extends Intelligence {
	
	protected BehaviorListMDO mdo;
	protected PriorityQueue<Intent> intents;
	protected IntentAct busy;
	
	/**
	 * Creates a new intelligence for an actor based on data.
	 * @param 	mdo				The data to create the intelligence from
	 * @param 	actor			The actor to create the intelligence for
	 */
	public BehaviorList(BehaviorListMDO mdo, EnemyEvent actor) {
		super(mdo, actor);
		this.mdo = mdo;
		this.actor = actor;
		intents = new PriorityQueue<Intent>();
		for (IntentMDO intentMDO : mdo.intents) {
			Intent intent = new Intent(this, intentMDO, actor);
			intents.add(intent);
			assets.add(intent);
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
