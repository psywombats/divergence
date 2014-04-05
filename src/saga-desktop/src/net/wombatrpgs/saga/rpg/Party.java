/**
 *  Party.java
 *  Created on Apr 4, 2014 6:39:19 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.sagaschema.rpg.chara.PartyMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.PartyEntryMDO;

/**
 * Just a collection of characters. The player party is in a different subclass.
 */
public class Party {
	
	protected List<List<Chara>> members;
	
	/**
	 * Creates a party from data.
	 * @param	mdo				The data to create party from
	 */
	public Party(PartyMDO mdo) {
		members = new ArrayList<List<Chara>>();
		for (PartyEntryMDO entryMDO : mdo.members) {
			List<Chara> group = new ArrayList<Chara>();
			for (int i = 0; i < entryMDO.count; i += 1) {
				group.add(new Chara(entryMDO.monster));
			}
			members.add(group);
		}
	}
	
	/** @return The number of groups in this party */
	public int groupCount() { return members.size(); }
	
	/** @return The nth monster group */
	public List<Chara> getGroupMembers(int n) { return members.get(n); }
	
	/** @return A representative of the nth group */
	public Chara getMember(int n) { return getGroupMembers(n).get(0); }

}
