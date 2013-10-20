/**
 *  MonsterGenerator.java
 *  Created on Oct 12, 2013 4:13:20 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.core.Turnable;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogueschema.characters.EnemyMDO;
import net.wombatrpgs.mrogueschema.characters.GlobalMonsterListMDO;
import net.wombatrpgs.mrogueschema.characters.data.MonsterNameMDO;
import net.wombatrpgs.mrogueschema.characters.data.MonsterNamePreMDO;
import net.wombatrpgs.mrogueschema.maps.MonsterGeneratorMDO;

/**
 * Generates random enemies from given text files and weights for danger and the
 * like, eventually. It adds things to its given level on update, if conditions
 * allow.
 */
public class MonsterGenerator implements	Turnable,
											Queueable {
	
	protected MonsterGeneratorMDO mdo;
	protected GlobalMonsterListMDO listMDO;
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
		listMDO = MGlobal.data.getEntryFor(mdo.list, GlobalMonsterListMDO.class);
		for (MonsterNameMDO name : listMDO.names) {
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
	 * @param	host			The parent level to gen for
	 */
	public Enemy createEnemy() {
		MonsterNameMDO name = listMDO.names[MGlobal.rand.nextInt(listMDO.names.length)];
		MonsterNamePreMDO pre = listMDO.prefixes[MGlobal.rand.nextInt(listMDO.prefixes.length)];
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
		for (int i = parent.getPopulation()-1; i < getCapacity(); i += 1) {
			if (MGlobal.rand.nextInt(mdo.respawnRate) == 0) {
				spawn();
			}
		}
	}
	
	/**
	 * Generates monster up to the mdo-specified value.
	 */
	public void spawnToDensity() {
		while (parent.getPopulation()-1 < getCapacity()) {
			spawn();
		}
	}
	
	/**
	 * Spawns a hidden enemy to the map.
	 */
	public void spawn() {
		Enemy e = createEnemy();
		e.postProcessing(MGlobal.assetManager, 0);
		e.spawnUnseen();
	}
	
	/**
	 * Calculates how many monsters should max be on this map.
	 * @return					The max monsters supported by this map
	 */
	protected int getCapacity() {
		return (int) Math.ceil(mdo.density * (float)(parent.getWidth()*parent.getHeight() / (100f*100f)));
	}

}
