/**
 *  Gender.java
 *  Created on Apr 11, 2014 8:30:12 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.chara.data;

/**
 * Enum for character's sex.
 */
public enum Gender {

	MALE		("M"),
	FEMALE		("F"),
	NONE		("");
	
	private String label;
	
	/**
	 * Enum constructor.
	 * @param	label			The label of the gender to display in-game
	 */
	Gender(String label) {
		this.label = label;
	}
	
	/** @return The human-readable tag name */
	public String getLabel() { return label; }
}
