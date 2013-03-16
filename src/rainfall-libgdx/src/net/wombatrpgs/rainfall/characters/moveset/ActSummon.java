/**
 *  ActSummon.java
 *  Created on Dec 29, 2012 1:02:07 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.moveset;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.characters.Block;
import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.AnimationPlayer;
import net.wombatrpgs.rainfall.maps.objects.TimerListener;
import net.wombatrpgs.rainfall.maps.objects.TimerObject;
import net.wombatrpgs.rainfall.physics.FallResult;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.physics.RectHitbox;
import net.wombatrpgs.rainfall.physics.TargetPosition;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.SummonMDO;
import net.wombatrpgs.rainfallschema.graphics.AnimationMDO;
import net.wombatrpgs.rainfallschema.maps.data.Direction;

/**
 * Summon the block to your side!
 */
public class ActSummon extends MovesetAct {
	
	protected SummonMDO mdo;
	protected AnimationPlayer goodPlayer;
	protected Block myBlock;
	
	/**
	 * Creates and initializes this summon MDO. Involves loading an image, it
	 * seems.
	 * @param	actor			The character performing the action
	 * @param 	mdo				The MDO singleton devoted to this act
	 */
	public ActSummon(CharacterEvent actor, SummonMDO mdo) {
		super(actor, mdo);
		this.mdo = mdo;
		myBlock = new Block(mdo);
		if (RGlobal.block == null) {
			RGlobal.block = myBlock;
		}
		AnimationMDO goodMDO = RGlobal.data.getEntryFor(mdo.blockAnimation, AnimationMDO.class);
		this.goodPlayer = new AnimationPlayer(goodMDO);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.Actionable#act
	 * (net.wombatrpgs.rainfall.maps.Level, net.wombatrpgs.rainfall.characters.CharacterEvent)
	 */
	@Override
	public void coreAct(Level map, final CharacterEvent actor) {
		if (RGlobal.block != null && map.contains(RGlobal.block) && 
				map.getZ(actor) == map.getZ(RGlobal.block)+1 &&
				RGlobal.block.getUpperBox().isColliding(actor.getHitbox()).isColliding) {
			return;
		}
		if (actor.isMoveActive(this)) return;
		actor.halt();
		actor.startAction(this);
		float heroX = RGlobal.hero.getX() + RGlobal.hero.getHitbox().getWidth()/2;
		float heroY = RGlobal.hero.getY() + RGlobal.hero.getHitbox().getHeight()/2;
		int targetTileX = Math.round((heroX - heroX % 32) / 32); // TODO: generalize the size
		int targetTileY = Math.round((heroY - heroY % 32) / 32);
		Direction dir = RGlobal.hero.getFacing();
		targetTileX += dir.getVector().x;
		targetTileY += dir.getVector().y;
		
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
				RGlobal.block.selfDestructAt(map, targetTileX, targetTileY, result.z);
			}
		} else {
			// there was nothing to drop the block onto
		}
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
	 * @param 	deltaX			The x-coord to land at (in tiles)
	 * @param 	deltaY			The y-coord to land at (in tiles)
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
	 * @param 	deltaX			The x-coord to land at (in tiles)
	 * @param 	deltaY			The y-coord to land at (in tiles)
	 * @param	z				The z-coord to land at (depth index)
	 */
	private void summonAt(final Level map, 
			final int targetTileX, final int targetTileY, final int z) {
		if (RGlobal.block.getLevel() != null) {
			RGlobal.block.getLevel().removeEvent(RGlobal.block);
		}
		if (RGlobal.block != myBlock) {
			RGlobal.block = myBlock;
		}
		map.addEvent(goodPlayer, targetTileX, targetTileY, z);
		goodPlayer.start();
		new TimerObject(mdo.duration, RGlobal.hero, new TimerListener() {
			@Override
			public void onTimerZero(TimerObject source) {
				if (RGlobal.hero.canAct()) {
					map.addEventAbsolute(RGlobal.block, 
							targetTileX * map.getTileWidth(), 
							targetTileY * map.getTileHeight() + 2, // fudge factor
							z);
				}
			}
		});
		RGlobal.block.setSummoning(true);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.MovesetAct#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		myBlock.queueRequiredAssets(manager);
		goodPlayer.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.MovesetAct#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		myBlock.postProcessing(manager, pass);
		goodPlayer.postProcessing(manager, pass);
	}

}
