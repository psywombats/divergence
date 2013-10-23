package net.wombatrpgs.mrogue.screen.instances;

import com.badlogic.gdx.Gdx;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.graphics.Graphic;
import net.wombatrpgs.mrogue.io.SplashCommandMap;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogue.screen.Screen;
import net.wombatrpgs.mrogueschema.cutscene.SceneMDO;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;
import net.wombatrpgs.mrogueschema.settings.IntroSettingsMDO;
import net.wombatrpgs.mrogueschema.settings.TitleSettingsMDO;

/**
 * FALL INTO MADNESS.
 */
public class TitleScreen extends Screen {
	
	protected TitleSettingsMDO mdo;
	protected Graphic screen;
	protected SceneParser introParser;

	/**
	 * Creates the title screen by looking up default title screen settings.
	 */
	public TitleScreen() {
		super();
		mdo = MGlobal.data.getEntryFor(Constants.TITLE_KEY, TitleSettingsMDO.class);
		screen = new Graphic(mdo.bg);
		assets.add(screen);
		pushCommandContext(new SplashCommandMap());
		
		IntroSettingsMDO introMDO=MGlobal.data.getEntryFor("default_intro", IntroSettingsMDO.class);
		SceneMDO sceneMDO = MGlobal.data.getEntryFor(introMDO.scene, SceneMDO.class);
		introParser = new SceneParser(sceneMDO, this);
		
		init();
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.Screen#render()
	 */
	@Override
	public void render() {
		super.render();
		screen.renderAt(getViewBatch(), 0, 0);
	}

	/**
	 * @see net.wombatrpgs.mrogue.screen.Screen#onCommand
	 * (net.wombatrpgs.mrogueschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		switch (command) {
		case INTENT_QUIT:
			Gdx.app.exit();
			return true;
		case INTENT_CONFIRM:
			toGame();
			return true;
		default:
			return super.onCommand(command);
		}
	}
	
	/**
	 * Transitions to the main game.
	 */
	public void toGame() {
		MGlobal.screens.pop();
		MGlobal.levelManager.setScreen(new GameScreen());
		MGlobal.screens.push(MGlobal.levelManager.getScreen());
	}

}