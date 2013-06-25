/**
 *  ActDummyAttack.java
 *  Created on Jun 16, 2013 12:20:50 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.moveset;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.maps.objects.TimerListener;
import net.wombatrpgs.rainfall.maps.objects.TimerObject;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.AttackMDO;
import net.wombatrpgs.rainfallschema.maps.data.Direction;

/**
 * Daddy of all attacks. It's not abstract, so basic "hit the dummy" attacks
 * work fine.
 */
public class ActAttack extends MovesetAct {
	
	protected AttackMDO mdo;
	protected boolean hitSomething;
	protected Map<Direction, Integer> attackRanges;

	/**
	 * Creates a new attack from data. Should be called from subclasses too.
	 * @param 	actor			The actor performing the attack
	 * @param 	mdo				The data to make the attack from
	 */
	public ActAttack(CharacterEvent actor, AttackMDO mdo) {
		super(actor, mdo);
		this.hitSomething = false;
		this.mdo = mdo;
		this.attackRanges = new HashMap<Direction, Integer>();
	}
	
	/** @return How long this attack's stun should last, in s */
	public float getStunDuration() { return mdo.stun; }
	
	/** @return The kickback velocity, in px/s */
	public int getKnockback() { return mdo.kick; }

	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.MovesetAct#coreAct
	 * (net.wombatrpgs.rainfall.maps.Level, net.wombatrpgs.rainfall.characters.CharacterEvent)
	 */
	@Override
	public void coreAct(Level map, final CharacterEvent actor) {
		actor.startAction(this);
		
		// this is general enough
		hitSomething = false;
		float duration = this.idleAppearance.getDuration();
		final MovesetAct parentMove = this;
		new TimerObject(duration, actor, new TimerListener() {
			@Override public void onTimerZero(TimerObject source) {
				actor.stopAction(parentMove);
			}
		});
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.MovesetAct#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		Hitbox attackBox = actor.getAppearance().getAttackBox();
		if (attackBox != null && !hitSomething) {
			for (MapEvent event : actor.getLevel().getEvents()) {
				if (event.isCollisionEnabled() &&
						event.getHitbox().isColliding(attackBox).isColliding) {
					event.respondToAttack(this);
					hitSomething = true;
					break;
				}
			}
		}
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.MovesetAct#getType()
	 */
	@Override
	public MoveType getType() {
		return MoveType.ATTACK;
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.MovesetAct#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		for (Direction dir : Direction.values()) {
			//this.
		}
		
	}

	/**
	 * Applies any special stuff associated with this attack. Called when this
	 * attack lands automatically by the victim itself. Override to use.
	 * @param 	victim			The character getting hit with this attack
	 */
	public void applySpecialEffects(CharacterEvent victim) {
		// default does nothing
	}

}
