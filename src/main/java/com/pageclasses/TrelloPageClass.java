package com.pageclasses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.baseclasses.ControlActions;
import com.constants.ApplicationConstants;
import com.utility.PropertyFileOperations;

/*
 * Page Layer
 * 
 * @information - will contain all functionality related methods of Trello page
 */
public class TrelloPageClass extends ControlActions {

	private Logger log = LogManager.getLogger(this.getClass());

	PropertyFileOperations locators;
	private static TrelloPageClass trelloPageClass;

	private TrelloPageClass() {
		
		//Load properties file for locators
		locators = new PropertyFileOperations("trellopage");
		log.info("STEP : TrelloPage locators loaded");
	}

	/*
	 * To get trelloPageClass instance
	 * 
	 * @return trelloPageClass
	 */
	public static TrelloPageClass getTrelloPageClassInstance() {
		trelloPageClass = new TrelloPageClass();
		return trelloPageClass;
	}

	/*
	 * Login to trello
	 * 
	 * @param String - Username
	 * 		  String - password
	 * @return boolean
	 */
	public boolean loginToTrello(String username, String password) {
		initBrowser(ApplicationConstants.BROWSER, ApplicationConstants.URL);
		enterText(locators.getKey("user"), username);
		log.info("STEP : Entered username - "+username);
		clickOnElement(locators.getKey("continueButton"));
		log.info("STEP : Clicked on continue button");
		wait(500);
		enterText(locators.getKey("password"), password);
		log.info("STEP : Entered password - "+password);
		clickOnElement(locators.getKey("loginButton"));
		log.info("STEP : Clicked on login button");
		log.info("STEP :- Page Title is "+getPageTitle());
		return getPageTitle().contains("Log in with Atlassian account");
	}
	
	/*
	 * To terminate browser
	 */
	public void terminateBrowser() {
		closeBrowser();
		log.info("STEP : Browser closed");
	}

	/*
	 * Create board
	 * 
	 * @param String - boardName
	 * @return boolean 
	 */
	public boolean createBoard(String boardName) {
		clickOnElement(locators.getKey("createNewBoard"));
		log.info("STEP : Clicked on create new board");
		enterText(locators.getKey("enterBoardTitle"), boardName);
		log.info("STEP : Entered board title - " + boardName);
		clickOnElement(locators.getKey("createBoardSubmitButton"));
		log.info("STEP : Clicked on create board submit button");
		return isElementDisplayed(String.format(locators.getKey("boardNameFromList"), boardName));
	}

	/*
	 * Create list on trello board
	 * 
	 * @param List<String> - listNames
	 * @return boolean
	 */
	public boolean createLists(List<String> listNames) {
		boolean flag = true;
		for (String listName : listNames) {
			
			enterText(locators.getKey("listNameInput"), listName);
			log.info("STEP : Entered List name - " + listName);
			clickOnElement(locators.getKey("addListButton"));
			log.info("STEP : Clicked on Add list button");
			if(!isElementPresent(String.format(locators.getKey("listNameVerify"),listName))) {
				flag=false;
				log.info("STEP : List not created with name "+listName);
				break;
			}
		}
		return flag;
	}

	/*
	 * Create cards on board
	 * 
	 * @param String - cardName
	 * @return boolean
	 */
	public boolean createCards(String cardName) {
		boolean flag = true;
		clickOnElement(locators.getKey("addACard"));
		log.info("STEP : Clicked on Add card button");
		enterText(locators.getKey("enterTitleOfCard"), cardName);
		log.info("STEP : Entered Card name - " + cardName);
		clickOnElement(locators.getKey("addCardButton"));
		log.info("STEP : Clicked on Add card button");
		if (!isElementPresent(String.format(locators.getKey("cardNameVerify"), cardName))) {
			flag = false;
			log.info("STEP : Card not created with name " + cardName);
		}
		return flag;
	}

	/*
	 * Perform drag and drop of card 
	 * 
	 * @param String - cardName, listName
	 * @return boolean
	 */
	public boolean performDragAndDrop(String cardName, String listName) {
		String srcCard = String.format(locators.getKey("createdCard"), cardName);
		performDragAndDropUsingActionClass(srcCard, locators.getKey("secondCard"));
		log.info("STEP : Drag and drop performed for cardname  " + cardName);
		return isElementPresent(String.format(locators.getKey("droppedCard"), listName, cardName));
	}

	/*
	 * To logout from trello account
	 * 
	 * @return boolean
	 */
	public boolean logout() {
		clickOnElement(locators.getKey("openMenuBar"));
		log.info("STEP : Clicked on open menu bar");
		clickOnElement(locators.getKey("logout"));
		log.info("STEP : Clicked on Logout");
		String logoutTxt = getElement(locators.getKey("logoutMsg")).getText();
		if (logoutTxt.equalsIgnoreCase("Log out of your Atlassian account")) {
			clickOnElement(locators.getKey("logoutButton"));
			log.info("STEP : Clicked on Logout button");
			if (isElementDisplayed(locators.getKey("loginLink"))) {
				log.info("STEP : logout successfully.");
				return true;
			}
		} else
			return false;
		return false;
	}

	/*
	 * To close created board
	 * 
	 * @param String - boardName
	 */
	public void closeBoard(String boardName) {
		clickOnElementUsingAction(String.format(locators.getKey("boardNameFromList"), boardName),
				locators.getKey("threeDotsOfBoard"));
		log.info("STEP : Clicked on Three dots of board");
		clickOnElement(locators.getKey("closeBoardAction"));
		log.info("STEP : Clicked on close board");
		clickOnElement(locators.getKey("closeButton"));
		log.info("STEP : Clicked on close button");
		wait(2000);
	}

	/*
	 * To get X Y coordinates of element on page
	 * 
	 * @param String - cardName,listName
	 * @return Map<String,Integer>
	 */
	public Map<String,Integer> getXYCordinateOfElement(String cardName, String listName) {
		WebElement element = getElement(String.format(locators.getKey("droppedCard"), listName, cardName));
		Point point = element.getLocation();
		int xcord = point.getX();
		log.info("Position of the webelement from left side is "+xcord +" pixels");
		int ycord = point.getY();
		log.info("Position of the webelement from top side is "+ycord +" pixels");
		Map<String,Integer> pixels = new HashMap<>();
		pixels.put("X-Pixel",xcord);
		pixels.put("Y-Pixel",ycord);
		return pixels;
	}
}