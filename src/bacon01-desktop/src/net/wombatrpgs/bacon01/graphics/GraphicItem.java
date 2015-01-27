/**
 *  GraphicItem.java
 *  Created on Jan 27, 2015 2:10:38 AM for project bacon01-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.bacon01.graphics;

import net.wombatrpgs.baconschema.rpg.ItemMDO;
import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.io.CommandMap;
import net.wombatrpgs.mgne.io.command.CMapMenu;
import net.wombatrpgs.mgne.maps.objects.Picture;
import net.wombatrpgs.mgne.screen.Screen;
import net.wombatrpgs.mgneschema.io.data.InputCommand;

/**
 *
 */
public class GraphicItem extends Picture implements CommandListener {
	
	protected ItemMDO mdo;
	protected CommandMap context;
	
	/**
	 * Creates a new graphic item to represent an item.
	 * @param mdo
	 */
	public GraphicItem(ItemMDO mdo) {
		super(mdo.graphic, 0);
		this.mdo = mdo;
		context = new CMapMenu();
	}

	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		if (command == InputCommand.UI_CONFIRM ||
				command == InputCommand.UI_CANCEL ||
				command == InputCommand.UI_FINISH) {
			fadeOut(.5f);
			parent.removeCommandListener(this);
			parent.removeCommandContext(context);
		}
		return true;
	}
	
	public void show() {
		Screen screen = MGlobal.screens.peek();
		x = screen.getWidth()/2;
		y = screen.getHeight()/2;
		fadeIn(screen, .5f);
		parent.pushCommandListener(this);
		parent.pushCommandContext(context);
	}

}
