/**
 *  Enemy.java
 *  Created on Jan 23, 2013 9:14:38 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.ai.Intelligence;
import net.wombatrpgs.rainfall.core.Constants;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.particles.Emitter;
import net.wombatrpgs.rainfall.graphics.particles.GibParticleSet;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfallschema.characters.enemies.EnemyEventMDO;
import net.wombatrpgs.rainfallschema.characters.enemies.VulnerabilityMDO;
import net.wombatrpgs.rainfallschema.characters.enemies.ai.IntelligenceMDO;
import net.wombatrpgs.rainfallschema.graphics.EmitterMDO;
import net.wombatrpgs.rainfallschema.graphics.GibsetMDO;

/**
 * The one and only class for those pesky badniks that hunt down the valiant
 * hero and hinder his quest to save the earth.
 */
public class EnemyEvent extends CharacterEvent {
	
	protected static final String PROPERTY_RESPAWNS = "respawns";
	
	protected EnemyEventMDO mdo;
	protected Intelligence ai;
	protected Vulnerability vuln;
	protected Emitter emitter;
	protected int againstWall;
	
	/**
	 * Creates a new enemy on a map from a database entry.
	 * @param 	mdo				The MDO with data to create from
	 * @param	object			The object on the map that made us
	 * @param 	parent			The parent map of the object
	 * @param	x				The initial x-coord (in tiles)
	 * @param	y				The intitila y-coord (in tiles)
	 */
	public EnemyEvent(EnemyEventMDO mdo, TiledObject object, Level parent, int x, int y) {
		super(mdo, object, parent, x, y);
		this.mdo = mdo;
		againstWall = 0;
		IntelligenceMDO aiMDO = RGlobal.data.getEntryFor(
				mdo.intelligence, IntelligenceMDO.class);
		ai = new Intelligence(aiMDO, this);
		VulnerabilityMDO vulnMDO = RGlobal.data.getEntryFor(
				mdo.vulnerability, VulnerabilityMDO.class);
		vuln = new Vulnerability(vulnMDO);
		if (!Constants.NULL_MDO.equals(mdo.emitter) && !Constants.NULL_MDO.equals(mdo.gibset)) {
			GibsetMDO gibsetMDO = RGlobal.data.getEntryFor(mdo.gibset, GibsetMDO.class);
			EmitterMDO emitterMDO = RGlobal.data.getEntryFor(mdo.emitter, EmitterMDO.class);
			GibParticleSet gibs = new GibParticleSet(gibsetMDO);
			emitter = new Emitter(emitterMDO, gibs);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (canAct()) ai.act();
		super.update(elapsed);
		if (againstWall > 0) againstWall -= 1;
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#onCharacterCollide
	 * (net.wombatrpgs.rainfall.characters.CharacterEvent,
	 * net.wombatrpgs.rainfall.physics.CollisionResult)
	 */
	@Override
	public boolean onCharacterCollide(CharacterEvent other, CollisionResult result) {
		if (dead) return true;
		if (other == RGlobal.block) {
			if (vuln.killableByTouch()) {
				selfDestruct(RGlobal.block);
				return true;
			}
			if (vuln.killableByPush() && RGlobal.block.isMoving()) {
				selfDestruct(RGlobal.block);
				return true;
			}
			if (vuln.killableBySummon() && RGlobal.block.isSummoning()) {
				selfDestruct(RGlobal.block);
				return true;
			}
			if (vuln.killableByPin() && againstWall > 0) {
				selfDestruct(RGlobal.block);
				return true;
			}
		}
		return super.onCharacterCollide(other, result); // ie false
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#supportsBlockLanding()
	 */
	@Override
	public boolean supportsBlockLanding() {
		return vuln.killableBySummon();
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		ai.queueRequiredAssets(manager);
		if (emitter != null) {
			emitter.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		ai.postProcessing(manager, pass);
		if (emitter != null) {
			emitter.postProcessing(manager, pass);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#resolveWallCollision
	 * (net.wombatrpgs.rainfall.physics.CollisionResult)
	 */
	@Override
	public void resolveWallCollision(CollisionResult result) {
		super.resolveWallCollision(result);
		againstWall = 2;
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.CharacterEvent#reset()
	 */
	@Override
	public void reset() {
		if (object == null) {
			super.reset();
		} else {
			if (object.properties.get(PROPERTY_RESPAWNS) != null) {
				super.reset();
			} else {
				if (!dead) super.reset();
				// we're still dead :(
			}
		}
	}

	/**
	 * Kills self in a spectacular manner. Another object is supplied so that
	 * gibs can scatter correctly. If this enemy just imploded randomly, then
	 * pass in itself and the distribution will be random.
	 * @param 	cause			The event that caused this enemy's death
	 */
	public void selfDestruct(MapEvent cause) {
		if (emitter != null) {
			float xComp, yComp;
			if (cause.isMoving()) {
				xComp = cause.getVX();
				yComp = cause.getVY();
			} else {
				xComp = cause.getX() - getX();
				yComp = cause.getY() - getY();
			}
			float norm = (float) Math.sqrt(xComp*xComp + yComp*yComp);
			norm *= .8;
			xComp /= norm;
			yComp /= norm;
			if (!parent.contains(emitter)) {
				parent.addEvent(emitter, 0, 0, parent.getZ(this));
			}
			emitter.setX(getX());
			emitter.setY(getY());
			emitter.fire(xComp, yComp);
		}
		kill();
		setX(0);
		setY(0);
	}
	
}
