/**
 *  CharaSelector.java
 *  Created on Apr 6, 2014 3:56:39 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
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
public class CharaSelector extends ScreenGraphic implements CommandListener {
	
	protected Screen parent;
	protected Party party;
	
	// layout
	protected static final int INSERTS_MARGIN = 5;
	protected static final int DEFAULT_COLUMNS = 2;
	protected static final int DEFAULT_ROWS = 3;
	
	// inserts
	protected int cols, rows;
	protected float paddingFudge;
	protected boolean fullMode, combatMode, showBG;
	protected int insertsWidth, insertsHeight;
	protected List<CharaInsert> inserts;
	protected Nineslice bg;
	
	// cursor
	protected boolean cursorOn, indentOn;
	protected boolean cancellable;
	protected int selectedX, selectedY;
	protected int indentX, indentY;
	protected float cursorX, cursorY;
	protected SelectionListener onSelect, onHover;
	
	/**
	 * Creates a new character selector with a custom party.
	 * @param	fullMode		True to use large version
	 * @param	combatMode		True to use combat status and not race etc
	 * @param	showBG			True to use the automatic nineslice bg
	 * @param	padding			The horizontal floating fudge... ugly
	 * @param	cols			The number of columns of inserts
	 * @param	rows			The number of rows of inserts
	 */
	public CharaSelector(Party party, boolean fullMode, boolean combatMode,
			boolean showBG, float padding, int cols, int rows) {
		this.party = party;
		this.fullMode = fullMode;
		this.combatMode = combatMode;
		this.showBG = showBG;
		this.paddingFudge = padding;
		this.cols = cols;
		this.rows = rows;
		insertsWidth = getInsertWidth();
		insertsHeight = getInsertHeight();
		insertsWidth *= cols;
		insertsHeight *= rows;
		insertsWidth += 2 * INSERTS_MARGIN;
		insertsHeight += 2 * (INSERTS_MARGIN+2);
		insertsWidth += padding * (cols-1);
		
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
		this(party, fullMode, combatMode, showBG, padding,
				DEFAULT_COLUMNS, DEFAULT_ROWS);
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
		this(SGlobal.heroes, full, combat, true, 0, cols, rows);
	}
	
	/**
	 * Creates a new character selector for the hero party, showing bg, no pad.
	 * @param	fullMode		True to use large version
	 * @param	combatMode		True to use combat status and not race etc
	 * @param	showBG			True to show the nineslice bg default
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
		for (CharaInsert insert : inserts) {
			insert.setX(insert.getX() + x - this.x);
		}
		super.setX(x);
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#setY(float)
	 */
	@Override
	public void setY(float y) {
		for (CharaInsert insert : inserts) {
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
		for (CharaInsert insert : inserts) {
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
		for (CharaInsert insert : inserts) {
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
		for (CharaInsert insert : inserts) {
			insert.render(batch);
		}
		if (cursorOn) {
			Graphic cursor = MGlobal.ui.getCursor();
			cursorX = x;
			cursorY = y + insertsHeight;
			cursorX += (getInsertWidth() + paddingFudge) * selectedX;
			cursorY -= getInsertHeight() * selectedY;
			cursorY -= inserts.get(0).getHeight() * 3 / 2;
			cursorY += (getInsertHeight() - cursor.getHeight()) / 2;
			cursorX -= (cursor.getWidth() / 2 - 3);
			MGlobal.ui.getCursor().renderAt(batch, cursorX, cursorY);
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
		indentOn = false;
		selectedX = 0;
		selectedY = 0;
		updateCursor();
	}
	
	/**
	 * Updates the inserts to reflect health, status, etc.
	 */
	public void refresh() {
		for (CharaInsert insert : inserts) {
			insert.refresh();
		}
	}
	
	/**
	 * Marks the selected cursor position by indenting a copy of the cursor.
	 */
	public void setIndent(int selected) {
		
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
		if (inserts == null) {
			inserts = new ArrayList<CharaInsert>();
		} else {
			for (CharaInsert insert : inserts) {
				assets.remove(insert);
			}
			inserts.clear();
		}
		
		float insertX = x + INSERTS_MARGIN;
		float insertY = y + insertsHeight - INSERTS_MARGIN*3/2 - getInsertHeight();
		boolean left = true;
		for (Chara hero : party.getAll()) {
			CharaInsert insert;
			if (fullMode) {
				insert = new CharaInsertFull(hero, combatMode);
			} else {
				insert = new CharaInsertSmall(hero);
			}
			insert.setX(insertX);
			insert.setY(insertY);
			if (left) {
				insertX += (insert.getWidth() + paddingFudge);
			} else {
				insertX -= (insert.getWidth() + paddingFudge);
				insertY -= insert.getHeight();
			}
			left = !left;
			inserts.add(insert);
			assets.add(insert);
		}
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
			selectedY = (int) Math.floor(inserts.size() / cols);
		}
		if (selectedY >= rows) {
			selectedY = 0;
		}
		while (selectedX + selectedY * cols >= inserts.size()) {
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
		while (selectedX + selectedY * cols >= inserts.size()) {
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
		return inserts.get(selected).getChara();
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
