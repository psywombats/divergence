/**
 *  CharaInsertBG.java
 *  Created on Apr 23, 2014 1:58:56 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.graphics.ScreenGraphic;
import net.wombatrpgs.mgne.ui.Nineslice;

/**
 * A chara insert with a nineslice attached.
 */
public class CharaInsertBG extends ScreenGraphic {
	
	protected CharaInsert insert;
	protected Nineslice bg;
	protected int width, height;
	protected int padding;
	
	/**
	 * Creates a new insert with a background. The insert is preconstructed.
	 * Padding is applied in addition to nineslice border width.
	 * @param	insert			The insert to construct around
	 * @param	padding			The padding (in px) between insert and frame
	 */
	public CharaInsertBG(CharaInsert insert, int padding) {
		this.insert = insert;
		this.padding = padding;
		
		bg = new Nineslice();
		this.width = insert.getWidth() + (padding + bg.getBorderWidth()) * 2;
		this.height = insert.getHeight() + (padding + bg.getBorderHeight()) * 2;
		assets.add(bg);
	}
	
	/** @see net.wombatrpgs.mgne.graphics.ScreenGraphic#getWidth() */
	@Override public int getWidth() { return width; }

	/** @see net.wombatrpgs.mgne.graphics.ScreenGraphic#getHeight() */
	@Override public int getHeight() { return height; }
	
	/** @return The width of the nineslice border */
	public int getBorderWidth() { return bg.getBorderWidth(); }
	
	/** @return The height of the nineslice border */
	public int getBorderHeight() { return bg.getBorderHeight(); }

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		if (pass == 0) {
			bg.resizeTo(getWidth(), getHeight());
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#coreRender
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void coreRender(SpriteBatch batch) {
		bg.renderAt(batch, x, y);
		insert.setX(x + bg.getBorderWidth() + padding);
		insert.setY(y + bg.getBorderHeight() + padding);
		insert.render(batch);
	}

}
