/**
 *  Particle.java
 *  Created on Jan 31, 2013 10:27:08 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics.particles;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.maps.events.MapEvent;
import net.wombatrpgs.rainfall.physics.CollisionResult;
import net.wombatrpgs.rainfall.physics.Hitbox;
import net.wombatrpgs.rainfall.physics.RectHitbox;
import net.wombatrpgs.rainfallschema.graphics.EmitterMDO;
import net.wombatrpgs.rainfallschema.graphics.data.BounceType;
import net.wombatrpgs.rainfallschema.graphics.data.ReflectionType;

/**
 * A particle of something or other that's going to be spat across the scene.
 * These are usually generate automatically by particle sets.
 */
public class Particle extends MapEvent {
	
	protected static final float BOUNCE_SPEED = 192; // in px/s
	protected static final float BOUNCE_DECAY = 768; // in px/s/s
	
	protected Emitter source;
	protected TextureRegion appearance;
	protected Hitbox box;
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
		this.parent = source.getLevel();
		this.angle = RGlobal.rand.nextInt(360);
		this.bounce = mdo.bounce;
		this.reflect = mdo.reflect;
		this.bounceY = 0;
		this.bounceVelocity = BOUNCE_SPEED;
		this.box = new RectHitbox(this, 
				appearance.getRegionWidth()/4, appearance.getRegionHeight()*3/4,
				appearance.getRegionWidth()/4, appearance.getRegionHeight()*3/4);
	}
	
	/** @see net.wombatrpgs.rainfall.maps.events.MapEvent#isMobile() */
	@Override
	public boolean isMobile() { return true; }

	/** @see net.wombatrpgs.rainfall.maps.events.MapEvent#isCollisionEnabled() */
	@Override
	public boolean isCollisionEnabled() { return reflect != ReflectionType.IGNORE_WALLS; }
	
	/** @param angularVelocity The new angular velocity in deg/s */
	public void setAngularVelocity(float angularVelocity) { this.angularVelocity = angularVelocity; }
	
	/** @param life The new life val in s */
	public void setLife(float life) { this.life = life; }
	
	/** @return The time remaining before this particle is destroyed */
	public float getLife() { return this.life; }

	/**
	 * We don't need to queue our assets because the emitter should be loading
	 * up our spritesheet for all particles.
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		renderLocal(camera, appearance, 0, (int) bounceY, (int) angle);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		angle += angularVelocity * elapsed;
		life -= elapsed; // the parent will take care of killing us
		if (bounce == BounceType.BOUNCE_ENABLED && !isFalling()) {
			bounceY += bounceVelocity * elapsed;
			bounceVelocity -= BOUNCE_DECAY * elapsed;
			if (bounceY < 0) {
				bounceVelocity = Math.min(-bounceVelocity, BOUNCE_SPEED);
			}
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#getHitbox()
	 */
	@Override
	public Hitbox getHitbox() {
		// TODO: if this takes too long, it can be moved to be per-emitter
		return box;
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#resolveWallCollision
	 * (net.wombatrpgs.rainfall.physics.CollisionResult)
	 */
	@Override
	public void resolveWallCollision(CollisionResult result) {
		super.resolveWallCollision(result);
		if (reflect == ReflectionType.REFLECT_OFF_WALLS) {
			if (result.mtvX != 0) this.vx *= -1;
			if (result.mtvY != 0) this.vy *= -1;
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#supportsBlockLanding()
	 */
	@Override
	public boolean supportsBlockLanding() {
		return true;
	}
	
}
