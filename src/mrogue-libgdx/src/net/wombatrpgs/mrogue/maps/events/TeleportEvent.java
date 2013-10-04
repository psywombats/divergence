/**
 *  TeleportEvent.java
 *  Created on Dec 24, 2012 2:00:17 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.events;

import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogueschema.maps.MapMDO;

/**
 * Constructs a teleportation device! (or event, just depends on your
 * perspective...)
 */
public class TeleportEvent extends MapEvent {
	
	protected MapMDO mdo;
	protected int targetX;
	protected int targetY;

	/**
	 * Creates a new teleport for the supplied parent level using coordinates
	 * inferred from the tiled object. Called from the superclass's factory
	 * method.
	 * @param 	parent		The parent levelt to make teleport for
	 */
	public TeleportEvent(Level parent) {
		super(parent);
		// TODO: TeleportEvent
	}

	// TODO: teleport
//	public boolean teleport(MapEvent other) {
//		if (other != MGlobal.hero) return true;
//		if (getLevel().contains(MGlobal.teleport.getPre())) return true;
//		final TeleportEvent parent = this;
//		MGlobal.teleport.getPre().addListener(new FinishListener() {
//			@Override
//			public void onFinish(Level map) {
//				Level newMap = MGlobal.levelManager.getLevel(mapID);
//				MGlobal.teleport.teleport(
//						newMap, 
//						targetX, 
//						newMap.getHeight() - targetY - 1);
//				MGlobal.teleport.getPost().run(newMap);
//				if (parent.getProperty(PROPERTY_Z) != null) {
//					newMap.changeZ(MGlobal.hero, 
//							Float.valueOf(parent.getProperty(PROPERTY_Z))+.5f);
//				}
//			}
//		});
//		MGlobal.teleport.getPre().run(MGlobal.hero.getLevel());
//		return true;
//	}
	
}
