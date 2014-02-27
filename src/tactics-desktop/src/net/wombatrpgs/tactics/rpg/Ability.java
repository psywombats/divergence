/**
 *  Ability.java
 *  Created on Feb 24, 2014 7:23:31 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg;

import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.tactics.rpg.TacticsController.AcquiredListener;
import net.wombatrpgs.tacticsschema.rpg.abil.AbilityMDO;

/**
 * Something you can do on your turn. This includes bumping into things, casting
 * majick spells, buffing things, etc. It really should be called "action" I
 * guess but w/e. This can be executed by players and enemies. The targeting
 * is dispatched to the controller that will decide on a target by either AI or
 * player control, and the action is actually performed here.
 * 
 * Each unit will have its own instance. (ie no flyweight)
 */
public class Ability {
	
	protected AbilityMDO mdo;
	protected TacticsController parent;
	
	protected Warhead warhead;
	protected AbilityFinishListener onFinish;
	
	/**
	 * Creates an ability from MDO.
	 * @param	mdo				The data to create ability from
	 * @param	parent			The parent owner of this ability instance
	 */
	public Ability(AbilityMDO mdo, TacticsController parent) {
		this.mdo = mdo;
		this.parent = parent;
		warhead = Warhead.create(mdo.warhead, this);
	}
	
	/**
	 * Creates an ability from MDO key.
	 * @param	key				The key to the data to create from
	 * @param	parent			The owner to create for
	 */
	public Ability(String key, TacticsController parent) {
		this(MGlobal.data.getEntryFor(key, AbilityMDO.class), parent);
	}
	
	/** @return Short-form non-descriptive ability name */
	public String getName() { return mdo.abilityName; }
	
	/**
	 * Called when this ability is selected for use. No target designated yet.
	 * @param	listener		The listener to notify when ability is done
	 */
	public void onUse(final AbilityFinishListener listener) {
		this.onFinish = listener;
		parent.acquireTargets(new AcquiredListener() {
			@Override public void onAcquired(List<TacticsController> targets) {
				warhead.invoke(targets);
				listener.onAbilityEnd(500);
			}
		}, mdo.range, mdo.projector);
	}
	
	/**
	 * Called when user is done interacting with an ability.
	 */
	public interface AbilityFinishListener {
		
		/**
		 * Called when user is done interacting with an ability.
		 * @param	energy		Energy spent using this ability, 0 means it was
		 * 						free but -1 means cancelled
		 */
		public void onAbilityEnd(int energy);
	}

}
