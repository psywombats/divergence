/**
 *  Battle.java
 *  Created on Apr 15, 2014 2:42:10 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.screen.CombatScreen;
import net.wombatrpgs.sagaschema.rpg.chara.PartyMDO;

/**
 * Counterpart to the tactics battle. Controls battle flow and logic but not its
 * display. Dictates turn order and things like that.
 */
public class Battle extends AssetQueuer implements Disposable {
	
	protected CombatScreen screen;
	protected HeroParty player;
	protected Party enemy;
	protected boolean anonymous;
	
	/**
	 * Creates a new encounter between the player and some enemy party. The
	 * player's assets are not queued.
	 * @param	player			The player controlling this battle
	 * @param	enemy			The enemy they're fighting
	 */
	public Battle(HeroParty player, Party enemy) {
		this.player = player;
		this.enemy = enemy;
		this.screen = new CombatScreen(this);
		assets.add(enemy);
		assets.add(screen);
		anonymous = false;
	}
	
	/**
	 * Creates a random encounter style battle with an enemy party. The other
	 * party is assumed to be the SGlobal heroes. Will dispose the enemy party
	 * when battle is finished.
	 * @param	mdo				The MDO of the enemy party in the battle
	 */
	public Battle(PartyMDO mdo) {
		this(SGlobal.heroes, new Party(mdo));
		anonymous = true;
	}
	
	/**
	 * Creates a random encounter style battle with an enemy party, looked up
	 * by its database key. The other party is assumed to be the SGlobal heroes.
	 * Will dispose the enemy party when battle is finished.
	 * @param	key				The key of the enemy party MDO
	 */
	public Battle(String key) {
		this(MGlobal.data.getEntryFor(key, PartyMDO.class));
		anonymous = true;
	}
	
	/** @return True if the battle is all over, including screen off */
	public boolean isDone() { return false; }
	
	/** @return The party representing the player */
	public Party getPlayer() { return player; }
	
	/** @return The party the player is against */
	public Party getEnemy() { return enemy; }

	/**
	 * Only call once the screen is removed, please.
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		screen.dispose();
		if (anonymous) {
			enemy.dispose();
		}
	}
	
	/**
	 * Call this to begin the battle. Will bring the screen to the front.
	 */
	public void start() {
		MGlobal.screens.push(screen);
	}

	/**
	 * Called when the user says that they want to fight.
	 */
	public void onFight() {
		
	}
	
	/**
	 * Called when the user says that they want to run.
	 */
	public void onRun() {
		
	}

}
