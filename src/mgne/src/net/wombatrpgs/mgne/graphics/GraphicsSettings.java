/**
 *  GraphicsSettings.java
 *  Created on Sep 3, 2013 5:43:35 PM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.graphics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgneschema.settings.GraphicsSettingsMDO;
import net.wombatrpgs.mgneschema.settings.data.ShaderEnabledState;
import net.wombatrpgs.mgneschema.test.data.TestState;

/**
 * Settings for graphics? These probably shouldn't get changed much in-game,
 * unless it's from some options menu that's yet to be implemented. Also
 * contains some global helper methods, like shadow rendering.
 */
public class GraphicsSettings extends AssetQueuer implements Disposable {
	
	/** Default database key */
	protected static final String DEFAULT_KEY = "graphics_default";
	
	/** Max number of tiles an event can have in height, in tiles */
	public static final int MAX_EVENT_HEIGHT = 4;
	
	protected GraphicsSettingsMDO mdo;
	
	protected List<SpriteBatch> batches;
	protected List<ShaderFromData> shaders;
	
	/**
	 * Creates a new graphics settings from data. Should only be called once per
	 * game lifetime.
	 * @param	mdo				The data to load settings from
	 */
	public GraphicsSettings(GraphicsSettingsMDO mdo) {
		this.mdo = mdo;
		batches = new ArrayList<SpriteBatch>();
		shaders = new ArrayList<ShaderFromData>();
	}
	
	/**
	 * Creates a new graphics settings from the default info in the database.
	 */
	public GraphicsSettings() {
		this(MGlobal.data.getEntryFor(DEFAULT_KEY, GraphicsSettingsMDO.class));
	}
	
	/** @return True if we should be running shader debug prints */
	public boolean isShaderDebugEnabled() { return mdo.shaderDebug == TestState.ENABLED; }
	
	/** @return True if the shaders are globally enabled */
	public boolean isShaderEnabled() { return mdo.enabled == ShaderEnabledState.ENABLED; }
	
	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		for (ShaderFromData shader : shaders) {
			shader.dispose();
		}
		for (SpriteBatch batch : batches) {
			batch.dispose();
		}
	}

	/**
	 * Constructs a batch with the given shader if shaders are enabled. Handles
	 * disposing of the shader.
	 * @param	shaderKey		The key to the data file of the shader
	 * @return					A batch that can be used with that shader
	 */
	public SpriteBatch constructBatch(String shaderKey) {
		SpriteBatch batch = constructBatch();
		if (isShaderEnabled() && shaderKey != null) {
			ShaderFromData shader = constructShader(shaderKey);
			batch.setShader(shader);
		}
		return batch;
	}
	
	/**
	 * Creates a new managed shader batch. Will handle disposing the shader.
	 * @return					A new blank shader
	 */
	public SpriteBatch constructBatch() {
		SpriteBatch batch = new SpriteBatch();
		batches.add(batch);
		return batch;
	}
	
	/**
	 * Creates the batch used to render from internal frame buffer to the
	 * screen, potentially using the global shader.
	 * @return					A new final pass batch
	 */
	public SpriteBatch constructFinalBatch() {
		return constructBatch(mdo.globalShader);
	}
	
	/**
	 * Creates the global shader used by all screens in final render pass.
	 * @return					A new final pass shader
	 */
	public ShaderFromData constructGlobalShader() {
		return constructShader(mdo.globalShader);
	}
	
	/**
	 * Creates a shader from a database key, adds it to the list of shaders,
	 * and returns it. Returns null if shaders are disabled.
	 * @param	shaderKey		The key to the data file of the shader
	 * @return					The constructed shader, or null if no shaders
	 */
	protected ShaderFromData constructShader(String shaderKey) {
		if (!isShaderEnabled() || shaderKey == null) {
			return null;
		}
		ShaderFromData shader = new ShaderFromData(shaderKey);
		shaders.add(shader);
		return shader;
	}

}
