/**
 *  SpecialCharacterMDO.java
 *  Created on Apr 12, 2014 3:58:34 AM for project mgne-schema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgneschema.settings.data;

import net.wombatrpgs.mgns.core.Annotations.Desc;
import net.wombatrpgs.mgns.core.HeadlessSchema;

/**
 * $A to ascii 233 etc.
 */
public class SpecialCharacterMDO extends HeadlessSchema {
	
	@Desc("Code")
	public String code;
	
	@Desc("ASCII")
	public Integer ascii;

}
