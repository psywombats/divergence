/**
 *  Global.java
 *  Created on Nov 4, 2012 5:49:52 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.global;

/**
 * GLOBAL - THE GLOBAL CLASS. It's a big giant class that's just "there" and
 * holds references to all those other lesser global classes. Singleton pattern
 * is annoying so there are no singleton guards, but this class would handle
 * them if there were any. Actually there really should be guards shouldn't
 * they?
 */
public class Global {
	
	public static DataLoader dataLoader;
	public static FileLoader fileLoader;
	public static Reporter reporter;
	
	/* Ho shit a static initializer... let's make some singletons! */
	static {
		dataLoader = new DataLoader();
		fileLoader = new FileLoader();
		reporter = new DebugReporter();
	}

}
