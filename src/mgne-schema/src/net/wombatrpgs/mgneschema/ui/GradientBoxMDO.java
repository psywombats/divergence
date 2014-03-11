/**
 *  GradientBoxMDO.java
 *  Created on Mar 10, 2014 7:59:37 PM for project mgne-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.ui;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Gradient mixed with Perlin noise. Cool looks.
 */
@Path("ui/")
public class GradientBoxMDO extends MainSchema {
	
	@Desc("Corner color: red component (0-1)")
	public Float r1;
	@Desc("Corner color: green component (0-1)")
	public Float g1;
	@Desc("Corner color: blue component (0-1)")
	public Float b1;
	
	@Desc("Middle color: red component (0-1)")
	public Float r2;
	@Desc("Middle color: green component (0-1)")
	public Float g2;
	@Desc("Middle color: blue component (0-1)")
	public Float b2;

}
