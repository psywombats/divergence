/**
 *  CommandTeleport.java
 *  Created on Feb 10, 2013 8:50:51 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.scenes.commands;

import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.scenes.SceneCommand;
import net.wombatrpgs.mrogue.scenes.SceneParser;

/**
 * A teleport to move the hero from one map to another.
 */
public class CommandTeleport extends SceneCommand {
	
	protected String mapID;
	protected Level map;
	protected int teleX, teleY;
	protected boolean basic;
	protected boolean phase2;

	/**
	 * Constructs a new teleport command from script.
	 * @param 	parent			The parent parser
	 * @param 	line			The line of code to interpret
	 */
	public CommandTeleport(SceneParser parent, String line) {
		super(parent, line);
		line = line.substring(line.indexOf(' ') + 1);
		mapID = line.substring(0, line.indexOf(' '));
		line = line.substring(line.indexOf(' ') + 1);
		teleX = Integer.valueOf(line.substring(0, line.indexOf(' ')));
		line = line.substring(line.indexOf(' ') + 1);
		if (line.indexOf(' ') == -1) {
			teleY = Integer.valueOf(line.substring(0, line.indexOf(']')));
			basic = false;
		} else {
			teleY = Integer.valueOf(line.substring(0, line.indexOf(' ')));
			line = line.substring(line.indexOf(' ') + 1);
			basic = true;
		}
		phase2 = false;
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#run()
	 */
	@Override
	public boolean run() {
		if (finished) return true;
//		SceneParser pre = MGlobal.teleport.getPre();
//		SceneParser post = MGlobal.teleport.getPost();
//		if (!phase2 && !basic) {
//			parent.getL().addObject(pre);
//			pre.run(parent.getParent());
//			getParent().setChild(pre);
//			post.reset();
//			phase2 = true;
//		}
//		if (pre.hasExecuted() || basic) {
//			if ((!post.isRunning() && !post.hasExecuted()) || basic) {
//				if (!basic) parent.getLevel().removeObject(pre);
//				parent.getParent().removeObject(parent);
//				if (map == null) {
//					map = MGlobal.levelManager.getLevel(mapID);
//				}
//				MGlobal.teleport.teleport(
//						map, 
//						teleX,
//						map.getHeight() - teleY - 1);
//				map.addObject(parent);
//				if (!basic) {
//					map.addObject(post);
//					post.run(map);
//					getParent().setChild(post);
//				}
//				finished = basic;
//				return finished;
//			} else {
//				finished = true;
//			}
//		}
		return false;
	}

	/**
	 * @see net.wombatrpgs.mrogue.scenes.SceneCommand#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		phase2 = false;
	}

}
