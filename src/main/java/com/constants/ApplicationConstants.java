package com.constants;

import java.io.File;

public abstract class ApplicationConstants {

	public static String URL = "https://trello.com/en/login";
	public static String BROWSER = "chrome";
	public static String ENVIRONMENT = "qa";
	public static final int EXP_WAIT = 30;
	public static final String EXTENT_REPORT = System.getProperty("user.dir") + File.separator
			+ "src" + File.separator + "test" + File.separator + "resources" + File.separator + "executionsReports" + File.separator;
	public static final String PROPERTIES_FOLDER_PATH = System.getProperty("user.dir") + File.separator + "src"
			+ File.separator + "main" + File.separator + "resources" + File.separator + "pageLocators" + File.separator;
	public static final String USERNAME = "poojavaidya777@gmail.com";
	public static final String PASSWORD = "Pooja1234#";
}