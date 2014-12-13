/**
 *  ScreenSaves.java
 *  Created on Jul 21, 2014 6:41:29 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.Memory;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.graphics.FacesAnimationFactory;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.saga.core.MemoryIndex;
import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.core.SaveDescriptor;

/**
 * A screen to select from a bunch of savefiles and an empty slot. This could be
 * used for both saving/loading.
 */
public class ScreenSaves extends SagaScreen {
	
	protected static final String FILEPREFIX_SAVE = "savedata/save";
	protected static final String FILESUFFIX_SAVE = ".sav";
	
	protected static final int FILE_WIDTH = 264;
	protected static final int FILE_HEIGHT = 48;
	protected static final int PAD_VERT = 3;
	protected static final int PAD_SPRITES = 2;
	protected static final int PAD_LEFT = 12;
	protected static final int SPACE_LEFT = 112;
	
	protected static final String STRING_NEW = "New slot";
	protected static final String STRING_OUTDATE = "old version save";
	
	// data
	protected MemoryIndex index;
	protected int perScreen, max;
	protected int scroll, selection;
	protected boolean saveMode;
	
	// graphics crap
	protected Nineslice bg;
	protected List<List<FacesAnimation>> sprites;
	
	/**
	 * Creates a new screen in either load or save mode.
	 * @param	saveMode		True to function in save mode, false for load
	 */
	public ScreenSaves(boolean saveMode) {
		this.saveMode = saveMode;
		this.index = MemoryIndex.loadIndex();
		
		scroll = 0;
		selection = 0;
		perScreen = MGlobal.window.getViewportHeight() / FILE_HEIGHT;
		
		bg = new Nineslice(FILE_WIDTH, FILE_HEIGHT);
		assets.add(bg);
		
		refresh();
		
		if (saveMode) {
			if (SGlobal.saveSlot != -1) {
				moveCursor(SGlobal.saveSlot);
			}
		} else {
			moveCursor(index.getLastSavedIndex());
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
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		
		FontHolder font = MGlobal.ui.getFont();
		WindowSettings window = MGlobal.window;
		
		for (int i = 0; i < perScreen; i += 1) {
			
			int atX = (MGlobal.window.getViewportWidth() - FILE_WIDTH) / 2;
			int atY = MGlobal.window.getViewportHeight() - ((i+1) * FILE_HEIGHT);
			bg.renderAt(batch, atX, atY);
			
			if (i + scroll < index.maxSavesCount() && index.getSave(i + scroll) != null) {
				SaveDescriptor save = index.getSave(i + scroll);
				FacesAnimation sample = sprites.get(i).get(0);
				atY += (FILE_HEIGHT - (sample.getHeight() + font.getLineHeight() + PAD_VERT)) / 2;
				atX += PAD_LEFT;
				for (FacesAnimation sprite : sprites.get(i)) {
					sprite.renderAt(batch, atX, atY);
					atX += (sprite.getWidth() + PAD_SPRITES);
				}
				atX = (MGlobal.window.getViewportWidth() - FILE_WIDTH) / 2 + PAD_LEFT;
				atY += (font.getLineHeight() + sample.getHeight() + PAD_VERT);
				batch.begin();
				String titleString = save.isOutdated() ? STRING_OUTDATE : save.getLeaderString();
				font.draw(batch, titleString, atX, atY);
				batch.end();
				
				if (!save.isOutdated()) {
					atX += SPACE_LEFT;
					batch.begin();
					font.draw(batch, save.getDateString(), atX, atY);
					atY -= (font.getLineHeight() + PAD_VERT);
					font.draw(batch, save.getLocation(), atX, atY);
					batch.end();
				}
			} else {
				atX = (MGlobal.window.getViewportWidth() - FILE_WIDTH) / 2 + PAD_LEFT;
				atY = (int) (MGlobal.window.getViewportHeight() - ((i+1) * FILE_HEIGHT) +
						(FILE_HEIGHT - font.getLineHeight()) / 2 + font.getLineHeight());
				batch.begin();
				font.draw(batch, STRING_NEW, atX, atY);
				batch.end();
			}
		}
		
		Graphic cursor = MGlobal.ui.getCursor();
		cursor.renderAt(batch,
				(window.getViewportWidth() - FILE_WIDTH) / 2 - (cursor.getWidth() * 1.2f),
				(perScreen - selection - 1) * FILE_HEIGHT +
				(FILE_HEIGHT - cursor.getHeight()) / 2);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		switch (command) {
		case MOVE_UP:		return moveCursor(-1);
		case MOVE_DOWN:		return moveCursor(1);
		case UI_CONFIRM:	return confirm();
		case UI_FINISH:		return cancel();
		case UI_CANCEL:		return cancel();
		default:			return super.onCommand(command);
		}
	}
	
	/**
	 * Moves the selection cursor up or down
	 * @param	delta			The amount to move (usually 1 or -1)
	 * @return					True to halt processing
	 */
	protected boolean moveCursor(int delta) {
		selection += delta;
		if (selection < 0) {
			selection = 0;
		}
		if (selection == 0) {
			if (scroll > 0) {
				scroll -= 1;
				selection += 1;
			}
		}
		if (selection == 4) {
			if (max > scroll + perScreen) {
				scroll += 1;
				selection -= 1;
			}
		}
		if (selection > 4) {
			selection = 4;
		}
		return true;
	}
	
	/**
	 * Called when the user confirms their save file selection. Should do the
	 * appropriate save/load to that slot.
	 * @return					True to halt processing
	 */
	protected boolean confirm() {
		MGlobal.audio.playSFX(SConstants.SFX_SAVE);
		int slot = scroll + selection;
		String slotno = (slot < 10) ? ("0" + slot) : String.valueOf(slot);
		final String fileName = Memory.saveToPath(slotno);
		if (saveMode) {
			SGlobal.saveSlot = slot;
			MGlobal.memory.save(fileName);
			index.addSave(slot);
			index.save();
			refresh();
		} else {
			if (index.getSave(slot).isOutdated()) {
				MGlobal.audio.playSFX(SConstants.SFX_FAIL);
			} else {
				this.fade(FadeType.TO_BLACK, new FinishListener() {
					@Override public void onFinish() {
						MGlobal.memory.loadAndSetScreen(fileName);
						SagaScreen screen = (SagaScreen) MGlobal.levelManager.getScreen();
						MGlobal.screens.push(screen);
						screen.fade(FadeType.FROM_BLACK, new FinishListener() {
							@Override public void onFinish() {
								dispose();
							}
						});
						screen.update(0);
					}
				});
			}
		}
		return true;
	}
	
	/**
	 * Called when the user cancels loading/saving.
	 * @return					True to halt processing
	 */
	protected boolean cancel() {
		if (saveMode) {
			MGlobal.screens.pop();
		} else {
			SagaScreen title = new ScreenTitle();
			MGlobal.assets.loadAsset(title, "back to title");
			title.transitonOn(TransitionType.BLACK, null);
		}
		dispose();
		return true;
	}
	
	/**
	 * Updates the display of the save slots for after saving a file.
	 */
	protected void refresh() {
		if (sprites != null) {
			for (List<FacesAnimation> spriteSet : sprites) {
				for (FacesAnimation sprite : spriteSet) {
					assets.remove(sprite);
					sprite.dispose();
				}
			}
		}
		sprites = new ArrayList<List<FacesAnimation>>();
		for (int i = 0; i < index.maxSavesCount(); i += 1) {
			List<FacesAnimation> spriteSet = new ArrayList<FacesAnimation>();
			sprites.add(spriteSet);
			if (index.getSave(i) == null) continue;
			for (String spriteKey : index.getSave(i).getSpriteKeys()) {
				FacesAnimation sprite = FacesAnimationFactory.create(spriteKey);
				assets.add(sprite);
				spriteSet.add(sprite);
				sprite.startMoving();
				if (!updateChildren.contains(sprite)) {
					addUChild(sprite);
				}
			}
		}
		MGlobal.assets.loadAsset(this, "save sprites");
	}

}
