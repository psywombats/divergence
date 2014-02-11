/**
 *  PrintReporter.java
 *  Created on Feb 4, 2013 11:19:05 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import net.wombatrpgs.mgne.core.interfaces.Reporter;
import net.wombatrpgs.mgne.core.reporters.DebugReporter;

/**
 * A reporter that prints out to files if it has problems. Also writes stuff to
 * the console if debug mode is enabled.
 */
public class PrintReporter implements Reporter {
	
	protected DebugReporter debugger;
	protected PrintWriter errLog, normalLog;

	/**
	 * Creates a new reporter.
	 */
	public PrintReporter() {
		debugger = new DebugReporter();
		File errFile = new File("error.log");
		File normalFile= new File("info.log");
		// this, unfortunately, will never be closed
		try {
			errLog = new PrintWriter(errFile);
			normalLog = new PrintWriter(normalFile);
		} catch (FileNotFoundException e) {
			// can't really check for shit here haha
			e.printStackTrace();
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Reporter#inform(java.lang.String)
	 */
	@Override
	public void inform(String info) {
		debugger.inform(info);
		normalLog.println(info);
		normalLog.flush();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Reporter#warn(java.lang.String)
	 */
	@Override
	public void warn(String warning) {
		debugger.warn(warning);
		errLog.println(warning);
		errLog.flush();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Reporter#warn(java.lang.String, java.lang.Exception)
	 */
	@Override
	public void warn(String error, Exception e) {
		debugger.warn(error, e);
		errLog.println(error);
		errLog.flush();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Reporter#err(java.lang.String)
	 */
	@Override
	public void err(String error) {
		debugger.err(error);
		errLog.println(error);
		errLog.flush();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Reporter#err(java.lang.String, java.lang.Exception)
	 */
	@Override
	public void err(String error, Exception e) {
		errLog.println(error);
		errLog.flush();
		err(e);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Reporter#err(java.lang.Exception)
	 */
	@Override
	public void err(Exception e) {
		debugger.err(e);
		e.printStackTrace(errLog);
		errLog.flush();
	}

}
