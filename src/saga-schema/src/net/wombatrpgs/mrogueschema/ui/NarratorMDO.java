/**
 *  NarratorMDO.java
 *  Created on Oct 10, 2013 3:33:23 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.ui;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Tells you how the game's going, bro. Hero hits Enemy for 10 damages!
 */
@Path("ui/")
public class NarratorMDO extends MainSchema {
	
	@Desc("Offset X - x coord to begin messages, from upper left, in px")
	public Integer offsetX;
	
	@Desc("Offset Y - y coord to begin messages, from upper left, in px")
	public Integer offsetY;
	
	@Desc("Width, in px")
	public Integer width;
	
	@Desc("Characters per line")
	public Integer chars;
	
	@Desc("Lines - how many to display")
	public Integer lines;
	
	@Desc("Time to live - after this many seconds, line will fade out, in s")
	public Float ttl;
	
	@Desc("Fadeout time - and it'll take this long to fade out, in s")
	public Float fadeout;

}
