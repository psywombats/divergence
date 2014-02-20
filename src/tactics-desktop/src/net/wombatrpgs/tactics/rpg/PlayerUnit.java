/**
 *  PlayerUnit.java
 *  Created on Feb 12, 2014 8:33:00 PM for project tactics-desktop
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.tactics.rpg;

import java.util.ArrayList;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.core.interfaces.FinishListener;
import net.wombatrpgs.mgne.ui.Option;
import net.wombatrpgs.mgne.ui.OptionSelector;
import net.wombatrpgs.mgneschema.io.data.InputCommand;
import net.wombatrpgs.mgneschema.maps.data.OrthoDir;
import net.wombatrpgs.tactics.core.TGlobal;
import net.wombatrpgs.tacticsschema.rpg.PlayerUnitMDO;

/**
 * Any unit controlled by the player.
 */
public class PlayerUnit extends GameUnit {
	
	protected static final String VOCAB_MOVE = "Move";
	protected static final String VOCAB_WAIT = "Wait";
	
	protected PlayerUnitMDO mdo;
	protected int energySpentThisTurn;
	
	protected OptionSelector mainTurnMenu;

	/**
	 * Constructs a player unit for a player. This should be only constructed
	 * during initialization, basically, and then assigned to battles as
	 * dictated by the player.
	 * @param	mdo				The mdo to construct from
	 */
	protected PlayerUnit(PlayerUnitMDO mdo) {
		super(mdo);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.tactics.rpg.GameUnit#internalStartTurn()
	 */
	@Override
	public void internalStartTurn() {
		// we should be receiving commands about now
		energySpentThisTurn = 0;
		
		List<Option> options = new ArrayList<Option>();
		options.add(new Option(VOCAB_MOVE) {
			@Override public boolean onSelect() {
				onMoveSelected();
				return true;
			}
		});
		options.add(new Option(VOCAB_WAIT) {
			@Override public boolean onSelect() {
				onWaitSelected();
				return true;
			}
		});
		mainTurnMenu = new OptionSelector(options);
		mainTurnMenu.loadAssets();
		mainTurnMenu.showAt(0, 0);
	}

	/**
	 * @see net.wombatrpgs.tactics.rpg.GameUnit#doneWithTurn()
	 */
	@Override
	public int doneWithTurn() {
		if (state == TurnState.TERMINATE) {
			state = TurnState.AWAIT_TURN;
			return energySpentThisTurn;
		} else {
			return -1;
		}
	}

	/**
	 * @see net.wombatrpgs.tactics.rpg.GameUnit#onCommand
	 * (net.wombatrpgs.mgneschema.io.data.InputCommand)
	 */
	@Override
	public boolean onCommand(InputCommand command) {
		switch (state) {
		case AWAIT_MOVEMENT:
			switch(command) {
			case MOVE_DOWN:		return moveCursor(OrthoDir.SOUTH);
			case MOVE_LEFT:		return moveCursor(OrthoDir.WEST);
			case MOVE_RIGHT:	return moveCursor(OrthoDir.EAST);
			case MOVE_UP:		return moveCursor(OrthoDir.NORTH);
			case UI_CONFIRM:	return attemptFollowCursor();
			default:			return false;
			}
		default:
			MGlobal.reporter.warn("Unknown turn state: " + state);
			return false;
		}
	}
	
	/**
	 * Moves the cursor in some direction.
	 * @param	dir				The direction to move the cursor in
	 * @return					True...
	 */
	protected boolean moveCursor(OrthoDir dir) {
		TGlobal.ui.getCursor().move(dir);
		return true;
	}
	
	/**
	 * Attempts to move to where the cursor is as part of our turn. Fails if
	 * there is no path to the cursor.
	 * @return					True if there was a path, false otherwise
	 */
	protected boolean attemptFollowCursor() {
		boolean moved = event.attemptFollowCursor(new FinishListener() {
			@Override public void onFinish() {
				// TODO: tactics: perform actions
				mainTurnMenu.close();
				state = TurnState.TERMINATE;
				energySpentThisTurn = 1000;
			}	
		});
		if (moved) {
			state = TurnState.ANIMATE_MOVEMENT;
			battle.getMap().clearHighlight();
			battle.getMap().hideCursor();
		}
		return moved;
	}
	
	/**
	 * Called when Move is selected from the initial menu selector.
	 */
	protected void onMoveSelected() {
		battle.getMap().highlightMovement(this);
		battle.getMap().showCursor(event.getTileX(), event.getTileY());
		mainTurnMenu.unhandControl();
	}
	
	/**
	 * Called when Wait is selected from the initial menu selector.
	 */
	protected void onWaitSelected() {
		state = TurnState.TERMINATE;
		// TODO: tactics: wait
		energySpentThisTurn = 500;
		mainTurnMenu.close();
	}

}
