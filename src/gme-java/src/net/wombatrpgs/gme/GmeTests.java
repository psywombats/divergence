/**
 *  GmeTests.java
 *  Created on Oct 4, 2014 2:44:32 PM for project gme-java
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.gme;

import java.io.IOException;

/**
 * Some tests for the useful but undocumented Java GME port.
 */
public class GmeTests {

	/**
	 * Entry point.
	 * @param	args			The test to run... ugly
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Need the test number");
			return;
		}
		int testNumber = Integer.valueOf(args[0]);
		String filename = (args.length > 1) ? args[1] : "res/ffl1.gbs";
		int track = (args.length > 2) ? Integer.valueOf(args[2]) : 1;
		GmeTester tests;
		try {
			tests = new GmeTester(filename, track);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		switch (testNumber) {
		case 0:		tests.testJava();		break;
		case 1:
		default: System.err.println("Unknown test");
		}
	}

}
