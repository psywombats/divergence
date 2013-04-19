/**
 *  Effect.java
 *  Created on Apr 18, 2013 11:13:08 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.screen.ScreenShowable;
import net.wombatrpgs.rainfallschema.graphics.effects.data.EffectMDO;

/**
 * A thing that represents a graphical effect on a map such as weather, fog,
 * etc.
 */
public abstract class Effect implements ScreenShowable {
	
	protected EffectMDO mdo;
	protected Level parent;
	protected SpriteBatch batch;
	
	/**
	 * Constructs an effect from data.
	 * @param	parent			The parent level
	 * @param	mdo				The data to construct from
	 */
	public Effect(Level parent, EffectMDO mdo) {
		this.mdo = mdo;
		this.parent = parent;
		batch = new SpriteBatch();
	}

	/** @see net.wombatrpgs.rainfall.screen.ScreenShowable#ignoresTint() */
	@Override public boolean ignoresTint() { return false; }

	/**
	 * Update if you need it.
	 * @see net.wombatrpgs.rainfall.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) { }

}
