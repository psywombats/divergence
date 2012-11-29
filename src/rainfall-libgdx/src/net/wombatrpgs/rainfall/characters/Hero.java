/**
 *  Hero.java
 *  Created on Nov 25, 2012 8:33:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.maps.EventMDO;

/**
 * Placeholder class for the protagonist player.
 */
public class Hero extends Character {

	/**
	 * Placeholder constructor
	 * @param parent
	 * @param mdo
	 * @param x
	 * @param y
	 */
	public Hero(Level parent, EventMDO mdo, int x, int y) {
		super(parent, mdo, x, y);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapEvent#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		this.parent.applyPhysicalCorrections(this);
	}
	
	

}
