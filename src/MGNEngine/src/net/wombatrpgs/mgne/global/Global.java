/**
 *  Global.java
 *  Created on Nov 4, 2012 5:49:52 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.global;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.wombatrpgs.rainfall.core.DataLoader;
import net.wombatrpgs.rainfall.core.Database;
import net.wombatrpgs.rainfall.core.DebugReporter;
import net.wombatrpgs.rainfall.core.DirectoryDataLoader;
import net.wombatrpgs.rainfall.core.Reporter;

/**
 * GLOBAL - THE GLOBAL CLASS. It's a big giant class that's just "there" and
 * holds references to all those other lesser global classes. Singleton pattern
 * is annoying so there are no singleton guards, but this class would handle
 * them if there were any. Actually there really should be guards shouldn't
 * they?
 */
public class Global {
	
	public static DataLoader dataLoader;
	
	/** The class used to load file resources */
	public static SimpleFileLoader fileLoader;
	
	/** Error-reporting dispatcher */
	public static Reporter reporter;
	
	/** Jackson object mapper for schema */
	public static ObjectMapper mapper;
	
	/** Storage container for data entries */
	public static Database data;
	
	/** True if global has been initialized yet */
	public static boolean initialized;
	
	/**
	 * Call in test suites that use global. This is the same thing as the old
	 * static initializer method.
	 */
	public static void setupGlobalForTesting() {
		dataLoader = new DirectoryDataLoader();
		fileLoader = new SimpleFileLoader();
		reporter = new DebugReporter();
		mapper = new ObjectMapper();
		data = new Database();
	}

}
