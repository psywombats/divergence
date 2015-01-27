/**
 *  InventoryMenu.java
 *  Created on Oct 21, 2013 1:35:16 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */

package net.wombatrpgs.bacon01.ui;

import net.wombatrpgs.bacon01.core.BGlobal;
import net.wombatrpgs.bacon01.rpg.Inventory;
import net.wombatrpgs.bacon01.rpg.InventoryItem;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.io.CommandMap;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.ScreenObject;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextFormat;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The pull-out tab for item selection
 */
public class InventoryUI extends ScreenObject implements CommandListener {
	
	protected static final int TEXT_WIDTH = 512;
	protected static final int TEXT_HEIGHT = 64;
	
	protected static final int ITEM_COUNT = 6;
	
	protected static final int ITEM_START_X = 28;
	protected static final int ITEM_START_Y = 20;
	protected static final int ITEM_PADDING = 32;
	
	protected static final int AMT_OFF_X = 6;
	
	protected static final int TEXT_OFF_X = 12;
	protected static final int TEXT_OFF_Y = 20;
	
	protected static final float PULL_TIME = .5f;
	
	protected Graphic backer, highlight, tab;
	protected FontHolder font;
	protected TextFormat amtFormat, descFormat;
	protected CommandMap context;
	
	protected Inventory items;
	protected boolean active;
	int selected, maxSelect;
	int scroll, maxScroll;
	protected float x, y;
	
	/**
	 * Creates a new inventory menu from nothing. We hack-assume an mdo.
	 */
	public InventoryUI() {
		backer = startGraphic("inventory_backer.png");
		highlight = startGraphic("inventory_highlight.png");
		tab = startGraphic("inventory_tab.png");
		font = MGlobal.ui.getFont();
		
		amtFormat = new TextFormat();
		amtFormat.align = BitmapFont.HAlignment.CENTER;
		amtFormat.width = TEXT_WIDTH;
		amtFormat.height = TEXT_HEIGHT;
		descFormat = new TextFormat();
		descFormat.align = BitmapFont.HAlignment.LEFT;
		descFormat.width = TEXT_WIDTH;
		descFormat.height = TEXT_HEIGHT;
		
		context = new CMapMenu();
	}

	@Override public int getWidth() {
		return backer.getWidth();
	}


	@Override public int getHeight() {
		return backer.getHeight();
	}

	public void show() {
		MGlobal.screens.peek().pushCommandContext(context);
		MGlobal.screens.peek().pushCommandListener(this);
		MGlobal.getHero().pause(true);
		active = true;
		items = BGlobal.items;
		selected = 0;
		scroll = 0;
		maxSelect = Math.min(items.size(), ITEM_COUNT);
		maxScroll = items.count() - ITEM_COUNT + 1;
		if (maxScroll < 1) maxScroll = 1;
	}
	
	public void hide() {
		MGlobal.screens.peek().removeCommandContext(context);
		MGlobal.screens.peek().removeCommandListener(this);
		MGlobal.getHero().pause(false);
		active = false;
	}

	/**
	 * @see net.wombatrpgs.mgne.core.AssetQueuer#postProcessing(net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		x = MGlobal.window.getViewportWidth()+backer.getWidth()/2;
		y = backer.getHeight()/2;
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.ScreenObject#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch viewBatch) {
		SpriteBatch batch = MGlobal.screens.peek().getUIBatch();
		backer.renderAt(batch, x, y);
		for (int i = 0; i < maxSelect; i += 1) {
			int atX = (int) (x - backer.getWidth()/2 + ITEM_START_X + ITEM_PADDING * i);
			int atY = ITEM_START_Y;
			InventoryItem item = items.at(i + scroll);
			if (item != null) {
				item.getIcon().renderAt(batch, atX, atY);
				amtFormat.x = atX - TEXT_WIDTH / 2 + item.getIcon().getWidth() / 2 - 1;
				amtFormat.y = atY - AMT_OFF_X;
				font.draw(batch, amtFormat, "x"+ item.getQuantity(), 0);
				if (selected == i) {
					int hx = atX;
					int hy = atY;
					highlight.renderAt(batch, hx, hy);
					descFormat.x = (int) (x - backer.getWidth()/2 + TEXT_OFF_X);
					descFormat.y = (int) (y + TEXT_OFF_Y);
					String t1 = item.getName() + " (x"+item.getQuantity()+")";
					String t2 = item.getDescription();
					font.draw(batch, descFormat, t1, (int) font.getLineHeight());
					font.draw(batch, descFormat, t2, 0);
				}
			}
		}
		tab.renderAt(batch, x - tab.getWidth()/2 - backer.getWidth()/2, y);
	}


	/**
	 * @see net.wombatrpgs.mrogue.ui.UIElement#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		float sign = active ? -1 : 1;
		x += sign * (elapsed / PULL_TIME) * backer.getWidth();
		if (x < MGlobal.window.getViewportWidth() - backer.getWidth()/2) {
			x = MGlobal.window.getViewportWidth() - backer.getWidth()/2;
		}
		if (x > MGlobal.window.getViewportWidth()+backer.getWidth()/2) {
			x = MGlobal.window.getViewportWidth()+backer.getWidth()/2;
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		switch (command) {
		case MOVE_LEFT:
			if (selected > 0) {
				selected -= 1;
			} else if (scroll > 0) {
				scroll -= 1;
			} else {
				scroll = maxScroll-1;
				selected = maxSelect - 1;
			}
			return true;
		case MOVE_RIGHT:
			if (selected < maxSelect - 1) {
				selected += 1;
			} else if (scroll < maxScroll - 1) {
				scroll += 1;
			} else {
				scroll = 0;
				selected = 0;
			}
			return true;
		case UI_CANCEL:
			hide();
			return true;
		case UI_CONFIRM:
			items.at(selected + scroll).onUse();
			return true;
		default:
			return false;
		}
	}

}
