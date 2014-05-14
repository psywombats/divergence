/**
 *  TweakOptimizer.java
 *  Created on May 8, 2014 7:40:06 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc.mfam;

/**
 * Optimizes via a more simple tweaking algorithm that selects the best change
 * and takes it.
 */
public class TweakOptimizer extends Optimizer {
	
	/**
	 * Constructs a new tweak optimizer.
	 * @param	target			The target to aim for
	 */
	public TweakOptimizer(FitnessStats target) {
		super(target);
	}

	/**
	 * Constructs a new tweak optimizer with a seed value.
	 * @param	target			The target to aim for
	 * @param	seed			The seed value to start with, or null
	 */
	public TweakOptimizer(FitnessStats target, Config seed) {
		super(target, seed);
	}

	/**
	 * @see net.wombatrpgs.saga.misc.mfam.Optimizer#run(int)
	 */
	@Override
	public Config run(int generations) {
		Config best = (seed == null) ? new Config() : seed;
		best.target = target;
		
		for (int i = 0; i < generations; i += 1) {
			Config next = Geneticist.idealize(best);
			System.out.print("Iteration " + i + ": error " + next.error);
			System.out.println("\t" + next.stats.attributes(target));
			best = next;
		}
		
		return best;
	}

}
