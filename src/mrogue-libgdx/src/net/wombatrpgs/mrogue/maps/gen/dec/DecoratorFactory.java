/**
 *  DecoratorFactory.java
 *  Created on Oct 13, 2013 6:26:17 AM for project mrogue-libgdx
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mrogue.maps.gen.dec;

import net.wombatrpgs.mrogue.core.MGlobal;
import net.wombatrpgs.mrogue.maps.gen.MapGenerator;
import net.wombatrpgs.mrogueschema.maps.decorators.Decorator1x1MDO;
import net.wombatrpgs.mrogueschema.maps.decorators.Decorator1x2MDO;
import net.wombatrpgs.mrogueschema.maps.decorators.Decorator1x2SpecialMDO;
import net.wombatrpgs.mrogueschema.maps.decorators.Decorator1x3SpecialMDO;
import net.wombatrpgs.mrogueschema.maps.decorators.Decorator2x1MDO;
import net.wombatrpgs.mrogueschema.maps.decorators.Decorator2x2MDO;
import net.wombatrpgs.mrogueschema.maps.decorators.Decorator2x2SpecialMDO;
import net.wombatrpgs.mrogueschema.maps.decorators.Decorator3x1MDO;
import net.wombatrpgs.mrogueschema.maps.decorators.Decorator3x2MDO;
import net.wombatrpgs.mrogueschema.maps.decorators.Decorator3x3MDO;
import net.wombatrpgs.mrogueschema.maps.decorators.DecoratorSetMDO;
import net.wombatrpgs.mrogueschema.maps.decorators.data.DecoratorMDO;

/**
 * Spits out decorators when fed MDOs.
 */
public class DecoratorFactory {
	
	/**
	 * Creates a decorator based on stored data with the given key.
	 * @param	key				The key to look up in the database
	 * @param	gen				The generator to create the decorator for
	 * @return					The decorator we created
	 */
	public static Decorator createDecor(String key, MapGenerator gen) {
		DecoratorMDO mdo = MGlobal.data.getEntryFor(key, DecoratorMDO.class);
		return createDecor(mdo, gen);
	}
	
	/**
	 * Creates a decorator based on MDO class.
	 * @param	mdo				The data to generate from
	 * @param	gen				The generator to generate for
	 * @return					The created decorator
	 */
	public static Decorator createDecor(DecoratorMDO mdo, MapGenerator gen) {
		if (DecoratorSetMDO.class.isAssignableFrom(mdo.getClass())) {
			return new DecoratorSet((DecoratorSetMDO) mdo, gen);
		} else if (Decorator3x1MDO.class.isAssignableFrom(mdo.getClass())) {
			return new Decorator3x1((Decorator3x1MDO) mdo, gen);
		} else if (Decorator3x2MDO.class.isAssignableFrom(mdo.getClass())) {
			return new Decorator3x2((Decorator3x2MDO) mdo, gen);
		} else if (Decorator1x2SpecialMDO.class.isAssignableFrom(mdo.getClass())) {
			return new Decorator1x2Special((Decorator1x2SpecialMDO) mdo, gen);
		} else if (Decorator1x1MDO.class.isAssignableFrom(mdo.getClass())) {
			return new Decorator1x1((Decorator1x1MDO) mdo, gen);
		} else if (Decorator3x3MDO.class.isAssignableFrom(mdo.getClass())) {
			return new Decorator3x3((Decorator3x3MDO) mdo, gen);
		} else if (Decorator2x2MDO.class.isAssignableFrom(mdo.getClass())) {
			return new Decorator2x2((Decorator2x2MDO) mdo, gen);
		} else if (Decorator1x2MDO.class.isAssignableFrom(mdo.getClass())) {
			return new Decorator1x2((Decorator1x2MDO) mdo, gen);
		} else if (Decorator2x1MDO.class.isAssignableFrom(mdo.getClass())) {
			return new Decorator2x1((Decorator2x1MDO) mdo, gen);
		} else if (Decorator1x3SpecialMDO.class.isAssignableFrom(mdo.getClass())) {
			return new Decorator1x3Special((Decorator1x3SpecialMDO) mdo, gen);
		} else if (Decorator2x2SpecialMDO.class.isAssignableFrom(mdo.getClass())) {
			return new Decorator2x2Special((Decorator2x2SpecialMDO) mdo, gen);
		}
		MGlobal.reporter.warn("Unknown decorator mdo: " + mdo);
		return null;
	}

}
