/**
 *  Picture.java
 *  Created on Feb 4, 2013 3:36:39 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.rainfall.graphics.Graphic;
import net.wombatrpgs.rainfall.maps.PositionSetable;
import net.wombatrpgs.rainfall.screen.ScreenShowable;
import net.wombatrpgs.rainfallschema.graphics.GraphicMDO;

/**
 * Replaces the old picture layer that the map had. This is exactly the RM
 * equivalent. The only difference it has with the map object is that it
 * can be compared against other pictures to sort by z-depth.
 */
public class Picture implements Comparable<Picture>,
								ScreenShowable,
								PositionSetable {
	
	protected Graphic appearance;
	public SpriteBatch batch;
	protected Color currentColor;
	protected float x, y;
	protected int z; // z is depth-y
	protected boolean preloaded;
	protected boolean ignoresTint;
	
	protected boolean tweening;
	protected Color tweenTargetColor;
	protected Color tweenBaseColor;
	protected float tweenTime;
	protected float tweenEnd;
	
	/**
	 * Create a picture at an explicit location
	 * @param 	appearance		The graphic the picture will render
	 * @param 	x				The x-coord (in pixels) to render at
	 * @param 	y				The y-coord (in pixels) to render at	
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(Graphic appearance, int x, int y, int z) {
		this(appearance, z);
		setX(x);
		setY(y);
		this.preloaded = true;
	}
	
	/**
	 * Creates a picture from data (a non-preloaded graphic).
	 * @param 	mdo				The data for the graphic that we'll be using
	 * @param 	x				The x-coord (in pixels) to render at
	 * @param 	y				The y-coord (in pixels) to render at
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(GraphicMDO mdo, int x, int y, int z) {
		this(new Graphic(mdo), z);
		setX(x);
		setY(y);
		this.preloaded = false;
	}
	
	/**
	 * Creates a picture with a certain appearance and depth.
	 * @param 	appearance		The graphic the picture will render
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(Graphic appearance, int z) {
		this.z = z;
		this.appearance = appearance;
		this.preloaded = true;
		this.ignoresTint = true;
		this.tweening = false;
		this.batch = new SpriteBatch();
		this.currentColor = new Color(1, 1, 1, 1);
		this.tweenBaseColor = new Color(1, 1, 1, 1);
		this.tweenTargetColor = new Color(1, 1, 1, 1);
	}
	
	/**
	 * Creates a picture from data (a non-preloaded graphic) at 0,0.
	 * @param 	mdo				The data for the graphic that we'll be using
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(GraphicMDO mdo, int z) {
		this(mdo, 0, 0, z);
		this.preloaded = false;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		if (tweening) {
			tweenTime += elapsed;
			float r;
			if (tweenTime > tweenEnd) {
				r = 1;
				tweening = false;
			} else {
				r = tweenTime / tweenEnd;
			}
			currentColor.a = tweenBaseColor.a * (1.f-r) + r * tweenTargetColor.a;
			currentColor.r = tweenBaseColor.r * (1.f-r) + r * tweenTargetColor.r;
			currentColor.g = tweenBaseColor.g * (1.f-r) + r * tweenTargetColor.g;
			currentColor.b = tweenBaseColor.b * (1.f-r) + r * tweenTargetColor.b;
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		batch.setColor(currentColor);
		appearance.renderAt(batch, x, y);
	}
	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		if (!preloaded) {
			appearance.queueRequiredAssets(manager);
		}
	}
	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		if (!preloaded) {
			appearance.postProcessing(manager, pass);
		}
	}
	/**
	 * @see net.wombatrpgs.rainfall.maps.Positionable#getX()
	 */
	@Override
	public float getX() {
		return this.x;
	}
	/**
	 * @see net.wombatrpgs.rainfall.maps.Positionable#getY()
	 */
	@Override
	public float getY() {
		return this.y;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.PositionSetable#setX(int)
	 */
	@Override
	public void setX(float x) {
		this.x = x;
	}
	/**
	 * @see net.wombatrpgs.rainfall.maps.PositionSetable#setY(int)
	 */
	@Override
	public void setY(float y) {
		this.y = y;
	}
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Picture other) {
		return z - other.z;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.screen.ScreenShowable#ignoresTint()
	 */
	@Override
	@Deprecated
	public boolean ignoresTint() {
		return ignoresTint;
	}
	
	/**
	 * Gets the default screen batch. This is usually what's used to render,
	 * although if some special thing is going on, it'd be best to use a
	 * different batch.
	 * @return					The current screen's sprite batch
	 */
	public SpriteBatch getBatch() {
		return batch;
	}
	
	/**
	 * Sets the batch we use to render. This is kind of hacky and shouldn't be
	 * used.
	 * @param 	batch			The batch to set to
	 */
	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}
	
	/**
	 * Tweens to a new color. Undefined behavior if already tweening. This is a
	 * smooth transition from one color to another.
	 * @param 	target			The color to end up as
	 * @param 	time			How long to take
	 */
	public void tweenTo(Color target, float time) {
		tweenTargetColor.set(target);
		tweenBaseColor.set(currentColor);
		this.tweenEnd = time;
		this.tweenTime = 0;
		this.tweening = true;
	}
	
	/**
	 * Sets the picture's rendering color.
	 * @param 	newColor
	 */
	public void setColor(Color newColor) {
		tweening = false;
		currentColor.set(newColor);
	}

}
