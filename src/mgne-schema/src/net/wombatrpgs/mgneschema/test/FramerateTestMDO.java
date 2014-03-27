/**
 *  FramerateTest.java
 *  Created on Feb 22, 2013 4:33:57 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.test;

import net.wombatrpgs.mgneschema.test.data.TestState;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Toggle the FPS counter on the screen.
 */
@Path("test/")
public class FramerateTestMDO extends MainSchema {
	
	private static final long serialVersionUID = 1L;
	public TestState enabled;

}
