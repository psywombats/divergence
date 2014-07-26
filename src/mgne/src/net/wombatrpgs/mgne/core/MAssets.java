/**
 *  AssetLoader.java
 *  Created on Mar 26, 2014 7:24:53 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.wombatrpgs.mgne.core.data.DataEntry;
import net.wombatrpgs.mgne.core.interfaces.Queueable;
import net.wombatrpgs.mgne.io.loaders.DataLoader;
import net.wombatrpgs.mgne.io.loaders.LuaLoader;
import net.wombatrpgs.mgne.io.loaders.SceneLoader;
import net.wombatrpgs.mgne.scenes.LineData;

import org.luaj.vm2.LuaValue;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Wrapper for AssetManager that encapsulates the loading previously performed
 * in MGlobal. Moved for easier memory save/load management. Should've been
 * done this way in the first place.
 */
public class MAssets extends AssetManager {
	
	protected boolean loadingRequested;
	
	/**
	 * Creates a new asset loader with no assets in memory and nothing to be
	 * loaded. Mirrors empty AssetManager superconstructor. Also sets custom
	 * file handlers.
	 */
	public MAssets() {
		super();
		setLoader(LineData.class, new SceneLoader(new InternalFileHandleResolver()));
		setLoader(DataEntry.class, new DataLoader(new InternalFileHandleResolver()));
		setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		setLoader(LuaValue.class, new LuaLoader(new InternalFileHandleResolver()));
		
		loadingRequested = false;
	}
	
	/**
	 * Ensures that at least one more pass of loading/processing will occur. Use
	 * this for objects that require more than one pass to guarantee that even
	 * if those objects required no new assets, they will get processed.
	 */
	public void requestLoading() {
		loadingRequested = true;
	}
	
	/**
	 * Queues, loads queued assets. Blocks.
	 * @param	toLoad			All assets to load
	 * @param	name			The name of what is being loadded
	 */
	public void loadAssets(Collection<? extends Queueable> toLoad, String name) {
		boolean previousRequest = loadingRequested;
		loadingRequested = false;
		for (Queueable q : toLoad) q.queueRequiredAssets(this);
		int pass;
		for (pass = 0; needsLoading(); pass++) {
			float assetStart = System.currentTimeMillis();
			finishLoading();
			float assetEnd = System.currentTimeMillis();
			for (Queueable q : toLoad) q.postProcessing(this, pass);
			float assetElapsed = (assetEnd - assetStart) / 1000f;
			MGlobal.reporter.inform("Loading " + name + " p " + pass + ", " + assetElapsed + "s");
		}
		if (pass == 0) {
			for (Queueable q : toLoad) q.postProcessing(this, pass);
		}
		loadingRequested = previousRequest;
	}
	
	/**
	 * Loads a single asset. Blocks.
	 * @param	toLoad			The thing to load
	 * @param	name			The name of the thing
	 */
	public void loadAsset(Queueable toLoad, String name) {
		List<Queueable> dummy = new ArrayList<Queueable>();
		dummy.add(toLoad);
		loadAssets(dummy, name);
	}
	
	/**
	 * End condition for the loading loop.
	 * @return					True if loading/processing should continue.
	 */
	protected boolean needsLoading() {
		boolean needsLoading = getProgress() < 1 || loadingRequested;
		loadingRequested = false;
		return needsLoading;
	}

}
