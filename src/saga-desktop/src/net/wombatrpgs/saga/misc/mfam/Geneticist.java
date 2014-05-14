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
	 * @param	gen				The generation of the simulation we're on
	 * @return					Their offspring
	 */
	public static Config mate(Config p1, Config p2, int gen) {
		int changeLevel = rand.nextInt(6);
		switch (changeLevel) {
		case 0:		return groupsSwap(p1, p2);
		case 1:		return changeJump(p1, p2);
		case 2:		return takeFamily(p2, p2);
		case 3:		return levelsSwap(p1, p2);
		case 4:		return rebuildSet(p1, p2);
		default:	return tweakValue(p1, p2);
		}
	}
	
	/**
	 * Runs the ideal tweak for the parent and returns it as a descendant.
	 * @param	p1				The autosexual parent
	 * @return					The child
	 */
	public static Config idealize(Config p1) {
		return idealTweak(p1, p1);
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
				fam.setMember(j, power, target);
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
				newFam.setLink(grp, child.families.get(index));
			}
		}
		// randomly point families at this
		for (Family fam : child.families) {
			for (Group grp : child.groups) {
				if (grp.families.contains(newFam) &&
						(rand.nextInt(child.families.size()) == 0)) {
					fam.setLink(grp, newFam);
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
					fam.setLink(grp, newFam);
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
				fam.removeLink(oldGroup);
				if (oldTarget == null) continue;
				int i = child.groups.indexOf(oldGroup);
				if (i < newGroups.size()) {
					Group newGroup = newGroups.get(i);
					if (!newGroup.families.contains(fam)) {
						fam.setLink(newGroup, oldTarget);
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
						fam.setLink(grp, child.families.get(index));
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
			child.genInfo = "rebdMembs";
		} else {
			// tweak random link
			fam.fillLinks(rand, child.groups, child.families);
			child.genInfo = "rebdLinks";
		}
		
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
				fam.removeLink(grp);
			} else if (!grp.families.contains(fam)) { 
				fam.setLink(grp, child.families.get(index));
			}
			child.genInfo = "tweakLink";
		} else {
			int index = rand.nextInt(fam.members.length);
			Member mem = fam.members[index];
			int power = mem.power;
			int target = mem.target;
			if (rand.nextBoolean()) {
				// change power level
				power += rand.nextBoolean() ? 1 : -1;
				child.genInfo = "tweakPower";
			} else {
				// change target level (and power level)
				List<Integer> targets = new ArrayList<Integer>();
				for (Member other : fam.members) {
					if (other != mem) targets.add(other.target);
				}
				do {
					target = rand.nextInt(MFamConstants.POWER_MAX);
				} while (targets.contains(target));
				power = target + 1;
				child.genInfo = "tweakTarget";
			}
			if (power < 0) power = 0;
			if (target < 0) target = 0;
			if (power > MFamConstants.POWER_MAX)
				power = MFamConstants.POWER_MAX;
			if (target > MFamConstants.POWER_MAX)
				target = MFamConstants.POWER_MAX;
			fam.setMember(index, power, target);
		}
		
		return child;
	}
	
	/**
	 * Changes the target of the biggest jump in the config.
	 * @param	p1				The first parent
	 * @param	p2				The second parent
	 * @return					Their offspring
	 */
	protected static Config changeJump(Config p1, Config p2) {
		Config child = new Config(p1);
		
		int biggestGain = 0;
		Family gainFam = null;
		Group gainGrp = null;
		for (Family family : child.families) {
			family.rebuildPowerTargets();
		}
		for (Family family : child.families) {
			for (Member eater : family.members) {
				for (Family ateFamily : child.families) {
					for (Member ate : ateFamily.members) {
						// if (ate.power > eater.power) continue;
						Group memberGroup = ateFamily.group;
						Family targetFamily = family.links.get(memberGroup);
						if (targetFamily == null) continue;
						int power = ate.power;
						if (eater.power > power) power = eater.power;
						Member result = targetFamily.powerTargets[power];
						
						int gain = result.power - ((eater.power>ate.power) ? eater.power : ate.power);
						if (gain > biggestGain) {
							biggestGain = gain;
							gainFam = family;
							gainGrp = memberGroup;
						}
					}
				}
			}
		}
		gainFam.setLink(gainGrp, child.families.get(rand.nextInt(child.families.size())));
		
		child.genInfo = "changeJump";
		return child;
	}
	
	/**
	 * Finds the best single change in one parent and returns that as child.
	 * @param	p1				The first parent
	 * @param	p2				The second parent
	 * @return					Their offspring
	 */
	protected static Config idealTweak(Config p1, Config p2) {
		p1.finalizeStats();
		Config best = p1;
		float bestErr = p1.error;
		
		// ideal link tweak
		for (int famInd = 0; famInd < p1.families.size(); famInd += 1) {
			for (int grpInd = 0; grpInd < p1.groups.size(); grpInd += 1) {
				for (int tgtInd = 0; tgtInd < p1.families.size()+1; tgtInd += 1) {
					Config child = new Config(p1);
					Family fam = child.families.get(famInd);
					Group grp = child.groups.get(grpInd);
					Family tgt = null;
					if (tgtInd < p1.families.size()) {
						tgt = child.families.get(tgtInd);
					}
					if (grp.families.contains(fam)) continue;
					if (tgt == null) {
						fam.removeLink(grp);
					} else {
						fam.setLink(grp, tgt);
					}
					child.finalizeStats();
					if (child.error < bestErr) {
						best = child;
						best.genInfo = "bestLink";
						bestErr = child.error;
					}
				}
			}
		}
		
		// ideal target/power
		for (int famInd = 0; famInd < p1.families.size(); famInd += 1) {
			for (int memInd = 0; memInd < MFamConstants.FAMILY_SIZE; memInd += 1) {
				for (int lvl = 0; lvl <= MFamConstants.POWER_MAX; lvl += 1) {
					Config child = new Config(p1);
					Family fam = child.families.get(famInd);
					int power = lvl;
					int target = lvl - 1;
					if (target < 0) target = 0;
					fam.setMember(memInd, power, target);
					child.finalizeStats();
					if (child.error < bestErr) {
						best = child;
						best.genInfo = "bestLevel";
						bestErr = child.error;
					}
				}
			}
		}
		
		return best;
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
				fam.removeLink(grp);
			}
		}
		for (Family fam : config.families) {
			for (Group grp : config.groups) {
				if (fam.links.get(grp) == delFam) {
					int index2 = rand.nextInt(config.families.size());
					fam.setLink(grp, config.families.get(index2));
				}
			}
		}
	}

}
