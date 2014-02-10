/**
 *  DatabaseEntrySchemaException.java
 *  Created on Aug 6, 2012 3:55:59 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.exception;

/**
 * An exception for the general class of errors that involve an existing
 * database entry not matching up with its specified schema. Usually the
 * result of out-of-synch database and schema.
 */
public class MisplacedDatabaseEntryException extends Exception{

	/** some autogen shit */
	private static final long serialVersionUID = 4278217749422360917L;

	public MisplacedDatabaseEntryException(String string) {
		super(string);
	}
	

}
