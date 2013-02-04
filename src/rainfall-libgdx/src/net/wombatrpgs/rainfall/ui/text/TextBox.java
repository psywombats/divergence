/**
 *  TextBox.java
 *  Created on Feb 2, 2013 3:47:40 AM for project rainfall-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfall.ui.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.rainfall.core.RGlobal;
import net.wombatrpgs.rainfall.graphics.Graphic;
import net.wombatrpgs.rainfall.maps.MapObject;
import net.wombatrpgs.rainfallschema.graphics.GraphicMDO;
import net.wombatrpgs.rainfallschema.ui.TextBoxMDO;

/**
 * A box that appears on-screen and does its dirty business. Hmph. Actually it
 * stores some data from its MDO, but dynamically updates its strings. In the
 * future it will do some other cool stuff. These things only display on the
 * map the hero is on. Otherwise it really wouldn't make sense, would it?
 */
// TODO: turn into picture
public class TextBox extends MapObject {
	
	protected TextBoxMDO mdo;
	protected FontHolder font;
	protected TextBoxFormat format;
	protected Graphic backer;
	protected String text;
	
	/**
	 * Creates a new text box from data. Does not deal with the loading of its
	 * font's assets.
	 * @param 	mdo				The MDO to create from
	 * @param 	font			The font to use in rendering, can change
	 */
	public TextBox(TextBoxMDO mdo, FontHolder font) {
		this.mdo = mdo;
		this.font = font;
		this.format = new TextBoxFormat();
		format.x = mdo.x1;
		format.y = mdo.y1;
		format.align = HAlignment.LEFT;
		format.height = mdo.y2 - mdo.y1;
		format.width = mdo.x2 - mdo.x1;
		if (mdo.image != null) {
			GraphicMDO graphicMDO = RGlobal.data.getEntryFor(mdo.image, GraphicMDO.class);
			this.backer = new Graphic(graphicMDO);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		if (backer != null) {
			backer.renderAt(RGlobal.hero.getBatch(),
					mdo.graphicX,
					Gdx.graphics.getHeight() - mdo.graphicY - backer.getGraphic().getRegionHeight());
		}
		font.draw(RGlobal.hero.getBatch(), format, text);
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		super.queueRequiredAssets(manager);
		if (backer != null) {
			backer.queueRequiredAssets(manager);
		}
	}

	/**
	 * @see net.wombatrpgs.rainfall.maps.MapObject#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		super.postProcessing(manager, pass);
		if (backer != null) {
			backer.postProcessing(manager, pass);
		}
	}
	
	/**
	 * Sets the internal text to be displayed at this textbox. Nothing fancy.
	 * @param 	text			The text to be displayed
	 */
	public void setText(String text) {
		this.text = text;
	}

}
