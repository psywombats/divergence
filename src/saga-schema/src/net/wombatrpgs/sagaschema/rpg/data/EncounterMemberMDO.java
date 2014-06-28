/**
 *  EncounterMemberMDO.java
 *  Created on Jun 27, 2014 7:26:11 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.rpg.data;

import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.sagaschema.rpg.chara.CharaMDO;

/**
 * A group of enemies with variable number, part of an encounter.
 */
public class EncounterMemberMDO extends HeadlessSchema {
	
	@Desc("Enemy")
	@SchemaLink(CharaMDO.class)
	public String enemy;
	
	@Desc("Possible amount - max-min syntax, eg 0-2 or 1-4")
	public String amount;

}
