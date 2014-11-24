/**
 *  PrintReporter.java
 *  Created on Feb 4, 2013 11:19:05 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core.reporters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.wombatrpgs.mgne.core.interfaces.Reporter;

/**
 * A reporter that prints out to files if it has problems. Also writes stuff to
 * the console if debug mode is enabled.
 */
public class FileReporter implements Reporter {
	
	protected DummyReporter debugger;
	protected PrintWriter errLog, normalLog;

	/**
	 * Creates a new reporter.
	 */
	public FileReporter() {
		debugger = new DummyReporter();
		File errFile = new File("error.log");
		File normalFile= new File("info.log");
		// this, unfortunately, will never be closed
		try {
			if (errFile.exists()) {
				errLog = new PrintWriter(new FileWriter(errFile, true));
				errLog.println();
			} else {
				errLog = new PrintWriter(errFile);
			}
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			errLog.println("########################################");
			errLog.println(dateFormat.format(cal.getTime()));
			errLog.println("########################################");
			errLog.flush();
			
			// this will redirect all exceptions to the error log and console
			final PrintStream console = System.err;
			System.setErr(new PrintStream(errFile) {
				@Override public void write(byte[] arg0, int arg1, int arg2) {
					super.write(arg0, arg1, arg2);
					console.write(arg0, arg1, arg2);
				}
				@Override public void write(int arg0) {
					super.write(arg0);
					console.write(arg0);
				}
				@Override public void flush() {
					super.flush();
					console.flush();
				}
			});
			
			normalLog = new PrintWriter(normalFile);
		} catch (FileNotFoundException e) {
			// can't really check for shit here haha
			e.printStackTrace();
		} catch (IOException e) {
			// yeah screw it
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
