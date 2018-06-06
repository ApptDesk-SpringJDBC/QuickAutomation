package com.quicktest.flows;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.quicktest.utils.UserActions;
/**
 * 
 * @author Balaji
 *
 */

public class AdminFlow extends UserActions {
	private Logger logger = Logger.getLogger(AdminFlow.class);
	private WebDriver driver;

	public AdminFlow(WebDriver driver, String browserType) throws Exception {
		super(driver, browserType);
		setdriver(driver);
	}

	public WebDriver getdriver() {
		return driver;
	}

	void setdriver(WebDriver driver) {
		this.driver = driver;
	}

}
