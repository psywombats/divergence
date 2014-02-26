/**
 *  OptionSelector.java
 *  Created on Feb 18, 2014 11:50:36 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.ui;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextBoxFormat;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.ui.NinesliceMDO;

/**
 * The spiritual successor to the old menu from 7DRL. This thing displays a list
 * of options and a cursor, and the player selects one of the options.
 */
public class OptionSelector extends UIElement implements CommandListener {
	
	protected static float DEFAULT_PADDING_HORIZ = 24;
	protected static float DEFAULT_PADDING_VERT = 10;
	protected static float DEFAULT_SPACING = 5;
	
	// from constructor
	protected List<Option> options;
	protected NinesliceMDO bgMDO;
	protected FontHolder font;
	protected float padHoriz, padVert;
	protected float spacingVert;
	
	// calculated
	protected Nineslice bg;
	protected TextBoxFormat format;
	protected int screenX, screenY;
	protected float width, height;
	
	// cursor
	protected boolean cursorOn;
	protected int selected;
	protected float cursorX, cursorY;
	
	// etc state
	protected boolean controlling;
	
	/**
	 * Creates a fully custom options selector!
	 * @param	options			The options the player can select from
	 * @param	bg				The background nineslice to use
	 * @param	font			The font to render option text with
	 * @param	padLeft			The space (in pixels) from left border
	 * @param	padRight		The space (in pixels) from right border
	 * @param	spacing			The space (in pixels) between two options
	 */
	public OptionSelector(List<Option> options, NinesliceMDO bgMDO,
			FontHolder font, float padHoriz, float padVert, float spacing) {
		this.options = options;
		this.bgMDO = bgMDO;
		this.font = font;
		this.padHoriz = padHoriz;
		this.padVert = padVert;
		this.spacingVert = spacing;
		
		float maxTextWidth = 0;
		for (Option o : options) {
			float width = font.getWidth(o.getText());
			maxTextWidth = Math.max(maxTextWidth, width);
		}
		width = maxTextWidth + 
				padHoriz * 2;
		height = options.size() * font.getLineHeight() +
				(options.size()-1) * spacingVert +
				padVert * 2;
		bg = new Nineslice(bgMDO, (int) width, (int) height);
		assets.add(bg);
	}
	
	/**
	 * Creates a new options selector with default padding.
	 * @param	options			The options the player can select from
	 * @param	bg				The background nineslice to use
	 * @param	font			The font to render option text in
	 */
	public OptionSelector(List<Option> options, NinesliceMDO bgMDO, FontHolder font) {
		this(options, bgMDO, font,
				DEFAULT_PADDING_HORIZ,
				DEFAULT_PADDING_VERT,
				DEFAULT_SPACING);
	}
	
	/**
	 * Creates a new options selector with some default settings (like padding,
	 * appearance, cursor, etc) for a list of options.
	 * @param	options				The options the player can select from
	 */
	public OptionSelector(List<Option> options) {
		this(options,
				MGlobal.ui.getNinesliceMDO(),
				MGlobal.ui.getFont());
	}

	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		switch (command) {
		case MOVE_UP:		moveCursor(-1);		return true;
		case MOVE_DOWN:		moveCursor(1);		return true;
		case UI_CONFIRM:	confirm();			return true;
		default:								return true;
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.ScreenObject#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		bg.renderAt(getBatch(), screenX, screenY);
		int off = 0;
		for (Option o : options) {
			font.draw(getBatch(), format, o.getText(), off);
			off -= (spacingVert + font.getLineHeight());
		}
		MGlobal.ui.getCursor().renderAt(getBatch(), cursorX, cursorY);
	}
	
	/**
	 * Shows this UI element on the screen. No smooth fading just yet. This will
	 * add the selector to the current screen and start intercepting commands.
	 * @param	screenX			The x-coord to show at (in screen px)
	 * @param	screenY			The y-coord to show at (in screen px)
	 */
	public void showAt(int screenX, int screenY) {
		this.screenX = screenX;
		this.screenY = screenY;
		
		MGlobal.screens.peek().addObject(this);
		focus();
		
		format = new TextBoxFormat();
		format.align = HAlignment.LEFT;
		format.width = (int) (width + padHoriz);
		format.height = 100;
		format.x = screenX + (int) padHoriz;
		format.y = screenY + (int) (height - padVert);
		
		selected = 0;
		cursorOn = true;
		setCursorLoc();
	}
	
	/**
	 * Stops this menu from receiving input. It still displays on the screen.
	 */
	public void unfocus() {
		if (controlling) {
			MGlobal.screens.peek().removeCommandListener(this);
			MGlobal.screens.peek().popCommandContext();
		}
		controlling = false;
	}
	
	/**
	 * Resumes the menu for input reception. Should already be on screen.
	 */
	public void focus() {
		MGlobal.screens.peek().pushCommandContext(new CMapMenu());
		MGlobal.screens.peek().pushCommandListener(this);
		controlling = true;
	}
	
	/**
	 * Removes the menu from the screen and unhands control.
	 */
	public void close() {
		unfocus();
		MGlobal.screens.peek().removeObject(this);
	}

	/**
	 * Snaps the cursor to the correct location. No tweening.
	 */
	protected void setCursorLoc() {
		cursorX = screenX + padHoriz - MGlobal.ui.getCursor().getWidth() - 4;
//		cursorY = screenY + padVert + MGlobal.ui.getCursor().getHeight()/2 - 5 -
//				selected * (font.getLineHeight() + spacingVert);
		cursorY = screenY + (options.size() - selected - 1) * (font.getLineHeight() + spacingVert);
	}
	
	/**
	 * Moves the cursor in one direction or the other. Applies bounds. Negative
	 * to move upwards, positive is downwards.
	 * @param	delta			How far to move the cursor
	 */
	protected void moveCursor(int delta) {
		selected += delta;
		while (selected < 0) selected += options.size();
		selected = selected % options.size();
		setCursorLoc();
	}
	
	/**
	 * Called when the user confirms. Should run the appropriate command.
	 */
	protected void confirm() {
		options.get(selected).onSelect();
	}
	
}
