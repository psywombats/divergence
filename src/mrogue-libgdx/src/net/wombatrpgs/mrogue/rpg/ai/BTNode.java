package net.wombatrpgs.mrogue.rpg.ai;

import net.wombatrpgs.mrogue.core.Updateable;
import net.wombatrpgs.mrogue.rpg.CharacterEvent;

/**
 * Classic behavior trees. Who've thought, a roguelike with formal AI!
 */
public abstract class BTNode implements Updateable {
	
	protected CharacterEvent actor;
	
	/**
	 * Sets up a new BTNode for a specific actor.
	 * @param	actor			The character that will be taking action
	 */
	public BTNode(CharacterEvent actor) {
		this.actor = actor;
	}

	/**
	 * @see net.wombatrpgs.mrogue.core.Updateable#update(float)
	 */
	@Override
	public void update(float elapsed) {
		// default is nothing
	}
	
	/**
	 * Resets this node if it was once ruuning but now isn't. Usually shouldn't
	 * need to do anything unless state was involved.
	 */
	public abstract void reset();
	
	/**
	 * Report your status and do your action! These things are condensed so that
	 * the traversal doesn't have to be performed twice.
	 * @return					The status of this node: SUCCESS if it finished
	 * 							acting etc, FAILURE if no action was found, or
	 * 							RUNNING if an action was selected.
	 */
	public abstract BTState getStatusAndAct();

}
