/**
 *  ScreenGameOver.java
 *  Created on Feb 7, 2015 3:44:18 PM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.bacon01.core.BMemory;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.core.interfaces.Timer;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 * bacon
 */
public class ScreenGameOver extends Screen {
	
	protected Graphic graphic;
	protected boolean ended;
	
	public ScreenGameOver(boolean frag) {
		if (!frag) {
			graphic = new Graphic("GameOver01.png");
		} else {
			graphic = new Graphic("GameOver02.png");
		}
		assets.add(graphic);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		graphic.renderAt(finalBatch, MGlobal.window.getWidth() / 2, MGlobal.window.getHeight() / 2);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		new Timer(0, new FinishListener() {
			@Override public void onFinish() {
				MGlobal.levelManager.getTele().getPost().addListener(new FinishListener() {
					@Override public void onFinish() {
						ended = true;
					}
				});
				MGlobal.levelManager.getTele().getPost().run();
			}
		});
		pushCommandContext(new CMapMenu());
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onCommand(net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (!ended) return true;
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
		return super.onCommand(command);
	}

}
