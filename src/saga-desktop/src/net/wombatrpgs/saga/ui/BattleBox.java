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
import net.wombatrpgs.mgne.ui.text.TextBox;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.ui.TextBoxMDO;
import net.wombatrpgs.saga.screen.CombatScreen;

/**
 * Textbox equivalent for the combat screen. Supports the multiline functions
 * that SpeakAll usually provides, but with an on-the-fly interface. Implements
 * a command listener so that writing new text produces a callback to the parent
 * screen when the text is finished.
 */
public class BattleBox extends TextBox implements CommandListener {
	
	protected CombatScreen parent;
	
	protected boolean auto;

	/**
	 * Creates a new box with default font from supplied dimensions. Creates an
	 * MDO with the linecount supplied but with otherwise default settings from
	 * the text box mdo.
	 * @param	parent			The parent screen to which this box belongs
	 * @param	lines			The new amount of lines in the battlebox
	 */
	public BattleBox(CombatScreen parent, int lines) {
		super(generateMDO(lines), MGlobal.ui.getFont());
		this.parent = parent;
		auto = false;
	}
	
	/** @param auto True to not wait for human input */
	public void setAutoMode(boolean auto) { this.auto = auto; }
	
	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		// TODO: polish: you should be able to hold 'a' to scroll the battle
		if (command == InputCommand.UI_CONFIRM) {
			if (isFinished()) {
				parent.removeCommandListener(this);
				parent.onTextFinished();
			} else {
				hurryUp();
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
		super.update(elapsed);
		if (isFinished() && auto) {
			parent.removeCommandListener(this);
			parent.onTextFinished();
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
		print(text + " \n");
	}
	
	/**
	 * Prints a line of text in the box. Doesn't include newline. Text is split
	 * over multiple lines if it doesn't fit.
	 * @param	text			The text to write to the screen
	 */
	public void print(String text) {
		if (parent.getTopCommandListener() != this) {
			parent.pushCommandListener(this);
		}
		Collections.addAll(words, text.split(" "));
	}
	
	/**
	 * Copies and reformats the text box format data.
	 * @param	lines			The new amount of lines in the battlebox
	 * @return					The modified MDO
	 */
	protected static TextBoxMDO generateMDO(int lines) {
		TextBoxMDO orig = MGlobal.ui.getBoxMDO();
		TextBoxMDO mdo = new TextBoxMDO();
		mdo.anchor = orig.anchor;
		mdo.lines = lines;
		mdo.marginHeight = orig.marginHeight + 2;
		mdo.marginWidth = orig.marginWidth + 2;
		mdo.nineslice = orig.nineslice;
		mdo.scaling = orig.scaling;
		mdo.typeSfx = orig.typeSfx;
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
