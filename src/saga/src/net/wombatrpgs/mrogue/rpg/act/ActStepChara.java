/**
 *  ActStepChara.java
 *  Created on Oct 16, 2013 3:21:52 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg.act;

import net.wombatrpgs.mrogue.rpg.CharacterEvent;

/**
 * Steps towards a character.
 */
public class ActStepChara extends Action {
	
	protected ActStep step;
	protected CharacterEvent other;
	
	/**
	 * Creates a step with no particular target.
	 * @param	actor			The chara that will be acting
	 */
	public ActStepChara(CharacterEvent actor) {
		super(actor);
		step = new ActStep(actor);
	}
	
	/**
	 * Constructs a new act step specifically for two characters.
	 * @param	parent			The chara that will be acting
	 * @param	other			The chara to aim for
	 */
	public ActStepChara(CharacterEvent actor, CharacterEvent other) {
		this(actor);
		this.other = other;
	}
	
	/**
	 * Sets the target to another character.
	 * @param	target			The new charac to target
	 */
	public void setTarget(CharacterEvent target) {
		this.other = target;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.act.Action#act()
	 */
	@Override
	public void act() {
		step.setDirection(actor.directionTo(other));
		step.act();
	}

}
