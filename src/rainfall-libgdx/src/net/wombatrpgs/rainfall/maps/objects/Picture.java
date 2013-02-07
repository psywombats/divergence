/**
 *  Picture.java
 *  Created on Feb 4, 2013 3:36:39 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.maps.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.Graphic;
import net.wombatrpgs.rainfall.graphics.Renderable;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfall.maps.PositionSetable;
import net.wombatrpgs.rainfallschema.graphics.GraphicMDO;

/**
 * Replaces the old picture layer that the map had. This is exactly the RM
 * equivalent. The only difference it has with the map object is that it
 * can be compared against other pictures to sort by z-depth.
 */
public class Picture extends MapObject implements 	Comparable<Picture>,
													Renderable,
													PositionSetable {
	
	protected Graphic appearance;
	protected boolean preloaded;
	protected int x, y, z; // z is depth-y
	
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
	 * Creates a picture with a certain appearance and depth.
	 * @param 	appearance		The graphic the picture will render
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(Graphic appearance, int z) {
		this.z = z;
		this.appearance = appearance;
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
		this.appearance = new Graphic(mdo);
		setX(x);
		setY(y);
		this.z = z;
		this.preloaded = false;
	}
	
	/**
	 * Creates a picture from data (a non-preloaded graphic) at 0,0.
	 * @param 	mdo				The data for the graphic that we'll be using
	 * @param 	z				The z-depth (number in RM terms) of the picture
	 */
	public Picture(GraphicMDO mdo, int z) {
		this(mdo, 0, 0, z);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		appearance.renderAt(getX(), getY());
	}
	/**
	 * @see net.wombatrpgs.rainfall.maps.events.MapEvent#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
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
		super.postProcessing(manager, pass);
		if (!preloaded) {
			appearance.postProcessing(manager, pass);
		}
	}
	/**
	 * @see net.wombatrpgs.rainfall.maps.Positionable#getX()
	 */
	@Override
	public int getX() {
		return this.x;
	}
	/**
	 * @see net.wombatrpgs.rainfall.maps.Positionable#getY()
	 */
	@Override
	public int getY() {
		return this.y;
	}
	
	/**
	 * @see net.wombatrpgs.rainfall.maps.PositionSetable#setX(int)
	 */
	@Override
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @see net.wombatrpgs.rainfall.maps.PositionSetable#setY(int)
	 */
	@Override
	public void setY(int y) {
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
	 * Gets the default screen batch. This is usually what's used to render,
	 * although if some special thing is going on, it'd be best to use a
	 * different batch.
	 * @return					The current screen's sprite batch
	 */
	public SpriteBatch getBatch() {
		return RGlobal.screens.peek().getBatch();
	}
	
}
