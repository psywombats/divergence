/**
 *  SoundObject.java
 *  Created on Mar 24, 2013 1:42:28 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.io.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import net.wombatrpgs.rainfall.maps.MapThing;
import net.wombatrpgs.rainfallschema.audio.SoundMDO;
import net.wombatrpgs.rainfallschema.audio.data.RepeatType;

/**
 * A sound effect map object.
 */
public class SoundObject extends AudioObject {
	
	protected SoundMDO mdo;
	protected Sound coreSound;

	/**
	 * Constructs a sound object for another object from data.
	 * @param	mdo				The data to create from
	 * @param 	parent			The parent to make for
	 */
	public SoundObject(SoundMDO mdo, MapThing parent) {
		super(mdo, parent);
		this.mdo = mdo;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.graphics.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		coreSound.dispose();
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.audio.AudioObject#corePlay()
	 */
	@Override
	protected void corePlay() {
		long id = coreSound.play((float) mdo.volume / 100.f);
		coreSound.setLooping(id, mdo.repeats == RepeatType.LOOP);
	}

	/**
	 * Note this stops all playing instances.
	 * @see net.wombatrpgs.rainfall.io.audio.AudioObject#coreStop()
	 */
	@Override
	protected void coreStop() {
		coreSound.stop();
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.audio.AudioObject#getLoaderClass()
	 */
	@Override
	protected Class<?> getLoaderClass() {
		return Sound.class;
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.audio.AudioObject#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	protected void postProcessing(AssetManager manager) {
		coreSound = (Sound) manager.get(filename, getLoaderClass());
	}

}
