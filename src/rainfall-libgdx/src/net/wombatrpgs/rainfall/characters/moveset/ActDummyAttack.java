/**
 *  ActDummyAttack.java
 *  Created on Jun 16, 2013 12:20:50 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.characters.moveset;

import net.wombatrpgs.rainfall.characters.CharacterEvent;
import net.wombatrpgs.rainfall.maps.Level;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.maps.objects.TimerListener;
import net.wombatrpgs.rainfall.maps.objects.TimerObject;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.data.MoveMDO;

/**
 * hurf durf kill kill!!
 * I don't think this actually needs to do anything but test attack code. code.
 */
public class ActDummyAttack extends MovesetAct {
	
	protected boolean hitSomething;

	public ActDummyAttack(CharacterEvent actor, MoveMDO mdo) {
		super(actor, mdo);
		this.hitSomething = false;
	}

	/**
	 * @see net.wombatrpgs.rainfall.characters.moveset.MovesetAct#coreAct
	 * (net.wombatrpgs.rainfall.maps.Level, net.wombatrpgs.rainfall.characters.CharacterEvent)
	 */
	@Override
	public void coreAct(Level map, final CharacterEvent actor) {
		actor.startAction(this);
		
		// TODO: generalize
		hitSomething = false;
		float duration = this.idleAppearance.getDuration();
		final MovesetAct parentMove = this;
		new TimerObject(duration, actor, new TimerListener() {
			@Override public void onTimerZero(TimerObject source) {
				actor.stopAction(parentMove);
			}
		});
	}

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

}
