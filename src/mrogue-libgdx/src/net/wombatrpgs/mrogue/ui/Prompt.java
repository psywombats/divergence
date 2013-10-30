/**
 *  Prompt.java
 *  Created on Oct 25, 2013 10:33:51 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.screen.WindowSettings;
import net.wombatrpgs.mrogue.ui.text.FontHolder;
import net.wombatrpgs.mrogue.ui.text.TextBoxFormat;
import net.wombatrpgs.mrogueschema.maps.data.OrthoDir;
import net.wombatrpgs.mrogueschema.ui.PromptMDO;

/**
 * Binary selection box.
 */
public class Prompt extends Popup {
	
	protected PromptMDO mdo;
	protected Graphic backer, cursor;
	protected TextBoxFormat promptFormat;
	protected TextBoxFormat optFormat1, optFormat2;
	protected BinaryChoiceResultListener listener;
	protected boolean defaultSelected;
	protected float alpha;
	
	/**
	 * Creates a new prompt from data.
	 * @param	mdo				The data to use
	 */
	public Prompt(PromptMDO mdo) {
		this.mdo = mdo;
		defaultSelected = true;
		z = 90;
		backer = startGraphic(mdo.backer);
		cursor = startGraphic(mdo.cursor);
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.ScreenObject#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		Color old = getBatch().getColor();
		getBatch().setColor(1, 1, 1, alpha);	
		WindowSettings win = MGlobal.window;
		FontHolder font = MGlobal.ui.getFont();
		
		int backerX = win.getWidth()/2 - backer.getWidth()/2;
		int backerY = win.getHeight()/2 - backer.getHeight()/2;
		backer.renderAt(getBatch(), backerX, backerY);
		
		font.draw(getBatch(), promptFormat, mdo.prompt, 0);
		font.draw(getBatch(), optFormat1, mdo.choice1, 0);
		font.draw(getBatch(), optFormat2, mdo.choice2, 0);
		
		int cursorX;
		int cursorY = (int) (backerY + backer.getHeight()*1/3 -
				cursor.getHeight()/2 - font.getLineHeight()/2);
		if (defaultSelected) {
			cursorX = backerX + backer.getWidth()*1/3 - cursor.getWidth()/2;
		} else {
			cursorX = backerX + backer.getWidth()*2/3 - cursor.getWidth()/2;
		}
		getBatch().setColor(1, 1, 1, alpha/2);
		cursor.renderAt(getBatch(), cursorX, cursorY);
		
		getBatch().setColor(old);
		
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.ScreenObject#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		
		WindowSettings win = MGlobal.window;
		int backerX = win.getWidth()/2 - backer.getWidth()/2;
		int backerY = win.getHeight()/2 - backer.getHeight()/2;
		
		promptFormat = new TextBoxFormat();
		promptFormat.align = HAlignment.CENTER;
		promptFormat.x = backerX + backer.getWidth()/2 - TEXT_WIDTH/2;
		promptFormat.y = backerY + backer.getHeight()*2/3;
		promptFormat.width = TEXT_WIDTH;
		promptFormat.height = TEXT_HEIGHT;
		
		optFormat1 = new TextBoxFormat();
		optFormat1.align = HAlignment.CENTER;
		optFormat1.x = backerX + backer.getWidth()/3 - TEXT_WIDTH/2;
		optFormat1.y = backerY + backer.getHeight()*1/3;
		optFormat1.width = TEXT_WIDTH;
		optFormat1.height = TEXT_HEIGHT;
		
		optFormat2 = new TextBoxFormat();
		optFormat2.align = HAlignment.CENTER;
		optFormat2.x = backerX + backer.getWidth()*2/3 - TEXT_WIDTH/2;
		optFormat2.y = backerY + backer.getHeight()*1/3;
		optFormat2.width = TEXT_WIDTH;
		optFormat2.height = TEXT_HEIGHT;
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.ScreenObject#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (active) {
			alpha = Math.min(1, alpha + elapsed / .2f);
		} else {
			alpha -= elapsed / .2f;
			if (alpha <= 0) {
				MGlobal.screens.peek().removeObject(this);
			}
		}
	}
	
	/**
	 * Displays the prompt and asks the user what they think. The result is one
	 * of the two provided options. True is for the left value (default) and
	 * false is for the right value (option).
	 * @param	listener			Who to notify when finished
	 */
	public void ask(BinaryChoiceResultListener listener) {
		MGlobal.screens.peek().addObject(this);
		this.listener = listener;
		show();
	}

	/**
	 * @see net.wombatrpgs.mrogue.ui.Popup#onCursorMove
	 * (net.wombatrpgs.mrogueschema.maps.data.OrthoDir)
	 */
	@Override
	protected boolean onCursorMove(OrthoDir dir) {
		defaultSelected = !defaultSelected;
		return true;
	}

	/**
	 * @see net.wombatrpgs.mrogue.ui.Popup#cancel()
	 */
	@Override
	protected boolean cancel() {
		listener.onDecision(BinaryChoice.CANCEL);
		return super.cancel();
	}

	/**
	 * @see net.wombatrpgs.mrogue.ui.Popup#confirm()
	 */
	@Override
	protected boolean confirm() {
		listener.onDecision(defaultSelected ? BinaryChoice.OPTION_1 : BinaryChoice.OPTION_2);
		hide();
		return true;
	}
	
}
