/**
 *  RadioBox.java
 *  Created on Jan 22, 2015 8:47:59 PM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgne.ui.text.BlockingTextBox;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgneschema.ui.TextBoxMDO;

/**
 *
 */
public class RadioBox extends BlockingTextBox {
	
	Graphic backer;
	float h;

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
		
		if (expandingIn || expandingOut) {
			elapsedExpand += elapsed;
			float r = elapsedExpand / expandTime;
			if (r > 1) r = 1;
			if (expandingOut) r = 1f - r;
			backer.setTextureHeight((int) (h * r));
		}
		
		super.update(elapsed);
	}

	/**
	 * @see net.wombatrpgs.mgne.ui.text.TextBox#coreRender
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void coreRender(SpriteBatch batch) {
		backer.renderAt(batch, backer.getWidth()/2, screen.getHeight() - h / 2);
		super.coreRender(batch);
	}

	/**
	 * @see net.wombatrpgs.mgne.ui.text.TextBox#postProcessing(net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		h = backer.getHeight();
	}

}
