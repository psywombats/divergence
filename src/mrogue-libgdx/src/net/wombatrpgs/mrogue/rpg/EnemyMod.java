/**
 *  EnemyMod.java
 *  Created on Oct 29, 2013 5:44:59 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.rpg.abil.Ability;
import net.wombatrpgs.mrogueschema.characters.AbilityMDO;
import net.wombatrpgs.mrogueschema.characters.EnemyModMDO;

/**
 * Modifier unit for enemies.
 */
public class EnemyMod implements Queueable {
	
	protected EnemyModMDO mdo;
	protected Enemy parent;
	
	protected StatsModifier stats;
	protected List<Ability> abilities;
	protected Color color;
	
	protected List<Queueable> assets;
	
	/**
	 * Creates a new enemy mod from data.
	 * @param	mdo				The data to create from
	 * @param	enemy			The enemy to create for
	 */
	public EnemyMod(EnemyModMDO mdo, Enemy parent) {
		this.mdo = mdo;
		this.parent = parent;
		this.assets = new ArrayList<Queueable>();
		stats = new StatsModifier(mdo.stats);
		if (mdo.color != null) {
			color = new Color(mdo.color.r, mdo.color.g, mdo.color.b, mdo.color.a);
		}
		
		abilities = new ArrayList<Ability>();
		if (mdo.abilities != null) {
			for (String abilKey : mdo.abilities) {
				Ability abil = new Ability(parent, MGlobal.data.getEntryFor(abilKey, AbilityMDO.class));
				abilities.add(abil);
				assets.add(abil);
			}
		}
		
	}
	
	/** @return The danger modifier of this mod */
	public int getDanger() { return mdo.danger; }
	
	/** @return The color this monster should flash */
	public Color getColor() { return color; }

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
	}
	
	/**
	 * Applies stats and abilities to the parent. Does not cover things like
	 * danger level or flash color, which need to be queried repeatedly by the
	 * parent.
	 */
	public void apply() {
		stats.applyTo(parent.getStats());
		for (Ability abil : abilities) {
			parent.getUnit().getAbilities().add(abil);
		}
	}

}
