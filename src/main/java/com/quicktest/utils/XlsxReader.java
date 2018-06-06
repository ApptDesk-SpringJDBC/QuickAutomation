package com.quicktest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * 
 * @author Balaji
 *
 */

public class XlsxReader {
	private static Logger logger = Logger.getLogger(XlsxReader.class);
	private XSSFWorkbook workbook;
	private XSSFSheet Sheet;
	private XSSFRow row;
	private XSSFCell cell;
	private FileInputStream fis;
	private int iIndex;

	/**
	 * Purpose- Constructor to pass Excel file name
	 * 
	 * @param sFileName
	 */
	public XlsxReader(String sFileName) {
		try {
			String dir = null;
			File directory = new File(".");
			dir = directory.getCanonicalPath();
			String sFilePath = dir + File.separator + "Data" + File.separator + "Controllers" + File.separator + sFileName + ".xlsx";
			File file = new File(sFilePath);
			if (file.exists()) {
				fis = new FileInputStream(sFilePath);
				workbook = new XSSFWorkbook(fis);

				fis.close();
			} else {
				// TODO: logger ("File with name-'" + sFileName +
				// "' doesn't exists in Data Folder, Please Re-check given file name",
				// "Config.properties");
				System.exit(0);
			}
		} catch (Exception e) {
			logger.error("Exceptione is =" + e.getMessage());
			// TODO: logger(e.getMessage(), "Exception");
			System.exit(0);
		}
	}

	/**
	 * Purpose- To check if the sheet with given name exists or not
	 * 
	 * @param sheetName
	 *            - Sheet name should be provided
	 * @return - if sheet with specified name exists it returns true else it
	 *         returns false
	 * @throws Exception
	 */
	public boolean isSheetExist(String sheetName) throws Exception {
		iIndex = workbook.getSheetIndex(sheetName);
		if (iIndex == -1) {
			// TODO: logger("Sheet with name-'" + sheetName +
			// "' doesn't exists in this excel file, please Re-check given sheet name",
			// "Missing sheet");
			System.exit(0);
			return false;
		} else
			return true;
	}

	/**
	 * Purpose- To check if the sheet with given name exists or not , but not
	 * exits from program
	 * 
	 * @param sheetName
	 *            - Sheet name should be provided
	 * @return - if sheet with specified name exists it returns true else it
	 *         returns false
	 * @throws Exception
	 */
	public boolean isSheetExistButRuns(String sheetName) throws Exception {
		iIndex = workbook.getSheetIndex(sheetName);
		if (iIndex == -1) {
			// //TODO:
			// logger("Sheet with name-'"+sheetName+"' doesn't exists in this excel file, please Re-check given sheet name","Missing sheet");
			// System.exit(0);
			logger.info("Sheet" + sheetName + "is missing and hence continuing with default procedure");
			return false;
		} else
			return true;
	}

	/**
	 * Purpose- To get the number of sheets in a workbook
	 * 
	 * @return- Returns value of number of sheets
	 * @throws Exception
	 */
	public int getNumberOfSheets() throws Exception {
		return workbook.getNumberOfSheets();
	}

	/**
	 * Purpose- To get the name of the sheet using index
	 * 
	 * @param SheetIndex
	 *            number
	 * @return- Returns name of the sheet
	 * @throws Exception
	 */
	public String getSheetNameOfIndex(int sheetIndex) throws Exception {
		return workbook.getSheetName(sheetIndex);
	}

	/**
	 * Purpose- To get the row count of specified sheet
	 * 
	 * @param sheetName
	 *            - Sheet name should be provided
	 * @return- Returns value of row count
	 * @throws Exception
	 */
	public int getRowCount(String sheetName) throws Exception {
		int number = 0;
		if (isSheetExist(sheetName)) {
			Sheet = workbook.getSheetAt(iIndex);
			number = Sheet.getLastRowNum() + 1;
		}
		return number;

	}

	/**
	 * Purpose- To get column count of specified sheet
	 * 
	 * @param sheetName
	 *            - Sheet name should be provided
	 * @return- Returns value of column count
	 * @throws Exception
	 */
	public int getColumnCount(String sheetName) throws Exception {
		if (isSheetExist(sheetName)) {
			Sheet = workbook.getSheet(sheetName);
			row = Sheet.getRow(0);

			if (row == null)
				return -1;

			return row.getLastCellNum();
		}
		return 0;
	}

	public int getRowCountUntilEmptyCell(String sheetName, int rowPadding, int columnPadding) throws Exception {
		int number = 0;
		if (isSheetExist(sheetName)) {
			Sheet = workbook.getSheetAt(iIndex);

			for (int i = rowPadding; i <= Sheet.getLastRowNum(); i++) {
				row = Sheet.getRow(i);
				if (row.getCell(columnPadding).getStringCellValue().trim().isEmpty())

					break;

				number++;
			}

		}

		return number + rowPadding;

	}

	/**
	 * Purpose- To get the column count of specified sheet and the column count
	 * ends if it has not found any value
	 * 
	 * @param sheetName
	 *            - Sheet name,row padding,column padding should be provided
	 * @return- Returns value of column count
	 * @throws Exception
	 */
	public int getColumnCountUntilEmptyCell(String sheetName, int rowPadding, int columnPadding) throws Exception {
		int number = 0;
		if (isSheetExist(sheetName)) {
			Sheet = workbook.getSheetAt(iIndex);

			row = Sheet.getRow(rowPadding);

			for (int i = columnPadding; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().isEmpty())

					break;
				number++;
			}

		}
		return number;

	}

	/**
	 * Purpose- Returns the value from Excel based on Sheetname, column name,
	 * row value, row and column padding (i.e., number of rows and columns left
	 * to draw the table)
	 * 
	 * @param sheetName
	 *            - Sheet name should be provided
	 * @param colName
	 *            - Column Name should be provided
	 * @param rowNum
	 *            - Row value should be provided
	 * @param row
	 *            padding - number of rows left
	 * @param column
	 *            padding - number of columns left
	 * @return
	 */
	public String getCellData(String sheetName, String colName, int rowNum, int rowPadding, int columnPadding) {
		try {

			if (isSheetExist(sheetName)) {
				if (rowNum <= 0) {
					// TODO: logger("Row number should be greater than 0", "");
					System.exit(0);
					return "";
				}
				int col_Num = -1;
				Sheet = workbook.getSheetAt(iIndex);

				row = Sheet.getRow(rowPadding);

				for (int i = columnPadding; i < row.getLastCellNum(); i++) {
					if (row.getCell(i).getStringCellValue().trim().contains(colName.trim())) {
						col_Num = i;

						break;
					}

				}

				if (col_Num == -1) {
					// TODO:
					// logger("Column with specified name is not being displayed",
					// "Config.properties");
					System.exit(0);
					return "";
				}

				row = Sheet.getRow(rowNum - 1);
				if (row == null)
					return "";
				cell = row.getCell(col_Num);

				if (cell == null)
					return "";
				if (cell.getCellType() == Cell.CELL_TYPE_STRING)
					return cell.getStringCellValue();
				else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

					String cellText = NumberToTextConverter.toText(cell.getNumericCellValue());

					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						// format in form of D/M/YY
						double d = cell.getNumericCellValue();
						Calendar cal = Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));
						int Year = cal.get(Calendar.YEAR);
						int Day = cal.get(Calendar.DAY_OF_MONTH);
						int Month = cal.get(Calendar.MONTH) + 1;
						cellText = Day + "/" + Month + "/" + (String.valueOf(Year)).substring(2);
					}
					return cellText;
				} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
					return "";
				else
					return String.valueOf(cell.getBooleanCellValue());
			}
			return "";
		} catch (Exception e) {
			logger.error("Exceptione is =" + e.getMessage());
			// TODO: logger("row " + rowNum + " or column " + colName +
			// " does not exist in xls", "Config.properties");
			return "row " + rowNum + " or column " + colName + " does not exist in xls";
		}
	}

	/**
	 * Purpose- Returns the value from Excel based on Sheetname, column name,
	 * row value
	 * 
	 * @param sheetName
	 *            - Sheet name should be provided
	 * @param colName
	 *            - Column Name should be provided
	 * @param rowNum
	 *            - Row value should be provided
	 * @return
	 */
	public String getCellData(String sheetName, String colName, int rowNum) {
		try {

			if (isSheetExist(sheetName)) {
				if (rowNum <= 0) {
					// TODO: logger("Row number should be greater than 0", "");
					System.exit(0);
					return "";
				}
				int col_Num = -1;
				Sheet = workbook.getSheetAt(iIndex);

				row = Sheet.getRow(0);
				for (int i = 0; i < row.getLastCellNum(); i++) {
					if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
						col_Num = i;
				}
				if (col_Num == -1) {
					// TODO:
					// logger("Column with specified name is not being displayed",
					// "Config.properties");
					System.exit(0);
					return "";
				}

				row = Sheet.getRow(rowNum - 1);
				if (row == null)
					return "";
				cell = row.getCell(col_Num);

				if (cell == null)
					return "";
				if (cell.getCellType() == Cell.CELL_TYPE_STRING)
					return cell.getStringCellValue();
				else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
					// String cellText =
					// String.valueOf(cell.getNumericCellValue());
					String cellText = NumberToTextConverter.toText(cell.getNumericCellValue());
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						// format in form of D/M/YY
						double d = cell.getNumericCellValue();
						Calendar cal = Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));
						int Year = cal.get(Calendar.YEAR);
						int Day = cal.get(Calendar.DAY_OF_MONTH);
						int Month = cal.get(Calendar.MONTH) + 1;
						cellText = Day + "/" + Month + "/" + (String.valueOf(Year)).substring(2);
					}
					return cellText;
				} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
					return "";
				else
					return String.valueOf(cell.getBooleanCellValue());
			}
			return "";
		} catch (Exception e) {
			logger.error("Exceptione is =" + e.getMessage());
			// TODO: logger("row " + rowNum + " or column " + colName +
			// " does not exist in xls", "Config.properties");
			return "row " + rowNum + " or column " + colName + " does not exist in xls";
		}
	}

	/**
	 * Purpose- Returns the value from Excel based on Sheetname, column number,
	 * row number
	 * 
	 * @param sheetName
	 *            - Sheet name should be provided
	 * @param colNum
	 *            - Column number should be provided
	 * @param rowNum
	 *            - Row number should be provided
	 * @return
	 */
	public String getCellData(String sheetName, int colNum, int rowNum) {
		try {
			if (isSheetExist(sheetName)) {
				if (rowNum <= 0) {
					// TODO: logger("Row number should be greater than 0", "");
					System.exit(0);
					return "";
				}

				Sheet = workbook.getSheetAt(iIndex);
				row = Sheet.getRow(rowNum - 1);
				if (row == null)
					return "";
				cell = row.getCell(colNum);

				if (cell == null)
					return "";
				if (cell.getCellType() == Cell.CELL_TYPE_STRING)
					return cell.getStringCellValue();
				else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
					// String cellText =
					// String.valueOf(cell.getNumericCellValue());
					String cellText = NumberToTextConverter.toText(cell.getNumericCellValue());
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						// format in form of D/M/YY
						double d = cell.getNumericCellValue();
						Calendar cal = Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));
						int Year = cal.get(Calendar.YEAR);
						int Day = cal.get(Calendar.DAY_OF_MONTH);
						int Month = cal.get(Calendar.MONTH) + 1;
						cellText = Day + "/" + Month + "/" + (String.valueOf(Year)).substring(2);
					}
					return cellText;
				} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
					return "";
				else
					return String.valueOf(cell.getBooleanCellValue());
			}
			return "";
		} catch (Exception e) {
			logger.error("Exceptione is =" + e.getMessage());
			// TODO: logger("row " + rowNum + " or column " + colNum +
			// " does not exist in xls", "Config.properties");
			return "row " + rowNum + " or column " + colNum + " does not exist in xls";
		}
	}

}
