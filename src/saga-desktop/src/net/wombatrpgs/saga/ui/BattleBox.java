/**
 *  BattleBox.java
 *  Created on Apr 22, 2014 2:47:36 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.ui;

import java.util.Collections;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.io.Keymap.KeyState;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextBox;
import net.wombatrpgs.mgneschema.io.data.InputButton;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.ui.TextBoxMDO;
import net.wombatrpgs.saga.screen.ScreenBattle;

/**
 * Textbox equivalent for the combat screen. Supports the multiline functions
 * that SpeakAll usually provides, but with an on-the-fly interface. Implements
 * a command listener so that writing new text produces a callback to the parent
 * screen when the text is finished.
 */
public class BattleBox extends TextBox implements CommandListener {
	
	protected ScreenBattle parent;
	
	protected boolean receivedDown;
	protected boolean shouldAdvance;

	/**
	 * Creates a new box with default font from supplied dimensions. Creates an
	 * MDO with the linecount supplied but with otherwise default settings from
	 * the text box mdo.
	 * @param	parent			The parent screen to which this box belongs
	 * @param	lines			The new amount of lines in the battlebox
	 */
	public BattleBox(ScreenBattle parent, int lines) {
		super(generateMDO(lines-1), MGlobal.ui.getFont());
		this.parent = parent;
	}
	
	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (command == InputCommand.UI_CONFIRM) {
			if (isFinished()) {
				parent.removeCommandListener(this);
				shouldAdvance = true;
			} else {
				receivedDown = true;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.ui.text.TextBox#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (MGlobal.keymap.getButtonState(InputButton.BUTTON_A) == KeyState.DOWN && receivedDown) {
			super.update(elapsed * 4);
		} else {
			super.update(elapsed);
		}
		if (receivedDown && (MGlobal.keymap.getButtonState(InputButton.BUTTON_A) != KeyState.DOWN)) {
			receivedDown = false;
		}
		if (isFinished()) {
			parent.removeCommandListener(this);
			shouldAdvance = true;
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#fadeOut(float)
	 */
	@Override
	public void fadeOut(float fadeTime) {
		super.fadeOut(fadeTime);
		if (parent.getTopCommandListener() == this) {
			parent.removeCommandListener(this);
		}
	}

	/**
	 * Prints a line of text in the box. Includes a newline. Text will be split
	 * over multiple lines if it doesn't fit.
	 * @param	text			The text to write to the screen
	 */
	public void println(String text) {
		shouldAdvance = false;
		print(text + " \n");
	}
	
	/**
	 * Prints a line of text in the box. Doesn't include newline. Text is split
	 * over multiple lines if it doesn't fit.
	 * @param	text			The text to write to the screen
	 */
	public void print(String text) {
		shouldAdvance = false;
		if (parent.getTopCommandListener() != this) {
			parent.pushCommandListener(this);
		}
		Collections.addAll(words, text.split(" "));
	}
	
	/**
	 * Checks if the battle should continue past this battle box.
	 * @return					True if the battle can advance
	 */
	public boolean shouldAdvance() {
		return shouldAdvance;
	}
	
	/**
	 * Copies and reformats the text box format data.
	 * @param	lines			The new amount of lines in the battlebox
	 * @return					The modified MDO
	 */
	protected static TextBoxMDO generateMDO(int lines) {
		TextBoxMDO orig = MGlobal.ui.getBoxMDO();
		TextBoxMDO mdo = new TextBoxMDO();
		FontHolder font = MGlobal.ui.getFont();
		mdo.anchor = orig.anchor;
		mdo.lines = lines;
		mdo.marginTop = orig.marginTop + 2;
		mdo.marginBottom = (int) (orig.marginBottom + 2 + font.getLineHeight() * 2);
		mdo.marginWidth = orig.marginWidth + 2;
		mdo.nineslice = orig.nineslice;
		mdo.scaling = orig.scaling;
		mdo.typeSpeed = orig.typeSpeed;
		return mdo;
	}

	/**
	 * @see net.wombatrpgs.mgne.ui.text.TextBox#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		advanceLines(mdo.lines);
	}

	/**
	 * @see net.wombatrpgs.mgne.ui.text.TextBox#waitOnNewline()
	 */
	@Override
	protected boolean waitOnNewline() {
		return false;
	}
	
}
