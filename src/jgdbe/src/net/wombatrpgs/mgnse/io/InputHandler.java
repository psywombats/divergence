/**
 *  InputHandler.java
 *  Created on Aug 8, 2012 4:20:57 AM for project MGNSE
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import net.wombatrpgs.mgns.core.MainSchema;
import net.wombatrpgs.mgnse.Global;
import net.wombatrpgs.mgnse.MainFrame;
import net.wombatrpgs.mgnse.schema.AppConfig;
import net.wombatrpgs.mgnse.schema.ProjectConfig;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Handles all file input reading (json) and other weird stuff like requesting
 * files from user.
 */
public class InputHandler {
	
	private MainFrame parent;
	
	/**
	 * Creates a new input handler.
	 * @param parent The parent application frame
	 */
	public InputHandler(MainFrame parent) {
		this.parent = parent;
	}
	
	/**
	 * Asks the user to get the project configuration file.
	 * @param oldFile The old selected file, null if none loaded
	 * @return The selected config file, null if none selected
	 */
	public File requestProjectFile(File oldFile) {
		File selected = null;
		FileFilter filter = new FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory()) return true;
				String name = f.getName();
				return name.endsWith(".mgndb");
			}
			public String getDescription() {
				return "MGN Database Files (*.mgndb)";
			}
		};
		JFileChooser chooser;
		if (oldFile == null) {
			chooser = new JFileChooser();
		} else {
			chooser = new JFileChooser(oldFile.getParentFile());
		}
		chooser.setApproveButtonText("Open");
		chooser.setApproveButtonToolTipText("Opens the project file");
		chooser.setDialogTitle("Open Database");
		chooser.setApproveButtonMnemonic('o');
		chooser.setFileFilter(filter);
		switch (chooser.showOpenDialog(parent)) {
		case JFileChooser.APPROVE_OPTION:
			selected = chooser.getSelectedFile();
			if (!selected.getName().contains(".")){
				selected = new File(oldFile.getAbsolutePath() + ".mgndb");
			}
			break;
		}
		return selected;
	}
	
	/**
	 * Reads in config data for a project from a file
	 * @param projectConfigFile A file containing json config data
	 * @return The config data in that file, null if error or no file
	 */
	public ProjectConfig parseProjectConfig(File projectConfigFile) {
		if (projectConfigFile == null) return null;
		ProjectConfig data = null;
		try {
			data = Global.instance().mapper().readValue(projectConfigFile, ProjectConfig.class);
		} catch (JsonProcessingException e) {
			Global.instance().err("Malformatted project file", e);
		} catch (IOException e) {
			Global.instance().err("Couldn't open project file", e);
		}
		Global.instance().debug("Opened a project " + data.name);
		return data;
	}
	
	/**
	 * Loads the user settings config file.
	 * @return the config contained in the json file, null if error
	 */
	public AppConfig parseAppConfig(File appConfigFile) {
		AppConfig data = null;
		try {
			data = Global.instance().mapper().readValue(appConfigFile, AppConfig.class);
		} catch (JsonProcessingException e) {
			Global.instance().err("Malformatted app config file", e);
		} catch (IOException e) {
			Global.instance().err("Couldn't find app config file", e);
		};
		Global.instance().debug("Got config data for app with project " + data.projectFile);
		return data;
	}
	
	/**
	 * For those pesky times where you're not sure if it's absolute or relative.
	 * @param configString The configuration data string
	 * @param parent The application running file
	 * @return The directory file it's refering to 
	 */
	public File getFile(File parent, String configString) {
		if (configString == null) return null;
		File test = new File(configString);
		if (test.exists()) {
			Global.instance().debug("Reading a file from " + test.getAbsolutePath());
			return test;
		} else {
			File f = new File(parent.getAbsolutePath() + File.separator + configString);
			Global.instance().debug("Reading a file from " + f.getAbsolutePath());
			return f;
		}
	}
	
	/**
	 * Returns all files in the root directory, recursively. Does not include
	 * directories.
	 * @param root The root directory to get files from
	 * @return An ArrayList of all those files
	 */
	public ArrayList<File> recursivelyGetFiles(File root) {
		ArrayList<File> files = new ArrayList<File>();
		for (File f : root.listFiles()) {
			if (f.isDirectory()) {
				files.addAll(recursivelyGetFiles(f));
			} else {
				files.add(f);
			}
		}
		return files;
	}
	
	/**
	 * Loads up and instantiates a database entry into a schema.
	 * @param schema The schema class of the new entry
 	 * @param data The file with the data used to populate it
	 */
	public MainSchema instantiateData(Class<? extends MainSchema> schema,  File data) {
		MainSchema object = null;
		try {
			object = Global.instance().mapper().readValue(data, schema);
		} catch (JsonParseException e) {
			Global.instance().err("Malformatted data file " + data, e);
		} catch (JsonMappingException e) {
			Global.instance().err("Data file doesn't match schema " + data, e);
		} catch (IOException e) {
			Global.instance().err("Couldn't read data file " + data, e);
		}
		return object;
	}

}
