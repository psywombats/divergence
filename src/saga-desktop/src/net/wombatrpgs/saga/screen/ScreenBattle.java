/**
 *  CombatScreen.java
 *  Created on Apr 15, 2014 2:26:58 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.graphics.ShaderFromData;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.Option;
import net.wombatrpgs.mgne.ui.OptionSelector;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextFormat;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.graphics.PortraitAnim;
import net.wombatrpgs.saga.graphics.banim.BattleAnim;
import net.wombatrpgs.saga.graphics.banim.BattleAnimFactory;
import net.wombatrpgs.saga.rpg.battle.PlayAnim;
import net.wombatrpgs.saga.rpg.battle.Battle;
import net.wombatrpgs.saga.rpg.battle.PlayNumber;
import net.wombatrpgs.saga.rpg.battle.PlayShake;
import net.wombatrpgs.saga.rpg.battle.PlaybackStep;
import net.wombatrpgs.saga.rpg.battle.PlayText;
import net.wombatrpgs.saga.rpg.battle.Intent.TargetListener;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.chara.Party;
import net.wombatrpgs.saga.rpg.mutant.Mutation;
import net.wombatrpgs.saga.ui.BattleBox;
import net.wombatrpgs.saga.ui.CharaInsert;
import net.wombatrpgs.saga.ui.CharaInsertFull;
import net.wombatrpgs.saga.ui.CharaSelector;
import net.wombatrpgs.saga.ui.CharaSelector.SelectionListener;
import net.wombatrpgs.saga.ui.ItemSelector;
import net.wombatrpgs.saga.ui.ItemSelector.SlotListener;
import net.wombatrpgs.sagaschema.graphics.banim.data.BattleAnimMDO;

/**
 * Screen for killing shit. This also encompasses a battle. Like Tactics, the
 * idea is that a battle is owned by a screen and controls the screen, but the
 * logic is kept separate from the display. Owned by a battle.
 */
public class ScreenBattle extends SagaScreen {
	
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
	protected static final int SHAKE_VERT = 8;
	
	// battle box and other constants
	protected static final String KEY_SHADER_SHAKE = "shader_quake";
	protected static final float SHAKE_DURATION = .5f;
	protected static final int TEXT_LINES = 8;
	protected static final float TEXT_FADE_TIME = 0f;
	
	protected Battle battle;
	
	// display
	protected Nineslice optionsBG, insertsBG, monsterlistBG, abilsBG, actorBG;
	protected CharaSelector partyInserts, enemyInserts, miniInserts;
	protected TextFormat monsterlistFormat, meatFormat;
	protected String[] monsterlist;
	protected OptionSelector fightOptions, meatOptions;
	protected List<FacesAnimation> sprites;
	protected BattleBox text;
	protected CharaInsert actor;
	protected ItemSelector abils;
	protected List<String> meatMessages;
	protected Matrix4 playerMatrix, enemyMatrix;
	protected ShaderFromData shakeShader;
	protected transient FrameBuffer playerBuffer, enemyBuffer;
	protected transient SpriteBatch playerBatch, playerFinalBatch;
	protected transient SpriteBatch enemyBatch, enemyFinalBatch;
	protected float globalX, globalY;
	
	// display toggles
	protected boolean showPlayerInserts, showEnemyInserts;
	protected boolean showMonsterList;
	protected boolean showActor;
	protected boolean showMeatMessage;
	
	// selection mode
	protected boolean selectionMode;
	protected TargetListener targetListener;
	protected int selectedIndex;
	protected boolean multiMode;
	
	// animation + graphics
	protected List<PlaybackStep> playbackQueue;
	protected List<PortraitAnim> anims;
	protected Map<Integer, PortraitAnim> animsOnGroups;
	protected Map<Integer, Float> shakeTimers;
	
	/**
	 * Creates a new combat setup. This initializes the screen and passes the
	 * arguments to the battle.
	 * @param	battle			The battle this screen will be used for
	 */
	public ScreenBattle(final Battle battle) {
		this.battle = battle;
		pushCommandContext(new CMapMenu());
		
		fightOptions = new OptionSelector(false, true, new Option("FIGHT") {
			@Override public boolean onSelect() {
				battle.onFight();
				return true;
			}
		}, new Option("RUN") {
			@Override public boolean onSelect() {
				battle.onRun();
				return true;
			}
		});
		assets.add(fightOptions);
		
		meatOptions = new OptionSelector(false, false, new Option("EAT") {
			@Override public boolean onSelect() {
				battle.onEat();
				return true;
			}
		}, new Option("CANCEL") {
			@Override public boolean onSelect() {
				battle.onEatCancel();
				return true;
			}
		});
		assets.add(meatOptions);
		
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
		
		partyInserts = new CharaSelector(battle.getPlayer(), true, true, false, 3);
		enemyInserts = new CharaSelector(battle.getEnemy(), true, true, false, 3);
		miniInserts = new CharaSelector(battle.getPlayer(), false, true, false, -5);
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
		
		fightOptions.setX(globalX + OPTIONS_MARGIN);
		fightOptions.setY(globalY - OPTIONS_MARGIN + OPTIONS_HEIGHT - fightOptions.getHeight());
		meatOptions.setX(globalX + (getWidth() - meatOptions.getWidth()) / 2);
		meatOptions.setY(globalY + optionsBG.getBorderHeight() - 1);
		
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
		
		monsterlistFormat = new TextFormat();
		monsterlistFormat.align = HAlignment.LEFT;
		monsterlistFormat.width = MONSTERLIST_WIDTH;
		monsterlistFormat.height = MONSTERLIST_HEIGHT;
		monsterlistFormat.x = (int) (globalX + MONSTERLIST_MARGIN);
		monsterlistFormat.y = (int) (globalY + (MONSTERLIST_HEIGHT / 2) + font.getLineHeight()/2);
		updateMList();
		
		meatFormat = new TextFormat();
		meatFormat.align = HAlignment.LEFT;
		meatFormat.width = INSERTS_WIDTH;
		meatFormat.height = ABILS_HEIGHT;
		meatFormat.x = (int) (globalX + MONSTERLIST_WIDTH + ABILS_EDGE_PADDING);
		meatFormat.y = (int) (globalY + OPTIONS_HEIGHT - ABILS_VERT_FUDGE - font.getLineHeight()*3);
		
		anims = new ArrayList<PortraitAnim>();
		animsOnGroups = new HashMap<Integer, PortraitAnim>();
		playbackQueue = new ArrayList<PlaybackStep>();
		shakeTimers = new HashMap<Integer, Float>();
	}
	
	/** @param auto True to ignore human prompts for newline */
	public void setAuto(boolean auto) { text.setAutoMode(auto); }
	
	/** @return True if the text box is not blocking battle playback */
	public boolean isTextFinished() { return text.shouldAdvance(); }
	
	/** @return True if no animations are currently playing */
	public boolean isAnimFinished() { return anims.size() == 0; }

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		boolean done = true;
		for (PortraitAnim anim : anims) {
			if (anim.isDone()) {
				if (anim.isPlaying()) {
					anim.finish(this);
				}
			} else {
				done = false;
			}
		}
		if (done) {
			for (PortraitAnim anim : anims) {
				removeUChild(anim);
				anim.dispose();
			}
			anims.clear();
		}
		if (playbackQueue.size() > 0) {
			PlaybackStep current = playbackQueue.get(0);
			if (!current.isStarted()) {
				current.start();
			}
			if (current.isDone()) {
				playbackQueue.remove(current);
				if (playbackQueue.size() > 0) {
					PlaybackStep next = playbackQueue.get(0);
					next.start();
				} else {
					battle.onPlaybackFinished();
				}
			}
		}
		List<Integer> toRemove = new ArrayList<Integer>();
		for (int key : shakeTimers.keySet()) {
			shakeTimers.put(key, shakeTimers.get(key) - elapsed);
			if (shakeTimers.get(key) < 0) {
				toRemove.add(key);
			}
		}
		for (int key : toRemove) {
			shakeTimers.remove(key);
		}
		if (battle.isDone()) {
			MGlobal.screens.pop();
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		FontHolder font = MGlobal.ui.getFont();
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
		if (showActor || showMeatMessage) {
			abilsBG.renderAt(batch, MONSTERLIST_WIDTH - monsterlistBG.getBorderWidth(), globalY);
		}
		if (containsChild(abils)) {
			abils.render(batch);
		} else if (showMeatMessage) {
			int off = 0;
			for (String line : meatMessages) {
				font.draw(batch, meatFormat, line, (int) (off * -font.getLineHeight()));
				off += 1;
			}
		}
		
		WindowSettings win = MGlobal.window;
		
		enemyBuffer.begin();
		int enemyHeight = win.getViewportHeight() - OPTIONS_HEIGHT - SPRITES_HEIGHT;
		shapes.setProjectionMatrix(enemyMatrix);
		shapes.begin(ShapeType.Filled);
		float[] white = SGlobal.graphics.getWhite();
		shapes.setColor(white[0], white[1], white[2], 0);
		shapes.rect(0, 0, win.getViewportWidth(), enemyHeight);
		shapes.end();
		for (int i = 0 ; i < groups; i += 1) { 
			List<Chara> group = enemy.getGroup(i);
			if (group.size() == 0) continue;
			if (!battle.isEnemyAlive(i)) continue;
			Graphic portrait = enemy.getFront(i).getPortrait();
			if (portrait == null) continue;
			float renderX = globalX + (win.getViewportWidth() - portrait.getWidth()*groups) *
					(i+1)/(groups+1) + (portrait.getWidth()) * i;
			float renderY = ((win.getViewportHeight() - OPTIONS_HEIGHT - SPRITES_HEIGHT) -
					portrait.getHeight()) / 2;
			portrait.renderAt(enemyBatch, renderX, renderY);
			if (selectionMode && i == selectedIndex) {
				Graphic cursor = MGlobal.ui.getCursor();
				cursor.renderAt(enemyBatch,
						renderX - cursor.getWidth() / 2,
						renderY + (portrait.getHeight() - cursor.getHeight()) / 2);
			}
			PortraitAnim anim = animsOnGroups.get(i);
			if (anim != null) {
				anim.renderAt(enemyBatch,
						renderX + portrait.getWidth() / 2,
						renderY + portrait.getHeight() / 2);
			}
		}
		enemyBuffer.end();
		resumeNormalBuffer();
		
		enemyFinalBatch.begin();
		enemyFinalBatch.draw(
				enemyBuffer.getColorBufferTexture(),			// texture
				0, globalY + OPTIONS_HEIGHT + SPRITES_HEIGHT,	// x/y in screen space
				0, 0,											// origin x/y screen
				win.getViewportWidth(), enemyHeight,			// width/height screen
				1, 1,											// scale x/y
				0,												// rotation in degrees
				0, 0,											// x/y in texel space
				win.getViewportWidth(), enemyHeight,			// width/height texel
				false, true										// flip horiz/vert
			);
		enemyFinalBatch.end();
		
		playerBuffer.begin();
		shapes.setProjectionMatrix(playerMatrix);
		shapes.begin(ShapeType.Filled);
		shapes.setColor(white[0], white[1], white[2], 0);
		shapes.rect(0, 0, win.getViewportWidth(), SPRITES_HEIGHT);
		shapes.end();
		int players = sprites.size();
		for (int i = 0; i < players; i += 1) {
			FacesAnimation anim = sprites.get(i);
			float renderX = globalX + (win.getViewportWidth() - anim.getWidth()*players) *
					(i+1)/(players+1) + (anim.getWidth()) * i;
			float renderY = (SPRITES_HEIGHT - anim.getHeight()) / 2;
			shakeShader.begin();
			shakeShader.setUniformf("u_offX", -1);
			playerBatch.setShader(shakeShader);
			if (shakeTimers.containsKey(i)) {
				shakeShader.setUniformf("u_elapsed", shakeTimers.get(i));
				shakeShader.setUniformf("u_offY", SHAKE_VERT);
			} else {
				shakeShader.setUniformf("u_offY", -1);
			}
			anim.renderAt(playerBatch, renderX, renderY);
			shakeShader.end();
		}
		playerBuffer.end();
		resumeNormalBuffer();
		
		playerFinalBatch.begin();
		playerFinalBatch.draw(
				playerBuffer.getColorBufferTexture(),	// texture
				0, globalY + OPTIONS_HEIGHT,			// x/y in screen space
				0, 0,									// origin x/y screen
				win.getViewportWidth(), SPRITES_HEIGHT,	// width/height screen
				1, 1,									// scale x/y
				0,										// rotation in degrees
				0, 0,									// x/y in texel space
				win.getViewportWidth(), SPRITES_HEIGHT,	// width/height texel
				false, true								// flip horiz/vert
			);
		playerFinalBatch.end();
		
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
		
		WindowSettings win = MGlobal.window;
		
		enemyBuffer = new FrameBuffer(Format.RGB565,
				win.getViewportWidth(),
				win.getViewportHeight() - OPTIONS_HEIGHT - SPRITES_HEIGHT,
				false);
		enemyBuffer.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		playerBuffer = new FrameBuffer(Format.RGB565,
				win.getViewportWidth(),
				SPRITES_HEIGHT,
				false);
		playerBuffer.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		playerBatch = new SpriteBatch();
		
		enemyBatch = new SpriteBatch();
		enemyMatrix = new Matrix4();
		enemyMatrix.setToOrtho2D(0, 0,
				win.getViewportWidth(),
				win.getViewportHeight() - OPTIONS_HEIGHT - SPRITES_HEIGHT);
		enemyBatch.setProjectionMatrix(enemyMatrix);
		enemyBatch.setShader(background.getShader());
		
		playerBatch = new SpriteBatch();
		playerMatrix = new Matrix4();
		playerMatrix.setToOrtho2D(0, 0,
				win.getViewportWidth(),
				SPRITES_HEIGHT);
		playerBatch.setProjectionMatrix(playerMatrix);
		playerBatch.setShader(shakeShader);
		
		shakeShader = SGlobal.graphics.constructShader(KEY_SHADER_SHAKE);
		
		enemyFinalBatch = new SpriteBatch();
		Matrix4 matrix2 = new Matrix4();
		matrix2.setToOrtho2D(0, 0,
				win.getViewportWidth(),
				win.getViewportHeight());
		enemyFinalBatch.setProjectionMatrix(matrix2);
		
		playerFinalBatch = new SpriteBatch();
		Matrix4 matrix3 = new Matrix4();
		matrix3.setToOrtho2D(0, 0,
				win.getViewportWidth(),
				win.getViewportHeight());
		playerFinalBatch.setProjectionMatrix(matrix3);
		playerFinalBatch.setShader(foreground.getShader());
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (anims.size() != 0) {
			return true;
		} else {
			return super.onCommand(command);
		}
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
		playerBuffer.dispose();
		playerBatch.dispose();
		playerFinalBatch.dispose();
		enemyBuffer.dispose();
		enemyBatch.dispose();
		enemyFinalBatch.dispose();
	}
	
	/**
	 * Prints some text to the battle textbox, right away, right now,
	 * regardless of animation queue. This will cause the battlebox to fade in
	 * on the screen if it isn't already.
	 * @param	line			The text to print
	 */
	public void immediatePrint(String line) {
		if (!containsChild(text)) {
			text.fadeIn(this, TEXT_FADE_TIME);
		}
		text.print(line);
	}
	
	/**
	 * Adds a chunk of text playback to the playback queue, to be called when
	 * everything else in the queue has finished resolving. Appends a space.
	 * @param	line			The text to print
	 */
	public void print(String line) {
		PlayText step = new PlayText(this, line + " ");
		playbackQueue.add(playbackQueue.size(), step);
	}
	
	/**
	 * Adds a line of text playback to the playback queue, to be called when
	 * everything else in the queue has finished resolving. Appends a newline.
	 * @param	line			The text to print
	 */
	public void println(String line) {
		PlayText step = new PlayText(this, line + "\n");
		playbackQueue.add(playbackQueue.size(), step);
	}
	
	/**
	 * Immediately plays a battle animation without regard for the anim queue.
	 * @param	animMDO			The mdo of the animation to play
	 * @param	targets			The targets to play it on
	 */
	public void immediateAnimate(BattleAnimMDO animMDO, List<Chara> targets) {
		List<Integer> groups = constructGroups(targets);
		for (Integer index : groups) {
			BattleAnim anim = BattleAnimFactory.create(animMDO);
			anims.add(anim);
			animsOnGroups.put(index, anim);
			addUChild(anim);
		}
		MGlobal.assets.loadAssets(anims, "battle animation " + animMDO);
		for (PortraitAnim anim : anims) {
			anim.start(this);
		}
	}
	
	/**
	 * Animates a pre-loaded battle animation. Disregards animation queue. Only
	 * call this from anim step classes, basically.
	 * @param	anim			The animation to play
	 * @param	targets			The targets to play it on
	 */
	public void immediateAnimate(PortraitAnim anim, List<Chara> targets) {
		List<Integer> groups = constructGroups(targets);
		anims.add(anim);
		addUChild(anim);
		for (Integer index : groups) {
			animsOnGroups.put(index, anim);
		}
		anim.start(this);
	}
	
	/**
	 * Animates a shake on a player character without regard for play order.
	 * @param	target			The player character to shake
	 */
	public void immediateShake(Chara target) {
		Party heroes = battle.getPlayer();
		for (int i = 0; i < heroes.groupCount(); i += 1) {
			if (heroes.getGroup(i).contains(target)) {
				shakeTimers.put(i, SHAKE_DURATION);
			}
		}
	}
	
	/**
	 * Plays a battle animation on a bunch of dumb targets. The single mdo is
	 * instantiated multiple times so that any RNG use within the anim is reset.
	 * Probably uses ugly searches to map from the list of targets to locations
	 * on screen.
	 * @param	animMDO			The MDO of the animation to play
	 * @param	targets			The enemies to play the animation on
	 */
	public void animate(BattleAnimMDO animMDO, List<Chara> targets) {
		PlaybackStep step = new PlayAnim(this, animMDO, targets);
		playbackQueue.add(step);
	}
	
	/**
	 * Plays the damage-taken animation on some targets. Respects playback. This
	 * is the number popup equivalent of battle animations.
	 * @param	damage			The damage takne
	 * @param	targets			The targets taking the damage
	 */
	public void animateDamage(int damage, List<Chara> targets) {
		PlaybackStep step = new PlayNumber(this, damage, targets);
		playbackQueue.add(step);
	}
	
	/**
	 * Shakes a (player) target. Respects battle playback order.
	 * @param	target			The player to shake
	 */
	public void shake(Chara target) {
		PlaybackStep step = new PlayShake(this, target);
		playbackQueue.add(step);
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
		if (containsChild(fightOptions)) {
			fightOptions.focus();
		} else {
			fightOptions.showAt((int) fightOptions.getX(), (int) fightOptions.getY());
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
	 * Called by the battle when it's time to eat the meat!!
	 */
	public void onMeatChoice() {
		meatOptions.showAt((int) meatOptions.getX(), (int) meatOptions.getY());
		meatOptions.focus();
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
				ABILS_WIDTH, ABILS_LIST_PADDING, true, false);
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
		if (containsChild(fightOptions)) {
			removeChild(fightOptions);
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
	 * Prompts the user to select a character to eat the meat!!
	 * @param	selected		The character to start selected
	 * @param	onHover			The callback for when the cursor moves
	 * @param	onSelection		The callback for when the cursor selects
	 */
	public void selectMeatEater(int selected, SelectionListener onHover,
			final SelectionListener onSelection) {
		miniInserts.setHoverListener(onHover);
		showMeatMessage = true;
		if (containsChild(text)) {
			text.fadeOut(TEXT_FADE_TIME);
		}
		if (containsChild(abils)) {
			removeChild(abils);
		}
		selectAlly(selected, new TargetListener() {
			@Override public void onTargetSelection(List<Chara> targets) {
				boolean close;
				if (targets == null) {
					close = onSelection.onSelection(null);
				} else {
					close = onSelection.onSelection(targets.get(0));
				}
				if (close) {
					removeChild(meatOptions);
					removeChild(miniInserts);
					showMeatMessage = false;
				}
			}
		});
	}
	
	/**
	 * Prompts the player to choose between one of two mutations to apply, then
	 * calls the finish listener. There's no need to save which mutation was
	 * selected because it will already have been applied.
	 * @param	mutations		The mutations to choose from, always size 2
	 * @param	listener		The listener to call when finished
	 */
	public void selectMutation(List<Mutation> mutations, final FinishListener listener) {
		List<Option> options = new ArrayList<Option>();
		for (final Mutation mutation : mutations) {
			options.add(new Option(mutation.getDesc()) {
				@Override public boolean onSelect() {
					mutation.apply();
					listener.onFinish();
					return true;
				}
			});
		}
		OptionSelector mutationOptions = new OptionSelector(true, false,
				options.toArray(new Option[options.size()]));
		mutationOptions.showAt(
				(int) (globalX + (getWidth() - mutationOptions.getWidth()) / 2),
				(int) (globalY + optionsBG.getBorderHeight() - 1));
	}
	
	/**
	 * Sets the informative meat transformation message for meat selection mode.
	 * @param	messages		The messages to display, broken into lines
	 */
	public void setMeatMessage(List<String> messages) {
		this.meatMessages = messages;
	}
	
	/**
	 * Sets the shader used to render all enemy portraits. Used by banims.
	 * @param	shader			The shader to use for enemies
	 */
	public void setBattleShader(ShaderFromData shader) {
		enemyFinalBatch.setShader(shader);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#clear()
	 */
	@Override
	protected void clear() {
		float[] wipe = SGlobal.graphics.getWhite();
		WindowSettings window = MGlobal.window;
		Gdx.gl.glClearColor(wipe[0], wipe[1], wipe[2], 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shapes.setColor(wipe[0], wipe[1], wipe[2], 1);
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
		removeCommandListener(this);
	}
	
	/**
	 * Constructs a set of the indices of groups containing the targets
	 * @param	targets			The targets to get the groups of
	 * @return					A list of the groups containing those targets
	 */
	protected List<Integer> constructGroups(List<Chara> targets) {
		List<Integer> groups = new ArrayList<Integer>();
		for (Chara enemy : targets) {
			int index = battle.getEnemy().index(enemy);
			if (!groups.contains(index)) {
				groups.add(index);
			}
		}
		return groups;
	}

}
