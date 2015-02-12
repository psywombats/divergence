/**
 *  ScreenTitle.java
 *  Created on Jul 21, 2014 1:50:47 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */

package net.wombatrpgs.bacon01.screens;

import net.wombatrpgs.bacon01.core.BMemory;
import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.io.audio.BackgroundMusic;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextFormat;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.settings.IntroSettingsMDO;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

/**
 * As might be obvious, this is the title screen.
 */
public class ScreenTitle extends Screen {
	
	protected static final int TEXT_Y = 20;
	protected static final int TEXT_WIDTH = 120;
	protected static final int BEGIN_X = 20;
	protected static final int CONTINUE_X = 84;
	
	protected static final String STRING_BEGIN = "START";
	protected static final String STRING_CONTINUE = "CONTINUE";
	
	protected IntroSettingsMDO intro;
	protected Graphic bg;
	protected TextFormat formatBegin, formatContinue;
	protected int selection;
	protected boolean transitioning, bgmd;
	protected BackgroundMusic bgm;
	
	/**
	 * Creates the title screen!
	 */
	public ScreenTitle() {
		intro = MGlobal.data.getEntryFor(Constants.KEY_INTRO, IntroSettingsMDO.class);
		
		bg = new Graphic(intro.titleBG);
		assets.add(bg);
		
		formatBegin = new TextFormat();
		formatBegin.align = HAlignment.LEFT;
		formatBegin.height = 32;
		formatBegin.width = TEXT_WIDTH;
		formatBegin.x = BEGIN_X;
		formatBegin.y = TEXT_Y;
		
		formatContinue = new TextFormat();
		formatContinue.align = HAlignment.LEFT;
		formatContinue.height = 32;
		formatContinue.width = TEXT_WIDTH;
		formatContinue.x = CONTINUE_X;
		formatContinue.y = TEXT_Y;
		
		bgm = MGlobal.audio.generateMusicForKey("dawn");
		assets.add(bgm);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (!bgmd) {
			bgmd = true;
			MGlobal.audio.playBGM(bgm);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		pushCommandContext(new CMapMenu());
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		bg.renderAt(finalBatch, MGlobal.window.getWidth() / 2, MGlobal.window.getHeight() / 2);
		
		FontHolder font = MGlobal.ui.getFont();
		batch.setColor(new Color(0, 0, 0, 1));
		font.draw(batch, formatBegin, STRING_BEGIN, 0);
		font.draw(batch, formatContinue, STRING_CONTINUE, 0);
		batch.setColor(new Color(1, 1, 1, 1));
		
		Graphic cursor = MGlobal.ui.getCursor();
		int offX = cursor.getWidth() * -1 + 2;
		int offY = cursor.getHeight()/2 * -1 - 2;
		if (selection == 0) {
			cursor.renderAt(batch, BEGIN_X + offX, TEXT_Y + offY);
		} else if (selection == 1) {
			cursor.renderAt(batch, CONTINUE_X + offX, TEXT_Y + offY);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (super.onCommand(command)) return true;
		if (transitioning) return true;
		switch (command) {
		case MOVE_LEFT:		return moveCursor(-1);
		case MOVE_RIGHT:	return moveCursor(1);
		case UI_CONFIRM:	return confirm();
		case UI_FINISH:		return confirm();
		default:			return false;
		}
	}
		
	/**
	 * Moves the selection cursor one way or another.
	 * @param	delta			The delta to move cursor by
	 * @return					True to indicate command was processed
	 */
	public boolean moveCursor(int delta) {
		selection += delta;
		if (selection < 0) selection = 1;
		if (selection > 1) selection = 0;
		return true;
	}

	/**
	 * Called when the user confirms their selection on the menu.
	 * @return					True to indicate command was processed
	 */
	public boolean confirm() {
		if (selection == 0) {
			// start
			transitioning = true;
			final Screen introScreen = new ScreenIntro();
			MGlobal.assets.loadAsset(introScreen, "intro screen");
			MGlobal.levelManager.getTele().getPre().run();
			MGlobal.levelManager.getTele().getPre().addListener(new FinishListener() {
				@Override public void onFinish() {
					introScreen.getTint().r = 0;
					introScreen.getTint().g = 0;
					introScreen.getTint().b = 0;
					MGlobal.screens.push(introScreen);
					MGlobal.levelManager.getTele().getPost().run();
				};
			});
		} else {
			// load
			if (Gdx.files.getFileHandle(BMemory.FILE_NAME, FileType.Internal).exists()) {
				MGlobal.levelManager.getTele().getPre().run();
				MGlobal.levelManager.getTele().getPre().addListener(new FinishListener() {
					@Override public void onFinish() {
						MGlobal.memory.loadAndSetScreen(BMemory.FILE_NAME);
						Screen screen = MGlobal.levelManager.getScreen();
						MGlobal.screens.push(screen);
						MGlobal.levelManager.getTele().getPost().run();
						MGlobal.audio.playBGM(MGlobal.levelManager.getActive().getBGM());
					};
				});
			}
		}
		return true;
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
	}
	
}
