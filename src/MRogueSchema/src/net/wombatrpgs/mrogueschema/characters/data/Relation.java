/**
 *  Relation.java
 *  Created on Oct 16, 2013 2:23:11 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters.data;

/**
 * A relation is how one faction relates to another, or how an enemy reacts to
 * the player, etc. It's a display of friendliness or hostility, basically.
 */
public enum Relation {
	
	ALLIED			(3, false, false, false, true),
	FRIENDLY		(2, false, false, true, false),
	NEUTRAL			(1, false, true, true, false),
	HOSTILE			(0, true, true, true, false);
	

	public boolean attackOnSight;
	public boolean attackIfBored;
	public boolean retaliate;
	public boolean avenge;
	protected int hostility;
	
	private Relation(int hostility, boolean attackOnSight,
			boolean attackIfBored, boolean retaliate, boolean avenge) {
		this.hostility = hostility;
		this.attackOnSight = attackOnSight;
		this.attackIfBored = attackIfBored;
		this.retaliate = retaliate;
		this.avenge = avenge;
	}
	
	/**
	 * Gets the hostility as an integer representation. The idea is that lower
	 * numbers indicate more hostile responses, so if multiple reactions must
	 * be compared, always used the lower relation.
	 * @return
	 */
	public int getHostility() {
		return hostility;
	}
	
}
