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
import net.wombatrpgs.mgne.io.CommandMap;
import net.wombatrpgs.mgne.io.command.CMapRaw;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgne.ui.Option;
import net.wombatrpgs.mgne.ui.OptionSelector;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextFormat;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.rpg.items.Inventory;
import net.wombatrpgs.saga.screen.TargetSelectable;

/**
 * Allows the user to select an ability from a list.
 */
public class ItemSelector extends ScreenGraphic implements CommandListener {
	
	protected static final int INDENT_SIZE = 4;
	
	protected Inventory inventory;
	protected CommandMap context;
	
	// layout
	protected Screen parent;
	protected TextFormat nameFormat, usesFormat, priceFormat;
	protected int width, height;
	protected int padding;
	protected int count;
	
	// cursor
	protected SlotListener listener, selectListener;
	protected boolean cursorOn;
	protected boolean cancellable;
	protected float cursorX, cursorY;
	protected int selected;
	
	// cursor indent
	protected boolean indentOn;
	protected float indentX, indentY;
	
	protected boolean battleOnly, showPrice;
	
	/**
	 * Creates a new selector for a given inventory.
	 * @param	inventory		The set of items to create for
	 * @param	count			The number of items to display at once
	 * @param	width			The width of the selector (in virt px)
	 * @param	padding			The vertical padding between items (in virt px)
	 * @param	battleOnly		True if only battle-useable items shown
	 * @param	showPrice		True to show the buy/sell value of the items
	 */
	public ItemSelector(Inventory inventory, int count, int width,
			int padding, boolean battleOnly, boolean showPrice) {
		this.inventory = inventory;
		this.width = width;
		this.padding = padding;
		this.count = count;
		this.battleOnly = battleOnly;
		this.showPrice = showPrice;
		
		FontHolder font = MGlobal.ui.getFont();
		height = (int) (count * font.getLineHeight());
		height += padding * (count-1);
		
		nameFormat = new TextFormat();
		nameFormat.align = HAlignment.LEFT;
		nameFormat.width = width;
		nameFormat.height = 240;
		
		usesFormat = new TextFormat();
		usesFormat.align = HAlignment.RIGHT;
		usesFormat.width = 16;
		usesFormat.height = 240;
		
		priceFormat = new TextFormat();
		priceFormat.align = HAlignment.RIGHT;
		priceFormat.width = 48;
		priceFormat.height = 240;
		
		context = new CMapRaw();
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
		
		FontHolder font = MGlobal.ui.getFont();
		for (int i = 0; i < inventory.slotCount(); i += 1) {
			CombatItem item = inventory.get(i);
			int offY = (int) (-i * (font.getLineHeight() + padding));
			if (shouldDisplayItem(item)) {
				font.draw(batch, nameFormat, item.getName(), offY);
				String uses = item.isUnlimited() ? "--" : String.valueOf(item.getUses());
				String price = item.isSellable() ? String.valueOf(item.getCost()) : "--";
				font.draw(batch, usesFormat, uses, offY);
				if (showPrice) {
					font.draw(batch, priceFormat, price, offY);
				}
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
		case RAW_UP:		moveCursor(-1);		return true;
		case RAW_DOWN:		moveCursor(1);		return true;
		case RAW_A:			confirm();			return true;
		case RAW_B:			cancel();			return true;
		case RAW_START:		cancel();			return true;
		case RAW_SELECT:	select();			return true;
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
		
		FontHolder font = MGlobal.ui.getFont();
		nameFormat.x = (int) x;
		nameFormat.y = (int) (y + height + font.getLineHeight());
		if (showPrice) {
			priceFormat.x = (int) (x + width - priceFormat.width );
			priceFormat.y = (int) (y + height + font.getLineHeight());
			usesFormat.x = priceFormat.x - (usesFormat.width + 8);
			usesFormat.y = (int) (y + height + font.getLineHeight());
		} else {
			usesFormat.x = (int) (x + width - usesFormat.width );
			usesFormat.y = (int) (y + height + font.getLineHeight());
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
		
		selected = findFirstSelectableSlot();
		updateCursor();
	}
	
	/**
	 * Also listens for when the user presses select on an item for some reason.
	 * @param	listener		The listener to call when a selection is made
	 */
	public void attachSelectListener(SlotListener listener) {
		this.selectListener = listener;
	}
	
	/**
	 * Prompts the user to use or discard the selected item.
	 * @param	screen			The screen that will be used for selecting chara
	 */
	public void useOrDiscard(final TargetSelectable screen) {
		final CombatItem item = inventory.get(selected);
		OptionSelector selector = new OptionSelector(true,
				new Option("Use") {
					@Override public boolean onSelect() {
						if (item.isMapUsable()) {
							item.onMapUse(screen);
							return true;
						} else {
							MGlobal.audio.playSFX(SConstants.SFX_FAIL);
							return false;
						}
					}
				},
				new Option("Drop") {
					@Override public boolean onSelect() {
						inventory.remove(selected);
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
		parent.removeCommandContext(context);
	}
	
	/**
	 * Resumes the menu for input reception. Should already be on screen.
	 */
	protected void focus() {
		clearIndent();
		parent = MGlobal.screens.peek();
		parent.pushCommandContext(context);
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
	 * Called when the weird user presses select on an item?
	 */
	protected void select() {
		handleListener(selectListener.onSelection(selected));
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
	 * Looks up the first item that can be used from this selection.
	 * @return					The index of the first usable item, if any
	 */
	protected int findFirstSelectableSlot() {
		for (int i = 0; i < inventory.slotCount(); i += 1) {
			CombatItem item = inventory.get(i);
			if (item == null) continue;
			if (!item.isBattleUsable() && battleOnly) continue;
			return i;
		}
		return 0; // oops?
	}
	
	/**
	 * Determines if a specific item should be selectable.
	 * @param	item			The item to check
	 * @return					True if it should be displayed
	 */
	protected boolean shouldDisplayItem(CombatItem item) {
		if (item == null) return false;
		if (battleOnly) {
			return item.isBattleUsable() && item.getUses() > 0;
		} else {
			return true;
		}
	}

}
