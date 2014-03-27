/**
 *  Schema.java
 *  Created on Oct 19, 2012 7:45:13 PM for project MGNSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgns.core;

import java.io.Serializable;

import net.wombatrpgs.mgns.core.Annotations.ExcludeFromTree;

/**
 * Finally! The base of all schema, both in-line and editable. I guess things
 * can go here if they really need to. Now this is serializable for the MDOs
 * that get packed into savefiles.
 */
@ExcludeFromTree
public class Schema implements Serializable {

	private static final long serialVersionUID = 1L;

}
