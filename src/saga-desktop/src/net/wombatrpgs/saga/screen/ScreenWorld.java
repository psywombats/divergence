/**
 *  WorldScreen.java
 *  Created on Mar 28, 2014 5:14:00 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.command.CMapGame;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 * Shows up when you wander the overworld.
 */
public class ScreenWorld extends SagaScreen {
	
	/**
	 * Constructs the SaGa world screen.
	 */
	public ScreenWorld() {
		pushCommandContext(new CMapGame());
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.instances.ScreenGame#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (super.onCommand(command)) return true;
		switch (command) {
		case WORLD_PAUSE:
			ScreenPause menu = new ScreenPause();
			MGlobal.assets.loadAsset(menu, "main menu");
			MGlobal.screens.push(menu);
			return true;
		default:
			return MGlobal.getHero().onCommand(command);
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		MGlobal.levelManager.getActive().update(elapsed);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		MGlobal.levelManager.getActive().render(batch);
		super.render(batch);
	}

}
