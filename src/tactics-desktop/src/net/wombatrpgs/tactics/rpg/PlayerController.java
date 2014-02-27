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
import net.wombatrpgs.tactics.ui.DirectionSelector.DirListener;
import net.wombatrpgs.tacticsschema.rpg.PlayerUnitMDO;
import net.wombatrpgs.tacticsschema.rpg.abil.data.ProjectorType;

/**
 * Any unit controlled by the player.
 */
public class PlayerController extends TacticsController {
	
	protected static final String VOCAB_MOVE = "Move";
	protected static final String VOCAB_STAY = "Stay";
	protected static final String VOCAB_ACT = "Act";
	protected static final String VOCAB_WAIT = "Wait";
	
	protected PlayerUnitMDO mdo;
	protected int energySpentThisTurn;
	
	protected OptionSelector currentMenu;

	/**
	 * Constructs a player unit for a player. This should be only constructed
	 * during initialization, basically, and then assigned to battles as
	 * dictated by the player.
	 * @param	mdo				The mdo to construct from
	 */
	protected PlayerController(PlayerUnitMDO mdo) {
		super(mdo);
		this.mdo = mdo;
	}

	/**
	 * @see net.wombatrpgs.tactics.rpg.TacticsController#internalStartTurn()
	 */
	@Override
	public void internalStartTurn() {
		// we should be receiving commands about now
		energySpentThisTurn = 0;
		showMoveMenu();
	}

	/**
	 * @see net.wombatrpgs.tactics.rpg.TacticsController#doneWithTurn()
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
	 * @see net.wombatrpgs.tactics.rpg.Ability.AbilityFinishListener#onAbilityEnd(int)
	 */
	@Override
	public void onAbilityEnd(int energySpent) {
		if (energySpent >= 0) {
			state = TurnState.TERMINATE;
			energySpentThisTurn += energySpent;
			currentMenu.close();
		} else {
			state = TurnState.AWAIT_ACTION;
			currentMenu.focus();
		}
	}

	/**
	 * @see net.wombatrpgs.tactics.rpg.TacticsController#acquireTargets
	 * (net.wombatrpgs.tactics.rpg.TacticsController.AcquiredListener, int,
	 * net.wombatrpgs.tacticsschema.rpg.abil.data.ProjectorType)
	 */
	@Override
	public void acquireTargets(final AcquiredListener listener, int range, ProjectorType projector) {
		TGlobal.ui.getDirectionSelector().requestOrthoDir(new DirListener<OrthoDir>() {
			@Override public void onSelect(OrthoDir dir) {
				List<TacticsController> victims = new ArrayList<TacticsController>();
				int tileX = (int) (event.getTileX() + dir.getVector().x);
				int tileY = (int) (event.getTileY() + dir.getVector().y);
				TacticsController victim = battle.getMap().getUnitAt(tileX, tileY);
				if (victim != null) {
					victims.add(victim);
				}
				listener.onAcquired(victims);
			}
		});
	}

	/**
	 * @see net.wombatrpgs.tactics.rpg.TacticsController#onCommand
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
				currentMenu.close();
				showActionMenu();
			}	
		});
		if (moved) {
			state = TurnState.ANIMATE_MOVEMENT;
			energySpentThisTurn += 500;
			battle.getMap().clearHighlight();
			battle.getMap().hideCursor();
		}
		return moved;
	}
	
	/**
	 * Displays the move/stay options menu.
	 */
	protected void showMoveMenu() {
		List<Option> options = new ArrayList<Option>();
		options.add(new Option(VOCAB_MOVE) {
			@Override public boolean onSelect() {
				onMoveSelected();
				return true;
			}
		});
		options.add(new Option(VOCAB_STAY) {
			@Override public boolean onSelect() {
				onStaySelected();
				return true;
			}
		});
		currentMenu = new OptionSelector(options);
		currentMenu.loadAssets();
		currentMenu.showAt(0, 0);
	}
	
	/**
	 * Called when Move is selected from the initial menu selector.
	 */
	protected void onMoveSelected() {
		battle.getMap().highlightMovement(this);
		battle.getMap().showCursor(event.getTileX(), event.getTileY());
		currentMenu.unfocus();
	}
	
	/**
	 * Called when Wait is selected from the initial menu selector.
	 */
	protected void onStaySelected() {
		energySpentThisTurn += 250;
		currentMenu.close();
		showActionMenu();
	}
	
	/**
	 * Displays the attack/wait options menu.
	 */
	protected void showActionMenu() {
		state = TurnState.AWAIT_ACTION;
		List<Option> options = new ArrayList<Option>();
		options.add(new Option(VOCAB_ACT) {
			@Override public boolean onSelect() {
				onAttackSelected();
				return true;
			}
		});
		options.add(new Option(VOCAB_WAIT) {
			@Override public boolean onSelect() {
				onWaitSelected();
				return true;
			}
		});
		currentMenu = new OptionSelector(options);
		currentMenu.loadAssets();
		currentMenu.showAt(0, 0);
	}
	
	/**
	 * Called when attack is selected from attack/wait.
	 */
	protected void onAttackSelected() {
		currentMenu.close();
		List<Option> options = new ArrayList<Option>();
		final TacticsController parent = this;
		for (final Ability abil : unit.getAbilities()) {
			options.add(new Option(abil.getName()) {
				@Override public boolean onSelect() {
					state = TurnState.ANIMATE_ACTION;
					currentMenu.unfocus();
					abil.onUse(parent);
					return false;
				}
			});
		}
		currentMenu = new OptionSelector(options);
		currentMenu.loadAssets();
		currentMenu.showAt(0, 0);
	}
	
	/**
	 * Called when wait is selected from the attack/wait.
	 */
	protected void onWaitSelected() {
		energySpentThisTurn += 250;
		currentMenu.close();
		state = TurnState.TERMINATE;
	}

}
