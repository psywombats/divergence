/**
 *  SpeakerMDO.java
 *  Created on Feb 1, 2013 2:44:34 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.cutscene;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Someone who can say something during a cutscene. This may or may not be
 * superseded by the stuff in chat-mapper.
 */
@Path("cutscene/")
public class SpeakerMDO extends MainSchema {
	
	@Desc("Portrait file - displayed while they talk, should be png")
	@FileLink("portraits")
	public String file;
	
	@Desc("Script name - how the script refers to this character, for instance "
			+ "\"NICO: blah blah\" would be NICO")
	public String id;
	
	@Desc("Text box name - how this character is referred to in text box nametags")
	public String name;

}
