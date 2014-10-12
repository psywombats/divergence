/**
 *  SoundManagerMDO.java
 *  Created on Sep 14, 2014 4:42:28 PM for project mgne-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.audio;

import net.wombatrpgs.mgneschema.audio.data.EmuMusicEntryMDO;
import net.wombatrpgs.mgneschema.audio.data.SoundManagerEntryMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Manages a shortcut index of sounds.
 */
@Path("audio/")
public class SoundManagerMDO extends MainSchema {
	
	@Desc("Sound manager entries")
	@InlineSchema(SoundManagerEntryMDO.class)
	public SoundManagerEntryMDO[] entries;
	
	@Desc("Emu track entries")
	@InlineSchema(EmuMusicEntryMDO.class)
	public EmuMusicEntryMDO[] musicEntries;

}
