/**
 *  MenuScreen.java
 *  Created on Mar 31, 2014 3:52:20 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.Option;
import net.wombatrpgs.mgne.ui.OptionSelector;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextBoxFormat;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.Chara;
import net.wombatrpgs.saga.ui.CharaSelector;
import net.wombatrpgs.saga.ui.CharaSelector.SelectionListener;

/**
 * Any menu that takes up an entire scren.
 */
public class MenuScreen extends Screen implements Disposable {
	
	protected static final int INFO_HEIGHT = 28;
	protected static final int INFO_MARGINS = 6;
	
	protected OptionSelector menu, saveSelector;
	
	protected CharaSelector inserts;
	protected TextBoxFormat format;
	protected Nineslice infoBG;
	protected String info1, info2;
	protected int insertsX, insertsY;
	
	protected boolean silentAdd;

	/**
	 * Creates a new menu screen with the main menu in it.
	 */
	public MenuScreen() {
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
		inserts = new CharaSelector(true);
		assets.add(inserts);
		addUChild(inserts);
		
		format = new TextBoxFormat();
		
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
	 * (com.badlogic.gdx.assets.AssetManager, int)
	 */
	@Override
	public void postProcessing(AssetManager manager, int pass) {
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
				inserts.unfocus();
				if (selected != null) {
					Screen nextMenu = new CharaInfoScreen(selected);
					MGlobal.assets.loadAsset(nextMenu, "abil menu");
					MGlobal.screens.push(nextMenu);
				}
				return false;
			}
		}, true);
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
		menu.unfocus();
		saveSelector.showAt(0, 0);
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
		FontHolder font = MGlobal.ui.getFont();
		insertsX = MGlobal.window.getViewportWidth()/2 - inserts.getWidth()/2;
		insertsY = MGlobal.window.getViewportHeight()/2 - inserts.getHeight()/2;
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
