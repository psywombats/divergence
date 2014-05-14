/**
 *  Optimizer.java
 *  Created on May 8, 2014 7:41:00 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam;

import java.util.Random;

/**
 * Superclass for all optimizers.
 */
public abstract class Optimizer {
	
	protected static Random rand = MFamConstants.rand;
	
	protected FitnessStats target;
	protected Config seed;
	
	/**
	 * Creates a new optimizer run trying to meet the target supplied. Does not
	 * yet run the trial.
	 * @param	target			The target stats to aim for
	 */
	public Optimizer(FitnessStats target) {
		this.target = target;
	}
	
	/**
	 * Creates an optimzer to carry on where some other seed left off. Does not
	 * yet run the trial. Adds the seed to the first generation.
	 * @param	target			The target stats to aim for
	 * @param	seed			An existing config to use as well
	 */
	public Optimizer(FitnessStats target, Config seed) {
		this.target = target;
		this.seed = seed;
	}
	
	/**
	 * Runs the optimizer for a number of generations then returns the result.
	 * @param	generations		The number of generations to run for.
	 */
	public abstract Config run(int generations);

}
