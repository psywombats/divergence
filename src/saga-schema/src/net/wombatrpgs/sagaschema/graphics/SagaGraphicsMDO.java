/**
 *  SagaGraphicsMDO.java
 *  Created on May 15, 2014 1:16:38 AM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.graphics;

import net.wombatrpgs.mgneschema.graphics.ShaderMDO;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * SaGa-specific graphics settings.
 */
@Path("graphics/")
public class SagaGraphicsMDO extends MainSchema {
	
	@Desc("Shader - used to render pretty much everything")
	@SchemaLink(ShaderMDO.class)
	public String shader;
	
	@Desc("Filter black - what shaders treat as black in source graphics, use rrr,ggg,bbb format")
	public String filterBlack;
	@Desc("Filter dark gray - what shaders treat as dark gray in source graphics, use rrr,ggg,bbb format")
	public String filterDgray;
	@Desc("Filter light gray - what shaders treat as light gray in source graphics, use rrr,ggg,bbb format")
	public String filterLgray;
	@Desc("Filter white - what shaders treat as white in source graphics, use rrr,ggg,bbb format")
	public String filterWhite;
	
	@Desc("Background palette - color mappings for the background level")
	@SchemaLink(PaletteMDO.class)
	public String bgPalette;
	
	@Desc("Foreground palette - color mappings for moving sprites")
	@SchemaLink(PaletteMDO.class)
	public String fgPalette;
	
	@Desc("Wipe shader - used for some screen transitions")
	@SchemaLink(ShaderMDO.class)
	public String wipeShader;

}
