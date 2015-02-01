/**
 *  GraphicItem.java
 *  Created on Jan 27, 2015 2:10:38 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.graphics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;

import net.wombatrpgs.bacon01.rpg.InventoryItem;
import net.wombatrpgs.baconschema.rpg.NotesSetMDO;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.io.CommandMap;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.maps.objects.Picture;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgne.screen.ScreenObject;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 *
 */
public class ItemGraphicDisplayer extends ScreenObject implements CommandListener {
	
	protected NotesSetMDO mdo;
	protected CommandMap context;
	protected List<Picture> itemPics;
	protected Screen parent;
	protected Picture currentPic;
	protected FinishListener listener;
	protected Picture right, left;
	protected int currentIndex;
	
	public ItemGraphicDisplayer(InventoryItem item) {
		mdo = MGlobal.data.getEntryFor(item.getNotesKey(), NotesSetMDO.class);
		currentIndex = item.getQuantity() - 1;
		context = new CMapMenu();
		
		itemPics = new ArrayList<Picture>();
		for (int i = 0; i < item.getQuantity(); i += 1) {
			Picture pic = new Picture(mdo.notes[i].graphic, 0);
			assets.add(pic);
			itemPics.add(pic);
		}
		
		right = new Picture("arrow_right.png", 0);
		left = new Picture("arrow_left.png", 0);
		assets.add(right);
		assets.add(left);
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getWidth()
	 */
	@Override
	public int getWidth() {
		return MGlobal.screens.peek().getWidth();
	}

	/**
	 * @see net.wombatrpgs.mgne.graphics.interfaces.Boundable#getHeight()
	 */
	@Override
	public int getHeight() {
		return MGlobal.screens.peek().getHeight();
	}

	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		switch (command) {
		case UI_CANCEL: case UI_FINISH:
			hide();
			break;
		case MOVE_LEFT:
			currentIndex -= 1;
			updatePic();
			break;
		case MOVE_RIGHT:
			currentIndex += 1;
			updatePic();
			break;
		default:
			return true;
		}
		
		return true;
	}
	
	public void show(FinishListener listener) {
		this.listener = listener;
		
		updateColor();
		
		parent = MGlobal.screens.peek();
		parent.pushCommandListener(this);
		parent.pushCommandContext(context);
		parent.addChild(this);
		
		parent.addChild(right);
		parent.addChild(left);
		
		right.setX(parent.getWidth() / 2);
		right.setY(parent.getHeight() / 2);
		left.setX(parent.getWidth() / 2);
		left.setY(parent.getHeight() / 2);
		for (Picture pic : itemPics) {
			pic.setX(parent.getWidth() / 2);
			pic.setY(parent.getHeight() / 2);
		}
		
		currentPic = itemPics.get(currentIndex);
		currentPic.fadeIn(parent, .2f);
	}
	
	public void hide() {
		currentPic.fadeOut(.5f);
		parent.removeCommandListener(this);
		parent.removeCommandContext(context);
		parent.removeChild(this);
		listener.onFinish();
		parent.removeChild(left);
		parent.removeChild(right);
	}
	
	protected void updatePic() {
		if (currentIndex < 0) currentIndex = 0;
		if (currentIndex >= itemPics.size()) currentIndex = itemPics.size() - 1;
		
		if (itemPics.size() == 1) return;
		
		currentPic.fadeOut(.1f);
		currentPic = itemPics.get(currentIndex);
		currentPic.fadeIn(parent, .1f);
		MGlobal.audio.playSFX("swap");
		
		updateColor();
	}
	
	protected void updateColor() {
		if (currentIndex > 0) {
			left.setColor(new Color(1, 1, 1, 1));
		} else {
			left.setColor(new Color(1, 1, 1, 0));
		}
		if (currentIndex < itemPics.size()-1) {
			right.setColor(new Color(1, 1, 1, 1));
		} else {
			right.setColor(new Color(1, 1, 1, 0));
		}
	}

}
