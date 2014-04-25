/**
 *  CombatScreen.java
 *  Created on Apr 15, 2014 2:26:58 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.Option;
import net.wombatrpgs.mgne.ui.OptionSelector;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextBoxFormat;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;
import net.wombatrpgs.saga.rpg.Battle;
import net.wombatrpgs.saga.rpg.Chara;
import net.wombatrpgs.saga.rpg.Intent.TargetListener;
import net.wombatrpgs.saga.rpg.Party;
import net.wombatrpgs.saga.ui.BattleBox;
import net.wombatrpgs.saga.ui.CharaInsert;
import net.wombatrpgs.saga.ui.CharaInsertFull;
import net.wombatrpgs.saga.ui.CharaSelector;
import net.wombatrpgs.saga.ui.CharaSelector.SelectionListener;
import net.wombatrpgs.saga.ui.ItemSelector;
import net.wombatrpgs.saga.ui.ItemSelector.SlotListener;

/**
 * Screen for killing shit. This also encompasses a battle. Like Tactics, the
 * idea is that a battle is owned by a screen and controls the screen, but the
 * logic is kept separate from the display. Owned by a battle.
 */
public class CombatScreen extends Screen {
	
	// positional constants
	protected static final int OPTIONS_WIDTH = 108;
	protected static final int OPTIONS_HEIGHT = 96;
	protected static final int OPTIONS_MARGIN = 2;
	protected static final int INSERTS_WIDTH = 212;
	protected static final int INSERTS_HEIGHT = 96;
	protected static final int MONSTERLIST_WIDTH = 124;
	protected static final int MONSTERLIST_HEIGHT = 48;
	protected static final int MONSTERLIST_MARGIN = 10;
	protected static final int SPRITES_HEIGHT = 24;
	protected static final int ACTOR_PADDING = 10;
	protected static final int ABILS_WIDTH = 112;
	protected static final int ABILS_HEIGHT = 96;
	protected static final int ABILS_EDGE_PADDING = 12;
	protected static final int ABILS_LIST_PADDING = 2;
	protected static final int ABILS_VERT_FUDGE = -16;
	protected static final int ABILS_BG_WIDTH = 196;
	protected static final int ACTOR_BG_WIDTH = 124;
	
	// battle box constants
	protected static final int TEXT_LINES = 8;
	protected static final float TEXT_FADE_TIME = .1f;
	
	protected Battle battle;
	
	// display
	protected Nineslice optionsBG, insertsBG, monsterlistBG, abilsBG, actorBG;
	protected CharaSelector partyInserts, enemyInserts, miniInserts;
	protected TextBoxFormat monsterlistFormat;
	protected String[] monsterlist;
	protected OptionSelector options;
	protected List<FacesAnimation> sprites;
	protected BattleBox text;
	protected CharaInsert actor;
	protected ItemSelector abils;
	protected float globalX, globalY;
	
	// display toggles
	protected boolean showPlayerInserts, showEnemyInserts;
	protected boolean showMonsterList;
	protected boolean showActor;
	
	// selection mode
	protected boolean selectionMode;
	protected TargetListener targetListener;
	protected int selectedIndex;
	protected boolean multiMode;
	
	/**
	 * Creates a new combat setup. This initializes the screen and passes the
	 * arguments to the battle.
	 * @param	battle			The battle this screen will be used for
	 */
	public CombatScreen(final Battle battle) {
		this.battle = battle;
		pushCommandContext(new CMapMenu());
		
		options = new OptionSelector(false, true, new Option("FIGHT") {
			@Override public boolean onSelect() {
				battle.onFight();
				return false;
			}
		}, new Option("RUN") {
			@Override public boolean onSelect() {
				battle.onRun();
				return false;
			}
		});
		assets.add(options);
		
		optionsBG = new Nineslice(OPTIONS_WIDTH, OPTIONS_HEIGHT);
		insertsBG = new Nineslice(INSERTS_WIDTH + optionsBG.getBorderWidth(), INSERTS_HEIGHT);
		abilsBG = new Nineslice(ABILS_BG_WIDTH + optionsBG.getBorderWidth(), INSERTS_HEIGHT);
		monsterlistBG = new Nineslice(MONSTERLIST_WIDTH, MONSTERLIST_HEIGHT);
		actorBG = new Nineslice(ACTOR_BG_WIDTH, INSERTS_HEIGHT);
		assets.add(optionsBG);
		assets.add(insertsBG);
		assets.add(monsterlistBG);
		assets.add(abilsBG);
		assets.add(actorBG);
		
		partyInserts = new CharaSelector(battle.getPlayer(), true, true, false, 5);
		enemyInserts = new CharaSelector(battle.getEnemy(), true, true, false, 5);
		miniInserts = new CharaSelector(battle.getPlayer(), false, true, false, -3);
		assets.add(partyInserts);
		assets.add(enemyInserts);
		assets.add(miniInserts);
		addUChild(partyInserts);
		addUChild(enemyInserts);
		addUChild(miniInserts);
		
		text = new BattleBox(this, TEXT_LINES);
		assets.add(text);
		
		globalX = (getWidth() - (INSERTS_WIDTH + OPTIONS_WIDTH)) / 2;
		globalY = 0;
		
		options.setX(globalX + OPTIONS_MARGIN);
		options.setY(globalY - OPTIONS_MARGIN + OPTIONS_HEIGHT - options.getHeight());
		
		sprites = new ArrayList<FacesAnimation>();
		for (Chara chara : battle.getPlayer().getAll()) {
			FacesAnimation anim = chara.createSprite();
			anim.startMoving();
			anim.setFacing(OrthoDir.NORTH);
			addUChild(anim);
			sprites.add(anim);
			assets.add(anim);
		}
		
		FontHolder font = MGlobal.ui.getFont();
		monsterlistFormat = new TextBoxFormat();
		monsterlistFormat.align = HAlignment.LEFT;
		monsterlistFormat.width = MONSTERLIST_WIDTH;
		monsterlistFormat.height = MONSTERLIST_HEIGHT;
		monsterlistFormat.x = (int) (globalX + MONSTERLIST_MARGIN);
		monsterlistFormat.y = (int) (globalY + (MONSTERLIST_HEIGHT / 2) + font.getLineHeight()/2);
		updateMList();
	}
	
	/** @param auto True to ignore human prompts for newline */
	public void setAuto(boolean auto) { text.setAutoMode(auto); }

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		if (showActor) {
			actorBG.renderAt(batch, globalX, globalY);
		} else {
			optionsBG.renderAt(batch, globalX, globalY);
			insertsBG.renderAt(batch, globalX + OPTIONS_WIDTH - optionsBG.getBorderWidth(), globalY);
		}
		if (showEnemyInserts) {
			enemyInserts.render(batch);
		} else if (showPlayerInserts) {
			partyInserts.render(batch);
		} else if (containsChild(miniInserts)) {
			miniInserts.render(batch);
		} else if (showActor) {
			actor.render(batch);
		}
		Party enemy = battle.getEnemy();
		int groups = enemy.groupCount();
		if (showMonsterList) {
			FontHolder font = MGlobal.ui.getFont();
			monsterlistBG.renderAt(batch, globalX, globalY);
			switch (groups) {
			case 1:
				renderMList(batch, 0, 0);
				break;
			case 2:
				renderMList(batch, 0, font.getLineHeight() *  2/3);
				renderMList(batch, 1, font.getLineHeight() * -2/3);
				break;
			case 3:
				renderMList(batch, 0, font.getLineHeight() *  1);
				renderMList(batch, 1, font.getLineHeight() *  0);
				renderMList(batch, 2, font.getLineHeight() * -1);
				break;
			}
		}
		if (showActor) {
			abilsBG.renderAt(batch, MONSTERLIST_WIDTH - monsterlistBG.getBorderWidth(), globalY);
		}
		if (containsChild(abils)) {
			abils.render(batch);
		}
		WindowSettings win = MGlobal.window;
		for (int i = 0 ; i < groups; i += 1) { 
			List<Chara> group = enemy.getGroup(i);
			if (group.size() == 0) continue;
			if (!battle.isEnemyAlive(i)) continue;
			Graphic portrait = enemy.getFront(i).getPortrait();
			if (portrait == null) continue;
			float renderX = globalX + (win.getViewportWidth() - portrait.getWidth()*groups) * (i+1)/(groups+1) +
					(portrait.getWidth()) * i;
			float renderY = globalY + OPTIONS_HEIGHT + SPRITES_HEIGHT +
					((win.getViewportHeight() - OPTIONS_HEIGHT - SPRITES_HEIGHT) - portrait.getHeight()) / 2;
			portrait.renderAt(batch, renderX, renderY);
			if (selectionMode && i == selectedIndex) {
				Graphic cursor = MGlobal.ui.getCursor();
				cursor.renderAt(batch,
						renderX - cursor.getWidth() / 2,
						renderY + (portrait.getHeight() - cursor.getHeight()) / 2);
			}
		}
		int players = sprites.size();
		for (int i = 0; i < players; i += 1) {
			FacesAnimation anim = sprites.get(i);
			float renderX = globalX + (win.getViewportWidth() - anim.getWidth()*players) * (i+1)/(players+1) +
					(anim.getWidth()) * i;
			float renderY = globalY + OPTIONS_HEIGHT + (SPRITES_HEIGHT - anim.getHeight()) / 2;
			anim.renderAt(batch, renderX, renderY);
		}
		super.render(batch);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#postProcessing
	 * (net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		partyInserts.setX(globalX + OPTIONS_WIDTH + (INSERTS_WIDTH - partyInserts.getWidth()) / 2);
		partyInserts.setY(globalY + (INSERTS_HEIGHT - partyInserts.getHeight()) / 2);
		enemyInserts.setX(globalX + OPTIONS_WIDTH + (INSERTS_WIDTH - enemyInserts.getWidth()) / 2);
		enemyInserts.setY(globalY + (INSERTS_HEIGHT - enemyInserts.getHeight()) / 2);
		miniInserts.setX(globalX + (ACTOR_BG_WIDTH - miniInserts.getWidth()) / 2 + 5);
		miniInserts.setY(globalY + (INSERTS_HEIGHT - miniInserts.getHeight()) / 2);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		for (FacesAnimation sprite : sprites) {
			sprite.dispose();
		}
	}

	/**
	 * Prints some text to the battle textbox. This will cause the battlebox to
	 * fade in on the screen if it isn't already. The child battle will receive
	 * a call when the line finishes displaying.
	 * @param	line			The text to print
	 */
	public void println(String line) {
		if (!containsChild(text)) {
			text.fadeIn(this, TEXT_FADE_TIME);
		}
		text.println(line);
	}
	
	/**
	 * Prints some text to the battle textbox. Causes focus etc. Appends a
	 * space. Make sure to call println to flush.
	 * @param	line			The text to print
	 */
	public void print(String line) {
		if (!containsChild(text)) {
			text.fadeIn(this, TEXT_FADE_TIME);
		}
		text.print(line + " ");
	}
	
	/**
	 * Called by the battle textbox when its text has been consumed.
	 */
	public void onTextFinished() {
		// TODO: battle: only advance if the animation is done too
		battle.onPlaybackFinished();
	}
	
	/**
	 * Called by the battle when a new run/fight round is beginning.
	 */
	public void onNewRound() {
		showEnemyInserts = false;
		showPlayerInserts = true;
		showMonsterList = true;
		showActor = false;
		setAuto(false);
		updateMList();
		partyInserts.refresh();
		miniInserts.refresh();
		if (containsChild(text)) {
			text.fadeOut(TEXT_FADE_TIME);
		}
		if (containsChild(abils)) {
			removeChild(abils);
		}
		if (containsChild(options)) {
			options.focus();
		} else {
			options.showAt((int) options.getX(), (int) options.getY());
		}
	}
	
	/**
	 * Called by the battle when the character with the given index is killed.
	 * @param	index			The party slot of the dead character
	 */
	public void onPlayerDeath(int index) {
		sprites.get(index).stopMoving();
	}
	
	/**
	 * Prompts the player to choose an ability/item from the selected chara.
	 * @param	chara			The character to be the actor
	 * @param	selected		The index of the item previously selected, or -1
	 * @param	listener		The callback for when a slot is selected
	 */
	public void selectItem(Chara chara, int selected, final SlotListener listener) {
		
		if (abils != null && containsChild(abils)) {
			removeChild(abils);
		}
		
		List<Queueable> assets = new ArrayList<Queueable>();
		actor = new CharaInsertFull(chara, true);
		abils = new ItemSelector(chara.getInventory(),chara.getInventory().slotCount(),
				ABILS_WIDTH, ABILS_LIST_PADDING, false, true);
		assets.add(actor);
		assets.add(abils);
		
		actor.setX(globalX + (MONSTERLIST_WIDTH - actor.getWidth()) / 2);
		actor.setY(globalY + OPTIONS_HEIGHT - actor.getHeight() - ACTOR_PADDING);
		abils.setX(globalX + MONSTERLIST_WIDTH + ABILS_EDGE_PADDING);
		abils.setY(globalY + OPTIONS_HEIGHT - abils.getHeight() + ABILS_VERT_FUDGE);
		
		showActor = true;
		showPlayerInserts = false;
		showEnemyInserts = false;
		// showMonsterList = false;
		if (containsChild(options)) {
			removeChild(options);
		}
		addChild(abils);
		
		MGlobal.assets.loadAssets(assets, "battle abil selector assets");
		abils.awaitSelection(new SlotListener() {
			@Override public boolean onSelection(int selected) {
				ItemSelector oldAbils = abils;
				boolean willUnfocus = listener.onSelection(selected);
				if (willUnfocus && selected != -1) {
					if (oldAbils == abils) {
						abils.setIndent();
					}
				}
				return willUnfocus;
			} 
		}, true);
		if (selected != -1) {
			abils.setSelected(selected);
		}
	}
	
	/**
	 * Prompts the user to select an enemy group. This can be used for targeting
	 * single enemies as well by targeting the first person in that group.
	 * @param	selected		The currently selected enemy group index, or 0
	 * @param	listener		The listener to call when done
	 * @param	multiMode		True to show enemy inserts as if selecting group
	 */
	public void selectEnemyIndex(int selected, TargetListener listener, boolean multiMode) {
		this.targetListener = listener;
		this.selectedIndex = selected;
		this.multiMode = multiMode;
		
		showMonsterList = true;
		selectionMode = true;
		moveCursor(0);
		pushCommandListener(new CommandListener() {
			@Override public boolean onCommand(InputCommand command) {
				switch (command) {
				case MOVE_LEFT:		moveCursor(-1);		break;
				case MOVE_RIGHT:	moveCursor(1);		break;
				case UI_CONFIRM:	selectConfirm();	break;
				case UI_CANCEL:		selectCancel();		break;
				default:								break;
				}
				return true;
			}
		});
		// TODO: battle: use multimode to render enemy inserts maybe?
	}
	
	/**
	 * Prompts the user to select a single ally.
	 * @param	selected		The index of the currently selected ally, or 0
	 * @param	listener		The listener to call when done
	 */
	public void selectAlly(int selected, final TargetListener listener) {
		this.targetListener = listener;
		showMonsterList = false;
		showActor = true;
		miniInserts.awaitSelection(new SelectionListener() {
			@Override public boolean onSelection(Chara selected) {
				if (selected == null) {
					listener.onTargetSelection(null);
				} else {
					listener.onTargetSelection(Arrays.asList(selected));
				}
				removeChild(miniInserts);
				showMonsterList = true;
				return true;
			}
		}, true);
		if (selected > 0) {
			miniInserts.setSelected(selected);
		}
		addChild(miniInserts);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#wipe()
	 */
	@Override
	protected void wipe() {
		WindowSettings window = MGlobal.window;
		Gdx.gl.glClearColor(248.f/255.f, 248.f/255.f, 248.f/255.f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shapes.setColor(248.f/255.f, 248.f/255.f, 248.f/255.f, 1);
		shapes.begin(ShapeType.Filled);
		shapes.rect(0, 0, window.getWidth(), window.getHeight());
	}

	/**
	 * Renders part of the monster list at the given location.
	 * @param	batch			The batch to render with
	 * @param	groupNo			The index of the group to render
	 * @param	y				The offset y to apply to the textbox format
	 */
	protected void renderMList(SpriteBatch batch, int groupNo, float y) {
		FontHolder font = MGlobal.ui.getFont();
		font.draw(batch, monsterlistFormat, monsterlist[groupNo], (int) y);
	}
	
	/**
	 * Rewrites the monster list strings to reflect current party.
	 */
	protected void updateMList() {
		monsterlist = new String[battle.getEnemy().groupCount()];
		for (int i = 0; i < battle.getEnemy().groupCount(); i += 1) {
			List<Chara> group = battle.getEnemy().getGroup(i);
			monsterlist[i] = group.get(0).getName();
			while (monsterlist[i].length() < 10) {
				monsterlist[i] += " ";
			}
			int count = 0;
			for (Chara chara : group) {
				if (chara.isAlive()) {
					count += 1;
				}
			}
			monsterlist[i] = monsterlist[i] + " x" + count;
		}
	}
	
	/**
	 * Moves the cursor in selection mode.
	 * @param	delta			Amount to move, negative to go left
	 */
	protected void moveCursor(int delta) {
		selectedIndex += delta;
		selectedBounds();
		selectedCheck();
		selectedBounds();
		selectedCheck();
	}
	
	/**
	 * Cancels the user selection in selection mode.
	 */
	protected void selectCancel() {
		cancelSelectionMode();
		targetListener.onTargetSelection(null);
	}
	
	/**
	 * Confirms the user selection in selection mode.
	 */
	protected void selectConfirm() {
		cancelSelectionMode();
		targetListener.onTargetSelection(battle.getEnemy().getGroup(selectedIndex));
	}
	
	/**
	 * Applies edges to the selection mode cursor.
	 */
	protected void selectedBounds() {
		int count = battle.getEnemy().groupCount();
		if (selectedIndex < 0) selectedIndex += count;
		if (selectedIndex >= count) selectedIndex %= count;
	}
	
	/**
	 * Moves the selection cursor past dead enemy groups.
	 */
	protected void selectedCheck() {
		while (!battle.isEnemyAlive(selectedIndex)) {
			selectedIndex += 1;
		}
	}
	
	/**
	 * Moves the screen out of selection mode.
	 */
	protected void cancelSelectionMode() {
		selectionMode = false;
		popCommandListener();
	}

}
