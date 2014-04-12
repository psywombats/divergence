/**
 *  AbilSelector.java
 *  Created on Apr 12, 2014 2:47:33 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.ScreenGraphic;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextBoxFormat;
import net.wombatrpgs.saga.rpg.Chara;
import net.wombatrpgs.saga.rpg.CharaInventory;
import net.wombatrpgs.saga.rpg.CombatItem;

/**
 * Allows the user to select an ability from a list.
 */
//TODO: ui: scrolling capabilities of abilselector
public class AbilSelector extends ScreenGraphic {
	
	protected CharaInventory inventory;
	
	protected TextBoxFormat format, usesFormat;
	protected Nineslice bg;
	protected int width, height;
	protected int padding;
	
	/**
	 * Creates a new selector for a given character.
	 * @param	chara			The character to create for
	 * @param	count			The number of items to display at once
	 * @param	width			The width of the selector (in virt px)
	 * @param	padding			The vertical padding between items (in virt px)
	 * @param	useBG			True to use a nineslice bg, false for none
	 */
	public AbilSelector(Chara chara, int count, int width, int padding, boolean useBG) {
		this.inventory = chara.getInventory();
		this.width = width;
		this.padding = padding;
		
		FontHolder font = MGlobal.ui.getFont();
		height = (int) (count * font.getLineHeight());
		height += padding * (count-1);
		
		format = new TextBoxFormat();
		format.align = HAlignment.LEFT;
		format.width = width;
		format.height = 16;
		
		usesFormat = new TextBoxFormat();
		usesFormat.align = HAlignment.RIGHT;
		usesFormat.width = 16;
		usesFormat.height = 16;
		
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
		for (int i = 0; i < CharaInventory.SLOT_COUNT; i += 1) {
			CombatItem item = inventory.at(i);
			int offY = (int) (i * (font.getLineHeight() + padding));
			if (item != null) {
				font.draw(batch, format, item.getName(), offY);
				String uses = item.isUnlimited() ? "--" : String.valueOf(item.getUses());
				font.draw(batch, usesFormat, uses, offY);
			} else if (!inventory.equippableAt(i)) {
				font.draw(batch, format, "-", offY);
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.ScreenObject#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		if (pass == 0 && bg != null) {
			bg.resizeTo(width, height);
		}
		
		FontHolder font = MGlobal.ui.getFont();
		format.x = (int) x;
		format.y = (int) (y + height - font.getLineHeight());
		usesFormat.x = (int) (x + width - usesFormat.width );
		usesFormat.y = (int) (y + height - font.getLineHeight());
		if (bg != null) {
			format.y -= bg.getBorderHeight();
			usesFormat.y -= bg.getBorderHeight();
			usesFormat.x -= bg.getBorderWidth();
		}
	}

}
