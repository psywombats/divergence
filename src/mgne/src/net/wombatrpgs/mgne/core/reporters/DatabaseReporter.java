/**
 *  DatabaseReporter.java
 *  Created on Nov 23, 2014 6:45:28 PM for project mgne
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
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.badlogic.gdx.Gdx;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.VersionInfo;
import net.wombatrpgs.mgne.core.interfaces.Reporter;
import net.wombatrpgs.mgne.io.net.MySQLConnector;
import net.wombatrpgs.mgne.io.net.columns.ColumnEntry;
import net.wombatrpgs.mgne.io.net.columns.EntryDate;
import net.wombatrpgs.mgne.io.net.columns.EntryInt;
import net.wombatrpgs.mgne.io.net.columns.EntryString;

/**
 * A reporter that phones home to the WRPGs SQL database.
 */
public class DatabaseReporter implements Reporter {
	
	protected static final String TABLE_ERRORS = "mgn_errors";
	
	protected static final String FIELD_GAME = "game";
	protected static final String FIELD_VERSION = "version";
	protected static final String FIELD_BUILD = "build";
	protected static final String FIELD_USER = "user";
	protected static final String FIELD_ERROR = "error";
	protected static final String FIELD_TIME = "time";
	
	protected PrintWriter errLog, normalLog;
	protected MySQLConnector connector;
	protected boolean sqlOn;
	
	/**
	 * Creates a new database reporter. Does not initialize the database
	 * connection until an error actually comes in.
	 */
	public DatabaseReporter() {
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

				@Override public void write(byte[] buffer, int offset, int length) {
					super.write(buffer, offset, length);
					console.write(buffer, offset, length);
				}
				
				@Override public void write(int bite) {
					super.write(bite);
					console.write(bite);
				}
				
				@Override public void write(byte[] bytes) throws IOException{
					super.write(bytes);
					console.write(bytes);
				}
				
				@Override public void flush() {
					super.flush();
					console.flush();
				}
				
			});
			
			normalLog = new PrintWriter(normalFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Reporter#inform
	 * (java.lang.String)
	 */
	@Override
	public void inform(String info) {
		System.out.println(info);
		normalLog.println(info);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Reporter#warn(java.lang.String)
	 */
	@Override
	public void warn(String warning) {
		errLog.println(warning);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Reporter#warn
	 * (java.lang.String, java.lang.Exception)
	 */
	@Override
	public void warn(String error, Exception e) {
		StringWriter writer = new StringWriter();
		PrintWriter printer = new PrintWriter(writer);
		e.printStackTrace(printer);
		warn(error + "\n" + e);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Reporter#err(java.lang.String)
	 */
	@Override
	public void err(String error) {
		System.err.println(error);
		sendError(error);
		Gdx.app.exit();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Reporter#err
	 * (java.lang.String, java.lang.Exception)
	 */
	@Override
	public void err(String error, Exception e) {
		StringWriter writer = new StringWriter();
		PrintWriter printer = new PrintWriter(writer);
		e.printStackTrace(printer);
		err(error + "\n" + writer.toString());
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Reporter#err
	 * (java.lang.Exception)
	 */
	@Override
	public void err(Exception e) {
		err("Unhandled MGN exception", e);
	}
	
	/**
	 * Initializes the database connection if not already active.
	 */
	protected void prepareConnector() {
		sqlOn = true;
		if (connector == null) {
			connector = new MySQLConnector();
			try {
				connector.connect();
			} catch (SQLException e) {
				err(e);
			}
		}
	}
	
	/**
	 * Marks the connector as done for now.
	 */
	protected void doneConnector() {
		sqlOn = false;
	}
	
	/**
	 * Sends an error along to the database.
	 * @param	errorString		The text of the error
	 */
	protected void sendError(String errorString) {
		if (sqlOn) {
			// the error was generated in the sql handler, screw this
			return;
		}
		MGlobal.reporter.inform("Sending error information to db...");
		prepareConnector();
		
		VersionInfo info = MGlobal.getVersion();
		List<ColumnEntry<?>> entries = new ArrayList<ColumnEntry<?>>();
		entries.add(new EntryString(FIELD_GAME, info.gameName));
		entries.add(new EntryString(FIELD_VERSION, info.version));
		entries.add(new EntryInt(FIELD_BUILD, info.build));
		entries.add(new EntryString(FIELD_USER, info.getUserName()));
		entries.add(new EntryDate(FIELD_TIME));
		entries.add(new EntryString(FIELD_ERROR, errorString));
		connector.insert(TABLE_ERRORS, entries);
		
		doneConnector();
	}

}
