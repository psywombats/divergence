/**
 *  AnimationMDO.java
 *  Created on Nov 10, 2012 6:08:24 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.graphics;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Spritestrip sort of thing.
 */
@Path("graphics/")
public class AnimationMDO extends MainSchema {
	
	@Desc("Image file")
	@FileLink("sprites")
	public String file;
	
	@Desc("Frame count")
	@DefaultValue("1")
	public Integer frameCount;
	
	@Desc("Frame width")
	@DefaultValue("32")
	public Integer frameWidth;
	
	@Desc("Frame height")
	@DefaultValue("64")
	public Integer frameHeight;
	
	@Desc("Offset X (x-coord in pixels of where the anim starts within the sheet)")
	@DefaultValue("0")
	public Integer offX;
	
	@Desc("Offset Y (y-coord in pixels of where the anim starts within the sheet)")
	@DefaultValue("0")
	public Integer offY;
	
	@Desc("Animation speed (in frames per second)")
	@DefaultValue("4")
	public Integer animSpeed;
	

}
