/**
 *  NpcEvent.java
 *  Created on Jan 22, 2014 9:21:33 PM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.maps;

import net.wombatrpgs.saga.rpg.CharacterEvent;
import net.wombatrpgs.sagaschema.characters.NpcMDO;

/**
 * An NPC is a guy that sits in pubs all day and says one-liners. Does not
 * include shopkeepers, roaming monsters, or dead people.
 */
public class NpcEvent extends CharacterEvent {
	
	protected NpcMDO mdo;

	/**
	 * Creates a new NPC from mdo. If you want to make one from a map object,
	 * there should be code in EventFactory for that.
	 * @param	mdo				The data to generate from
	 * @param	parent			The parent to generate for
	 */
	public NpcEvent(NpcMDO mdo, Level parent) {
		super(mdo, parent);
		this.mdo = mdo;
	}

}
