/**
 *  CharaInsertFull.java
 *  Created on Apr 11, 2014 11:21:43 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.ui;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.saga.rpg.Chara;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Chara insert with name, race, status info, etc.
 */
public class CharaInsertFull extends CharaInsert {
	
	public static final int WIDTH = 86;
	public static final int HEIGHT = 28;
	
	protected String line1, line2, line3;

	/**
	 * Creates a full chara insert for the given chara.
	 * @param	chara			The character to generate for
	 */
	public CharaInsertFull(Chara chara) {
		super(chara);
		format.width = 104;
		format.height = 80;
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
		font.draw(batch, format, line3, (int) font.getLineHeight() * 0);
		font.draw(batch, format, line2, (int) font.getLineHeight() * 1);
		font.draw(batch, format, line1, (int) font.getLineHeight() * 2);
	}

	/**
	 * @see net.wombatrpgs.saga.ui.CharaInsert#coreRefresh()
	 */
	@Override
	public void coreRefresh() {
		format.y -= 4;
		line1 = chara.getName();
		line2 = chara.getRace().getName() + " " + chara.getGender().getLabel();
		String mid = chara.get(Stat.MHP) > 100 ? "/" : " / ";
		line3 = (int) chara.get(Stat.HP) + mid + (int) chara.get(Stat.MHP);
	}

}
