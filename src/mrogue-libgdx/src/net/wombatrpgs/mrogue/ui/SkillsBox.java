/**
 *  SkillsBox.java
 *  Created on Oct 19, 2013 7:47:38 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.ui;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.rpg.Hero;
import net.wombatrpgs.mrogue.rpg.abil.Ability;
import net.wombatrpgs.mrogue.ui.text.FontHolder;
import net.wombatrpgs.mrogue.ui.text.TextBoxFormat;
import net.wombatrpgs.mrogueschema.ui.FontMDO;
import net.wombatrpgs.mrogueschema.ui.SkillsBoxMDO;

/**
 * UI for skills etc.
 */
public class SkillsBox extends UIElement {
	
	protected static final int MAX_TEXT_WIDTH = 256;
	
	protected SkillsBoxMDO mdo;
	protected Graphic noAbil;
	protected FontHolder font;
	protected TextBoxFormat format;
	
	/**
	 * Creates a new skills box from data.
	 * @param	mdo				The MDO to create from
	 */
	public SkillsBox(SkillsBoxMDO mdo) {
		this.mdo = mdo;
		noAbil = new Graphic(mdo.empty);
		assets.add(noAbil);
		font = new FontHolder(MGlobal.data.getEntryFor(mdo.font, FontMDO.class));
		assets.add(font);
		
		format = new TextBoxFormat();
		format.align = BitmapFont.HAlignment.CENTER;
		format.height = 32;		// ignores these, mostly
		format.width = MAX_TEXT_WIDTH;
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		List<Ability> abilities = MGlobal.hero.getUnit().getAbilities();
		SpriteBatch batch = MGlobal.screens.peek().getUIBatch();
		Color oldColor = batch.getColor();
		for (int i = 0; i < Hero.ABILITIES_MAX; i += 1) {
			Graphic icon;
			String cost, key, name;
			if (i < abilities.size()) {
				Ability abil = abilities.get(i);
				name = abil.getName();
				if (!MGlobal.hero.getUnit().canUse(abil)) {
					batch.setColor(1, .5f, .5f, .5f);
				} else {
					batch.setColor(1, 1, 1, 1);
				}
				icon = abil.getIcon();
				if (MGlobal.raveMode && !MGlobal.stasis) {
					cost = String.valueOf((char)(MGlobal.rand.nextInt(26) + 'A')) +
							String.valueOf((char)(MGlobal.rand.nextInt(26) + 'A')) +
							": " + MGlobal.rand.nextInt(100);
				} else {
					cost = "  MP:" + String.valueOf(abil.getMP());
				}
				// TODO: KEY INPUT HAX, look up the real key!
				if (MGlobal.raveMode && !MGlobal.stasis) {
					key = String.valueOf((char)(MGlobal.rand.nextInt(26) + 'a')) + MGlobal.rand.nextInt(10);
				} else {
					key = "F" + (i+1);
				}
			} else {
				batch.setColor(1, .5f, .5f, .5f);
				icon = noAbil;
				cost = "";
				key = "";
				name = "";
			}
			int atX = mdo.allOffX + i * mdo.paddingX;
			int atY = mdo.allOffY;
			if (icon != null) {
				icon.renderAt(MGlobal.screens.peek().getUIBatch(), atX, atY);
				
				format.x = atX + icon.getWidth()/2 - MAX_TEXT_WIDTH/2;
				format.y = atY + mdo.textY;
				String text = key + cost;
				font.draw(batch, format, text, 0);
				
				if (i % 2 == 0) {
					format.y = (int) (atY + (mdo.textY - font.getLineHeight()));
				} else {
					format.y = (int) (atY + -mdo.textY + icon.getHeight() + font.getLineHeight()*1.2f);
				}
				if (!MGlobal.raveMode) {
					font.draw(batch, format, name, 0);
				}
			}
		}
		batch.setColor(oldColor);
	}

}
