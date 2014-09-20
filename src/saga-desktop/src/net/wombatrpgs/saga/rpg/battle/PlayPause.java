/**
 *  PlayPause.java
 *  Created on Sep 20, 2014 2:17:18 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.battle;

import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.io.CommandMap;
import net.wombatrpgs.mgne.io.command.CMapRaw;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.saga.screen.ScreenBattle;

/**
 * Waits for the player to confirm before continuing.
 */
public class PlayPause extends PlaybackStep implements CommandListener {
	
	protected CommandMap context;
	protected boolean done;

	/**
	 * Inherited constructor.
	 * @param	screen			The battle screen to construct for
	 */
	public PlayPause(ScreenBattle screen) {
		super(screen);
		context = new CMapRaw();
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.battle.PlaybackStep#internalStart()
	 */
	@Override
	public void internalStart() {
		screen.pushCommandContext(context);
		screen.pushCommandListener(this);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (command == InputCommand.RAW_A) {
			done = true;
		}
		return true;
	}
	
	/**
	 * @see net.wombatrpgs.saga.rpg.battle.PlaybackStep#isDone()
	 */
	@Override
	public boolean isDone() {
		return done;
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.battle.PlaybackStep#finish()
	 */
	@Override
	public void finish() {
		super.finish();
		screen.removeCommandContext(context);
		screen.removeCommandListener(this);
	}

}
