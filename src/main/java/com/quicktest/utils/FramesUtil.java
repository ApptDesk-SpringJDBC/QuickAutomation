package com.quicktest.utils;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
/**
 * 
 * @author Balaji
 *
 */

//TODO: not use
public class FramesUtil {
	private static Logger logger = Logger.getLogger(FramesUtil.class);
	/**
	 * Method for switching to frame using frame id
	 * 
	 * @param driver
	 * @param frame
	 */
	public static void selectFrame(WebDriver driver, String frame) {
		try {
			driver.switchTo().frame(frame);
			logger.info("Navigated to frame with id " + frame);
		} catch (Exception e) {
			logger.error("Unable to navigate to frame with id " + frame + e.getMessage());
			Assert.fail("Unable to navigate to frame with id " + frame + "\n" + e.getMessage());
		}
	}

	/**
	 * Method - Method for switching to frame using any locator of the frame
	 * 
	 * @param driver
	 * @param ParentFrame
	 * @param ChildFrame
	 */
	public static void selectFrame(WebDriver driver, By FrameLocator) {
		try {
			WebElement Frame = driver.findElement(FrameLocator);
			driver.switchTo().frame(Frame);
			logger.info("Navigated to frame with locator " + FrameLocator);
		} catch (Exception e) {
			logger.error("Unable to navigate to frame with locator " + FrameLocator + e.getMessage());
			Assert.fail("Unable to navigate to frame with locator " + FrameLocator + "\n" + e.getMessage());
		}
	}

	/**
	 * Method - Method for switching back to webpage from frame
	 * 
	 * @param driver
	 */
	public static void defaultFrame(WebDriver driver) {
		try {
			driver.switchTo().defaultContent();
			logger.info("Navigated to back to webpage from frame");
		} catch (Exception e) {
			logger.error("unable to navigate back to webpage from frame");
			Assert.fail("unable to navigate back to webpage from frame" + "\n" + e.getMessage());
		}
	}

}
