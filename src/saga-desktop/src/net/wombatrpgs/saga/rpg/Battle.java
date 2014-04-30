/**
 *  Battle.java
 *  Created on Apr 15, 2014 2:42:10 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.Intent.IntentListener;
import net.wombatrpgs.saga.rpg.Intent.TargetListener;
import net.wombatrpgs.saga.rpg.warheads.EffectDefend;
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
	protected List<Intent> playerTurn, globalTurn;
	protected List<Boolean> enemyAlive, playerAlive;
	protected List<TempStats> boosts, defendBoosts;
	protected Map<Chara, List<EffectDefend>> defendEffects;
	protected int actorIndex;
	protected boolean finished;
	protected boolean targetingMode;
	
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
		globalTurn = new ArrayList<Intent>();
		boosts = new ArrayList<TempStats>();
		defendBoosts = new ArrayList<TempStats>();
		defendEffects = new HashMap<Chara, List<EffectDefend>>();
		
		updateLivenessLists();
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
		for (TempStats temp : boosts) {
			temp.decombine();
		}
		for (TempStats temp : defendBoosts) {
			temp.decombine();
		}
		for (Chara chara : player.getAll()) {
			chara.onBattleEnd(this);
		}
		finished = true;
	}
	
	/**
	 * Called by the combat screen when playback for an attack, text output, etc
	 * has finished animating and it's time to move on to the next one.
	 */
	public void onPlaybackFinished() {
		if (playbackListener != null) {
			FinishListener old = playbackListener;
			playbackListener = null;
			old.onFinish();
		}
	}

	/**
	 * Called when the user says that they want to fight.
	 */
	public void onFight() {
		actorIndex = 0;
		buildNextIntent();
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
			playback("Can't escape. \n", new FinishListener() {
				@Override public void onFinish() {
					queueEnemyIntents();
					playRound();
				}
			});
		}
	}
	
	/**
	 * Writes some text to the screen using the battle text output box. Will
	 * draw focus to the text box if it isn't selected.
	 * @param	text			The text to write
	 */
	public void print(String text) {
		screen.print(text);
	}
	
	/**
	 * Writes some text to the screen using battle box with newline. Gets focus.
	 * @param	line			The line to write
	 */
	public void println(String line) {
		screen.println(line);
	}
	
	/**
	 * Prompts the user to select an enemy, then calls the listener. Internally
	 * selects the first enemy if null is passed as the current selection.
	 * @param	selected		The currently selected enemy, or null if none
	 * @param	listener		The callback once target is selected
	 */
	public void selectSingleEnemy(Chara selected, TargetListener listener) {
		int index = enemy.index(selected);
		if (index == -1) index = 0;
		while (enemy.getGroup(index).size() == 0) {
			index += 1;
		}
		targetingMode = true;
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
		targetingMode = true;
		screen.selectEnemyIndex(index, listener, true);
	}
	
	/**
	 * Prompts the user to select an ally, then calls the listener.
	 * @param	index			The index of the currently selected ally, or 0
	 * @param	listener		The callback once target is selected
	 */
	// TODO: come up with some way of targeting dead people
	public void selectAlly(int index, TargetListener listener) {
		targetingMode = true;
		screen.selectAlly(index, listener);
	}
	
	/**
	 * Checks if any members of the nth enemy group are still alive.
	 * @param	n				The index of the group to check
	 * @return					True if any of that group remain
	 */
	public boolean isEnemyAlive(int n) {
		return enemyAlive.get(n);
	}
	
	/**
	 * Checks if the nth play is alive. This is a render caching method.
	 * @param	n				The index of the player to check
	 * @return					True if that player fights on
	 */
	public boolean isPlayerAlive(int n) {
		return playerAlive.get(n);
	}
	
	/**
	 * Call this to check if the victim is dead. If the victim is dead, prints
	 * out an appropriate message, updates the displays, and checks the win
	 * conditions.
	 * @param	victim			The dolt who may have died
	 * @param	silent			True to not print out the message, else prints
	 */
	public void checkDeath(Chara victim, boolean silent) {
		if (!victim.isDead()) return;
		if (!silent) println(SConstants.TAB + victim.getName() + " is defeated.");
	}
	
	/**
	 * Neuters a character's action in the global cue. This is sort a stun.
	 * @param	chara			The character whose action to negate
	 * @return					True if cancelled, false if already acted
	 */
	public boolean cancelAction(Chara chara) {
		for (Intent intent : globalTurn) {
			if (intent.getActor() == chara) {
				intent.setItem(null);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Registers a change in stats to the designated character that will last
	 * the duration of this battle.
	 * @param	chara			The character to affect
	 * @param	stats			The stats to boost by
	 */
	public void applyBoost(Chara chara, SagaStats stats) {
		boosts.add(new TempStats(chara, stats));
	}
	
	/**
	 * Registers a turn-only change in stats to the designed character that will
	 * last until the end of this round.
	 * @param 	chara			The character to affect
	 * @param	stats			The stats to boost by
	 */
	public void applyDefendBoost(Chara chara, SagaStats stats) {
		defendBoosts.add(new TempStats(chara, stats));
	}
	
	/**
	 * Registers a defend effect as affecting a character. This will last until
	 * the end of the round. The defend effects can be called by other effects
	 * when relevant.
	 * @param	chara			The character to defend
	 * @param	defense			The effect to defend them with
	 */
	public void applyDefense(Chara chara, EffectDefend defense) {
		List<EffectDefend> defenses = defendEffects.get(chara);
		if (defenses == null) {
			defenses = new ArrayList<EffectDefend>();
			defendEffects.put(chara, defenses);
		}
		defenses.add(defense);
	}
	
	/**
	 * Returns the list of all effects being used to defend the chara this turn.
	 * @param	chara			The character to check
	 * @return					A non-null list of all defenses for that chara
	 */
	public List<EffectDefend> getDefenses(Chara chara) {
		List<EffectDefend> defenses = defendEffects.get(chara);
		if (defenses == null) {
			defenses = new ArrayList<EffectDefend>();
		}
		return defenses;
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
		playerTurn.clear();
		globalTurn.clear();
		screen.onNewRound();
	}
	
	/**
	 * Called when a round draws to a close.
	 */
	protected void finishRound() {
		for (Chara chara : player.getAll()) {
			chara.onRoundEnd(this);
		}
		for (Chara chara : enemy.getAll()) {
			chara.onRoundEnd(this);
		}
		for (TempStats temp : defendBoosts) {
			temp.decombine();
		}
		defendEffects.clear();
		defendBoosts.clear();
	}
	
	/**
	 * Plays out a round once the player turn is constructed.
	 */
	protected void playRound() {
		globalTurn.addAll(playerTurn);
		Collections.sort(globalTurn);
		screen.setAuto(true);
		for (Intent intent : globalTurn) {
			intent.onRoundStart();
		}
		playNextIntent();
	}
	
	/**
	 * Plays the next intent in the global intent stack, recursively. Do not
	 * call if no intents are left.
	 */
	protected void playNextIntent() {
		Intent intent = globalTurn.get(0);
		globalTurn.remove(0);
		intent.resolve();
		playbackListener = new FinishListener() {
			@Override public void onFinish() {
				updateLivenessLists();
				if (enemyWon()) {
					onDefeat();
					return;
				} else if (playerWon()) {
					onVictory();
					return;
				}
				if (globalTurn.size() > 0) {
					println("");
					playNextIntent();
				} else {
					finishRound();
					screen.setAuto(false);
					println("");
					playbackListener = new FinishListener() {
						@Override public void onFinish() {
							newRound();
						}
					};
				}
			}
		};
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
	protected void buildNextIntent() {
		Chara chara = player.getFront(actorIndex);
		final Intent intent;
		if (actorIndex < playerTurn.size()) {
			intent = playerTurn.get(actorIndex);
		} else {
			intent = new Intent(chara, this);
			playerTurn.add(intent);
		}
		targetingMode = false;
		modifyIntent(intent, new IntentListener() {
			@Override public void onIntent(Intent newIntent) {
				if (newIntent == null) {
					if (!targetingMode) {
						if (actorIndex > 0) {
							actorIndex -= 1;
							playerTurn.remove(intent);
							buildNextIntent();
						} else {
							newRound();
						}
					} else {
						buildNextIntent();
					}
				} else {
					do {
						actorIndex += 1;
					} while (actorIndex < player.size() && player.getFront(actorIndex).isDead());
					
					if (actorIndex == player.size()) {
						queueEnemyIntents();
						playRound();
					} else {
						buildNextIntent();
					}
				}
			}
		});
	}
	
	/**
	 * Asks all the living AIs for their intents and then piles them into the
	 * queue.
	 */
	protected void queueEnemyIntents() {
		for (Enemy chara : enemy.getEnemies()) {
			if (chara.isAlive()) {
				globalTurn.add(chara.act(this));
			}
		}
	}
	
	/**
	 * Make sure all the enemy portraits are still rendering correctly.
	 */
	protected void updateLivenessLists() {
		enemyAlive = new ArrayList<Boolean>();
		for (int i = 0; i < enemy.groupCount(); i += 1) {
			boolean alive = false;
			for (Chara chara : enemy.getGroup(i)) {
				if (chara.isAlive()) {
					alive = true;
					break;
				}
			}
			enemyAlive.add(alive);
		}
		if (playerAlive == null) {
			playerAlive = new ArrayList<Boolean>();
			for (int i = 0; i < player.groupCount(); i += 1) {
				playerAlive.add(player.getFront(i).isAlive());
			}
		}
		for (int i = 0; i < player.groupCount(); i += 1) {
			// TODO: battle: status effect display
			// TODO: battle: chara revive display
			boolean dead = player.getFront(i).isDead();
			if (dead && isPlayerAlive(i)) {
				screen.onPlayerDeath(i);
				playerAlive.set(i, false);
			}
		}
	}
	
	/**
	 * Checks if either side has won the battle and the battle should abort.
	 * @return					True if either side won
	 */
	protected boolean checkWinConditions() {
		return playerWon() || enemyWon();
	}
	
	/**
	 * Checks if the player has won this battle and all enemies are dead.
	 * @return					True if the player won
	 */
	protected boolean playerWon() {
		for (Boolean alive : enemyAlive) {
			if (alive) return false;
		}
		return true;
	}
	
	/**
	 * Checks if the enemy has won this battle and all players are dead.
	 * @return					True if the player won
	 */
	protected boolean enemyWon() {
		for (Boolean alive : playerAlive) {
			if (alive) return false;
		}
		return true;
	}
	
	/**
	 * Called internally when the player wins the battle.
	 */
	protected void onVictory() {
		// TODO: battle: spoils of war
		println("");
		println("");
		screen.setAuto(false);
		String leadername = player.findLeader().getName();
		playback(leadername + " is victorious.", new FinishListener() {
			@Override public void onFinish() {
				finish();
			}
		});
	}
	
	/**
	 * Called internall when the enemy wins the battle.
	 */
	protected void onDefeat() {
		// TODO: battle: game over, player!
		println("");
		screen.setAuto(false);
		playback("The party is lost...", new FinishListener() {
			@Override public void onFinish() {
				finish();
			}
		});
	}

}
