/**
 *  Global.java
 *  Created on Nov 4, 2012 5:49:52 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.global;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.wombatrpgs.mgne.data.DataLoader;
import net.wombatrpgs.mgne.data.Database;
import net.wombatrpgs.mgne.data.DirectoryDataLoader;

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
	public static FileLoader fileLoader;
	
	/** Error-reporting dispatcher */
	public static Reporter reporter;
	
	/** Jackson object mapper for schema */
	public static ObjectMapper mapper;
	
	/** Storage container for data entries */
	public static Database data;
	
	/* Ho shit a static initializer... let's make some singletons! */
	// TODO: dependency list, some INIT CODE
	static {
		dataLoader = new DirectoryDataLoader();
		fileLoader = new FileLoader();
		reporter = new DebugReporter();
		mapper = new ObjectMapper();
		data = new Database();
	}

}
