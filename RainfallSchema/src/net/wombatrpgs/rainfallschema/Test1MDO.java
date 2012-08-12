/**
 *  Test1MDO.java
 *  Created on Aug 4, 2012 9:38:51 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema;

import net.wombatrpgs.mgns.core.Schema;
import net.wombatrpgs.mgns.core.Annotations.*;

/**
 * Just testin'
 */
@Path("junk/child/")
public class Test1MDO extends Schema {
	
	@DisplayName("Herp.")
	@Desc("descript text goes here.")
	@DefaultValue("hahahaha default")
	@Nullable(true)
	public String herpaderp;

}
