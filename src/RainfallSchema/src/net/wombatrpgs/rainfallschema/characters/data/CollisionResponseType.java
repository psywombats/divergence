/**
 *  CollisionResultType.java
 *  Created on Jan 24, 2013 1:52:17 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters.data;

/**
 * What happens when the hero runs into this enemy?
 */
public enum CollisionResponseType {
	
	ETHEREAL,
	IMMOBILE,
	PUSHABLE,
	STUN,
	STUNBOUNCE,
	BOUNCE,
	INSTADEATH,

}
