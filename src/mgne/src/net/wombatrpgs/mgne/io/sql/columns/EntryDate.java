/**
 *  EntryDate.java
 *  Created on Nov 23, 2014 6:25:38 PM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.sql.columns;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.GregorianCalendar;

/**
 * Date wrapper for column entry.
 */
public class EntryDate extends ColumnEntry<Date> {

	/**
	 * Creates a new entry for a specific date.
	 * @param	columnName		The name of the column to set
	 * @param	value			The explicit date to use as the value
	 */
	public EntryDate(String columnName, Date value) {
		super(columnName, value);
	}
	
	/**
	 * Creates a new entry for the current date.
	 * @param	columnName		The name of the column to set
	 */
	public EntryDate(String columnName) {
		this(columnName, new Date(new GregorianCalendar().getTimeInMillis()));
	}

	/**
	 * @see net.wombatrpgs.mgne.io.sql.columns.ColumnEntry#set
	 * (java.sql.PreparedStatement, int)
	 */
	@Override
	public void set(PreparedStatement statement, int index) throws SQLException {
		statement.setDate(index, value);
	}

}
