/**
 *  IntentSpitfire.java
 *  Created on Feb 11, 2013 8:03:55 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.ai.actions;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.characters.ai.Intelligence;
import net.wombatrpgs.rainfall.characters.ai.IntentAct;
import net.wombatrpgs.rainfall.characters.enemies.EnemyEvent;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.objects.TimerListener;
import net.wombatrpgs.rainfall.maps.objects.TimerObject;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfallschema.characters.enemies.EnemyEventMDO;
import net.wombatrpgs.rainfallschema.maps.data.DirVector;

/**
 * Shoot fire towards the hero.
 */
public class IntentSpitfire extends IntentAct {
	
	protected static final String FIREBALL_MDO_KEY = "enemy_fireball";
	protected static final float RATE_OF_FIRE = 4; // shots per second
	protected static final int MAX_FIREBALLS = 8;
	protected static final int AIM_VARIANCE = 6; // degrees
	protected static final int MAX_ANGLE = 3; // degrees
	
	protected List<EnemyEvent> fireballs;
	protected boolean charging;
	protected int fireIndex;

	public IntentSpitfire(Intelligence parent, CharacterEvent actor) {
		super(parent, actor);
		charging = false;
		fireballs = new ArrayList<EnemyEvent>();
		fireIndex = 0;
		EnemyEventMDO mdo = RGlobal.data.getEntryFor(FIREBALL_MDO_KEY, EnemyEventMDO.class);
		for (int i = 0; i < MAX_FIREBALLS; i++) {
			fireballs.add(new EnemyEvent(mdo, null, actor.getLevel(), 0, 0) {
				@Override
				public void resolveWallCollision(CollisionResult result) {
					getLevel().removeEvent(this);
				}
			});
		}
	}

	@Override
	public void act() {
		if (!charging) {
			Level map = actor.getLevel();
			DirVector vec = actor.getFacing().getVector();
			int tileX = actor.getX() + (int) vec.x * map.getTileWidth()/3;
			int tileY = actor.getY() + (int) vec.y * map.getTileHeight()/3;
			EnemyEvent fireball = fireballs.get(fireIndex);
			fireIndex = (fireIndex + 1) % MAX_FIREBALLS;
			if (!map.contains(fireball)) map.addEvent(fireball, map.getZ(actor));
			fireball.setX(tileX);
			fireball.setY(tileY);
			fireball.setDead(false);
			int dx = RGlobal.hero.getX() - fireball.getX();
			int dy = RGlobal.hero.getY() - fireball.getY();
			float angle = (float) Math.atan2(dy, dx);
			angle += (RGlobal.rand.nextFloat()-.5) * 2.0 *(float) AIM_VARIANCE * (Math.PI / 180f);
			if (angle < 0) angle += 2*Math.PI;
			float targetAngle = angle;
			if (Math.abs(angle-Math.PI/4 - Math.PI*0/2) < Math.PI/2) targetAngle = (float) Math.PI*0/2;
			if (Math.abs(angle-Math.PI/4 - Math.PI*1/2) < Math.PI/2) targetAngle = (float) Math.PI*1/2;
			if (Math.abs(angle-Math.PI/4 - Math.PI*2/2) < Math.PI/2) targetAngle = (float) Math.PI*2/2;
			if (Math.abs(angle-Math.PI/4 - Math.PI*3/2) < Math.PI/2) targetAngle = (float) Math.PI*3/2;
			if (Math.abs(angle-Math.PI/4 - Math.PI*4/2) < Math.PI/2) targetAngle = (float) Math.PI*0/2;
			float maxVariance = (float) ((float) MAX_ANGLE * (Math.PI / 180f));
			if (angle > targetAngle) {
				if (angle > targetAngle + maxVariance) angle = targetAngle + maxVariance;
			} else {
				if (angle < targetAngle - maxVariance) angle = targetAngle - maxVariance;
			}
			float v = fireball.getSpeed();
			float targetVX = (float) (Math.cos(angle) * v);
			float targetVY = (float) (Math.sin(angle) * v);
			fireball.setVelocity(0, 0);
			fireball.targetVelocity(targetVX, targetVY);
			charging = true;
			new TimerObject(1.0f / RATE_OF_FIRE, actor, new TimerListener() {
				@Override
				public void onTimerZero(TimerObject source) {
					charging = false;
				}
			});
		} else {
			actor.faceToward(RGlobal.hero);
		}
	}

	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		for (EnemyEvent fireball : fireballs) {
			fireball.queueRequiredAssets(manager);
		}
	}

	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		for (EnemyEvent fireball : fireballs) {
			fireball.postProcessing(manager, pass);
		}
	}

}
