/**
 *  BattleAnimSeriesMDO.java
 *  Created on Jul 29, 2014 12:25:42 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.graphics.banim;

import net.wombatrpgs.mgns.core.Annotations.DefaultValue;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;

/**
 * A series of battle animation strips play.
 */
@Path("graphics/")
public class BattleAnimSeriesMDO extends BattleAnimMDO {
	
	@Desc("Animation - this is the strip that plays a bunch of times")
	@SchemaLink(BattleAnimStripMDO.class)
	public String anim;
	
	@Desc("Count - how many to play total")
	@DefaultValue("6")
	public Integer count;
	
	@Desc("Concurrent count - how many play at once")
	@DefaultValue("1")
	public Integer concurrent;
	
	@Desc("Span - each anim will appear in an n x n area around target")
	public Integer span;
	
	@Desc("Granularity - each anim will appear on an n by n grid of this size")
	@DefaultValue("16")
	public Integer granularity;

}
