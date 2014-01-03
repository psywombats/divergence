/**
 *  Particle.java
 *  Created on Jan 31, 2013 10:27:08 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics.particles;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogueschema.graphics.EmitterMDO;
import net.wombatrpgs.mrogueschema.graphics.data.BounceType;
import net.wombatrpgs.mrogueschema.graphics.data.ReflectionType;

/**
 * A particle of something or other that's going to be spat across the scene.
 * These are usually generate automatically by particle sets.
 */
public class Particle extends MapEvent {
	
	protected static final float BOUNCE_SPEED = 192; // in px/s
	protected static final float BOUNCE_DECAY = 768; // in px/s/s
	
	protected Emitter source;
	protected TextureRegion appearance;
	protected BounceType bounce;
	protected ReflectionType reflect;
	protected float angle; // in degrees?
	protected float angularVelocity; // in deg/s
	protected float life; // in s
	protected float bounceY; // in px
	protected float bounceVelocity;
	
	/**
	 * Creates a new particle based on a certain appearance. The MDO shouldn't
	 * be used for speed assignments because the parent emitter is in charge
	 * of assigning them. Source is only here to read booleans.
	 * @param	mdo				The emitter's data, to copy some fields from
	 * @param 	source			The thing launching particles
	 * @param 	appearance		The appearance of the particle
	 */
	public Particle(EmitterMDO mdo, Emitter source, TextureRegion appearance) {
		this.source = source;
		this.appearance = appearance;
		this.parent = source.getParent();
		this.angle = MGlobal.rand.nextInt(360);
		this.bounce = mdo.bounce;
		this.reflect = mdo.reflect;
		this.bounceY = 0;
		this.bounceVelocity = BOUNCE_SPEED;
	}
	
	/** @param angularVelocity The new angular velocity in deg/s */
	public void setAngularVelocity(float angularVelocity) { this.angularVelocity = angularVelocity; }
	
	/** @param life The new life val in s */
	public void setLife(float life) { this.life = life; }
	
	/** @return The time remaining before this particle is destroyed */
	public float getLife() { return this.life; }

	/**
	 * We don't need to queue our assets because the emitter should be loading
	 * up our spritesheet for all particles.
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		renderLocal(camera, appearance, 0, (int) bounceY, (int) angle);
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		angle += angularVelocity * elapsed;
		life -= elapsed; // the parent will take care of killing us
		if (bounce == BounceType.BOUNCE_ENABLED) {
			bounceY += bounceVelocity * elapsed;
			bounceVelocity -= BOUNCE_DECAY * elapsed;
			if (bounceY < 0) {
				bounceVelocity = Math.min(-bounceVelocity, BOUNCE_SPEED);
			}
		}
	}
	
}
