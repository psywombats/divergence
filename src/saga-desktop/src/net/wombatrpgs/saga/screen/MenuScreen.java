/**
 *  MenuScreen.java
 *  Created on Mar 31, 2014 3:52:20 PM for project saga-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.saga.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.Option;
import net.wombatrpgs.mgne.ui.OptionSelector;
import net.wombatrpgs.mgne.ui.text.FontHolder;
import net.wombatrpgs.mgne.ui.text.TextBoxFormat;
import net.wombatrpgs.saga.CharacterInsert;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.Chara;

/**
 * Any menu that takes up an entire scren.
 */
public class MenuScreen extends Screen implements Disposable {
	
	protected static final int INSERTS_MARGIN = 4;
	protected static final int INSERTS_COUNT_HORIZ = 2;
	protected static final int INSERTS_COUNT_VERT = 3;
	protected static final int INFO_HEIGHT = 28;
	
	protected OptionSelector menu;
	protected List<CharacterInsert> inserts;
	protected Nineslice insertsBG;
	protected int insertsX, insertsY;
	
	protected FontHolder font;
	protected TextBoxFormat format;
	protected Nineslice infoBG;
	protected String info1, info2;
	protected int insertsWidth, insertsHeight;

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
		assets.add(menu);
		
		insertsBG = new Nineslice();
		assets.add(insertsBG);
		infoBG = new Nineslice();
		assets.add(infoBG);
		
		font = MGlobal.ui.getFont();
		format = new TextBoxFormat();
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		menu.showAt(0, 0);
		createDisplay();
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		insertsBG.renderAt(batch, insertsX, insertsY);
		infoBG.renderAt(batch, insertsX, insertsY + insertsBG.getHeight());
		for (CharacterInsert insert : inserts) {
			insert.render(batch);
		}
		font.draw(batch, format, info1, 0);
		font.draw(batch, format, info2, -(int) font.getLineHeight());
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#update(float)
	 */
	@Override
	public void update(float elapsed) {
		super.update(elapsed);
		for (CharacterInsert insert : inserts) {
			insert.update(elapsed);
		}
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
			insertsBG.resizeTo(insertsWidth, insertsHeight);
			infoBG.resizeTo(insertsWidth, INFO_HEIGHT);
		}
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		insertsBG.dispose();
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
	
	/**
	 * Creates the character inserts and other assorted items
	 */
	protected void createDisplay() {
		if (inserts == null) {
			inserts = new ArrayList<CharacterInsert>();
		} else {
			for (CharacterInsert insert : inserts) {
				assets.remove(insert);
			}
			inserts.clear();
		}
		CharacterInsert dummy = new CharacterInsert(SGlobal.heroes.getFrontMember(0));
		insertsWidth = dummy.getWidth();
		insertsHeight = dummy.getHeight();
		insertsWidth *= INSERTS_COUNT_HORIZ;
		insertsHeight *= INSERTS_COUNT_VERT;
		insertsWidth += 2 * INSERTS_MARGIN;
		insertsHeight += 2 * (INSERTS_MARGIN+1);
		insertsX = MGlobal.window.getViewportWidth()/2 - insertsWidth/2;
		insertsY = MGlobal.window.getViewportHeight()/2 - insertsHeight/2;
		int insertX = insertsX + INSERTS_MARGIN;
		int insertY = insertsY + insertsHeight - INSERTS_MARGIN - dummy.getHeight();
		boolean left = true;
		for (Chara hero : SGlobal.heroes.getAll()) {
			CharacterInsert insert = new CharacterInsert(hero);
			insert.setX(insertX);
			insert.setY(insertY);
			if (left) {
				insertX += insert.getWidth();
			} else {
				insertX -= insert.getWidth();
				insertY -= insert.getHeight();
			}
			left = !left;
			inserts.add(insert);
			assets.add(insert);
		}
		
		info1 = "Floor: " + SGlobal.heroes.getLocation();
		info2 = "GP: " + SGlobal.heroes.getGP();
		format.align = HAlignment.LEFT;
		format.width = insertsWidth;
		format.height = INFO_HEIGHT;
		format.x = insertsX + INSERTS_MARGIN+2;
		format.y = (int) (insertsY + insertsHeight + INSERTS_MARGIN+2 + font.getLineHeight()*2);
	}
	
}
