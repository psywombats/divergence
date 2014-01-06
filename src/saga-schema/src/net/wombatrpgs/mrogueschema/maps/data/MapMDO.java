/**
 *  MapMDO.java
 *  Created on Jan 3, 2014 7:58:49 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogueschema.maps.data;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;
import net.wombatrpgs.mgns.core.Annotations.Nullable;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mrogueschema.audio.MusicMDO;
import net.wombatrpgs.mrogueschema.characters.data.CharacterMDO;
import net.wombatrpgs.mrogueschema.cutscene.data.SceneParentMDO;
import net.wombatrpgs.mrogueschema.graphics.effects.data.EffectMDO;
import net.wombatrpgs.mrogueschema.maps.GeneratedMapMDO;
import net.wombatrpgs.mrogueschema.maps.MapGeneratorMDO;

/**
 * Superclass for generated and Tiled maps.
 */
@ExcludeFromTree
public class MapMDO extends MainSchema {
	
	@Desc("Map width, in tiles")
	public Integer mapWidth;
	
	@Desc("Map height, in tiles")
	public Integer mapHeight;
	
	@Desc("Danger level - approximate depth")
	public Integer danger;
	
	@Desc("Generator - The algorithm that will be used to generate this map")
	@SchemaLink(MapGeneratorMDO.class)
	public String generator;
	
	@Desc("Effect - graphical effect that plays on map, or none")
	@SchemaLink(EffectMDO.class)
	@Nullable
	public String effect;
	
	@Desc("Cutscene - scene or set of scenes that play on first entry")
	@SchemaLink(SceneParentMDO.class)
	@Nullable
	public String scene;
	
	@Desc("Floor # - displays in UI")
	public String floor;
	
	@Desc("BGM")
	@SchemaLink(MusicMDO.class)
	@Nullable
	public String bgm;
	
	@Desc("Characters to spawn here (offscreen)")
	@SchemaLink(CharacterMDO.class)
	public String[] characters;
	
	@Desc("All levels that can be reached by ascending")
	@SchemaLink(GeneratedMapMDO.class)
	public String[] pathsUp;
	
	@Desc("All levels that can be reached by descending")
	@SchemaLink(GeneratedMapMDO.class)
	public String[] pathsDown;

}
