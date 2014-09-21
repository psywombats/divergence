/**
 *  OptionSelector.java
 *  Created on Feb 18, 2014 11:50:36 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.ScreenGraphic;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.io.CommandMap;
import net.wombatrpgs.mgne.io.command.CMapRaw;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextFormat;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.ui.NinesliceMDO;

/**
 * The spiritual successor to the old menu from 7DRL. This thing displays a list
 * of options and a cursor, and the player selects one of the options.
 */
public class OptionSelector extends ScreenGraphic implements	CommandListener,
																Disposable{
	
	protected static int DEFAULT_PADDING_HORIZ = 24;
	protected static int DEFAULT_PADDING_VERT = 10;
	protected static int DEFAULT_SPACING = 5;
	
	// from constructor
	protected List<Option> options;
	protected NinesliceMDO bgMDO;
	protected boolean autoload;
	protected boolean disableBG;
	protected int padHoriz, padVert;
	protected int spacingVert;
	
	// calculated
	protected Nineslice bg;
	protected TextFormat format;
	protected int width, height;
	
	// cursor
	protected boolean cursorOn;
	protected int selected;
	protected float cursorX, cursorY;
	
	// etc state
	protected CommandMap context;
	protected FinishListener cancel;
	protected boolean cancellable;
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
			FontHolder font, int padHoriz, int padVert, int spacing) {
		super(0, 0);
		this.options = new ArrayList<Option>();
		this.options.addAll(options);
		this.bgMDO = bgMDO;
		this.padHoriz = padHoriz;
		this.padVert = padVert;
		this.spacingVert = spacing;
		
		int maxTextWidth = 0;
		for (Option o : options) {
			float width = font.getWidth(o.getText());
			maxTextWidth = (int) Math.max(maxTextWidth, width);
		}
		width = maxTextWidth + 
				padHoriz * 2;
		height = (int) (options.size() * font.getLineHeight() +
				(options.size()-1) * spacingVert +
				padVert * 2);
		bg = new Nineslice(bgMDO);
		assets.add(bg);
		
		context = new CMapRaw();
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
	 * @param	options			The options the player can select from
	 */
	public OptionSelector(List<Option> options) {
		this(options,
				MGlobal.ui.getNinesliceMDO(),
				MGlobal.ui.getFont());
	}
	
	/**
	 * Creates a new options selector with some default settings for a set.
	 * @param	options			The options the player can select from
	 */
	public OptionSelector(Option... options) {
		this(Arrays.asList(options));
	}
	
	/**
	 * Creates a new options selector for easy use. If autoload is enabled, will
	 * load itself when displayed and unload itself when undisplayed.
	 * @param	autoload		True to enable autoload
	 * @param	options			The options the player can select from
	 */
	public OptionSelector(boolean autoload, Option... options) {
		this(Arrays.asList(options));
		this.autoload = autoload;
	}
	
	/**
	 * Creates a new options selector for inline UI use.
	 * @param	autoload		True to enable autoload
	 * @param	disableBG		True to not render the nineslice
	 * @param	options			The options the player can select from
	 */
	public OptionSelector(boolean autoload, boolean disableBG, Option... options) {
		this(autoload, options);
		this.disableBG = disableBG;
	}
	
	/** @see net.wombatrpgs.mgne.graphics.ScreenGraphic#getWidth() */
	@Override public int getWidth() { return width; }

	/** @see net.wombatrpgs.mgne.graphics.ScreenGraphic#getHeight() */
	@Override public int getHeight() { return height; }
	
	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		switch (command) {
		case RAW_UP:		moveCursor(-1);		return true;
		case RAW_DOWN:		moveCursor(1);		return true;
		case RAW_A:			confirm();			return true;
		case RAW_B:			cancel();			return true;
		case RAW_START:		cancel();			return true;
		default:								return true;
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		bg.dispose();
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#coreRender
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void coreRender(SpriteBatch batch) {
		if (!disableBG) {
			bg.renderAt(getBatch(), x, y);
		}
		int off = 0;
		FontHolder font = MGlobal.ui.getFont();
		for (Option o : options) {
			font.draw(getBatch(), format, o.getText(), off);
			off -= (spacingVert + font.getLineHeight());
		}
		if (controlling) {
			MGlobal.ui.getCursor().renderAt(getBatch(), cursorX, cursorY);
		}
	}

	/**
	 * Sets the function to be called when the user cancels out of this
	 * selector. Really shouldn't be called a finish listener.
	 * @param	listener		The code to execute on cancel
	 */
	public void setCancel(FinishListener listener) {
		this.cancel = listener;
		setCancellable(true);
	}
	
	/**
	 * Enables or disables cancellation.
	 * @param	cancellable		True to enable cancel, false to disable
	 */
	public void setCancellable(boolean cancellable) {
		this.cancellable = cancellable;
	}

	/**
	 * Shows this UI element on the screen. No smooth fading just yet. This will
	 * add the selector to the current screen and start intercepting commands.
	 * @param	screenX			The x-coord to show at (in game px)
	 * @param	screenY			The y-coord to show at (in game px)
	 */
	public void showAt(int screenX, int screenY) {
		this.x = screenX;
		this.y = screenY;
		
		if (autoload) {
			MGlobal.assets.loadAsset(this, "autoloading options menu");
		}
		
		MGlobal.screens.peek().addChild(this);
		focus();
		
		format = new TextFormat();
		format.align = HAlignment.LEFT;
		format.width = (int) (width + padHoriz);
		format.height = 100;
		format.x = screenX + (int) padHoriz;
		format.y = screenY + (int) (height - padVert);
		
		if (bg.getWidth() == 0 || bg.getHeight() == 0) {
			bg.resizeTo((int) width, (int) height);
		}
		
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
			MGlobal.screens.peek().removeCommandContext(context);
		}
		controlling = false;
	}
	
	/**
	 * Resumes the menu for input reception. Should already be on screen.
	 */
	public void focus() {
		MGlobal.screens.peek().pushCommandContext(context);
		MGlobal.screens.peek().pushCommandListener(this);
		controlling = true;
	}
	
	/**
	 * Removes the menu from the screen and unhands control.
	 */
	public void close() {
		unfocus();
		if (MGlobal.screens.peek().containsChild(this)) {
			MGlobal.screens.peek().removeChild(this);
		}
		if (autoload) {
			dispose();
		}
	}

	/**
	 * Snaps the cursor to the correct location. No tweening.
	 */
	protected void setCursorLoc() {
		FontHolder font = MGlobal.ui.getFont();
		cursorX = x + padHoriz - MGlobal.ui.getCursor().getWidth() - 4;
		cursorY = y + (options.size() - selected - 1) * (font.getLineHeight() + spacingVert);
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
		if (options.get(selected).onSelect()) {
			close();
		}
	}
	
	/**
	 * Called when the user hits the cancel button. If no listener is
	 * associated, does nothing.
	 */
	protected void cancel() {
		if (cancellable) {
			if (cancel != null) {
				cancel.onFinish();
			} else {
				close();
			}
		}
	}
	
}
