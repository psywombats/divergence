/**
 *  ClassScreen.java
 *  Created on Oct 28, 2013 8:36:45 PM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.screen.instances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.io.command.CMapDirections;
import net.wombatrpgs.mrogue.maps.objects.Picture;
import net.wombatrpgs.mrogue.rpg.Hero;
import net.wombatrpgs.mrogue.rpg.UnitClass;
import net.wombatrpgs.mrogue.rpg.abil.Ability;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogue.screen.Screen;
import net.wombatrpgs.mrogue.ui.text.FontHolder;
import net.wombatrpgs.mrogue.ui.text.TextBoxFormat;
import net.wombatrpgs.mrogueschema.characters.ClassMDO;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;
import net.wombatrpgs.mrogueschema.settings.ClassSettingsMDO;
import net.wombatrpgs.mrogueschema.settings.IntroSettingsMDO;
import net.wombatrpgs.mrogueschema.ui.FontMDO;

/**
 * Thing for selecting what class you want to be!
 */
public class ClassScreen extends Screen {
	
	protected static final String KEY_DEFAULT = "classes_default";
	
	protected static final int TEXT_HEIGHT = 512;
	protected static final int PAD_X = 32;
	protected static final int PAD_Y = 32;
	
	protected ClassSettingsMDO mdo;
	protected List<UnitClass> classes;
	protected Picture screen;
	protected SceneParser inParser, outParser;
	protected TextBoxFormat format;
	protected FontHolder font;
	protected Graphic cursor;
	
	protected Screen gameScreen;
	protected int selectedX, selectedY;
	protected boolean fading;
	
	/**
	 * Creates a new class screen from default data.
	 */
	public ClassScreen() {
		mdo = MGlobal.data.getEntryFor(KEY_DEFAULT, ClassSettingsMDO.class);
		
		gameScreen = new GameScreen();
		MGlobal.levelManager.setScreen(gameScreen);
		
		screen = new Picture(mdo.bg, 0, 0, 0);
		addObject(screen);
		assets.add(screen);
		
		IntroSettingsMDO introMDO=MGlobal.data.getEntryFor(Constants.KEY_INTRO, IntroSettingsMDO.class);
		inParser = MGlobal.levelManager.getCutscene(introMDO.immScene, this);
		outParser = MGlobal.levelManager.getCutscene(introMDO.outScene, this);
		assets.add(inParser);
		assets.add(outParser);
		
		MGlobal.hero = new Hero(MGlobal.levelManager.getActive());
		assets.add(MGlobal.hero);
		
		font = new FontHolder(MGlobal.data.getEntryFor(mdo.font, FontMDO.class));
		assets.add(font);
		
		cursor = new Graphic(mdo.cursor);
		assets.add(cursor);
		
		pushCommandContext(new CMapDirections());
		
		List<ClassMDO> classMDOs = MGlobal.data.getEntriesByClass(ClassMDO.class);
		classes = new ArrayList<UnitClass>();
		UnitClass op = null;
		for (ClassMDO classMDO : classMDOs) {
			UnitClass c = new UnitClass(classMDO, MGlobal.hero);
			if (classMDO.key.equals(mdo.op)) {
				op = c;
			}
			classes.add(c);
			assets.add(c);
		}
		classes.remove(op);
		Collections.shuffle(classes);
		if (MGlobal.deathCount >= 2) {
			classes.add(0, op);
		}
		
		format = new TextBoxFormat();
		format.align = HAlignment.CENTER;
		format.height = TEXT_HEIGHT;
		
		selectedX = 0;
		selectedY = 0;
		
		init();
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.Screen#onCommand
	 * (net.wombatrpgs.mrogueschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (super.onCommand(command)) {
			return true;
		}
		if (fading) return true;
		switch (command) {
		case INTENT_QUIT:
			Gdx.app.exit();
			return true;
		case INTENT_CONFIRM:
			confirm();
			return true;
		case MOVE_NORTH:						selectedY = 0;	return true;
		case MOVE_NORTHEAST:	selectedX = 1;	selectedY = 0;	return true;
		case MOVE_EAST:			selectedX = 1;					return true;
		case MOVE_SOUTHEAST:	selectedX = 1;	selectedY = 1;	return true;
		case MOVE_SOUTH:						selectedY = 1;	return true;
		case MOVE_SOUTHWEST:	selectedX = 0;	selectedY = 1;	return true;
		case MOVE_WEST:			selectedX = 0;					return true;
		case MOVE_NORTHWEST:	selectedX = 0;	selectedY = 0;	return true;
		default:
			return false;
		}
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.screen.Screen#render()
	 */
	@Override
	public void render() {
		super.render();
		for (int x = 0; x < 2; x += 1) {
			for (int y = 0; y < 2; y += 1) {
				if (y*2+x >= classes.size()) break;
				boolean sel = selectedX == x && selectedY == y;
				renderClass(classes.get(y*2+x), x, y, sel);
			}
		}
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.Screen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (!inParser.hasExecuted() && !inParser.isRunning()) {
			inParser.run();
		}
		for (UnitClass c : classes) {
			c.getAnim().update(elapsed);
		}
		if (fading) {
			if (outParser.hasExecuted()) {
				MGlobal.screens.pop();
				MGlobal.screens.push(gameScreen);
			} else if (!outParser.isRunning()) {
				outParser.run();
			}
		}
	}

	/**
	 * Sets the player up with the specified stats and moves to the next screen.
	 */
	protected void confirm() {
		gameScreen.init();
		fading = true;
		classes.get(selectedY*2 + selectedX).apply();
		MGlobal.hero.setAppearance(classes.get(selectedY*2 + selectedX).getAnim());
	}
	
	/**
	 * Renders a class in a cell.
	 * @param	c				The class to render selection for
	 * @param	x				The x-coord to render at (in cells)
	 * @param	y				The y-coord to render at (in cells)
	 * @param	selected		True to draw highlight on this class
	 */
	protected void renderClass(UnitClass c, int x, int y, boolean selected) {
		int atX = (MGlobal.window.getWidth()/2 + PAD_X/2) * x + PAD_X;
		int atY = (MGlobal.window.getHeight()/2 + PAD_Y/2) * (1-y) + PAD_Y;
		int cellWidth = MGlobal.window.getWidth()/2 - PAD_X*2;
		int cellHeight = MGlobal.window.getHeight()/2 - PAD_Y*2;
		
		uiBatch.setColor(getTint());
		uiBatch.begin();
		TextureRegion tex = c.getAnim().getRegion();
		if (selected) {
			uiBatch.draw(cursor.getGraphic(),
					atX + cellWidth/2 - cursor.getWidth()/2,
					atY + cellHeight*5/6 - cursor.getHeight()/2 - tex.getRegionHeight()/2);
		}
		uiBatch.draw(tex,
				atX + cellWidth/2 - tex.getRegionWidth()/2,
				atY + cellHeight*5/6 - tex.getRegionHeight()/2);
		uiBatch.end();
		
		format.align = HAlignment.CENTER;
		format.width = cellWidth;
		format.x = atX;
		format.y = atY + cellHeight*2/3;
		font.draw(uiBatch, format, c.getName(), 0);
		
		format.align = HAlignment.LEFT;
		format.y = atY + cellHeight*1/2;
		font.draw(uiBatch, format, c.getDesc(), 0);
		
		int offY = 0;
		for (Ability abil : c.getSkills()) {
			format.x = atX;
			format.y = atY + cellHeight*1/4;
			font.draw(uiBatch, format, abil.getName()+":", offY);
			format.x = atX + cellWidth/3;
			font.draw(uiBatch, format, abil.getDesc(), offY);
			offY -= font.getLineHeight();
		}
	}
}
