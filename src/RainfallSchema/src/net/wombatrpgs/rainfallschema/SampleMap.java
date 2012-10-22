/**
 *  Test1MDO.java
 *  Created on Aug 4, 2012 9:38:51 PM for project RainfallSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.rainfallschema;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgns.core.Annotations.*;

/**
 * Just testin'
 */
@Path("sample/")
public class SampleMap extends MainSchema {
	
	public enum MapType { 
		TYPE_ONE, TYPE_TWO
	}
	
	@DisplayName("File")
	@Desc("Theee file with the map data? (test)")
	@Nullable(true)
	@FileLink("maps")
	public String herpaderp;
	
	@DisplayName("Map Type")
	@Desc("This is just an enum test")
	public MapType mapType;
	
	@DisplayName("Sample Linkz")
	@Desc("some reference list idgaf")
	@InlineSchema(Test1MDO.class)
	public Test1MDO[] links;

}
