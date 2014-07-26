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

import com.badlogic.gdx.utils.Disposable;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.saga.rpg.chara.Chara;

/**
 * An actual description of the save file that gets stored in the index. It can
 * be queued/loaded to make sure the sprites are in memory, but they are created
 * and read in from the memory index.
 */
public class SaveDescriptor extends AssetQueuer implements Disposable {
	
	protected List<FacesAnimation> sprites;
	protected String leaderString;
	protected String location;
	protected Date lastSaved;
	
	/**
	 * Creates a new descriptor describing pretty much nothing.
	 */
	public SaveDescriptor() {
		
	}
	
	/** @return The name of the leader of the saved party */
	public String getLeaderString() { return leaderString; }
	
	/** @return The name of the location of the saved party */
	public String getLocation() {  return location; }
	
	/** @return The date the saved data was last saved */
	public Date getSaveDate() { return lastSaved; }
	
	/** @return The sprites used for this descriptor */
	public List<FacesAnimation> getSprites() { return sprites; }
	
	/**
	 * Generates a new save descriptor for the party currently in memory.
	 * @return					A descriptor for the in-memory party
	 */
	public static SaveDescriptor generateDescriptor() {
		SaveDescriptor save = new SaveDescriptor();
		save.sprites = new ArrayList<FacesAnimation>();
		for (Chara hero : SGlobal.heroes.getAll()) {
			FacesAnimation sprite = hero.createSprite();
			save.assets.add(sprite);
			save.sprites.add(sprite);
		}
		Chara leader = SGlobal.heroes.findLeader();
		save.leaderString = leader.getName();
//		save.leaderString = leader.getName() + "   " + leader.get(Stat.HP) +
//				"/" + leader.get(Stat.MHP);
		save.location = SGlobal.heroes.getLocation();
		save.lastSaved = new Date();
		return save;
	}

	/**
	 * @see com.badlogic.gdx.utils.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		for (FacesAnimation sprite : sprites) {
			sprite.dispose();
		}
	}
	
	/**
	 * Converts the save date into human-readable format.
	 * @return					The mm/yy/dd whatever data conversion
	 */
	public String getDateString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(lastSaved);
	}

}
