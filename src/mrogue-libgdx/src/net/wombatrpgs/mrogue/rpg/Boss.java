/**
 *  Boss.java
 *  Created on Oct 27, 2013 3:27:02 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.effects.Effect;
import net.wombatrpgs.mrogue.graphics.effects.EffectFactory;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogue.maps.layers.EventLayer;
import net.wombatrpgs.mrogue.rpg.abil.Ability;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogueschema.characters.AbilityMDO;
import net.wombatrpgs.mrogueschema.characters.BossMDO;
import net.wombatrpgs.mrogueschema.characters.data.AbilityTargetType;

/**
 * Just the boss of the game, nbd.
 */
public class Boss extends CharacterEvent {
	
	protected static final String BOSS_DEFAULT = "boss_default";
	
	protected BossMDO mdo;
	protected Effect effect;
	protected Ability fxAbil;
	protected SceneParser scene;
	
	/**
	 * Creates a new boss character. I'm not sure how this'll work yet.
	 * @param	parent			The parent level to create on
	 * @param	tileX			The tile to spawn at
	 * @param	tileY			The tile to spawn at
	 */
	public Boss(Level parent, int tileX, int tileY) {
		super(MGlobal.data.getEntryFor(BOSS_DEFAULT, BossMDO.class), parent, tileX, tileY);
		this.mdo = MGlobal.data.getEntryFor(BOSS_DEFAULT, BossMDO.class);
		unit.setName(MGlobal.levelManager.getBossName());
		if (mdoHasProperty(mdo.effect)) {
			effect = EffectFactory.create(parent, mdo.effect);
			assets.add(effect);
			parent.getScreen().addScreenObject(effect);
		}
		if (mdoHasProperty(mdo.abilFX)) {
			AbilityMDO abilMDO = new AbilityMDO();
			abilMDO.range = 3f;
			abilMDO.fx = mdo.abilFX;
			abilMDO.effect = "effect_physical";
			abilMDO.target = AbilityTargetType.BALL;
			fxAbil = new Ability(this, abilMDO);
			assets.add(fxAbil);
		}
		if (mdoHasProperty(mdo.sightedScene)) {
			scene = MGlobal.levelManager.getCutscene(mdo.sightedScene);
			assets.add(scene);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#getName()
	 */
	@Override
	public String getName() {
		return "boss";
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.CharacterEvent#onAdd
	 * (net.wombatrpgs.mrogue.maps.layers.EventLayer)
	 */
	@Override
	public void onAdd(EventLayer layer) {
		super.onAdd(layer);
		if (fxAbil != null) {
			fxAbil.fxSpawn();
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.CharacterEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (scene != null &&
				!scene.hasExecuted() &&
				!scene.isRunning() &&
				MGlobal.hero.inLoS(this)) {
			scene.run();
		}
	}

}
