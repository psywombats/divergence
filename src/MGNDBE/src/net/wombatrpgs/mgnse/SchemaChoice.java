/**
 *  SchemaChoice.java
 *  Created on Aug 18, 2012 1:28:00 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse;

import net.wombatrpgs.mgns.core.MainSchema;

/**
 * God damn I hate inner classes but this is necessary for sorting. It's a
 * struct. It's also formatted like shit I'm sorry okay?
 */
public class SchemaChoice {
	public Class<? extends MainSchema> schema;
	public String name;
	public SchemaChoice(Class<? extends MainSchema> schema, String name) {
		this.name = name;
		this.schema = schema;
	}
	@Override public String toString() { return name; }
}
