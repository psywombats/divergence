/**
 *  TextPlayback.java
 *  Created on May 23, 2014 10:01:26 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import net.wombatrpgs.saga.screen.ScreenBattle;

/**
 * Plays back some lines of text on the screen.
 */
public class TextPlayback extends PlaybackStep {
	
	protected String text;

	/**
	 * Creates a playback for a given string and piece of text.
	 * @param	screen			The screen to create for
	 * @param	text			The text to play back
	 */
	public TextPlayback(ScreenBattle screen, String text) {
		super(screen);
		this.text = text;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.PlaybackStep#isDone()
	 */
	@Override
	public boolean isDone() {
		return screen.isTextFinished();
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.PlaybackStep#internalStart()
	 */
	@Override
	public void internalStart() {
		screen.immediatePrint(text);
	}

}
