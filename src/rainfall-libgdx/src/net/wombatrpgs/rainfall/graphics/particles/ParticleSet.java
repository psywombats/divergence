/**
 *  ParticleSet.java
 *  Created on Jan 31, 2013 10:26:21 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.graphics.particles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.Queueable;

/**
 * Something that provides particles for an emitter. Must be subclassed. Right
 * now its only subclass is gibs.
 */
public abstract class ParticleSet implements Queueable {
	
	protected String fileName;
	protected TextureRegion[] particleSources;
	protected int frameCount;
	protected int frameWidth, frameHeight;
	
	/**
	 * Creates a new particle set and binds it to a file with texture data to
	 * create particles from. This way the super class can handle asset queuing.
	 * The file name should be a combination of a directory and the MDO file
	 * name.
	 * @param 	fileName			The full path to the file with particles
	 * @param	frameCount			How many frames are contained in that tex
	 */
	public ParticleSet(String fileName, int frameCount, int frameWidth, int frameHeight) {
		this.fileName = fileName;
		this.frameCount = frameCount;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
	}
	
	/**
	 * Generate a new particle appropriate for the set
	 * @param	source			The thing that spat/spits the particle
	 * @return					A particle as designated by this set
	 */
	public abstract Particle generateParticle(Emitter source);

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		RGlobal.assetManager.load(fileName, Texture.class);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		if (RGlobal.assetManager.isLoaded(fileName)) {
			Texture spritesheet = RGlobal.assetManager.get(fileName, Texture.class);
			particleSources = new TextureRegion[frameCount];
			for (int i = 0; i < frameCount; i++) {
				particleSources[i] = new TextureRegion(spritesheet,
						frameWidth * i, 0,
						frameWidth, frameHeight);
			}
		} else {
			RGlobal.reporter.warn("Particlesheet not loaded: " + fileName);
		}
	}

}
