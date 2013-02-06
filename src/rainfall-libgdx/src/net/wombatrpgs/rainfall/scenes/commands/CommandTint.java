/**
 *  CommandTint.java
 *  Created on Feb 5, 2013 9:34:29 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.scenes.commands;

import com.badlogic.gdx.graphics.Color;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfall.maps.PauseLevel;
import net.wombatrpgs.rainfall.scenes.SceneCommand;
import net.wombatrpgs.rainfall.scenes.SceneParser;

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
	 * @see net.wombatrpgs.rainfall.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (!finished) {
			if (tinting) return false;
			tinting = true;
			startTime = parent.getTimeSinceStart();
			final SceneParser parser = parent;
			final float oldR = RGlobal.screens.peek().getTint().r;
			final float oldG = RGlobal.screens.peek().getTint().g;
			final float oldB = RGlobal.screens.peek().getTint().b;
			MapObject child = new MapObject() {
				protected Color tint;
				@Override
				public void update(float elapsed) {
					super.update(elapsed);
					float ratio = (parser.getTimeSinceStart() - startTime) / duration;
					if (ratio >= 1) ratio = 1;
					if (tint == null) tint = RGlobal.screens.peek().getTint();
					tint.r = oldR + ratio * (r - oldR);
					tint.g = oldG + ratio * (r - oldG);
					tint.b = oldB + ratio * (r - oldB);
					if (ratio >= 1) {
						parent.removeObject(this);
						tinting = false;
					}
				}	
			};
			child.setPauseLevel(PauseLevel.PAUSE_RESISTANT);
			parent.getLevel().addObject(child);
			finished = true;
		}
		return true;
	}

}
