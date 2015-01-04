/**
 *  SaveDescriptor.java
 *  Created on Jul 21, 2014 5:46:55 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.saga.rpg.chara.Chara;

/**
 * An actual description of the save file that gets stored in the index. It can
 * be queued/loaded to make sure the sprites are in memory, but they are created
 * and read in from the memory index.
 */
public class SaveDescriptor {
	
	public List<String> spriteKeys;
	public String leaderString;
	public String location;
	public String fileName;
	public Date lastSaved;
	public int build;
	
	/** @return The name of the leader of the saved party */
	public String getLeaderString() { return leaderString; }
	
	/** @return The name of the location of the saved party */
	public String getLocation() {  return location; }
	
	/** @return The sprite keys used for this descriptor */
	public List<String> getSpriteKeys() { return spriteKeys; }
	
	/**
	 * Generates a new save descriptor for the party currently in memory.
	 * @return					A descriptor for the in-memory party
	 */
	public static SaveDescriptor generateDescriptor() {
		SaveDescriptor save = new SaveDescriptor();
		save.spriteKeys = new ArrayList<String>();
		for (Chara hero : SGlobal.heroes.getAll()) {
			FacesAnimation sprite = hero.createSprite();
			save.spriteKeys.add(sprite.getKey());
		}
		Chara leader = SGlobal.heroes.findLeader();
		save.leaderString = leader.getName();
//		save.leaderString = leader.getName() + "   " + leader.get(Stat.HP) +
//				"/" + leader.get(Stat.MHP);
		save.location = SGlobal.heroes.getLocation();
		save.lastSaved = new Date();
		save.build = SConstants.SAVE_BUILD;
		save.fileName = save.getLeaderString().replace(' ', '_');
		save.fileName += ("_" + save.getDateString());
		save.fileName += ("_" + System.currentTimeMillis());
		return save;
	}
	
	/**
	 * Converts the save date into human-readable format.
	 * @return					The mm/yy/dd whatever data conversion
	 */
	@JsonIgnore
	public String getDateString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(lastSaved);
	}
	
	/**
	 * Checks if this save is outdated. Because the serialization system sucks
	 * and there's no migration system in place, loading this save file would
	 * probably crash.
	 * @return					True for outdated, false for current build
	 */
	@JsonIgnore
	public boolean isOutdated() {
		return build != SConstants.SAVE_BUILD;
	}

}
