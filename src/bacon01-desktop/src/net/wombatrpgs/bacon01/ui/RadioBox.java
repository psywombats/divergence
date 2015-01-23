/**
 *  RadioBox.java
 *  Created on Jan 22, 2015 8:47:59 PM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgne.ui.text.BlockingTextBox;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgneschema.ui.TextBoxMDO;

/**
 *
 */
public class RadioBox extends BlockingTextBox {
	
	Graphic backer;

	public RadioBox(TextBoxMDO mdo, FontHolder font) {
		super(mdo, font);
		backer = new Graphic("backer.png");
		assets.add(backer);
	}

	/**
	 * @see net.wombatrpgs.mgne.ui.text.BlockingTextBox#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		
		if (expandingIn || expandingOut) {
			elapsedExpand += elapsed;
			float r = elapsedExpand / expandTime;
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.ui.text.TextBox#coreRender
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void coreRender(SpriteBatch batch) {
		backer.renderAt(batch, 0, screen.getHeight() - backer.getHeight());
		super.coreRender(batch);
	}

}
