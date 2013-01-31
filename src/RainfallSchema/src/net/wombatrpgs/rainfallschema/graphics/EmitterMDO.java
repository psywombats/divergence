/**
 *  ParticleEmitterMDO.java
 *  Created on Jan 31, 2013 10:03:39 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.graphics;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.rainfallschema.graphics.data.EmitterType;

/**
 * Spit particles out your ass. Direction is determined at runtime.
 */
@Path("graphics/")
public class EmitterMDO extends MainSchema {
	
	@Desc("Mode - will this emitter fire once or keep firing?")
	public EmitterType mode;
	
	@Desc("Rate of fire- how many particles to fire (possibly per second)")
	public Integer count;
	
	@Desc("Velocity - maximum velocity of a particle (in px/s)")
	public Integer velocity;
	
	@Desc("Rotation velocity - maximum rotational velocity of a particle (in rotations/s)")
	public Integer rotationalVelocity;
	
	@Desc("Min lifetime - minimum time a particle persists, in seconds")
	public Float minLife;
	
	@Desc("Max lifetime - maximum time a particle persists, in seconds")
	public Float maxLife;

}
