/**
 *  Context.java
 *  Created on Nov 23, 2012 4:33:37 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import net.wombatrpgs.rainfall.core.Queueable;
import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.core.Updateable;
import net.wombatrpgs.rainfall.graphics.TrackerCam;
import net.wombatrpgs.rainfall.io.CommandListener;
import net.wombatrpgs.rainfall.io.CommandMap;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;

/**
 * A screen is the environment in which the game is now running. It's
 * essentially part of a massive state machine that defines the game, and saves
 * some notes about its parameters, such as the screen to display and how to
 * handle keys. Screens can be layered one on top of the other. In this case,
 * the screens with lower z values are rendered first, and the screens behind
 * are only rendered if the screen on top of them is counted as transparent.
 * More info in the ScreenStack class.
 */
public abstract class GameScreen implements CommandListener, 
											Comparable<GameScreen>,
											Updateable,
											Queueable {
	
	/** Command map to use while this screen is active */
	protected CommandMap commandContext;
	/** The thing to draw if this canvas is visible */
	protected ScreenShowable canvas;
	/** What we'll use to render */
	protected TrackerCam cam;
	/** Depth, lower values are rendered last */
	protected float z;
	/** If true, layers with higher z won't be rendered */
	protected boolean transparent;
	/** Batch used for rendering by contents */
	protected SpriteBatch batch;
	/** Batch used to render frame buffers */
	protected SpriteBatch privateBatch;
	/** Buffer we'll be using to draw to */
	protected FrameBuffer buffer;
	/** What we'll be tinting the screen before each render */
	protected Color tint;
	/** Have we been set up yet? */
	protected boolean initialized;
	
	/**
	 * Creates a new game screen. Remember to call intialize when done setting
	 * things up yourself. Fair warning: if you don't give us a camera, we'll
	 * create one for ourselves, potentially reformatting the game window.
	 */
	public GameScreen() {
		transparent = false;
		initialized = false;
		z = 0;
		batch = new SpriteBatch();
		privateBatch = new SpriteBatch();
		buffer = new FrameBuffer(Format.RGB565, 
				RGlobal.window.width, RGlobal.window.height, 
				false);
		tint = Color.WHITE;
		cam = new TrackerCam(RGlobal.window.width, RGlobal.window.height);
		cam.init();
	}
	
	/**
	 * This time we won't create an apocalyptic camera.
	 * @param 	cam				The camera to use instead
	 */
	public GameScreen(TrackerCam cam) {
		transparent = false;
		initialized = false;
		z = 0;
		this.cam = cam;
	}
	
	/**
	 * Called whenever this screen stops being the top screen on the stack. The
	 * screen will stop receiving player commands.
	 */
	public abstract void onFocusLost();
	
	/**
	 * Called whenever this screen starts being the top screen on the stack. The
	 * screen will start receiving player commands.
	 */
	public abstract void onFocusGained();
	
	/**
	 * Sets the z value (depth) of the screen. Higher z values are rendered
	 * later.
	 * @param 	z			The new z-value
	 */
	public void setZ(float z) {
		this.z = z;
	}
	
	/**
	 * Returns the z value (depth) of the screen. Higher z values are rendered
	 * later.
	 * @return					The current z-value
	 */
	public float getZ() {
		return z;
	}
	
	/** @return The camera this screen uses to render */
	public TrackerCam getCamera() { return cam; }
	
	/** @return Batch used for rendering contents */
	public SpriteBatch getBatch() { return batch; }
	
	/** @return Buffer used for rendering contents */
	public FrameBuffer getBuffer() { return buffer; }
	
	/** @return Game screen whole tint */
	public Color getTint() { return tint; }
	
	/**
	 * Gets the command parser used on this screen. Usually only used by engine.
	 * @return					The command parser used on this screen
	 */
	public CommandMap getCommandContext() {
		return commandContext;
	}
	
	/**
	 * Sets the command parser used on the screen. Usually only used by engine.
	 * @param 	map				The command map to use instead
	 */
	public void setCommandContext(CommandMap map) {
		if (commandContext != null) {
			RGlobal.keymap.unregisterListener(commandContext);
		}
		this.commandContext = map;
		RGlobal.keymap.registerListener(commandContext);
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(GameScreen other) {
		if (z < other.z) {
			return (int) Math.floor(z - other.z) - 1;
		} else if (z > other.z) {
			return 0;
		} else {
			return (int) Math.floor(z - other.z) + 1;
		}
	}

	/**
	 * The root of all evil. We supply the camera.
	 */
	public void render() {
		if (!initialized) {
			RGlobal.reporter.warn("Forgot to intialize screen " + this);
		}
		int width = RGlobal.window.width;
		int height = RGlobal.window.height;
		buffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
		canvas.render(cam);
		buffer.end();
		privateBatch.begin();
		privateBatch.setColor(tint);
		// oh god I'm so sorry
		privateBatch.draw(buffer.getColorBufferTexture(), 0, 0, 0, 0, 
				width, height, 1, 1, 0, 0, 0, width, height, false, true);
		privateBatch.end();
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		//canvas.queueRequiredAssets(manager);
	}

	/**
	 * @see net.wombatrpgs.rainfall.graphics.Renderable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		//canvas.postProcessing(manager);
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		canvas.update(elapsed);
		RGlobal.keymap.update(elapsed);
		cam.update(elapsed);
	}

	/**
	 * @see net.wombatrpgs.rainfall.io.CommandListener#onCommand
	 * (net.wombatrpgs.rainfallschema.io.data.InputCommand)
	 */
	@Override
	public void onCommand(InputCommand command) {
		// default does nothing I think
	}

	/**
	 * Changes the screen's canvas.
	 * @param 	newCanvas		The new renderable canvas
	 */
	public void setCanvas(ScreenShowable newCanvas) {
		this.canvas = newCanvas;
	}

	/**
	 * Run some final safety checks and finish initialization. Call once during
	 * the constructor.
	 */
	protected final void init() {
		if (canvas == null) {
			RGlobal.reporter.warn("No canvas for screen " + this);
		}
		if (commandContext == null) {
			RGlobal.reporter.warn("No command context for screen " + this);
		} else {
			commandContext.registerListener(this);
		}
		// TODO: load with a loading bar
		this.queueRequiredAssets(RGlobal.assetManager);
		RGlobal.assetManager.finishLoading();
		this.postProcessing(RGlobal.assetManager, 0);
		initialized = true;
	}

}
