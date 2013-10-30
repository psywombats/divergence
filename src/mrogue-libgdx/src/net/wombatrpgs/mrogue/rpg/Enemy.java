/**
 *  Enemy.java
 *  Created on Jan 23, 2013 9:14:38 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.rpg.ai.BTNode;
import net.wombatrpgs.mrogue.rpg.ai.IntelligenceFactory;
import net.wombatrpgs.mrogueschema.characters.EnemyMDO;
import net.wombatrpgs.mrogueschema.characters.EnemyModMDO;

/**
 * The one and only class for those pesky badniks that hunt down the valiant
 * hero and hinder his quest to save the earth.
 */
public class Enemy extends CharacterEvent {
	
	protected static final String KEY_MOD_DEFAULT = "mod_dummy";
	protected static final String KEY_TYPE_DEFAULT = "enemy_dummy";
	
	protected static final float FLASH_DURATION = 2f;
	
	protected EnemyMDO mdo;
	protected EnemyMod mod;
	protected BTNode intelligence;
	protected float sinceLastFlash;
	
	/**
	 * Creates a new enemy on a map from a database entry.
	 * @param 	mdo				The MDO with data to create from
	 * @param	modMDO			The modifier data for generation
	 * @param 	parent			The parent map of the object
	 */
	public Enemy(EnemyMDO mdo, EnemyModMDO modMDO, Level parent) {
		super(mdo, parent);
		this.mdo = mdo;
		this.intelligence = IntelligenceFactory.createIntelligence(mdo.intelligence, this);
		this.mod = new EnemyMod(modMDO, this);
		assets.add(mod);
		mod.apply();
		
		sinceLastFlash = FLASH_DURATION;
	}
	
	/**
	 * Haha "public enemy"
	 * @param	mdo				The mdo to generate from
	 * @param	parent			The parent to generate for
	 */
	public Enemy(EnemyMDO mdo, Level parent) {
		this(mdo, MGlobal.data.getEntryFor(KEY_MOD_DEFAULT, EnemyModMDO.class), parent);
	}
	
	/**
	 * yeah that was lame
	 * @param	modMDO			The modifier with data to create from
	 * @param	parent			The parent to generate for
	 */
	public Enemy(EnemyModMDO modMDO, Level parent) {
		this(MGlobal.data.getEntryFor(KEY_TYPE_DEFAULT, EnemyMDO.class), modMDO, parent);
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.CharacterEvent#act()
	 */
	@Override
	public void act() {
		if (unit.getStats().hp <= 0) {
			this.ticksRemaining += 100000;
			return;
		} else {
			intelligence.getStatusAndAct();
		}
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.rpg.CharacterEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		sinceLastFlash += elapsed;
		if (sinceLastFlash > FLASH_DURATION) {
			sinceLastFlash -= FLASH_DURATION;
			flash(mod.getColor(), FLASH_DURATION);
		}
	}
	
	/**
	 * Computes the danger level of this enemy. This is equal to the danger of
	 * its archetype plus danger for any addon stats.
	 * @return					The danger of this enemy.
	 */
	public int getDangerLevel() {
		return mdo.danger + mod.getDanger();
	}

}
