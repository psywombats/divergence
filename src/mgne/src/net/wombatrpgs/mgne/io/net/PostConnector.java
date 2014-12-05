/**
 *  PostConnector.java
 *  Created on Dec 5, 2014 3:23:54 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import net.wombatrpgs.mgne.io.net.columns.ColumnEntry;

/**
 * Posts queries via HTTP.
 */
public class PostConnector {
	
	protected String urlString;
	
	/**
	 * Creates a new connector to post to a specific location.
	 * @param	url				The URL to post to, eg "http://www.a.net/a.php"
	 */
	public PostConnector(String url) {
		this.urlString = url;
	}
	
	/**
	 * Posts via HTTP with the given parameters. Blocks -- deal with it.
	 * @param	params			The data params to pass
	 * @return					The HTTP response
	 * @throws	IOException 	If something goes wrong during the post
	 */
	public String post(List<ColumnEntry<?>> params) throws IOException {
		
		URL url = new URL(urlString);
		
		String queryString = "";
		for (int i = 0; i < params.size(); i += 1) {
			ColumnEntry<?> param = params.get(i);
			queryString += param.getColumnName();
			queryString += "=";
			queryString += param.getValueString();
			if (i < params.size()-1) {
				queryString += "&";
			}
		}
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("charset", "utf-8");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(queryString.getBytes().length));
		connection.setUseCaches(false);

		DataOutputStream stream = new DataOutputStream(connection.getOutputStream());
		stream.writeBytes(queryString);
		stream.flush();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
		String line = "";
		String result = "";
		while ((line = reader.readLine()) != null) {
			result += line;
		}
		
		stream.flush();
		stream.close();
		reader.close();
		connection.disconnect();
		
		return result;
	}

}
