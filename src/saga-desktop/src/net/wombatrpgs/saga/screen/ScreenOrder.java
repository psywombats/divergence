/**
 *  OrderScreen.java
 *  Created on May 14, 2014 1:41:12 AM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.rpg.chara.Party;
import net.wombatrpgs.saga.ui.CharaSelector;
import net.wombatrpgs.saga.ui.CharaSelector.SelectionListener;

/**
 * Screen for changing party order.
 */
public class ScreenOrder extends SagaScreen {
	
	protected static final int BG_WIDTH = 140;
	protected static final int BG_HEIGHT = 180;
	protected static final int INSERTS_PADDING = 5;
	protected static final int CURSOR_SPACE = 1;

	protected Party party;
	
	protected CharaSelector selector;
	protected Nineslice bg;
	protected float bgX, bgY;
	
	protected Chara firstSelected;
	
	/**
	 * Constructs an order screen to change the order of a particular party.
	 * @param	party			The party to set up for
	 */
	public ScreenOrder(Party party) {
		this.party = party;
		
		selector = new CharaSelector(party, true, false, false, 0,
				INSERTS_PADDING, CURSOR_SPACE, 1, party.size());
		addUChild(selector);
		assets.add(selector);
		
		bg = new Nineslice(BG_WIDTH, BG_HEIGHT);
		assets.add(bg);
		
		bgX = (MGlobal.window.getViewportWidth() - BG_WIDTH) / 2;
		bgY = (MGlobal.window.getViewportHeight() - BG_HEIGHT) / 2;
	}
	
	/**
	 * Default constructor. Makes a swap screen for the hero party.
	 */
	public ScreenOrder() {
		this(SGlobal.heroes);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		bg.renderAt(batch, bgX, bgY);
		selector.render(batch);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#postProcessing
	 * (net.wombatrpgs.mgne.core.MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		selector.setX(bgX + (BG_WIDTH - selector.getWidth()) / 2);
		selector.setY(bgY + (BG_HEIGHT - selector.getHeight()) / 2);
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		awaitFirstSelection();
	}
	
	/**
	 * @see net.wombatrpgs.saga.screen.SagaScreen#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (selector.isSwapping()) {
			return true;
		} else {
			return super.onCommand(command);
		}
	}

	/**
	 * Awaits the player selecting a first character to swap.
	 */
	protected void awaitFirstSelection() {
		selector.clearIndent();
		selector.awaitSelection(new SelectionListener() {
			@Override public boolean onSelection(Chara selected) {
				if (selected == null) {
					MGlobal.screens.pop();
					return true;
				} else {
					return onFirstSelect(selected);
				}
			}
		}, true);
	}
	
	/**
	 * Called when the first party member to swap is selected.
	 * @param	selected		The chara that was selected, never null
	 * @return					False to keep the menu open
	 */
	protected boolean onFirstSelect(Chara selected) {
		firstSelected = selected;
		selector.setIndent();
		selector.awaitSelection(new SelectionListener() {
			@Override public boolean onSelection(Chara selected) {
				if (selected == null) {
					awaitFirstSelection();
					selector.setSelected(firstSelected);
					firstSelected = null;
				} else {
					return onSecondSelect(selected);
				}
				return false;
			}
		}, true);
		selector.setSelected(firstSelected);
		return false;
	}
	
	/**
	 * Called when the second party member to swap is selected.
	 * @param	selected		The chara that was selected, never null
	 * @return					False to keep the menu open
	 */
	protected boolean onSecondSelect(Chara selected) {
		Chara chara1 = firstSelected;
		Chara chara2 = selected;
		if (chara1 == chara2) return false;
		party.swap(chara1, chara2);
		selector.swap(chara1, chara2, new FinishListener() {
			@Override public void onFinish() {
				awaitFirstSelection();
				selector.setSelected(firstSelected);
			}
		});
		return false;
	}

}
