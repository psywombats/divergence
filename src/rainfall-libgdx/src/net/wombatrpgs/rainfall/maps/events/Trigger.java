/**
 *  TriggerEvent.java
 *  Created on Feb 4, 2013 2:04:30 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.events;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.physics.RectHitbox;
import net.wombatrpgs.rainfall.scenes.SceneParser;
import net.wombatrpgs.rainfallschema.cutscene.SceneMDO;

import com.badlogic.gdx.maps.MapObject;

/**
 * An event that triggers a cutscene or other scene script.
 */
public class Trigger extends MapEvent {
	
	protected static final String PROPERTY_ID = "id";
	protected static final String PROPERTY_AUTOSTART = "auto";
	protected static final String PROPERTY_CONDITION = "condition";
	
	protected SceneParser scene;
	protected Hitbox box;
	protected TriggerCondition condition;
	protected boolean autorun;
	
	/**
	 * Constructs a new trigger. Called by the superclass factory method.
	 * @param 	parent			The parent level of this event
	 * @param 	object			The tiled object that this object is based on
	 */
	protected Trigger(Level parent, MapObject object) {
		super(parent, object, false, true);
		// TODO: correct this awful width thing
		this.box = new RectHitbox(this, 
				0,
				-1 * extractHeight(parent, object),
				extractWidth(parent, object),
				0);
		String id = getProperty(PROPERTY_ID);
		if (id == null) {
			RGlobal.reporter.warn("Trigger event had no id: " + object);
		} else {
			SceneMDO mdo = RGlobal.data.getEntryFor(id, SceneMDO.class);
			scene = new SceneParser(mdo, getLevel());
			assets.add(scene);
		}
		String conditionKey = getProperty(PROPERTY_CONDITION);
		if (conditionKey != null) {
			condition = TriggerCondition.create(this, conditionKey);
		}
		autorun = getProperty(PROPERTY_AUTOSTART) != null;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#getHitbox()
	 */
	@Override
	public Hitbox getHitbox() {
		return box;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#onCollide
	 * (net.wombatrpgs.rainfall.maps.MapObject, 
	 * net.wombatrpgs.rainfall.physics.CollisionResult)
	 */
	@Override
	public boolean onCollide(MapEvent other, CollisionResult result) {
		if (other != RGlobal.hero) return true;
		run();
		return true;
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (autorun) run();
	}
	
	/**
	 * Exceutes (maybe) the underlying scene.
	 */
	protected void run() {
		if ((condition == null || condition.shouldExecute()) && !hidden()) {
			scene.run(parent);
		}
	}

}
