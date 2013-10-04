/**
 *  Database.java
 *  Created on Nov 8, 2012 3:18:08 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import net.wombatrpgs.mgns.core.MainSchema;

/**
 * I'm not really sure if this should be an interface, but the way I see it,
 * this should just be the *best* database because all databases should have the
 * same use cases and the same underlying good methods. But yeah, the idea is
 * that you ask this class to do get all the schema of type x or named x or
 * whatever the hell. As of 2012-02-04, is responsible for its own loading.
 */
public class Database {
	
	/** Our library by class. Supply a class, get a list of all extending schema. */
	protected Map<Class<? extends MainSchema>, List<MainSchema>> classShelf;
	/** Our library by name. Supply a key, get the schema with that key */
	protected Map<String, MainSchema> keyShelf;
	/** If a file appears here, it won't be loaded */
	protected List<String> loadedDirs;
	/** What we're going to use to map objects */
	protected ObjectMapper mapper;
	
	/**
	 * Called whenever a database is created. Set up our giant library shelves!!
	 */
	public Database() {
		classShelf = new HashMap<Class<? extends MainSchema>, List<MainSchema>>();
		keyShelf = new HashMap<String, MainSchema>();
		loadedDirs = new ArrayList<String>();
		mapper = new ObjectMapper();
	}
	
	/**
	 * Adds all the supplied schema to the database.
	 * @param 	entries			The list of schema to add
	 */
	public void addData(List<? extends MainSchema> entries) {
		for (MainSchema s : entries) {
			//Global.reporter.inform("Added entry to database: " + s.key);
			addData(s);
		}
	}
	
	/**
	 * Adds a singular entry to the database.
	 * @param 	entry			The entry to add
	 */
	public void addData(MainSchema entry) {
		List<MainSchema> currentList = classShelf.get(entry.getClass());
		if (currentList == null) {
			currentList = new ArrayList<MainSchema>();
			classShelf.put(entry.getClass(), currentList);
		}
		currentList.add(entry);
		keyShelf.put(entry.key, entry);
	}
	
	/**
	 * Fetches the entry with the supplied key from the database, then casts it
	 * to the appropriate type for you. How convenient!
	 * @param 	<T>		The type of schema expected
	 * @param 	key		The key of the schema
	 * @param 	clazz	The type of schema expected
	 * @return			The schema with that key of that type
	 */
	@SuppressWarnings("unchecked")
	public <T extends MainSchema> T getEntryFor(String key, Class<T> clazz) {
		MainSchema candidate = getEntryByKey(key);
		T result = null;
		try {
			result = (T) candidate;
		} catch (ClassCastException e) {
			MGlobal.reporter.err("Wrong class for " + key, e);
		}
		return result;
	}
	
	/**
	 * Fetches all entries that are of the specified schema class.
	 * @param 	clazz	The class that the entries must be
	 * @return			All entries of that class
	 */
	@SuppressWarnings("unchecked")
	public <T extends MainSchema> List<T> getEntriesByClass(Class<T> clazz) {
		List<T> result = (List<T>) classShelf.get(clazz);
		if (result == null) return new ArrayList<T>();
		else return result;
	}
	
	/**
	 * Queues up all entries of the listed classes in the asset manager.
	 * @param	manager			The manager to load classes with
	 * @param 	types			All the classes to load
	 */
	public void queueData(AssetManager manager, List<Class<? extends MainSchema>> types) {
		for (Class<? extends MainSchema> schema : types) {
			String name = schema.getCanonicalName();
			String path = Constants.DATA_DIR;
			while (name.indexOf('.') != -1) {
				path += name.substring(0, name.indexOf('.'));
				path += File.separator;
				name = name.substring(name.indexOf('.') + 1);
			}
			path += name;
			queueFilesInDir(manager, Gdx.files.local(path));
		}
	}
	
	/**
	 * Turns a chunk of preloaded data into meaningful code.
	 * @param 	data			The data string to read
	 * @param	clazz			The class to format it as
	 */
	public void parseString(String data, Class<? extends MainSchema> clazz) {
		MainSchema object = null;
		try {
			object = mapper.readValue(data, clazz);
			addData(object);
		} catch (JsonParseException e) {
			MGlobal.reporter.err("Malformatted data file " + data, e);
		} catch (UnrecognizedPropertyException e) {
			MGlobal.reporter.warn("Unknown field for class " + clazz.getName() +
					": " + e.getUnrecognizedPropertyName());
		} catch (JsonMappingException e) {
			MGlobal.reporter.err("Data file doesn't match schema " + data, e);
		} catch (IOException e) {
			MGlobal.reporter.err("Couldn't read data file " + data, e);
		}
	}
	
	/**
	 * Recursively queues all files in a specified directory for loading as
	 * database entries. Does not duplicate entries if things have already been
	 * added.
	 * @param 	manager			The manager to load with
	 * @param 	dir				The file to load from
	 */
	public void queueFilesInDir(AssetManager manager, FileHandle dir) {
		if (dir.isDirectory()) {
			for (String entry : loadedDirs) {
				if (dir.path().equals(entry)) return;
			}
			loadedDirs.add(dir.path());
			for (FileHandle child : dir.list()) {
				queueFilesInDir(manager, child);
			}
		} else {
			manager.load(dir.path(), DataEntry.class);
		}
	}
	
	/**
	 * Fetches the entry with the supplied key from the database.
	 * @param 	key		The key to look up
	 * @return			The entry with that key
	 */
	protected MainSchema getEntryByKey(String key) {
		MainSchema result = keyShelf.get(key);
		if (result == null) {
			MGlobal.reporter.err("Couldn't find an entry for key: " + key);
		}
		return result;
	}

}
