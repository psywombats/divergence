/**
 *  HeroUnit.java
 *  Created on Oct 11, 2013 5:51:14 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.rpg;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogueschema.characters.data.CharacterMDO;

/**
 * Overrides the hero unit because heroes are **special**.
 */
public class HeroUnit extends GameUnit {

	/**
	 * Constructs the hero unit.
	 * @param	mdo				The character data to make from
	 * @param	hero			That's us!
	 */
	public HeroUnit(CharacterMDO mdo, Hero hero) {
		super(mdo, hero);
		setName(MGlobal.levelManager.getHeroName());
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.GameUnit#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see net.wombatrpgs.mrogue.rpg.GameUnit#die()
	 */
	@Override
	public void die() {
		
	}

}
