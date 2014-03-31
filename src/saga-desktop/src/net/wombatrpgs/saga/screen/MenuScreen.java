/**
 *  MenuScreen.java
 *  Created on Mar 31, 2014 3:52:20 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Option;
import net.wombatrpgs.mgne.ui.OptionSelector;

/**
 * Runs on top of the world screen to handle menus and that sort of thing.
 */
public class MenuScreen extends Screen {
	
	protected OptionSelector mainMenu;

	/**
	 * Creates a new menu screen with the main menu in it.
	 */
	public MenuScreen() {
		pushCommandContext(new CMapMenu());
		mainMenu = new OptionSelector(
				new Option("Abil") {
					@Override public boolean onSelect() { return onAbil(); }
				},
				new Option("Equip") {
					@Override public boolean onSelect() { return onEquip(); }
				},
				new Option("Items") {
					@Override public boolean onSelect() { return onItems(); }
				},
				new Option("Save") {
					@Override public boolean onSelect() { return onSave(); }
				});
		assets.add(mainMenu);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		mainMenu.showAt(0, 0);
	}

	/**
	 * Called when the abilities option is selected from main menu.
	 * @return					False to keep menu open
	 */
	protected boolean onAbil() {
		return false;
	}
	
	/**
	 * Called when the abilities option is selected from main menu.
	 * @return					False to keep menu open
	 */
	protected boolean onEquip() {
		return false;
	}
	
	/**
	 * Called when the abilities option is selected from main menu.
	 * @return					False to keep menu open
	 */
	protected boolean onItems() {
		return false;
	}
	
	/**
	 * Called when the abilities option is selected from main menu.
	 * @return					False to keep menu open
	 */
	protected boolean onSave() {
		return false;
	}
}
