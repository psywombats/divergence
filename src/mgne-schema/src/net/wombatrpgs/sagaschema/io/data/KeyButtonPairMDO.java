/**
 *  KeyCommandPairMDO.java
 *  Created on Jan 21, 2014 11:29:05 PM for project saga-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.sagaschema.io.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * A mapping between a single key and a single command.
 */
@ExcludeFromTree
public class KeyButtonPairMDO extends HeadlessSchema {
	
	@Desc("Keycode - from libgdx's input.keys static stuff, use a wizard for this")
	public GdxKey keyCode;
	
	@Desc("Input button - the virtual button that fires on that key")
	public InputButton button;

}
