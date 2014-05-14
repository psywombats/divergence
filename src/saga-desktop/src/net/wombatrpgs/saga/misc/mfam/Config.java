/**
 *  MFamConfiguration.java
 *  Created on May 2, 2014 11:54:36 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.wombatrpgs.saga.misc.mfam.data.Family;
import net.wombatrpgs.saga.misc.mfam.data.Group;
import net.wombatrpgs.saga.misc.mfam.data.Member;

/**
 * A configuration of monster families in the optimizer.
 */
public class Config implements Comparable<Config> {
	
	protected static Random rand = MFamConstants.rand;
	
	public FitnessStats target;
	
	public List<Family> families;
	public List<Group> groups;
	
	public transient String genInfo;
	public transient FitnessStats stats;
	public transient float error;
	
	/**
	 * Creates a configuration with random entries that ~sort of~ looks like the
	 * target. Mostly populates with garbage as an initial configuration.
	 * @param	targetStats		The target stats to strive for
	 */
	public Config(FitnessStats targetStats) {
		this();
		this.target = targetStats;
		
		for (int i = 0; i < targetStats.familyCount; i += 1) {
			Family fam = new Family();
			fam.fillRandomly(rand);
			families.add(fam);
		}
		
		int addIndex = 0;
		for (int size : targetStats.sizeDistrib.keySet()) {
			for (int i = 0; i < targetStats.sizeDistrib.get(size); i++) {
				Group grp = new Group();
				for (int j = 0; j < size; j += 1) {
					grp.addFamily(families.get(addIndex));
					addIndex += 1;
				}
				groups.add(grp);
			}
		}
		
		for (int i = 0; i < families.size(); i += 1) {
			Family fam = families.get(i);
			fam.fillLinks(rand, groups, families);
		}
		
		genInfo = "randumbized";
	}
	
	/**
	 * Copy constructor.
	 * @param	parent			The parent to copy from
	 */
	public Config(Config parent) {
		this();
		this.target = parent.target;
		
		for (Family fam : parent.families) {
			families.add(new Family(fam));
		}
		
		for (Group grp : parent.groups) {
			Group grp2 = new Group();
			for (Family fam : grp.families) {
				int i = parent.families.indexOf(fam);
				grp2.addFamily(families.get(i));
			}
			groups.add(grp2);
		}
		
		for (Family fam : parent.families) {
			for (Group grp : fam.links.keySet()) {
				int i = parent.families.indexOf(fam);
				int j = parent.groups.indexOf(grp);
				int k = parent.families.indexOf(fam.links.get(grp));
				if (j == -1 || k == -1) continue;
				Family fam2 = families.get(i);
				Group grp2 = groups.get(j);
				Family target = families.get(k);
				fam2.links.put(grp2, target);
			}
		}
	}
	
	/**
	 * Construct an ascii representation of the relations in this config.
	 * @return					An ascii graph for this config
	 */
	public String graph() {
		String graph = "";
		
		families.clear();
		for (Group grp : groups) {
			for (Family fam  : grp.families) {
				families.add(fam);
			}
		}
		
		int maxGroupSize = 0;
		for (Group grp : groups) {
			if (grp.families.size() > maxGroupSize) {
				maxGroupSize = grp.families.size();
			}
		}
		for (int i = maxGroupSize - 1; i >= 0; i -= 1) {
			graph += "       |";
			for (Group grp : groups) {
				if (i < grp.families.size()) {
					graph += " " + grp.families.get(i).name + " |";
				} else {
					graph += "     |";
				}
			}
			graph += "\n";
		}
		graph += "-------+";
		for (int i = 0; i < groups.size(); i += 1) {
			graph += "-----+";
		}
		graph += "\n";
		
		for (Family fam : families) {
			graph += " " + fam.name + "   |";
			for (Group grp : groups) {
				Family tgt = fam.links.get(grp);
				if (tgt == null) {
					graph += "     |";
				} else {
					graph += " " + tgt.name + " |";
				}
			}
			graph += "\n";
		}
		graph += "-------+";
		for (int i = 0; i < groups.size(); i += 1) {
			graph += "-----+";
		}
		graph +="\n\n";
		
		String divider = "";
		divider += "-------+";
		for (int i = 0; i < MFamConstants.FAMILY_SIZE+1; i += 1) {
			divider += "------------+";
		}
		for (int j = 0; j < 2; j += 1) {
			divider += "-";
			for (int i = 0; i < MFamConstants.FAMILY_SIZE+1; i += 1) {
				divider += "-";
			}
			divider += "-+";
		}
		
		String columns = "Family:  Members:";
		while (columns.length() < divider.length() - 16) columns += " ";
		columns += "Meat:   Target:\n";
		graph += columns;
		
		graph += divider;
		graph += "\n";
		
		for (Family fam : families) {
			graph += " " + fam.name + "   |";
			String power = "";
			String target = "";
			for (Member mem : fam.members) {
				String memname = mem.name;
				while (memname.length() < 10) memname += " ";
				graph += " " + memname + " ";
				if (mem == fam.members[MFamConstants.FAMILY_SIZE-1]) {
					graph += "|";
				} else {
					graph += " ";
				}
				power += Integer.toHexString(mem.power);
				target += Integer.toHexString(mem.target);
			}
			power += "f";
			target += "f";
			graph += "            |";
			graph += " " + power + " |";
			graph += " " + target + " |";
			graph += "\n" + divider + "\n";
		}
		
		return graph;
	}
	
	@Override
	public int compareTo(Config other) {
		other.finalizeStats();
		finalizeStats();
		return Math.round(error - other.error);
	}
	
	/**
	 * Internal constructor.
	 */
	protected Config() {
		families = new ArrayList<Family>();
		groups = new ArrayList<Group>();
	}
	
	/**
	 * Makes sure we have stats.
	 */
	protected void finalizeStats() {
		for (Family fam : families) {
			List<Member> sorted = Arrays.asList(fam.members);
			Collections.sort(sorted);
			for (int i = 0; i < fam.members.length; i += 1) {
				fam.members[i] = sorted.get(i);
			}
		}
		if (stats == null) {
			stats = new FitnessStats(this);
			if (target != null) {
				error = stats.compare(target);
			} else {
				error = Float.MAX_VALUE;
			}
		}
	}

}
