/**
 *  EventLiftBottom.java
 *  Created on Mar 9, 2013 7:33:22 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.custom.instances;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.custom.CustomEvent;
import net.wombatrpgs.rainfall.maps.custom.EventButton;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.physics.RectHitbox;
import net.wombatrpgs.rainfall.scenes.SceneParser;
import net.wombatrpgs.rainfallschema.cutscene.SceneMDO;
import net.wombatrpgs.rainfallschema.maps.CustomEventMDO;

/**
 *
 */
public class EventLiftBottom extends CustomEvent {
	
	public static final String ID = CustomEvent.EVENT_PREFIX + "haven_lift";
	
	protected static final String KEY_NEEDS_PRESS = "scene_event_lift_needs_press";
	protected static final String KEY_GOT_PARTS = "scene_event_lift_got_parts";
	protected static final String KEY_FIRST_SEEN = "scene_event_lift_first_seen";
	protected static final String KEY_COMPLETE = "scene_event_lift_complete";
	
	protected static final String SWTICH_SPROCKET = "got_sprocket";
	protected static final String SWITCH_CHAIN = "got_chain";
	protected static final String SWITCH_INTRO_DONE = "intro_done";
	
	protected SceneParser needsPress, gotParts, firstSeen, complete;
	protected Hitbox box;
	protected boolean firedOnce, firedTwice;

	public EventLiftBottom(TiledObject object, Level parent) {
		super(RGlobal.data.getEntryFor(ID, CustomEventMDO.class), object, parent);
		needsPress = new SceneParser(RGlobal.data.getEntryFor(KEY_NEEDS_PRESS, SceneMDO.class));
		gotParts = new SceneParser(RGlobal.data.getEntryFor(KEY_GOT_PARTS, SceneMDO.class));
		firstSeen = new SceneParser(RGlobal.data.getEntryFor(KEY_FIRST_SEEN, SceneMDO.class));
		complete = new SceneParser(RGlobal.data.getEntryFor(KEY_COMPLETE, SceneMDO.class));
		parent.addObject(needsPress);
		parent.addObject(gotParts);
		parent.addObject(firstSeen);
		parent.addObject(complete);
		firedOnce = false;
		firedTwice = false;
		box = new RectHitbox(this, 0, 0, object.width, object.height);
	}
	
	@Override
	public Hitbox getHitbox() {
		return box;
	}

	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		needsPress.queueRequiredAssets(manager);
		gotParts.queueRequiredAssets(manager);
		firstSeen.queueRequiredAssets(manager);
		complete.queueRequiredAssets(manager);
	}

	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		needsPress.postProcessing(manager, pass);
		gotParts.postProcessing(manager, pass);
		firstSeen.postProcessing(manager, pass);
		complete.postProcessing(manager, pass);
	}
	
	@Override
	public boolean onCollide(MapEvent other, CollisionResult result) {
		if (other != RGlobal.hero) return true;
		if (!RGlobal.hero.isSet(SWITCH_INTRO_DONE)) return true;
		if (!firedOnce) {
			firedOnce = true;
			firstSeen.run(getLevel());
			return true;
		}
		if (RGlobal.hero.isSet(SWTICH_SPROCKET) && 
				RGlobal.hero.isSet(SWITCH_CHAIN) && !parent.isPaused()) {
			if (firedTwice) {
				EventButton button = (EventButton) getLevel().getCustomObject(EventButton.ID);
				if (button.pressed) {
					complete.run(getLevel());
					RGlobal.hero.setSwitch("lift_complete", true);
				} else {
					//needsPress.run(getLevel()); buggy
				}
			} else {
				firedTwice = true;
				gotParts.run(getLevel());
			}
		}
		return true;
	}

	@Override
	public void update(float elapsed) {
		super.update(elapsed);
	}

	@Override
	public void resolveWallCollision(CollisionResult result) {
		// TODO: idiocidy bug
		// note from future self: I have no idea what this means
	}
	
}
