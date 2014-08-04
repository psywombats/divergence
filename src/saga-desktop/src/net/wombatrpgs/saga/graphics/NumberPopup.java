/**
 *  NumberPopup.java
 *  Created on Jul 31, 2014 6:17:00 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextboxFormat;
import net.wombatrpgs.saga.core.SGlobal;

/**
 * An in-battle popup to show the amount of damage dealt to a group
 */
public class NumberPopup extends PortraitAnim {
	
	protected static final float DURATION = .8f;		// in s
	protected static final float BUMP_DURATION = .5f;	// in s
	protected static final float RAISE_HEIGHT = 24;		// in px
	
	protected String damage;
	protected FontHolder font;
	protected TextboxFormat format;
	protected int x, y;
	protected int offY;
	protected int width, height;
	
	/**
	 * Creates a popup for a single damage value.
	 * @param	damage			The damage value to show
	 * @param	startX			The x-coord of the middle of the display target
	 * @param	startY			The y-coord of the middle of the display target
	 */
	public NumberPopup(int damage) {
		this.damage = String.valueOf(damage);
		
		font = SGlobal.graphics.getNumFont();
		width = (int) font.getWidth(this.damage);
		height = (int) font.getLineHeight();
		
		format = new TextboxFormat();
		format.align = HAlignment.LEFT;
		format.height = height + 1;
		format.width = width + 1;
	}

	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getWidth() */
	@Override public int getWidth() { return width; }

	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getHeight() */
	@Override public int getHeight() { return height; }
	
	/** @see net.wombatrpgs.saga.graphics.PortraitAnim#isDone() */
	@Override public boolean isDone() { return sinceStart > DURATION; }

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.PosRenderable#renderAt
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch, float, float)
	 */
	@Override
	public void renderAt(SpriteBatch batch, float x, float y) {
		format.x = (int) (x - width / 2);
		format.y = (int) (y - height / 2);
		font.draw(batch, format, damage, offY);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		sinceStart += elapsed;
		float x = (sinceStart / BUMP_DURATION);
		if (x > 1f) x = 1f;
		float y = -2.55f * (x*x) + 3.65f * x - .027f;
		offY = (int) (y * RAISE_HEIGHT);
	}

}
