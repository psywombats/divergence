/**
 *  DynamicBoxMDO.java
 *  Created on Jun 13, 2013 9:06:26 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.graphics.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * A rectangle, really. It's called a dynamic box because each frame in an
 * animation gets a different one. You seriously shouldn't be entering these
 * by hand, it's a pain. The reason it's here and not uh anywhere else I guess
 * is because it can be put into an inline schema array.
 */
public class DynamicBoxMDO extends HeadlessSchema {
	
	@Desc("X-coord of upper left corner of hitbox")
	public Integer x1;
	
	@Desc("Y-coord of upper left corner of hitbox")
	public Integer y1;
	
	@Desc("X-coord of lower right corner of hitbox")
	public Integer x2;
	
	@Desc("Y-coord of lower right corner of hitbox")
	public Integer y2;

}
