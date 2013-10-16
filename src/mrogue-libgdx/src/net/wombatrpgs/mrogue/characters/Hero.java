/**
 *  Hero.java
 *  Created on Nov 25, 2012 8:33:22 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

import net.wombatrpgs.mrogue.characters.ai.act.ActStep;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.io.CommandListener;
import net.wombatrpgs.mrogue.maps.Level;
import net.wombatrpgs.mrogueschema.characters.HeroMDO;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;
import net.wombatrpgs.mrogueschema.maps.data.EightDir;

/**
 * Placeholder class for the protagonist player.
 */
public class Hero extends CharacterEvent implements CommandListener {
	
	protected static final String HERO_DEFAULT = "hero_default";
	
	protected ActStep step;
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
	 * MR: Creates the hero, places it on a map, sets its x/y.
	 * @param	object			The tiled obejct that generated the character
	 * @param 	parent			The level the hero starts on
	 * @param	tileX			The x-coord to start on, in tiles
	 * @param	tileY			The y-coord to start on, in tiles
	 */
	public Hero(Level parent, int tileX, int tileY) {
		// TODO: Hero
		super(MGlobal.data.getEntryFor(HERO_DEFAULT, HeroMDO.class), parent, tileX, tileY);
		this.unit = new HeroUnit(mdo, this);
		MGlobal.hero = this;
		step = new ActStep(this);
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
	 * @see net.wombatrpgs.mrogue.maps.MapThing#onAddedToMap
	 * (net.wombatrpgs.mrogue.maps.Level)
	 */
	@Override
	public void onAddedToMap(Level map) {
		super.onAddedToMap(map);
		seenCache = new boolean[map.getHeight()][map.getWidth()];
		refreshVisibilityMap();
	}

	/**
	 * @see net.wombatrpgs.mrogue.characters.CharacterEvent#reset()
	 */
	@Override
	public void reset() {
		// oh hell no we ain't dyin
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#getName()
	 */
	@Override
	public String getName() {
		return "hero";
	}

	/**
	 * @see net.wombatrpgs.mrogue.characters.CharacterEvent#inLoS(int, int)
	 */
	@Override
	public boolean inLoS(int targetX, int targetY) {
		if (viewCache == null) return false;
		return viewCache[targetY][targetX];
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.CommandListener#onCommand
	 * (net.wombatrpgs.mrogueschema.io.data.InputCommand)
	 */
	@Override
	public void onCommand(InputCommand command) {
		if (parent.isMoving()) {
			return;
		}
		EightDir dir;
		if (command == InputCommand.MOVE_WAIT) {
			actAndWait(defaultWait);
		} else {
			switch (command) {
			case MOVE_NORTH:		dir = EightDir.NORTH;		break;
			case MOVE_NORTHEAST:	dir = EightDir.NORTHEAST;	break;
			case MOVE_EAST:			dir = EightDir.EAST;		break;
			case MOVE_SOUTHEAST:	dir = EightDir.SOUTHEAST;	break;
			case MOVE_SOUTH:		dir = EightDir.SOUTH;		break;
			case MOVE_SOUTHWEST:	dir = EightDir.SOUTHWEST;	break;
			case MOVE_WEST:			dir = EightDir.WEST;		break;
			case MOVE_NORTHWEST:	dir = EightDir.NORTHWEST;	break;
			default:				dir = null;					break;
			}
			step.setDirection(dir);
			actAndWait(step);
		}
		refreshVisibilityMap();
		parent.onTurn();
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
		int startTX = tileX - getStats().getVision();
		int startTY = tileY - getStats().getVision();
		int endTX = tileX + getStats().getVision();
		int endTY = tileY + getStats().getVision();
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
	
}
