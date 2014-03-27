/**
 *  CursorMDO.java
 *  Created on Feb 14, 2014 2:36:21 AM for project tactics-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tacticsschema.ui;

import net.wombatrpgs.mgneschema.graphics.DirMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * The tactics cursor?
 */
@Path("ui/")
public class CursorMDO extends MainSchema {
	
	@Desc("Appearance")
	@SchemaLink(DirMDO.class)
	public String appearance;
	
	@Desc("Move speed - in px/s")
	public Integer speed;

}
