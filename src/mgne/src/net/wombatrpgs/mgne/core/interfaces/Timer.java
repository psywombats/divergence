/**
 *  Timer.java
 *  Created on Jan 24, 2015 6:56:57 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core.interfaces;

import net.wombatrpgs.mgne.core.MGlobal;

/**
 * bacon todo
 */
public class Timer implements Updateable {
	
	protected float time;
	protected float totalElapsed;
	protected FinishListener listener;
	
	public Timer(float time, FinishListener listener) {
		this.listener = listener;
		this.time = time;
		MGlobal.screens.peek().addUChild(this);
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		totalElapsed += elapsed;
		if (totalElapsed >= time) {
			MGlobal.screens.peek().removeUChild(this);
			listener.onFinish();
		}
	}

}
