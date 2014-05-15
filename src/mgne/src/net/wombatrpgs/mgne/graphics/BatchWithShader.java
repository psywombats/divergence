/**
 *  BatchWithShader.java
 *  Created on May 15, 2014 1:44:40 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Struct because the stupid libgdx batch doesn't include a getter for shader.
 */
public class BatchWithShader {
	
	protected ShaderProgram shader;
	protected SpriteBatch batch;
	
	/**
	 * Constructs a new tuple with a batch and shader.
	 * @param	batch			The batch part of the tuple
	 * @param	shader			The shader that that batch is using
	 */
	public BatchWithShader(SpriteBatch batch, ShaderProgram shader) {
		this.batch = batch;
		this.shader = shader;
	}
	
	/** @reutrn The batch part of the tuple */
	public SpriteBatch getBatch() { return batch; }
	
	/** @return The shader part of the tuple */
	public ShaderProgram getShader() { return shader; }

}
