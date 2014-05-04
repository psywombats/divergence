/**
 *  Geneticist.java
 *  Created on May 3, 2014 4:36:52 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.wombatrpgs.saga.misc.mfam.data.Family;
import net.wombatrpgs.saga.misc.mfam.data.Group;
import net.wombatrpgs.saga.misc.mfam.data.Member;

/**
 * Static thing for making configs out of other configs. Sexy.
 */
public class Geneticist {
	
	protected static Random rand = MFamConstants.rand;
	
	/**
	 * Pairs two configurations together via a genetic algorithm and returns
	 * their offspring. Could choose one of several algorithms.
	 * @param	p1				The first parent
	 * @param	p2				The second parent
	 * @return					Their offspring
	 */
	public static Config mate(Config p1, Config p2) {
		switch (rand.nextInt(5)) {
		case 0:		return levelsSwap(p1, p2);
		case 1:		return takeFamily(p2, p2);
		case 2:		return groupsSwap(p1, p2);
		case 3:		return rebuildSet(p1, p2);
		case 4:		return tweakValue(p1, p2);
		default:	System.err.println("Default mate!"); return null;
		}
	}
	
	/**
	 * Mating that just returns one of the parents, essentially.
	 * @param	p1				The first parent
	 * @param	p2				The second parent
	 * @return					Their offspring
	 */
	protected static Config keepParent(Config p1, Config p2) {
		Config child = new Config(p1);
		child.genInfo = "keepParent";
		return child;
	}
	
	/**
	 * Swaps the innards of one config with another.
	 * @param	p1				The first parent
	 * @param	p2				The second parent
	 * @return					Their offspring
	 */
	protected static Config levelsSwap(Config p1, Config p2) {
		Config child = new Config(p1);
		for (int i = 0; i < child.families.size(); i += 1) {
			Family fam = child.families.get(i);
			if (i >= p2.families.size()) break;
			Family pfam = p2.families.get(i);
			for (int j = 0; j < fam.members.length; j += 1) {
				Member pmem = pfam.members[j];
				int power = pmem.power;
				int target = pmem.target;
				if (power < 0) power = 0;
				if (target < 0) target = 0;
				if (power > MFamConstants.POWER_MAX) power = MFamConstants.POWER_MAX;
				if (target > MFamConstants.POWER_MAX) target = MFamConstants.POWER_MAX;
				fam.members[j] = new Member(power, target);
			}
		}
		child.genInfo = "levelsSwap";
		return child;
	}
	
	/**
	 * Grabs an additional family from the parent.
	 * @param	p1				The first parent
	 * @param	p2				The second parent
	 * @return					Their offspring
	 */
	protected static Config takeFamily(Config p1, Config p2) {
		Config child = new Config(p1);
		
		// delete 0 to 2 families
		int toRemove = rand.nextInt(3);
		for (int i = 0; i < toRemove; i += 1) {
			int index = rand.nextInt(child.families.size());
			Family delFam = child.families.get(index);
			deleteFamily(child, delFam);
		}
		
		// add the family
		int index3 = rand.nextInt(p2.families.size());
		Family newFam = new Family(p2.families.get(index3));
		child.families.add(newFam);
		child.groups.get(rand.nextInt(child.groups.size())).addFamily(newFam);
		// add to random group
		for (Group grp : child.groups) {
			if (!grp.families.contains(newFam)) {
				int index = rand.nextInt(child.families.size());
				newFam.links.put(grp, child.families.get(index));
			}
		}
		// randomly point families at this
		for (Family fam : child.families) {
			for (Group grp : child.groups) {
				if (grp.families.contains(newFam) &&
						(rand.nextInt(child.families.size()) == 0)) {
					fam.links.put(grp, newFam);
				}
			}
		}
		// claim half of empty spaces
		for (Family fam : child.families) {
			for (Group grp : child.groups) {
				if (!fam.links.keySet().contains(grp) &&
						!grp.families.contains(fam) &&
						!grp.families.contains(newFam) &&
						rand.nextFloat() > .4) {
					fam.links.put(grp, newFam);
				}
			}
		}
		child.genInfo = "takeFamily";
		return child;
	}
	
	/**
	 * Swaps the group configuration of one config with another.
	 * @param	p1				The first parent
	 * @param	p2				The second parent
	 * @return					Their offspring
	 */
	protected static Config groupsSwap(Config p1, Config p2) {
		Config child = new Config(p1);
		
		// construct the new groups
		List<Group> newGroups = new ArrayList<Group>();
		for (int i = 0; i < p2.groups.size(); i += 1) {
			Group newGroup = new Group();
			for (Family fam : p2.groups.get(i).families) {
				int index = p2.families.indexOf(fam);
				if (index < child.families.size()) {
					newGroup.addFamily(child.families.get(index));
				}
			}
			if (newGroup.families.size() > 0) {
				newGroups.add(newGroup);
			}
		}
		
		// add anything we missed
		for (Family fam : child.families) {
			boolean found = false;
			for (Group grp : newGroups) {
				if (grp.families.contains(fam)) {
					found = true;
					break;
				}
			}
			if (!found) {
				int index = rand.nextInt(newGroups.size());
				newGroups.get(index).addFamily(fam);
			}
		}
		
		// replace the links
		for (Family fam : child.families) {
			for (Group oldGroup : child.groups) {
				Family oldTarget = fam.links.get(oldGroup);
				fam.links.remove(oldGroup);
				if (oldTarget == null) continue;
				int i = child.groups.indexOf(oldGroup);
				if (i < newGroups.size()) {
					Group newGroup = newGroups.get(i);
					if (!newGroup.families.contains(fam)) {
						fam.links.put(newGroup, oldTarget);
					}
				}
			}
		}
		
		// randomly fill empty space
		child.groups = newGroups;
		for (Family fam : child.families) {
			for (Group grp : child.groups) {
				if (!grp.families.contains(fam) && fam.links.get(grp) == null) {
					int index = rand.nextInt(child.families.size() + 10);
					if (index < child.families.size()) {
						fam.links.put(grp, child.families.get(index));
					}
				}
			}
		}
		
		child.genInfo = "groupsSwap";
		return child;
	}
	
	/**
	 * Rebuilds a random family or link within the child.
	 * @param	p1				The first parent
	 * @param	p2				The second parent
	 * @return					Their offspring
	 */
	protected static Config rebuildSet(Config p1, Config p2) {
		Config child = new Config(p1);
		
		int index = rand.nextInt(child.families.size());
		Family fam = child.families.get(index);
		if (rand.nextBoolean()) {
			// tweak random family
			fam.fillRandomly(rand);
		} else {
			// tweak random link
			fam.fillLinks(rand, child.groups, child.families);
		}
		
		child.genInfo = "rebuildSet";
		return child;
	}
	
	/**
	 * Tweaks an individual link target or member level.
	 * @param	p1				The first parent
	 * @param	p2				The second parent
	 * @return					Their child
	 */
	protected static Config tweakValue(Config p1, Config p2) {
		Config child = new Config(p1);
		
		Family fam = child.families.get(rand.nextInt(child.families.size()));
		if (rand.nextBoolean()) {
			// change link target
			Group grp = child.groups.get(rand.nextInt(child.groups.size()));
			int index = rand.nextInt(child.families.size() + 6);
			if (index >= child.families.size()) {
				fam.links.remove(grp);
			} else if (!grp.families.contains(fam)) { 
				fam.links.put(grp, child.families.get(index));
			}
		} else {
			Member mem = fam.members[rand.nextInt(fam.members.length)];
			if (rand.nextBoolean()) {
				// change power level
				mem.power += rand.nextBoolean() ? 1 : -1;
			} else {
				// change target level (and power level)
				List<Integer> targets = new ArrayList<Integer>();
				for (Member other : fam.members) {
					if (other != mem) targets.add(other.target);
				}
				do {
					mem.target = rand.nextInt(MFamConstants.POWER_MAX);
				} while (targets.contains(mem.target));
				mem.power = mem.target + 1;
			}
			if (mem.power < 0) mem.power = 0;
			if (mem.target < 0) mem.target = 0;
			if (mem.power > MFamConstants.POWER_MAX)
				mem.power = MFamConstants.POWER_MAX;
			if (mem.target > MFamConstants.POWER_MAX)
				mem.target = MFamConstants.POWER_MAX;
		}
		
		child.genInfo = "tweakValue";
		return child;
	}
	
	/**
	 * Deletes a family from a config.
	 * @param	config			The config to work on
	 * @param	delFam			The family to remove
	 */
	protected static void deleteFamily(Config config, Family delFam) {
		config.families.remove(delFam);
		List<Group> toRemove = new ArrayList<Group>();
		for (Group grp : config.groups) {
			if (grp.families.contains(delFam)) {
				grp.families.remove(delFam);
				if (grp.families.size() == 0) {
					toRemove.add(grp);
				}
			}
		}
		for (Group grp : toRemove) {
			config.groups.remove(grp);
			for (Family fam : config.families) {
				fam.links.remove(grp);
			}
		}
		for (Family fam : config.families) {
			for (Group grp : config.groups) {
				if (fam.links.get(grp) == delFam) {
					int index2 = rand.nextInt(config.families.size());
					fam.links.put(grp, config.families.get(index2));
				}
			}
		}
	}

}
