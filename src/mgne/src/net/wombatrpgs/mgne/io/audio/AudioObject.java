/**
 *  AudioObject.java
 *  Created on Oct 12, 2014 12:54:04 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.audio;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;

/**
 * Abstract representation of all audio types, sampled and generated, effect and
 * background music.
 */
public abstract class AudioObject extends AssetQueuer implements	Disposable,
																	Updateable {
	
	/**
	 * Default does nothing
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		// noop
	}
	
	/**
	 * Default does nothing.
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		// noop
	}
	
	/**
	 * The real legit play thing that handles both playing the sound and
	 * managing the map that we're a part of.
	 */
	public final void play() {
		corePlay();
	}
	
	/**
	 * The counterpart to play. Handles stopping sound and mappy stuff.
	 */
	public final void stop() {
		coreStop();
	}
	
	/**
	 * Start playing the underlying sound.
	 */
	protected abstract void corePlay();
	
	/**
	 * Stop playing the underlying sound.
	 */
	protected abstract void coreStop();

}
