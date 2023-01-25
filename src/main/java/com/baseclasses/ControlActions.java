package com.baseclasses;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.constants.ApplicationConstants;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * The Class ControlActions - Base Layer
 * 
 * @information - Contains all wrapper methods
 */
public abstract class ControlActions {
	
	private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
	private static ThreadLocal<WebDriverWait> waitThread = new ThreadLocal<>();
	static Logger log = LogManager.getLogger(ControlActions.class);
	
	/*
	 * Method for initialize browser.
	 * 
	 * @param - string broswername, string url
	 */
	protected void initBrowser(String browserName, String url) {
		log.debug("STEP: " + browserName + " browser launched and load " + url + " in browser");
		switch (browserName.toLowerCase()) {
		case "chrome":
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			driverThread.set(new ChromeDriver(options));
			break;

		default:
			break;
		}
		getDriver().manage().window().maximize();
		//getDriver().manage().window().setSize(new Dimension(1366, 768));
		getDriver().get(url);
		log.debug("STEP: Explicite wait set on WebDriver " + ApplicationConstants.EXP_WAIT);
		waitThread.set(new WebDriverWait(getDriver(), ApplicationConstants.EXP_WAIT));
	}
	
	/*
	 * Method for driver instance.
	 * 
	 * @return - webdriver object. 
	 */
	private static WebDriver getDriver() {
		return driverThread.get();
	}

	/*
	 * Method for Javascript executor object.
	 * 
	 * @return - JS object. 
	 */
	private JavascriptExecutor jsDriver(){
		 return (JavascriptExecutor) getDriver();
	}

	/*
	 * Method for Quit browser.
	 */
	protected void terminateBrowser() {
		getDriver().quit();
		log.debug("STEP - Browser Quit");
	}

	/*
	 * Method for webdriver wait object.
	 */
	private static WebDriverWait getWait(){
		return waitThread.get();
	}	
	
	/* 
	 * Method for take a screenshot.
	 */
	public static String takeScreenShot() {
		try {
			TakesScreenshot ts = (TakesScreenshot) getDriver();
			return ts.getScreenshotAs(OutputType.BASE64);
		} catch (Exception e) {
			log.debug("FAIL: Unable to screenshot, due to " + e.getCause());
		}
		return null;
	}
	
	/*
	 * Click on element
	 * 
	 * @param String - locator
	 */
	protected void clickOnElement(String locator) {
		getWait().until(ExpectedConditions.elementToBeClickable(getElement(locator))).click();
		log.debug("STEP: click on element " + getLocatorValue(locator));
	}
	
	/*
	 * Method to get element.
	 * 
	 * @param - string locator
	 * @return - Webelement
	 */
	protected WebElement getElement(String locator) {
		WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(getLocator(locator)));
		jsDriver().executeScript("arguments[0].style.border='3px solid red'", element);
		return element;
	}
	
	/*
	 * Method to get element.
	 * 
	 * @param - string locator
	 * @return - Webelement
	 */
	protected WebElement getElementWithPresence(String locator) {
		WebElement element = getWait().until(ExpectedConditions.presenceOfElementLocated(getLocator(locator)));
		jsDriver().executeScript("arguments[0].style.border='3px solid red'", element);
		return element;
	}
	
	/*
	 * Method to get locator
	 * 
	 * @param - string locator
	 * @return - By
	 */
	private By getLocator(String locator){
		String locatorType = getLocatorType(locator);
		String locatorValue = getLocatorValue(locator);
		
		switch(locatorType){
			case "xpath":
				return By.xpath(locatorValue);
			case "css":
				return By.cssSelector(locatorValue);
			case "id":
				return By.id(locatorValue);
			case "name"	:
				return By.name(locatorValue);
			case "className" :
				return By.className(locatorValue);
			case "linkText" :
				return By.linkText(locatorValue);
			case "partialLinkText" :	
				return By.partialLinkText(locatorValue);
		}
		return null;
	}
	
	/*
	 * Method to get Locator Type.
	 * 
	 * @param - string locator
	 * @return - string
	 */
	private String getLocatorType(String locator) {
		String type = null;
		try {
			type = locator.split(":-")[0].replace("[", "").replace("]", "").toLowerCase();
			log.debug("STEP: Locator type " + type + " in " + locator);
		} catch (Exception e) {
			log.fatal(e.getMessage());
			log.fatal(e.getCause());
		}
		return type;
	}

	/*
	 * Method to get Locator value.
	 * 
	 * @param - string locator
	 * @return - string
	 */
	private String getLocatorValue(String locator) {
		String value = null;
		try {
			value = locator.split(":-")[1];
			log.debug("STEP: Locator value" + value + " in " + locator);
		} catch (Exception e) {
			log.fatal(e.getMessage());
			log.fatal(e.getCause());
		}
		return value;
	}
	
	/*
	 * To Enter text in field.
	 * 
	 * @param - string locator, string message
	 */
	protected void enterText(String locator, String strMessage) {
		getElement(locator).click();
		getElement(locator).sendKeys(strMessage);
		log.debug("STEP: " + strMessage + " is set inside element");
	}
	
	/*
	 * Get page title
	 * 
	 * @return String - title of page
	 */
	protected String getPageTitle() {
		return getDriver().getTitle();
	}
	
	/*
	 * Close browser
	 */
	protected void closeBrowser() {
		getDriver().quit();
	}
	
	/*
	 * Method for wait.
	 * 
	 * @param - int miliseconds
	 */
	public void wait(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * To find whether the WebElement is displayed on the webpage using
	 * WebElement, should return a boolean based on the evaluation
	 * 
	 * @param locator
	 * @return - True if element is displayed.
	 */
	protected boolean isElementDisplayed(String locator) {
		WebElement element = null;
		boolean elementDisplay;
		try {
			element = getElement(locator);
			elementDisplay = element.isDisplayed();
			log.debug("STEP: Element displayed in screen viewport");
		} catch (Exception e) {
			scrollTillElementDisplay(element);
			log.debug("STEP: Scroll till element to get visible in screen viewport");
			elementDisplay = element.isDisplayed();
			log.debug("STEP: Retry to element display in screen viewport and status " + element);
		}
		return elementDisplay;
	}
	
	/*
	 * Scroll till to a particular Webelement.
	 * 
	 * @param - webelement element 
	 */
	protected void scrollTillElementDisplay(WebElement element) {
		JavascriptExecutor je = (JavascriptExecutor) getDriver();
		wait(1000);
		je.executeScript("arguments[0].scrollIntoView(true)", element);
		log.debug("STEP: Scroll till element");
	}
	
	/*
	 * To check element is present or not.
	 * 
	 * @param - String locator
	 * @return - boolean
	 */
	protected boolean isElementPresent(String locator) {
		By element = getLocator(locator);
		try {
	        getDriver().findElement(element);
	        return true;
	    } catch (NoSuchElementException e) {
	        return false;
	    }
	}
	
	/*
	 * To perform drag and drop operation using Action Class
	 * 
	 * @param String source,destination
	 */
	protected void performDragAndDropUsingActionClass(String source, String destination) {
		WebElement from = new WebDriverWait(getDriver(), 20)
				.until(ExpectedConditions.elementToBeClickable(getElement(source)));
		
		Point point = from.getLocation();
		int xcord = point.getX();
		log.info("Position of the webelement from left side is "+xcord +" pixels");
		int ycord = point.getY();
		log.info("Position of the webelement from top side is "+ycord +" pixels");
		
		Actions actions = new Actions(getDriver());
		try {
			actions.clickAndHold(from).moveByOffset(xcord, 0).release(from).pause(Duration.ofSeconds(1)).build()
					.perform();
		} catch (Exception e) {

		}
		wait(3000);
	}
	
	/*
	 * Click on Element using Action
	 * 
	 * @param - locator
	 */
	public void clickOnElementUsingAction(String locator1, String locator2) {
		Actions action = new Actions(getDriver());
		action.moveToElement(getElement(locator1)).click(getElementWithPresence(locator2)).build().perform();
	}
}