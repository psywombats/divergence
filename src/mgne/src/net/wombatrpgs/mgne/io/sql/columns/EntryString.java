/**
 *  EntryString.java
 *  Created on Nov 23, 2014 6:24:28 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.sql.columns;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * String wrapper for column entry.
 */
public class EntryString extends ColumnEntry<String> {

	/**
	 * Inherited constructor.
	 * @param	columnName		The name of the column to set
	 * @param	value			The value to set it to
	 */
	public EntryString(String columnName, String value) {
		super(columnName, value);
	}

	/**
	 * @see net.wombatrpgs.mgne.io.sql.columns.ColumnEntry#set
	 * (java.sql.PreparedStatement, int)
	 */
	@Override
	public void set(PreparedStatement statement, int index) throws SQLException {
		statement.setString(index, value);
	}

}
