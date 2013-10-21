/**
 *  InventoryMenuMDO.java
 *  Created on Oct 21, 2013 1:36:07 AM for project MRogueSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.ui;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.FileLink;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Data for creating that item select thing.
 */
@Path("ui/")
public class InventoryMenuMDO extends MainSchema {
	
	@Desc("Backer - overall background for inventory pullout")
	@FileLink("ui")
	public String backer;
	
	@Desc("Highlight - graphic used to highlight items")
	@FileLink("ui")
	public String highlight;
	
	@Desc("Tab - graphic used for that TAB pullout thing")
	@FileLink("ui")
	public String tab;
	
	@Desc("Item count - how many items to display total")
	public Integer itemCount;
	
	@Desc("Pull time - time it takes to expand the inventory, in s")
	public Float pullTime;
	
	@Desc("Font - used for quantity and item description")
	@SchemaLink(FontMDO.class)
	public String font;
	
	@Desc("Text off x - pixels from bottom left of backer to start printing text")
	public Integer textOffX;
	
	@Desc("Text off y - pixels from bottom left of backer to start printing text")
	public Integer textOffY;
	
	@Desc("Item start x - pixels from bottom left of backer to start rendering images at")
	public Integer itemStartX;
	
	@Desc("Item start y - pixels from bottom left of backer to start rendering images at")
	public Integer itemStartY;
	
	@Desc("Item padding - distance between two items, in px")
	public Integer itemPadding;
	
	@Desc("Quantity offset - distance between bottom of item and quantity info")
	public Integer amyOffX;

}
