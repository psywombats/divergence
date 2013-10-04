/**
 *  Emitter.java
 *  Created on Jan 31, 2013 10:24:56 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.graphics.particles;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.events.MapEvent;
import net.wombatrpgs.mrogueschema.graphics.EmitterMDO;

/**
 * A simple particle emitter. Generate one from MDO, then fill it with something
 * that meets the definition of a particle set.
 */
public class Emitter extends MapEvent {
	
	protected EmitterMDO mdo;
	protected ParticleSet set;
	protected List<Particle> particles;
	
	/**
	 * Creates a new emitter from data and a generator for its particles.
	 * @param 	mdo				The data to create the emitter from
	 * @param 	set				The pre-created set of particles
	 */
	public Emitter(EmitterMDO mdo, ParticleSet set) {
		this.mdo = mdo;
		this.particles = new ArrayList<Particle>();
		this.set = set;
		assets.add(set);
	}
	
	/**
	 * Starts the emitter firing in a certain direction. The parameters don't
	 * mean the particles fly directly at those angles, but that they are
	 * biased towards that angle. Random velocities are generated and then
	 * multiplied by something resembled these numbers depending on their aim.
	 * @param 	xComp			The normalized x-dir to fire in (-1 to 1)
	 * @param 	yComp			The normalized y-dir to fire in (-1 to 1)
	 */
	public void fire(float xComp, float yComp) {
		// TODO: support sustained fire mode
		for (int i = 0; i < mdo.count; i++) {
			fireSingle(xComp, yComp);
		}
	}
	
	/**
	 * Fires a single particle biased towards a certain direction.
	 * @param xComp				The normalized x-dir to fire in (-1 to 1)
	 * @param yComp				The normalized y-dir to fire in (-1 to 1)
	 */
	protected void fireSingle(float xComp, float yComp) {
		Particle part = set.generateParticle(this);
		int angle = MGlobal.rand.nextInt(360);
		float vx = (float) (mdo.velocity * Math.cos(angle));
		float vy = (float) (mdo.velocity * Math.sin(angle));
		vx += xComp * mdo.velocity;
		vy += yComp * mdo.velocity;
		vx *= (MGlobal.rand.nextFloat()/2 + .5f);
		vy *= (MGlobal.rand.nextFloat()/2 + .5f);
		part.setVelocity(vx, vy);
		part.setAngularVelocity(mdo.rotationalVelocity * MGlobal.rand.nextFloat()*2-1);
		part.setLife(MGlobal.rand.nextFloat() * (mdo.maxLife - mdo.minLife) + mdo.minLife);
		particles.add(part);
		parent.addEventAbsolute(part, 
				getX() + MGlobal.rand.nextInt(32), 
				getY() + MGlobal.rand.nextInt(32));
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
//		for (Particle part : particles) {
//			part.render(camera);
//		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.maps.events.MapEvent#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		List<Particle> deadParts = new ArrayList<Particle>();
		for (Particle part : particles) {
			//part.update(elapsed);
			if (part.getLife() <= 0) {
				deadParts.add(part);
			}
		}
		for (Particle part : deadParts) {
			particles.remove(part);
			parent.removeEvent(part);
		}
	}

}
