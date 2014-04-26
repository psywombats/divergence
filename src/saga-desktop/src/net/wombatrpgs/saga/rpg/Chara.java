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
		heal(get(Stat.MHP));
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
	public int get(Stat stat) { return Math.round(stats.stat(stat)); }
	
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
	
	/** @return True if this character has moved on to the next life */
	public boolean isDead() { return get(Stat.HP) <= 0 || status == Status.STONE; }
	
	/** @return True if this character isn't dead yet */
	public boolean isAlive() { return !isDead(); }

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		appearance.dispose();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName() + "(" + super.toString() + ")";
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
		for (Flag flag : type.getResistFlags()) {
			if (is(flag)) return true;
		}
		return false;
	}
	
	/**
	 * Checks if this character is weak to a certain damage or status type.
	 * @param	type			The damage or status to check
	 * @return					True if this character resists that type
	 */
	public boolean isWeakTo(Resistable type) {
		for (Flag flag : type.getWeakFlags()) {
			if (is(flag)) return true;
		}
		return false;
	}
	
	/**
	 * Called when this character adds the listed item to their inventory.
	 * @param	item			The item being equipped
	 */
	public void onEquip(CombatItem item) {
		applyStatset(item.getStatset(), false);
		if (getRace() == Race.ROBOT) {
			item.halveUses();
			applyStatset(item.getRobostats(), false);
		}
	}
	
	/**
	 * Called when this character removes the listed item from their inventory.
	 * @param	item			The item being unequipped
	 */
	public void onUnequip(CombatItem item) {
		applyStatset(item.getStatset(), true);
		if (getRace() == Race.ROBOT) {
			item.halveUses();
			applyStatset(item.getRobostats(), true);
		}
	}
	
	/**
	 * Checks if this character can act and does not suffer from any status
	 * conditions (such as death) that would prevent action.
	 * @return					True if chara can act, false if not
	 */
	public boolean canAct() {
		if (isDead()) return false;
		if (status != null && status.preventsAction()) return false;
		return true;
	}
	
	/**
	 * Causes this character to lose some HP. Defense etc is not checked. Death
	 * can be inflicted, but it will be silent.
	 * @param	damage			The damage to deal, in HP
	 * @return					True if this character died from the damage
	 */
	public boolean damage(int damage) {
		stats.subtract(Stat.HP, damage);
		if (get(Stat.HP) <= 0) {
			stats.setStat(Stat.HP, 0);
			return true;
		}
		return false;
	}
	
	/**
	 * Causes this character to regain HP. Caps at the chara's max HP.
	 * @param	heal			The damage amount to heal, in HP
	 * @return					The damage actually healed
	 */
	public int heal(int heal) {
		int old = get(Stat.HP);
		stats.add(Stat.HP, heal);
		if (get(Stat.HP) > get(Stat.MHP)) {
			stats.setStat(Stat.HP, get(Stat.MHP));
		}
		return get(Stat.HP) - old; 
	}
	
	/**
	 * Modifies this character's stats in accordance with the given set.
	 * @param	mod				The values to modify by
	 * @param	decombine		True to decombine rather than apply stats
	 */
	public void applyStatset(SagaStats mod, boolean decombine) {
		if (decombine) {
			stats.decombine(mod);
		} else {
			stats.combine(mod);
		}
	}

}
