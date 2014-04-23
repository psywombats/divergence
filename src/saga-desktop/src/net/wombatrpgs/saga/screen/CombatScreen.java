/**
 *  CombatScreen.java
 *  Created on Apr 15, 2014 2:26:58 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.screen.WindowSettings;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.Option;
import net.wombatrpgs.mgne.ui.OptionSelector;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextBoxFormat;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;
import net.wombatrpgs.saga.rpg.Battle;
import net.wombatrpgs.saga.rpg.Chara;
import net.wombatrpgs.saga.rpg.Party;
import net.wombatrpgs.saga.ui.BattleBox;
import net.wombatrpgs.saga.ui.CharaSelector;

/**
 * Screen for killing shit. This also encompasses a battle. Like Tactics, the
 * idea is that a battle is owned by a screen and controls the screen, but the
 * logic is kept separate from the display. Owned by a battle.
 */
public class CombatScreen extends Screen {
	
	protected static final int OPTIONS_WIDTH = 108;
	protected static final int OPTIONS_HEIGHT = 96;
	protected static final int OPTIONS_MARGIN = 2;
	protected static final int INSERTS_WIDTH = 212;
	protected static final int INSERTS_HEIGHT = 96;
	protected static final int MONSTERLIST_WIDTH = 124;
	protected static final int MONSTERLIST_HEIGHT = 48;
	protected static final int MONSTERLIST_MARGIN = 10;
	protected static final int SPRITES_HEIGHT = 24;
	
	protected static final int TEXT_LINES = 6;
	protected static final float TEXT_FADE_TIME = .1f;
	
	protected Battle battle;
	
	protected Nineslice optionsBG, insertsBG, monsterlistBG;
	protected CharaSelector partyInserts, enemyInserts;
	protected TextBoxFormat monsterlistFormat;
	protected String[] monsterlist;
	protected OptionSelector options;
	protected List<FacesAnimation> sprites;
	protected BattleBox text;
	protected float globalX, globalY;
	
	protected boolean showEnemyInserts;
	protected boolean showMonsterList;
	
	/**
	 * Creates a new combat setup. This initializes the screen and passes the
	 * arguments to the battle.
	 * @param	battle			The battle this screen will be used for
	 */
	public CombatScreen(final Battle battle) {
		this.battle = battle;
		pushCommandContext(new CMapMenu());
		
		showEnemyInserts = false;
		showMonsterList = true;
		
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
		monsterlistBG = new Nineslice(MONSTERLIST_WIDTH, MONSTERLIST_HEIGHT);
		assets.add(optionsBG);
		assets.add(insertsBG);
		assets.add(monsterlistBG);
		
		partyInserts = new CharaSelector(battle.getPlayer(), true, true, false, 5);
		enemyInserts = new CharaSelector(battle.getEnemy(), true, true, false, 5);
		assets.add(partyInserts);
		assets.add(enemyInserts);
		addUChild(partyInserts);
		addUChild(enemyInserts);
		
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

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		insertsBG.renderAt(batch, globalX + OPTIONS_WIDTH - optionsBG.getBorderWidth(), globalY);
		optionsBG.renderAt(batch, globalX, globalY);
		if (showEnemyInserts) {
			enemyInserts.render(batch);
		} else {
			partyInserts.render(batch);
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
		WindowSettings win = MGlobal.window;
		for (int i = 0 ; i < groups; i += 1) { 
			List<Chara> group = enemy.getGroup(i);
			if (group.size() == 0) continue;
			Graphic portrait = enemy.getFront(i).getPortrait();
			if (portrait == null) continue;
			float renderX = globalX + (win.getViewportWidth() - portrait.getWidth()*groups) * (i+1)/(groups+1) +
					(portrait.getWidth()) * i;
			float renderY = globalY + OPTIONS_HEIGHT + SPRITES_HEIGHT +
					((win.getViewportHeight() - OPTIONS_HEIGHT - SPRITES_HEIGHT) - portrait.getHeight()) / 2;
			portrait.renderAt(batch, renderX, renderY);
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
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		options.showAt((int) options.getX(), (int) options.getY()); 
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
	 * Called by the battle textbox when its text has been consumed.
	 */
	public void onTextFinished() {
		// TODO: battle: only advance if the animation is done too
		battle.onPlaybackFinished();
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
			monsterlist[i] = monsterlist[i] + " x" + group.size();
		}
	}

}
