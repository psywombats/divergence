/**
 *  EntryInt.java
 *  Created on Nov 23, 2014 6:19:25 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.net.columns;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Column entry for an integer.
 */
public class EntryInt extends ColumnEntry<Integer> {

	/**
	 * Inherited constructor.
	 * @param	columnName		The name of column to enter in
	 * @param	value			The integer to set it to
	 */
	public EntryInt(String columnName, Integer value) {
		super(columnName, value);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.net.columns.ColumnEntry#set
	 * (java.sql.PreparedStatement, int)
	 */
	@Override
	public void set(PreparedStatement statement, int index) throws SQLException {
		statement.setInt(index, value);
	}

}
