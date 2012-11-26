/**
 *  Character.java
 *  Created on Nov 25, 2012 8:26:55 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.DirVector;
import net.wombatrpgs.rainfall.maps.Direction;
import net.wombatrpgs.rainfall.maps.MapEvent;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.maps.EventMDO;
import net.wombatrpgs.rainfallschema.settings.GameSpeedMDO;

/**
 * A placeholder class for all entities that take part in the ABS.
 */
public class Character extends MapEvent {

	public Character(Level parent, EventMDO mdo, int x, int y) {
		super(parent, mdo, x, y);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Start moving in a particular direction. Does not switch immediately to
	 * that direction but rather adds some speed in that direction based on hero
	 * walk rate.
	 * @param 	dir			The direction to move in
	 */
	public void startMove(Direction dir) {
		addMoveComponent(dir.getVector());
	}

	/**
	 * Stop moving in a particular direction. Does not switch immediately to
	 * that direction but rather adds some speed in that direction based on hero
	 * walk rate.
	 * @param 	dir			The direction to cancel velocity in 
	 */
	public void stopMove(Direction dir) {
		DirVector vec = dir.getVector();
		vec.x *= -1;
		vec.y *= -1;
		addMoveComponent(vec);
	}
	
	/**
	 * The character starts moving in the specified direction. Uses its built-in
	 * speed. (but right now it just takes it from the speed mdo)
	 * @param 	vector			The vector direction to start moving in
	 */
	protected void addMoveComponent(DirVector vector) {
		GameSpeedMDO mdo = RGlobal.data.getEntryFor("game_speed", GameSpeedMDO.class);
		float newX = this.vx + vector.x * mdo.heroWalkRate;
		float newY = this.vy + vector.y * mdo.heroWalkRate;
		this.setVelocity(newX, newY);
	}

}
