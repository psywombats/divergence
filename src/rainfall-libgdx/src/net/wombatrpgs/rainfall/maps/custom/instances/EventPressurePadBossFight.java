/**
 *  EventPressurePadBossFight.java
 *  Created on Mar 15, 2013 7:07:55 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.custom.instances;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.FacesAnimation;
import net.wombatrpgs.rainfall.graphics.FacesAnimationFactory;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.custom.CustomEvent;
import net.wombatrpgs.rainfall.maps.custom.EventPressurePad;
import net.wombatrpgs.rainfall.maps.custom.EventTrickWall;
import net.wombatrpgs.rainfall.maps.events.MapEvent;

public class EventPressurePadBossFight extends EventPressurePad {
	
	public static final String ID = CustomEvent.EVENT_PREFIX + "pressure_pad_bossfight";
	
	private static final String KEY_ANIM_SEMICLOSED = "animation_wall_semiclosed";
	private static final String KEY_ANIM_SEMIOPEN= "animation_wall_semiopen";
	
	private FacesAnimation semiclosed, semiopen, closed;

	public EventPressurePadBossFight(TiledObject object, Level parent) {
		super(object, parent);
		EventTrickWall door = (EventTrickWall) parent.getCustomObject(EventTrickWall.ID);
		semiclosed = FacesAnimationFactory.create(KEY_ANIM_SEMICLOSED, door);
		semiopen = FacesAnimationFactory.create(KEY_ANIM_SEMIOPEN, door);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.custom.EventPressurePad#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		EventTrickWall door = (EventTrickWall) parent.getCustomObject(EventTrickWall.ID);
		if (closed == null) closed = door.getAppearance();
		int pressed = 0;
		for (MapEvent event : getLevel().getEventsByGroup("pad")) {
			EventPressurePad pad = (EventPressurePad) event;
			if (pad.pressed) pressed += 1;
		}
		switch (pressed) {
		case 0:
			door.setIdleAppearance(closed);
			break;
		case 1:
			semiclosed.setParent(door);
			door.setIdleAppearance(semiclosed);
			break;
		case 2:
			semiopen.setParent(door);
			door.setIdleAppearance(semiopen);
			break;
		case 3:
			RGlobal.hero.setSwitch("beat_venustron", true);
			break;
		}
	}

	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		semiclosed.queueRequiredAssets(manager);
		semiopen.queueRequiredAssets(manager);
	}

	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		semiclosed.postProcessing(manager, pass);
		semiopen.postProcessing(manager, pass);
	}
	
}
