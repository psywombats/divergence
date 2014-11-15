/**
 *  Global.java
 *  Created on Aug 8, 2012 2:06:27 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse;

import java.util.ArrayList;
import java.util.HashMap;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Schema;
import net.wombatrpgs.mgnse.io.PerfectPrinter;
import net.wombatrpgs.mgnse.tree.SchemaNode;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * All global tools needed for the application.
 */
public class Global {
	
	public enum Debug { DEBUG, BETA, DEPLOY }
	public enum Level { DEBUG, WARNING, ERROR }
	
	public static final Debug DEBUG = Debug.DEBUG;
	private static Global instance;
	
	private HashMap<Class<? extends MainSchema>, SchemaNode> map;
	private ObjectMapper mapper;
	private PrettyPrinter printer;
	
	/**
	 * Creates and initializes a new global... but globals are singletons!
	 */
	private Global() {
		mapper = new ObjectMapper();
		printer = new PerfectPrinter();
	}
	
	public static boolean debugMode() {
		return DEBUG == Debug.DEBUG;
	}
	
	/**
	 * Singleton Global.
	 * @return The global instance
	 */
	public static Global instance() {
		if (instance == null) {
			instance = new Global();
		}
		return instance;
	}
	
	/**
	 * Gets the node associated with a class of schema.
	 * @param schema The schema to fetch for
	 * @return The node for that schema
	 */
	public SchemaNode getNode(Class<? extends MainSchema> schema) {
		for (Class<? extends Schema> subClass : map.keySet()) {
			if (subClass.getName().equals(schema.getName())) {
				return map.get(subClass);
			}
		}
		return null;
	}
	
	/** @param map The new map to set */
	public void setSchemaMap(HashMap<Class<? extends MainSchema>, SchemaNode> map) { this.map = map; }
	
	/**
	 * Returns all nodes that correspond to schema that subclass the supplied
	 * schema class. Useful for a semi-inheritance thing.
	 * @param superClass The big class to get inheritors of
	 * @return A list of all nodes with inheriting classes (maybe empty)
	 */
	public ArrayList<SchemaNode> getImplementers(Class<? extends Schema> superClass) {
		ArrayList<SchemaNode> implementers = new ArrayList<SchemaNode>();
		for (Class<? extends Schema> subClass : map.keySet()) {
			if (superClass.isAssignableFrom(subClass)) {
				implementers.add(map.get(subClass));
			}
		}
		return implementers;
	}
	
	/**
	 * Handles some error in a user-friendly way.
	 * @param msg The user-facing error message
	 * @param e The exception for debug info
	 */
	public void err(String msg, Exception e) {
		if (DEBUG != Debug.DEPLOY) {
			e.printStackTrace();
		}
		debug(msg, Level.ERROR);
	}
	
	/**
	 * Prints to the err at the warning level.
	 * @param msg The message to print
	 */
	public void warn(String msg) {
		debug(msg, Level.WARNING);
		
	}
	
	/** @return The global Jackson mapper */
	public ObjectMapper mapper() {
		return mapper;
	}
	
	/** @return The global Jackson writer, with pretty print! */
	public ObjectWriter writer() {
		return mapper.writer(printer);
	}
	
	/**
	 * Debug message printout. Assumes DEBUG level
	 * @param msg The message to display
	 */
	public void debug(String msg) {
		debug(msg, Level.DEBUG);
	}
	
	/**
	 * Debug message printout. DEBUG prints to System.out, WARNING prints to
	 * err, ERROR prints to err and crashes.
	 * @param msg
	 * @param level
	 */
	public void debug(String msg, Level level) {
		switch(level) {
		case DEBUG:
			if (!debugMode()) break;
			System.out.println("INFO: " + msg);
			break;
		case WARNING:
			if (DEBUG == Debug.DEPLOY) break;
			System.err.println("WARNING: " + msg);
			break;
		case ERROR:
			System.err.println("ERROR: " + msg);
			System.exit(1);
			break;
		}
	}

}
