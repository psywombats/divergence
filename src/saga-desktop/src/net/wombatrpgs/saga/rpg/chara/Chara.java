/**
 *  Chara.java
 *  Created on Apr 2, 2014 10:53:07 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.chara;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.Constants;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.lua.Lua;
import net.wombatrpgs.mgne.core.lua.LuaConvertable;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.graphics.FacesAnimationFactory;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.maps.MapThing;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.battle.Battle;
import net.wombatrpgs.saga.rpg.items.CharaInventory;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.rpg.mutant.MutantEvent;
import net.wombatrpgs.saga.rpg.mutant.Mutation;
import net.wombatrpgs.saga.rpg.mutant.MutationManager;
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
public class Chara extends AssetQueuer implements Disposable, LuaConvertable {
	
	// careful, this is only guaranteed for NPCs
	protected CharaMDO mdo;
	
	protected SagaStats stats;
	protected FacesAnimation appearance;
	protected Graphic portrait;
	protected CharaInventory inventory;
	protected Status status;
	protected MonsterFamily family;
	protected String name;
	protected MutationManager mutantManager;
	protected Race race;
	protected Gender gender;
	protected String species;
	protected transient LuaValue lua;
	
	/**
	 * Creates a new character from data template.
	 * @param	mdo				The data to create from
	 */
	public Chara(CharaMDO mdo) {
		super();
		this.mdo = mdo;
		
		name = mdo.name;
		gender = mdo.gender;
		race = mdo.race;
		species = mdo.species;
		
		stats = new SagaStats(mdo.stats);
		inventory = new CharaInventory(mdo, this);
		heal(get(Stat.MHP));
		status = null;
		
		appearance = FacesAnimationFactory.create(mdo.appearance);
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
	
	/**
	 * Creates a new character from saved serialized data.
	 * @param	memory			The memory to create from
	 */
	public Chara(CharaMemory memory) {
		this.stats = memory.stats;
		this.inventory = new CharaInventory(this, memory.inventory);
		this.name = memory.name;
		this.race = memory.race;
		this.gender = memory.gender;
		this.species = memory.species;
		
		this.appearance = FacesAnimationFactory.create(memory.animKey);
		assets.add(appearance);
		appearance.startMoving();
		
		if (MapThing.mdoHasProperty(memory.statusKey)) {
			this.status = Status.get(memory.statusKey);
		}
		if (MapThing.mdoHasProperty(memory.graphicKey)) {
			this.portrait = new Graphic("", memory.graphicKey);
			assets.add(portrait);
		}
		if (MapThing.mdoHasProperty(memory.monsterFamilyKey)) {
			this.family = MonsterFamily.get(memory.monsterFamilyKey);
		}
	}
	
	/**	@param flag The flag to check
	 *	@return The current value of the request flag */
	public boolean is(Flag flag) { return stats.flag(flag); }
	
	/** @return The appearance of this character */
	public FacesAnimation getAppearance() { return appearance; }
	
	/** @return The in-battle portrait, or null if none */
	public Graphic getPortrait() { return portrait; }
	
	/** @return The human name of the character */
	public String getName() { return name; }
	
	/** @param name The new name for this character */
	public void setName(String name) { this.name = name; }
	
	/** @return The stat values of this chara, don't use this */
	public SagaStats getStats() { return this.stats; }
	
	/** @return The race of the character, never null */
	public Race getRace() { return race; }
	
	/** @return The gender of the character, never null */
	public Gender getGender() { return gender; }
	
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
	
	/** @return The power of meat needed for this character */
	public int getTargetLevel() { return mdo.meatTargetLevel; }
	
	/** @return The key of the loot this chara can drop, or null or None */
	public String getLootKey() { return mdo.loot; }
	
	/**
	 * @see net.wombatrpgs.mgne.core.lua.LuaConvertable#toLua()
	 */
	@Override
	public LuaValue toLua() {
		if (lua == null) {
			regenerateLua();
		}
		return lua;
	}

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
	 * Gets the unique key of the monster family, if it exists.
	 * @return					The mdo key of the mon fam, or empty string
	 */
	public String getMonsterFamilyKey() {
		if (family == null) {
			return null;
		} else {
			return family.getKey();
		}
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
			return species;
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
		return FacesAnimationFactory.create(appearance.getKey());
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
			//item.halveUses();
			applyStatset(item.getRobostats(), true);
			heal(0);
		}
	}
	
	/**
	 * Called when a battle with this character begins.
	 * @param	battle			The battle that just started
	 */
	public void onBattleStart(Battle battle) {
		mutantManager = new MutationManager(this, battle);
	}
	
	/**
	 * Called when a battle with this character ends.
	 * @param	battle			The battle that just ended
	 */
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
		if (isDead()) {
			return;
		}
		if (status != null) {
			status.checkHeal(battle, this);
			if (status != null) {
				status.onRoundEnd(battle, this);
			}
		}
		if (is(Flag.REGENERATING)) {
			int toRegen = get(Stat.MHP) / 10;
			if (get(Stat.HP) + toRegen > get(Stat.MHP)) {
				toRegen = get(Stat.MHP) - get(Stat.HP); 
			}
			if (toRegen > 0) {
				battle.println(getName() + " recovers by " + toRegen + ".");
				heal(toRegen);
			}
		}
	}
	
	/**
	 * Checks if this character can act and does not suffer from any status
	 * conditions (such as death) that would prevent action. Prints a message
	 * if the character cannot act.
	 * @param	battle			The battle this check is a part of
	 * @param	absolute		True if only 100% inaction rate should be used
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
	 * Checks if this character has any of the provided flags.
	 * @param	flags			The flags to check for
	 * @return					True if any exist on this character
	 */
	public boolean isAny(Collection<Flag> flags) {
		for (Flag flag : flags) {
			if (is(flag)) return true;
		}
		return false;
	}
	
	/**
	 * Checks if this character is carrying at item of the given type. Note that
	 * this checks keys rather than specific instantiations of the item.
	 * @param	itemKey			The key of the item to search for
	 * @return					True if this character is carrying that type
	 */
	public boolean isCarryingItemType(String itemKey) {
		return inventory.containsItemType(itemKey);
	}
	
	/**
	 * Modifies the character's base stat by a certain amount.
	 * @param	stat			The stat to modify
	 * @param	delta			The amount to modify it by
	 */
	public void modifyStat(Stat stat, int delta) {
		stats.add(stat, delta);
	}
	
	/**
	 * Causes this character to lose some HP. Defense etc is not checked. Death
	 * can be inflicted, but it will be silent. The physical nature of the
	 * attack is only useful insofar as a mutation is concerned.
	 * @param	damage			The damage to deal, in HP
	 * @param	physical		True if damage inflicted was physical
	 * @return					True if this character died from the damage
	 */
	public boolean damage(int damage, boolean physical) {
		stats.subtract(Stat.HP, damage);
		mutantManager.recordEvent(MutantEvent.DAMAGED);
		if (physical) {
			mutantManager.recordEvent(MutantEvent.DAMAGED_PHYSICALLY);
		}
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
	 * Heals all of this character's HP.
	 */
	public void restoreHP() {
		heal(get(Stat.MHP));
	}
	
	/**
	 * Restores all natural (or robot-y) abilities to full power.
	 */
	public void resotreAbilUses() {
		inventory.restoreAbilUses();
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
		appearance = null;
		if (MapThing.mdoHasProperty(mdo.portrait)) {
			assets.remove(portrait);
			portrait.dispose();
		}
		
		// copy relevant stuff
		this.mdo = newForm;
		this.species = newForm.species;
		
		// create new stuff
		stats = new SagaStats(newForm.stats);
		heal(get(Stat.MHP));
		status = null;
		inventory = new CharaInventory(mdo, this);
		appearance = FacesAnimationFactory.create(mdo.appearance);
		assets.add(appearance);
		SGlobal.heroes.setLeaderAppearance();
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
	
	/**
	 * Checks if this character knows the given combat item as an ability. If
	 * the item is null, not held by chara, or not the exact instance of an
	 * abil owned by this chara, returns false.
	 * @param	item			The item to check
	 * @return					True if that item is an abil of this chara
	 */
	public boolean knowsAsAbil(CombatItem item) {
		int slot = inventory.slotFor(item);
		return inventory.reservedAt(slot);
	}
	
	/**
	 * Records a mutation event that can influence mutant direction.
	 * @param	event			The event that occurred
	 */
	public void recordEvent(MutantEvent event) {
		mutantManager.recordEvent(event);
	}
	
	/**
	 * Produces a set of mutation options available for mutants for the most
	 * recent battle. This will be called once per battle. Should return null
	 * if not a mutant or the RNG says no mutations this battle.
	 * @return					A list of mutation options, or null
	 */
	public List<Mutation> generateMutations() {
		if (!SGlobal.settings.getMutations().shouldMutate()) {
			return null;
		} else if (getRace() != Race.MUTANT) {
			return null;
		} else {
			return mutantManager.produceOptions();
		}
	}
	
	/**
	 * Generates lua object with some basic commands to get chara info.
	 */
	public void regenerateLua() {
		lua = LuaValue.tableOf();
		Lua.generateFunction(this, lua, "getName");
		Lua.generateFunction(this, lua, "isAlive");
		lua.set("getSpriteName", new ZeroArgFunction() {
			@Override public LuaValue call() {
				return CoerceJavaToLua.coerce(appearance.getKey());
			}
		});
	}

}
