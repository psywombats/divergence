/**
 *  AnimationMDO.java
 *  Created on Nov 10, 2012 6:08:24 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.graphics;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mrogueschema.graphics.data.AnimationType;
import net.wombatrpgs.mrogueschema.graphics.data.DynamicBoxMDO;

/**
 * Spritestrip sort of thing.
 */
@Path("graphics/")
public class AnimationMDO extends DirMDO {
	
	@Desc("Image file - actual spritesheet, should be png")
	@FileLink("sprites")
	public String file;
	
	@Desc("Animation type")
	@DefaultValue("REPEAT")
	public AnimationType mode;
	
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
	public Integer animSpeed;
	
	@Desc("Hitbox upper left X - defaults to 0, only used if event has a hitbox")
	@Nullable
	public Integer hit1x;
	
	@Desc("Hitbox upper left Y - defaults to 0, only used if event has a hitbox")
	@Nullable
	public Integer hit1y;
	
	@Desc("Hitbox lower right X - defaults to frame width, only used if event has a hitbox")
	@Nullable
	public Integer hit2x;
	
	@Desc("Hitbox lower right Y - defaults to frame height, only used if event has a hitbox")
	@Nullable
	public Integer hit2y;
	
	@Desc("Optionally, attack boxes for each frame of the animation. One per frame. These should be " +
			"generated with a wizard, probably")
	@InlineSchema(DynamicBoxMDO.class)
	public DynamicBoxMDO[] attackBoxes;
	
	
}
