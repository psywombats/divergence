/**
 *  SagaScreen.java
 *  Created on May 15, 2014 1:34:09 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.graphics.BatchWithShader;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.saga.core.SGlobal;

/**
 * Saga-specific screen meant to apply some gameboy filters.
 */
public class SagaScreen extends Screen {

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#constructFinalBatch()
	 */
	@Override
	protected SpriteBatch constructFinalBatch() {
		BatchWithShader result = SGlobal.graphics.constructGameboyBatch();
		return result.getBatch();
	}

}
