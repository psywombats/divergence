/**
 *  SagaSettings.java
 *  Created on Apr 4, 2014 7:56:04 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.settings;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.sagaschema.rpg.chara.PartyMDO;

/**
 * Some random settings for saga.
 */
@Path("settings/")
public class SagaSettingsMDO extends MainSchema {
	
	@Desc("Hero party - how the hero party starts, usually empty")
	@SchemaLink(PartyMDO.class)
	public String heroParty;
	
	@Desc("Monster settings - meat chance, random encounter stuff, etc")
	@SchemaLink(MonsterSettingsMDO.class)
	public String monsterSettings;

}
