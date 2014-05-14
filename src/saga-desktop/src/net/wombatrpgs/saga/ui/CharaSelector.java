/**
 *  CharaSelector.java
 *  Created on Apr 6, 2014 3:56:39 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.ScreenGraphic;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.chara.Party;

/**
 * Instead of selecting options, it selects characters. This should only really
 * work on the hero party, so it takes it by default.
 */
public class CharaSelector extends ScreenGraphic implements	CommandListener {
	
	protected Screen parent;
	protected Party party;
	
	protected static final int INSERTS_MARGIN = 7;
	protected static final int INDENT_SIZE = 4;
	protected static final int DEFAULT_COLUMNS = 2;
	protected static final int DEFAULT_ROWS = 3;
	protected static final int DEFAULT_CURSOR_SPACE = -3;
	protected static final float SWAP_VELOCITY = 60f; // in scrpx/s
	
	// inserts
	protected int cols, rows;
	protected float padX, padY;
	protected boolean fullMode, combatMode, showBG;
	protected int insertsWidth, insertsHeight;
	protected Collection<CharaInsert> allInserts;
	protected List<CharaInsert> orderedInserts;
	protected Nineslice bg;
	
	// cursor
	protected boolean cursorOn, indentOn;
	protected boolean cancellable;
	protected int selectedX, selectedY;
	protected int indentX, indentY;
	protected float cursorX, cursorY;
	protected float cursorSpace;
	protected SelectionListener onSelect, onHover;
	
	/**
	 * Creates a new character selector with a custom party.
	 * @param	full			True to use large version
	 * @param	comb			True to use combat status and not race etc
	 * @param	showBG			True to use the automatic nineslice bg
	 * @param	padX			The horizontal floating fudge
	 * @param	padY			The vertical floating fudge
	 * @param	cursorSpace		The pixels between charas and the cursor
	 * @param	cols			The number of columns of inserts
	 * @param	rows			The number of rows of inserts
	 */
	public CharaSelector(Party party, boolean full, boolean comb, boolean bgOn,
			float padX, float padY, float cursorSpace, int cols, int rows) {
		this.party = party;
		this.fullMode = full;
		this.combatMode = comb;
		this.showBG = bgOn;
		this.padX = padX;
		this.padY = padY;
		this.cursorSpace = cursorSpace;
		this.cols = cols;
		this.rows = rows;
		insertsWidth = getInsertWidth();
		insertsHeight = getInsertHeight();
		insertsWidth *= cols;
		insertsHeight *= rows;
		insertsWidth += 2 * INSERTS_MARGIN;
		insertsHeight += 2 * (INSERTS_MARGIN+2);
		insertsWidth += padX * (cols-1);
		insertsHeight += padY * (rows-1);
		
		bg = new Nineslice();
		assets.add(bg);
	}
	
	/**
	 * Creates a new character selector with a custom party. Uses the default
	 * two columns three rows approach.
	 * @param	fullMode		True to use large version
	 * @param	combatMode		True to use combat status and not race etc
	 * @param	showBG			True to use the automatic nineslice bg
	 * @param	padding			The horizontal floating fudge... ugly
	 */
	public CharaSelector(Party party, boolean fullMode, boolean combatMode,
			boolean showBG, float padding) {
		this(party, fullMode, combatMode, showBG, padding, DEFAULT_CURSOR_SPACE,
				0, DEFAULT_COLUMNS, DEFAULT_ROWS);
	}
	
	/**
	 * Creates a new character selector for the hero party with no bg or padding
	 * but with custom row/column count.
	 * @param	full			True to use large version
	 * @param	combat			True to use combat status and not race etc
	 * @param	cols			The number of columns of inserts
	 * @param	rows			The number of rows of inserts
	 */
	public CharaSelector(boolean full, boolean combat, int cols, int rows) {
		this(SGlobal.heroes, full, combat, true, 0, 0, DEFAULT_CURSOR_SPACE,
				cols, rows);
	}
	
	/**
	 * Creates a new character selector for the hero party, showing bg, no pad.
	 * @param	fullMode		True to use large version
	 * @param	combatMode		True to use combat status and not race etc
	 */
	public CharaSelector(boolean fullMode, boolean combatMode) {
		this(fullMode, combatMode, DEFAULT_COLUMNS, DEFAULT_ROWS);
	}

	/** @see net.wombatrpgs.mgne.ui.OptionSelector#getWidth() */
	@Override public int getWidth() { return insertsWidth; }

	/** @see net.wombatrpgs.mgne.ui.OptionSelector#getHeight() */
	@Override public int getHeight() { return insertsHeight; }
	
	/** @return The height of the border of the background slice, in px */
	public int getBorderHeight() { return bg.getBorderHeight(); }
	
	/** @param listener The callback for when the cursor moves */
	public void setHoverListener(SelectionListener listener) { onHover = listener; }

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#setX(float)
	 */
	@Override
	public void setX(float x) {
		for (CharaInsert insert : allInserts) {
			insert.setX(insert.getX() + x - this.x);
		}
		super.setX(x);
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#setY(float)
	 */
	@Override
	public void setY(float y) {
		for (CharaInsert insert : allInserts) {
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
		for (CharaInsert insert : allInserts) {
			insert.update(elapsed);
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.ScreenObject#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		if (pass == 0) {
			createDisplay();
			bg.resizeTo(insertsWidth, insertsHeight);
		}
		for (CharaInsert insert : allInserts) {
			insert.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		switch (command) {
		case MOVE_UP:		moveCursorVert(-1);		return true;
		case MOVE_DOWN:		moveCursorVert(1);		return true;
		case MOVE_LEFT:		moveCursorHoriz(-1);	return true;
		case MOVE_RIGHT:	moveCursorHoriz(1);		return true;
		case UI_CONFIRM:	confirm();				return true;
		case UI_CANCEL:		cancel();				return true;
		default:									return true;
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.ui.OptionSelector#coreRender
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void coreRender(SpriteBatch batch) {
		if (showBG) {
			bg.renderAt(batch, x, y);
		}
		for (CharaInsert insert : orderedInserts) {
			insert.render(batch);
		}
		if (indentOn) {
			renderCursor(batch, indentX, indentY, INDENT_SIZE);
		}
		if (cursorOn) {
			renderCursor(batch, selectedX, selectedY, 0);
		}
	}
	
	/**
	 * Waits for the user to select a character then returns the listener.
	 * @param	listener		The callback for selection
	 * @param	canCancel		True if the user can cancel to select nobody
	 */
	public void awaitSelection(SelectionListener listener, boolean canCancel) {
		this.onSelect = listener;
		this.cancellable = canCancel;
		focus();
		cursorOn = true;
		selectedX = 0;
		selectedY = 0;
		updateCursor();
	}
	
	/**
	 * Updates the inserts to reflect health, status, etc.
	 */
	public void refresh() {
		for (CharaInsert insert : allInserts) {
			insert.refresh();
		}
	}
	
	/**
	 * Marks the selected cursor position by indenting a copy of the cursor.
	 */
	public void setIndent() {
		indentOn = true;
		indentX = selectedX;
		indentY = selectedY;
	}
	
	/**
	 * Removes indent information.
	 */
	public void clearIndent() {
		indentOn = false;
	}
	
	/**
	 * Manually moves the cursor.
	 * @param	selected		The index of the character to move to
	 */
	public void setSelected(int selected) {
		selectedX = selected % cols;
		selectedY = (int) Math.floor((float) selected / (float) cols);
		updateCursor();
	}
	
	/**
	 * Manually moves the cursor to be over a certain character.
	 * @param	chara			The chara to select
	 */
	public void setSelected(Chara chara) {
		setSelected(charaToIndex(chara));
	}
	
	/**
	 * Animates the sprites from two inserts swapping places. For use with the
	 * party order selector screen. Performs both animation and order swap in
	 * the inserts, but not the swap within the party.
	 * @param	chara1			The index of the first character to move
	 * @param	chara2			The index of the second character to move
	 * @param	onFinish		The listener to call when done (or null)
	 */
	public void swap(final Chara chara1, final Chara chara2, final FinishListener onFinish) {
		final CharaInsert insert1 = charaToInsert(chara1);
		final CharaInsert insert2 = charaToInsert(chara2);
		float x1 = insert1.getSpriteX();
		float y1 = insert1.getSpriteY();
		float x2 = insert2.getSpriteX();
		float y2 = insert2.getSpriteY();
		float t = Math.abs((y1 - y2) / SWAP_VELOCITY);
		insert1.moveSprite(x2, y2, t, null);
		insert2.moveSprite(x1, y1, t, new FinishListener() {
			@Override public void onFinish() {
				CharaInsert firstInsert, secondInsert;
				int index1 = charaToIndex(chara1);
				int index2 = charaToIndex(chara2);
				if (index2 > index1) {
					firstInsert = insert1;
					secondInsert = insert2;
				} else {
					firstInsert = insert2;
					secondInsert = insert1;
					index1 = charaToIndex(chara2);
					index2 = charaToIndex(chara1);
				}
				orderedInserts.remove(insert1);
				orderedInserts.remove(insert2);
				orderedInserts.add(index1, secondInsert);
				orderedInserts.add(index2, firstInsert);
				float tempX = insert1.getX();
				float tempY = insert1.getY();
				insert1.setX(insert2.getX());
				insert1.setY(insert2.getY());
				insert2.setX(tempX);
				insert2.setY(tempY);
				if (onFinish != null) {
					onFinish.onFinish();
				}
			}
		});
	}
	
	/**
	 * Stops this menu from receiving input. It still displays on the screen.
	 */
	public void unfocus() {
		cursorOn = false;
		onHover = null;
		parent.removeCommandListener(this);
		parent.popCommandContext();
	}
	
	/**
	 * Resumes the menu for input reception. Should already be on screen.
	 */
	protected void focus() {
		parent = MGlobal.screens.peek();
		parent.pushCommandContext(new CMapMenu());
		parent.pushCommandListener(this);
	}
	
	/**
	 * Sets up the inserts. Called from the constructor?
	 */
	protected void createDisplay() {
		if (allInserts == null) {
			orderedInserts = new ArrayList<CharaInsert>();
			allInserts = new ArrayList<CharaInsert>();
		} else {
			for (CharaInsert insert : allInserts) {
				assets.remove(insert);
			}
			orderedInserts.clear();
			allInserts.clear();
		}
		
		float baseX = x + INSERTS_MARGIN;
		float baseY = y + insertsHeight - INSERTS_MARGIN*3/2 - getInsertHeight();
		int col = 0;
		for (Chara hero : party.getAll()) {
			CharaInsert insert;
			if (fullMode) {
				insert = new CharaInsertFull(hero, combatMode);
			} else {
				insert = new CharaInsertSmall(hero);
			}
			float insertX = baseX + (insert.getWidth() + padX) * col;
			float insertY = baseY;
			insert.setX(insertX);
			insert.setY(insertY);
			col += 1;
			if (col >= cols) {
				col = 0;
				baseY -= (insert.getHeight() + padY);
			}
			orderedInserts.add(insert);
			assets.add(insert);
		}
		allInserts.addAll(orderedInserts);
	}
	
	/**
	 * Updates the cursor position based on the selection variable.
	 */
	protected void updateCursor() {		
		if (onHover != null) {
			onHover.onSelection(getSelected());
		}
	}
	
	/**
	 * Moves the cursor vertically in the direction of the delta.
	 * @param	delta			The distance to move by
	 */
	protected void moveCursorVert(int delta) {
		selectedY += delta;
		if (selectedY < 0) {
			selectedY = (int) Math.floor(allInserts.size() / cols);
		}
		if (selectedY >= rows) {
			selectedY = 0;
		}
		while (selectedX + selectedY * cols >= allInserts.size()) {
			selectedY -= 1;
		}
		updateCursor();
	}
	
	/**
	 * Moves the cursor horizontally in the direction of the delta.
	 * @param	delta			The distance to move by
	 */
	protected void moveCursorHoriz(int delta) {
		selectedX += delta;
		if (selectedX < 0) {
			selectedX = cols - 1;
		}
		if (selectedX >= cols) {
			selectedX = 0;
		}
		while (selectedX + selectedY * cols >= allInserts.size()) {
			selectedX -= 1;
		}
		updateCursor();
	}
	
	/**
	 * Called when user cancels the selection process.
	 */
	protected void cancel() {
		if (cancellable) {
			handleSelectResponse(onSelect.onSelection(null));
		}
	}
	
	/**
	 * Called when the user confirms a character selection.
	 */
	protected void confirm() {
		handleSelectResponse(onSelect.onSelection(getSelected()));
	}
	
	/**
	 * Handles the response of the listener callback.
	 * @param	unfocus			True to unfocus the selector
	 */
	protected void handleSelectResponse(boolean unfocus) {
		if (unfocus) {
			unfocus();
		}
	}
	
	/**
	 * Returns the width of one insert based on full mode.
	 * @return					The width of an insert (in virtual px)
	 */
	protected int getInsertWidth() {
		return fullMode ? CharaInsertFull.WIDTH : CharaInsertSmall.WIDTH;
	}
	
	/**
	 * Returns the height of one insert based on full mode.
	 * @return					The height of an insert (in virtual px)
	 */
	protected int getInsertHeight() {
		return fullMode ? CharaInsertFull.HEIGHT : CharaInsertSmall.HEIGHT;
	}
	
	/**
	 * Calculates the selected character from the cursor coords.
	 * @return					The currently selected characters
	 */
	protected Chara getSelected() {
		int selected = selectedX + selectedY * cols;
		return orderedInserts.get(selected).getChara();
	}
	
	/**
	 * Renders that pointy figure thing at a given row/column.
	 * @param batch				The batch to render as part of
	 * @param col				The selected column
	 * @param row				The selected row
	 * @param off				The offset to add to final x-coord (for indent)
	 */
	protected void renderCursor(SpriteBatch batch, int col, int row, int off) {
		Graphic cursor = MGlobal.ui.getCursor();
		cursorX = x;
		cursorY = y + insertsHeight;
		cursorX += (getInsertWidth() + padX) * col;
		cursorY -= (getInsertHeight() + padY) * row;
		cursorY -= orderedInserts.get(0).getHeight() * 3 / 2;
		cursorY += (getInsertHeight() - cursor.getHeight()) / 2;
		cursorX -= (cursor.getWidth() / 2 + cursorSpace);
		MGlobal.ui.getCursor().renderAt(batch, cursorX + off, cursorY);
	}
	
	/**
	 * Returns the insert associated with the given character.
	 * @param	chara			The chara to get the insert for
	 * @return					The insert containing that chara
	 */
	protected CharaInsert charaToInsert(Chara chara) {
		int index = charaToIndex(chara);
		if (index >= 0) {
			return orderedInserts.get(index);
		} else {
			return null;
			
		}
	}
	
	/**
	 * Returns the index of the insert associated with the given character.
	 * @param	chara			The chara to get the insert for
	 * @return					The index of the insert containing that chara
	 */
	protected int charaToIndex(Chara chara) {
		for (int i = 0; i < orderedInserts.size(); i += 1) {
			if (orderedInserts.get(i).getChara() == chara) {
				return i;
			}
		}
		MGlobal.reporter.err("No insert index found for " + chara);
		return -1;
	}
	
	/**
	 * When the user selects a character, this gets called.
	 */
	public static abstract class SelectionListener {
		
		/**
		 * Called when the user selects a character.
		 * @param	selected		The character selected, or null for cancel
		 * @return					True if the selector should be unfocused
		 */
		public abstract boolean onSelection(Chara selected);
		
	}

}
