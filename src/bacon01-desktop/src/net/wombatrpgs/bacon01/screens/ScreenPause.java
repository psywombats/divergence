/**
 *  ScreenPause.java
 *  Created on Feb 11, 2015 3:29:35 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.bacon01.core.BMemory;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextFormat;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 *
 */
public class ScreenPause extends Screen {
	
	protected TextFormat format;
	protected TextFormat caret;
	protected int selected;
	
	public ScreenPause() {
		super.onFocusGained();
		
		format  = new TextFormat();
		format.x = 0;
		format.y = getHeight()/2 + 64;
		format.align = HAlignment.CENTER;
		format.width = getWidth();
		format.height = getHeight();
		
		caret = new TextFormat();
		caret.x = getWidth()/2 - 96;
		caret.y = format.y;
		caret.align = HAlignment.LEFT;
		caret.width = 32;
		caret.height = 32;
	}
	
	public void onFocusGained() {
		pushCommandContext(new CMapMenu());
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		FontHolder font = MGlobal.ui.getFont();
		font.draw(batch, format, "RESUME", 0);
		font.draw(batch, format, "RESTART", -16);
		font.draw(batch, format, "EXIT", -32);
		font.draw(batch, format, "TOGGLE FULLSCREEN", -48);
		
		font.draw(batch, caret, ">", -16*selected);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onCommand(net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		switch (command) {
		case MOVE_DOWN:
			selected += 1;
			if (selected == 4) selected = 0;
			break;
		case MOVE_UP:
			selected -= 1;
			if (selected == -1) selected = 3;
			break;
		case UI_CANCEL: case UI_FINISH:
			MGlobal.screens.pop();
			break;
		case UI_CONFIRM:
			switch (selected) {
			case 0:
				MGlobal.screens.pop();
				break;
			case 1:
				if (Gdx.files.getFileHandle(BMemory.FILE_NAME, FileType.Internal).exists()) {
					MGlobal.levelManager.getTele().getPre().run();
					MGlobal.levelManager.getTele().getPre().addListener(new FinishListener() {
						@Override public void onFinish() {
							MGlobal.memory.loadAndSetScreen(BMemory.FILE_NAME);
							Screen screen = MGlobal.levelManager.getScreen();
							MGlobal.screens.push(screen);
							MGlobal.levelManager.getTele().getPost().run();
						};
					});
				} else {
					MGlobal.levelManager.getTele().teleport(
							MGlobal.memory.levelKey,
							MGlobal.memory.heroMemory.tileX,
							MGlobal.memory.heroMemory.tileY);
				}
				break;
			case 2:
				Gdx.app.exit();
				break;
			case 3:
				Gdx.graphics.setDisplayMode(
						MGlobal.window.getWidth(), 
						MGlobal.window.getHeight(), 
						!Gdx.graphics.isFullscreen());
			}
		default:
		}
		return true;
	}
	
	
}
