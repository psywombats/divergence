/**
 *  NumberSetMDO.java
 *  Created on Aug 24, 2013 12:40:27 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.ui;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Data for a spritesheet that's actually 0123456789. Monospaced.
 */
@Path("ui/")
public class NumberSetMDO extends MainSchema {
	
	@Desc("Graphics file")
	@FileLink("ui")
	public String file;
	
	@Desc("Width - Width of a number, in px")
	public Integer width;
	
	@Desc("Height - Height of a number, in px")
	public Integer height;
	
	@Desc("Kerning - Space between two numbers when printed, in px")
	public Integer kerning;

}
