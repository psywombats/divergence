/**
 *  CharacterInsert.java
 *  Created on Apr 4, 2014 10:26:43 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.graphics.ScreenGraphic;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.saga.rpg.Chara;

/**
 * Thing with walking character sprite and their HP.
 */
public class CharacterInsert extends ScreenGraphic {
	
	protected static int WIDTH = 40;
	protected static int HEIGHT = 24;
	protected static int PADDING = 2;
	
	protected FontHolder font;
	protected Chara chara;

	/**
	 * Creates a new insert for a character at 0, 0. Dynamically links to the
	 * character and will update.
	 * @param	chara			The character to create for
	 */
	public CharacterInsert(Chara chara) {
		this.chara = chara;
		this.font = MGlobal.ui.getFont();
	}
	
	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getWidth() */
	@Override public int getWidth() { return WIDTH; }
	
	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getHeight() */
	@Override public int getHeight() { return HEIGHT; }

	/**
	 * @see net.wombatrpgs.mgne.screen.ScreenObject#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		FacesAnimation sprite = chara.getAppearance();
		float renderX = x + PADDING;
		float renderY = y + HEIGHT/2 - sprite.getHeight()/2;
		sprite.renderAt(batch, renderX, renderY);
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (!chara.getAppearance().isMoving()) {
			chara.getAppearance().startMoving();
		}
		chara.getAppearance().update(elapsed);
	}

}
