/**
 *  BattleAnimShotsMDO.java
 *  Created on Jul 30, 2014 1:05:55 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.graphics.banim;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;

/**
 * Multiple gunshot wound animation data.
 */
@Path("graphics/")
public class BattleAnimShotsMDO extends BattleAnimMDO {
	
	@Desc("Animation - the individual shot animation")
	@SchemaLink(BattleAnimStripMDO.class)
	public String anim;
	
	@Desc("Count - shots to fire in total")
	public Integer count;
	
	@Desc("Delay - interval between each shot, in seconds")
	public Float delay;
	
	@Desc("Columns - this many shots appear in a row")
	public Integer cols;
	
	@Desc("Horizontal padding - two shots have this many pixels between them")
	public Float padX;
	
	@Desc("Vertical padding - two rows of shots have this many pixels between them")
	public Float padY;
	
	@Desc("Max vertical gain - each shot appears +/- by this many pixels")
	public Float gainX;
	
	@Desc("Max horizontal gain - each shot appears +/- by this many pixels")
	public Float gainY;
	
	@Desc("Horizontal jitter - each shot height is offset by this many pixels")
	public Float jitterX;
	
	@Desc("Vertical jitter - each shot height is offset by this many pixels")
	public Float jitterY;

}
