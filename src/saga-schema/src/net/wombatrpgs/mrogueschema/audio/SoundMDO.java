/**
 *  SoundMDO.java
 *  Created on Mar 21, 2013 9:10:10 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.audio;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogueschema.audio.data.AudioMDO;
import net.wombatrpgs.mrogueschema.audio.data.RepeatType;

/**
 * Thing that translates into libgdx sound.
 */
@Path("audio/")
public class SoundMDO extends AudioMDO {
	
	@Desc("Repeats - usually false for SFX")
	@DefaultValue("PLAY_ONCE")
	public RepeatType repeats;

}
