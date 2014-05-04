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
	public Map<Integer, Float> levelAvgs;
	
	/** Map from family intro level to deviation targeted count at that level */
	public Map<Integer, Float> levelDevs;
	
	/** Map from gains to how often they occur as a percentage */
	public Map<Integer, Float> gains;
	
	/** How many different power levels at which monsters are introduced */
	public int introLevels;
	
	public float averageGain;
	public float gainDev;
	public int biggestGain;
	public int biggestLoss;
	
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
		levelDevs = new HashMap<Integer, Float>();
		levelAvgs = new HashMap<Integer, Float>();
		gains = new HashMap<Integer, Float>();
	}
	
	/**
	 * Produces a new fitness stats for a given configuration.
	 * @param	config			The config to calculate for
	 */
	public FitnessStats(Config config) {
		this();
		
		// basic counts
		familyCount = config.families.size();
		groupCount = config.groups.size();
		
		// group sizes
		for (Group group : config.groups) {
			Integer existing = sizeDistrib.get(group.families.size());
			if (existing == null) existing = 0;
			existing += 1;
			sizeDistrib.put(group.families.size(), existing);
		}
		
		// introductory levels
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
		
		// gain statistics
		List<Member> reachables = new ArrayList<Member>();
		List<Integer> allGains = new ArrayList<Integer>();
		biggestGain = 0;
		biggestLoss = 0;
		for (Family family : config.families) {
			for (Member eater : family.members) {
				unreachables.add(eater);
				for (Family ateFamily : config.families) {
					for (Member ate : ateFamily.members) {
						if (ate.target > eater.power) continue;
						Group memberGroup = null;
						for (Group grp : config.groups) {
							if (grp.families.contains(ateFamily)) {
								memberGroup = grp;
								break;
							}
						}
						Family targetFamily = family.links.get(memberGroup);
						if (targetFamily == null) continue;
						int power = ate.power;
						if (eater.power > power) power = eater.power;
						Member result = null;
						for (Member candidate : targetFamily.members) {
							if (result == null || 
									(candidate.target > result.target &&
									power >= candidate.target)) {
								result = candidate;
							}
						}
						if (family != targetFamily && !reachables.contains(result)) {
							reachables.add(result);
						}
						int gain = result.target - eater.target;
						if (eater.power == 0 && result.power != 0) gain += 1;
						Float existing = gains.get(gain);
						if (existing == null) existing = 0f;
						gains.put(gain, existing+1);
						allGains.add(gain);
						if (gain > biggestGain || (gain == biggestGain &&
								result.target < lowerBigGainTarget)) {
							lowerBigGainTarget = result.target;
							biggestGain = gain;
							gainString = family + "/" + eater + " eats " +
									ateFamily + "/" + ate + " to " +
									targetFamily + "/" + result;
									
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
		for (Member reachable : reachables) {
			unreachables.remove(reachable);
		}
		unreachCount = unreachables.size();
		
		// targeted by transformations
		Map<Family, Integer> targeted = new HashMap<Family, Integer>();
		for (Family fam : config.families) {
			targeted.put(fam, 0);
		}
		for (Family fam : config.families) {
			for (Group grp : fam.links.keySet()) {
				Family tgt = fam.links.get(grp);
				if (fam == tgt) continue;
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
		
		// trans intro distrib
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
				levelAvgs.put(i, avg(tcounts));
				levelDevs.put(i, stdev(tcounts));
			}
		}
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
		error += errorLDevs(other);
		error += errorLAvgs(other);
		error += errorGains(other);
		
		if (gains.get(3) != null) {
			error += gains.get(3) * 40000f;
		}
		
		return error;
	}
	
	public float errorFCount	(FitnessStats other) { return 100f	* sqerr(familyCount, other.familyCount); }
	public float errorGCount	(FitnessStats other) { return 8f	* sqerr(groupCount, other.groupCount); }
	public float errorIntroC	(FitnessStats other) { return 20f	* sqerr(introLevels, other.introLevels); }
	public float errorGainAvg	(FitnessStats other) { return 10f	* sqerr(averageGain, other.averageGain); }
	public float errorGainDev	(FitnessStats other) { return 15f	* sqerr(gainDev, other.gainDev); }
	public float errorGainHi	(FitnessStats other) { return 150f	* sqerr(biggestGain, other.biggestGain); }
	public float errorGainLo	(FitnessStats other) { return 2f	* sqerr(biggestLoss, other.biggestLoss); }
	public float errorReach		(FitnessStats other) { return 2f	* sqerr(unreachCount, other.unreachCount); }
	public float errorTTAvg		(FitnessStats other) { return 2f	* sqerr(transtoAvg, other.transtoAvg); }
	public float errorTTDev		(FitnessStats other) { return 3f	* sqerr(transtoDev, other.transtoDev); }
	public float errorTTMin		(FitnessStats other) { return 15f	* sqerr(transtoMin, other.transtoMin); }
	public float errorSizes		(FitnessStats other) {
		float error = 0;
		for (Integer size : other.sizeDistrib.keySet()) {
			Integer ourSize = sizeDistrib.get(size);
			if (ourSize == null) ourSize = 0;
			Integer theirSize = other.sizeDistrib.get(size);
			error += 2f * sqerr(ourSize, theirSize);
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
			error += 40f * sqerr(ourCount, theirCount);
		}
		return error;
	}
	public float errorLAvgs		(FitnessStats other) {
		float error = 0;
		for (Integer level : other.levelAvgs.keySet()) {
			Float value = levelAvgs.get(level);
			if (value == null) value = 0f;
			Float theirValue = other.levelAvgs.get(level);
			error += 3f * sqerr(value, theirValue);
		}
		return error;
	}
	public float errorLDevs		(FitnessStats other) {
		float error = 0;
		for (Integer level : other.levelDevs.keySet()) {
			Float value = levelDevs.get(level);
			if (value == null) value = 0f;
			Float theirValue = other.levelDevs.get(level);
			error += 10f * sqerr(value, theirValue);
		}
		return error;
	}
	public float errorGains		(FitnessStats other) {
		float error = 0;
		for (Integer gain : gains.keySet()) {
			Float value = gains.get(gain);
			Float their = other.gains.get(gain);
			if (their == null) their = 0f;
			float e = 2f * sqerr(value, their);
			if (gain > other.biggestGain) e *= 50;
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
		report += "\n";
		
		report += "\nTransformation power gains breakdown: ";
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
		report += "\nBiggest jump: " + gainString + " (+" + biggestGain + ")";
		report += "\n";
		
		String tavg = digits(transtoAvg, 3);
		report += "\n" + ("The average group is transformed to " + tavg +
				" times (deviation " + digits(transtoDev, 3) + ", min " + 
				rareFam + " with " + transtoMin + ")");
		report += "\n" + ("By-tier breakdown:");
		for (int level = 0; level < MFamConstants.POWER_MAX; level += 1) {
			if (!levelAvgs.containsKey(level)) continue;
			String avg = digits(levelAvgs.get(level), 4);
			String dev = digits(levelDevs.get(level), 4);
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
	 * Rounds to nearest digit for display.
	 * @param	num			The number to round
	 * @param	n			The number of digits to round to
	 * @return				The number with that many digits
	 */
	protected static String digits(float num, int n) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(n);
		return df.format(num);
	}
	
	/**
	 * Calculates the square difference (error) between two values.
	 * @param	target			The target value
	 * @param	other			The actual value
	 * @return					The square error
	 */
	protected static float sqerr(float target, float other) {
		if (target == 0) return other;
		if (other == 0) return target;
		float e = (target - other) / other;
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
		List<Float> deltas = new ArrayList<Float>();
		float average = avg(gains);
		for (int gain : gains) {
			float d = gain - average;
			deltas.add(d * d);
		}
		float sum = 0;
		for (float delta : deltas) {
			sum += delta;
		}
		return (float) Math.sqrt(sum / deltas.size());
	}

}
