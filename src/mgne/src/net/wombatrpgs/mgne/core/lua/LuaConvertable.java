/**
 *  LuaConvertable.java
 *  Created on Jan 29, 2014 3:06:05 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.core.lua;

import org.luaj.vm2.LuaValue;

/**
 * Indicates that the selected class can be turned into a Lua object and used as
 * the caller context on Lua script evaluations.
 */
public interface LuaConvertable {
	
	/**
	 * Convert this class to Lua. Note that this should not perform the actual
	 * computation every time, just retrieve it. And the "computation" should
	 * just be creating a Lua object with functions that map to internal class
	 * methods.
	 * @return					This class as a lua value
	 */
	public LuaValue toLua();

}
