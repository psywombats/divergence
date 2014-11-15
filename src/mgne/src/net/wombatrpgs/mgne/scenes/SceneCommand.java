/**
 *  SceneCommand.java
 *  Created on Feb 3, 2013 8:51:29 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.scenes;

import java.util.List;

import org.luaj.vm2.LuaValue;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 * A chunk of code to execute later, returned from a lua function. The idea is
 * that a bunch of these are placed in a queue when a script is evaluated and
 * then executed sequentially. This is to support blocking commands. Instead of
 * the parser being assigned at construction, it's assigned at runtime. Commands
 * can still queue assets like before. 
 * 
 * Blocking as in the press-button-to-go type is no longer support here because
 * really only the speak command needed that thing in the first place and it can
 * block itself. However, a collection of timing methods have been added.
 */
public abstract class SceneCommand extends AssetQueuer implements	Updateable,
																	CommandListener {
	
	protected SceneParser parent;
	
	protected float timeToWait, timeSinceStart;
	protected int index, count;
	protected boolean finished;
	protected boolean running;
	
	/**
	 * Creates a new command. Before being run it must be possessed by a parent
	 * and have its assets queued.
	 */
	public SceneCommand() {
		super();
		finished = false;
		timeToWait = 0;
		timeSinceStart = 0;
	}
	
	/** @return The parent parser that will execute this command */
	public final SceneParser getParent() { return this.parent; }
	
	/** @return The screen this command is running on */
	public final Screen getScreen() { return parent.getScreen(); }
	
	/** @return True if this command is done running */
	public final boolean isFinished() { return this.finished; }
	
	/**
	 * This will only be called if this command is actually running.
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		timeSinceStart += elapsed;
		if (shouldFinish()) {
			finish();
		}
	}
	
	/**
	 * Scene commands are equipped to handle user input. Most of the time it's
	 * just ignored, but for text boxes, it could advance the text.
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		return false;
	}

	/**
	 * Call this when it's this scene's turn to run.
	 * @param	parent			The parser to run within the context of
	 */
	public final void run(SceneParser parent) {
		this.parent = parent;
		finished = false;
		running = true;
		timeSinceStart = 0;
		timeToWait = 0;
		internalRun();
	}
	
	/**
	 * This identifier marks the type of command. Commands with identical
	 * identifiers will have their merge function called together.
	 * @return					The identifier of this command
	 */
	public final String getID() {
		return String.valueOf(getClass().hashCode());
	}
	
	/**
	 * Called on the first of a group of commands of the same type that appear
	 * together in sequence. By default, this just returns the whole group. 
	 * Some commands may wish to modify themselves when placed with others (such
	 * as the text box).
	 * @param	commands		The original commands of the same type
	 * @return					Those commands, modified for sequence
	 */
	public List<SceneCommand> merge(List<SceneCommand> commands) {
		for (int i = 0; i < commands.size(); i += 1) {
			commands.get(i).index = i;
			commands.get(i).count = commands.size();
		}
		return commands;
	}
	
	/**
	 * What happens when this command has its turn in the scene and runs. All
	 * the running and finished flags are handled elsewhere. This is a good
	 * place to initialize fields.
	 */
	protected abstract void internalRun();
	
	/**
	 * Commands that require assets should take this opportunity to add all
	 * those assets to the assets list. The assets will probably be owned by the
	 * function that generated this command, but that's okay.
	 */
	protected void addToQueue() {
		// default is nothing
	}
	
	/**
	 * Extracts the lua value of an event from an argument passed to a scene
	 * command call. The arg could be the name of an event or the event itself.
	 * @param	eventArg		The event parameter passed from script
	 * @return					The lua value of that event
	 */
	protected static LuaValue argToLua(LuaValue eventArg) {
		if (eventArg.isstring()) {
			String eventName = eventArg.checkjstring();
			MapEvent event = MGlobal.levelManager.getActive().getEventByName(eventName);
			if (event == null) {
				MGlobal.reporter.err("No event named '" + eventName + "'");
				return LuaValue.NIL;
			}
			return event.toLua();
		} else {
			return eventArg;
		}
	}
	
	/**
	 * Extracts the map event from an argument passed to a script. The arg could
	 * be the lua valua of an event or its string name.
	 * @param	eventArg		The event parameter passed from script
	 * @return					The map event corresponding to that arg
	 */
	protected static MapEvent argToEvent(LuaValue eventArg) {
		String eventName;
		if (eventArg.isstring()) {
			eventName = eventArg.checkjstring();
		} else {
			eventName = eventArg.get("getName").call().checkjstring();
		}
		MapEvent event = MGlobal.levelManager.getActive().getEventByName(eventName);
		if (event == null) {
			MGlobal.reporter.err("No event named '" + eventName + "'");
			return null;
		}
		return event;
	}
	
	/**
	 * Called the very last time this command is run. Usually just tidies up the
	 * state variables. Override if things like clamping positions are needed.
	 */
	protected void finish() {
		finished = true;
	}
	
	/**
	 * Waits for a certain amount of time before progressing. A ton of commands
	 * use this, so why not?
	 * @param	time			The time to wait before reporting finished, in s
	 */
	protected final void waitFor(float time) {
		timeToWait = time;
	}
	
	/**
	 * End condition for the function. By default this just makes sure some
	 * amount of time has expired, but if a command is waiting for user input
	 * or something, then this should stall until that instead. Overrides should
	 * almost always call super. This is checked every update step.
	 * @return					True if command should terminate, else false
	 */
	protected boolean shouldFinish() {
		if (timeSinceStart < timeToWait) return false;
		return true;
	}

}
