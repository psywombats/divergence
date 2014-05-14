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
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.graphics.ScreenGraphic;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextboxFormat;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;
import net.wombatrpgs.saga.rpg.chara.Chara;

/**
 * Thing with walking character sprite and their HP. Subclassed for how much
 * info shown. Does not own any assets.
 */
public abstract class CharaInsert extends ScreenGraphic {
	
	protected final static int PADDING = 2;
	
	protected TextboxFormat format;
	protected Chara chara;
	
	protected float spriteX, spriteY;
	
	protected boolean tracking;
	protected FinishListener finishListener;
	protected float origX, origY;
	protected float targetX, targetY;
	protected float moveTime, since;
	
	/**
	 * Creates a new insert for a character.
	 * @param	chara			The character to link to
	 */
	public CharaInsert(Chara chara) {
		this.chara = chara;
		format = new TextboxFormat();
		format.align = HAlignment.LEFT;
		refresh();
	}
	
	/** @return The character associated with this insert */
	public Chara getChara() { return chara; }
	
	/** @return The x-coord the sprite is rendered at (in scr px) */
	public float getSpriteX() { return spriteX; }
	
	/** @return The y-coord the sprite is rendered at (in scr px) */
	public float getSpriteY() { return spriteY; }
	
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
		if (!chara.getAppearance().isMoving() && chara.isAlive()) {
			chara.getAppearance().startMoving();
		}
		chara.getAppearance().update(elapsed);
		
		if (tracking) {
			since += elapsed;
			if (since < moveTime) {
				float r = since / moveTime;
				spriteX = targetX * r + (1f - r) * origX;
				spriteY = targetY * r + (1f - r) * origY;
			} else {
				spriteX = targetX;
				spriteY = targetY;
				tracking = false;
				if (finishListener != null) {
					finishListener.onFinish();
				}
			}
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#coreRender
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public final void coreRender(SpriteBatch batch) {
		FacesAnimation sprite = chara.getAppearance();
		sprite.renderAt(batch, spriteX, spriteY);
		renderInserts(batch);
	}

	/**
	 * Call to reconstruct the display and font positions.
	 */
	public final void refresh() {
		FontHolder font = MGlobal.ui.getFont();
		format.x = (int) (x + PADDING*2 + chara.getAppearance().getWidth());
		format.y = (int) (y + font.getLineHeight()*2);
		if (chara.isDead()) {
			chara.getAppearance().setFacing(OrthoDir.NORTH);
			chara.getAppearance().stopMoving();
		}
		spriteX = x + PADDING;
		spriteY = y + getHeight()/2 - chara.getAppearance().getHeight()/2;
		coreRefresh();
	}
	
	/**
	 * Animates the sprite of this character moving to given screen location.
	 * @param	x				The x-coord to move the sprite to (in screen px)
	 * @param	y				The y-coord to move the sprite to (in screen px)
	 * @param	time			How long the transition should take (in s)
	 * @param	onFinish		The listener to call when done, or null
	 */
	public void moveSprite(float x, float y, float time, FinishListener onFinish) {
		this.targetX = x;
		this.targetY = y;
		this.moveTime = time;
		this.finishListener = onFinish;
		
		tracking = true;
		since = 0;
		origX = spriteX;
		origY = spriteY;
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
