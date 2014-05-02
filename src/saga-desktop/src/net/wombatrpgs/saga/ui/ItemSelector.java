/**
 *  AbilSelector.java
 *  Created on Apr 12, 2014 2:47:33 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.ScreenGraphic;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.Option;
import net.wombatrpgs.mgne.ui.OptionSelector;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextBoxFormat;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.saga.rpg.CombatItem;
import net.wombatrpgs.saga.rpg.chara.Inventory;

/**
 * Allows the user to select an ability from a list.
 */
// TODO: ui: scrolling capabilities of abilselector
public class ItemSelector extends ScreenGraphic implements CommandListener {
	
	protected static final int INDENT_SIZE = 4;
	
	protected Inventory inventory;
	
	// layout
	protected Screen parent;
	protected TextBoxFormat format, usesFormat;
	protected Nineslice bg;
	protected int width, height, padding;
	protected int count;
	
	// cursor
	protected boolean cursorOn;
	protected boolean cancellable;
	protected float cursorX, cursorY;
	protected int selected;
	protected SlotListener listener;
	
	// cursor indent
	protected boolean indentOn;
	protected float indentX, indentY;
	
	protected boolean battleOnly;
	
	/**
	 * Creates a new selector for a given inventory.
	 * @param	inventory		The set of items to create for
	 * @param	count			The number of items to display at once
	 * @param	width			The width of the selector (in virt px)
	 * @param	padding			The vertical padding between items (in virt px)
	 * @param	useBG			True to use a nineslice bg, false for none
	 * @para	battleOnly		True if only battle-useable items shown
	 */
	public ItemSelector(Inventory inventory, int count, int width,
			int padding, boolean useBG, boolean battleOnly) {
		this.inventory = inventory;
		this.width = width;
		this.padding = padding;
		this.count = count;
		this.battleOnly = battleOnly;
		
		FontHolder font = MGlobal.ui.getFont();
		height = (int) (count * font.getLineHeight());
		height += padding * (count-1);
		
		format = new TextBoxFormat();
		format.align = HAlignment.LEFT;
		format.width = width;
		format.height = 240;
		
		usesFormat = new TextBoxFormat();
		usesFormat.align = HAlignment.RIGHT;
		usesFormat.width = 16;
		usesFormat.height = 240;
		
		if (useBG) {
			bg = new Nineslice();
			assets.add(bg);
			height += bg.getBorderHeight();
			format.width -= bg.getBorderWidth() * 2;
		}
	}

	/** @see net.wombatrpgs.mgne.graphics.ScreenGraphic#getWidth() */
	@Override public int getWidth() { return width; }

	/** @see net.wombatrpgs.mgne.graphics.ScreenGraphic#getHeight() */
	@Override public int getHeight() { return height; }

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#coreRender
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void coreRender(SpriteBatch batch) {
		if (bg != null) {
			bg.renderAt(batch, x, y);
		}
		
		FontHolder font = MGlobal.ui.getFont();
		for (int i = 0; i < inventory.slotCount(); i += 1) {
			CombatItem item = inventory.get(i);
			int offY = (int) (-i * (font.getLineHeight() + padding));
			if (item != null && (!battleOnly || item.isBattleUsable())) {
				font.draw(batch, format, item.getName(), offY);
				String uses = item.isUnlimited() ? "--" : String.valueOf(item.getUses());
				font.draw(batch, usesFormat, uses, offY);
			}
		}
		
		Graphic cursor = MGlobal.ui.getCursor();
		if (indentOn) {
			cursor.renderAt(batch, indentX, indentY);
		}
		if (cursorOn) {
			cursor.renderAt(batch, cursorX, cursorY);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		switch (command) {
		case MOVE_UP:		moveCursor(-1);		return true;
		case MOVE_DOWN:		moveCursor(1);		return true;
		case UI_CANCEL:		cancel();			return true;
		case UI_CONFIRM:	confirm();			return true;
		default:								return true;
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.ScreenObject#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		if (pass == 0 && bg != null) {
			bg.resizeTo(width, height);
		}
		
		FontHolder font = MGlobal.ui.getFont();
		format.x = (int) x;
		format.y = (int) (y + height + font.getLineHeight());
		usesFormat.x = (int) (x + width - usesFormat.width );
		usesFormat.y = (int) (y + height + font.getLineHeight());
		if (bg != null) {
			format.y -= bg.getBorderHeight();
			usesFormat.y -= bg.getBorderHeight();
			usesFormat.x -= bg.getBorderWidth();
		}
	}
	
	/**
	 * Sets the selected slot to be whatever and updates cursor position.
	 * @param	slot			The new slot to select
	 * @return					The old selected slot
	 */
	public int setSelected(int slot) {
		int old = selected;
		selected = slot;
		updateCursor();
		return old;
	}
	
	/**
	 * Marks the selected cursor position by indenting a copy of the cursor.
	 */
	public void setIndent() {
		indentOn = true;
		indentX = cursorX + INDENT_SIZE;
		indentY = cursorY;
	}
	
	/**
	 * Removes the indented cursor copy.
	 */
	public void clearIndent() {
		indentOn = false;
	}
	
	/**
	 * Waits for the user to select a slot then returns to the listener.
	 * @param	listener		The callback for selection
	 * @param	canCancel		True if the user can cancel to select nobody
	 */
	public void awaitSelection(SlotListener listener, boolean canCancel) {
		this.listener = listener;
		this.cancellable = canCancel;
		focus();
		cursorOn = true;
		
		selected = 0;
		if (battleOnly) {
			while (!inventory.get(selected).isBattleUsable() &&
					selected < inventory.slotCount()) {
				selected += 1;
			}
		}
		updateCursor();
	}
	
	/**
	 * Prompts the user to use or discard the selected item.
	 */
	public void useOrDiscard() {
		final ItemSelector parent = this;
		final CombatItem item = inventory.get(selected);
		OptionSelector selector = new OptionSelector(true,
				new Option("Use") {
					@Override public boolean onSelect() {
						if (item.isMapUsable()) {
							item.onMapUse(parent);
							return true;
						} else {
							// TODO: sfx: failure sound
							return false;
						}
					}
				},
				new Option("Drop") {
					@Override public boolean onSelect() {
						inventory.drop(selected);
						return true;
					}
				});
		selector.setCancellable(true);
		selector.showAt((int) cursorX, (int) cursorY); 
	}
	
	/**
	 * Stops this menu from receiving input. It still displays on the screen.
	 */
	public void unfocus() {
		cursorOn = false;
		parent.removeCommandListener(this);
		parent.popCommandContext();
	}
	
	/**
	 * Resumes the menu for input reception. Should already be on screen.
	 */
	protected void focus() {
		clearIndent();
		parent = MGlobal.screens.peek();
		parent.pushCommandContext(new CMapMenu());
		parent.pushCommandListener(this);
	}
	
	/**
	 * Called when user cancels the selection process.
	 */
	protected void cancel() {
		if (cancellable) {
			handleListener(listener.onSelection(-1));
		}
	}
	
	/**
	 * Called when the user confirms a character selection.
	 */
	protected void confirm() {
		handleListener(listener.onSelection(selected));
	}
	
	/**
	 * Moves the cursor by the delta.
	 * @param	delta			The delta to move the cursor by
	 */
	protected void moveCursor(int delta) {
		selected += delta;
		if (selected < 0) selected = count - 1;
		if (selected >= count) selected = 0;
		updateCursor();
	}
	
	/**
	 * Updates the cursor's position.
	 */
	protected void updateCursor() {
		Graphic cursor = MGlobal.ui.getCursor();
		FontHolder font = MGlobal.ui.getFont();
		cursorX = x - cursor.getWidth() - 3;
		cursorY = y + height - (selected * (font.getLineHeight() + padding) + cursor.getHeight()/2);
	}
	
	/**
	 * Handles the result from the callback function.
	 * @param	result			True to unfocus the listener
	 */
	protected void handleListener(boolean result) {
		if (result) {
			unfocus();
		}
	}
	
	/**
	 * Listener for selecting an ability.
	 */
	public static abstract class SlotListener {
		
		/**
		 * Called when the user selects a slot. Returns negative if cancelled.
		 * @param	selected		The slot that was selected, -1 for cancel
		 * @return					True to unfocus the selector
		 */
		public abstract boolean onSelection(int selected);
	}

}
