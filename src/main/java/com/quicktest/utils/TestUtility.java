package com.quicktest.utils;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.telappoint.common.utils.PropertyUtils;

/**
 *
 * @author Balaji
 */
public class TestUtility {

	private static final Logger logger = Logger.getLogger(TestUtility.class.getName());

	public static WebDriver getWebDriverInstance(String browserType) {
		WebDriver webDriver = null;
		if (null == browserType || "".equals(browserType.trim())) {
			webDriver = new FirefoxDriver();
		} else if ("firefox".equalsIgnoreCase(browserType)) {
			webDriver = new FirefoxDriver();
		} else if ("iexplorer".equalsIgnoreCase(browserType)) {
			System.setProperty("webdriver.ie.driver", "drivers/IEDriverServer.exe");
			webDriver = new InternetExplorerDriver();
		} else if ("chrome".equalsIgnoreCase(browserType)) {
			System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
			webDriver = new ChromeDriver();
		} else {
			webDriver = new FirefoxDriver();
		}
		webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		return webDriver;
	}

	public static void waitForConditionByXpath(final WebDriver driver, final String xpath) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver arg0) {
				return driver.findElement(By.xpath(xpath));
			}
		});
	}

	public static void waitForConditionByName(final WebDriver driver, final String name) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver arg0) {
				return driver.findElement(By.name(name));
			}
		});
	}

	private static String pageLoadStatus = null;
	private static JavascriptExecutor js;

	public static String getPageLoadStatus() {
		return pageLoadStatus;
	}

	public static void setPageLoadStatus(String pageLoadStatus) {
		TestUtility.pageLoadStatus = pageLoadStatus;
	}

	public static JavascriptExecutor getJscriptExecutor() {
		return js;
	}

	public static void setJscriptExecutor(JavascriptExecutor js) {
		TestUtility.js = js;
	}

	public static Alert switchToAlert(WebDriver driver) {
		WebDriver.TargetLocator locator = driver.switchTo();
		try {
			Method alertMethod = locator.getClass().getMethod("alert");
			alertMethod.setAccessible(true);
			return (Alert) alertMethod.invoke(locator);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String closeAlertAndGetItsText(WebDriver driver) {
		boolean acceptNextAlert = true;
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

	public static void quitBrowser(WebDriver webDriver) {
		webDriver.quit();
	}

	public static String getBrowserName(String browserType) {
		if ("firefox".equals(browserType)) {
			return "Mozilla Firefox";
		} else if ("iexplorer".equals(browserType)) {
			return "Intenet Explorer";
		} else if ("chrome".equals(browserType)) {
			return "Google Chrome";
		} else {
			return "Mozilla Firefox";
		}
	}

	/**
	 * Method - Waits for an element till the timeout expires
	 * 
	 * @param driver
	 * @param bylocator
	 * @param iWaitTime
	 * @return - Boolean (returns True when element is located within timeout
	 *         period else returns false)
	 * @throws Exception
	 */
	public static boolean WaitForElementExistbutNotFail(WebDriver driver, By bylocator, int iWaitTime) throws Exception {
		boolean bFlag = false;
		WebDriverWait wait = new WebDriverWait(driver, iWaitTime);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(bylocator));
			if (driver.findElement(bylocator).isDisplayed() || driver.findElement(bylocator).isEnabled()) {
				bFlag = true;
				logger.info("Element " + bylocator + " is displayed");
			}
		}

		catch (NoSuchElementException e) {
			e.printStackTrace();
			logger.info("Element " + bylocator + " is not displayed" + "\n" + e.getMessage());
			bFlag = false;
		}

		catch (Exception e) {
			e.printStackTrace();
			logger.info("Element " + bylocator + " is not displayed" + "\n" + e.getMessage());
			bFlag = false;
		}
		return bFlag;
	}

	public static boolean WaitForElementExist(WebDriver driver, By bylocator, int iWaitTime) throws Exception {
		boolean bFlag = false;
		WebDriverWait wait = new WebDriverWait(driver, iWaitTime);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(bylocator));
			if (driver.findElement(bylocator).isDisplayed() || driver.findElement(bylocator).isEnabled()) {
				bFlag = true;
				logger.info("Element " + bylocator + " is displayed");
			}
		}

		catch (NoSuchElementException e) {
			e.printStackTrace();
			logger.info("Element " + bylocator + " is not displayed" + "\n" + e.getMessage());
			bFlag = false;
			Assert.fail("Element " + bylocator + " is not displayed" + "\n" + e.getMessage());
		}

		catch (Exception e) {
			e.printStackTrace();
			logger.info("Element " + bylocator + " is not displayed" + "\n" + e.getMessage());

			bFlag = false;
			Assert.fail("Element " + bylocator + " is not displayed" + "\n" + e.getMessage());
		}
		return bFlag;
	}

	public static boolean WaitForSomeTime(int iWaitTime) throws Exception {
		boolean bFlag = false;

		try {
			Thread.sleep(iWaitTime * 1000);
		}

		catch (InterruptedException e) {
			e.printStackTrace();
			logger.info("Wait for" + iWaitTime + "seconds");
			bFlag = false;

		}

		catch (Exception e) {
			e.printStackTrace();
			logger.info("Wait for" + iWaitTime + "seconds");
			bFlag = false;

		}
		return bFlag;
	}

	/**
	 * Method - Waits for an element till the element is clickable
	 * 
	 * @param driver
	 * @param bylocator
	 * @param iWaitTime
	 * @return - Boolean (returns True when element is located within timeout
	 *         period and is clickable else returns false)
	 * @throws Exception
	 */
	public static boolean WaitUntilClickable(WebDriver driver, By bylocator, int iWaitTime) throws Exception {
		boolean bFlag = false;
		WebDriverWait wait = new WebDriverWait(driver, iWaitTime);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(bylocator));
			if (driver.findElement((bylocator)).isDisplayed()) {
				bFlag = true;
				logger.info("Element " + bylocator + " is displayed and is clickable");
			}
		}

		catch (NoSuchElementException e) {
			e.printStackTrace();
			logger.info("Element " + bylocator + " is not displayed" + "\n" + e.getMessage());

			bFlag = false;
			Assert.fail("Element " + bylocator + " is not displayed" + "\n" + e.getMessage());
		}

		catch (Exception e) {
			e.printStackTrace();
			logger.info("Element " + bylocator + " is not displayed" + "\n" + e.getMessage());

			bFlag = false;
			Assert.fail("Element " + bylocator + " is not displayed" + "\n" + e.getMessage());
		}
		return bFlag;
	}

	/**
	 * Method - Waits for an element till the element is visible
	 * 
	 * @param driver
	 * @param bylocator
	 * @param iWaitTime
	 * @return - Element (returns the element)
	 * @throws Exception
	 */
	public static boolean waitForVisibility(WebDriver driver, By bylocator, int iWaitTime) throws Exception {
		boolean bFlag = false;
		WebDriverWait wait = new WebDriverWait(driver, iWaitTime);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(bylocator));
			if (driver.findElement((bylocator)).isDisplayed()) {
				bFlag = true;
				logger.info("Element " + bylocator + " is displayed");
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			logger.info("Element " + bylocator + " is not displayed" + "\n" + e.getMessage());

			Assert.fail("Element " + bylocator + " is not displayed." + "\n" + e.getMessage());
			bFlag = false;
		}

		catch (Exception e) {
			e.printStackTrace();
			logger.info("Element " + bylocator + " is not displayed" + "\n" + e.getMessage());

			Assert.fail("Element " + bylocator + " is not displayed." + "\n" + e.getMessage());
			bFlag = false;
		}
		return bFlag;
	}

	/**
	 * Wait until a page loads - verifies by checking until the browser page
	 * load status set to "complete"
	 * 
	 * @param current
	 *            Webdriver - getDriver()
	 * @throws Exception
	 */
	public static void waitForPageToLoad(WebDriver driver) throws Exception {
		try {
			int iWaitTime = 0;
			do {
				setJscriptExecutor((JavascriptExecutor) driver);
				setPageLoadStatus((String) getJscriptExecutor().executeScript("return document.readyState"));
				System.out.print(".");
				Thread.sleep(500);
				iWaitTime++;
				if (iWaitTime > 500) {
					break;
				}
			} while (!getPageLoadStatus().equals("complete"));

			if (!getPageLoadStatus().equals("complete")) {

				logger.info("Unable to load webpage");
				Assert.fail("unable to load webpage");

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Unable to load web page" + "\n" + e.getMessage());

			Assert.fail("unable to load webpage" + "\n" + e.getMessage());
		}

	}

	/**
	 * WaitforPageToload Until a particular text appears on the screen
	 * 
	 * @param driver
	 *            webdriver instance
	 * @param UItext
	 *            appears until which webdriver should wait
	 */
	public static void waitForPageToLoad(WebDriver driver, final String text) {
		// wait for the application to get fully loaded

		(new WebDriverWait(driver, 20)).until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver d) {

				return d.findElement(By.partialLinkText(text));
			}
		});
	}

	/**
	 * Purpose- Wait for an element till it is either invisible or not present
	 * on the DOM.
	 * 
	 * @param driver
	 * @param Locator
	 * @param iWaitTime
	 * @return - Element (returns the element)
	 * @throws Exception
	 */
	public static boolean WaitForElementDisappear(WebDriver driver, By Locator, int iWaitTime) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, iWaitTime);
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(Locator));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Element " + Locator + " is not disappearing" + "\n" + e.getMessage());
			Assert.fail("Element " + Locator + " is not disappearing." + "\n" + e.getMessage());
			return false;
		}

	}

	/**
	 * 
	 * Purpose- to verify if the element exists or not
	 * 
	 * @param driver
	 * @param bylocator
	 * @param iWaitTime
	 * @return - Boolean (returns True when element is located within timeout
	 *         period else returns false)
	 * @throws Exception
	 */
	public static boolean safeIsElementDisplayed(WebDriver driver, By bylocator, int iWaitTime) throws Exception {
		boolean bFlag = false;

		WebDriverWait wait = new WebDriverWait(driver, iWaitTime);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(bylocator));
			if (driver.findElement(bylocator).isDisplayed()) {
				bFlag = true;
				highlightElement(driver, bylocator);
				logger.info("Element " + bylocator + " is displayed");
			}
		}

		catch (NoSuchElementException e) {
			bFlag = false;
			logger.info("Element " + bylocator + " is not displayed");
			e.printStackTrace();

		}

		catch (Exception e) {
			bFlag = false;
			e.printStackTrace();
			logger.info("Element " + bylocator + " is not displayed");
		}
		return bFlag;
	}

	/**
	 * @Method Highlights on current working element or locator
	 * @param Webdriver
	 *            instance
	 * @param WebElement
	 * @return void (nothing)
	 */
	public static void setHighlight(WebDriver driver, WebElement element) throws Exception {
		String value = PropertyUtils.getValueFromProperties("HighlightElements", "TestingProperties.properties");
		if (value.equalsIgnoreCase("true")) {
			String attributevalue = "border:3px solid red;";
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			String getattrib = element.getAttribute("style");
			executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, attributevalue);
			Thread.sleep(100);
			executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, getattrib);
		}
	}

	/**
	 * Method: Highlights on current working element or locator
	 * 
	 * @param driver
	 * @param locator
	 * @throws Exception
	 */
	public static void highlightElement(WebDriver driver, By locator) throws Exception {

		WebElement element = driver.findElement(locator);
		String value = PropertyUtils.getValueFromProperties("HighlightElements", "TestingProperties.properties");
		if (value.equalsIgnoreCase("true")) {
			String attributevalue = "border:3px solid green;";
			// change border width and colour values if required
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			String getattrib = element.getAttribute("style");
			executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, attributevalue);
			Thread.sleep(100);
			executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, getattrib);
		}
	}

	public static int selectTimeCountToWait(String WaitTime) throws Exception {
		int iSecondsToWait;

		if (WaitTime.equals("SHORTWAIT")) {
			String wait = PropertyUtils.getValueFromProperties("SHORTWAIT", "TestingProperties.properties");
			iSecondsToWait = Integer.parseInt(wait);
		} else if (WaitTime.equals("NORMALWAIT")) {
			String wait = PropertyUtils.getValueFromProperties("NORMALWAIT", "TestingProperties.properties");
			iSecondsToWait = Integer.parseInt(wait);
		} else if (WaitTime.equals("LONGWAIT")) {
			String wait = PropertyUtils.getValueFromProperties("LONGWAIT", "TestingProperties.properties");
			iSecondsToWait = Integer.parseInt(wait);
		} else {
			String wait = PropertyUtils.getValueFromProperties("VERYLONGWAIT", "TestingProperties.properties");
			iSecondsToWait = Integer.parseInt(wait);
		}
		return iSecondsToWait;
	}

	public static String removeErrorNumber(String stMethodName) {
		int lastIndex = stMethodName.lastIndexOf(":");
		return stMethodName.substring(0, lastIndex);
	}

	public static String getMethodName(final int depth) {
		StackTraceElement stackTraceElements[] = (new Throwable()).getStackTrace();
		return stackTraceElements[depth].toString();
	}
	
	public static String getCurrentDateStr() {
		DateFormat outputFormat = new SimpleDateFormat("yyyy-mm-dd_HH_mm_ss");
		return outputFormat.format(new Date());
	}
}
