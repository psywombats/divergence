/**
 *  MenuScreen.java
 *  Created on Mar 31, 2014 3:52:20 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MAssets;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.Option;
import net.wombatrpgs.mgne.ui.OptionSelector;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextboxFormat;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.chara.Chara;
import net.wombatrpgs.saga.ui.CharaSelector;
import net.wombatrpgs.saga.ui.CharaSelector.SelectionListener;

/**
 * The main menu that gets called when the user pauses on the world map.
 */
public class PauseScreen extends SagaScreen implements Disposable {
	
	protected static final int INFO_HEIGHT = 32;
	protected static final int INFO_MARGINS = 8;
	
	protected static final int GLOBAL_Y = 92;
	
	protected OptionSelector menu, saveSelector;
	protected CharaSelector inserts;
	protected TextboxFormat format;
	protected Nineslice infoBG;
	protected String info1, info2;
	protected int insertsX, insertsY;
	
	protected boolean silentAdd;

	/**
	 * Creates a new menu screen with the main menu in it.
	 */
	public PauseScreen() {
		pushCommandContext(new CMapMenu());
		menu = new OptionSelector(
			new Option("Save") {
				@Override public boolean onSelect() { return onSave(); }
			},
			new Option("Abil") {
				@Override public boolean onSelect() { return onAbil(); }
			},
			new Option("Equip") {
				@Override public boolean onSelect() { return onEquip(); }
			},
			new Option("Items") {
				@Override public boolean onSelect() { return onItems(); }
			},
			new Option("Order") {
				@Override public boolean onSelect() { return onOrder(); }
			});
		menu.setCancel(new FinishListener() {
			@Override public void onFinish() {
				menu.close();
				MGlobal.screens.pop();
			}
		});
		assets.add(menu);
		saveSelector = new OptionSelector(
				new Option("Really save") {
					@Override public boolean onSelect() {
						Screen menuScreen = MGlobal.screens.pop();
						MGlobal.memory.save("alpha_save.sav");
						silentAdd = true;
						MGlobal.screens.push(menuScreen);
						silentAdd = false;
						refocusMain();
						return true;
					}
				},
				new Option("Cancel") {
					@Override public boolean onSelect() {
						refocusMain();
						return true;
					}
				});
		saveSelector.setCancel(new FinishListener() {
			@Override public void onFinish() {
				saveSelector.close();
				refocusMain();
			}
		});
		assets.add(saveSelector);
		
		infoBG = new Nineslice();
		assets.add(infoBG);
		
		format = new TextboxFormat();
		
		silentAdd = false;
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		if (!silentAdd) {
			refocusMain();
			createDisplay();
		}
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		inserts.render(batch);
		infoBG.renderAt(batch, insertsX, insertsY + inserts.getHeight() - inserts.getBorderHeight());
		FontHolder font = MGlobal.ui.getFont();
		font.draw(batch, format, info1, 0);
		font.draw(batch, format, info2, -(int) font.getLineHeight());
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#postProcessing
	 * (MAssets, int)
	 */
	@Override
	public void postProcessing(MAssets manager, int pass) {
		super.postProcessing(manager, pass);
		if (pass == 0) {
			createDisplay();
			infoBG.resizeTo(inserts.getWidth(), INFO_HEIGHT);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		infoBG.dispose();
	}

	/**
	 * Called when the abilities option is selected from main menu.
	 * @return					False to keep menu open
	 */
	protected boolean onAbil() {
		inserts.awaitSelection(new SelectionListener() {
			@Override public boolean onSelection(Chara selected) {
				if (selected != null) {
					Screen nextMenu = new CharaInfoScreen(selected);
					MGlobal.assets.loadAsset(nextMenu, "abil menu");
					MGlobal.screens.push(nextMenu);
				}
				return true;
			}
		}, true);
		return false;
	}
	
	/**
	 * Called when the abilities option is selected from main menu.
	 * @return					False to keep menu open
	 */
	protected boolean onEquip() {
		inserts.awaitSelection(new SelectionListener() {
			@Override public boolean onSelection(Chara selected) {
				if (selected != null) {
					Screen nextMenu = new EquipScreen(selected);
					MGlobal.assets.loadAsset(nextMenu, "equip menu");
					MGlobal.screens.push(nextMenu);
				}
				return true;
			}
		}, true);
		return false;
	}
	
	/**
	 * Called when the abilities option is selected from main menu.
	 * @return					False to keep menu open
	 */
	protected boolean onItems() {
		Screen nextMenu = new InventoryScreen();
		MGlobal.assets.loadAsset(nextMenu, "items menu");
		MGlobal.screens.push(nextMenu);
		return false;
	}
	
	/**
	 * Called when the abilities option is selected from main menu.
	 * @return					False to keep menu open
	 */
	protected boolean onSave() {
		menu.unfocus();
		saveSelector.showAt(0, 0);
		return false;
	}
	
	/**
	 * Called when the order option is selected from the main menu.
	 * @return					False to keep menu open
	 */
	protected boolean onOrder() {
		Screen nextMenu = new OrderScreen();
		MGlobal.assets.loadAsset(nextMenu, "order menu");
		MGlobal.screens.push(nextMenu);
		return false;
	}
	
	/**
	 * Puts the main menu back in focus.
	 */
	protected void refocusMain() {
		if (containsChild(menu)) {
			menu.focus();
		} else {
			menu.showAt(0, 0);
		}
		if (containsChild(saveSelector)) {
			saveSelector.close();
		}
	}
	
	/**
	 * Creates the character inserts and other assorted items
	 */
	protected void createDisplay() {
		
		if (inserts != null) {
			removeUChild(inserts);
		}
		inserts = new CharaSelector(true, false);
		MGlobal.assets.loadAsset(inserts, "pause menu inserts");
		addUChild(inserts);
		
		FontHolder font = MGlobal.ui.getFont();
		insertsX = MGlobal.window.getViewportWidth()/2 - inserts.getWidth()/2;
		insertsY = GLOBAL_Y;
		inserts.setX(insertsX);
		inserts.setY(insertsY);

		info1 = "Floor: " + SGlobal.heroes.getLocation();
		info2 = "GP: " + SGlobal.heroes.getGP();
		format.align = HAlignment.LEFT;
		format.width = inserts.getWidth();
		format.height = INFO_HEIGHT;
		format.x = insertsX + INFO_MARGINS;
		format.y = (int) (insertsY + inserts.getHeight() + INFO_MARGINS +
				font.getLineHeight()*2  - inserts.getBorderHeight());
	}
	
}
