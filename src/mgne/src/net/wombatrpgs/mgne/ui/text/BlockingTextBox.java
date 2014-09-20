/**
 *  BlockingTextBox.java
 *  Created on Sep 12, 2014 2:20:11 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.ui.text;

import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.io.CommandMap;
import net.wombatrpgs.mgne.io.command.CMapScene;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.ui.TextBoxMDO;

/**
 * A textbox that takes control of the scene.
 */
public class BlockingTextBox extends TextBox implements CommandListener {
	
	protected static final float FADE_TIME = 0f;
	
	protected CommandMap context;
	protected boolean blocking;
	protected boolean setText;
	protected Screen screen;
	protected String textToShow;

	/**
	 * Inherited constructor.
	 * @param	mdo				The data to create from
	 * @param	font			The font to display with
	 */
	public BlockingTextBox(TextBoxMDO mdo, FontHolder font) {
		super(mdo, font);
		blocking = false;
		setText = false;
		context = new CMapScene();
	}

	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (blocking && command == InputCommand.UI_CONFIRM) {
			if (isFinished()) {
				fadeOut(FADE_TIME);
				screen.removeCommandListener(this);
				screen.removeCommandContext(context);
				blocking = false;
			} else {
				hurryUp();
			}
		}
		return true;
	}

	/**
	 * @see net.wombatrpgs.mgne.ui.text.TextBox#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (isTweening()) return;
		if (!setText) {
			showText(textToShow);
			setText = true;
		}
	}
	
	/**
	 * Checks if this box is currently clogging the command flow.
	 * @return					True if this box is blocking
	 */
	public boolean isBlocking() {
		return blocking;
	}
	
	/**
	 * Displays some text stuff. Fades in and displays the textbox.
	 * @param	screen			The screen to fade in on
	 * @param	text			The text to display
	 */
	public void blockText(Screen screen, String text) {
		this.textToShow = text;
		this.screen = screen;
		setText = false;
		fadeIn(screen, FADE_TIME);
		screen.pushCommandListener(this);
		screen.pushCommandContext(context);
		blocking = true;
	}

}
