/**
 *  MonsterGenerator.java
 *  Created on Oct 12, 2013 4:13:20 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.core.Turnable;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogueschema.characters.EnemyMDO;
import net.wombatrpgs.mrogueschema.characters.MonsterGeneratorMDO;
import net.wombatrpgs.mrogueschema.characters.data.MonsterNameMDO;
import net.wombatrpgs.mrogueschema.characters.data.MonsterNamePreMDO;

/**
 * Generates random enemies from given text files and weights for danger and the
 * like, eventually. It adds things to its given level on update, if conditions
 * allow.
 */
public class MonsterGenerator implements	Turnable,
											Queueable {
	
	protected MonsterGeneratorMDO mdo;
	protected List<Enemy> loaderDummies;
	protected Level parent;
	
	/**
	 * Creates a new monster generator from data.
	 * @param	mdo				The mdo to take data from
	 */
	public MonsterGenerator(Level parent, MonsterGeneratorMDO mdo) {
		this.mdo = mdo;
		this.parent = parent;
		this.loaderDummies = new ArrayList<Enemy>();
		List<String> neededTypes = new ArrayList<String>();
		for (MonsterNameMDO name : mdo.names) {
			if (!neededTypes.contains(name.archetype)) {
				neededTypes.add(name.archetype);
			}
		}
		for (String key : neededTypes) {
			Enemy e = new Enemy(MGlobal.data.getEntryFor(key, EnemyMDO.class), parent);
			loaderDummies.add(e);
		}
	}
	
	/**
	 * Creates an enemy for the parent level.
	 * @param	parent			The parent level to gen for
	 */
	public Enemy createEnemy() {
		MonsterNameMDO name = mdo.names[MGlobal.rand.nextInt(mdo.names.length)];
		MonsterNamePreMDO pre = mdo.prefixes[MGlobal.rand.nextInt(mdo.prefixes.length)];
		Enemy enemy = new Enemy(MGlobal.data.getEntryFor(name.archetype, EnemyMDO.class), parent);
		enemy.getUnit().setName(pre.prefix + " " + name.typeName);
		return enemy;
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets(com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Enemy dummy : loaderDummies) {
			dummy.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing(com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Enemy dummy : loaderDummies) {
			dummy.postProcessing(manager, pass);
		}
		loaderDummies.clear();
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Turnable#onTurn()
	 */
	@Override
	public void onTurn() {
		if (parent.getPopulation() < 3) {
			Enemy e = createEnemy();
			e.postProcessing(MGlobal.assetManager, 0);
			e.spawnUnseen();
		}
	}

}
