/**
 *  Battle.java
 *  Created on Apr 15, 2014 2:42:10 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.saga.core.SConstants;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.battle.Intent.IntentListener;
import net.wombatrpgs.saga.rpg.battle.Intent.TargetListener;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.chara.Enemy;
import net.wombatrpgs.saga.rpg.chara.EnemyParty;
import net.wombatrpgs.saga.rpg.chara.HeroParty;
import net.wombatrpgs.saga.rpg.chara.Party;
import net.wombatrpgs.saga.rpg.items.CombatItem;
import net.wombatrpgs.saga.rpg.items.Inventory;
import net.wombatrpgs.saga.rpg.mutant.Mutation;
import net.wombatrpgs.saga.rpg.stats.SagaStats;
import net.wombatrpgs.saga.rpg.stats.TempStats;
import net.wombatrpgs.saga.rpg.warheads.EffectDefend;
import net.wombatrpgs.saga.screen.SagaScreen;
import net.wombatrpgs.saga.screen.SagaScreen.FadeType;
import net.wombatrpgs.saga.screen.SagaScreen.TransitionType;
import net.wombatrpgs.saga.screen.ScreenBattle;
import net.wombatrpgs.saga.ui.CharaSelector.SelectionListener;
import net.wombatrpgs.saga.ui.ItemSelector.SlotListener;
import net.wombatrpgs.sagaschema.graphics.banim.data.BattleAnimMDO;
import net.wombatrpgs.sagaschema.rpg.chara.CharaMDO;
import net.wombatrpgs.sagaschema.rpg.chara.PartyMDO;
import net.wombatrpgs.sagaschema.rpg.stats.Flag;

/**
 * Counterpart to the tactics battle. Controls battle flow and logic but not its
 * display. Dictates turn order and things like that.
 */
public class Battle extends AssetQueuer implements Disposable {
	
	protected static final float AMBUSH_RATE = .15f;
	protected static final boolean POPUP_MODE = true;
	
	// battle attributes
	protected ScreenBattle screen;
	protected HeroParty player;
	protected EnemyParty enemy;
	protected boolean anonymous;
	protected boolean random;
	protected boolean fleeable;
	
	// internal constructs
	protected FinishListener playbackListener;
	protected List<Intent> globalTurn;
	protected Intent[] playerTurn;
	protected List<Boolean> enemyAlive, playerAlive;
	protected List<TempStats> boosts, defendBoosts;
	protected Map<Chara, List<EffectDefend>> defendEffects;
	protected Chara meatDropper;
	protected int actorIndex;
	protected int mutateIndex;
	protected boolean initialized;
	protected boolean enemyDisabled;
	protected boolean finished;
	protected boolean targetingMode;
	
	/**
	 * Creates a new encounter between the player and some enemy party. The
	 * player's assets are not queued.
	 * @param	player			The player controlling this battle
	 * @param	enemy			The enemy they're fighting
	 * @param	random			True to enable random encounter feature ambush
	 */
	public Battle(HeroParty player, EnemyParty enemy, boolean random) {
		this.player = player;
		this.enemy = enemy;
		this.screen = new ScreenBattle(this);
		this.random = random;
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
		
		playerTurn = new Intent[player.size()];
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
	 * @param	random			True to enable random encounter feature ambush
	 */
	public Battle(PartyMDO mdo, boolean random) {
		this(new EnemyParty(mdo), random);
	}
	
	/**
	 * Creates a random encounter style battle with an enemy party. The other
	 * party is assumed to be the SGlobal heroes. Will dispose the enemy party
	 * when battle is finished.
	 * @param	enemy			The enemy party in the battle
	 * @param	random			True to enable random encounter feature ambush
	 */
	public Battle(EnemyParty enemy, boolean random) {
		this(SGlobal.heroes, enemy, random);
		anonymous = true;
	}
	
	/** @return True if the battle is all over, including screen off */
	public boolean isDone() { return finished; }
	
	/** @return The party representing the player */
	public Party getPlayer() { return player; }
	
	/** @return The party the player is against */
	public Party getEnemy() { return enemy; }
	
	/** @param fleeable True if the party can run from this encounter */
	public void setFleeable(boolean fleeable) { this.fleeable = fleeable; }

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
	 * @see net.wombatrpgs.mgne.core.AssetQueuer#postProcessing
	 * (net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		initialized = true;
	}

	/**
	 * Call this to begin the battle. Will bring the screen to the front. Should
	 * smoothly transition etc.
	 */
	public void start() {
		final Battle battle = this;
		if (initialized) {
			screen.transitonOn(TransitionType.WHITE, new FinishListener() {
				@Override public void onFinish() {
					battle.internalStart();
				}
			});
		} else {
			SagaScreen current = (SagaScreen) MGlobal.screens.peek();
			current.fade(FadeType.TO_WHITE, new FinishListener() {
				@Override public void onFinish() {
					MGlobal.assets.loadAsset(battle, "battle");
					MGlobal.screens.push(screen);
					screen.fade(FadeType.FROM_WHITE, new FinishListener() {
						@Override public void onFinish() {
							battle.internalStart();
						}
					});
					screen.update(0);
				}
			});
		}
	}
	
	/**
	 * Ends the battle without worrying about things gold or meat or xp. Should
	 * play transitions etc.
	 */
	public void finish() {
		final Battle battle = this;
		screen.transitonOff(TransitionType.WHITE, new FinishListener() {
			@Override public void onFinish() {
				battle.internalFinish();
			}
		});
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
		actorIndex = -1;
		incrementActorIndex(false);
		buildNextIntent();
	}
	
	/**
	 * Called when the user says that they want to run.
	 */
	public void onRun() {
		if (fleeable && calcRunChance() >= MGlobal.rand.nextFloat()) {
			playback(player.getFront().getName() + " runs.", new FinishListener() {
				@Override public void onFinish() {
					finish();
				}
			});
		} else {
			playback("Can't escape.", new FinishListener() {
				@Override public void onFinish() {
					queueEnemyIntents();
					playRound();
				}
			});
		}
	}
	
	/**
	 * Called when the player elects to eat the meat!!
	 */
	public void onEat() {
		screen.selectMeatEater(0, new SelectionListener() {
			@Override public boolean onSelection(Chara selected) {
				
				String eatername = selected.getName();
				String droppername = meatDropper.getName();
				String sp = SConstants.NBSP;
				List<String> lines = new ArrayList<String>();
				
				String line1 = eatername;
				while (line1.length() < droppername.length() - 1) {
					line1 = sp + line1 + sp;
				}
				lines.add(line1);
				String line2 = "+";
				while (line2.length() < droppername.length() - 1) {
					line2 = sp + line2 + sp;
				}
				lines.add(line2);
				String line3 = droppername;
				while (line3.length() < eatername.length() - 1) {
					line3 = sp + line3 + sp;
				}
				lines.add(line3);
				lines.add("");
				
				String line4;
				CharaMDO result = selected.predictEat(meatDropper);
				String species = selected.getSpecies();
				if (result == null || result.species.equals(species)) {
					line4 = sp + "Nothing happens.";
				} else {
					line4 = sp + "to " + result.species;
				}
				lines.add(line4);
				
				screen.setMeatMessage(lines);
				return true;
			}
		}, new SelectionListener() {
			@Override public boolean onSelection(Chara selected) {
				if (selected == null) {
					onEatCancel();
					return true;
				}
				if (selected.isDead()) return false;
				
				String eatername = selected.getName();
				String oldSpecies = selected.getSpecies();
				selected.eat(meatDropper);
				String newSpecies = selected.getSpecies();
				if (oldSpecies.equals(newSpecies)) {
					println("Nothing happens.");
				} else {
					println(eatername + " transforms into " + newSpecies + ".");
				}
				
				screen.animatePause();
				playbackListener = new FinishListener() {
					@Override public void onFinish() {
						finish();
					}
				};
				return true;
			}
		});
	}
	
	/**
	 * Called when the player declines to eat the meat... how sad.
	 */
	public void onEatCancel() {
		finish();
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
	 * Prints a message or displays an animation the conveys to the player that
	 * a character has taken damage. This depends on some ugly static flags and
	 * will just be finalized with whatever looks better.
	 * @param	target			The target taking damage
	 * @param	damage			The damage being taken
	 */
	public void damagePlayback(Chara target, int damage) {
		String tab = SConstants.TAB;
		String victimname = target.getName();
		if (damage > 0) {
			if (player.contains(target)) {
				screen.shake(target);
			}
			println(SConstants.TAB + victimname + " takes " + damage + " damage.");
		} else {
			println(tab + victimname + " takes no damage.");
		}
	}
	
	/**
	 * Displays a battle animation on some enemy characters. This will only do
	 * anything for the enemies that get passed in.
	 * @param	animMDO			The MDO of the animation to play
	 * @param	targets			The targets to play the animation on
	 */
	public void animate(BattleAnimMDO animMDO, List<Chara> targets) {
		List<Chara> enemyTargets = new ArrayList<Chara>();
		for (Chara target : targets) {
			if (enemy.contains(target)) {
				enemyTargets.add(target);
			}
		}
		screen.animate(animMDO, targets);
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
	// TODO: battle: come up with some way of targeting dead people
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
	 * Returns the monster level of the enemies.
	 * @return					The monster level of enemies
	 */
	public int getLevel() {
		return enemy.getFront(0).getEatLevel();
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
	 * The actual changes that need to happen when the battle is finally on
	 * screen and ready to begin.
	 */
	protected void internalStart() {
		for (Chara chara : player.getAll()) {
			chara.onBattleStart(this);
		}
		for (Chara chara : enemy.getAll()) {
			chara.onBattleStart(this);
		}
		if (random && MGlobal.rand.nextFloat() < AMBUSH_RATE) {
			if (player.hasFlag(Flag.AMBUSHER) && !enemy.hasFlag(Flag.NO_AMBUSH)) {
				String leader = player.findLeader().getName();
				println(leader + " ambushes the enemy.");
				screen.animatePause();
				playbackListener = new FinishListener() {
					@Override public void onFinish() {
						screen.onNewRound();
						enemyDisabled = true;
					}
				};
			} else if (enemy.hasFlag(Flag.AMBUSHER) && !player.hasFlag(Flag.NO_AMBUSH)) {
				String leader = player.findLeader().getName();
				playback(leader + " is ambushed!", new FinishListener() {
					@Override public void onFinish() {
						queueEnemyIntents();
						playRound();
					}
				});
			} else {
				screen.onNewRound();
			}
		} else {
			screen.onNewRound();
		}
	}
	
	/**
	 * Actual finish component after screen transitions.
	 */
	protected void internalFinish() {
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
		for (int i = 0; i < player.size(); i += 1) {
			playerTurn[i] = null;
		}
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
		for (Intent intent : playerTurn) {
			if (intent != null) {
				globalTurn.add(intent);
			}
		}
		Collections.sort(globalTurn);
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
					screen.animatePause();
					playbackListener = new FinishListener() {
						@Override public void onFinish() {
							newRound();
						}
					};
				}
			}
		};
		if (globalTurn.size() > 0) {
			Intent intent = globalTurn.get(0);
			globalTurn.remove(0);
			intent.resolve();
		} else {
			playbackListener.onFinish();
		}
	}
	
	/**
	 * Plays the mutation of the character with the index of the mutation field.
	 * If the mutation index runs out, plays the levelup finish meat thing.
	 */
	protected void playNextMutation() {
		if (mutateIndex < player.size()) {
			Chara mutant = player.getAll().get(mutateIndex);
			mutateIndex += 1;
			final List<Mutation> mutations = mutant.generateMutations();
			if (mutations == null || mutant.isDead()) {
				playNextMutation();
			} else {
				println("");
				println(mutant.getName() + " mutates.");
				println("");
				println("");
				playback("", new FinishListener() {
					@Override public void onFinish() {
						screen.selectMutation(mutations, new FinishListener() {
							@Override public void onFinish() {
								playNextMutation();
							}
						});
					}
				});
			}
		} else {
			onLevelupFinished();
		}
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
		if (playerTurn[actorIndex] != null) {
			intent = playerTurn[actorIndex];
		} else {
			intent = new Intent(chara, this);
			playerTurn[actorIndex] = intent;
		}
		targetingMode = false;
		modifyIntent(intent, new IntentListener() {
			@Override public void onIntent(Intent newIntent) {
				if (newIntent == null) {
					if (!targetingMode) {
						int oldIndex = actorIndex;
						incrementActorIndex(true);
						if (oldIndex == actorIndex ) {
							newRound();
						} else {
							buildNextIntent();
						}
					} else {
						buildNextIntent();
					}
				} else {
					incrementActorIndex(false);
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
		if (enemyDisabled) {
			enemyDisabled = false;
		} else {
			for (Enemy chara : enemy.getEnemies()) {
				if (chara.isAlive()) {
					globalTurn.add(chara.act(this));
				}
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
		println("");
		println("");
		String leadername = player.findLeader().getName();
		println(leadername + " is victorious.");
		mutateIndex = 0;
		playNextMutation();
	}
	
	/**
	 * Called when levelups (mutations) are finished playing back.
	 */
	protected void onLevelupFinished() {
		println("");
		int gp = enemy.getDeathGold();
		player.addGP(gp);
		String gpstring = "Found " + gp + " GP.";
		
		meatDropper = enemy.chooseMeatFamily();
		if (meatDropper == null) {	
			println(gpstring);
			screen.animatePause();
			playbackListener = new FinishListener() {
				@Override public void onFinish() {
					finish();
				}
			};
		} else {
			println(gpstring);
			println("");
			String droppername = meatDropper.getName();
			println("Found meat of " + droppername + ".");
			println("");
			println("");
			playback("", new FinishListener() {
				@Override public void onFinish() {
					screen.onMeatChoice();
				}
			});
		}
	}
	
	/**
	 * Called internall when the enemy wins the battle.
	 */
	protected void onDefeat() {
		// TODO: battle: game over, player!
		println("");
		println("The party is lost...");
		screen.animatePause();
		playbackListener = new FinishListener() {
			@Override public void onFinish() {
				finish();
			}
		};
	}
	
	/**
	 * Moves the index along by at least 1, skipping dead and unable to act.
	 * Does not move past the end of the player size.
	 * @param	decrement		True to move backwards instead, else false
	 */
	protected void incrementActorIndex(boolean decrement) {
		if (decrement) {
			do {
				if (actorIndex > 0) {
					actorIndex -= 1;
				}
			} while (actorIndex > 0 &&
					!player.getFront(actorIndex).canConstructIntents(this));
			if (actorIndex == 0) {
				actorIndex = -1;
				incrementActorIndex(false);
			}
		} else {
			do {
				actorIndex += 1;
			} while (actorIndex < player.size() &&
					!player.getFront(actorIndex).canConstructIntents(this));
		}
	}

}
