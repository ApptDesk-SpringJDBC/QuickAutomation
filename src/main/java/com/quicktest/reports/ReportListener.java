package com.quicktest.reports;

import java.awt.AWTException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.quicktest.core.DataProvide;
import com.quicktest.core.MainClass;
import com.quicktest.utils.TestUtility;
import com.quicktest.utils.VideoCapture;
/**
 * 
 * @author Balaji
 * @author Swetha
 *
 */

public class ReportListener extends TestListenerAdapter {
	private Logger logger = Logger.getLogger(ReportListener.class);
	public static int passCount = 0, failCount = 0, skipCount = 0, blockCount = 0;
	private static BufferedWriter out = null;
	private static FileWriter fileStream = null;
	public static String reportHTML;
	private static boolean videoOn;
	public static Date startTime;
	public static Date endTime;

	public void onTestFailure(ITestResult result) {
		try {
			System.out.println("Test Case Failed");
			failCount++;
			out.write("<tr>\n");
			out.write("<td width=150 align=left nowrap='nowrap'><FONT COLOR=#090909 FACE=verdana SIZE=2.75><b>" + MainClass.getClientName() + "</b></td>\n");
			out.write("<td width=150 align=left nowrap='nowrap'><FONT COLOR=#090909 FACE=verdana SIZE=2.75><b>" + MainClass.getClientCode() + "</b></td>\n");
			out.write("<td width=150 align=left nowrap='nowrap'><FONT COLOR=#090909 FACE=verdana SIZE=2.75><b>" + MainClass.getFlowName() + "</b></td>\n");
			out.write("<td width=180 align=left nowrap='nowrap'><FONT COLOR=#090909 FACE=verdana SIZE=2.75>" + MainClass.getTestCaseName() + "</td>\n");
			if (MainClass.getBlockFlag() > 1) {
				out.write("<td width=150 align=left><FONT COLOR=#ff0000 FACE=verdana SIZE=2.75><b>Fail</b></td>\n");
			} else {
				out.write("<td width=150 align=left nowrap='nowrap'><FONT COLOR=#ff0000 FACE=verdana SIZE=2.75><b>Blocked</b></td>\n");
				failCount--;
				blockCount++;
			}

			out.write("</tr>\n");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		logger.error("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n");
		logger.error("ERROR ----------" + MainClass.getTestCaseName() + " has failed");
		logger.error("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n");
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		Reporter.setCurrentTestResult(result);
		try {
			VideoCapture.stopVideoCapture(result.getName());
		} catch (AWTException ex) {
			ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void onTestSkipped(ITestResult result) {
		skipCount++;
		int totalTC = MainClass.count + skipCount;

		logger.warn("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		logger.warn("WARN ------------" + DataProvide.resultData[totalTC - 1][2] + " has skipped");
		logger.warn("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		try {
			out.write("<tr>\n");
			out.write("<td width=150 align=left nowrap='nowrap'><FONT COLOR=#090909 FACE=verdana SIZE=2.75><b>" + DataProvide.resultData[totalTC - 1][1] + "</b></td>\n");
			out.write("<td width=150 align=left nowrap='nowrap'><FONT COLOR=#090909 FACE=verdana SIZE=2.75><b>" + DataProvide.resultData[totalTC - 1][0] + "</b></td>\n");
			out.write("<td width=150 align=left nowrap='nowrap'><FONT COLOR=#090909 FACE=verdana SIZE=2.75><b>" + DataProvide.resultData[totalTC - 1][6] + "</b></td>\n");
			out.write("<td width=180 align=left nowrap='nowrap'><FONT COLOR=#090909 FACE=verdana SIZE=2.75>" + DataProvide.resultData[totalTC - 1][2] + "</td>\n");
			out.write("<td width=150 align=left><FONT COLOR=#0000ff FACE=verdana SIZE=2.75><b>Skipped</b></td>\n");
			out.write("</tr>\n");
		} catch (Exception exception1) {
			exception1.printStackTrace();
		}
	}

	public void onTestSuccess(ITestResult result) {
		try {
			passCount++;
			out.write("<tr>\n");
			out.write("<td width=150 align=left nowrap='nowrap'><FONT COLOR=#090909 FACE=verdana SIZE=2.75><b>" + MainClass.getClientName() + "</b></td>\n");
			out.write("<td width=150 align=left nowrap='nowrap'><FONT COLOR=#090909 FACE=verdana SIZE=2.75><b>" + MainClass.getClientCode() + "</b></td>\n");
			out.write("<td width=150 align=left nowrap='nowrap'><FONT COLOR=#090909 FACE=verdana SIZE=2.75><b>" + MainClass.getFlowName() + "</b></td>\n");
			out.write("<td width=180 align=left nowrap='nowrap'><FONT COLOR=#090909 FACE=verdana SIZE=2.75>" + MainClass.getTestCaseName() + "</td>\n");

			out.write("<td width=150 align=left><FONT COLOR=#008000 FACE=verdana SIZE=2.75><b>Pass</b></td>\n");
			out.write("</tr>\n");
		} catch (Exception exception1) {
			exception1.printStackTrace();
		}
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		logger.info("###############################################################");
		logger.info("SUCCESS ---------" + MainClass.getTestCaseName() + " has passed");
		logger.info("###############################################################");
		Reporter.setCurrentTestResult(result);
		try {
			VideoCapture.stopVideoCapture(result.getName());
			try {
				TestUtility.WaitForSomeTime(2);
			} catch(Exception e) {
				logger.error("Failed!");
			}
		} catch (AWTException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void onTestStart(ITestResult result) {
		logger.info("start test!!");
		try {
			VideoCapture.startVideoCapture();
			videoOn = true;
		} catch (AWTException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		}

	}

	public void onStart(ITestContext context) {
		try {
			startTime = Calendar.getInstance().getTime();
			String sSeperator = File.separator;
			String dir = ".";
			File src = new File(".");
			String[] rootFolders = src.list();
			String realF = "";
			for (String root : rootFolders) {
				if (root.startsWith("Automation_Report")) {
					realF = root;
					File resultPath = new File(dir + File.separator + realF);
					FileUtils.deleteDirectory(resultPath);
				}
			}

			if (new File(dir + sSeperator + "exports").exists()) {
				File exportpath = new File(dir + sSeperator + "exports");
				FileUtils.cleanDirectory(exportpath);
			}

			// Create file
			reportHTML = "emailReport" + sSeperator + "email.html";
			fileStream = new FileWriter(reportHTML);
			out = new BufferedWriter(fileStream);

			// out.newLine();
			out.write("<html>\n");
			out.write("<HEAD>\n");
			out.write(" <TITLE>Automation Test Results</TITLE>\n");
			out.write("</HEAD>\n");
			out.write("<body>\n");
			out.write("<br>");
			out.write("<br>");
			out.write("<h4> <FONT COLOR=#660000 FACE=verdana SIZE=3.5> <u>Flow Results:</u></h4>\n");
			out.write("<table  border=1 cellspacing=1 cellpadding=1 >\n");
			out.write("<tr>\n");

			out.write("<tr>\n");
			out.write("<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=verdana SIZE=2.75><b>Client Name</b></td>\n");
			out.write("<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=verdana SIZE=2.75><b>Client Code</b></td>\n");
			out.write("<td width=180 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=verdana SIZE=2.75><b>Flow Name</b></td>\n");
			out.write("<td width=180 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=verdana SIZE=2.75><b>Test Case Name</b></td>\n");
			out.write("<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=verdana SIZE=2.75><b>Result</b></td>\n");
			out.write("</tr>\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onFinish(ITestContext context) {
		endTime = Calendar.getInstance().getTime();
		videoOn = false;
		try {
			out.write("</table>\n");
			out.write("<br>\n");
			out.write("<br>\n");
			out.write("</body>\n");
			out.write("</html>");
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					logger.error("Error while closing the out stream");
				}

			}
			EmailReportClient.emailHtmlSummaryReport();
			// TODO back up the logs.

		} catch (Exception e) {
			logger.error("Error in on finish");
		}
	}
}
