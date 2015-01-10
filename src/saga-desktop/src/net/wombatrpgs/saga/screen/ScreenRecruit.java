/**
 *  ScreenRecruit.java
 *  Created on Jun 20, 2014 8:43:30 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.FacesAnimation;
import net.wombatrpgs.mgne.graphics.FacesAnimationFactory;
import net.wombatrpgs.mgne.io.CommandMap;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.ui.Graphic;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextFormat;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.sagaschema.rpg.chara.CharaMDO;
import net.wombatrpgs.sagaschema.rpg.chara.RecruitSelectionMDO;
import net.wombatrpgs.sagaschema.settings.SagaIntroSettingsMDO;

/**
 * Screen to select from a bunch of different 
 */
public class ScreenRecruit extends SagaScreen {
	
	protected static final String INTRO_MDO_KEY = "sagaintro_default";
	
	protected static final String TEXT_RACE_INFO = "Race information";
	
	protected static final int TITLE_HEIGHT = 38;
	protected static final int MENU_WIDTH = 200;
	protected static final int HELP_WIDTH = 260;
	protected static final int PADDING_VERT = 1;
	protected static final int PADDING_HORIZ = 8;
	protected static final int PADDING_EDGE = 10;
	
	protected RecruitSelectionMDO mdo;
	
	protected ScreenName nameScreen;
	protected CommandMap context;
	protected List<String> names;
	protected List<FacesAnimation> sprites;
	protected FinishListener listener;
	protected int selected;
	protected boolean confirming;
	protected boolean displayingHelp; // I hade this mode switches
	
	protected Nineslice titleBG, recruitBG, helpBG;
	protected TextFormat titleFormat, recruitFormat, helpFormat;
	protected String raceText;
	protected int menuX, menuY;
	protected int menuHeight;
	protected int titleY;

	/**
	 * Creates a new recruitment screen from data. Will later prompt the player
	 * to name the recruited character.
	 * @param	mdo				The data to generate recruiting charas from
	 */
	public ScreenRecruit(RecruitSelectionMDO mdo) {
		this.mdo = mdo;
		SagaIntroSettingsMDO introMDO =
				MGlobal.data.getEntryFor(INTRO_MDO_KEY, SagaIntroSettingsMDO.class);
		raceText = introMDO.raceText.replace("\\n", "\n");
		
		sprites = new ArrayList<FacesAnimation>();
		names = new ArrayList<String>();
		
		for (String mdoKey : mdo.options) {
			CharaMDO charaMDO = MGlobal.data.getEntryFor(mdoKey, CharaMDO.class);
			names.add(charaMDO.species);
			FacesAnimation anim = FacesAnimationFactory.create(charaMDO.appearance);
			anim.startMoving();
			sprites.add(anim);
			assets.add(anim);
			addUChild(anim);
		}
		names.add("More information");
		
		menuHeight = PADDING_EDGE * 2 + names.size() *
				(sprites.get(0).getHeight() + PADDING_VERT) - PADDING_VERT;
		
		recruitBG = new Nineslice(MENU_WIDTH, menuHeight);
		assets.add(recruitBG);
		titleBG = new Nineslice(MENU_WIDTH, TITLE_HEIGHT);
		assets.add(titleBG);
		helpBG = new Nineslice(HELP_WIDTH, menuHeight);
		assets.add(helpBG);
		
		menuX = (MGlobal.window.getViewportWidth() - recruitBG.getWidth()) / 2;
		menuY = (MGlobal.window.getViewportHeight() - (recruitBG.getHeight() +
				TITLE_HEIGHT - recruitBG.getBorderHeight())) / 2;
		titleY = menuY + recruitBG.getHeight() - recruitBG.getBorderHeight();
		
		FontHolder font = MGlobal.ui.getFont();
		
		recruitFormat = new TextFormat();
		recruitFormat.align = HAlignment.LEFT;
		recruitFormat.height = sprites.get(0).getHeight();
		recruitFormat.width = MENU_WIDTH - recruitBG.getBorderWidth()*2;
		recruitFormat.x = menuX + sprites.get(0).getWidth() +
				PADDING_HORIZ * 2 + recruitBG.getBorderWidth();
		recruitFormat.y = 0;
		
		titleFormat = new TextFormat();
		titleFormat.align = HAlignment.CENTER;
		titleFormat.height = sprites.get(0).getHeight();
		titleFormat.width = MENU_WIDTH;
		titleFormat.x = menuX;
		titleFormat.y = (int) (titleY + (titleBG.getHeight() - font.getLineHeight()) / 2 +
				font.getLineHeight());
		
		helpFormat = new TextFormat();
		helpFormat.align = HAlignment.CENTER;
		helpFormat.height = getHeight();
		helpFormat.width = HELP_WIDTH - recruitBG.getBorderWidth() * 3;
		helpFormat.x = menuX + (MENU_WIDTH-HELP_WIDTH)/2 + recruitBG.getBorderWidth() * 3 / 2;
		helpFormat.y = menuY + recruitBG.getHeight() - PADDING_EDGE;
		
		selected = 0;
		context = new CMapMenu();
		displayingHelp = false;
	}
	
	/**
	 * Creates a new recruit screen based on the key to recruit data in the DB.
	 * @param	mdoKey			The key of data to use (RectruitSelectionMDO)
	 */
	public ScreenRecruit(String mdoKey) {
		this(MGlobal.data.getEntryFor(mdoKey, RecruitSelectionMDO.class));
	}
	
	/**
	 * Creates a new recruit screen from data key and calls a finish listener
	 * when player is done selecting their member.
	 * @param	mdoKey			The key of data to use (RectruitSelectionMDO)
	 * @param	listener		The listener to call when recruit is done
	 */
	public ScreenRecruit(String mdoKey, FinishListener listener) {
		this(mdoKey);
		this.listener = listener;
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		pushCommandContext(context);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		FontHolder font = MGlobal.ui.getFont();
		if (displayingHelp) {
			helpBG.renderAt(batch, menuX + (MENU_WIDTH-HELP_WIDTH)/2, menuY);
			font.draw(batch, helpFormat, raceText, 0);
		} else {
			recruitBG.renderAt(batch, menuX, menuY);
			for (int i = 0; i < names.size(); i += 1) {
				int renderY = (menuY + recruitBG.getHeight()) - PADDING_EDGE - sprites.get(0).getHeight()
						- (PADDING_VERT + sprites.get(0).getHeight()) * i;
				if (i < sprites.size()) {
					sprites.get(i).renderAt(batch,
							menuX + PADDING_HORIZ + recruitBG.getBorderWidth(), renderY);
				}
				if (i == selected) {
					Graphic cursor = MGlobal.ui.getCursor();
					cursor.renderAt(batch, menuX + PADDING_HORIZ - cursor.getWidth()/2,
							renderY + (sprites.get(0).getHeight() - cursor.getHeight()) / 2 - 4);
				}
				renderY -= (sprites.get(0).getHeight() - font.getLineHeight()) / 2;
				renderY += font.getLineHeight()*2;
				font.draw(batch, recruitFormat, names.get(i), renderY);
			}
		}
		
		titleBG.renderAt(batch, menuX, titleY);
		if (displayingHelp) {
			font.draw(batch, titleFormat, TEXT_RACE_INFO, 0);
		} else {
			font.draw(batch, titleFormat, mdo.title, 0);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		switch (command) {
		case MOVE_DOWN:			selected += 1;		break;
		case MOVE_UP:			selected -= 1;		break;
		case UI_CONFIRM:		onConfirm();		break;
		default:				return super.onCommand(command);
		}
		if (selected >= names.size()) {
			selected = 0;
		}
		if (selected < 0) {
			selected = names.size() - 1;
		}
		return true;
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusLost()
	 */
	@Override
	public void onFocusLost() {
		super.onFocusLost();
		removeCommandContext(context);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		titleBG.dispose();
		recruitBG.dispose();
		for (FacesAnimation sprite : sprites) {
			sprite.dispose();
		}
		if (nameScreen != null) {
			nameScreen.dispose();
		}
	}

	/**
	 * Checks if the character has been recruited yet etc.
	 * @return					True if chara is done
	 */
	public boolean isDone() {
		return nameScreen != null && nameScreen.isDone();
	}
	
	/**
	 * Designates a listener to call when recruiting is copmlete.
	 * @param listener
	 */
	public void setFinishListener(FinishListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Transitions to the name screen.
	 */
	protected void onConfirm() {
		if (confirming) {
			return;
		}
		if (displayingHelp) {
			displayingHelp = false;
			return;
		}
		if (selected >= mdo.options.length) {
			displayingHelp = true;
		} else {
			confirming = true;
			CharaMDO result = MGlobal.data.getEntryFor(mdo.options[selected], CharaMDO.class);
			Chara chara = new Chara(result, false);
			MGlobal.assets.loadAsset(chara, "recruited chara");
			SGlobal.heroes.addHero(chara);
			nameScreen = new ScreenName(chara, listener);
			MGlobal.assets.loadAsset(nameScreen, "name screen");
			MGlobal.screens.pop();
			MGlobal.screens.push(nameScreen);
		}
	}
	
}
