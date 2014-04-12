/**
 *  Race.java
 *  Created on Apr 2, 2014 9:40:01 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.chara.data;

/**
 * Playable character races.
 */
public enum Race {
	
	HUMAN		("Human"),
	MUTANT		("Mutant"),
	MONSTER		("Monster"),
	ROBOT		("Robot");
	
	private String name;
	
	/**
	 * Enum constructor.
	 * @param	name			The name of the race to display in-game
	 */
	Race(String name) {
		this.name = name;
	}
	
	/** @return The human-readable name of the race */
	public String getName() { return name; }

}
