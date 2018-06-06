package com.quicktest.flows;

import org.openqa.selenium.WebDriver;

import com.quicktest.utils.TestUtility;
import com.quicktest.utils.UserActions;
/**
 * 
 * @author Balaji
 *
 */
public class OnlineFlow extends UserActions {
	private WebDriver driver;
	private String browser;
	
	public static String tag = "";

	public OnlineFlow(WebDriver driver, String browserType) throws Exception {
		super(driver, browserType);
		setdriver(driver);
	}

	WebDriver getdriver() {
		return driver;
	}

	void setdriver(WebDriver driver) {
		this.driver = driver;
	}

	public UserActions startTest(String serverURL) throws Exception {
		String url = serverURL;
		driver.get(url);
		TestUtility.waitForPageToLoad(driver);
		return new UserActions(driver, browser);
	}
}
