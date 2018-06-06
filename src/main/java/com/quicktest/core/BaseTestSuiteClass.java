package com.quicktest.core;

import java.io.File;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import atu.testng.reports.ATUReports;
import atu.testng.reports.logging.LogAs;
import atu.testng.reports.utils.Utils;
import atu.testng.selenium.reports.CaptureScreen;
import atu.testng.selenium.reports.CaptureScreen.ScreenshotOf;

import com.quicktest.utils.TestUtility;
import com.quicktest.utils.XlsxReader;
/**
 * 
 * @author Balaji
 *
 */
public class BaseTestSuiteClass {
	private Logger logger = Logger.getLogger(BaseTestSuiteClass.class);
	private WebDriver webDriver;
	private XlsxReader info;
	private String browser;

	@BeforeSuite
	public void beforesuite() throws Exception {
		// TODO: set wait here
	}

	@BeforeClass(groups = { "regression" })
	public void initialiseBaseSetup(ITestContext context) throws Exception {
		System.setProperty("atu.reporter.config", "src/main/resources/atu.properties");

		info = new XlsxReader(".." + File.separator + "Client_Input");
		browser = info.getCellData("info", 2, 3);
		webDriver = TestUtility.getWebDriverInstance(browser);
		ATUReports.setWebDriver(webDriver);
		ATUReports.add("Pass Step", LogAs.PASSED, new CaptureScreen(ScreenshotOf.DESKTOP));
	}

	public void setAuthorInfoForReports() {
		ATUReports.setAuthorInfo("Automation Tester", Utils.getCurrentTime(), "1.0");
	}

	@AfterClass(groups = { "regression" }, alwaysRun = true)
	public void closeBrowser() throws Exception {
		TestUtility.quitBrowser(webDriver);
	}

	@AfterSuite
	public void addLogFileToReport() throws Exception {
		String sSeperator = File.separator;
		String logFilePath = ".." + sSeperator + ".." + sSeperator + ".." + sSeperator + ".." + sSeperator + ".." + sSeperator + "Log" + sSeperator + "Log.log";
		Reporter.log("<br>");
		Reporter.log("<a href=" + logFilePath + ">Click here to see the Log output</a>");
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public XlsxReader getInfo() {
		return info;
	}

	public void setInfo(XlsxReader info) {
		this.info = info;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}
}
