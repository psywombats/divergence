/**
 *  MoveMDO.java
 *  Created on Dec 19, 2012 9:50:41 AM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema.characters.hero.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;
import net.wombatrpgs.mgns.core.Annotations.SchemaLink;
import net.wombatrpgs.mgns.core.HeadlessSchema;
import net.wombatrpgs.rainfallschema.characters.hero.moveset.data.MoveMDO;
import net.wombatrpgs.rainfallschema.io.data.InputCommand;

/**
 * A moveset entry is different from a move in that it encompasses a move and
 * an index into a moveset. That index is usually a hotkey, but it could be
 * something else, in theory, although this implementation is rather rigid.
 */
@ExcludeFromTree
public class MovesetEntryMDO extends HeadlessSchema {
	
	@Desc("Move data")
	@SchemaLink(MoveMDO.class)
	public String move;
	
	@Desc("Command - usually use ACTION_1, ACTION_2, etc, what those are " +
			"depends on the user's keymap")
	public InputCommand command;
	
}
