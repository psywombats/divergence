/**
 *  EmuMusicEntryMDO.java
 *  Created on Oct 9, 2014 7:38:35 PM for project mgne-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.audio.data;

import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;

/**
 * Entry in the emu music manager.
 */
public class EmuMusicEntryMDO extends HeadlessSchema {
	
	@Desc("Reference key - can be used to refer to this key in-game")
	public String refKey;
	
	@Desc("File - .gbs containing the relevant music")
	@FileLink("audio")
	public String gbsPath;
	
	@Desc("Track number - index of this track in the gbs file, from 0")
	public Integer track;

}
