/**
 *  EventChest.java
 *  Created on Mar 3, 2013 7:34:20 AM for project rainfall-libgdx
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
import net.wombatrpgs.rainfall.scenes.SceneParser;
import net.wombatrpgs.rainfallschema.cutscene.SceneMDO;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.maps.CustomEventMDO;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

/**
 * A wall that opens.
 */
public class EventChest extends CustomEvent {
	
	public static final String ID = CustomEvent.EVENT_PREFIX + "chest";
	
	protected static final String KEY_ANIM_OPEN = "animation_chest_opened";
	
	protected static final String PROPERTY_ITEM_NAME = "item";
	protected static final String PROPERTY_SCENE = "scene";
	
	protected SceneParser scene;
	protected FacesAnimation openAppearance;
	public boolean open;

	public EventChest(TiledObject object, Level parent) {
		super(RGlobal.data.getEntryFor(ID, CustomEventMDO.class), object, parent);
		open = false;
		openAppearance = new OneDir(RGlobal.data.getEntryFor(
				KEY_ANIM_OPEN, AnimationMDO.class), this);
		String sceneKey = object.properties.get(PROPERTY_SCENE);
		if (sceneKey == null) {
			RGlobal.reporter.warn("Treasure chest with no scene: " + this);
		} else {
			scene = new SceneParser(RGlobal.data.getEntryFor(sceneKey, SceneMDO.class));
		}
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		openAppearance.queueRequiredAssets(manager);
		if (scene != null) {
			scene.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		openAppearance.postProcessing(manager, pass);
		if (scene != null) {
			scene.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#onCharacterCollide
	 * (net.wombatrpgs.rainfall.characters.CharacterEvent,
	 * net.wombatrpgs.rainfall.physics.CollisionResult)
	 */
	@Override
	public boolean onCharacterCollide(CharacterEvent other, CollisionResult result) {
		if (!open && other == RGlobal.hero) {
			setAppearance(openAppearance);
			open = true;
			scene.run(getLevel());
		}
		return super.onCharacterCollide(other, result);
	}

}
