/**
 *  StatsBar.java
 *  Created on Jan 1, 2015 6:14:02 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.ScreenGraphic;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextFormat;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.sagaschema.rpg.stats.Stat;

/**
 * A bar that shows the stats of a character.
 */
public class StatsBar extends ScreenGraphic {
	
	protected static final int DEFAULT_STATS_WIDTH = 48;
	protected static final int DEFAULT_STATS_HEIGHT = 114;
	protected static final int DEFAULT_STATS_PADDING = 8;
	
	protected Chara chara;
	protected Nineslice statsBG;
	protected TextFormat statFormat, labelFormat;
	protected List<Stat> statDisplay;
	protected boolean horizontal;
	protected int width, height;
	protected int padding;
	
	/**
	 * Creates a stats bar with custom width, height, and padding.
	 * @param	chara			The chara to draw the bar for
	 * @param	width			The width of the bar, in pixels
	 * @param	height			The height of the bar, in pixels
	 * @param	padding			The vertical padding between stats, in pixels
	 * @param	horizontal		True to lay out horizontal, false for vertical
	 */
	public StatsBar(Chara chara, int width, int height, int padding, boolean horizontal) {
		this.chara = chara;
		this.width = width;
		this.height = height;
		this.padding = padding;
		this.horizontal = horizontal;
		
		statsBG = new Nineslice(width, height);
		assets.add(statsBG);
		
		statDisplay = new ArrayList<Stat>();
		statDisplay.add(Stat.STR);
		statDisplay.add(Stat.AGI);
		statDisplay.add(Stat.DEF);
		statDisplay.add(Stat.MANA);
	}
	
	/**
	 * Creates a stats bar with default width, height, and padding.
	 * @param	chara			The chara to generate the bar for
	 */
	public StatsBar(Chara chara) {
		this(chara, DEFAULT_STATS_WIDTH, DEFAULT_STATS_HEIGHT, DEFAULT_STATS_PADDING, false);
	}

	/** @see net.wombatrpgs.mgne.graphics.ScreenGraphic#getWidth() */
	@Override public int getWidth() { return width; }

	/** @see net.wombatrpgs.mgne.graphics.ScreenGraphic#getHeight() */
	@Override public int getHeight() { return height; }
	
	/** @return The border width of the stats background */
	public int getBorderWidth() { return statsBG.getBorderWidth(); }
	
	/** @return The border height of the stats background */
	public int getBorderHeight() { return statsBG.getBorderHeight(); }

	/**
	 * @see net.wombatrpgs.mgne.graphics.ScreenGraphic#coreRender
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void coreRender(SpriteBatch batch) {
		statsBG.renderAt(batch, x, y);
		
		FontHolder font = MGlobal.ui.getFont();
		for (int i = 0; i < statDisplay.size(); i += 1) {
			Stat stat = statDisplay.get(i);
			int offY = 0;
			int offX = 0;
			if (horizontal) {
				offX = (int) (i * padding);
			} else {
				offY = (int) (-i * (font.getLineHeight()*2 + padding));
			}
			font.draw(batch, labelFormat, stat.getLabel(), offX, offY);
			font.draw(batch, statFormat, (int) chara.get(stat) + "", offX, offY);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.core.AssetQueuer#postProcessing
	 * (net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		
		FontHolder font = MGlobal.ui.getFont();
		
		labelFormat = new TextFormat();
		labelFormat.align = HAlignment.LEFT;
		labelFormat.width = width - statsBG.getBorderWidth()*2;
		labelFormat.height = 80;
		labelFormat.x = (int) (x + statsBG.getBorderWidth());
		labelFormat.y = (int) (y + getHeight() - (statsBG.getBorderHeight() / 2) -
				statsBG.getBorderHeight());
		
		statFormat = new TextFormat();
		statFormat.align = HAlignment.RIGHT;
		if (horizontal) {
			statFormat.width  = padding * 2 / 3;
		} else {
			statFormat.width = width - statsBG.getBorderWidth()*5/2;
		}
		statFormat.height = 80;
		statFormat.x = (int) (x + statsBG.getBorderWidth());
		statFormat.y = (int) (y + (int) (height - (statsBG.getBorderHeight() / 2 +
				font.getLineHeight() + statsBG.getBorderHeight())));
	}

}
