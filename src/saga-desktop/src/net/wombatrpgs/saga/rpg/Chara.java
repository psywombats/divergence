/**
 *  Chara.java
 *  Created on Apr 2, 2014 10:53:07 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.graphics.FacesAnimationFactory;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.maps.MapThing;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.sagaschema.rpg.chara.CharacterMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.Gender;
import net.wombatrpgs.sagaschema.rpg.chara.data.Race;
import net.wombatrpgs.sagaschema.rpg.chara.data.Resistable;
import net.wombatrpgs.sagaschema.rpg.chara.data.Status;
import net.wombatrpgs.sagaschema.rpg.stats.Flag;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * An in-game character. Not called Character so as not to conflict with the one
 * in java.lang.
 */
public class Chara extends AssetQueuer implements Disposable {
	
	protected CharacterMDO mdo;
	
	protected SagaStats stats;
	protected FacesAnimation appearance;
	protected Graphic portrait;
	protected CharaInventory inventory;
	protected Status status;
	
	/**
	 * Creates a new character from data template.
	 * @param	mdo				The data to create from
	 */
	public Chara(CharacterMDO mdo) {
		super();
		this.mdo = mdo;
		
		stats = new SagaStats(mdo.stats);
		inventory = new CharaInventory(mdo, this);
		status = null;
		
		appearance = createSprite();
		assets.add(appearance);
		if (MapThing.mdoHasProperty(mdo.portrait)) {
			portrait = new Graphic(Constants.SPRITES_DIR, mdo.portrait);
			assets.add(portrait);
		}
	}
	
	/**
	 * Creates a new character from a database entry.
	 * @param	key				The key of the data to create from
	 */
	public Chara(String key) {
		this(MGlobal.data.getEntryFor(key, CharacterMDO.class));
	}
	
	/** @return The current value of the requested stat */
	public float get(Stat stat) { return stats.stat(stat); }
	
	/** @return The current value of the request flag */
	public boolean is(Flag flag) { return stats.flag(flag); }
	
	/** @return The appearance of this character */
	public FacesAnimation getAppearance() { return appearance; }
	
	/** @return The in-battle portrait, or null if none */
	public Graphic getPortrait() { return portrait; }
	
	/** @return The human name of the character */
	public String getName() { return mdo.name; }
	
	/** @return The race of the character, never null */
	public Race getRace() { return mdo.race; }
	
	/** @return The gender of the character, never null */
	public Gender getGender() { return mdo.gender; }
	
	/** @return The status condition of the character, or null for normal */
	public Status getStatus() { return status; }
	
	/** @return The object of equipped items for this character */
	public CharaInventory getInventory() { return inventory; }

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		appearance.dispose();
	}
	
	/**
	 * Creates an animation for use wherever. Use this if you need to have
	 * animations in multiple places rather than just the standard anim of this
	 * character. Make sure to queue and dispose it.
	 * @return					A new sprite for this character
	 */
	public FacesAnimation createSprite() {
		return FacesAnimationFactory.create(mdo.appearance);
	}

	/**
	 * Checks if this character resists a certain damage or status type.
	 * @param	type			The damage or status to check
	 * @return					True if this character resists that type
	 */
	public boolean resists(Resistable type) {
		for (Flag flag : type.getResistFlag()) {
			if (is(flag)) return true;
		}
		return false;
	}
	
	/**
	 * Checks if this character is weak to a certain damage or status type.
	 * @param	type			The damage or status to check
	 * @return					True if this character resists that type
	 */
	public boolean isVulnerableTo(Resistable type) {
		for (Flag flag : type.getWeakFlag()) {
			if (is(flag)) return true;
		}
		return false;
	}
	
	/**
	 * Modifies this character's stats in accordance with the given set.
	 * @param	mod				The values to modify by
	 * @param	decombine		True to decombine rather than apply stats
	 */
	protected void applyStatset(SagaStats mod, boolean decombine) {
		float oldMHP = mod.stat(Stat.MHP);
		if (mdo.race != Race.ROBOT) {
			mod.setStat(Stat.MHP,  0);
		}
		if (decombine) {
			stats.decombine(mod);
		} else {
			stats.combine(mod);
		}
		mod.setStat(Stat.MHP, oldMHP);
	}

}
