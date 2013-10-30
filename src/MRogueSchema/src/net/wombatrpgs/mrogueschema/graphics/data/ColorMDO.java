/**
 *  ColorMDO.java
 *  Created on Oct 29, 2013 5:42:20 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.graphics.data;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * Color entry.
 */ 
public class ColorMDO extends HeadlessSchema {
	
	@Desc("Red - 0-1")
	@DefaultValue("1")
	public Float r;
	
	@Desc("Green - 0-1")
	@DefaultValue("1")
	public Float b;
	
	@Desc("Blue - 0-1")
	@DefaultValue("1")
	public Float g;
	
	@Desc("Alpha - 0-1 (translucent-opaque)")
	@DefaultValue("1")
	public Float a;

}
