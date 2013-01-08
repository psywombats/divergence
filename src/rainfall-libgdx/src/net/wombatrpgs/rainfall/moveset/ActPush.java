/**
 *  ActPush.java
 *  Created on Dec 29, 2012 12:44:28 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.moveset;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.hero.moveset.PushMDO;

/**
 * Push or pull the block.
 */
public class ActPush implements Actionable {
	
	protected PushMDO mdo;

	public ActPush(PushMDO mdo) {
		this.mdo = mdo;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.moveset.Actionable#act
	 * (net.wombatrpgs.rainfall.maps.Level)
	 */
	@Override
	public void act(Level map) {
		// TODO Auto-generated method stub
		Global.reporter.inform("Act " + this);
	}

}
