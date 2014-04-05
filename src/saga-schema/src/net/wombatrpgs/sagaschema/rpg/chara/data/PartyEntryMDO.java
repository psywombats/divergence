/**
 *  PartyEntryMDO.java
 *  Created on Apr 2, 2014 10:04:40 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.chara.data;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.sagaschema.rpg.chara.CharacterMDO;

/**
 * Entry for a party?
 */
public class PartyEntryMDO extends HeadlessSchema {
	
	@Desc("Monster")
	@SchemaLink(CharacterMDO.class)
	public String monster;
	
	@Desc("Count")
	@DefaultValue("1")
	public Integer count;

}
