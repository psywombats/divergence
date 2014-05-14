/**
 *  MFamFamily.java
 *  Created on May 3, 2014 12:11:53 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.wombatrpgs.saga.misc.mfam.MFamConstants;

/**
 * A family of monsters in the optimizer.
 */
public class Family {
	
	/** The n optimzied members of the family */
	public Member[] members;
	
	/** Transform targets when eating meat of group */
	public Map<Group, Family> links;
	
	/** Group this family is a part of */
	public Group group;
	
	/** Just for fun */
	public String name;
	
	/** Index by meat power and the result will be transform target */
	public Member[] powerTargets;
	
	public Family(String name) {
		this.name = name;
		members = new Member[MFamConstants.FAMILY_SIZE];
		links = new HashMap<Group, Family>();
	}
	
	public Family() {
		this(MFamConstants.NAMEGEN.getTag());
	}
	
	public Family(Family parent) {
		this();
		for (int i = 0; i < MFamConstants.FAMILY_SIZE; i += 1) {
			Member mem = parent.members[i];
			Member mem2 = new Member(mem.power, mem.target);
			members[i] = mem2;
		}
	}
	
	public void setMember(int i, int power, int target) {
		members[i].power = power;
		members[i].target = target;
	}
	
	public void setLink(Group group, Family target) {
		links.put(group, target);
	}
	
	public void removeLink(Group group) {
		links.remove(group);
	}
	
	/**
	 * Fills with random, valid members.
	 * @param	rand			The RNG to use
	 */
	public void fillRandomly(Random rand) {
		List<Integer> levels = new ArrayList<Integer>();
		for (int j = 0; j < MFamConstants.FAMILY_SIZE; j += 1) {
			int target;
			do {
				target = rand.nextInt(MFamConstants.POWER_MAX);
			} while (levels.contains(target));
			levels.add(target);
			int power = target;
			if (target > 0 || rand.nextBoolean()) {
				power += 1;
			}
			if (rand.nextFloat() > .95) {
				power += rand.nextInt(3) - 1;
			}
			if (power < 0) power = 0;
			members[j] = new Member(power, target);
		}
		List<Member> sorted = Arrays.asList(members);
		Collections.sort(sorted);
		for (int j = 0; j < sorted.size(); j += 1) {
			members[j] = sorted.get(j);
		}
	}
	
	/**
	 * Fills this family's links randomly with groups to families.
	 * @param	rand			The RNG to use
	 * @param	groups			The groups to use as keys
	 * @param	families		The families to use as values
	 */
	public void fillLinks(Random rand, List<Group> groups, List<Family> families) {
		for (Group grp : groups) {
			if (!grp.families.contains(this)) {
				int index = rand.nextInt(families.size() + 1);
				if (index < families.size()) {
					links.put(grp, families.get(index));
				}
			}
		}
	}
	
	/**
	 * Rebuilds the power target array.
	 */
	public void rebuildPowerTargets() {
		powerTargets = new Member[MFamConstants.POWER_MAX+2];
		for (int power = 0; power < MFamConstants.POWER_MAX+2; power += 1) {
			Member result = null;
			for (Member candidate : members) {
				if (result == null || 
						(candidate.target > result.target &&
						power >= candidate.target)) {
					result = candidate;
				}
			}
			powerTargets[power] = result;
		}
	}
	
	/** @see java.lang.Object#toString() */
	@Override public String toString() { return name; }

}
