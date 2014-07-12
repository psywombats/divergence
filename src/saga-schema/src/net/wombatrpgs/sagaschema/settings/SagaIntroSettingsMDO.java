/**
 *  IntroSettingsMDO.java
 *  Created on 2014/07/10 22:39:32 for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.sagaschema.rpg.RecruitSelectionMDO;

/**
 * More settings for game stuff.
 */
@Path("settings/")
public class SagaIntroSettingsMDO extends MainSchema {
	
	@Desc("Intro text - sloooowly scrolls, make sure to use \\n etc")
	public String introText;
	
	@Desc("Leader recruiting selection (x1)")
	@SchemaLink(RecruitSelectionMDO.class)
	public String recruitLeader;
	
	@Desc("Member recruiting selection (x3)")
	@SchemaLink(RecruitSelectionMDO.class)
	public String recruitMember;

}
