/**
 *  ScreenDeath.java
 *  Created on Oct 1, 2014 8:16:41 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.mgne.ui.Option;
import net.wombatrpgs.mgne.ui.OptionSelector;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextFormat;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.battle.Battle;

/**
 * If games have an afterlife, then this screen would be it. Challenge again?
 */
public class ScreenDeath extends SagaScreen {
	
	protected static final float CHAR_DURATION = .065f;
	
	protected Battle battle;
	protected TextFormat format;
	protected String text, fullText;
	protected OptionSelector menu;
	protected int lastChars;
	protected float sinceStart;
	protected boolean prompted;
	
	/**
	 * Creates a new death screne after the hero dies in battle.
	 * @param	cause			The battle that is the cause of death
	 */
	public ScreenDeath(Battle cause) {
		this.battle = cause;
		
		WindowSettings win = MGlobal.window;
		format = new TextFormat();
		format.align = HAlignment.CENTER;
		format.height = win.getViewportHeight() / 3;
		format.width = win.getViewportWidth();
		format.x = 0;
		format.y = win.getViewportHeight() * 2 / 3;
		
		fullText = "The party is lost...\n\n Challenge again?";
		text = "";
		
		lastChars = 0;
		
		menu = new OptionSelector(
				new Option("Once more!") {
					@Override public boolean onSelect() { return onRetry(); }
				},
				new Option("Accept defeat") {
					@Override public boolean onSelect() { return onEnd(); }
				});
		assets.add(menu);
	}

	/**
	 * @see net.wombatrpgs.saga.screen.SagaScreen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		
		if (!isFading()) {
			sinceStart += elapsed;
		}
		
		int chars = (int) Math.floor(sinceStart / CHAR_DURATION);
		if (chars > lastChars) {
			if (chars <= fullText.length()) {
				lastChars = chars;
				text = fullText.substring(0, chars);
				for (int i = chars; i < fullText.length(); i += 1) {
					if (fullText.charAt(i) == '\n') {
						text += '\n';
					} else {
						text += ' ';
					}
				}
			} else if (!prompted) {
				WindowSettings win = MGlobal.window;
				menu.showAt(
						win.getViewportWidth()/2 - menu.getWidth()/2,
						win.getViewportHeight()/2 - menu.getHeight());
				prompted = true;
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		
		WindowSettings win = MGlobal.window;
		shapes.begin(ShapeType.Filled);
		float[] white = SGlobal.graphics.getWhite();
		shapes.setColor(white[0], white[1], white[2], 0);
		shapes.rect(0, 0, win.getWidth(), win.getHeight());
		shapes.end();
		
		FontHolder font = MGlobal.ui.getFont();
		font.draw(batch, format, text, 0);
		
		super.render(batch);
	}
	
	/**
	 * Called when the player elects to refight this battle.
	 * @return					True to close the menu
	 */
	protected boolean onRetry() {
		battle.restart();
		return true;
	}
	
	/**
	 * Called when the player elects to end the battle.
	 * @return					True to close the menu
	 */
	protected boolean onEnd() {
		SagaScreen title = new ScreenTitle();
		MGlobal.assets.loadAsset(title,  "new title");
		MGlobal.levelManager.reset();
		SGlobal.heroes = null;
		title.transitonOn(TransitionType.WHITE, null);
		return true;
	}

}
