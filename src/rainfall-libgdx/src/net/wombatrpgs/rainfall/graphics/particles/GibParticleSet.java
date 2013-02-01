/**
 *  GibParticleSet.java
 *  Created on Jan 31, 2013 12:57:44 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics.particles;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfallschema.graphics.GibsetMDO;

/**
 * Generates particles from gibs.
 */
public class GibParticleSet extends ParticleSet {
	
	protected GibsetMDO mdo;
	
	/**
	 * Creates a gibset from data. Remember to tell this thing to queue!
	 * @param 	mdo				The MDO to get data from
	 */
	public GibParticleSet(GibsetMDO mdo) {
		super(RGlobal.GIBS_DIR+mdo.file, mdo.count, mdo.frameWidth, mdo.frameHeight);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.particles.ParticleSet#generateParticle
	 * (net.wombatrpgs.rainfall.graphics.particles.Emitter)
	 */
	@Override
	public Particle generateParticle(Emitter source) {
		Particle part = new Particle(source.mdo, source,
				particleSources[RGlobal.rand.nextInt(mdo.count)]);
		return part;
	}

}
