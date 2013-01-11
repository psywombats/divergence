/**
 *  ActSummon.java
 *  Created on Dec 29, 2012 1:02:07 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.moveset;

import net.wombatrpgs.rainfall.characters.Block;
import net.wombatrpgs.rainfall.collisions.FallResult;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.collisions.RectHitbox;
import net.wombatrpgs.rainfall.collisions.TargetPosition;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Direction;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfallschema.hero.moveset.SummonMDO;

/**
 * Summon the block to your side!
 */
// TODO: handle z, anywhere
public class ActSummon implements Actionable {
	
	protected SummonMDO mdo;
	
	/**
	 * Creates and initializes this summon MDO. Involves loading an image, it
	 * seems.
	 * @param 	mdo			The MDO singleton devoted to this act
	 */
	public ActSummon(SummonMDO mdo) {
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.rainfall.moveset.Actionable#act
	 * (net.wombatrpgs.rainfall.maps.Level)
	 */
	@Override
	public void act(Level map) {
		int heroX = RGlobal.hero.getX() + RGlobal.hero.getHitbox().getWidth()/2;
		int heroY = RGlobal.hero.getY() + RGlobal.hero.getHitbox().getHeight()/2;
		int targetTileX = (heroX - heroX % 32) / 32;
		int targetTileY = (heroY - heroY % 32) / 32;
		Direction dir = RGlobal.hero.getFacing();
		targetTileX += dir.getVector().x;
		targetTileY += dir.getVector().y;
		
		if (RGlobal.block == null) {
			RGlobal.block = new Block(mdo);
		}
		FallResult result = attemptFallAt(map, targetTileX, targetTileY);
		if (result.finished && !result.cleanLanding && result.collidingObject == RGlobal.hero) {
			targetTileX += dir.getVector().x;
			targetTileY += dir.getVector().y;
			result = attemptFallAt(map, targetTileX, targetTileY);
		}
		if (result.finished) {
			if (result.cleanLanding || result.collidingObject == RGlobal.block) {
				int deltaZ = result.z - map.getZ(RGlobal.hero);
				summonAt(map, targetTileX, targetTileY + deltaZ, result.z);
			} else {
				selfDestructAt(map, targetTileX, targetTileY);
			}
		} else {
			// there was nothing to drop the block onto
			RGlobal.reporter.inform("Fell off into the abyss!!!");
		}
		RGlobal.reporter.inform(result.toString());
	}
	
	/**
	 * Returns what would happen if the block were dropped at this tile.
	 * @param	map				The map to drop on
	 * @param 	targetX			The x-coord to land at (in tiles)
	 * @param 	targetY			The y-coord to land at (in tiles)
	 * @return					The result of the potential fall
	 */
	private FallResult attemptFallAt(Level map, int targetTileX, int targetTileY) {
		final int targetX = targetTileX * map.getTileWidth();
		final int targetY = targetTileY * map.getTileHeight();
		TargetPosition target = new TargetPosition(targetX, targetY);
		Hitbox targetBox = new RectHitbox(target, 0, 0, 32, 32);
		return map.dropObject(targetBox, map.getZ(RGlobal.hero) + .5f, target);
	}
	
	/**
	 * Nicely animates the block landing at the specified location.
	 * @param	map				The map to drop the block on
	 * @param 	targetX			The x-coord to land at (in tiles)
	 * @param 	targetY			The y-coord to land at (in tiles)
	 * @param	z				The z-coord to land at (depth index)
	 */
	private void summonAt(Level map, int targetTileX, int targetTileY, int z) {
		RGlobal.block.setX(targetTileX * map.getTileWidth());
		RGlobal.block.setY(targetTileY * map.getTileHeight());
		if (RGlobal.block.getLevel() != null) {
			RGlobal.block.getLevel().teleportOff(RGlobal.block);
		}
		map.teleportOn(RGlobal.block, targetTileX, targetTileY, z);
	}
	
	/**
	 * Nicely animates the block landing (and killing) itself at the location.
	 * @param	map				The map to drop the block on
	 * @param 	targetX			The x-coord to land at (in tiles)
	 * @param 	targetY			The y-coord to land at (in tiles)
	 */
	private void selfDestructAt(Level map, int targetTileX, int targetTileY) {
		RGlobal.reporter.inform("BOOM! Summon failed.");
		RGlobal.block.getLevel().teleportOff(RGlobal.block);
	}

}
