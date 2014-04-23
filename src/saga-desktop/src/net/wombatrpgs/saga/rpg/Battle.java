/**
 *  Battle.java
 *  Created on Apr 15, 2014 2:42:10 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.screen.CombatScreen;
import net.wombatrpgs.sagaschema.rpg.chara.PartyMDO;

/**
 * Counterpart to the tactics battle. Controls battle flow and logic but not its
 * display. Dictates turn order and things like that.
 */
public class Battle extends AssetQueuer implements Disposable {
	
	// battle attributes
	protected CombatScreen screen;
	protected HeroParty player;
	protected Party enemy;
	protected boolean anonymous;
	
	// internal constructs
	protected FinishListener playbackListener;
	protected boolean finished;
	
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
		finished = false;
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
	public boolean isDone() { return finished; }
	
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
	 * Call this to begin the battle. Will bring the screen to the front. Should
	 * smoothly transition etc.
	 */
	public void start() {
		// TODO: battle: start transitions
		MGlobal.screens.push(screen);
		screen.onNewRound();
	}
	
	/**
	 * Ends the battle without worrying about things gold or meat or xp. Should
	 * play transitions etc.
	 */
	public void finish() {
		// TODO: battle: finish transitions
		MGlobal.screens.pop();
		finished = true;
	}
	
	/**
	 * Called by the combat screen when playback for an attack, text output, etc
	 * has finished animating and it's time to move on to the next one.
	 */
	public void onPlaybackFinished() {
		if (playbackListener != null) {
			playbackListener.onFinish();
			playbackListener = null;
		}
	}
	
	/**
	 * Writes some text to the screen using the battle text output box. Will
	 * draw focus to the text box if it isn't selected.
	 * @param	text			The text to write
	 */
	public void write(String text) {
		
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
		if (calcRunChance() >= MGlobal.rand.nextFloat()) {
			playback(player.getFront().getName() + " runs.", new FinishListener() {
				@Override public void onFinish() {
					finish();
				}
			});
		} else {
			playback("Can't escape.", new FinishListener() {
				@Override public void onFinish() {
					// TODO: battle: on run failure
					newRound();
				}
			});
		}
	}
	
	/**
	 * Plays back a line of text on the battle screen, waits for it to finish,
	 * then calls the listener.
	 * @param	line				The text to display on the screen
	 * @param	listener			What to do when the text display is done
	 */
	protected void playback(String line, FinishListener listener) {
		if (playbackListener != null) {
			MGlobal.reporter.warn("Multiple playback finish listeners.");
		}
		playbackListener = listener;
		screen.println(line);
	}
	
	/**
	 * Calculates the chance of fleeing successfully, from 0 to 1.
	 * @return					The chance of escape, 0=never 1=always
	 */
	protected float calcRunChance() {
		return .5f;
	}
	
	/**
	 * Called between turns of the turn-based battle. This calls the run/fight
	 * prompt pop-up and removes the ugly textbox.
	 */
	protected void newRound() {
		screen.onNewRound();
	}

}
