/**
 *  Test2MDO.java
 *  Created on Aug 11, 2012 11:44:50 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.*;

/**
 * Test object
 */
@Path("junk/")
public class Test3MDO extends MainSchema {
	
	@DisplayName("Test String")
	@Desc("Description of the test string.")
//	@DefaultValue("hahahaha default")
//	@Nullable(true)
	public String test;
	
	@DisplayName("Test Integer")
	@Desc("Description of the test number.")
	public Integer test2;

}
