/**
 *  ActSummon.java
 *  Created on Dec 29, 2012 1:02:07 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.moveset;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.characters.Block;
import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.collisions.FallResult;
import net.wombatrpgs.rainfall.collisions.Hitbox;
import net.wombatrpgs.rainfall.collisions.RectHitbox;
import net.wombatrpgs.rainfall.collisions.TargetPosition;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Direction;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.AnimationPlayer;
import net.wombatrpgs.rainfall.maps.objects.TimerListener;
import net.wombatrpgs.rainfall.maps.objects.TimerObject;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.SummonMDO;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;

/**
 * Summon the block to your side!
 */
public class ActSummon extends MovesetAct {
	
	protected SummonMDO mdo;
	protected AnimationPlayer player;
	
	/**
	 * Creates and initializes this summon MDO. Involves loading an image, it
	 * seems.
	 * @param	actor			The character performing the action
	 * @param 	mdo				The MDO singleton devoted to this act
	 */
	public ActSummon(CharacterEvent actor, SummonMDO mdo) {
		super(actor, mdo);
		this.mdo = mdo;
		AnimationMDO animMDO = RGlobal.data.getEntryFor(mdo.blockAnimation, AnimationMDO.class);
		this.player = new AnimationPlayer(animMDO);
	}

	/**
	 * @see net.wombatrpgs.rainfall.moveset.Actionable#act
	 * (net.wombatrpgs.rainfall.maps.Level, net.wombatrpgs.rainfall.characters.CharacterEvent)
	 */
	@Override
	public void coreAct(Level map, final CharacterEvent actor) {
		if (actor.isMoveActive(this)) return;
		actor.halt();
		actor.startAction(this);
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
		final MovesetAct parent = this;
		new TimerObject(mdo.duration * 1.2f, RGlobal.hero, new TimerListener() {
			@Override
			public void onTimerZero(TimerObject source) {
				actor.stopAction(parent);
				RGlobal.block.setSummoning(false);
			}
		});
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
	private void summonAt(final Level map, 
			final int targetTileX, final int targetTileY, final int z) {
		if (RGlobal.block.getLevel() != null) {
			RGlobal.block.getLevel().removeEvent(RGlobal.block);
		}
		map.addEvent(player, 0, 0, map.getZ(actor));
		player.setX(targetTileX * map.getTileWidth());
		player.setY(targetTileY * map.getTileHeight());
		player.start();
		new TimerObject(mdo.duration, RGlobal.hero, new TimerListener() {
			@Override
			public void onTimerZero(TimerObject source) {
				map.addEvent(RGlobal.block, targetTileX, targetTileY, z);
			}
		});
		RGlobal.block.setSummoning(true);
	}
	
	/**
	 * Nicely animates the block landing (and killing) itself at the location.
	 * @param	map				The map to drop the block on
	 * @param 	targetX			The x-coord to land at (in tiles)
	 * @param 	targetY			The y-coord to land at (in tiles)
	 */
	private void selfDestructAt(Level map, int targetTileX, int targetTileY) {
		RGlobal.reporter.inform("BOOM! Summon failed.");
		if (RGlobal.block != null && RGlobal.block.getLevel() != null) {
			RGlobal.block.getLevel().removeEvent(RGlobal.block);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.moveset.MovesetAct#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		player.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.moveset.MovesetAct#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void postProcessing(AssetManager manager) {
		super.postProcessing(manager);
		player.postProcessing(manager);
	}

}
