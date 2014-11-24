/**
 *  ColumnEntry.java
 *  Created on Nov 23, 2014 6:15:12 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.sql.columns;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Struct for prepared statements that aren't actually all that prepared. Can
 * wrap one of several value types.
 * @param	<T>			The type of the column that will be entered 
 */
public abstract class ColumnEntry<T> {
	
	protected String columnName;
	protected T value;
	
	/**
	 * Creates a new entry for a column with a given value.
	 * @param	columnName		The name of the column to enter in
	 * @param	value			The value to enter
	 */
	public ColumnEntry(String columnName, T value) {
		this.columnName = columnName;
		this.value = value;
	}
	
	/**
	 * Returns the name of the column to be altered. Should be called as part of
	 * preparing the prepared statement.
	 * @return					The name of the column to be inserted into
	 */
	public String getColumnName() {
		return columnName;
	}
	
	/**
	 * Sets this column/value in the given statement.
	 * @param	statement		The statement to change
	 * @param	index			The index of the statement to set
	 * @throws	SQLException	If something goes wrong in setting statement
	 */
	public abstract void set(PreparedStatement statement, int index) throws SQLException;

}
