package com.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.constants.ApplicationConstants;

public class PropertyFileOperations {

	private Properties property = new Properties();

	public PropertyFileOperations(String filename) {
		InputStream input;
		try {
			input = new FileInputStream(new File(ApplicationConstants.PROPERTIES_FOLDER_PATH + filename + ".properties"));
			property.load(input);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final String getKey(String key) {
		return property.getProperty(key);
	}

}
