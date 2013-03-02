/**
 *  EventTrickDoor.java
 *  Created on Mar 2, 2013 7:34:20 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.custom;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.FacesAnimation;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.AnimationListener;
import net.wombatrpgs.rainfall.maps.events.AnimationPlayer;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.physics.NoHitbox;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.maps.CustomEventMDO;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

/**
 * A wall that opens.
 */
public class EventTrickWall extends CustomEvent {
	
	public static final String ID = CustomEvent.EVENT_PREFIX + "trick_wall";
	
	protected static final String KEY_ANIM_OPEN = "animation_wall_open";
	protected static final String KEY_ANIM_CLOSE = "animation_wall_close";
	
	protected AnimationPlayer animOpen;
	protected AnimationPlayer animClose;
	protected FacesAnimation closedAppearance;
	
	public boolean open;

	public EventTrickWall(TiledObject object, Level parent) {
		super(RGlobal.data.getEntryFor(ID, CustomEventMDO.class), object, parent);
		final EventTrickWall door = this;
		animOpen = new AnimationPlayer(RGlobal.data.getEntryFor(
				KEY_ANIM_OPEN, AnimationMDO.class));
		animClose = new AnimationPlayer(RGlobal.data.getEntryFor(
				KEY_ANIM_CLOSE, AnimationMDO.class));
		animClose.addListener(new AnimationListener() {
			@Override
			public void onAnimationFinish(AnimationPlayer source) {
				door.setAppearance(closedAppearance);
			}
		});
		open = false;
		closedAppearance = getAppearance();
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		animOpen.queueRequiredAssets(manager);
		animClose.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		animOpen.postProcessing(manager, pass);
		animClose.postProcessing(manager, pass);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#getHitbox()
	 */
	@Override
	public Hitbox getHitbox() {
		if (!open) return super.getHitbox();
		else return NoHitbox.getInstance();
	}

	public void open() {
		if (open) return;
		if (parent.contains(animClose)) {
			parent.removeEvent(animClose);
		}
		setAppearance(null);
		parent.addEvent(animOpen, parent.getZ(this));
		animOpen.setY(getY());
		animOpen.setX(getX());
		animOpen.start();
		open = true;
	}
	
	public void close() {
		if (!open) return;
		if (parent.contains(animOpen)) {
			parent.removeEvent(animOpen);
		}
		parent.addEvent(animClose, parent.getZ(this));
		animClose.start();
		animClose.setX(getX());
		animClose.setY(getY());
		open = false;
	}

}
