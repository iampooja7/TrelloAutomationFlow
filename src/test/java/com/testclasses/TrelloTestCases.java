package com.testclasses;

import com.constants.ApplicationConstants;
import com.pageclasses.TrelloPageClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/*
 * Test Layer
 * 
 * @information - will contain all test cases related to Trello page
 */
public class TrelloTestCases {
	
	TrelloPageClass trelloPageObj;
	
	@BeforeClass
	public void createPageClassObject() {
		trelloPageObj = TrelloPageClass.getTrelloPageClassInstance();
	}
	
	@BeforeMethod
	public void login() {
		//Login to trello
		boolean loginFlag = trelloPageObj.loginToTrello(ApplicationConstants.USERNAME, ApplicationConstants.PASSWORD);
		Assert.assertTrue(loginFlag, "ERROR - Login in not successfull");
	}
	
	/*
	 * @author - Pooja Vaidya
	 * 
	 * @date - 24th Jan 2023
	 */
	@Test(priority = 1, groups = { "smoke", "regression" })
	public void trelloAutomationFlow() {
		
		String boardName = "pooja123";
		//Create new board on trello
		boolean flagForBoardCreation =trelloPageObj.createBoard(boardName);
		Assert.assertTrue(flagForBoardCreation, "ERROR - New board not created");
		
		List<String> listNames = new ArrayList<String>();
		listNames.add("A");
		listNames.add("B");
		
		// Create new lists 
		boolean flagForList = trelloPageObj.createLists(listNames);
		Assert.assertTrue(flagForList, "ERROR - List not created");
		
		String cardName = "C";
		
		// Create new Card 
		boolean flagForCard = trelloPageObj.createCards(cardName);
		Assert.assertTrue(flagForCard, "ERROR - Cards not created");
		
		//Perform drag and drop for card 
		boolean flagForDragAndDrop = trelloPageObj.performDragAndDrop(cardName,listNames.get(1));
		Assert.assertTrue(flagForDragAndDrop, "ERROR - Cards not dragged and dropped");
		
		//Get X Y cordinates of dropped card
		Map<String,Integer> xyCordinate = trelloPageObj.getXYCordinateOfElement(cardName,listNames.get(1));
		System.out.println("Cordinates of dropped element : "+xyCordinate);
		
		//Close created board
		trelloPageObj.closeBoard(boardName);
	}
	
	@AfterMethod
	public void logout() {
		
		//Log out from trello account
		boolean flagForLogOut = trelloPageObj.logout();
		Assert.assertTrue(flagForLogOut, "ERROR - Not logged out");
		
		//Close browser
		trelloPageObj.terminateBrowser();
	}
}