/**
 *  PerfectPrinter.java
 *  Created on Aug 8, 2012 4:12:31 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.io;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

/**
 * IT'S PERFECT BECAUSE I SAID SO GODDAMMIT.
 * Coincidentally I spent ~3 hours setting up source and libraries for Jackson
 * just so I could write tabs instead of spaces. THAT'S RIGHT.
 */
public class PerfectPrinter extends DefaultPrettyPrinter {

	public PerfectPrinter() {
		super();
		_arrayIndenter = new PerfectIndenter();
		_objectIndenter = new PerfectIndenter();
	}
	
}
