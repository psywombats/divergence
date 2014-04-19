/**
 *  CombatScreen.java
 *  Created on Apr 15, 2014 2:26:58 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.Option;
import net.wombatrpgs.mgne.ui.OptionSelector;
import net.wombatrpgs.saga.rpg.Battle;

/**
 * Screen for killing shit. This also encompasses a battle. Like Tactics, the
 * idea is that a battle is owned by a screen and controls the screen, but the
 * logic is kept separate from the display. Owned by a battle.
 */
public class CombatScreen extends Screen {
	
	protected static int OPTIONS_WIDTH = 96;
	protected static int OPTIONS_HEIGHT = 120;
	protected static int INSERTS_WIDTH = 224;
	protected static int INSERTS_HEIGHT = 120;
	
	protected Battle battle;
	
	protected Nineslice optionsBG, insertsBG;
	protected OptionSelector options;
	protected float globalX, globalY;
	
	/**
	 * Creates a new combat setup. This initializes the screen and passes the
	 * arguments to the battle.
	 * @param	battle			The battle this screen will be used for
	 */
	public CombatScreen(final Battle battle) {
		this.battle = battle;
		pushCommandContext(new CMapMenu());
		options = new OptionSelector(false, true, new Option("FIGHT") {
			@Override public boolean onSelect() {
				battle.onFight();
				return false;
			}
		}, new Option("RUN") {
			@Override public boolean onSelect() {
				battle.onRun();
				return false;
			}
		});
		assets.add(options);
		optionsBG = new Nineslice(OPTIONS_WIDTH, OPTIONS_HEIGHT);
		insertsBG = new Nineslice(INSERTS_WIDTH + optionsBG.getBorderWidth(), INSERTS_HEIGHT);
		assets.add(optionsBG);
		assets.add(insertsBG);
		
		globalX = (getWidth() - (INSERTS_WIDTH + OPTIONS_WIDTH)) / 2;
		globalY = (getHeight() - (INSERTS_HEIGHT + OPTIONS_HEIGHT)) / 2;
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		optionsBG.renderAt(batch, globalX, globalY);
		insertsBG.renderAt(batch, globalX + INSERTS_WIDTH - optionsBG.getBorderWidth(), globalY);
		super.render(batch);
	}

}
