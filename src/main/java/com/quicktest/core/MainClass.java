package com.quicktest.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import atu.testng.reports.ATUReports;
import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import atu.testng.selenium.reports.CaptureScreen.ScreenshotOf;

import com.quicktest.utils.TestUtility;

/**
 * 
 * @author Balaji
 *
 */
public class MainClass extends BaseTestSuiteClass {
	private Logger logger = Logger.getLogger(MainClass.class);
	private static String clientCode;
	private static String clientName;
	private static String testCaseName;
	private static String flowName;
	public static int count = 0;
	public static int blockFlag;
	public static String browser;

	@BeforeMethod(groups = { "regression" })
	public void baseClassSetUp() throws Exception {
		System.out.print("before");
	}

	@AfterMethod(groups = { "regression" }, alwaysRun = true)
	public void closeException() throws Exception {
		System.out.println("Next Test Case...");
	}

	@Test(enabled = true, dataProvider = "clients", dataProviderClass = DataProvide.class)
	public void startTest(String clientCode, String clientName, String clientURL, String testCaseName, List<String[]> params, String flowName) throws Exception {
		setAuthorInfoForReports();
		ATUReports.add("Pass Step", LogAs.PASSED, new CaptureScreen(ScreenshotOf.DESKTOP));
		count++;
		setClientCode(clientCode);
		setClientName(clientName);
		setTestCaseName(testCaseName);
		setFlowName(flowName);
		browser = getBrowser();
		WebDriver webDriver = getWebDriver();
		webDriver.get(clientURL);

		for (String[] param : params) {
			String labelname = param[0];
			String typeOfLabel = param[1];
			String fieldNameOrId = param[2];
			String inputsValue = param[3];

			switch (typeOfLabel) {
			case "date-select": {
				// TODO: we have to generalize this method.

				if (!inputsValue.equalsIgnoreCase("NOINPUTVALUE")) {
					String selectcssValue[] = inputsValue.split("\\|");

					String currentCalendarSelect = "";
					String loopSelect = "";
					if (selectcssValue.length > 1) {
						currentCalendarSelect = selectcssValue[0];
						loopSelect = selectcssValue[1];
					}

					TestUtility.WaitForSomeTime(5);
					webDriver.findElement(By.id(fieldNameOrId)).click();
					TestUtility.WaitForSomeTime(5);

					StringBuilder cssSelector = new StringBuilder(currentCalendarSelect);
					StringBuilder cssNextSelector = new StringBuilder(loopSelect);

					int maxMonths = 3;
					int i = 0;
					while (i < maxMonths) {
						try {
							webDriver.findElement(By.cssSelector(cssSelector.toString())).click();
							break;
						} catch (Exception e) {
							i++;
							webDriver.findElement(By.cssSelector(cssNextSelector.toString())).click();
							continue;
						}
					}
					TestUtility.WaitForSomeTime(10);
				}
			}
				break;
			case "text-box":
				TestUtility.WaitForSomeTime(2);
				if (!inputsValue.equalsIgnoreCase("NOINPUTVALUE")) {
					WebElement webElement = null;
					try {
						webElement = webDriver.findElement(By.name(fieldNameOrId));
						ATUReports.add("Pass Step", LogAs.PASSED, new CaptureScreen(ScreenshotOf.DESKTOP));
					} catch (Exception ex) {
						webElement = webDriver.findElement(By.id(fieldNameOrId));
					}
					webElement.sendKeys(inputsValue);
				}
				break;
			case "button":
				if (inputsValue.equalsIgnoreCase("Y")) {
					blockFlag++;
					TestUtility.WaitForSomeTime(10);
					webDriver.findElement(By.id(fieldNameOrId)).click();
				}
				break;

			case "button-xpath":
				if (!inputsValue.equalsIgnoreCase("NOINPUTVALUE")) {
					blockFlag++;
					WebDriverWait wait = new WebDriverWait(webDriver, 10);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(inputsValue)));
				}
				break;
			case "menu-selection":
				if (!inputsValue.equalsIgnoreCase("NOINPUTVALUE")) {
					blockFlag++;
				}
				break;

			case "check-box":
				if (inputsValue.equalsIgnoreCase("Y")) {
					blockFlag++;
					TestUtility.waitForPageToLoad(webDriver);
				}
				break;
			case "radio":
				if (inputsValue.equalsIgnoreCase("Y")) {
					blockFlag++;
					TestUtility.waitForPageToLoad(webDriver);
				}
				break;

			case "drop":
				if (!inputsValue.equalsIgnoreCase("NOINPUTVALUE")) {
					blockFlag++;
					WebElement webElement = webDriver.findElement(By.name(fieldNameOrId));
					Select toactSel = new Select(webElement);
					TestUtility.WaitForSomeTime(3);
					toactSel.selectByVisibleText(inputsValue);
				}
				break;
			case "drop-by-index":
				if (!inputsValue.equalsIgnoreCase("NOINPUTVALUE")) {
					blockFlag++;
					TestUtility.WaitForSomeTime(2);
					WebElement webElement = webDriver.findElement(By.id(fieldNameOrId));
					Select toactSel = new Select(webElement);
					toactSel.selectByIndex(Integer.valueOf(inputsValue));
				}
				break;
			case "xpath-drop":
				if (!inputsValue.equalsIgnoreCase("NOINPUTVALUE")) {
					blockFlag++;
					TestUtility.waitForPageToLoad(webDriver);
				}
				break;

			case "link":
				if (inputsValue.equalsIgnoreCase("Y")) {
					blockFlag++;

				}
				break;

			case "link-xpath":
				TestUtility.WaitForSomeTime(10);
				blockFlag++;
				webDriver.findElement(By.xpath(inputsValue)).click();
				break;
			case "link-css-selector":
				TestUtility.WaitForSomeTime(10);
				blockFlag++;
				TestUtility.WaitForSomeTime(3);
				webDriver.findElement(By.cssSelector(inputsValue)).click();

				break;
			case "alert-click":
				TestUtility.WaitForSomeTime(2);
				if (inputsValue.equalsIgnoreCase("Y")) {
					blockFlag++;
					TestUtility.closeAlertAndGetItsText(webDriver);
				}
				break;
			case "wait-for-pageload":
				blockFlag++;
				TestUtility.waitForPageToLoad(webDriver);
				break;
			case "Next":
				break;
			case "verify":
				if (!inputsValue.equalsIgnoreCase("NOINPUTVALUE")) {
					blockFlag++;
					TestUtility.WaitForSomeTime(5);
					try {
						Assert.assertTrue(webDriver.getPageSource().contains(inputsValue));
					} catch (AssertionError er) {
						logger.info("No inline text is matched with the given text");
						Assert.fail("No inline text is matched with the given text");
					}
					logger.info("The message is matched");
				}
				break;
			}
		}
	}

	public static String getClientCode() {
		return clientCode;
	}

	public static void setClientCode(String clientCode) {
		MainClass.clientCode = clientCode;
	}

	public static String getClientName() {
		return clientName;
	}

	public static void setClientName(String clientName) {
		MainClass.clientName = clientName;
	}

	public static String getTestCaseName() {
		return testCaseName;
	}

	public static void setTestCaseName(String testCaseName) {
		MainClass.testCaseName = testCaseName;
	}

	public static String getFlowName() {
		return flowName;
	}

	public static void setFlowName(String flowName) {
		MainClass.flowName = flowName;
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		MainClass.count = count;
	}

	public static int getBlockFlag() {
		return blockFlag;
	}

	public static void setBlockFlag(int blockFlag) {
		MainClass.blockFlag = blockFlag;
	}
}
