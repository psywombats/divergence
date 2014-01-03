/**
 *  TriggerCondition.java
 *  Created on Mar 7, 2013 7:18:26 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.events;

/**
 * A custom thing that can cause triggers not to function. A trigger for
 * triggers, if you will. Not a tigger.
 */
public abstract class TriggerCondition {
	
	protected Trigger parent;
	
	/**
	 * Determines whether the parent event should fire its cutscene. Should do
	 * this by evaluating the game state.
	 * @return				True if parent should fire, false if no
	 */
	public abstract boolean shouldExecute();
	
	// good god why does this still exist
	/**
	 * Creates a condition based on a key. It's sort of a factory method.
	 * @param parent
	 * @param key
	 * @return
	 */
	public static TriggerCondition create(Trigger parent, String key) {
//		final Level map = parent.getLevel();
//		if (key.equals("beetles_dead")) {
//			return new TriggerCondition() {
//				private TimerObject timer;
//				@Override public boolean shouldExecute() {
//					for (MapEvent event : map.getEventsByGroup("beetles")) {
//						if (((CharacterEvent) event).canAct()) return false;
//					}
//					if (timer == null) {
//						timer = new TimerObject(3.5f);
//						map.addObject(timer);
//						timer.set(true);
//					}
//					if (timer.hasCompleted()) {
//						map.removeObject(timer);
//						return true;
//					} else {
//						return false;
//					}
//					return false;
//				}
//			};
//		} else {
//			MGlobal.reporter.warn("No trigger condition for key: " + key);
			return null;
//		}
	}

}
