/**
 *  HudMDO.java
 *  Created on Feb 6, 2013 1:34:26 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.ui;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.InlineSchema;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.rainfallschema.graphics.GraphicMDO;
import net.wombatrpgs.rainfallschema.maps.data.Direction;
import net.wombatrpgs.rainfallschema.ui.data.IconPlacementMDO;

/**
 * A graphic that is display on top of the screen.
 */
@Path("ui/")
public class HudMDO extends MainSchema {
	
	@Desc("Graphic - the actual image that will be displayed")
	@SchemaLink(GraphicMDO.class)
	public String graphic;
	
	@Desc("Anchor side - graphic will be pinned to this side of the screen")
	public Direction anchorDir;
	
	@Desc("Minimap x - upper left x of where minimap is displayed, relative to left (in px)")
	public Integer minimapX;
	
	@Desc("Minimap y - upper left y of where minimap is displayed, relative to top (in px)")
	public Integer minimapY;
	
	@Desc("Minimap alpha mask - alpha-channel graphic that serves as alpha for minimap, or none")
	@Nullable
	@SchemaLink(GraphicMDO.class)
	public String alphaMask;
	
	@Desc("Minimap mask - graphic that will be rendered on top of the minimap, or none")
	@SchemaLink(GraphicMDO.class)
	@Nullable
	public String mask;
	
	@Desc("Icon locations - where icons will appear for bound moves, if any")
	@InlineSchema(IconPlacementMDO.class)
	public IconPlacementMDO[] icons;

}
