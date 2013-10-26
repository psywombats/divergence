package net.wombatrpgs.mrogue.screen.instances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import net.wombatrpgs.mrogue.core.Constants;
import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.io.command.CMapSplash;
import net.wombatrpgs.mrogue.maps.objects.Picture;
import net.wombatrpgs.mrogue.maps.objects.TimerListener;
import net.wombatrpgs.mrogue.maps.objects.TimerObject;
import net.wombatrpgs.mrogue.scenes.SceneParser;
import net.wombatrpgs.mrogue.screen.Screen;
import net.wombatrpgs.mrogueschema.io.data.InputCommand;
import net.wombatrpgs.mrogueschema.settings.IntroSettingsMDO;
import net.wombatrpgs.mrogueschema.settings.TitleSettingsMDO;

/**
 * FALL INTO MADNESS.
 */
public class TitleScreen extends Screen {
	
	protected TitleSettingsMDO mdo;
	protected Picture screen, prompt;
	protected SceneParser introParser, immParser;
	protected TimerObject timer;
	protected boolean shouldIntroduce;

	/**
	 * Creates the title screen by looking up default title screen settings.
	 */
	public TitleScreen() {
		super();
		mdo = MGlobal.data.getEntryFor(Constants.KEY_TITLE, TitleSettingsMDO.class);
		screen = new Picture(mdo.bg, 0, 0, 0);
		assets.add(screen);
		addScreenObject(screen);
		pushCommandContext(new CMapSplash());
		shouldIntroduce = false;
		
		IntroSettingsMDO introMDO=MGlobal.data.getEntryFor(Constants.KEY_INTRO, IntroSettingsMDO.class);
		introParser = MGlobal.levelManager.getCutscene(introMDO.titleScene, this);
		immParser = MGlobal.levelManager.getCutscene(introMDO.immScene, this);
		assets.add(introParser);
		assets.add(immParser);
		
		prompt = new Picture(mdo.prompt, mdo.promptX, mdo.promptY, 1);
		prompt.setColor(new Color(1, 1, 1, 0));
		timer = new TimerObject(0f);
		final TitleScreen host = this;
		timer.addListener(new TimerListener() {
			boolean trans = false;
			@Override public void onTimerZero(TimerObject source) {
				if (trans) {
					prompt.tweenTo(new Color(1, 1, 1, 0), mdo.cycle);
				} else {
					prompt.tweenTo(new Color(1, 1, 1, 1), mdo.cycle);
				}
				trans = !trans;
				source.setTime(mdo.cycle);
				source.set(true);
				source.attach(host);
			}
		});
		timer.set(true);
		assets.add(prompt);
		addScreenObject(prompt);
		timer.attach(this);
		
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
		switch (command) {
		case INTENT_QUIT:
			Gdx.app.exit();
			return true;
		case INTENT_CONFIRM:
			shouldIntroduce = true;
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * @see net.wombatrpgs.mrogue.screen.Screen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		if (!immParser.isRunning() && !immParser.hasExecuted()) {
			immParser.run();
		}
		if (shouldIntroduce) {
			if (introParser.hasExecuted()) {
				MGlobal.screens.pop();
				MGlobal.levelManager.setScreen(new GameScreen());
				MGlobal.screens.push(MGlobal.levelManager.getScreen());
			} else if (!introParser.isRunning()) {
				introParser.run();
				removeScreenObject(prompt);
			}
		}
	}

}