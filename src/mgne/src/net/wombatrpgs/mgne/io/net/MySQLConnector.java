package net.wombatrpgs.mgne.io.net;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import net.wombatrpgs.mgne.core.MGlobal;
import net.wombatrpgs.mgne.io.net.columns.ColumnEntry;

/**
 * Adapted from the class of the same name in Golden Age. This version is
 * supposed to be a little bit more generalized so as to support multiple games.
 * Games using the MGN engine can wrap this to provide the parameters more
 * easily.
 */
public class MySQLConnector {
	
	Connection connection;
	
	protected static final String DEFAULT_SERVER_URL = "jdbc:mysql://frih.net/wombatrp_games";
	protected static final String DATABASE_NAME = "wombatrp_games";
	
	/**
	 * Initiates a connection with the WombatRPGs MySQL server. Must be called
	 * before any other methods are operable, pretty much. Pretty sure this
	 * blocks, unfortunately, so run it in a separate thread.
	 * @param	url				The URL of the server to connect to 
	 * @throws	SQLException	If connection could not be established
	 */
	public void connect(String url) throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			MGlobal.reporter.err(e);
		}
		connection = DriverManager.getConnection(url, "wombatrp_games", "");
	}
	
	/**
	 * Connects to the WombatRPGs server.
	 * @throws	SQLException	if connection could not be established
	 */
	public void connect() throws SQLException {
		connect(DEFAULT_SERVER_URL);
	}
	
	/**
	 * Inserts a a new row into a table.
	 * @param	tableName		The name of the table to insert into
	 * @param	entries			The entries to insert
	 */
	public void insert(String tableName, List<ColumnEntry<?>> entries) {
		String query = "INSERT INTO `" + DATABASE_NAME + "`.`" + tableName + "`";
		query += " (";
		for (int i = 0; i < entries.size(); i += 1) {
			ColumnEntry<?> entry = entries.get(i);
			query += "`" + entry.getColumnName() + "`";
			if (i+1 < entries.size()) {
				query += ", ";
			}
		}
		query += ") ";
		query += "VALUES (";
		for (int i = 0; i < entries.size(); i += 1) {
			query += "?";
			if (i+1 < entries.size()) {
				query += ", ";
			}
		}
		query += ");";
		
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			for (int i = 0; i < entries.size(); i += 1) {
				entries.get(i).set(statement, i+1);
			}
			statement.execute();
		} catch (SQLException e) {
			MGlobal.reporter.err(e);
		}
	}
	
}

