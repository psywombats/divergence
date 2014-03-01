/**
 *  DirectionSelector.java
 *  Created on Feb 26, 2014 9:13:33 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;

import net.wombatrpgs.mgne.graphics.Graphic;
import net.wombatrpgs.mgne.io.CommandListener;
import net.wombatrpgs.mgne.maps.events.MapEvent;
import net.wombatrpgs.mgne.ui.UIElement;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.maps.data.DirEnum;
import net.wombatrpgs.mgneschema.maps.data.EightDir;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;
import net.wombatrpgs.tactics.core.TGlobal;
import net.wombatrpgs.tactics.io.CMapDirSelect;
import net.wombatrpgs.tacticsschema.ui.DirectionSelectorMDO;

/**
 * That target thing that asks you for cardinal directions.
 */
public class DirectionSelector extends UIElement implements CommandListener {
	
	protected DirectionSelectorMDO mdo;
	protected Graphic orthoTarget;
	protected Graphic eightTarget;
	
	protected boolean orthoActive;
	protected boolean eightActive;
	protected DirListener<OrthoDir> orthoListener;
	protected DirListener<EightDir> eightListener;
	
	/**
	 * Creates a new target from data.
	 * @param	mdo				The data to use
	 */
	public DirectionSelector(DirectionSelectorMDO mdo) {
		this.mdo = mdo;
		orthoTarget = new Graphic(mdo.orthoDir);
		eightTarget = new Graphic(mdo.eightTarget);
		assets.add(orthoTarget);
		assets.add(eightTarget);
	}
	
	/**
	 * @see net.wombatrpgs.mgne.io.CommandListener#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		EightDir selected;
		switch (command) {
		case MOVE_DOWN:		selected = EightDir.SOUTH;		break;
		case MOVE_LEFT:		selected = EightDir.WEST;		break;
		case MOVE_RIGHT:	selected = EightDir.EAST;		break;
		case MOVE_UP:		selected = EightDir.NORTH;		break;
		default:			selected = null;				break;
		}
		if (orthoActive) {
			orthoListener.onSelect(selected.toOrtho());
		} else {
			eightListener.onSelect(selected);
		}
		TGlobal.screen.removeObject(this);
		TGlobal.screen.removeCommandListener(this);
		TGlobal.screen.popCommandContext();
		return true;
	}
	
	/**
	 * @see net.wombatrpgs.mgne.screen.ScreenObject#render
	 * (com.badlogic.gdx.graphics.OrthographicCamera)
	 */
	@Override
	public void render(OrthographicCamera camera) {
		super.render(camera);
		MapEvent target = TGlobal.screen.getBattle().getActor().getEvent();
		int renderOffX = target.getAppearance().getWidth() / 2;
		int renderOffY = target.getAppearance().getHeight() / 2;
		renderOffX -= orthoTarget.getWidth() / 2;
		renderOffY -= orthoTarget.getHeight() / 2;
		target.getParent().getBatch().begin();
		if (orthoActive) {
			target.renderLocal(camera, orthoTarget.getGraphic(),
					renderOffX, renderOffY, 0);
		}
		if (eightActive) {
			target.renderLocal(camera, eightTarget.getGraphic(),
					renderOffX, renderOffY, 0);
		}
		target.getParent().getBatch().end();
	}

	/**
	 * Requests a directional input from the player and then calls the listener
	 * when the player responds. There's no return because the callback will
	 * receive the actual value. It's too bad this has to be asynchronous.
	 * @param	listener		The callback for when player is done
	 */
	public void requestEightDir(DirListener<EightDir> listener) {
		this.eightListener = listener;
		activate(false);
	}
	
	/**
	 * Requests a directional input from the player and then calls the listener
	 * when the player responds. There's no return because the callback will
	 * receive the actual value. It's too bad this has to be asynchronous.
	 * @param	listener		The callback for when player is done
	 */
	public void requestOrthoDir(DirListener<OrthoDir> listener) {
		this.orthoListener = listener;
		activate(true);
	}
	
	/**
	 * Starts showing graphic and receiving input from player.
	 * @param	orthoMode		True for ortho mode, false for 8dir mode
	 */
	protected void activate(boolean orthoMode) {
		orthoActive = orthoMode;
		eightActive = !orthoMode;
		TGlobal.screen.addObject(this);
		TGlobal.screen.pushCommandContext(new CMapDirSelect());
		TGlobal.screen.pushCommandListener(this);
	}
	
	/**
	 * Callback for using the selector. Used for both ortho and eightdir.
	 */
	public interface DirListener<T extends DirEnum> {
		
		/**
		 * Called when the player finally selected a direction.
		 * @param	dir				The direction the player input, or null if
		 * 							they cancelled instead.
		 */
		public void onSelect(T dir);
	}

}

