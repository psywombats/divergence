/**
 *  Warhead.java
 *  Created on Feb 24, 2014 8:12:20 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg;

import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.tactics.rpg.warhead.AbilMelee;
import net.wombatrpgs.tacticsschema.rpg.abil.WarMeleeMDO;
import net.wombatrpgs.tacticsschema.rpg.abil.data.WarheadMDO;

/**
 * An action that is performed on a thing has a warhead that happens to that
 * thing. A Warhead instance is associated with an Ability instance and is
 * responsible for applying the effects of that ability on call to the targets.
 * 
 * Despite being called a "warhead" it could still heal you or some shit.
 */
public abstract class Warhead {
	
	protected WarheadMDO mdo;
	protected Ability parent;
	
	/**
	 * Creates a new warhead. This should be invoked by the warhead factory.
	 * @param	mdo				The data to create warhead from
	 * @param	parent			The ability this warhead will be invoked for
	 */
	protected Warhead(WarheadMDO mdo, Ability parent) {
		this.mdo = mdo;
		this.parent = parent;
	}
	
	/**
	 * Called by the ability when punch people. This is in charge of the
	 * scheduled punching.
	 * @param	targets			The jerks to affect
	 */
	public abstract void invoke(List<TacticsController> targets);
	
	/**
	 * Creates a new warhead through one of those gawky factory methods that
	 * does a class lookup. All known subtypes are grouped here.
	 * @param	key				The key of the warhead to look up in database
	 * @param	parent			The ability this is to be created for
	 * @return					A new instance of that warhead
	 */
	public static Warhead create(String key, Ability parent) {
		WarheadMDO mdo = MGlobal.data.getEntryFor(key, WarheadMDO.class);
		if (WarMeleeMDO.class.isAssignableFrom(mdo.getClass())) {
			return new AbilMelee((WarMeleeMDO) mdo, parent);
		} else {
			MGlobal.reporter.err("Unknown warhead subclass: " + mdo.getClass());
			return null;
		}
	}

}
