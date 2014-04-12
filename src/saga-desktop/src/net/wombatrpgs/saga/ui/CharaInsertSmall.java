/**
 *  CharacterInsert.java
 *  Created on Apr 4, 2014 10:26:43 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.saga.rpg.Chara;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * Thing with walking character sprite and their HP.
 */
public class CharaInsertSmall extends CharaInsert {
	
	public static final int WIDTH = 58;
	public static final int HEIGHT = 18;

	protected String hpText1, hpText2;

	/**
	 * Creates a new insert for a character at 0, 0. Dynamically links to the
	 * character and will update.
	 * @param	chara			The character to create for
	 */
	public CharaInsertSmall(Chara chara) {
		super(chara);
		format.height = 80;
		format.width = 160;
	}
	
	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getWidth() */
	@Override public int getWidth() { return WIDTH; }
	
	/** @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getHeight() */
	@Override public int getHeight() { return HEIGHT; }

	/**
	 * @see net.wombatrpgs.saga.ui.CharaInsert#renderInserts
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	protected void renderInserts(SpriteBatch batch) {
		FontHolder font = MGlobal.ui.getFont();
		font.draw(batch, format, hpText1, -(int) font.getLineHeight() * 0);
		font.draw(batch, format, hpText2, -(int) font.getLineHeight() * 1);
	}

	/**
	 * @see net.wombatrpgs.saga.ui.CharaInsert#coreRefresh()
	 */
	@Override
	protected void coreRefresh() {
		hpText1 = new Integer((int) chara.get(Stat.HP)).toString();
		hpText2 = "/ " + (int) chara.get(Stat.MHP);
	}

}
