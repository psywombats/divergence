/**
 *  RemoteReporter.java
 *  Created on Nov 27, 2014 8:02:56 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core.reporters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.VersionInfo;
import net.wombatrpgs.mgne.core.interfaces.Reporter;
import net.wombatrpgs.mgne.io.net.PostConnector;
import net.wombatrpgs.mgne.io.net.columns.ColumnEntry;
import net.wombatrpgs.mgne.io.net.columns.EntryDate;
import net.wombatrpgs.mgne.io.net.columns.EntryInt;
import net.wombatrpgs.mgne.io.net.columns.EntryString;

import com.badlogic.gdx.Gdx;

/**
 * HTTP POST's to a PHP script that updates the error database.
 */
public class PostReporter implements Reporter {
	
	protected static final String POST_URL = "http://www.wombatrpgs.net/misc/mgne_errors.php";
	
	protected static final String FIELD_GAME = "game";
	protected static final String FIELD_VERSION = "version";
	protected static final String FIELD_BUILD = "build";
	protected static final String FIELD_USER = "user";
	protected static final String FIELD_ERROR = "error";
	protected static final String FIELD_TIME = "time";
	
	protected PrintWriter errLog, normalLog;
	protected FileOutputStream errStream;
	protected PostConnector connector;
	protected boolean httpOn;
	
	/**
	 * Creates a new database reporter. Does not initialize the database
	 * connection until an error actually comes in.
	 */
	public PostReporter() {
		File errFile = new File("error.log");
		File normalFile = new File("info.log");
		// this, unfortunately, will never be closed
		try {
			if (errFile.exists()) {
				errStream = new FileOutputStream(errFile, true);
				errLog = new PrintWriter(errStream);
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
			errStream.flush();
			
			// this will redirect all exceptions to the error log and console
			final PrintStream console = System.err;
			System.setErr(new PrintStream(errStream) {

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
		errLog.flush();
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
		warn(error + "\n" + writer);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Reporter#err(java.lang.String)
	 */
	@Override
	public void err(String error) {
		System.err.println(error);
		sendError(error);
		try {
			errStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		errLog.close();
		normalLog.close();
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
		err(error + "\n" + writer);
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
	protected void prepareHTTP() {
		httpOn = true;
		if (connector == null) {
			connector = new PostConnector(POST_URL);
		}
	}
	
	/**
	 * Marks the connector as done for now.
	 */
	protected void doneHTTP() {
		httpOn = false;
	}
	
	/**
	 * Sends an error along to the database.
	 * @param	errorString		The text of the error
	 */
	protected void sendError(String errorString) {
		if (httpOn) {
			// the error was generated in the http handler, screw this
			return;
		}
		MGlobal.reporter.inform("Sending error information to db...");
		prepareHTTP();
		
		VersionInfo info = MGlobal.getVersion();
		List<ColumnEntry<?>> entries = new ArrayList<ColumnEntry<?>>();
		entries.add(new EntryString(FIELD_GAME, info.gameName));
		entries.add(new EntryString(FIELD_VERSION, info.version));
		entries.add(new EntryInt(FIELD_BUILD, info.build));
		entries.add(new EntryString(FIELD_USER, info.getUserName()));
		entries.add(new EntryDate(FIELD_TIME));
		entries.add(new EntryString(FIELD_ERROR, errorString));
		
		try {
			String result = connector.post(entries);
			MGlobal.reporter.inform("Posted error to DB done, status: " + result);
		} catch (IOException e) {
			System.err.println("Error sending other error to DB, oh crap");
			e.printStackTrace();
		}
		
		doneHTTP();
	}
}
