/**
 *  Database.java
 *  Created on Nov 8, 2012 3:18:08 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wombatrpgs.mgns.core.MainSchema;

/**
 * I'm not really sure if this should be an interface, but the way I see it,
 * this should just be the *best* database because all databases should have the
 * same use cases and the same underlying good methods. But yeah, the idea is
 * that you ask this class to do get all the schema of type x or named x or
 * whatever the hell.
 */
public class Database {
	
//	/** Our library by class. Supply a class, get a list of all extending schema. */
//	protected Map<Class<Schema>, List<Schema>> classShelf;
	/** Our library by name. Supply a key, get the schema with that key */
	protected Map<String, MainSchema> keyShelf;
	
	/**
	 * Called whenever a database is created. Set up our giant library shelves!!
	 */
	public Database() {
//		classShelf = new HashMap<Class<Schema>, List<Schema>>();
		keyShelf = new HashMap<String, MainSchema>();
	}
	
	/**
	 * Adds all the supplied schema to the database.
	 * @param 	entries		The list of schema to add
	 */
	public void addData(List<? extends MainSchema> entries) {
		for (MainSchema s : entries) {
			// TODO: finish the classShelf
//			List<? extends Schema> currentList = classShelf.get(s.getClass());
//			if (currentList == null) {
//				currentList = new List<Schema>
//			}
			keyShelf.put(s.key, s);
		}
	}
	
	/**
	 * Fetches the entry with the supplied key from the library.
	 * @param 	key		The key to look up
	 * @return			The schema with that key
	 */
	public MainSchema getEntryByKey(String key) {
		return keyShelf.get(key);
	}

}
