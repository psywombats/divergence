/**
 *  StatsMDO.java
 *  Created on Aug 19, 2013 5:26:56 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.characters.data;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * To correspond to the old stats object from the roguey engine
 */
@Path("characters/")
public class StatsMDO extends HeadlessSchema {
	
	@Desc("Max HP - max health value of a character, in HP")
	@DefaultValue("0")
	public Integer mhp;
	
	@Desc("HP - current health value of a character, in HP")
	@DefaultValue("0")
	public Integer hp;
	
	@Desc("Max MP - max special points of a character, in MP")
	@DefaultValue("0")
	public Integer mmp;
	
	@Desc("MP - current special points of a character, in MP")
	@DefaultValue("0")
	public Integer mp;

	@Desc("Speed - base is 100, 110 for example is a 10% speed increase")
	@DefaultValue("0")
	public Integer speed;
	
	@Desc("Vision radius - in tiles")
	@DefaultValue("0")
	public Integer vision;
	
	@Desc("Defense - likeliness to dodge, 0-100")
	@DefaultValue("0")
	public Integer dodge;
	
	@Desc("Armor - deduction from melee attacks, in HP")
	@DefaultValue("0")
	public Integer armor;
	
	@Desc("Base damage - minimum melee damage output, in HP")
	@DefaultValue("0")
	public Integer dmgBase;
	
	@Desc("Damage range - maximum melee damage is (base+range)")
	@DefaultValue("0")
	public Integer dmgRange;

}
