/**
 *  AudioSchema.java
 *  Created on Mar 21, 2013 8:28:50 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.audio.data;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;

/**
 * Holds an audio file that can be interpreted by LWJGL. This can be both a
 * sound effect and a background track. Hidden.
 */
@ExcludeFromTree
public class AudioMDO extends MainSchema {
	
	@Desc("File - actual data file, mp3, ogg, wav all supported")
	@FileLink("audio")
	public String file;
	
	@Desc("Volume - 1 to 100")
	@DefaultValue("100")
	public Integer volume;

}
