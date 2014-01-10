package net.wombatrpgs.saga.screen.instances;

import com.badlogic.gdx.Gdx;

import net.wombatrpgs.saga.core.Constants;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.io.command.CMapSplash;
import net.wombatrpgs.saga.maps.objects.Picture;
import net.wombatrpgs.saga.scenes.SceneParser;
import net.wombatrpgs.saga.screen.Screen;
import net.wombatrpgs.sagaschema.io.data.InputCommand;
import net.wombatrpgs.sagaschema.settings.EndSettingsMDO;
import net.wombatrpgs.sagaschema.settings.IntroSettingsMDO;

/**
 * FALLed INTO MADNESS.
 */
public class EndingScreen extends Screen {
	
	protected static final String KEY_ENDING = "ending_default";
	
	protected EndSettingsMDO mdo;
	protected Picture screen;
	protected SceneParser outroParser, inParser, outParser;

	/**
	 * Creates the title screen by looking up default title screen settings.
	 */
	public EndingScreen() {
		super();
		mdo = SGlobal.data.getEntryFor(KEY_ENDING, EndSettingsMDO.class);
		screen = new Picture(mdo.bg, 0, 0, 0);
		assets.add(screen);
		addObject(screen);
		pushCommandContext(new CMapSplash());
		
		IntroSettingsMDO introMDO=SGlobal.data.getEntryFor(Constants.KEY_INTRO, IntroSettingsMDO.class);
		outroParser = SGlobal.levelManager.getCutscene(mdo.ending, this);
		inParser = SGlobal.levelManager.getCutscene(introMDO.immScene, this);
		outParser = SGlobal.levelManager.getCutscene(introMDO.outScene, this);
		assets.add(outroParser);
		assets.add(inParser);
		assets.add(outParser);
		
		init();
	}

	/**
	 * @see net.wombatrpgs.saga.screen.Screen#onCommand
	 * (net.wombatrpgs.sagaschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (super.onCommand(command)) {
			return true;
		}
		switch (command) {
		case INTENT_QUIT:
			Gdx.app.exit();
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * @see net.wombatrpgs.saga.screen.Screen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (!inParser.isRunning() && !inParser.hasExecuted()) {
			inParser.run();
		} else if (outroParser.hasExecuted()) {
			if (outParser.hasExecuted()) {
				Gdx.app.exit();
			} else if (!outParser.isRunning()) {
				outParser.run();
			}
		} else if (!outroParser.isRunning()) {
			outroParser.run();
		}
	}

}