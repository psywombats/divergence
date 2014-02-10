/**
 *  PerfectIndenter.java
 *  Created on Aug 8, 2012 5:22:49 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.io;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Indenter;

/**
 * An indenter for JSON writing.
 */
public class PerfectIndenter implements Indenter {
	
	final static String SYSTEM_LINE_SEPARATOR;
	static {
		String lf = null;
		try {
			lf = System.getProperty("line.separator");
		} catch (Throwable t) { } // access exception?
		SYSTEM_LINE_SEPARATOR = (lf == null) ? "\n" : lf;
	}

	@Override
	public boolean isInline() { return false; }

	@Override
	public void writeIndentation(JsonGenerator jg, int level)
		throws IOException, JsonGenerationException
	{
		jg.writeRaw(SYSTEM_LINE_SEPARATOR);
		for (int i = 0; i < level; i++) {
			jg.writeRaw("\t", 0, 1);
		}
	}

}
