/**
 *  Battle.java
 *  Created on Apr 15, 2014 2:42:10 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.Intent.IntentListener;
import net.wombatrpgs.saga.rpg.Intent.TargetListener;
import net.wombatrpgs.saga.screen.CombatScreen;
import net.wombatrpgs.saga.ui.ItemSelector.SlotListener;
import net.wombatrpgs.sagaschema.rpg.chara.PartyMDO;

/**
 * Counterpart to the tactics battle. Controls battle flow and logic but not its
 * display. Dictates turn order and things like that.
 */
public class Battle extends AssetQueuer implements Disposable {
	
	// battle attributes
	protected CombatScreen screen;
	protected HeroParty player;
	protected EnemyParty enemy;
	protected boolean anonymous;
	
	// internal constructs
	protected FinishListener playbackListener;
	protected List<Intent> playerTurn;
	protected int actorIndex;
	protected boolean finished;
	
	/**
	 * Creates a new encounter between the player and some enemy party. The
	 * player's assets are not queued.
	 * @param	player			The player controlling this battle
	 * @param	enemy			The enemy they're fighting
	 */
	public Battle(HeroParty player, EnemyParty enemy) {
		this.player = player;
		this.enemy = enemy;
		this.screen = new CombatScreen(this);
		anonymous = false;
		finished = false;
		
		assets.add(enemy);
		assets.add(screen);
		queueInventory(player.getInventory());
		for (Chara chara : player.getAll()) {
			queueInventory(chara.getInventory());
		}
		for (Chara chara : enemy.getAll()) {
			queueInventory(chara.getInventory());
		}
		
		playerTurn = new ArrayList<Intent>();
	}
	
	/**
	 * Creates a random encounter style battle with an enemy party. The other
	 * party is assumed to be the SGlobal heroes. Will dispose the enemy party
	 * when battle is finished.
	 * @param	mdo				The MDO of the enemy party in the battle
	 */
	public Battle(PartyMDO mdo) {
		this(SGlobal.heroes, new EnemyParty(mdo));
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
		playerTurn.clear();
		actorIndex = 0;
		nextIntent();
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
	 * Prompts the user to select an enemy, then calls the listener. Internally
	 * selects the first enemy if null is passed as the current selection.
	 * @param	selected		The currently selected enemy, or null if none
	 * @param	listener		The callback once target is selected
	 */
	public void selectSingleEnemy(Chara selected, TargetListener listener) {
		int index = index(selected);
		if (index == -1) index = 0;
		while (enemy.getGroup(index).size() == 0) {
			index += 1;
		}
		screen.selectEnemyIndex(index, listener, false);
	}
	
	/**
	 * Prompts the user to select an enemy group, then calls the listener.
	 * Internally moves up the cursor if an empty group is selected.
	 * @param	index			The index of the currently selected group
	 * @param	listener		The callback once targets are selected
	 */
	public void selectEnemyGroup(int index, TargetListener listener) {
		if (index < 0) index = 0;
		while (enemy.getGroup(index).size() == 0) {
			index += 1;
		}
		screen.selectEnemyIndex(index, listener, true);
	}
	
	/**
	 * Gets the index of the group the enemy is in. Returns -1 if no group.
	 * @param	target			The enemy to to check
	 * @return					The index of that enemy's group
	 */
	public int index(Chara target) {
		for (int i = 0; i < enemy.groupCount(); i += 1) {
			if (enemy.getGroup(i).contains(target)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Adds all items in a chara's inventory to the asset list.
	 * @param	inventory			The inventory to queue from
	 */
	protected void queueInventory(Inventory inventory) {
		for (CombatItem item : inventory.getItems()) {
			if (item != null) {
				assets.add(item);
			}
		}
	}
	
	/**
	 * Plays back a line of text on the battle screen, waits for it to finish,
	 * then calls the listener.
	 * @param	line			The text to display on the screen
	 * @param	listener		What to do when the text display is done
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
	
	/**
	 * Plays out a round once the player turn is constructed.
	 */
	protected void playRound() {
		System.out.println("Play round!");
	}
	
	/**
	 * Modifies an intent in this battle. Intent could be an edit or a brand new
	 * one, but it's always a player intent.
	 * @param	intent			The intent to modify.
	 * @param	listener		The final listener to call when intent done
	 */
	protected void modifyIntent(final Intent intent, final IntentListener listener) {
		final Chara chara = intent.getActor();
		SlotListener slotListener = new SlotListener() {
			@Override public boolean onSelection(int selected) {
				if (selected == -1) {
					listener.onIntent(null);
					return true;
				}
				CombatItem item = chara.getInventory().get(selected);
				if (item == null || !item.isBattleUsable()) {
					return false;
				}
				intent.setItem(item);
				item.modifyIntent(intent, listener);
				return true;
			}
		};
		int slot = chara.getInventory().slotFor(intent.getItem());
		screen.selectItem(chara, slot, slotListener);
	}
	
	/**
	 * Prompts the player to create an intent for the current actor.
	 */
	protected void nextIntent() {
		Chara chara = player.getFront(actorIndex);
		final Intent intent;
		if (actorIndex < playerTurn.size()) {
			intent = playerTurn.get(actorIndex);
		} else {
			intent = new Intent(chara, this);
		}
		playerTurn.add(intent);
		modifyIntent(intent, new IntentListener() {
			@Override public void onIntent(Intent newIntent) {
				if (newIntent == null) {
					if (actorIndex > 0) {
						actorIndex -= 1;
						playerTurn.remove(intent);
						nextIntent();
					} else {
						newRound();
					}
				} else {
					actorIndex += 1;
					if (actorIndex == player.size()) {
						playRound();
					} else {
						nextIntent();
					}
				}
			}
		});
	}

}
