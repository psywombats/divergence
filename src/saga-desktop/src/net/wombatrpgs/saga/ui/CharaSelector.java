/**
 *  CharaSelector.java
 *  Created on Apr 6, 2014 3:56:39 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.graphics.ScreenGraphic;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.saga.CharacterInsert;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.Chara;

/**
 * Instead of selecting options, it selects characters. This should only really
 * work on the hero party, so it takes it by default.
 */
public class CharaSelector extends ScreenGraphic {
	
	protected static final int INSERTS_MARGIN = 4;
	protected static final int INSERTS_COUNT_HORIZ = 2;
	protected static final int INSERTS_COUNT_VERT = 3;
	
	protected int insertsWidth, insertsHeight;
	protected List<CharacterInsert> inserts;
	protected Nineslice bg;
	
	/**
	 * Creates a new character selector using the party for options.
	 */
	public CharaSelector() {
		insertsWidth = CharacterInsert.WIDTH;
		insertsHeight = CharacterInsert.HEIGHT;
		insertsWidth *= INSERTS_COUNT_HORIZ;
		insertsHeight *= INSERTS_COUNT_VERT;
		insertsWidth += 2 * INSERTS_MARGIN;
		insertsHeight += 2 * (INSERTS_MARGIN+1);
		
		bg = new Nineslice();
	}

	/**
	 * @see net.wombatrpgs.mgne.ui.OptionSelector#getWidth()
	 */
	@Override
	public int getWidth() {
		return insertsWidth;
	}

	/**
	 * @see net.wombatrpgs.mgne.ui.OptionSelector#getHeight()
	 */
	@Override
	public int getHeight() {
		return insertsHeight;
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#setX(float)
	 */
	@Override
	public void setX(float x) {
		for (CharacterInsert insert : inserts) {
			insert.setX(insert.getX() + x - this.x);
		}
		super.setX(x);
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#setY(float)
	 */
	@Override
	public void setY(float y) {
		for (CharacterInsert insert : inserts) {
			insert.setY(insert.getY() + y - this.y);
		}
		super.setY(y);
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		for (CharacterInsert insert : inserts) {
			insert.update(elapsed);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.ui.OptionSelector#coreRender
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void coreRender(SpriteBatch batch) {
		bg.renderAt(batch, x, y);
		for (CharacterInsert insert : inserts) {
			insert.render(batch);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.ScreenObject#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		if (pass == 0) {
			createDisplay();
			bg.resizeTo(insertsWidth, insertsHeight);
		}
		for (CharacterInsert insert : inserts) {
			insert.postProcessing(manager, pass);
		}
	}
	
	/**
	 * Sets up the inserts. Called from the constructor?
	 */
	protected void createDisplay() {
		if (inserts == null) {
			inserts = new ArrayList<CharacterInsert>();
		} else {
			for (CharacterInsert insert : inserts) {
				assets.remove(insert);
			}
			inserts.clear();
		}
		
		float insertX = x + INSERTS_MARGIN;
		float insertY = y + insertsHeight - INSERTS_MARGIN - CharacterInsert.HEIGHT;
		boolean left = true;
		for (Chara hero : SGlobal.heroes.getAll()) {
			CharacterInsert insert = new CharacterInsert(hero);
			insert.setX(insertX);
			insert.setY(insertY);
			if (left) {
				insertX += insert.getWidth();
			} else {
				insertX -= insert.getWidth();
				insertY -= insert.getHeight();
			}
			left = !left;
			inserts.add(insert);
			assets.add(insert);
		}
		
	}

}
