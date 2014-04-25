/**
 *  AgiPriorityTest.java
 *  Created on Apr 24, 2014 4:23:32 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.misc;

import java.util.Random;

/**
 * Probabiltiy calculator for turn order priority.
 */
public class AgiPriorityTest {
	
	protected static final int TRIALS = 10000;
	
	/**
	 * Entry point.
	 * @param args <agi1> <agi2>
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: AgiPriorityTest <agi1> <agi2>");
			return;
		}
		Random r = new Random();
		int agi1 = Integer.valueOf(args[0]);
		int agi2 = Integer.valueOf(args[1]);
		int wins1 = 0;
		int wins2 = 0;
		for (int i = 0; i < TRIALS; i += 1) {
			int t1 = agi1 + r.nextInt(agi1);
			int t2 = agi2 + r.nextInt(agi2);
			if (t1 > t2) {
				wins1 += 1;
			} else {
				wins2 += 1;
			}
		}
		float p1 = (float) wins1 / (float) TRIALS;
		float p2 = (float) wins2 / (float) TRIALS;
		System.out.println("p1 will go first " + p1 + "% of the time");
		System.out.println("p2 will go first " + p2 + "% of the time");
	}

}
