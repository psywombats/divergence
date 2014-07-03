/**
 *  Context.java
 *  Created on Nov 23, 2012 4:33:37 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import net.wombatrpgs.mgne.core.AssetQueuer;
import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.Updateable;
import net.wombatrpgs.mgne.graphics.Effect;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.graphics.interfaces.Renderable;
import net.wombatrpgs.mgne.io.ButtonListener;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.io.CommandMap;
import net.wombatrpgs.mgne.io.InputEvent;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 * A screen is the environment in which the game is now running. It's
 * essentially part of a massive state machine that defines the game, and saves
 * some notes about its parameters, such as the screen to display and how to
 * handle keys. Screens can be layered one on top of the other. In this case,
 * the screens with lower z values are rendered first, and the screens behind
 * are only rendered if the screen on top of them is counted as transparent.
 * More info in the ScreenStack class.
 */
public abstract class Screen extends AssetQueuer implements CommandListener,
															Updateable,
															Renderable,
															Disposable,
															ButtonListener {
	
	protected TrackerCam cam;
	protected transient OrthographicCamera uiCam;
	
	/** Graphics shit, reloaded new each time */
	protected transient SpriteBatch viewBatch;
	protected transient SpriteBatch finalBatch;
	protected transient SpriteBatch uiBatch;
	protected transient FrameBuffer buffer, lastBuffer;
	protected transient ShapeRenderer shapes;
	
	protected transient ShaderProgram mapShader;
	protected Color tint;
	
	protected List<Updateable> updateChildren, removeChildren, addChildren;
	protected List<ScreenObject> screenObjects;
	protected List<Effect> effects, removeEffects;
	protected Stack<CommandListener> commandListeners;
	protected Stack<CommandMap> commandContext;

	
	/**
	 * Creates a new game screen. Remember to call intialize when done setting
	 * things up yourself. Fair warning: if you don't give us a camera, we'll
	 * create one for ourselves, potentially reformatting the game window.
	 */
	public Screen() {
		commandContext = new Stack<CommandMap>();
		commandListeners = new Stack<CommandListener>();
		updateChildren = new ArrayList<Updateable>();
		removeChildren = new ArrayList<Updateable>();
		addChildren = new ArrayList<Updateable>();
		screenObjects = new ArrayList<ScreenObject>();
		removeEffects = new ArrayList<Effect>();
		effects = new ArrayList<Effect>();
		
		tint = new Color(1, 1, 1, 1);
		cam = new TrackerCam(MGlobal.window.getWidth(), MGlobal.window.getHeight());
		
		updateChildren.add(cam);
	}
	
	/**
	 * Called whenever this screen stops being the top screen on the stack. The
	 * screen will stop receiving player commands. Default does nothing.
	 */
	public void onFocusLost() {
		// nothing
	}
	
	/**
	 * Called whenever this screen starts being the top screen on the stack. The
	 * screen will start receiving player commands. Default does nothing.
	 */
	public void onFocusGained() {
		// nothing
	}
	
	/** @return The camera this screen uses to render */
	public TrackerCam getCamera() { return cam; }
	
	/** @return Batch used for rendering contents of the viewport */
	public SpriteBatch getViewBatch() { return viewBatch; }
	
	/** @return Batch used for UI components */
	public SpriteBatch getUIBatch() { return uiBatch; }
	
	/** @return Game screen whole tint */
	public Color getTint() { return tint; }
	
	/** @return The shader used to render maps */
	public ShaderProgram getMapShader() { return mapShader; }
	
	/** @param listener The listener to receive command updates */
	public void pushCommandListener(CommandListener listener) { commandListeners.push(listener); }
	
	/** Pops the latest listener from the command listener stack */
	public void popCommandListener() { commandListeners.pop(); }
	
	/** @param listener The listener to stop receiving command updates */
	public void removeCommandListener(CommandListener listener) { commandListeners.remove(listener); }
	
	/** @return The command listener currently receiving first commands */
	public CommandListener getTopCommandListener() {
		if (commandListeners.size() == 0) return null;
		else return commandListeners.peek();
	}
	
	/** @param u The updateable child to add */
	public void addUChild(Updateable u) { addChildren.add(u); }
	
	/** @param u The updateable child to (eventually) remove */
	public void removeUChild(Updateable u) { removeChildren.add(u); }
	
	/** @return The width (in px) of current frames */
	public int getWidth() { return MGlobal.window.getViewportWidth(); }
	
	/** @return The height (in px) of current frames */
	public int getHeight() { return MGlobal.window.getViewportHeight(); }
	
	/** @param effect The new effect to add to this screen */
	public void addEffect(Effect effect) { effects.add(effect); }
	
	/** @param effect The effect to remove from this screen */
	public void removeEffect(Effect effect) { removeEffects.add(effect); }
	
	/**
	 * Checks to see if a screen object exists on the screen. Also checks if the
	 * object is queued to be added next update step.
	 * @param	object			The object to check if exists
	 * @return					True if that object is here, false otherwise
	 */
	public boolean containsChild(ScreenObject object) {
		return screenObjects.contains(object) || addChildren.contains(object);
	}
	
	/**
	 * Removes an explicit command map from the stack.
	 * @param	map				The map to remove
	 */
	public void removeCommandContext(CommandMap map) {
		if (map == getTopCommandContext()) {
			popCommandContext();
		} else {
			commandContext.remove(map);
		}
	}
	
	/**
	 * Sets the command parser used on the screen. Usually only used by engine.
	 * @param 	map				The command map to use instead
	 */
	public void pushCommandContext(CommandMap map) {
		commandContext.push(map);
	}
	
	/**
	 * Removes the last active command context.
	 */
	public void popCommandContext() {
		commandContext.pop();
	}

	/**
	 * @see net.wombatrpgs.mgne.io.ButtonListener#onEvent
	 * (net.wombatrpgs.mgne.io.InputEvent)
	 */
	@Override
	public void onEvent(InputEvent event) {
		CommandMap map = getTopCommandContext();
		if (map == null) return;
		InputCommand cmd = map.parse(event);
		if (cmd == null) {
			// we have no use for this command
			return;
		}
		onCommand(cmd);
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Renderable#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		for (ScreenObject object : screenObjects) {
			object.render(batch);
		}
	}

	/**
	 * The root of all evil. We supply the batch.
	 */
	public final void render() {
		cam.update(0);
		viewBatch.setProjectionMatrix(cam.combined);
		WindowSettings window = MGlobal.window;
		buffer.begin();
		clear();
		shapes.end();
		buffer.end();
		
		// Draw the normal screen buffer into the last-buffer
		finalBatch.begin();
		lastBuffer.begin();
		finalBatch.draw(
				buffer.getColorBufferTexture(),			// texture
				0, 0,									// x/y in screen space
				0, 0,									// origin x/y screen
				window.getWidth(), window.getHeight(),	// width/height screen
				1, 1,									// scale x/y
				0,										// rotation in degrees
				0, 0,									// x/y in texel space
				window.getWidth(), window.getHeight(),	// width/height texel
				false, true								// flip horiz/vert
			);
		finalBatch.end();
		lastBuffer.end();
		
		buffer.begin();
		render(getUIBatch());
		buffer.end();
		
		// apply the screen effects
		for (Effect effect : effects) {
			effect.apply(buffer);
		}
		
		// now draw the results to the screen
		finalBatch.setColor(tint);
		finalBatch.begin();
		// oh god I'm so sorry
		finalBatch.draw(
				buffer.getColorBufferTexture(),			// texture
				0, 0,									// x/y in screen space
				0, 0,									// origin x/y screen
				window.getWidth(), window.getHeight(),	// width/height screen
				1, 1,									// scale x/y
				0,										// rotation in degrees
				0, 0,									// x/y in texel space
				window.getWidth(), window.getHeight(),	// width/height texel
				false, true								// flip horiz/vert
			);
		finalBatch.end();
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Renderable#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		if (pass == 0) {
			
			viewBatch = constructViewBatch();
			uiBatch = constructUIBatch();
			finalBatch = constructFinalBatch();
			
			buffer = new FrameBuffer(Format.RGB565, 
					MGlobal.window.getWidth(),
					MGlobal.window.getHeight(),
					false);
			lastBuffer = new FrameBuffer(Format.RGB565, 
					MGlobal.window.getWidth(),
					MGlobal.window.getHeight(),
					false);
			
			uiCam = new OrthographicCamera();
			uiCam.zoom = MGlobal.window.getZoom();
			uiCam.position.x = MGlobal.window.getWidth() / 2;
			uiCam.position.y = MGlobal.window.getHeight() / 2;
			uiCam.setToOrtho(false, MGlobal.window.getWidth(), MGlobal.window.getHeight());
			uiCam.update();
			uiBatch.setProjectionMatrix(uiCam.combined);
			shapes = new ShapeRenderer();
			mapShader = constructMapShader();
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.core.interfaces.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		for (Effect effect : effects) {
			effect.update(elapsed);
		}
		for (Effect effect : removeEffects) {
			effects.remove(effect);
		}
		for (Updateable up : updateChildren) {
			up.update(elapsed);
		}
		for (Updateable up : addChildren) {
			updateChildren.add(up);
		}
		addChildren.clear();
		for (Updateable up : removeChildren) {
			updateChildren.remove(up);
		}
		removeChildren.clear();
	}

	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		int listeners = commandListeners.size();
		for (int i = 0; i < listeners; i += 1) {
			CommandListener listener = commandListeners.get(listeners - i - 1);
			if (listener.onCommand(command)) return true;
		}
		switch (command) {
		
		case GLOBAL_FULLSCREEN:
			Gdx.graphics.setDisplayMode(
					MGlobal.window.getWidth(), 
					MGlobal.window.getHeight(), 
					!Gdx.graphics.isFullscreen());
			return true;
		default:
			return false;
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		if (buffer != null) buffer.dispose();
		if (lastBuffer != null) lastBuffer.dispose();
	}

	/**
	 * Adds a screen-showable picture to the screen.
	 * @param 	screenObject	The object to add
	 */
	public void addChild(ScreenObject screenObject) {
		screenObjects.add(screenObject);
		addChildren.add(screenObject);
		screenObject.onAddedToScreen(this);
	}
	
	/**
	 * Removes a screen-showable picture from the screen.
	 * @param	screenObject	The object to add
	 */
	public void removeChild(ScreenObject screenObject) {
		if (screenObjects.contains(screenObject)) {
			screenObjects.remove(screenObject);
			removeUChild(screenObject);
			screenObject.onRemovedFromScreen(this);
		} else {
			MGlobal.reporter.warn("Tried to remove non-existant object from "
					+ "screen: " + screenObject);
		}
	}
	
	/**
	 * Gets the command parser used on this screen. Usually only used by engine.
	 * @return					The command parser used on this screen
	 */
	protected CommandMap getTopCommandContext() {
		if (commandContext.size() == 0) {
			// ugly
			return null;
		} else {
			return commandContext.peek();
		}
	}
	
	/**
	 * Screen-wiping procedure.
	 */
	protected void clear() {
		WindowSettings window = MGlobal.window;
		Gdx.gl.glClearColor(15.f/255.f, 9.f/255.f, 7.f/255.f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shapes.setColor(15.f/255.f, 9.f/255.f, 7.f/255.f, 1);
		shapes.begin(ShapeType.Filled);
		shapes.rect(0, 0, window.getWidth(), window.getHeight());
	}
	
	/**
	 * Construct the batch used to draw from internal frame buffer to the
	 * screen. Placed here for easy override.
	 * @return					The final render phase batch
	 */
	protected SpriteBatch constructFinalBatch() {
		return MGlobal.graphics.constructFinalBatch();
	}
	
	/**
	 * Construct the batch used to draw UI elements that don't move with the
	 * screen. Placed here for easy override.
	 * @return					The ui render phase batch
	 */
	protected SpriteBatch constructUIBatch() {
		return MGlobal.graphics.constructBatch();
	}
	
	/**
	 * Construct the batch used to draw events that appear relative to the hero
	 * as they move on the screen. Placed here for easy override.
	 * @return					The view render phase batch
	 */
	protected SpriteBatch constructViewBatch() {
		return MGlobal.graphics.constructBatch();
	}
	
	/**
	 * Construct the batch used to draw the grid layers of maps, probably only
	 * loaded ones. Placed here for easy override. May return null for use
	 * default shader.
	 * @return					The map grid shader, or null for default
	 */
	protected ShaderProgram constructMapShader() {
		return null;
	}

}
