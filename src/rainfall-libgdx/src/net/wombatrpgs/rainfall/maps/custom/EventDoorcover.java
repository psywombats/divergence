/**
 *  EventDoorCover.java
 *  Created on Mar 15, 2013 7:43:03 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.custom;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.FacesAnimation;
import net.wombatrpgs.rainfall.graphics.FacesAnimationFactory;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.AnimationListener;
import net.wombatrpgs.rainfall.maps.events.AnimationPlayer;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.maps.CustomEventMDO;

/**
 * Covering for a door that slides like a Zelda.
 */
public class EventDoorcover extends CustomEvent {
	
	public static final String ID = CustomEvent.EVENT_PREFIX + "doorcover";
	
	protected static final String KEY_ANIM_CLOSE = "animation_doorcover";
	protected static final String KEY_ANIM_CLOSED = "animation_doorcover_static";
	
	protected AnimationPlayer animClose;
	protected FacesAnimation closedAppearance;
	protected boolean open;
	
	public EventDoorcover(TiledObject object, Level parent) {
		super(RGlobal.data.getEntryFor(ID, CustomEventMDO.class), object, parent);
		final EventDoorcover door = this;
		animClose = new AnimationPlayer(RGlobal.data.getEntryFor(
				KEY_ANIM_CLOSE, AnimationMDO.class));
		animClose.addListener(new AnimationListener() {
			@Override
			public void onAnimationFinish(AnimationPlayer source) {
				door.setIdleAppearance(closedAppearance);
			}
		});
		closedAppearance = FacesAnimationFactory.create(KEY_ANIM_CLOSED, this);
		open = false;
	}

	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		closedAppearance.queueRequiredAssets(manager);
		animClose.queueRequiredAssets(manager);
	}

	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		closedAppearance.postProcessing(manager, pass);
		animClose.postProcessing(manager, pass);
	}
	
	public void close() {
		if (!open) return;
		parent.addEvent(animClose, parent.getZ(this));
		animClose.start();
		animClose.setX(getX());
		animClose.setY(getY());
		open = false;
	}

}
