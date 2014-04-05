/**
 *  BaseMDO.java
 *  Created on Aug 4, 2012 7:20:11 PM for project MGNSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgns.core;

import net.wombatrpgs.mgns.core.Annotations.*;

/**
 * The class from which every schema extends. Designed with a few simple
 * elements that all schema should have.
 */
@ExcludeFromTree
public class MainSchema extends Schema implements Comparable<MainSchema> {
	
	@DisplayName("Key")
	@Desc("The key of this object used in code -- don't change these!")
	@Header
	@Immutable
	public String key;
	
	@DisplayName("Subfolder")
	@Desc("Used to subcategorize objects (\"/bosses\" or \"/areas/towns\" for instance)")
	@Header
	@Immutable
	public String subfolder;
	
	@DisplayName("Display Name")
	@Desc("The display name of this object, used only by humans like you")
	@Header
	public String name;
	
	@DisplayName("Description")
	@Desc("Notes and whatnot on this object")
	@Header
	public String description;

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MainSchema other) {
		return key.compareTo(other.key);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (key != null) return key + "(" + super.toString() + ")";
		else return super.toString();
	}

}
