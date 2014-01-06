/**
 *  Hero.java
 *  Created on Nov 25, 2012 8:33:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.rpg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

import net.wombatrpgs.saga.core.Constants;
import net.wombatrpgs.saga.core.MGlobal;
import net.wombatrpgs.saga.io.CommandListener;
import net.wombatrpgs.saga.maps.Level;
import net.wombatrpgs.saga.scenes.SceneParser;
import net.wombatrpgs.saga.screen.instances.GameOverScreen;
import net.wombatrpgs.sagaschema.characters.HeroMDO;
import net.wombatrpgs.sagaschema.io.data.InputCommand;
import net.wombatrpgs.sagaschema.settings.DeathSettingsMDO;

/**
 * Placeholder class for the protagonist player.
 */
public class Hero extends CharacterEvent implements CommandListener {
	
	public static final int ABILITIES_MAX = 6;
	
	protected static final String HERO_DEFAULT = "hero_default";
	
	protected SceneParser deathScene;
	
	// to facilitate shader calls, viewtex is like a b/w image version of cache
	protected boolean[][] viewCache;
	protected boolean[][] seenCache;
	protected Pixmap p;
	protected Texture viewTex;

	/**
	 * Placeholder constructor. When the hero is finally initialized properly
	 * this will change. Right now it sets up the hero on the map like any other
	 * event. Also sets up the moveset called "default_moveset" though that
	 * should be put in the hero MDO when it gets created.
	 * MR: Creates the hero
	 * @param	parent			The level to make the hero on
	 */
	public Hero(Level parent) {
		super(MGlobal.data.getEntryFor(HERO_DEFAULT, HeroMDO.class));
		this.parent = parent;
		MGlobal.hero = this;
		DeathSettingsMDO deathMDO = MGlobal.data.getEntryFor(
				Constants.KEY_DEATH, DeathSettingsMDO.class);
		deathScene = MGlobal.levelManager.getCutscene(deathMDO.scene);
		assets.add(deathScene);
	}
	
	/**
	 * See above. Deprecated.
	 * @param mdo
	 * @param parent
	 */
	public Hero(HeroMDO mdo, Level parent) {
		super(mdo, parent);
	}

	/**
	 * @see net.wombatrpgs.saga.maps.MapThing#onAddedToMap
	 * (net.net.wombatrpgs.mrogue.maps.Level)
	 */
	@Override
	public void onAddedToMap(Level map) {
		super.onAddedToMap(map);
		seenCache = new boolean[map.getHeight()][map.getWidth()];
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.CharacterEvent#reset()
	 */
	@Override
	public void reset() {
		// oh hell no we ain't dyin
	}

	/**
	 * @see net.wombatrpgs.saga.maps.events.MapEvent#getName()
	 */
	@Override
	public String getName() {
		return "hero";
	}

	/**
	 * @see net.wombatrpgs.saga.rpg.CharacterEvent#inLoS(int, int)
	 */
	@Override
	public boolean inLoS(int targetX, int targetY) {
		if (viewCache == null) return false;
		return viewCache[targetY][targetX];
	}
	
	/**
	 * @see net.wombatrpgs.saga.rpg.CharacterEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (unit.isDead() && !MGlobal.raveMode) {
			if (!deathScene.hasExecuted()) {
				if (!deathScene.isRunning()) {
					MGlobal.deathCount += 1;
					deathScene.run();
					MGlobal.screens.playMusic(null, false);
				}
			} else {
				MGlobal.screens.pop();
				MGlobal.screens.push(new GameOverScreen());
				MGlobal.screens.playMusic(null, false);
			}
		}
	}

	/**
	 * @see net.wombatrpgs.saga.io.CommandListener#onCommand
	 * (net.wombatrpgs.sagaschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		// TODO: onCommand
		return true;
	}
	
	/**
	 * Creates a cached table of which squares are in view. Call when things
	 * move etc.
	 */
	public void refreshVisibilityMap() {
		if (viewTex != null) {
			p.dispose();
		}
		p = new Pixmap(parent.getWidth(), parent.getHeight(), Format.RGBA8888);
		viewCache = new boolean[parent.getHeight()][parent.getWidth()];
		Pixmap.setBlending(Blending.SourceOver);
		p.setColor(Color.BLACK);
		p.fillRectangle(0, 0, parent.getWidth(), parent.getHeight());
		int startTX = tileX - getStats().vision;
		int startTY = tileY - getStats().vision;
		int endTX = tileX + getStats().vision;
		int endTY = tileY + getStats().vision;
		if (startTX < 0) startTX = 0;
		if (startTY < 0) startTY = 0;
		if (endTX > parent.getWidth()) endTX = parent.getWidth();
		if (endTY > parent.getHeight()) endTY = parent.getHeight();
		for (int x = startTX; x < endTX; x += 1) {
			for (int y = startTY; y < endTY; y += 1) {
				boolean result = super.inLoS(x, y);
				viewCache[y][x] = result;
				if (result) seenCache[y][x] = true;
			}
		}
		for (int x = 0; x < parent.getWidth(); x += 1) {
			for (int y = 0; y < parent.getHeight(); y += 1) {
				float r = viewCache[y][x] ? 1 : 0;
				float g = seenCache[y][x] ? 1 : 0;
				p.setColor(r, g, 0, 1);
				p.drawPixel(x, y);
			}
		}
		viewTex = new Texture(p, false);
		viewTex.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
	}
	
	/**
	 * A very technical thing that returns technical things. For shaders.
	 * @return					I wrote this with a bad cold.
	 */
	public Texture getVisibleData() {
		return viewTex;
	}
	
	/**
	 * Checks if the hero has visited a particular tile on the map.
	 * @param	tileX			The x-coord of the tile to check, in tiles
	 * @param	tileY			The y-coord of the tile to check, in tiles
	 * @return					True if tile was visited, false otherwise
	 */
	public boolean seen(int tileX, int tileY) {
		return seenCache[tileY][tileX];
	}
	
	/**
	 * @see net.wombatrpgs.saga.rpg.CharacterEvent#initUnit()
	 */
	@Override
	protected void initUnit() {
		this.unit = new HeroUnit(mdo, this);
	}
}
