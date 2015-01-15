/**
 *  SaveDescriptor.java
 *  Created on Jul 21, 2014 5:46:55 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * An actual description of the save file that gets stored in the index. It can
 * be queued/loaded to make sure the sprites are in memory, but they are created
 * and read in from the memory index.
 */
public class SaveDescriptor {
	
	public String fileName;
	public Date lastSaved;
	public int build;
	
	/**
	 * Generates a new save descriptor for the party currently in memory.
	 * @return					A descriptor for the in-memory party
	 */
	public static SaveDescriptor generateDescriptor() {
		SaveDescriptor save = new SaveDescriptor();
		save.lastSaved = new Date();
		save.build = BConstants.SAVE_BUILD;
		save.fileName += (save.getDateString());
		save.fileName += ("_" + System.currentTimeMillis());
		return save;
	}
	
	/**
	 * Converts the save date into human-readable format.
	 * @return					The mm/yy/dd whatever data conversion
	 */
	@JsonIgnore
	public String getDateString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(lastSaved);
	}
	
	/**
	 * Checks if this save is outdated. Because the serialization system sucks
	 * and there's no migration system in place, loading this save file would
	 * probably crash.
	 * @return					True for outdated, false for current build
	 */
	@JsonIgnore
	public boolean isOutdated() {
		return build != BConstants.SAVE_BUILD;
	}

}
