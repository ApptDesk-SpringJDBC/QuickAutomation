package com.quicktest.utils;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 
 * @author Balaji
 *
 */

//TODO: not use
public class UserActions {
	private static Logger logger = Logger.getLogger(UserActions.class);
	public WebDriver driver;
	private boolean flagElement;
	private String browser;

	public UserActions(WebDriver driver, String browserType) {
		this.driver = driver;
		this.browser = browserType;
	}

	public String getAttributes(String submit) {
		List<WebElement> buttons = driver.findElements(By.xpath("//tr//input"));
		String xpath = "//tr//input[@value=", reqxpat = "";
		String buttonName = "";
		int buttonflag = 1;
		for (WebElement button : buttons) {

			if (button.getAttribute("value").contains(submit)) {
				buttonName = button.getAttribute("value");
				buttonflag = 2;
				reqxpat = xpath + "'" + buttonName + "']";
				break;
			}

		}
		if (buttonflag == 1) {
			buttons = driver.findElements(By.xpath("//img"));
			xpath = "//tr//img[@alt=";
			for (WebElement button : buttons) {
				if (button.getAttribute("alt").contains(submit)) {
					buttonName = button.getAttribute("alt");
					buttonflag = 2;
					reqxpat = xpath + "'" + buttonName + "']";
					break;
				}
			}
		}
		if (buttonflag == 1) {
			buttons = driver.findElements(By.xpath("//div//input"));
			xpath = "//div//input[@value=";
			for (WebElement button : buttons) {
				if (button.getAttribute("value").contains(submit)) {
					buttonName = button.getAttribute("value");
					buttonflag = 2;
					reqxpat = xpath + "'" + buttonName + "']";
					break;
				}
			}
		}
		if (buttonflag == 1) {
			buttons = driver.findElements(By.xpath("//div//input"));
			xpath = "//div//input[@title=";
			for (WebElement button : buttons) {
				if (button.getAttribute("title").contains(submit)) {
					buttonName = button.getAttribute("title");
					buttonflag = 2;
					reqxpat = xpath + "'" + buttonName + "']";
					break;
				}
			}
		}
		if (buttonflag == 1) {
			buttons = driver.findElements(By.xpath("//tr//input"));
			xpath = "//tr//input[@title=";
			for (WebElement button : buttons) {
				if (button.getAttribute("title").contains(submit)) {
					buttonName = button.getAttribute("title");
					buttonflag = 2;
					reqxpat = xpath + "'" + buttonName + "']";
					break;
				}
			}
		}
		if (buttonflag == 1) {
			xpath = "//button[.='" + submit + "']";
			try {
				if (isElementPresent(By.xpath(xpath))) {
					buttonflag = 2;
					reqxpat = xpath;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (buttonflag == 1)
			reqxpat = "false";
		System.out.println(reqxpat);
		return reqxpat;
	}

	// ele present

	public boolean isElementPresent(By locator) throws Exception {
		flagElement = false;
		try {
			TestUtility.WaitForElementExistbutNotFail(driver, locator, Wait.getsNormalWait());
			driver.findElement(locator);
			flagElement = true;

		} catch (NoSuchElementException e) {

			flagElement = false;

		}

		return flagElement;
	}

	public boolean isElementPresentWithShortWait(By locator) throws Exception {
		flagElement = false;
		try {
			TestUtility.WaitForElementExistbutNotFail(driver, locator, 3);
			driver.findElement(locator);
			flagElement = true;

		} catch (NoSuchElementException e) {

			flagElement = false;

		}

		return flagElement;
	}

	/**
	 * Method - Safe Method for User Click, waits until the element is loaded
	 * and then performs a click action
	 * 
	 * @param Locator
	 * @param iWaitTime
	 * @return - boolean (returns True when click action is performed else
	 *         returns false)
	 * @throws Exception
	 */
	public boolean SafeClick(By Locator, int iWaitTime) throws Exception {
		try {
			ClickElement(Locator, iWaitTime);
			return true;
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			logger.error("Unable to click on element " + Locator + "\n" + e.getMessage());
			Assert.fail("Unable to click on element " + Locator + "\n" + e.getMessage());
			return false;
		}

		catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to click on element " + Locator + "\n" + e.getMessage());
			Assert.fail("Unable to click on element " + Locator + "\n" + e.getMessage());
			return false;
		}
	}

	/**
	 * Method - Support method for all SafeClickByX methods. This should be used
	 * only in UA.Java class but not in any other class
	 * 
	 * @param Locator
	 * @param iWaitTime
	 * @throws Exception
	 */
	private void ClickElement(By Locator, int iWaitTime) throws Exception {
		TestUtility.WaitUntilClickable(driver, Locator, iWaitTime);
		// scrollIntoElementView(Locator);
		WebElement element = driver.findElement(Locator);
		TestUtility.setHighlight(driver, element);
		element.click();
		logger.info("Clicked on element " + Locator);
	}

	/**
	 * Method - Safe Method for User Clear and Type, waits until the element is
	 * loaded and then enters some text
	 * 
	 * @param bylocator
	 * @param sText
	 * @param iWaitTime
	 * @return - boolean (returns True when the text is typed in the field else
	 *         returns false)
	 * @throws Exception
	 */
	public boolean SafeClearType(By bylocator, String sText, int iWaitTime) throws Exception {
		try {
			ClearTextAndType(bylocator, sText, iWaitTime);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to enter " + sText + " in field " + bylocator + "\n" + e.getMessage());
			Assert.fail("Unable to enter " + sText + " in field " + bylocator + "\n" + e.getMessage());
			return false;
		}
	}

	public boolean SafeClearTypeWithWebElement(WebElement ele, String sText, int iWaitTime) throws Exception {
		try {
			ClearTextAndType(ele, sText, iWaitTime);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to enter " + sText + " in field " + "\n" + e.getMessage());
			Assert.fail("Unable to enter " + sText + " in field " + "\n" + e.getMessage());
			return false;
		}
	}

	private void ClearTextAndType(By bylocator, String sText, int iWaitTime) throws Exception {
		TestUtility.WaitForElementExist(driver, bylocator, iWaitTime);
		scrollIntoElementView(bylocator);
		WebElement element = driver.findElement(bylocator);
		TestUtility.setHighlight(driver, element);
		element.clear();
		element.sendKeys(sText);
		logger.info("Safe cleared and type text on " + bylocator + "with text" + sText);
	}

	private void ClearTextAndType(WebElement element, String sText, int iWaitTime) throws Exception {

		TestUtility.setHighlight(driver, element);
		element.clear();
		element.sendKeys(sText);
		logger.info("Safe cleared and type text" + "with text" + sText);
	}

	/**
	 * Method - Safe Method for User Type, waits until the element is loaded and
	 * then enters some text
	 * 
	 * @param bylocator
	 * @param sText
	 * @param iWaitTime
	 * @return - boolean (returns True when the text is typed in the field else
	 *         returns false)
	 * @throws Exception
	 */
	public boolean SafeType(By bylocator, String sText, int iWaitTime) throws Exception {
		try {
			TypeInAField(bylocator, sText, iWaitTime);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to enter " + sText + " in field " + bylocator + "\n" + e.getMessage());
			Assert.fail("Unable to enter " + sText + " in field " + bylocator + "\n" + e.getMessage());
			return false;
		}
	}

	public boolean SafeTypeWithWebElement(WebElement ele, String sText, int iWaitTime) throws Exception {
		try {
			ele.sendKeys(sText);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to enter " + sText + " in field \n" + e.getMessage());
			Assert.fail("Unable to enter " + sText + " in field \n" + e.getMessage());
			return false;
		}
	}

	private void TypeInAField(By bylocator, String sText, int iWaitTime) throws Exception {
		TestUtility.WaitForElementExist(driver, bylocator, iWaitTime);
		scrollIntoElementView(bylocator);
		WebElement element = driver.findElement(bylocator);
		TestUtility.setHighlight(driver, element);
		element.sendKeys(sText);

	}

	/**
	 * Method - Safe Method for Radio button selection, waits until the element
	 * is loaded and then selects Radio button
	 * 
	 * @param bylocator
	 * @param iWaitTime
	 * @return - boolean (returns True when the Radio button is selected else
	 *         returns false)
	 * @throws Exception
	 */
	public boolean SafeSelectRadioButton(By bylocator, int iWaitTime) throws Exception {
		try {
			ClickElement(bylocator, iWaitTime);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to select Radio button " + bylocator + "\n" + e.getMessage());
			Assert.fail("Unable to select Radio button " + bylocator + "\n" + e.getMessage());
			return false;
		}
	}

	/**
	 * Method - Safe Method for checkbox selection, waits until the element is
	 * loaded and then selects checkbox
	 * 
	 * @param bylocator
	 * @param iWaitTime
	 * @return - boolean (returns True when the checkbox is selected else
	 *         returns false)
	 * @throws Exception
	 */
	public boolean SafeCheck(By bylocator, int iWaitTime) throws Exception {
		try {
			SelectCheckBox(bylocator, iWaitTime);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to select checkbox " + bylocator + "\n" + e.getMessage());
			Assert.fail("Unable to select checkbox " + bylocator + "\n" + e.getMessage());
			return false;
		}
	}

	private void SelectCheckBox(By Locator, int iWaitTime) throws Exception {
		TestUtility.WaitForElementExist(driver, Locator, iWaitTime);
		scrollIntoElementView(Locator);
		WebElement CheckBox = driver.findElement(Locator);
		TestUtility.setHighlight(driver, CheckBox);
		if (CheckBox.isSelected())
			System.out.println("CheckBox " + Locator + "is already selected");
		else
			CheckBox.click();
	}

	/**
	 * Method - Safe Method for User Select option from Drop down by option
	 * name, waits until the element is loaded and then selects an option from
	 * drop down
	 * 
	 * @param bylocator
	 * @param sOptionToSelect
	 * @param iWaitTime
	 * @return - boolean (returns True when option is selected from the drop
	 *         down else returns false)
	 * @throws Exception
	 */
	public boolean SafeSelectOptionInDropDown(By bylocator, String sOptionToSelect, int iWaitTime) throws Exception {
		boolean bFlag = false;
		TestUtility.WaitForElementExist(driver, bylocator, iWaitTime);
		// First, get the WebElement for the select tag
		scrollIntoElementView(bylocator);
		WebElement selectElement = driver.findElement(bylocator);
		TestUtility.setHighlight(driver, selectElement);

		// Then instantiate the Select class with that WebElement
		Select select = new Select(selectElement);

		// Get a list of the options
		List<WebElement> options = select.getOptions();

		// For each option in the list, verify if it's the one you want and then
		// click it
		for (WebElement option : options) {
			try {
				if (option.getText().contains(sOptionToSelect)) {
					option.click();
					logger.info("Selected " + option + " from " + bylocator + " dropdown");
					bFlag = true;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Unable to select " + option + " from " + bylocator + "\n" + e.getMessage());
				Assert.fail("Unable to select " + option + " from " + bylocator + "\n" + e.getMessage());
				bFlag = false;
			}
		}
		return bFlag;
	}

	/**
	 * Method - Safe Method for User Select option from drop down list and then
	 * selects an option from dropdown menu
	 * 
	 * @param WebElement
	 * @param sOptionToSelect
	 * @param iWaitTime
	 * @return
	 * @throws Exception
	 */
	public boolean SafeSelectOptionInDropDownwithWebElement(WebElement ele, String sOptionToSelect, int iWaitTime) throws Exception {
		boolean bFlag = false;

		WebElement selectElement = ele;
		TestUtility.setHighlight(driver, selectElement);

		// Then instantiate the Select class with that WebElement
		Select select = new Select(selectElement);

		// Get a list of the options
		List<WebElement> options = select.getOptions();

		// For each option in the list, verify if it's the one you want and then
		// click it
		for (WebElement option : options) {
			try {
				if (option.getText().contains(sOptionToSelect)) {
					option.click();
					logger.info("Selected " + option + " from " + ele + " dropdown");
					bFlag = true;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Unable to select " + option + " from " + ele + "\n" + e.getMessage());
				Assert.fail("Unable to select " + option + " from " + ele + "\n" + e.getMessage());
				bFlag = false;
			}
		}
		return bFlag;
	}

	/**
	 * Method - Safe Method for User Select option from list menu, waits until
	 * the element is loaded and then selects an option from list menu
	 * 
	 * @param bylocator
	 * @param sOptionToSelect
	 * @param iWaitTime
	 * @return
	 * @throws Exception
	 */
	public boolean SafeSelectListBox(By bylocator, String sOptionToSelect, int iWaitTime) throws Exception {
		boolean bFlag = false;
		TestUtility.WaitForElementExist(driver, bylocator, iWaitTime);
		// First, get the WebElement for the select tag
		WebElement selectElement = driver.findElement(bylocator);
		TestUtility.setHighlight(driver, selectElement);

		// Then instantiate the Select class with that WebElement
		Select select = new Select(selectElement);

		// Get a list of the options
		List<WebElement> options = select.getOptions();
		select.deselectAll();
		// For each option in the list, verify if it's the one you want and then
		// click it
		for (WebElement option : options) {
			try {
				System.out.println(option.getText());
				if (option.getText().contains(sOptionToSelect)) {
					option.click();
					logger.info("Selected " + option + " from " + bylocator + " Listbox");
					bFlag = true;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Unable to select " + option + " from " + bylocator + "\n" + e.getMessage());
				Assert.fail("Unable to select " + option + " from " + bylocator + "\n" + e.getMessage());
				bFlag = false;
			}
		}
		return bFlag;
	}

	/**
	 * Method: for verifying if accept exists and accepting the alert
	 * 
	 * @return - boolean (returns True when accept action is performed else
	 *         returns false)
	 * @throws Exception
	 */
	public boolean isAlertExistsAndAcceptAlert() throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.alertIsPresent());

			Alert alert = driver.switchTo().alert();
			alert.accept();
			logger.info("Accepted the alert:" + alert.getText());
			return true;
		} catch (Exception e) {
			// e.printStackTrace();
			logger.info("No alert is displayed for this action or unable to accept the alert" + "\n");// +
																										// e.getMessage());
			return false;
		}
	}

	public boolean isAlertExists() throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.alertIsPresent());
			driver.switchTo().alert();
			return true;
		} catch (Exception e) {
			// e.printStackTrace();
			logger.info("No alert is displayed for this action" + "\n");// +
																		// e.getMessage());
			return false;
		}
	}

	public boolean iscompareAcceptAlert(String alertText) throws Exception {
		int flag = 1;
		try {
			Alert alert = driver.switchTo().alert();
			String alertMessage = alert.getText();
			flag = 2;
			alert.accept();
			logger.info("Accepted the alert:" + alertMessage);
			if (!alertText.equalsIgnoreCase(alertMessage)) {
				logger.error("text is not matched");
				return false;
			}
			return true;
		} catch (Exception e) {
			// e.printStackTrace();
			if (flag == 1)
				logger.error("unable to accept the alert" + "\n");// +
																	// e.getMessage());
			return false;
		}
	}

	/**
	 * 
	 * TODO:
	 * 
	 * scroll method to scroll the page down until expected element is visible *
	 * 
	 * @param Locator
	 *            - Locator value by which an element is located
	 * @param iWaitTime
	 *            - Time to wait for an element
	 * @return - returns the text value from element
	 */
	public void scrollIntoElementView(By Locator) throws Exception {
		try {
			WebElement element = driver.findElement(Locator);
			if (browser.equalsIgnoreCase("ff") || browser.equalsIgnoreCase("ie")) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to scroll the page to find " + Locator + "\n" + e.getMessage());
			Assert.fail("Unable to scroll the page to find " + Locator + "\n" + e.getMessage());
		}
	}
}
