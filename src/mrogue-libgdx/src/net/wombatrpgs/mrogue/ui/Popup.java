/**
 *  Popup.java
 *  Created on Oct 25, 2013 10:43:44 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.ui;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.io.CommandListener;
import net.wombatrpgs.mrogue.io.CommandMap;
import net.wombatrpgs.mrogue.io.command.CMapDialog;
import net.wombatrpgs.mrogue.screen.Screen;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;
import net.wombatrpgs.mrogueschema.maps.data.OrthoDir;

/**
 * A dialog box that preempts things basically? I don't know. It has its own
 * associated command interface. The inventory menu should be one of these.
 */
public class Popup extends UIElement implements CommandListener {
	
	protected static final int TEXT_WIDTH = 512;
	protected static final int TEXT_HEIGHT = 64;
	
	protected CommandMap commands;
	protected boolean active;
	
	/**
	 * Creates a new popup with the default command map.
	 */
	public Popup() {
		commands = new CMapDialog();
		active = false;
	}
	
	/**
	 * Creates a new popup with the specified set of commands.
	 * @param	commands			The command set to use
	 */
	public Popup(CommandMap commands) {
		this.commands = commands;
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.CommandListener#onCommand
	 * (net.wombatrpgs.mrogueschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		switch (command) {
		case CURSOR_UP:			onCursorMove(OrthoDir.NORTH);	break;
		case CURSOR_RIGHT:		onCursorMove(OrthoDir.EAST);	break;
		case CURSOR_DOWN:		onCursorMove(OrthoDir.SOUTH);	break;
		case CURSOR_LEFT:		onCursorMove(OrthoDir.WEST);	break;
		case INTENT_CANCEL:		cancel();						break;
		case INTENT_CONFIRM:	confirm();						break;
		default:												return false;
		}
		return true;
	}
	
	/**
	 * Called whenever the menu should show up. Default activates the command
	 * map. This does not cover adding to the parent screen.
	 */
	public void show() {
		activateCommands();
		active = true;
	}
	
	/**
	 * Called whenever the menu should hide itself or otherwise go away. Default
	 * deactivates the command map.
	 */
	public void hide() {
		deactivateCommands();
		active = false;
	}
	
	/**
	 * Called whenever the user moves the cursor on this popup, if any. Default
	 * does nothing and returns false.
	 * @param	dir					The direction the cursor moved
	 * @return						True if the cursor event was consumed
	 */
	protected boolean onCursorMove(OrthoDir dir) {
		return false;
	}
	
	/**
	 * Called when the user presses the cancel button. Default hides this.
	 * @return					True if cancellation occurred
	 */
	protected boolean cancel() {
		hide();
		return true;
	}
	
	/**
	 * Called when the user presses the space bar or something. Default does
	 * nothing and returns false/
	 * @return					True if the event was consumed
	 */
	protected boolean confirm() {
		return false;
	}
	
	/**
	 * Registers our command listener with parent screen.
	 */
	protected final void activateCommands() {
		Screen s = MGlobal.screens.peek();
		s.registerCommandListener(this);
		s.pushCommandContext(commands);
	}
	
	/**
	 * Unregisters our command listener from parent screen.
	 */
	protected final void deactivateCommands() {
		Screen s = MGlobal.screens.peek();
		s.unregisterCommandListener(this);
		s.popCommandContext();
	}
}
