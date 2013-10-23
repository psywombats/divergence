/**
 *  SceneSetMDO.java
 *  Created on Oct 22, 2013 1:06:46 PM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.cutscene;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mrogueschema.cutscene.data.HeadlessSceneMDO;
import net.wombatrpgs.mrogueschema.cutscene.data.SceneParentMDO;

/**
 * A list of cutscenes to select from randomly.
 */
@Path("cutscene/")
public class SceneSetMDO extends SceneParentMDO {
	
	@Desc("All characters to substitute in these scenes")
	@SchemaLink(CharacterSetMDO.class)
	public String charas;
	
	@Desc("All scenes that can be chosen from")
	@InlineSchema(HeadlessSceneMDO.class)
	public HeadlessSceneMDO[] scenes;

}
