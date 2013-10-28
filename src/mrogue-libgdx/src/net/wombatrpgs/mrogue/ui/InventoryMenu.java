/**
 *  InventoryMenu.java
 *  Created on Oct 21, 2013 1:35:16 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.ui;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.rpg.item.FlatInventory;
import net.wombatrpgs.mrogue.rpg.item.FlatInventory.FlatItem;
import net.wombatrpgs.mrogue.ui.text.FontHolder;
import net.wombatrpgs.mrogue.ui.text.TextBoxFormat;
import net.wombatrpgs.mrogueschema.maps.data.OrthoDir;
import net.wombatrpgs.mrogueschema.ui.FontMDO;
import net.wombatrpgs.mrogueschema.ui.InventoryMenuMDO;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * The pull-out tab for item selection
 */
public class InventoryMenu extends Popup {
	
	public InventoryMenuMDO mdo;
	protected Graphic backer, highlight, tab;
	protected FontHolder font;
	protected TextBoxFormat amtFormat, descFormat;
	
	protected FlatInventory flats;
	int selected, maxSelect;
	int scroll, maxScroll;
	protected float x, y;
	
	/**
	 * Creates a new inventory menu from data.
	 * @param	mdo				The data to create from
	 */
	public InventoryMenu(InventoryMenuMDO mdo) {
		this.mdo = mdo;
		this.z = 2;
		backer = startGraphic(mdo.backer);
		highlight = startGraphic(mdo.highlight);
		tab = startGraphic(mdo.tab);
		font = new FontHolder(MGlobal.data.getEntryFor(mdo.font, FontMDO.class));
		assets.add(font);
		
		x = MGlobal.window.getWidth();
		y = 0;
		active = false;
		
		amtFormat = new TextBoxFormat();
		amtFormat.align = BitmapFont.HAlignment.CENTER;
		amtFormat.width = TEXT_WIDTH;
		amtFormat.height = TEXT_HEIGHT;
		descFormat = new TextBoxFormat();
		descFormat.align = BitmapFont.HAlignment.LEFT;
		descFormat.width = TEXT_WIDTH;
		descFormat.height = TEXT_HEIGHT;
	}
	
	/** @return True if this inventory menu is up on the screen */
	public boolean isDisplaying() { return active; }

	/**
	 * @see net.wombatrpgs.mrogue.ui.Popup#show()
	 */
	@Override
	public void show() {
		super.show();
		if (MGlobal.raveMode) return;
		flats = MGlobal.hero.getUnit().getInventory().flatten();
		selected = 0;
		scroll = 0;
		maxSelect = Math.min(flats.size(), mdo.itemCount);
		maxScroll = flats.size() - mdo.itemCount + 1;
		if (maxScroll < 1) maxScroll = 1;
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		backer.renderAt(getBatch(), x, y);
		if (flats != null) {
			for (int i = 0; i < maxSelect; i += 1) {
				int atX = (int) (x + mdo.itemStartX + mdo.itemPadding * i);
				int atY = mdo.itemStartY;
				FlatItem flat = flats.getFlat(i + scroll);
				if (flat != null) {
					flat.item.getIcon().renderAt(getBatch(), atX, atY);
					amtFormat.x = atX - TEXT_WIDTH / 2 + flat.item.getIcon().getWidth() / 2;
					amtFormat.y = atY - mdo.amyOffX;
					font.draw(getBatch(), amtFormat, "x"+flat.amt, 0);
					if (selected == i) {
						int hx = atX + flat.item.getIcon().getWidth()/2 - highlight.getWidth()/2;
						int hy = atY + flat.item.getIcon().getWidth()/2 - highlight.getWidth()/2;
						highlight.renderAt(getBatch(), hx, hy);
						descFormat.x = (int) (x + mdo.textOffX);
						descFormat.y = (int) (y + mdo.textOffY);
						String t1 = flat.item.getName() + " (x"+flat.amt+")";
						String t2 = flat.item.getDescription();
						font.draw(getBatch(), descFormat, t1, (int) font.getLineHeight());
						font.draw(getBatch(), descFormat, t2, 0);
					}
				}
			}
		}
		tab.renderAt(getBatch(), x - tab.getWidth(), y);
	}

	/**
	 * @see net.wombatrpgs.mrogue.ui.UIElement#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		float sign = active ? -1 : 1;
		x += sign * (elapsed / mdo.pullTime) * backer.getWidth();
		if (x < MGlobal.window.getWidth() - backer.getWidth()) {
			x = MGlobal.window.getWidth() - backer.getWidth();
		}
		if (x > MGlobal.window.getWidth()) {
			x = MGlobal.window.getWidth();
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.ui.Popup#onCursorMove
	 * (net.wombatrpgs.mrogueschema.maps.data.OrthoDir)
	 */
	@Override
	protected boolean onCursorMove(OrthoDir dir) {
		switch (dir) {
		case WEST:
			if (selected > 0) {
				selected -= 1;
			} else if (scroll > 0) {
				scroll -= 1;
			} else {
				scroll = maxScroll-1;
				selected = maxSelect - 1;
			}
			return true;
		case EAST:
			if (selected < maxSelect - 1) {
				selected += 1;
			} else if (scroll < maxScroll - 1) {
				scroll += 1;
			} else {
				scroll = 0;
				selected = 0;
			}
			return true;
		default:
			return false;
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.ui.Popup#confirm()
	 */
	@Override
	protected boolean confirm() {
		MGlobal.hero.actAndWait(flats.getFlat(scroll + selected).item);
		hide();
		return true;
	}

}
