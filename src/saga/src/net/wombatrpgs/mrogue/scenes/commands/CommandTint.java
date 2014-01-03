/**
 *  CommandTint.java
 *  Created on Feb 5, 2013 9:34:29 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import com.badlogic.gdx.graphics.Color;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Updateable;
import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;

/**
 * The RM2K Set Screen Tone thing.
 */
public class CommandTint extends SceneCommand {
	
	protected static boolean tinting = false;
	
	protected float r, g, b;
	protected float startTime, duration;

	/**
	 * Creates a new tint command from code
	 * @param 	parent			The parser that spawned us
	 * @param 	line			The code that spawned us
	 */
	public CommandTint(SceneParser parent, String line) {
		super(parent, line);
		line = line.substring(line.indexOf(' ') + 1);
		r = Float.valueOf(line.substring(0, line.indexOf(' ')));
		line = line.substring(line.indexOf(' ') + 1);
		g = Float.valueOf(line.substring(0, line.indexOf(' ')));
		line = line.substring(line.indexOf(' ') + 1);
		b = Float.valueOf(line.substring(0, line.indexOf(' ')));
		line = line.substring(line.indexOf(' ') + 1);
		duration = Float.valueOf(line.substring(0, line.indexOf(']')));
		if (duration <= 0) duration = .01f;
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!finished) {
			if (tinting) return false;
			tinting = true;
			startTime = parent.getTimeSinceStart();
			final SceneParser parser = parent;
			final float oldR = MGlobal.screens.peek().getTint().r;
			final float oldG = MGlobal.screens.peek().getTint().g;
			final float oldB = MGlobal.screens.peek().getTint().b;
			Updateable child = new Updateable() {
				protected Color tint;
				@Override
				public void update(float elapsed) {
					float ratio = (parser.getTimeSinceStart() - startTime) / duration;
					if (ratio >= 1) ratio = 1;
					if (tint == null) tint = MGlobal.screens.peek().getTint();
					tint.r = oldR + ratio * (r - oldR);
					tint.g = oldG + ratio * (g - oldG);
					tint.b = oldB + ratio * (b - oldB);
					if (ratio >= 1) {
						getScreen().removeUChild(this);
						tinting = false;
					}
				}	
			};
			getScreen().addUChild(child);
			finished = true;
		}
		return true;
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		tinting = false;
	}

}
