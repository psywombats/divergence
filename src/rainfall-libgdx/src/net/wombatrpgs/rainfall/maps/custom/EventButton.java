/**
 *  EventPressurePad.java
 *  Created on Mar 2, 2013 6:13:02 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.custom;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.FacesAnimation;
import net.wombatrpgs.rainfall.graphics.OneDir;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.maps.CustomEventMDO;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

/**
 * Turns on once if something is on top of it.
 */
public class EventButton extends CustomEvent {
	
	public static final String ID = CustomEvent.EVENT_PREFIX + "button";
	
	protected static final String KEY_ANIM_DEPRESSED = "animation_button_pressed";
	
	protected FacesAnimation animPressed, animUnpressed;
	public boolean pressed;

	public EventButton(TiledObject object, Level parent) {
		super(RGlobal.data.getEntryFor(ID, CustomEventMDO.class), object, parent);
		animPressed = new OneDir(RGlobal.data.getEntryFor(
				KEY_ANIM_DEPRESSED, AnimationMDO.class), this);
		animUnpressed = getAppearance();
		pressed = false;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		animPressed.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		animPressed.postProcessing(manager, pass);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#supportsBlockLanding()
	 */
	@Override
	public boolean supportsBlockLanding() {
		return true;
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#onCharacterCollide
	 * (net.wombatrpgs.rainfall.characters.CharacterEvent, net.wombatrpgs.rainfall.physics.CollisionResult)
	 */
	@Override
	public boolean onCharacterCollide(CharacterEvent other, CollisionResult result) {
		if (!other.isOverlappingAllowed()) {
			pressed = true;
			setAppearance(animPressed);
		}
		return super.onCharacterCollide(other, result);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#getAppearance()
	 */
	@Override
	public FacesAnimation getAppearance() {
		if (!pressed) return super.getAppearance();
		else return animPressed;
	}

}
