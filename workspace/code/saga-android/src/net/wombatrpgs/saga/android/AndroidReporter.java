/**
 *  AndroidReporter.java
 *  Created on Jan 2, 2014 10:54:14 PM for project saga-android
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.android;

import android.util.Log;
import net.wombatrpgs.mrogue.core.reporters.DebugReporter;

/**
 * Because the desktop version will crash Android.
 */
public class AndroidReporter extends DebugReporter {
	
	protected static final String TAG = "SAGA";

	/**
	 * @see net.wombatrpgs.mrogue.core.reporters.DebugReporter#inform(java.lang.String)
	 */
	@Override
	public void inform(String info) {
		Log.d(TAG, info);
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.reporters.DebugReporter#warn(java.lang.String)
	 */
	@Override
	public void warn(String warning) {
		Log.w(TAG, warning);
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.reporters.DebugReporter#err(java.lang.String)
	 */
	@Override
	public void err(String error) {
		Log.e(TAG, error);
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.reporters.DebugReporter#err(java.lang.String, java.lang.Exception)
	 */
	@Override
	public void err(String error, Exception e) {
		Log.e(TAG, error, e);
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.reporters.DebugReporter#err(java.lang.Exception)
	 */
	@Override
	public void err(Exception e) {
		Log.e(TAG, "(no error desc)", e);
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.reporters.DebugReporter#warn(java.lang.String, java.lang.Exception)
	 */
	@Override
	public void warn(String error, Exception e) {
		Log.w(TAG, error, e);
	}

}
