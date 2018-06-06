package com.quicktest.reports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.quicktest.core.DataProvide;
import com.quicktest.core.MainClass;
import com.quicktest.utils.TestUtility;
import com.telappoint.common.utils.BaseEmailRequest;
import com.telappoint.common.utils.MailUtils;

/**
 * @author Balaji N
 */
public class EmailReportClient {
	private static Logger logger = Logger.getLogger(EmailReportClient.class);

	public static void emailHtmlSummaryReport() throws Exception {
		// TODO: parameterize
		if ("true".equalsIgnoreCase("true")) {
			BufferedReader br = null;
			try {
				File fin = new File(ReportListener.reportHTML);
				FileInputStream fis = new FileInputStream(fin);
				br = new BufferedReader(new InputStreamReader(fis));

				String strLine = null;
				// create the message part

				String browser = MainClass.browser;
				StringBuilder builder = new StringBuilder();
				int continueFlag = 1;
				while ((strLine = br.readLine()) != null) {
					if (strLine.startsWith("<table")) {
						continueFlag = 2;
					}
					if (strLine.equalsIgnoreCase("</table>"))
						continueFlag = 1;

					if (continueFlag == 2) {
						continue;
					}

					if (strLine.equalsIgnoreCase("</body>")) {

						builder.append("<table name=\"outputSummary\" border=1 cellspacing=1 cellpadding=1>\n");
						builder.append("<tr>\n");
						builder.append("<td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= verdana  SIZE=2.75><b>Total Clients</b></td>\n");
						builder.append("<td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= verdana  SIZE=2.75><b>Total Flows</b></td>\n");
						builder.append("<td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= verdana  SIZE=2.75><b>Total Test Cases</b></td>\n");
						builder.append("<td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= verdana  SIZE=2.75><b>Passed Test Cases</b></td>\n");
						builder.append("<td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= verdana  SIZE=2.75><b>Failed Test Cases</b></td>\n");
						builder.append("<td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= verdana  SIZE=2.75><b>Skipped Test Cases</b></td>\n");
						builder.append("<td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= verdana  SIZE=2.75><b>Blocked Test Cases</b></td>\n");
						builder.append("</tr>\n");

						builder.append("<tr>\n");
						builder.append("<td width=250 align= left ><FONT COLOR=#090909 FACE= verdana  SIZE=2.75>").append(DataProvide.noOfClients.get()).append("</td>\n");
						builder.append("<td width=250 align= left ><FONT COLOR=#090909 FACE= verdana  SIZE=2.75>").append(DataProvide.noOfFlows.get()).append("</td>\n");
						int totalCount = MainClass.count + ReportListener.skipCount;
						builder.append("<td width=250 align= left ><FONT COLOR=#090909 FACE= verdana  SIZE=2.75>").append(totalCount).append("</td>\n");
						builder.append("<td width=250 align= left ><FONT COLOR=#090909 FACE= verdana  SIZE=2.75>").append(ReportListener.passCount).append("</td>\n");
						builder.append("<td width=250 align= left ><FONT COLOR=#090909 FACE= verdana  SIZE=2.75>").append(ReportListener.failCount).append("</td>\n");
						builder.append("<td width=250 align= left ><FONT COLOR=#090909 FACE= verdana  SIZE=2.75>").append(ReportListener.skipCount).append("</td>\n");
						builder.append("<td width=250 align= left ><FONT COLOR=#090909 FACE= verdana  SIZE=2.75>").append(ReportListener.blockCount).append("</td>\n");
						builder.append("</tr>\n");
						builder.append("</table>\n");

						builder.append("<BR/><BR/>");
						builder.append("<FONT COLOR=#090909 FACE= verdana  SIZE=2><u><b>*Note:</b></u>");

						java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();

						builder.append("<br/>");
						builder.append("<br/>");

						String temp1 = System.getProperty("user.dir");
						String temp2 = temp1.replace(":", "$");
						builder.append("<FONT COLOR=#090909 FACE= verdana  SIZE=2>1. The Screenshots of for failed scenarios and video of the entire scenarios are available @: <u>\\").append("\\");
						builder.append(localMachine.getHostName()).append("\\").append(temp2).append("\\").append("Automation_Report").append(TestUtility.getCurrentDateStr()).append("</u> .");
						builder.append("<BR/>");
						builder.append("<FONT COLOR=#090909 FACE= verdana  SIZE=2>2. For Detailed Results please refer to the corresponding HTML file @: <u>\\").append("\\")
								.append(localMachine.getHostName()).append("\\");
						builder.append(temp2).append("\\test-output\\SampleSuite-Linear</u> .");

						builder.append("<BR/>");
						builder.append("<FONT COLOR=#090909 FACE= verdana  SIZE=2>3. Status Legend:");
						builder.append("<BR/>");
						builder.append("<FONT COLOR=#090909 FACE= verdana  SIZE=2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Blocked:</b>&nbsp;Test case is blocked due to absence of all the inputs (Or) There is a problem before loading the page");
						builder.append("<BR/>");
						builder.append("<FONT COLOR=#090909 FACE= verdana  SIZE=2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>FAIL:</b>Test case is failed due to failure of a functionality.");
						builder.append("<BR/>");
						builder.append("<FONT COLOR=#090909 FACE= verdana  SIZE=2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Skipped:</b>&nbsp; In some cases, if there is a problem before the actual test case starts");
						builder.append("<BR/>").append("<P><FONT COLOR=#090909 FACE= verdana  SIZE=2>Regards,").append("<BR/>").append("TelAppointment Automation Team</P>");
					}

					builder.append(strLine);
					if (strLine.equalsIgnoreCase("<body>")) {
						builder.append("<P><FONT COLOR=#090909 FACE= verdana  SIZE=2>Dear Administrator,<BR/> <P><FONT COLOR=#090909 FACE= verdana  SIZE=2> Telappointment Automation suite for Clients Testing has been executed successfully for selected Clients. Please find the Summary of Test Results below.</P>");
						builder.append("<table width=65%>\n");
						builder.append("<tr>\n");
						builder.append("<td align=center width=65%><h5><FONT COLOR=#0000FF FACE=verdana SIZE=4><b><u>Telappointment Automation Test Results</u></b></h5></td>\n");
						builder.append("</tr>\n");
						builder.append("</table>\n");

						builder.append("<h4> <FONT COLOR=#660000 FACE=verdana SIZE=3.5> <u>Summary of Test Results:</u></h4>\n");
						builder.append("<table  border=1 cellspacing=1 cellpadding=1 >\n");
						builder.append("<tr>\n");

						builder.append("<tr>\n");
						builder.append("<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=verdana SIZE=2.75><b>Run StartTime</b></td>\n");
						builder.append("<td width=180 align=left><FONT COLOR=#090909 FACE=verdana SIZE=2.75>").append(ReportListener.startTime.toString()).append("</td>\n");
						builder.append("</tr>\n");

						builder.append("<tr>\n");
						builder.append("<td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= verdana  SIZE=2.75><b>Run EndTime</b></td>\n");
						builder.append("<td width=180 align= left ><FONT COLOR=#090909 FACE= verdana  SIZE=2.75>").append(ReportListener.endTime.toString()).append("</td>\n");
						builder.append("</tr>\n");

						builder.append("<tr>\n");
						builder.append("<td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= verdana  SIZE=2.75><b>Executed By</b></td>\n");
						// TODO: username parameterize
						builder.append("<td width=150 align= left ><FONT COLOR=#090909 FACE= verdana  SIZE=2.75>").append("Balaji").append("</td>\n");
						builder.append("</tr>\n");
						builder.append("<tr>\n");
						builder.append("<td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= verdana  SIZE=2.75><b>Browser Type</b></td>\n");
						// TODO: parameterize the browser version
						builder.append("<td width=250 align= left ><FONT COLOR=#090909 FACE= verdana  SIZE=2.75>").append(browser).append(" v").append("43.0.0").append("</td>\n");
						builder.append("</tr>\n");
						builder.append("</table>\n");
					}
				}

				MimeBodyPart attachFilePart = new MimeBodyPart();
				String emailBodyMessage = builder.toString();
				builder.setLength(0);
				builder.append(fin.toString());
				FileDataSource fds = new FileDataSource(builder.toString());
				attachFilePart.setDataHandler(new DataHandler(fds));
				attachFilePart.setFileName("Test-Flow-Results.html");

				Multipart multipart = new MimeMultipart("alternative");
				multipart.addBodyPart(attachFilePart);

				BaseEmailRequest baseEmailRequest = new BaseEmailRequest();
				baseEmailRequest.setEmailThroughInternalServer(false);
				baseEmailRequest.setFromAddress("support@quick.com");
				baseEmailRequest.setToAddress("balajinsr@gmail.com");//
				baseEmailRequest.setSubject("Telappointment Results");
				baseEmailRequest.setEmailBody(emailBodyMessage);
				MailUtils.sendEmail(null, baseEmailRequest, multipart);
			} catch (Exception e) {
				logger.error("Problem sending email");
				logger.error("Error:" + e.getMessage());
			} finally {
				if (br != null)
					br.close();
			}
		}
	}
}
