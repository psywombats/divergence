/**
 *  SoundManagerEntryMDO.java
 *  Created on Sep 14, 2014 4:44:06 PM for project mgne-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.audio.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * Entry in the sound manager?
 */
public class SoundManagerEntryMDO extends HeadlessSchema {
	
	@Desc("Reference key - can use this to refer to sound in-game")
	public String key;
	
	@Desc("File - this sfx will play")
	@FileLink("audio")
	public String file;

}
