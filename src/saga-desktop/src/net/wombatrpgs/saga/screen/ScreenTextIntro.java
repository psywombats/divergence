/**
 *  ScreenTextIntro.java
 *  Created on Jul 8, 2014 11:39:49 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.Effect;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextboxFormat;
import net.wombatrpgs.sagaschema.settings.SagaIntroSettingsMDO;

/**
 * Screen for scrolling text.
 */
public class ScreenTextIntro extends SagaScreen {
	
	protected static final String INTRO_MDO_KEY = "sagaintro_default";
	protected static final float SCROLL_SPEED = 12;	// in px/s
	
	protected SagaIntroSettingsMDO mdo;
	protected Effect effect;
	protected String text;
	protected boolean finished;
	
	protected TextboxFormat scrollFormat;
	protected float scrolled, height;
	
	/**
	 * Constructs a screen to display the given text. Loads the intro settings
	 * from the database.
	 */
	public ScreenTextIntro() {
		this.mdo = MGlobal.data.getEntryFor(INTRO_MDO_KEY, SagaIntroSettingsMDO.class);
		this.text = mdo.introText.replace("\\n", "\n");
		
		FontHolder font = MGlobal.ui.getFont();
		WindowSettings window = MGlobal.window;
		scrollFormat = new TextboxFormat();
		scrollFormat.align = HAlignment.CENTER;
		scrollFormat.height = 5000;
		scrollFormat.width = window.getViewportWidth() * 2 / 3;
		scrollFormat.x = window.getViewportWidth() / 6;
		scrollFormat.y = 0;
		
		finished = false;
		height = font.getHeight(text);
		scrolled = window.getViewportHeight() * .1f;
		
		effect = new Effect("margin.vert", "margin.frag");
		effects.add(effect);
		assets.add(effect);
	}
	
	/** @return True if the text has finished displaying */
	public boolean isFinished() { return finished; }

	/**
	 * @see net.wombatrpgs.saga.screen.SagaScreen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		scrolled += elapsed * SCROLL_SPEED;
		scrollFormat.y = (int) scrolled;
		if (scrolled > height + MGlobal.window.getHeight() * .9f) {
			finished = true;
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		MGlobal.ui.getFont().draw(batch, scrollFormat, text, 0);
	}

	/**
	 * @see net.wombatrpgs.saga.screen.SagaScreen#clear()
	 */
	@Override
	protected void clear() {
		WindowSettings window = MGlobal.window;
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shapes.setColor(255, 255, 255, 1);
		shapes.begin(ShapeType.Filled);
		shapes.rect(0, 0, window.getWidth(), window.getHeight());
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		effect.dispose();
	}

}
