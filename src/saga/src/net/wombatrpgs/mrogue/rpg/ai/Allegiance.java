/**
 *  Allegiance.java
 *  Created on Oct 16, 2013 2:09:46 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import net.wombatrpgs.mrogue.core.Turnable;
import net.wombatrpgs.mrogue.rpg.GameUnit;
import net.wombatrpgs.mrogueschema.characters.data.Faction;
import net.wombatrpgs.mrogueschema.characters.data.Relation;

/**
 * A GameUnit owns one of these. It tells the unit what to attack, what not to
 * attack, etc. An allegience represents relationships to multiple factions as
 * well as to individual enemies that have wrought us harm.
 */
public class Allegiance implements Turnable {
	
	protected GameUnit parent;
	protected List<GameUnit> hitlist;
	protected List<GameUnit> friendlist;
	protected Set<Faction> factions;
	
	/**
	 * Creates a new allegiance from a set of factions. Multi-faction support is
	 * accomplished by taking the lowest of the relations.
	 * @param	parent			The game unit with this allegiance
	 * @param	factions		The factions to align with
	 */
	public Allegiance(GameUnit parent, Faction...factions) {
		this.factions = EnumSet.copyOf(Arrays.asList(factions));
		this.parent = parent;
		this.hitlist = new ArrayList<GameUnit>();
		this.friendlist = new ArrayList<GameUnit>();
		
		friendlist.add(parent);
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.core.Turnable#onTurn()
	 */
	@Override
	public void onTurn() {
		List<GameUnit> deads = new ArrayList<GameUnit>();
		for (GameUnit unit : hitlist) {
			if (unit.isDead()) {
				deads.add(unit);
			}
		}
		for (GameUnit dead : deads) {
			hitlist.remove(dead);
		}
	}

	/**
	 * Determines the relation to some other unit, either by faction or
	 * personal preference. Not necessarily symmetric.
	 * @param	other			The game unit to get the relationship status to
	 * @return					The relation this unit has to the other unit
	 */
	public Relation getRelationTo(GameUnit other) {
		if (hitlist.contains(other)) {
			return Relation.HOSTILE;
		} else if (friendlist.contains(other)) {
			return Relation.ALLIED;
		} else {
			Relation result = Relation.ALLIED;		// highest friendliness
			for (Faction f1 : this.factions) {
				for (Faction f2 : other.getAllegiance().factions) {
					Relation newRelation = f1.getRelation(f2);
					if (newRelation.getHostility() < result.getHostility()) {
						result = newRelation;
					}
				}
			}
			return result;
		}
	}
	
	/**
	 * Call this when some shithead attacks us. The meanie. Add him to the
	 * hitlist if necessary.
	 * @param	jerk			The idiot who hit us
	 */
	public void addToHitlist(GameUnit jerk) {
		if (parent == jerk) return;
		if (getRelationTo(jerk).retaliate && !hitlist.contains(jerk)) {
			hitlist.add(jerk);
		}
	}
	
	/**
	 * This is really only called to give unnatural monsters some allegiance to
	 * each other, like when monsters spawn in a tension room.
	 * @param	buddy			The cool guy who's our friend now
	 */
	public void addToFriendlist(GameUnit buddy) {
		if (!friendlist.contains(buddy)) {
			friendlist.add(buddy);
		}
	}

}
