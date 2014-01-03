/**
 *  Class.java
 *  Created on Oct 29, 2013 1:38:54 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.graphics.FacesAnimation;
import net.wombatrpgs.mrogue.graphics.FacesAnimationFactory;
import net.wombatrpgs.mrogue.rpg.abil.Ability;
import net.wombatrpgs.mrogueschema.characters.AbilityMDO;
import net.wombatrpgs.mrogueschema.characters.ClassMDO;

/**
 * herp derp class Class.
 */
public class UnitClass implements Queueable {
	
	protected ClassMDO mdo;
	protected CharacterEvent actor;
	
	protected FacesAnimation anim;
	protected StatsModifier stats;
	protected List<Ability> abils;
	
	protected List<Queueable> assets;
	
	/**
	 * Creates a new class from data.
	 * @param	mdo				The data to create class from
	 * @param	actor			The actor to create the class for
	 */
	public UnitClass(ClassMDO mdo, CharacterEvent actor) {
		this.mdo = mdo;
		this.actor = actor;
		assets = new ArrayList<Queueable>();
		
		stats = new StatsModifier(mdo.stats);
		abils = new ArrayList<Ability>();
		for (String abilKey : mdo.abilities) {
			AbilityMDO abilMDO = MGlobal.data.getEntryFor(abilKey, AbilityMDO.class);
			abils.add(new Ability(actor, abilMDO));
		}
		assets.addAll(abils);
		
		anim = FacesAnimationFactory.create(mdo.appearance, actor);
		assets.add(anim);
	}
	
	/** @return The in-game name of this class */
	public String getName() { return mdo.className; }
	
	/** @return The in-game desc of this class */
	public String getDesc() { return mdo.classDesc; }
	
	/** @return What a character with this class looks like, maybe */
	public FacesAnimation getAnim() { return anim; }
	
	/** @return All the starting skills of this class */
	public List<Ability> getSkills() { return abils; }
	
	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
		anim.startMoving();
	}

	/**
	 * Applies this class to the designated character.
	 */
	public void apply() {
		stats.applyTo(actor.getStats());
		for (Ability abil : abils) {
			actor.getUnit().getAbilities().add(abil);
		}
	}

}
