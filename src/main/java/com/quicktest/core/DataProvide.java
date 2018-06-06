package com.quicktest.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;

import com.quicktest.utils.XlsxReader;

/**
 * 
 * @author Balaji
 *
 */

public class DataProvide {
	private static Logger loggers = Logger.getLogger(DataProvide.class);
	public static AtomicInteger noOfClients = new AtomicInteger(0);
	public static AtomicInteger noOfFlows = new AtomicInteger(0);
	public static Object[][] resultData;

	@DataProvider(name = "clients")
	public static Object[][] getData() throws Exception {
		noOfClients.getAndSet(0);
		int rows;
		XlsxReader Input = new XlsxReader(".." + File.separator + "Client_Input");
		ArrayList<Object> dataList = new ArrayList<Object>();
		if (Input.isSheetExist("main")) {
			rows = Input.getRowCount("main");
			for (int i = 2; i <= rows; i++) {
				String execute = Input.getCellData("main", 3, i);
				String serverURL = Input.getCellData("main", 4, i);
				String flowName = "";
				if (execute.equalsIgnoreCase("Y")) {
					noOfClients.getAndIncrement();
					String clientCode = Input.getCellData("main", 0, i);
					String clientName = Input.getCellData("main", 1, i);
					XlsxReader clientData = new XlsxReader(clientCode);
					int numberOfSheets = clientData.getNumberOfSheets();
					List<String> flows = new ArrayList<String>();
					if (clientData.isSheetExistButRuns("FlowController")) {
						for (int r = 3; r <= clientData.getRowCountUntilEmptyCell("FlowController", 1, 1); r++) {
							if (clientData.getCellData("FlowController", "RunMode", r, 1, 1).equalsIgnoreCase("Y")) {
								flows.add(clientData.getCellData("FlowController", "FlowName", r, 1, 1));
							}
						}
					} else {
						for (int sheetcount = 1; sheetcount < numberOfSheets; sheetcount++) {
							flowName = clientData.getSheetNameOfIndex(sheetcount);
							flows.add(flowName);
						}
					}

					for (String flow : flows) {
						noOfFlows.getAndIncrement();
						flowName = flow;
						for (int r = 5; r <= clientData.getRowCount(flowName); r++) {
							String testCaseName = clientData.getCellData(flowName, 2, r);
							Object[] dataLine = new Object[6];
							dataLine[0] = clientCode;
							dataLine[1] = clientName;
							dataLine[2] = serverURL;

							dataLine[3] = testCaseName;
							ArrayList<String[]> params = new ArrayList<String[]>();
							for (int col = 3; !clientData.getCellData(flowName, col, 2).isEmpty(); col++) {
								String labelName = clientData.getCellData(flowName, col, 2);
								String labelType = clientData.getCellData(flowName, col, 3);
								String fieldNameOrId = clientData.getCellData(flowName, col, 4);
								if (clientData.getCellData(flowName, col, 3).isEmpty())
									labelType = "NOINPUTVALUE";

								String value;
								if (clientData.getCellData(flowName, col, r).isEmpty()) {
									value = "NOINPUTVALUE";
								} else {
									value = clientData.getCellData(flowName, col, r);
								}
								params.add(new String[] { labelName, labelType, fieldNameOrId, value });
								dataLine[4] = params;

							}
							dataLine[5] = flowName;
							dataList.add(dataLine);
						}
					}
				}
			}
		}
		if (dataList.size() < 1) {
			loggers.info("No clients selected");
		}

		Object[][] data = new Object[dataList.size()][];
		for (int i = 0; i < dataList.size(); i++) {
			data[i] = (Object[]) dataList.get(i);
		}
		resultData = data;
		return resultData;
	}
}
