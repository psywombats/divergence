/**
 *  FileReaderTest.java
 *  Created on Nov 4, 2012 6:14:22 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.tests;

import java.util.Scanner;

import net.wombatrpgs.mgne.global.Global;

/**
 * Non-unit read in file from jar thing to test the FileLoader.
 */
public class FileReaderTest {

	/**
	 * @param args Unused
	 */
	public static void main(String[] args) {
		Global.setupGlobalForTesting();
		Scanner sc = new Scanner(Global.fileLoader.getStream("res/test/strings.txt"));
		while (sc.hasNextLine()) {
			Global.reporter.inform(sc.nextLine());
		}
	}

}
