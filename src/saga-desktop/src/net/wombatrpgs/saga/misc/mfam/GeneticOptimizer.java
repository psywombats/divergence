/**
 *  GeneticOptimizer.java
 *  Created on May 3, 2014 6:03:04 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Optimizes via genetic algorithm.
 */
public class GeneticOptimizer extends Optimizer {
	
	protected static final int GENERATION_SIZE = 12;
	protected static final int BEST_SELECTIONS = 1;
	
	/**
	 * Constructs a new genetic optimizer.
	 * @param	target			The target to aim for
	 */
	public GeneticOptimizer(FitnessStats target) {
		super(target);
	}
	
	/**
	 * Constructs a new genetic optimizer with a seed value.
	 * @param	target			The target to aim for
	 * @param	seed			The seed value to start with, or null
	 */
	public GeneticOptimizer(FitnessStats target, Config seed) {
		super(target, seed);
	}

	/**
	 * We instead interpret "generations" as the max number of stagnant gens.
	 * @see net.wombatrpgs.saga.misc.mfam.Optimizer#run(int)
	 */
	@Override
	public Config run(int maxStag) {
		
		// set up a random generation
		List<Config> generation = new ArrayList<Config>();
		if (seed != null) {
			generation.add(seed);
		}
		while (generation.size() < GENERATION_SIZE) {
			generation.add(new Config(target));
		}
		
		// iterate! geneticate!
		int stagnant = 0;
		float lastError = Float.MAX_VALUE;
		for (int i = 0; stagnant < maxStag; i += 1) {
			Collections.sort(generation);
			Config best = generation.get(0);
			if (best.error >= lastError) {
				stagnant += 1;
			} else {
				lastError = best.error;
				stagnant = 0;
				System.out.print("Generation " + i + ": error " + best.error);
				System.out.print(" (method: " + best.genInfo + ")");
				System.out.println("\t" + best.stats.attributes(target));
			}
			List<Config> nextGen = new ArrayList<Config>();
			int addIndex = 0;
			int addBest = 0;
			if (i > 500) addBest += 1;
			if (i > 1000) addBest += 1;
			if (i > 1500) addBest += 1;
			if (i > 2000) addBest += 1;
			while (nextGen.size() < BEST_SELECTIONS + addBest) {
				nextGen.add(generation.get(addIndex));
				generation.get(addIndex).genInfo = "previous";
				addIndex += 1;
			}
			List<Config> breeders = new ArrayList<Config>();
			for (int j = 0; j < GENERATION_SIZE / 2; j += 1) {
				breeders.add(generation.get(j));
			}
			int addBreed = 0;
			if (i > 100) addBreed += 1;
			if (i > 500) addBreed += 1;
			if (i > 1000) addBreed += 1;
			if (i > 1500) addBreed += 1;
			while (nextGen.size() < GENERATION_SIZE / 2 + addBreed) {
				Config p1 = breeders.get(rand.nextInt(breeders.size()));
				Config p2;
				do {
					p2 = breeders.get(rand.nextInt(breeders.size()));
				} while (p2 == p1);
				nextGen.add(Geneticist.mate(p1, p2, i));
			}
			while (nextGen.size() < GENERATION_SIZE) {
				nextGen.add(new Config(target));
			}
			generation = nextGen;
		}
		
		Collections.sort(generation);
		return generation.get(0);
	}

}
