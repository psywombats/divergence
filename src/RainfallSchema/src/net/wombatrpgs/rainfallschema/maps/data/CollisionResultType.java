/**
 *  CollisionResultType.java
 *  Created on Jan 24, 2013 1:52:17 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.maps.data;

/**
 * What happens when the hero runs into this enemy?
 */
public enum CollisionResultType {
	
	IMMOBILE,
	PUSHABLE,
	STUN,
	STUNBOUNCE,
	BOUNCE,
	INSTADEATH,

}
