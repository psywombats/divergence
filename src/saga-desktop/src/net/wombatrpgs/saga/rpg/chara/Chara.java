/**
 *  Chara.java
 *  Created on Apr 2, 2014 10:53:07 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.chara;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.graphics.FacesAnimationFactory;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.maps.MapThing;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.saga.rpg.Battle;
import net.wombatrpgs.saga.rpg.CombatItem;
import net.wombatrpgs.saga.rpg.stats.SagaStats;
import net.wombatrpgs.sagaschema.rpg.chara.CharaMDO;
import net.wombatrpgs.sagaschema.rpg.chara.data.Gender;
import net.wombatrpgs.sagaschema.rpg.chara.data.Race;
import net.wombatrpgs.sagaschema.rpg.chara.data.Resistable;
import net.wombatrpgs.sagaschema.rpg.stats.Flag;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * An in-game character. Not called Character so as not to conflict with the one
 * in java.lang.
 */
public class Chara extends AssetQueuer implements Disposable {
	
	protected CharaMDO mdo;
	
	protected SagaStats stats;
	protected FacesAnimation appearance;
	protected Graphic portrait;
	protected CharaInventory inventory;
	protected Status status;
	protected MonsterFamily family;
	protected String name;
	
	/**
	 * Creates a new character from data template.
	 * @param	mdo				The data to create from
	 */
	public Chara(CharaMDO mdo) {
		super();
		this.mdo = mdo;
		
		name = mdo.name;
		
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
		if (MapThing.mdoHasProperty(mdo.family)) {
			family = MonsterFamily.get(mdo.family);
		}
	}
	
	/**
	 * Creates a new character from a database entry.
	 * @param	key				The key of the data to create from
	 */
	public Chara(String key) {
		this(MGlobal.data.getEntryFor(key, CharaMDO.class));
	}
	
	/** @return The current value of the request flag */
	public boolean is(Flag flag) { return stats.flag(flag); }
	
	/** @return The appearance of this character */
	public FacesAnimation getAppearance() { return appearance; }
	
	/** @return The in-battle portrait, or null if none */
	public Graphic getPortrait() { return portrait; }
	
	/** @return The human name of the character */
	public String getName() { return name; }
	
	/** @return The race of the character, never null */
	public Race getRace() { return mdo.race; }
	
	/** @return The gender of the character, never null */
	public Gender getGender() { return mdo.gender; }
	
	/** @return The status condition of the character, or null for normal */
	public Status getStatus() { return status; }
	
	/** @param status The new condition of this character, or null for normal */
	public void setStatus(Status status) { this.status = status; }
	
	/** @return The object of equipped items for this character */
	public CharaInventory getInventory() { return inventory; }
	
	/** @return True if this character isn't dead yet */
	public boolean isAlive() { return !isDead(); }
	
	/** @return The amount of GP this character drops on death */
	public int getDeathGold() { return mdo.gp; }
	
	/** @return The monster family of this character, or null if none */
	public MonsterFamily getFamily() { return family; }
	
	/** @return The power of this character's meat */
	public int getEatLevel() { return mdo.meatEatLevel; }

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
	 * Calculates the current value of the requested stat, taking into account
	 * all modifiers, equipment, and status penalties.
	 * @param	stat			The stat to get the value of
	 * @return					The value of the requested stat
	 */
	public int get(Stat stat) {
		int value = Math.round(stats.stat(stat));
		if (status != null && status.reduces(stat)) {
			value /= 2;
		}
		return value;
	}
	
	/**
	 * Determines this character's species to use for transformation info. For
	 * monsters, this will be GOBLIN etc, the monster type. Others can just
	 * return their race name.
	 * @return					The name of the transform species of this chara
	 */
	public String getSpecies() {
		if (getRace() == Race.MONSTER) {
			return mdo.species;
		} else {
			return getRace().getName();
		}
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
	 * Called when a battle with this character ends. Leveling up and stuff
	 * might go here at some point, but it'd need a callback.
	 * @param	battle			The battle that just ended
	 */
	// TODO: battle: callbacks and leveling
	public void onBattleEnd(Battle battle) {
		if (status != null) {
			status.onBattleEnd(battle, this);
		}
	}
	
	/**
	 * Called when a battle with this characted ends a round. Take care of any
	 * turn-based upkeep here.
	 * @param	battle			The battle with a round that just ended
	 */
	public void onRoundEnd(Battle battle) {
		if (status != null) {
			status.checkHeal(battle, this);
		}
	}
	
	/**
	 * Checks if this character can act and does not suffer from any status
	 * conditions (such as death) that would prevent action. Prints a message
	 * if the character cannot act.
	 * @param	battle			The battle this check is a part of
	 * @param	aboslute		True if only 100% inaction rate should be used
	 * @param	silent			True to suppress the printout
	 * @return					True if chara can act, false if not
	 */
	public boolean canAct(Battle battle, boolean absolute, boolean silent) {
		if (isDead()) return false;
		if (status != null) {
			return !status.preventsAct(battle, this, absolute, silent);
		} else {
			return true;
		}
	}
	
	/**
	 * Checks if this character is confused and randomly selects abilities and
	 * targets. Prints a message if the character falls to confusion.
	 * @param	battle			The battle this check is a part of
	 * @param	absolute		True if only 100% confusion rate should be used
	 * @param	silent			True to suppress the printout
	 * @return					True if chara is confused, false if not
	 */
	public boolean isConfused(Battle battle, boolean absolute, boolean silent) {
		if (status != null) {
			return status.actsRandomly(battle, this, absolute, silent);
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if this character can act of their own free will in battle. A
	 * chara may be unable to do this if they are dead, paralyzed, or confused.
	 * Does not print any messages.
	 * @param	battle			The battle this check is a part of
	 * @return					True if this chara can construct intents
	 */
	public boolean canConstructIntents(Battle battle) {
		return !isConfused(battle, true, true) && canAct(battle, true, true);
	}
	
	/**
	 * Checks if this character is dead or has deadly status effects.
	 * @return					True if this character has kicked the bucket
	 */
	public boolean isDead() {
		if (get(Stat.HP) <= 0) {
			status = null;
			return true;
		}
		if (status != null) {
			return status.isDeadly();
		} else {
			return false;
		}
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
	
	/**
	 * Randomly selects a combat item from all useable items this chara has.
	 * @return					A random battle-useable item, or null of none
	 */
	public CombatItem getRandomCombatItem() {
		List<CombatItem> usable = new ArrayList<CombatItem>();
		for (CombatItem item : getInventory().getItems()) {
			if (item != null && item.isBattleUsable()) {
				usable.add(item);
			}
		}
		if (usable.size() == 0) {
			return null;
		} else {
			return usable.get(MGlobal.rand.nextInt(usable.size()));
		}
	}
	
	/**
	 * Transforms this chara into a more suitable format. This should basically
	 * only be called on monsters. It overrides a bunch of equipped items, abils
	 * etc in the process, so only abils should be equipped. Meant to be called
	 * by the monster family calculating the transform.
	 * @param	newForm			The data of the new template to transform to
	 */
	public void transform(CharaMDO newForm) {
		
		// destroy old stuff
		for (CombatItem item : inventory.getItems()) {
			inventory.drop(item);
		}
		assets.remove(appearance);
		appearance.dispose();
		if (MapThing.mdoHasProperty(mdo.portrait)) {
			assets.remove(portrait);
			portrait.dispose();
		}
		
		// copy relevant stuff
		this.mdo = newForm;
		
		// create new stuff
		stats = new SagaStats(newForm.stats);
		heal(get(Stat.MHP));
		status = null;
		inventory = new CharaInventory(mdo, this);
		appearance = createSprite();
		assets.add(appearance);
		if (MapThing.mdoHasProperty(mdo.portrait)) {
			portrait = new Graphic(Constants.SPRITES_DIR, mdo.portrait);
			assets.add(portrait);
		}
		if (MapThing.mdoHasProperty(mdo.family)) {
			family = MonsterFamily.get(mdo.family);
		}
		MGlobal.assets.loadAsset(this, "transformation to " + newForm.key);
	}
	
	/**
	 * Called by the battle when this character eats another.
	 * @param	dropper			The character dropping the meat we're eating
	 */
	public void eat(Chara dropper) {
		if (getFamily() != null) {
			getFamily().transform(this, dropper);
		}
	}
	
	/**
	 * Called by the battle to check what this character would become were it to
	 * eat another character. Returns null if nothing would happen.
	 * @param	dropper			The character dropping the meat we're eating
	 * @return					The MDO of this character after, or null
	 */
	public CharaMDO predictEat(Chara dropper) {
		if (getFamily() != null) {
			return getFamily().getTransformResult(this, dropper);
		} else {
			return null;
		}
	}

}
