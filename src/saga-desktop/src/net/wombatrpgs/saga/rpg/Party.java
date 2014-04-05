/**
 *  Party.java
 *  Created on Apr 4, 2014 6:39:19 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.sagaschema.rpg.chara.PartyMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.PartyEntryMDO;

/**
 * Just a collection of characters. The player party is in a different subclass.
 */
public class Party implements Queueable {
	
	protected List<List<Chara>> groups;
	protected List<Chara> members;
	
	/**
	 * Creates a party from data.
	 * @param	mdo				The data to create party from
	 */
	public Party(PartyMDO mdo) {
		groups = new ArrayList<List<Chara>>();
		members = new ArrayList<Chara>();
		for (PartyEntryMDO entryMDO : mdo.members) {
			List<Chara> group = new ArrayList<Chara>();
			for (int i = 0; i < entryMDO.count; i += 1) {
				Chara chara = new Chara(entryMDO.monster);
				group.add(chara);
				members.add(chara);
			}
			groups.add(group);
		}
	}
	
	/** @return The number of groups in this party */
	public int groupCount() { return groups.size(); }
	
	/** @return The total number of characters in the party */
	public int size() { return members.size(); }
	
	/** @return The nth monster group */
	public List<Chara> getGroup(int n) { return groups.get(n); }
	
	/** @return All members of this party */
	public List<Chara> getAll() { return members; }
	
	/** @return A representative of the nth group */
	public Chara getFrontMember(int n) { return getGroup(n).get(0); }

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Chara member : members) {
			member.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Chara member : members) {
			member.postProcessing(manager, pass);
		}
	}

}
