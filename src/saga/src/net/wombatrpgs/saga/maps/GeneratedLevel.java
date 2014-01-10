/**
 *  Level.java
 *  Created on Nov 12, 2012 6:08:39 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.saga.core.MGlobal;
import net.wombatrpgs.saga.maps.gen.MapGeneratorFactory;
import net.wombatrpgs.saga.maps.layers.EventLayer;
import net.wombatrpgs.saga.screen.Screen;
import net.wombatrpgs.sagaschema.maps.GeneratedMapMDO;
import net.wombatrpgs.sagaschema.maps.MapGeneratorMDO;

/**
 * 
 * MR note: This is no longer linked to a .tmx file!! This is a strange thing.
 * Instead, it uses a generator to generate itself. This means there will be a
 * whole mess of variables in here related to tile properties and bullshit. This
 * is mitigated by having the old layer handle most of themselves. In 99% of
 * cases though, all layers but 1 gridlayer (the map layout) and 1 event layer
 * (everything on it) will be empty.
 * Also note that there is only one z layer for objects.
 */
public class GeneratedLevel extends Level {
	
	protected GeneratedMapMDO mdo;
	
	/**
	 * Generates a level from the supplied level data.
	 * @param 	mdo				The data to make level from
	 * @param	screen			The screen we render to
	 */
	public GeneratedLevel(GeneratedMapMDO mdo, Screen screen) {
		super(mdo, screen);
		this.mdo = mdo;
		
		// map gen!
		eventLayer = new EventLayer(this);
		assets.add(eventLayer);
		this.mapGen = MapGeneratorFactory.createGenerator(
				MGlobal.data.getEntryFor(mdo.generator, MapGeneratorMDO.class),
				this);
		assets.add(mapGen);
		this.mapWidth = mdo.mapWidth;
		this.mapHeight = mdo.mapHeight;
	}

	/**
	 * @see net.wombatrpgs.saga.screen.ScreenObject#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		if (pass == 0) {
			mapGen.generateMe();
		}
	}

}
