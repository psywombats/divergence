/**
 *  MFamTargets.java
 *  Created on May 3, 2014 12:10:30 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wombatrpgs.saga.misc.mfam.data.Family;
import net.wombatrpgs.saga.misc.mfam.data.Group;
import net.wombatrpgs.saga.misc.mfam.data.Member;

/**
 * Contains data on what stats would be acceptable for a configuration.
 */
public class FitnessStats {
	
	/** Metrics ***************************************************************/
	
	public int familyCount;
	public int groupCount;

	/** Map from group size to how many groups of that size should exist */
	public Map<Integer, Integer> sizeDistrib;
	
	/** Map from family intro level to how many families intro at that level */
	public Map<Integer, List<Family>> introDistrib;
	
	/** Map from family intro level to average targeted count at that level */
	public Map<Integer, Float> tierAvgs;
	
	/** Map from family intro level to deviation targeted count at that level */
	public Map<Integer, Float> tierDevs;
	
	/** Map from level to monsters at that level */
	public Map<Integer, Integer> levelDistrib;
	
	/** Map from gains to how often they occur as a percentage */
	public Map<Integer, Float> gains;
	
	/** How many different power levels at which monsters are introduced */
	public int introLevels;
	
	public float averageGain;
	public float gainDev;
	public int biggestGain;
	public int biggestLoss;
	
	public int biggestLevel;
	public int smallestLevel;
	
	public int nontransCount;
	public int unreachCount;
	
	public float transtoAvg;
	public float transtoDev;
	public int transtoMin;
	
	/** Silly display vars ****************************************************/
	
	protected String gainString, lossString;
	protected String rareFam;
	protected List<Member> unreachables;
	protected int lowerBigGainTarget;
	
	/**
	 * Creates a new, empty fitness stat set.
	 */
	public FitnessStats() {
		sizeDistrib = new HashMap<Integer, Integer>();
		introDistrib = new HashMap<Integer, List<Family>>();
		unreachables = new ArrayList<Member>();
		tierDevs = new HashMap<Integer, Float>();
		tierAvgs = new HashMap<Integer, Float>();
		gains = new HashMap<Integer, Float>();
		levelDistrib = new HashMap<Integer, Integer>();
	}
	
	/**
	 * Produces a new fitness stats for a given configuration.
	 * @param	config			The config to calculate for
	 */
	public FitnessStats(Config config) {
		this();
		
		// basic counts
		calcBasicCounts(config);
		
		// group sizes
		calcGroupSizes(config);
		
		// introductory levels
		calcTiers(config);
		
		// gain statistics
		calcGains(config);
		
		// targeted by transformations
		Map<Family, Integer> targeted = calcTargeted(config);
		
		// trans intro distrib
		calcTargetTiers(config, targeted);
		
		// level distribution
		calcLevelDistrib(config);
		
		// null transformations
		calcNullTrans(config);
	}

	protected void calcNullTrans(Config config) {
		for (Family fam : config.families) {
			for (Group grp : config.groups) {
				if (fam.group != grp && fam.links.get(grp) == null) {
					nontransCount += 1;
				}
			}
		}
	}

	protected void calcLevelDistrib(Config config) {
		biggestLevel = 0;
		smallestLevel = Integer.MAX_VALUE;
		for (Family fam : config.families) {
			for (Member mem : fam.members) {
				int level = mem.target;
				Integer existing = levelDistrib.get(level);
				if (existing == null) {
					existing = 0;
				}
				levelDistrib.put(level, existing + 1);
				if (existing+1 > biggestLevel) {
					biggestLevel = existing+1;
				}
			}
		}
		for (Integer level : levelDistrib.keySet()) {
			int size = levelDistrib.get(level);
			if (size < smallestLevel) {
				smallestLevel = size;
			}
		}
	}

	protected void calcTargetTiers(Config config, Map<Family, Integer> targeted) {
		Map<Integer, List<Integer>> targetMap = new HashMap<Integer, List<Integer>>();
		for (int i = 0; i < MFamConstants.POWER_MAX; i += 1) {
			List<Integer> tcounts = new ArrayList<Integer>();
			for (Family fam : config.families) {
				List<Family> fams = introDistrib.get(i);
				if (fams != null && fams.contains(fam)) {
					Integer count = targeted.get(fam);
					if (count != null) {
						tcounts.add(count);
					}
				}
			}
			if (tcounts.size() > 0) {
				targetMap.put(i, tcounts);
			}
		}
		for (int i = 0; i < MFamConstants.POWER_MAX; i += 1) {
			List<Integer> tcounts = targetMap.get(i);
			if (tcounts != null) {
				tierAvgs.put(i, avg(tcounts));
				tierDevs.put(i, stdev(tcounts));
			}
		}
	}

	protected Map<Family, Integer> calcTargeted(Config config) {
		Map<Family, Integer> targeted = new HashMap<Family, Integer>();
		for (Family fam : config.families) {
			targeted.put(fam, 0);
		}
		for (Family fam : config.families) {
			for (Group grp : fam.links.keySet()) {
				Family tgt = fam.links.get(grp);
				if (fam == tgt) continue;
				if (tgt == null) continue;
				int existing = targeted.get(tgt);
				targeted.put(tgt, existing + 1);
			}
		}
		List<Integer> targetCounts = new ArrayList<Integer>();
		transtoMin = Integer.MAX_VALUE;
		for (Family fam : targeted.keySet()) {
			if (fam == null) continue;
			int count = targeted.get(fam);
			targetCounts.add(count);
			if (count < transtoMin) {
				rareFam = fam.name;
				transtoMin = count;
			}
		}
		transtoAvg = avg(targetCounts);
		transtoDev = stdev(targetCounts);
		return targeted;
	}

	protected void calcGains(Config config) {
		List<Member> reachables = new ArrayList<Member>();
		List<Integer> allGains = new ArrayList<Integer>();
		biggestGain = 0;
		biggestLoss = 0;
		for (Family family : config.families) {
			family.rebuildPowerTargets();
		}
		for (Family family : config.families) {
			for (Member eater : family.members) {
				unreachables.add(eater);
				for (Family ateFamily : config.families) {
					for (Member ate : ateFamily.members) {
						Group memberGroup = ateFamily.group;
						Family targetFamily = family.links.get(memberGroup);
						if (targetFamily == null) continue;
						int power = ate.power;
						if (eater.power > power) power = eater.power;
						Member result = targetFamily.powerTargets[power];
						if (family != targetFamily) {
							reachables.add(result);
						}
						
						// gains are based on what you ate, not what you are
						int gain = result.power - ((eater.power>ate.power) ? eater.power : ate.power);
						Float existing = gains.get(gain);
						if (existing == null) existing = 0f;
						gains.put(gain, existing+1);
						allGains.add(gain);
						if (gain > biggestGain || (gain == biggestGain &&
								result.target < lowerBigGainTarget)) {
							lowerBigGainTarget = result.target;
							biggestGain = gain;
							gainString = "+" + gain + " in " + family + "/" +
									eater + " eats " + ateFamily + "/" + ate +
									" to " + targetFamily + "/" + result;
									
						}
						if (gain < biggestLoss) {
							biggestLoss = gain;
							lossString = gain + " in " + family + "/" + eater +
									" eats " + ateFamily + "/" + ate + " to " +
									targetFamily + "/" + result;
									
						}
					}
				}
			}
		}
		
		// gain average
		averageGain = avg(allGains);
		gainDev = stdev(allGains);
		for (Integer gain : gains.keySet()) {
			gains.put(gain, gains.get(gain) / allGains.size());
		}
		
		// calc unreachables, we already iterated once
		List<Member> toRemove = new ArrayList<Member>();
		for (Member unreachable : unreachables) {
			if (reachables.contains(unreachable)) {
				toRemove.add(unreachable);
			}
		}
		for (Member reachable : toRemove) {
			unreachables.remove(reachable);
		}
		unreachCount = unreachables.size();
	}

	protected void calcTiers(Config config) {
		for (int i = 0; i <= MFamConstants.POWER_MAX; i += 1) {
			List<Family> families = new ArrayList<Family>();
			if (i > 0) {
				for (Family fam : config.families) {
					if (fam.members[0].target+1 == i &&
							fam.members[0].power != 0) {
						families.add(fam);
					}
				}
			} else {
				for (Family fam : config.families) {
					if (fam.members[0].target == 0 &&
							fam.members[0].power == 0) {
						families.add(fam);
					}
				}
			}
			if (families.size() > 0) {
				introDistrib.put(i, families);
			}
		}
		introLevels = introDistrib.keySet().size();
	}

	protected void calcGroupSizes(Config config) {
		for (Group group : config.groups) {
			Integer existing = sizeDistrib.get(group.families.size());
			if (existing == null) existing = 0;
			existing += 1;
			sizeDistrib.put(group.families.size(), existing);
		}
	}

	protected void calcBasicCounts(Config config) {
		familyCount = config.families.size();
		groupCount = config.groups.size();
	}
	
	/**
	 * Compares against another stat set and calculates error.
	 * @param	other			The stats to compare against
	 * @return					The calculated error of the other set
	 */
	public float compare(FitnessStats other) {
		float error = 0;
		error += errorFCount(other);
		error += errorGCount(other);
		error += errorIntroC(other);
		error += errorGainAvg(other);
		error += errorGainDev(other);
		error += errorGainHi(other);
		error += errorGainLo(other);
		error += errorSizes(other);
		error += errorReach(other);
		error += errorIntros(other);
		error += errorTTAvg(other);
		error += errorTTDev(other);
		error += errorTTMin(other);
		error += errorTDevs(other);
		error += errorTAvgs(other);
		error += errorGains(other);
		error += errorLevels(other);
		error += errorNotrans(other);
		error += errorLevelHi(other);
		error += errorLevelLo(other);
		
		return error;
	}
	
	public float errorIntroC	(FitnessStats other) { return 30f	* sqerr(introLevels, other.introLevels); }
	public float errorGainAvg	(FitnessStats other) { return 4f	* sqerr(averageGain, other.averageGain); }
	public float errorGainDev	(FitnessStats other) { return 2f	* sqerr(gainDev, other.gainDev); }
	public float errorGainHi	(FitnessStats other) { return 150f	* sqerr(biggestGain, other.biggestGain); }
	public float errorGainLo	(FitnessStats other) { return 2f	* sqerr(biggestLoss, other.biggestLoss); }
	public float errorReach		(FitnessStats other) { return 2f	* sqerr(unreachCount, other.unreachCount); }
	public float errorTTAvg		(FitnessStats other) { return 2f	* sqerr(transtoAvg, other.transtoAvg); }
	public float errorTTDev		(FitnessStats other) { return 3f	* sqerr(transtoDev, other.transtoDev); }
	public float errorTTMin		(FitnessStats other) { return 15f	* sqerr(transtoMin, other.transtoMin); }
	public float errorLevelHi	(FitnessStats other) { return 10f	* sqerr(biggestLevel, other.biggestLevel); }
	public float errorLevelLo	(FitnessStats other) { return 25f	* sqerr(smallestLevel, other.smallestLevel); }
	public float errorGCount	(FitnessStats other) {
		if (Math.abs(groupCount - other.groupCount) > 3) {
			return 8f * sqerr(groupCount, other.groupCount);
		} else {
			return 0;
		}
	}
	public float errorFCount	(FitnessStats other) {
		if (Math.abs(familyCount - other.familyCount) > 1) {
			return 100f	* sqerr(familyCount, other.familyCount);
		} else {
			return 0;
		}
	}
	public float errorNotrans	(FitnessStats other) {
		if (Math.abs(nontransCount - other.nontransCount) > 4) {
			return .1f * Math.abs(nontransCount - other.nontransCount);
		} else {
			return 0f;
		}
	}
	public float errorSizes		(FitnessStats other) {
		float error = 0;
		for (Integer size : other.sizeDistrib.keySet()) {
			Integer ourSize = sizeDistrib.get(size);
			if (ourSize == null) ourSize = 0;
			Integer theirSize = other.sizeDistrib.get(size);
			float e2 = 5f * sqerr(ourSize, theirSize);
			if (size > 3) {
				e2 *= (size + 1.5f);
			}
			error += e2;
		}
		return error;
	}
	public float errorIntros	(FitnessStats other) {
		float error = 0;
		for (Integer level : other.introDistrib.keySet()) {
			int ourCount = 0;
			List<Family> fams = introDistrib.get(level);
			if (fams != null) ourCount = fams.size();
			Integer theirCount = other.introDistrib.get(level).size();
			if (Math.abs(ourCount - theirCount) > 1) {
				error += 7f * sqerr(ourCount, theirCount);
			}
		}
		return error;
	}
	public float errorTAvgs		(FitnessStats other) {
		float error = 0;
		for (Integer level : other.tierAvgs.keySet()) {
			Float value = tierAvgs.get(level);
			if (value == null) value = 0f;
			Float theirValue = other.tierAvgs.get(level);
			if (Math.abs(value - theirValue) > 1) {
				error += 4f * sqerr(value, theirValue);
			}
		}
		return error;
	}
	public float errorTDevs		(FitnessStats other) {
		float error = 0;
		for (Integer level : other.tierDevs.keySet()) {
			Float value = tierDevs.get(level);
			if (value == null) value = 0f;
			Float theirValue = other.tierDevs.get(level);
			error += 6f * sqerr(value, theirValue);
		}
		return error;
	}
	public float errorGains		(FitnessStats other) {
		float error = 0;
		for (Integer gain : gains.keySet()) {
			Float value = gains.get(gain);
			Float their = other.gains.get(gain);
			if (their == null) their = 0f;
			float e = 10f * sqerr(value, their);
			error += e;
		}
		if (gains.get(4) != null) {
			error += gains.get(4) * 5000000f;
			error += 300;
		}
		if (gains.get(3) != null) {
			error += gains.get(3) * 4000000f;
			error += 200;
		}
		if (gains.get(2) != null) {
			error += gains.get(2) * 3000000f;
			error += 100;
		}
		return error;
	}
	public float errorLevels	(FitnessStats other) {
		float error = 0;
		for (Integer level : levelDistrib.keySet()) {
			Integer value = levelDistrib.get(level);
			Integer their = other.levelDistrib.get(level);
			if (their == null) their = 0;
			if (Math.abs(value - their) <= 1) continue;
			float e = 42f * sqerr(value, their);
			if (Math.abs(value - their) >= 3) {
				e *= Math.abs(value - their);
			}
			error += e;
		}
		return error;
	}
	
	/**
	 * Reports some information in string form.
	 */
	public String report() {
		String report = "";
		report += "\n" + ("Family count: " + familyCount);
		report += "\n" + ("Group count: " + groupCount);
		report += "\n" + ("Tier count: " + introLevels);
		report += "\n" + ("Size distribution:");
		for (Integer size : sizeDistrib.keySet()) {
			Integer count = sizeDistrib.get(size);
			report += "\n" + ("  " + size + "-member group x" + count);
		}
		report += "\nPower level distributions:";
		for (Integer level : levelDistrib.keySet()) {
			int count = levelDistrib.get(level);
			String levelStr = String.valueOf(level);
			if (levelStr.length() < 2) levelStr = "0" + levelStr;
			report += "\n" + ("  L" + levelStr + ": " + count + " monsters");
		}
		report += "\n";
		
		report += "\nTransformation power gains breakdown: ";
		report += "\n(only transformations within monster level recorded)";
		List<Integer> gainKeys = new ArrayList<Integer>();
		gainKeys.addAll(gains.keySet());
		Collections.sort(gainKeys);
		Collections.reverse(gainKeys);
		for (Integer gain : gainKeys) {
			String pct = digits(gains.get(gain), 4);
			String gainString = gain >= 0 ? "+" + gain : String.valueOf(gain);
			report += "\n" + gainString + ": " + pct + "%";
		}
		report += "\nAverage: " + digits(averageGain, 3) + " (deviation: " +
				digits(gainDev, 3) + ")";
		report += "\nBiggest jump: " + gainString;
		report += "\nBiggest loss: " + lossString;
		report += "\n" + nontransCount + " non-incestuous meat eatings resulted in no transformation";
		report += "\n";
		
		String tavg = digits(transtoAvg, 3);
		report += "\n" + ("The average group is transformed to " + tavg +
				" times (deviation " + digits(transtoDev, 3) + ", min " + 
				rareFam + " with " + transtoMin + ")");
		report += "\n" + ("By-tier breakdown:");
		for (int level = 0; level < MFamConstants.POWER_MAX; level += 1) {
			if (!tierAvgs.containsKey(level)) continue;
			String avg = digits(tierAvgs.get(level), 4);
			String dev = digits(tierDevs.get(level), 4);
			report += "\n" + ("  Tier " + level + ": targeted average " + avg +
					" times (deviation " + dev + ")");
		}
		
		String unreach = "[";
		for (Member mem : unreachables) {
			unreach += mem.toString();
			unreach += ", ";
		}
		if (unreach.length() > 2) {
			unreach = unreach.substring(0, unreach.length() - 2);
		}
		unreach += "]";
		report += "\n" + ("Unreachable members: " + unreach);
		report += "\n" + ("");
		return report;
	}
	
	/**
	 * Writes up a report on entry levels of monsters.
	 * @return					The report on monster intro levels
	 */
	public String ranks() {
		String ranks = "";
		for (int i = 0; i < MFamConstants.POWER_MAX; i += 1) {
			List<Family> fams = introDistrib.get(i);
			if (fams != null && fams.size() > 0) {
				ranks += "Tier " + i + ": ";
				for (Family fam : fams) {
					ranks += fam.name;
					if (fam != fams.get(fams.size()-1)) {
						ranks += ", ";
					}
				}
				ranks += "\n";
			}
		}
		return ranks;
	}
	
	/**
	 * Constructs the attribute string indicated the error this fitness stats
	 * receives from its various columns.
	 * @param	other			The target stats
	 * @return					A string for error
	 */
	public String attributes(FitnessStats other) {
		StringBuilder build = new StringBuilder();
		build.append("FC ");	build.append(digits(errorFCount(other), 2));
		build.append(", GC ");	build.append(digits(errorGCount(other), 2));
		build.append(", IC ");	build.append(digits(errorIntroC(other), 2));
		build.append(", GA ");	build.append(digits(errorGainAvg(other), 2));
		build.append(", GD ");	build.append(digits(errorGainDev(other), 2));
		build.append(", GH ");	build.append(digits(errorGainHi(other), 2));
		build.append(", GL ");	build.append(digits(errorGainLo(other), 2));
		build.append(", SZ ");	build.append(digits(errorSizes(other), 2));
		build.append(", UR ");	build.append(digits(errorReach(other), 2));
		build.append(", IN ");	build.append(digits(errorIntros(other), 2));
		build.append(", 2A ");	build.append(digits(errorTTAvg(other), 2));
		build.append(", 2D ");	build.append(digits(errorTTDev(other), 2));
		build.append(", 2M ");	build.append(digits(errorTTMin(other), 2));
		build.append(", TD ");	build.append(digits(errorTDevs(other), 2));
		build.append(", TA ");	build.append(digits(errorTAvgs(other), 2));
		build.append(", GS ");	build.append(digits(errorGains(other), 2));
		build.append(", LV ");	build.append(digits(errorLevels(other), 2));
		build.append(", NT ");	build.append(digits(errorNotrans(other), 2));
		build.append(", LH ");	build.append(digits(errorLevelHi(other), 2));
		build.append(", LL ");	build.append(digits(errorLevelLo(other), 2));
		return build.toString();
	}
	
	/**
	 * Rounds to nearest digit for display.
	 * @param	num			The number to round
	 * @param	n			The number of digits to round to
	 * @return				The number with that many digits
	 */
	protected static String digits(float num, int n) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(n);
		df.setMinimumFractionDigits(n);
		return df.format(num);
	}
	
	/**
	 * Calculates the square difference (error) between two values.
	 * @param	target			The target value
	 * @param	other			The actual value
	 * @return					The square error
	 */
	protected static float sqerr(float target, float other) {
		if (target == 0) return Math.abs(other);
		if (other == 0) return Math.abs(target);
		float e = (target - other) / target;
		return Math.abs(e);
	}
	
	/**
	 * Calculates the sum of a list.
	 * @param	list			The list to calculate the sum of
	 * @return					The sum
	 */
	protected static int sum(List<Integer> list) {
		int sum = 0;
		for (int gain : list) {
			sum += gain;
		}
		return sum;
	}
	
	/**
	 * Calculates the average of a list.
	 * @param	list			The list to calculate the average of
	 * @return					The numeric mean
	 */
	protected static float avg(List<Integer> list) {
		return (float) sum(list) / (float) list.size();
	}
	
	/**
	 * Calculates the standard deviation in a list.
	 * @param	list			The list to calculate the deviation of
	 * @return					The standard deviation
	 */
	protected static float stdev(List<Integer> gains) {
		float[] deltas = new float[gains.size()];
		float average = avg(gains);
		for (int i = 0; i < deltas.length; i += 1) {
			float d = gains.get(i) - average;
			deltas[i] = (d * d);
		}
		float sum = 0;
		for (int i = 0; i < deltas.length; i += 1) {
			sum += deltas[i];
		}
		return (float) Math.sqrt(sum / deltas.length);
	}

}
