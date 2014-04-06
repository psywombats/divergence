/**
 *  CharacterInsert.java
 *  Created on Apr 4, 2014 10:26:43 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.graphics.ScreenGraphic;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextBoxFormat;
import net.wombatrpgs.saga.rpg.Chara;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Thing with walking character sprite and their HP.
 */
public class CharacterInsert extends ScreenGraphic {
	
	protected static int WIDTH = 58;
	protected static int HEIGHT = 18;
	protected static int PADDING = 2;
	
	protected TextBoxFormat format;
	protected Chara chara;
	protected String hpText1, hpText2;
	protected int lastHP, lastMHP;

	/**
	 * Creates a new insert for a character at 0, 0. Dynamically links to the
	 * character and will update.
	 * @param	chara			The character to create for
	 */
	public CharacterInsert(Chara chara) {
		this.chara = chara;
		format = new TextBoxFormat();
		format.align = HAlignment.LEFT;
		format.height = 80;
		format.width = 160;
	}
	
	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getWidth() */
	@Override public int getWidth() { return WIDTH; }
	
	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getHeight() */
	@Override public int getHeight() { return HEIGHT; }

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#coreRender
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void coreRender(SpriteBatch batch) {
		FacesAnimation sprite = chara.getAppearance();
		FontHolder font = MGlobal.ui.getFont();
		float renderX = x + PADDING;
		float renderY = y + HEIGHT/2 - sprite.getHeight()/2;
		sprite.renderAt(batch, renderX, renderY);
		font.draw(batch, format, hpText1, 0);
		font.draw(batch, format, hpText2, -(int) font.getLineHeight());
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		FontHolder font = MGlobal.ui.getFont();
		if (!chara.getAppearance().isMoving()) {
			chara.getAppearance().startMoving();
		}
		chara.getAppearance().update(elapsed);
		
		if (chara.get(Stat.HP) != lastHP || chara.get(Stat.MHP) != lastMHP) {
			hpText1 = new Integer((int) chara.get(Stat.HP)).toString();
			hpText2 = "/ " + (int) chara.get(Stat.MHP);
		}
		format.x = (int) (x + PADDING*2 + chara.getAppearance().getWidth());
		format.y = (int) (y + font.getLineHeight()*2);
	}

}
