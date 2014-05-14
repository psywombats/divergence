/**
 *  TwoTweakOptimizer.java
 *  Created on May 9, 2014 1:20:03 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Optimizes by making a random change and then an optimal one. Sloow.
 */
public class TwoTweakOptimizer extends Optimizer {
	
	protected static final int GENERATION_SIZE = 3;
	protected static final int BEST_SELECTIONS = 2;

	/**
	 * Creates a new two tweak optimizer with a seed.
	 * @param	target			The target to aim for
	 * @param	seed			The seed to start with
	 */
	public TwoTweakOptimizer(FitnessStats target, Config seed) {
		super(target, seed);
	}

	/**
	 * @see net.wombatrpgs.saga.misc.mfam.Optimizer#run(int)
	 */
	@Override
	public Config run(int generations) {
		
		List<Config> generation = new ArrayList<Config>();
		while (generation.size() < GENERATION_SIZE) {
			generation.add(new Config(seed));
		}
		
		for (int i = 0; i < generations; i += 1) {
			Collections.sort(generation);
			Config best = generation.get(0);
			System.out.print("Generation " + i + ": error " + best.error);
			System.out.println("\t" + best.stats.attributes(target));
			List<Config> newGen = new ArrayList<Config>();
			for (int j = 0; j < BEST_SELECTIONS; j += 1) {
				newGen.add(generation.get(j));
			}
			while (newGen.size() < GENERATION_SIZE) {
				newGen.add(best);
			}
			generation.clear();
			for (Config parent : newGen) {
				Config child = Geneticist.tweakValue(parent, parent);
				Config child2 = Geneticist.idealTweak(child, child);
				generation.add(child2);
			}
		}
		
		return generation.get(0);
	}

}
