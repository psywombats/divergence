/**
 *  EventPressurePadHavenFight.java
 *  Created on Mar 2, 2013 7:07:55 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.custom.instances;

import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.custom.CustomEvent;
import net.wombatrpgs.rainfall.maps.custom.EventPressurePad;
import net.wombatrpgs.rainfall.maps.custom.EventTrickWall;

public class EventPressurePadHavenFight extends EventPressurePad {
	
	public static final String ID = CustomEvent.EVENT_PREFIX + "pressure_pad_havenfight";

	public EventPressurePadHavenFight(TiledObject object, Level parent) {
		super(object, parent);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.custom.EventPressurePad#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		EventPressurePad partner = (EventPressurePad) parent.getCustomObject(EventPressurePad.ID);
		EventTrickWall door = (EventTrickWall) parent.getCustomObject(EventTrickWall.ID);
		if (this.pressed && partner.pressed) {
			if (!door.open) {
				door.open();
			}
		} else {
			if (door.open) {
				door.close();
			}
		}
	}
	
}
