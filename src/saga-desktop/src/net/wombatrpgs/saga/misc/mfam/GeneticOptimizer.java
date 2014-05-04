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
import java.util.Random;

/**
 * Optimizes via genetic algorithm.
 */
public class GeneticOptimizer {
	
	protected static Random rand = MFamConstants.rand;
	
	protected static final int GENERATION_SIZE = 12;
	protected static final int BEST_SELECTIONS = 1;
	
	protected FitnessStats target;
	protected Config seed;
	
	/**
	 * Creates a new genetic optimizer run trying to meet the target supplied.
	 * Does not yet run the trial.
	 * @param	target			The target stats to aim for
	 */
	public GeneticOptimizer(FitnessStats target) {
		this.target = target;
	}
	
	/**
	 * Creates an optimzed to carry on where some other seed left off.
	 * @param	target			The target stats to aim for
	 * @param	seed			An existing config to use as well
	 */
	public GeneticOptimizer(FitnessStats target, Config seed) {
		this.target = target;
		this.seed = seed;
	}
	
	/**
	 * Runs this trial for the built-in number of generations and returns the
	 * result, the highest scoring config seen.
	 * @param	iterations		How many generations to run for
	 * @return					The highest scoring config
	 */
	public Config run(int generations) {
		
		// set up a random generation
		List<Config> generation = new ArrayList<Config>();
		if (seed != null) {
			generation.add(seed);
		}
		while (generation.size() < GENERATION_SIZE) {
			generation.add(new Config(target));
		}
		
		// iterate! geneticate!
		for (int i = 0; i < generations; i += 1) {
			Collections.sort(generation);
			Config best = generation.get(0);
			System.out.println("Generation " + i + ": error " + best.error);
			List<Config> nextGen = new ArrayList<Config>();
			int addIndex = 0;
			while (nextGen.size() < BEST_SELECTIONS) {
				nextGen.add(generation.get(addIndex));
				addIndex += 1;
			}
			List<Config> breeders = new ArrayList<Config>();
			for (int j = 0; j < GENERATION_SIZE / 2; j += 1) {
				breeders.add(generation.get(j));
			}
			while (nextGen.size() < GENERATION_SIZE / 2) {
				Config p1 = breeders.get(rand.nextInt(breeders.size()));
				Config p2;
				do {
					p2 = breeders.get(rand.nextInt(breeders.size()));
				} while (p2 == p1);
				nextGen.add(Geneticist.mate(p1, p2));
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
