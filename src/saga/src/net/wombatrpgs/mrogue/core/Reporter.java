/**
 *  Reporter.java
 *  Created on Nov 4, 2012 6:03:19 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.core;

/**
 * A thing for reporting errors/exceptions. It should be called ErrorReporter
 * or something but seeing as this class is going to get used all the damn time
 * I figured it should have a fun name. This is an interface because potentially
 * different distributions should handle their errors differently.
 */
public interface Reporter {

	/**
	 * Reports some debug information. Where this goes is ~~unknown~~ but the
	 * idea is that it shouldn't be done unless some special debug mode is set.
	 * @param info 		The info string to dump
	 */
	public void inform(String info);
	
	/**
	 * Another debug-ish function. Where it goes is unknown but it probably
	 * shouldn't happen in a deploy environment.
	 * @param warning	The warning string to dump
	 */
	public void warn(String warning);
	
	/**
	 * Warn clone if an error should be handled.
	 * @param 	error		Descripting of the problem
	 * @param 	e			The exception generated
	 */
	public void warn(String error, Exception e);
	
	/**
	 * This is the function to call when shit hits the fan. Recover if possible,
	 * but log anything that's probably fatal here.
	 * @param error		The error that occurred
	 */
	public void err(String error);
	
	/**
	 * An exception happened, and now what are you going to do? Call this, of
	 * course! It should probably be related to the other err function, like
	 * print a stack trace in addition or something.
	 * @param error		The error that occured (descriptive message)
	 * @param e			The exception that threw up
	 */
	public void err(String error, Exception e);
	
	/**
	 * When you're too lazy to write an error message.
	 * @param e			The exception that threw up
	 */
	public void err(Exception e);
}
