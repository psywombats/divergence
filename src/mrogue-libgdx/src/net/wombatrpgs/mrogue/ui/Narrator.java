/**
 *  Narrator.java
 *  Created on Oct 10, 2013 3:44:25 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.PositionSetable;
import net.wombatrpgs.mrogue.screen.ScreenShowable;
import net.wombatrpgs.mrogue.ui.text.FontHolder;
import net.wombatrpgs.mrogue.ui.text.TextBoxFormat;
import net.wombatrpgs.mrogueschema.ui.NarratorMDO;

/**
 * A UI element that happily spits out game info as it happens.
 */
public class Narrator implements ScreenShowable, PositionSetable {
	
	protected NarratorMDO mdo;
	protected FontHolder font;
	protected TextBoxFormat format;
	protected float x, y;
	
	protected List<Line> lines;
	
	/**
	 * Creates a new narrator from data. Does not deal with loading its font.
	 * @param	mdo				The data to create the narrator from
	 */
	public Narrator(NarratorMDO mdo, FontHolder font) {
		this.mdo = mdo;
		this.font = font;
		lines = new ArrayList<Line>();
		format = new TextBoxFormat();
		format.x = mdo.offsetX;
		format.y = MGlobal.window.getHeight() - mdo.offsetY;
		format.width = mdo.width;
		format.height = MGlobal.window.getHeight();
		format.align = HAlignment.LEFT;
	}
	
	/** @see net.wombatrpgs.mrogue.maps.Positionable#getX() */
	@Override public float getX() { return x; }

	/** @see net.wombatrpgs.mrogue.maps.Positionable#getY() */
	@Override public float getY() { return y; }

	/** @see net.wombatrpgs.mrogue.maps.PositionSetable#setX(float) */
	@Override public void setX(float x) { this.x = x; }

	/** @see net.wombatrpgs.mrogue.maps.PositionSetable#setY(float) */
	@Override public void setY(float y) { this.y = y; }

	/** @see net.wombatrpgs.mrogue.screen.ScreenShowable#ignoresTint() */
	@Override public boolean ignoresTint() { return true; }


	/**
	 * @see net.wombatrpgs.mrogue.graphics.Renderable#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			if (line.ttl < mdo.fadeout) {
				font.setAlpha(line.ttl/mdo.fadeout);
			} else {
				font.setAlpha(1.f);
			}
			font.draw(MGlobal.screens.peek().getUIBatch(), format,
					line.line, (int) (font.getLineHeight() * -i));
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#queueRequiredAssets
	 * (com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public void queueRequiredAssets(AssetManager manager) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Queueable#postProcessing
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		for (Line line : lines) {
			line.ttl -= elapsed;
		}
		while (lines.size() > 0) {
			Line line = lines.get(0);
			if (line.ttl <= 0) {
				lines.remove(0);
			} else {
				break;
			}
		}
	}
	
	/**
	 * Reports something to the player.
	 * @param	msg				What to say to the player.
	 */
	public void msg(String msg) {
		String thisLine;
		int split;
		if (msg.length() > mdo.chars) {
			for (split = mdo.chars-1; msg.charAt(split) != ' '; split -= 1);
			thisLine = msg.substring(0, split);
		} else {
			thisLine = msg;
			split = 0;
		}
		Line line = new Line(thisLine, mdo.ttl + mdo.fadeout);
		lines.add(Math.min(lines.size(), mdo.lines), line);
		if (lines.size() > mdo.lines) {
			lines.remove(0);
		}
		if (msg.length() != thisLine.length()) {
			msg(msg.substring(split+1));
		}
		
	}
	
	class Line {
		public String line;
		public float ttl;
		public Line(String line, float ttl) {
			this.line = line;
			this.ttl = ttl;
		}
	}
	
}
