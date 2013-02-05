/**
 *  DataLoader.java
 *  Created on Oct 25, 2012 8:36:45 PM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.mgns.core.MainSchema;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * An superclass for all classes that load and handle databases. This is split
 * into abstract because it's possible to read in files from both the normal
 * res folder like a normal person and from compressed game data somewhere. It's
 * encryption, so you know it's special!
 */
public abstract class DataLoader {
	
	protected LoadingBarCounter preloaderBar;
	protected LoadingBarCounter parserBar;
	protected List<PreloadedData> preloaded;
	protected List<MainSchema> schema;
	
	/**
	 * Sets up a few initial loading bars for a new data loader.
	 */
	public DataLoader() {
		preloaderBar = new LoadingBarCounter();
		parserBar = new LoadingBarCounter();
		preloaded = new ArrayList<PreloadedData>();
		schema = new ArrayList<MainSchema>();
	}
	
	/**
	 * Loads all the data files from the directory specified by the string. Note
	 * that in the case of compressed files, that dataDirectory could actually
	 * be one giant blob. In theory dataDirectory is a directory path and name
	 * but I guess subclasses could do weird things. Result is an arraylist of
	 * all schema that were loaded. They're unsorted, just by the way, but you
	 * can get their class to do neat things with them. Probably the best thing
	 * to do is to drop them all into their own little bag by class.
	 * @param dataDirectory		The file to load in data files from
	 * @return					A list of all schema that were there~
	 */
	public List<MainSchema> loadData(String dataDirectory) {
		setDirectory(dataDirectory);	
		for (final PreloadedData preload : preloaded) {
			parserBar.addTask(new LoadingBarRunnable() {
				@Override
				public void run() { parsePreloaded(preload); }
				@Override
				public int getSize() { return 1; }
			});
		}
		parserBar.run();
		return schema;
	}
	
	/**
	 * Loads the data then dumps it.
	 * @param dataDirectory
	 */
	public void addToDatabase(String dataDirectory) {
		Global.data.addData(loadData(dataDirectory));
	}
	
	/**
	 * Gets the counter used by the preloading scheme for use in loading bars.
	 * @return	The preloader's update counter
	 */
	public LoadingBarCounter getPreloaderCounter() {
		return preloaderBar;
	}
	
	/**
	 * Gets the counter used by the actual mapper for turning shit into usable
	 * schema. For use in loading bars.
	 * @return	The parser's update counter
	 */
	public LoadingBarCounter getParserCounter() {
		return parserBar;
	}
	
	/**
	 * A meta-constructor called before loading data. Think of it as a chance
	 * to zero everything out. File accessing should be done here and the
	 * preloaded loading bar counter should be incremented!
	 */
	protected abstract void setDirectory(String dataDirectory);
	
	/**
	 * Turns a chunk of preloaded data into meaningful code.
	 * @param 	preload		The preloaded data to pase
	 */
	protected void parsePreloaded(PreloadedData preload) {
		MainSchema object = null;
		try {
			object = Global.mapper.readValue(preload.data, preload.clazz);
			schema.add(object);
		} catch (JsonParseException e) {
			Global.reporter.err("Malformatted data file " + preload, e);
		} catch (JsonMappingException e) {
			Global.reporter.err("Data file doesn't match schema " + preload, e);
		} catch (IOException e) {
			Global.reporter.err("Couldn't read data file " + preload, e);
		}
	}

	/**
	 * Returns all files in the root directory, recursively. Does not include
	 * directories. Static helper.
	 * @param root 		The root directory to get files from
	 * @return 			An ArrayList of all those files
	 */
	protected static List<File> recursivelyGetFiles(File root) {
		List<File> files = new ArrayList<File>();
		for (File f : root.listFiles()) {
			if (f.isDirectory()) {
				files.addAll(recursivelyGetFiles(f));
			} else {
				files.add(f);
			}
		}
		return files;
	}

}
