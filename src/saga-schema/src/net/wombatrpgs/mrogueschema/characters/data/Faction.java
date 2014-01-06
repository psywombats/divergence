/**
 *  Faction.java
 *  Created on Oct 16, 2013 2:11:09 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters.data;

/**
 * A faction represents an allied group of enemies, such as all lizardmen or all
 * animals. It's used mainly by the Allegiance class and should rarely be
 * accessed directly.
 */
public enum Faction {
	
	HERO			("Player"),
	UNDEAD			("Undead"),
	HUMANS			("Humanity"),
	EXHUMANS		("Ex-Humans"),
	ANIMALS			("Animals"),
	ENEMIES			("Enemy");
	
	protected String displayName;
	
	private Faction(String displayName) {
		this.displayName = displayName;
	}
	
	/** @return The user-facing string for this allegiance */
	@Override public String toString() { return displayName; }
	
	/**
	 * Determines the relation this faction has to some other faction. Note that
	 * relationships are not necessarily symmetric.
	 * @param	other			The faction to get relation to
	 * @return
	 */
	public Relation getRelation(Faction other) {
		switch (this) {
		case HERO:
			switch (other) {
			case HERO: return Relation.ALLIED;
			default: return Relation.HOSTILE;
			}
		case UNDEAD:
			switch (other) {
			case HERO: return Relation.HOSTILE;
			case HUMANS: return Relation.HOSTILE;
			case ANIMALS: return Relation.NEUTRAL;
			case UNDEAD: return Relation.FRIENDLY;
			case EXHUMANS: return Relation.FRIENDLY;
			default: return Relation.NEUTRAL;
			}
		case HUMANS:
			switch (other) {
			case HERO: return Relation.NEUTRAL;
			case UNDEAD: return Relation.HOSTILE;
			default: return Relation.NEUTRAL;
			}
		case EXHUMANS:
			switch (other) {
			case HERO: return Relation.HOSTILE;
			default: return Relation.NEUTRAL;
			}
		case ANIMALS:
			switch (other) {
			case HERO: return Relation.HOSTILE;
			case HUMANS: return Relation.HOSTILE;
			case EXHUMANS: return Relation.HOSTILE;
			case ANIMALS: return Relation.ALLIED;
			default: return Relation.NEUTRAL;
			}
		case ENEMIES:
			switch (other) {
			case HERO: return Relation.HOSTILE;
			default: return Relation.NEUTRAL;
			}
		default:
			return Relation.NEUTRAL;
		}
	}

}
