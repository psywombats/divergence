/**
 *  Database.java
 *  Created on Nov 8, 2012 3:18:08 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * I'm not really sure if this should be an interface, but the way I see it,
 * this should just be the *best* database because all databases should have the
 * same use cases and the same underlying good methods. But yeah, the idea is
 * that you ask this class to do get all the schema of type x or named x or
 * whatever the hell.
 */
public class Database {
	
	/** Our library by class. Supply a class, get a list of all extending schema. */
	protected Map<Class<? extends MainSchema>, List<MainSchema>> classShelf;
	/** Our library by name. Supply a key, get the schema with that key */
	protected Map<String, MainSchema> keyShelf;
	
	/**
	 * Called whenever a database is created. Set up our giant library shelves!!
	 */
	public Database() {
		classShelf = new HashMap<Class<? extends MainSchema>, List<MainSchema>>();
		keyShelf = new HashMap<String, MainSchema>();
	}
	
	/**
	 * Adds all the supplied schema to the database.
	 * @param 	entries		The list of schema to add
	 */
	public void addData(List<? extends MainSchema> entries) {
		for (MainSchema s : entries) {
			Global.reporter.inform("Added entry to database: " + s.key);
			List<MainSchema> currentList = classShelf.get(s.getClass());
			if (currentList == null) {
				currentList = new ArrayList<MainSchema>();
			}
			currentList.add(s);
			keyShelf.put(s.key, s);
		}
	}
	
	/**
	 * Fetches the entry with the supplied key from the database.
	 * @param 	key		The key to look up
	 * @return			The entry with that key
	 */
	public MainSchema getEntryByKey(String key) {
		return keyShelf.get(key);
	}
	
	/**
	 * Fetches all entries that are of the specified schema class.
	 * @param 	clazz	The class that the entries must be
	 * @return			All entries of that class
	 */
	public List<MainSchema> getEntriesByClass(Class<? extends MainSchema> clazz) {
		return classShelf.get(clazz);
	}
	
	/**
	 * Fetches the single object of that class that exists in the database. 
	 * @param <T>		The type of entry to fetch
	 * @param clazz		The schema of the entry to fetch
	 * @return			The singleton entry
	 */
	@SuppressWarnings("unchecked")
	public <T> T getSingleton(Class<T> clazz) {
		List<MainSchema> currentList = classShelf.get(clazz);
		if (currentList == null) {
			Global.reporter.err("No singleton found for " + clazz);
			return null;
		}
		if (currentList.size() != 1) {
			Global.reporter.warn("Expected exactly one instance of " + clazz +
					" in database, found " + currentList.size());
		}
		MainSchema result = currentList.get(0);
		try {
			return (T) result;
		} catch (ClassCastException e) {
			Global.reporter.err("Singleton wasn't of expected type " + clazz);
			return null;
		}
	}

}
