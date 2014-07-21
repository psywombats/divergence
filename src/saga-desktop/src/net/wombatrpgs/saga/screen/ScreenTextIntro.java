/**
 *  ScreenTextIntro.java
 *  Created on Jul 8, 2014 11:39:49 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.io.InputEvent;
import net.wombatrpgs.mgne.io.Keymap.KeyState;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextboxFormat;
import net.wombatrpgs.mgneschema.io.data.InputButton;
import net.wombatrpgs.sagaschema.settings.SagaIntroSettingsMDO;

/**
 * Screen for scrolling text.
 */
public class ScreenTextIntro extends SagaScreen {
	
	protected static final String INTRO_MDO_KEY = "sagaintro_default";
	protected static final float SCROLL_SPEED = 12;	// in px/s
	
	protected SagaIntroSettingsMDO mdo;
	protected SagaScreen recruit;
	protected Nineslice bg;
	protected String text;
	protected boolean finished, transitioning;
	
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
		transitioning = false;
		height = font.getHeight(text);
		scrolled = window.getViewportHeight() * .1f;
		
		bg = new Nineslice(window.getWidth() + 32, window.getHeight() + 32);
		assets.add(bg);
	}
	
	/** @return True if the text has finished displaying */
	public boolean isFinished() { return finished; }

	/**
	 * @see net.wombatrpgs.saga.screen.SagaScreen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		
		KeyState turbo = MGlobal.keymap.getButtonState(InputButton.BUTTON_A);
		int mult = turbo == KeyState.DOWN ? 5 : 1;
		scrolled += elapsed * SCROLL_SPEED * mult;
		scrollFormat.y = (int) scrolled;
		if (scrolled > height + MGlobal.window.getHeight() * .9f) {
			finished = true;
		}
		
		if (finished && !transitioning) {
			transitioning = true;
			final SagaScreen textIntro = this;
			recruit = new ScreenRecruit(mdo.recruitLeader, new FinishListener() {
				@Override public void onFinish() {
					SagaScreen gameScreen = new ScreenWorld();
					MGlobal.assets.loadAsset(gameScreen, "game screen");
					gameScreen.transitonOn(TransitionType.BLACK, new FinishListener() {
						@Override public void onFinish() {
							textIntro.dispose();
							recruit.dispose();
						}
					});
				}
			});
			MGlobal.assets.loadAsset(recruit, "recruit screen");
			recruit.transitonOn(TransitionType.BLACK, null);
		}
		
		int margin = (fade == null) ? 32 : 0;
		
		background.getShader().begin();
		background.getShader().setUniformf("u_margin", margin);
		background.getShader().end();
		
		foreground.getShader().begin();
		foreground.getShader().setUniformf("u_margin", margin);
		foreground.getShader().end();
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onEvent
	 * (net.wombatrpgs.mgne.io.InputEvent)
	 */
	@Override
	public void onEvent(InputEvent event) {
		if (event.button == InputButton.BUTTON_START) {
			// finished = true;
		} else {
			super.onEvent(event);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		bg.renderAt(background.getBatch(),
				(MGlobal.window.getWidth() - bg.getWidth()) / 2,
				(MGlobal.window.getHeight() - bg.getHeight()) / 2);
		MGlobal.ui.getFont().draw(batch, scrollFormat, text, 0);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		bg.dispose();
	}

}
