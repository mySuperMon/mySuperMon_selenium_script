package com.trionxt.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.trionxt.constants.AppConstants;

public class CommonsUtil {

	public static WebDriver driver;

	/**
	 * 
	 * @param browserName
	 */
	public static void initializeDriver(String browserName) {

		if ((AppConstants.CHROME_BROWSER).equalsIgnoreCase(browserName)) {

			ChromeOptions options = new ChromeOptions();
			options.addArguments("test-type");
			options.addArguments("start-maximized");
			options.addArguments("--enable-automation");
			options.addArguments("test-type=browser");
			options.addArguments("disable-infobars");

			driver = new ChromeDriver(options);

		} else if ((AppConstants.IE_BROWSER).equalsIgnoreCase(browserName)) {

			driver = new InternetExplorerDriver();

		} else if ((AppConstants.FIREFOX_BROWSER).equalsIgnoreCase(browserName)) {

			driver = new FirefoxDriver();

		}
	}

	/**
	 * 
	 * @return WebDriver
	 */
	public static WebDriver getWebDriver() {
		return driver;
	}

	/**
	 * 
	 */
	public static void loadApplicationByURL() {
		String appUrlValue = readApplicationProperties("application_url");
		driver.get(appUrlValue);
		waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);
	}

	/**
	 * 
	 * @param propertyKey
	 * @return
	 */
	public String readLocatorsProperties(String propertyKey) {

		Properties properties = new Properties();
		File file = new File(AppConstants.APPLICATION_LOCATORS_FILE_PATH);
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			properties.load(fileInputStream);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
		return properties.getProperty(propertyKey);

	}

	/**
	 * 
	 * @param propertyKey
	 * @return
	 */
	public static String readApplicationProperties(String propertyKey) {

		Properties properties = new Properties();
		File file = new File(AppConstants.APPLICATION_PROPERTIES_FILE_PATH);
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			properties.load(fileInputStream);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
		return properties.getProperty(propertyKey);

	}

	/**
	 * 
	 * @param testName
	 * @param key
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
//		public static String readDataFromJsonFile(String testName, String key) throws IOException, ParseException {
//			File file = new File(AppConstants.TEST_DATA_JSON_FILE_PATH);
//			FileInputStream fileInputStream = new FileInputStream(file);
//			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//			JSONParser jParser = new JSONParser();
//			JSONObject jFileObj = (JSONObject) jParser.parse(inputStreamReader);
//			JSONObject jDataObj = (JSONObject) jFileObj.get(testName);
//			String value = (String) jDataObj.get(key);
//			return value;
//		}

	/**
	 * 
	 * @param time
	 */
	public static void waitForUserInterfaceToLoad(int time) {
		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
	}

}
