/**
 *  CharaInsert.java
 *  Created on Apr 11, 2014 9:36:44 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.graphics.ScreenGraphic;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextBoxFormat;
import net.wombatrpgs.saga.rpg.Chara;

/**
 * Thing with walking character sprite and their HP. Subclassed for how much
 * info shown. Does not own any assets.
 */
public abstract class CharaInsert extends ScreenGraphic {
	
	protected final static int PADDING = 2;
	
	protected TextBoxFormat format;
	protected Chara chara;
	
	/**
	 * Creates a new insert for a character.
	 * @param	chara			The character to link to
	 */
	public CharaInsert(Chara chara) {
		this.chara = chara;
		format = new TextBoxFormat();
		format.align = HAlignment.LEFT;
		refresh();
	}
	
	/** @return The character associated with this insert */
	public Chara getChara() { return chara; }
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#setX(float)
	 */
	@Override
	public void setX(float x) {
		super.setX(x);
		refresh();
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#setY(float)
	 */
	@Override
	public void setY(float y) {
		super.setY(y);
		refresh();
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (!chara.getAppearance().isMoving()) {
			chara.getAppearance().startMoving();
		}
		chara.getAppearance().update(elapsed);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#coreRender
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public final void coreRender(SpriteBatch batch) {
		FacesAnimation sprite = chara.getAppearance();
		float renderX = x + PADDING;
		float renderY = y + getHeight()/2 - sprite.getHeight()/2;
		sprite.renderAt(batch, renderX, renderY);
		renderInserts(batch);
	}

	/**
	 * Call to reconstruct the display and font positions.
	 */
	public final void refresh() {
		FontHolder font = MGlobal.ui.getFont();
		format.x = (int) (x + PADDING*2 + chara.getAppearance().getWidth());
		format.y = (int) (y + font.getLineHeight()*2);
		coreRefresh();
	}
	
	/**
	 * Construct the display based on update information from the chara.
	 */
	protected abstract void coreRefresh();
	
	/**
	 * Render the actual insert text with the supplied batch.
	 * @param	batch			The batch to render with
	 */
	protected abstract void renderInserts(SpriteBatch batch);

}
