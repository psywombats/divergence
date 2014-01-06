/**
 *  LoadedLevel.java
 *  Created on Jan 3, 2014 7:25:18 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps;

import net.wombatrpgs.mrogue.screen.Screen;
import net.wombatrpgs.mrogueschema.maps.data.MapMDO;

/**
 * A level that's loaded in from a Tiled map. Here's some older stuff.
 * 
 * A Level is comprised of a .tmx tiled map background and a bunch of events
 * that populate it. This thing is a wrapper around Tiled with a few RPG-
 * specific functions built in, like rendering its layers in order so that the
 * player's sprite can appear say above the ground but below a cloud or other
 * upper chip object.
 */
public class LoadedLevel extends Level {

	public LoadedLevel(MapMDO mdo, Screen screen) {
		super(mdo, screen);
		// TODO Auto-generated constructor stub
	}

}
