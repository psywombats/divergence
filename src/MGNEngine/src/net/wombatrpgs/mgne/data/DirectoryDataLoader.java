/**
 *  DirectoryDataLoader.java
 *  Created on Nov 4, 2012 8:22:34 AM for project MGNEngine
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;

import net.wombatrpgs.mgne.global.Global;
import net.wombatrpgs.mgns.core.MainSchema;


/**
 * The basic directory data loader implementation. This one reads straight from
 * .json files rather than from .blob stuff or whatever the hell compressed or
 * encrypted data format is used for data entries.
 */
public class DirectoryDataLoader extends DataLoader {
	
	protected File readingDirectory;

	/**
	 * @see net.wombatrpgs.mgne.data.DataLoader#setDirectory(java.lang.String)
	 */
	@Override
	protected void setDirectory(String dataDirectory) {
		Global.reporter.inform("Now reading from " + dataDirectory);
		readingDirectory = new File(dataDirectory);
		if (!readingDirectory.exists()) {
			Global.reporter.err("Tried to read data from a non-existant directory");
			return;
		}
		// we're going to preemptively load the entire thing
		List<File> toLoad = recursivelyGetFiles(readingDirectory);
		for (final File entry : toLoad) {
			preloaderBar.addTask(new LoadingBarRunnable() {
				@Override
				public void run() { loadFile(entry); }
				@Override
				public int getSize() { return 1; }
			});
		}
		preloaderBar.run();
	}
	
	/**
	 * Perform the actual load operator on a file.
	 * @param 	toLoad		The file to actually load
	 */
	protected void loadFile(File toLoad) {
		preloaded.add(new PreloadedData(getSchemaByFile(toLoad), readFile(toLoad)));
		Global.reporter.inform("Loaded database entry " + toLoad);
	}
	
	/**
	 * Gets the actual schema for a data object based on its file location.
	 * @param 		dataFile 	The data file to get the schema for
	 * @return 					The class of schema of the data
	 */
	@SuppressWarnings("unchecked")
	protected Class<? extends MainSchema> getSchemaByFile(File dataFile) {
		String rootPath = readingDirectory.getAbsolutePath();
		String ourPath = getMainSchemaDirectory(dataFile).getAbsolutePath();
		String relativePath = ourPath.substring(rootPath.length(), ourPath.length());
		if (relativePath.startsWith("\\")) relativePath = relativePath.substring(1);
		String className = relativePath.replace('\\', '.');
		try {
			// we do forName here instead of classloading because we can assumed
			// that this project includes its games' scheme files
			// If not something else will need to be done...
			Class<?> rawClass = Class.forName(className);
			if (!MainSchema.class.isAssignableFrom(rawClass)) {
				Global.reporter.warn("Loaded class " + rawClass + " didn't extend base schema");
			}
			return (Class<? extends MainSchema>) rawClass;
		} catch (ClassNotFoundException e) {
			Global.reporter.err("Couldn't find a class " + className, e);
			return null;
		}
	}
	
	/**
	 * Gets the directory corresponding to this file's schema. For instance, if
	 * this data file was a monster (even Path("/monster/bosses")), this would
	 * return net/wombatrpgs/schema/monster directory.
	 * @param dataFile The file to get the schema directory of
	 * @return The root schema directory for the data file
	 */
	protected File getMainSchemaDirectory(File dataFile) {
		String subdir = getDataSubdir(dataFile);
		if (subdir.equals("")) {
			return dataFile.getParentFile();
		} else {
			return new File(dataFile.getParentFile().getAbsolutePath());
		}
	}
	
	/**
	 * Get the subdirectory this data entry is located at within its parent
	 * schema directory.
	 * @param dataFile The file with the data to read
	 * @return The path this file should be in local to the schema
	 */
	protected String getDataSubdir(File dataFile) {
		try {
			Global.mapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			MainSchema base = Global.mapper.readValue(dataFile, MainSchema.class);
			Global.mapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			return base.subfolder;
		} catch (JsonParseException e) {
			Global.reporter.err("Malformed base schema " + dataFile, e);
		} catch (JsonMappingException e) {
			Global.reporter.err("Doesn't match base schema " + dataFile, e);
		} catch (IOException e) {
			Global.reporter.err("Can't read base " + dataFile, e);
		}
		return null;
	}
	
	/**
	 * A method from the ~internet~. Loads up a file as a string blob.
	 * @param 	source	The file to load
	 * @return			The stirng formed of all that file's contents
	 */
	protected static String readFile(File source) {
		FileInputStream stream;
		stream = Global.fileLoader.getFileStream(source.getPath());
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		} catch (IOException e) {
			Global.reporter.err("Screwup reading data file to string", e);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				Global.reporter.err("Deep, deep shit", e);
			}
		}
		
		return null;
	}
}
