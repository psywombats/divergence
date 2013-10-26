/**
 *  PromptMDO.java
 *  Created on Oct 25, 2013 10:31:38 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.ui;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.FileLink;

/**
 * Binary choice prompt.
 */
@Path("ui/")
public class PromptMDO extends MainSchema {
	
	@Desc("Backer graphic")
	@FileLink("ui")
	public String backer;
	
	@Desc("Cursor graphic")
	@FileLink("ui")
	public String cursor;
	
	@Desc("Prompt")
	public String prompt;
	
	@Desc("Choice 1")
	public String choice1;
	
	@Desc("Choice 2")
	public String choice2;

}
