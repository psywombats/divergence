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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.wombatrpgs.mgne.graphics.interfaces.Disposable;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.ui.Nineslice;
import net.wombatrpgs.mgne.ui.Option;
import net.wombatrpgs.mgne.ui.OptionSelector;
import net.wombatrpgs.saga.CharacterInsert;
import net.wombatrpgs.saga.core.SGlobal;
import net.wombatrpgs.saga.rpg.Chara;

/**
 * Any menu that takes up an entire scren.
 */
public class MenuScreen extends Screen implements Disposable {
	
	protected static final int INSERTS_START_X = 120;
	protected static final int INSERTS_START_Y = 120;
	protected static final int INSERTS_MARGIN = 4;
	protected static final int INSERTS_COUNT_HORIZ = 2;
	protected static final int INSERTS_COUNT_VERT = 3;
	
	protected OptionSelector menu;
	protected List<CharacterInsert> inserts;
	protected Nineslice insertsBG;
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
	}

	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#onFocusGained()
	 */
	@Override
	public void onFocusGained() {
		super.onFocusGained();
		menu.showAt(0, 0);
		createInserts();
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.Screen#render
	 * (com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		insertsBG.renderAt(batch, INSERTS_START_X, INSERTS_START_Y);
		for (CharacterInsert insert : inserts) {
			insert.render(batch);
		}
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
			createInserts();
			if (inserts.size() > 0) {
				insertsBG.resizeTo(insertsWidth, insertsHeight);
			}
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
	 * Creates the character inserts.
	 */
	protected void createInserts() {
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
		int insertX = INSERTS_START_X + INSERTS_MARGIN;
		int insertY = INSERTS_START_Y + insertsHeight - INSERTS_MARGIN - dummy.getHeight();
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
	}
	
}
