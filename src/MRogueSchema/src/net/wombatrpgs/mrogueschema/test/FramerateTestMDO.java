/**
 *  FramerateTest.java
 *  Created on Feb 22, 2013 4:33:57 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.test;

import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mrogueschema.test.data.TestState;

/**
 * Toggle the FPS counter on the screen.
 */
@Path("test/")
public class FramerateTestMDO extends MainSchema {
	
	public TestState enabled;

}
