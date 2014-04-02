/**
 *  SideEffect.java
 *  Created on Apr 1, 2014 3:13:13 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.abil.data;

/**
 * Set of weird side effects an attack can have.
 */
public enum OffenseFlag {
	
	KILLS_USER,
	ONLY_AFFECT_UNDEAD,
	ONLY_AFFECT_HUMANS,
	DRAIN_LIFE,
	CRITICAL_ON_WEAKNESS,
	IGNORE_RESISTANCES,
	STUNS_ON_HIT,

}
