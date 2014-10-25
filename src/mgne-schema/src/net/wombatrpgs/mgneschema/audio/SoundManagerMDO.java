/**
 *  SoundManagerMDO.java
 *  Created on Sep 14, 2014 4:42:28 PM for project mgne-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.audio;

import net.wombatrpgs.mgneschema.audio.data.EmuMusicEntryMDO;
import net.wombatrpgs.mgneschema.audio.data.LoadedMusicEntryMDO;
import net.wombatrpgs.mgneschema.audio.data.SoundEffectEntryMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Manages a shortcut index of sounds.
 */
@Path("audio/")
public class SoundManagerMDO extends MainSchema {
	
	@Desc("Sound effect entries")
	@InlineSchema(SoundEffectEntryMDO.class)
	public SoundEffectEntryMDO[] soundEffectEntries;
	
	@Desc("Emu track entries")
	@InlineSchema(EmuMusicEntryMDO.class)
	public EmuMusicEntryMDO[] emuMusicEntries;
	
	@Desc("Loaded music entries")
	@InlineSchema(LoadedMusicEntryMDO.class)
	public LoadedMusicEntryMDO[] loadedMusicEntries;

}
