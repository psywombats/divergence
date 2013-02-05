/**
 *  DataLoaderTest.java
 *  Created on Nov 8, 2012 3:03:51 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.tests;

import java.util.List;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.rainfall.core.DataLoader;
import net.wombatrpgs.rainfall.core.DirectoryDataLoader;

/**
 * Make sure the default directory data loader thing is working properly!
 */
public class DataLoaderTest {

	/**
	 * @param 	args	Unused
	 */
	public static void main(String[] args) {
		
		Global.setupGlobalForTesting();
		
		DataLoader loader = new DirectoryDataLoader();
		List<MainSchema> schema = loader.loadData("res/data");
		Global.data.addData(schema);
	}

}
