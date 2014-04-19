/**
 *  AssetQueuer.java
 *  Created on Apr 15, 2014 3:24:46 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.interfaces.Queueable;

/**
 * Simple superclass for things that want cheap asset queueing tech.
 */
public abstract class AssetQueuer implements Queueable {
	
	protected List<Queueable> assets;
	
	/**
	 * Creates a new asset queuer with an empty asset list.
	 */
	public AssetQueuer() {
		assets = new ArrayList<Queueable>();
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#queueRequiredAssets
	 * (MAssets)
	 */
	@Override
	public void queueRequiredAssets(MAssets manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Queueable#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}

}
