/**
 *  Context.java
 *  Created on Nov 23, 2012 4:33:37 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.core.Queueable;
import net.wombatrpgs.mrogue.core.Updateable;
import net.wombatrpgs.mrogue.graphics.Disposable;
import net.wombatrpgs.mrogue.graphics.PostRenderable;
import net.wombatrpgs.mrogue.io.ButtonListener;
import net.wombatrpgs.mrogue.io.CommandListener;
import net.wombatrpgs.mrogue.io.CommandMap;
import net.wombatrpgs.mrogue.ui.BinaryChoice;
import net.wombatrpgs.mrogue.ui.BinaryChoiceResultListener;
import net.wombatrpgs.mrogueschema.io.data.InputButton;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;

/**
 * A screen is the environment in which the game is now running. It's
 * essentially part of a massive state machine that defines the game, and saves
 * some notes about its parameters, such as the screen to display and how to
 * handle keys. Screens can be layered one on top of the other. In this case,
 * the screens with lower z values are rendered first, and the screens behind
 * are only rendered if the screen on top of them is counted as transparent.
 * More info in the ScreenStack class.
 */
public abstract class Screen implements CommandListener,
										Updateable,
										Queueable,
										Disposable,
										ButtonListener {
	
	protected TrackerCam cam;
	protected OrthographicCamera uiCam;
	protected SpriteBatch batch;
	protected SpriteBatch privateBatch;
	protected SpriteBatch uiBatch;
	protected ShaderProgram mapShader;
	protected FrameBuffer buffer, lastBuffer;
	protected Color tint;
	protected ShapeRenderer shapes;
	protected BitmapFont defaultFont;
	
	protected List<Queueable> assets;
	protected List<PostRenderable> postRenders;
	protected List<CommandListener> commandListeners;
	protected List<Updateable> updateChildren, removeChildren, addChildren;
	protected List<ScreenObject> screenObjects;
	protected Stack<CommandMap> commandContext;
	
	protected boolean initialized;

	
	/**
	 * Creates a new game screen. Remember to call intialize when done setting
	 * things up yourself. Fair warning: if you don't give us a camera, we'll
	 * create one for ourselves, potentially reformatting the game window.
	 */
	public Screen() {
		assets = new ArrayList<Queueable>();
		commandContext = new Stack<CommandMap>();
		postRenders = new ArrayList<PostRenderable>();
		commandListeners = new ArrayList<CommandListener>();
		updateChildren = new ArrayList<Updateable>();
		removeChildren = new ArrayList<Updateable>();
		addChildren = new ArrayList<Updateable>();
		screenObjects = new ArrayList<ScreenObject>();
		
		initialized = false;
		mapShader = null;
		batch = new SpriteBatch();
		privateBatch = new SpriteBatch();
		uiBatch = new SpriteBatch();
		buffer = new FrameBuffer(Format.RGB565, 
				MGlobal.window.getWidth(),
				MGlobal.window.getHeight(),
				false);
		lastBuffer = new FrameBuffer(Format.RGB565, 
				MGlobal.window.getWidth(),
				MGlobal.window.getHeight(),
				false);
		tint = new Color(0, 0, 0, 1);
		shapes = new ShapeRenderer();
		defaultFont = new BitmapFont();
		cam = new TrackerCam(MGlobal.window.getWidth(), MGlobal.window.getHeight());
		
		uiCam = new OrthographicCamera();
		uiCam.setToOrtho(false, MGlobal.window.getWidth(), MGlobal.window.getHeight());
		uiCam.zoom = MGlobal.window.getZoom();
		uiCam.position.x = MGlobal.window.getViewportWidth() / 2;
		uiCam.position.y = MGlobal.window.getViewportHeight() / 2;
		uiCam.update();
		uiBatch.setProjectionMatrix(uiCam.combined);
		
		updateChildren.add(cam);
		updateChildren.add(MGlobal.keymap);
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
	public SpriteBatch getViewBatch() { return batch; }
	
	/** @return Batch used for UI components */
	public SpriteBatch getUIBatch() { return uiBatch; }
	
	/** @return Buffer used for rendering contents */
	public FrameBuffer getBuffer() { return buffer; }
	
	/** @return Buffer used to accumulate frame data */
	public FrameBuffer getLastBuffer() { return lastBuffer; }
	
	/** @return Game screen whole tint */
	public Color getTint() { return tint; }
	
	/** @return The shader used to render maps */
	public ShaderProgram getMapShader() { return mapShader; }
	
	/** @param pr The new post renderable to render */
	public void registerPostRender(PostRenderable pr) { postRenders.add(pr); }
	
	/** @param pr The post render to no longer render */
	public void removePostRender(PostRenderable pr) { postRenders.remove(pr); }
	
	/** @param listener The listener to receive command updates */
	public void registerCommandListener(CommandListener listener) { commandListeners.add(listener); }
	
	/** @param listener The listener to stop receiving command updates */
	public void unregisterCommandListener(CommandListener listener) { commandListeners.remove(listener); }
	
	/** @param u The updateable child to add */
	public void addUChild(Updateable u) { addChildren.add(u); }
	
	/** @param u The updateable child to (eventually) remove */
	public void removeUChild(Updateable u) { removeChildren.add(u); }
	
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
	 * @see net.wombatrpgs.mrogue.io.ButtonListener#onButtonPressed
	 * (net.wombatrpgs.mrogueschema.io.data.InputButton)
	 */
	@Override
	public void onButtonPressed(InputButton button) {
		InputCommand cmd = getTopCommandContext().get(button, false);
		if (cmd == null) {
			// we have no use for this key
		} else {
			onCommand(cmd);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.io.ButtonListener#onButtonReleased
	 * (net.wombatrpgs.mrogueschema.io.data.InputButton)
	 */
	@Override
	public void onButtonReleased(InputButton button) {
		InputCommand cmd = getTopCommandContext().get(button, true);
		if (cmd == null) {
			// we have no use for this key
		} else {
			onCommand(cmd);
		}
	}

	/**
	 * The root of all evil. We supply the camera.
	 */
	public void render() {
		if (!initialized) {
			MGlobal.reporter.warn("Forgot to intialize screen " + this);
		}
		cam.update(0);
		batch.setProjectionMatrix(cam.combined);
		WindowSettings window = MGlobal.window;
		buffer.begin();
		Gdx.gl.glClearColor(15.f/255.f, 9.f/255.f, 7.f/255.f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shapes.setColor(15.f/255.f, 9.f/255.f, 7.f/255.f, 1);
		shapes.begin(ShapeType.Filled);
		shapes.rect(0, 0, window.getWidth(), window.getHeight());
		shapes.end();
		for (ScreenObject pic : screenObjects) {
			if (pic.getZ() == 0) {
				pic.render(cam);
			}
		}
		buffer.end();
		
		// Draw the normal screen buffer into the last-buffer
		privateBatch.begin();
		lastBuffer.begin();
		privateBatch.draw(
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
		privateBatch.end();
		lastBuffer.end();
		
		buffer.begin();
		for (PostRenderable pr : postRenders) {
			pr.renderPost();
		}
		for (ScreenObject pic : screenObjects) {
			if (pic.getZ() > 0) {
				pic.render(cam);
			}
		}
		buffer.end();
		
		// now draw the results to the screen
		privateBatch.setColor(tint);
		privateBatch.begin();
		// oh god I'm so sorry
		privateBatch.draw(
				buffer.getColorBufferTexture(),			// texture
				0, 0,									// x/y in screen space
				0, 0,									// origin x/y screen
				window.getResolutionWidth(), window.getResolutionHeight(),	// width/height screen
				1, 1,									// scale x/y
				0,										// rotation in degrees
				0, 0,									// x/y in texel space
				window.getWidth(), window.getHeight(),	// width/height texel
				false, true								// flip horiz/vert
			);
		privateBatch.end();
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		for (Queueable asset : assets) {
			asset.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		for (Queueable asset : assets) {
			asset.postProcessing(manager, pass);
		}
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		Collections.sort(screenObjects);
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
	 * @see net.wombatrpgs.mrogue.io.CommandListener#onCommand
	 * (net.wombatrpgs.mrogueschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (MGlobal.stasis) return true;
		for (CommandListener listener : commandListeners) {
			if (listener.onCommand(command)) return true;
		}
		switch (command) {
		case INTENT_QUIT:
			MGlobal.ui.getPrompt().ask(new BinaryChoiceResultListener() {
				@Override public void onDecision(BinaryChoice result) {
					if (result == BinaryChoice.OPTION_2) {
						Gdx.app.exit();
					}
				}
			});
			return true;
		case INTENT_FULLSCREEN:
			Gdx.graphics.setDisplayMode(
					MGlobal.window.getResolutionWidth(), 
					MGlobal.window.getResolutionHeight(), 
					!Gdx.graphics.isFullscreen());
			return true;
		default:
			return false;
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.graphics.Disposable#dispose()
	 */
	@Override
	public void dispose() {
		batch.dispose();
		privateBatch.dispose();
		buffer.dispose();
		lastBuffer.dispose();
		uiBatch.dispose();
	}

	/**
	 * Adds a screen-showable picture to the screen.
	 * @param 	screenObject	The object to add
	 */
	public void addObject(ScreenObject screenObject) {
		screenObjects.add(screenObject);
		addChildren.add(screenObject);
		screenObject.onAddedToScreen();
	}
	
	/**
	 * Removes a screen-showable picture from the screen.
	 * @param	screenObject	The object to add
	 */
	public void removeObject(ScreenObject screenObject) {
		if (screenObjects.contains(screenObject)) {
			screenObjects.remove(screenObject);
			removeUChild(screenObject);
			screenObject.onRemovedFromScreen();
		} else {
			MGlobal.reporter.warn("Tried to remove non-existant picture from screen: " + screenObject);
		}
	}

	/**
	 * Run some final safety checks and finish initialization. Call once during
	 * the constructor.
	 */
	public final void init() {
		this.queueRequiredAssets(MGlobal.assetManager);
		MGlobal.assetManager.finishLoading();
		this.postProcessing(MGlobal.assetManager, 0);
		initialized = true;
	}
	
	/**
	 * Gets the command parser used on this screen. Usually only used by engine.
	 * @return					The command parser used on this screen
	 */
	protected CommandMap getTopCommandContext() {
		return commandContext.peek();
	}

}
