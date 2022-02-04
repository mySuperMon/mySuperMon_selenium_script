package com.trionxt.util;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.trionxt.constants.AppConstants;

public class EventsUtil {
	public static final WebDriver webDriver = CommonsUtil.getWebDriver();

	/**
	 * 
	 * @param locator
	 */
	public static void clickWebElementr(By locator) {
		CommonsUtil.waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);
		WebElement webElement = webDriver.findElement(locator);
		webElement.click();
	}

	/**
	 * 
	 * @param webElement
	 */
	public static void clearWebElement(WebElement webElement) {
		webElement.clear();
	}

	/**
	 * 
	 * @param locator
	 * @param text
	 */
	public static void enterTextInTextbox(By locator, String text) {
		CommonsUtil.waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);
		WebElement webElement = webDriver.findElement(locator);
		if (!webElement.getAttribute("value").isEmpty() || !webElement.getText().isEmpty()) {
			clearWebElement(webElement);
		}
		webElement.sendKeys(text);
	}

	/**
	 * 
	 * @param locator
	 * @param optionSelected
	 */
	public static void selectDropDownByText(By locator, String optionSelected) {
		try {
			Select select = new Select(webDriver.findElement(locator));
			select.selectByValue(optionSelected);
		} catch (NoSuchElementException noSuchElementException) {
			noSuchElementException.getStackTrace();
		}

	}

	public static boolean isNavigatedToCorrectPage(String xpathForPageTitle, String expectedPageTitle) {

		try {

			CommonsUtil.waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);
			String actualPageTitle = webDriver.findElement(By.xpath(xpathForPageTitle)).getText();
			if (actualPageTitle != null) {
				actualPageTitle = actualPageTitle.trim();
			}
			if (!expectedPageTitle.equalsIgnoreCase(actualPageTitle)) {
				return false;

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public static WebElement getWebElement(By locator) {
		CommonsUtil.waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);
		WebElement webElement = webDriver.findElement(locator);
		return webElement;
	}

}
