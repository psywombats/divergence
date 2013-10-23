/**
 *  TriggerEvent.java
 *  Created on Feb 4, 2013 2:04:30 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.events;

import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.scenes.SceneParser;

/**
 * An event that triggers a cutscene or other scene script.
 */
public class Trigger extends MapEvent {
	
	protected static final String PROPERTY_ID = "id";
	protected static final String PROPERTY_AUTOSTART = "auto";
	protected static final String PROPERTY_CONDITION = "condition";
	
	protected SceneParser scene;
	protected TriggerCondition condition;
	protected boolean autorun;
	
	/**
	 * Constructs a new trigger. Called by the superclass factory method.
	 * @param 	parent			The parent level of this event
	 */
	protected Trigger(Level parent) {
		super(parent);
//		// TODO: Trigger
//		this.box = new RectHitbox(this, 
//				0,
//				0,
//				extractWidth(parent, object),
//				extractHeight(parent, object));
//		String id = getProperty(PROPERTY_ID);
//		if (id == null) {
//			MGlobal.reporter.warn("Trigger event had no id: " + object);
//		} else {
//			SceneMDO mdo = MGlobal.data.getEntryFor(id, SceneMDO.class);
//			scene = new SceneParser(mdo, getLevel());
//			assets.add(scene);
//		}
//		String conditionKey = getProperty(PROPERTY_CONDITION);
//		if (conditionKey != null) {
//			condition = TriggerCondition.create(this, conditionKey);
//		}
//		autorun = getProperty(PROPERTY_AUTOSTART) != null;
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#update(float)
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
			scene.run();
		}
	}

}
