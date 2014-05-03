/**
 *  TransformationEntryMDO.java
 *  Created on May 2, 2014 6:56:51 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.chara.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.sagaschema.rpg.chara.MonsterFamilyMDO;
import net.wombatrpgs.sagaschema.rpg.chara.MeatGroupMDO;

/**
 * Connection between monster families.
 */
public class TransformationMDO extends HeadlessSchema {
	
	@Desc("Meat group eaten")
	@SchemaLink(MeatGroupMDO.class)
	public String eat;
	
	@Desc("Resulting family")
	@SchemaLink(MonsterFamilyMDO.class)
	public String result;

}
