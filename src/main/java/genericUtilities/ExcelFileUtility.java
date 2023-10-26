package genericUtilities;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * This class contains generic methods to handle Excel File
 * 
 * @author sharu
 *
 */
public class ExcelFileUtility {

	/**
	 * This method will return value at given cell from excel sheet
	 * 
	 * @param sheetName
	 * @param rowIndex
	 * @param colIndex
	 * @return
	 * @throws IOException
	 */
	public String readDataFromExcelSheet(String sheetName, int rowIndex, int colIndex) throws IOException {
		// Step1: Convert physical representation of file to Java readable format
		FileInputStream fis = new FileInputStream(IConstantsUtility.ExcelFilePath);

		// Step 2: Create workbook
		Workbook wb = WorkbookFactory.create(fis);

		// Step 3: Get access to sheet
		Sheet sh = wb.getSheet(sheetName);

		// Step 4: Get access to required row and column
		String value = sh.getRow(rowIndex).getCell(colIndex).getStringCellValue();

		// Step 5: Return value
		return value;
	}

	/**
	 * This method will write value at given cell index of excel sheet
	 * 
	 * @param sheetName
	 * @param rowIndex
	 * @param colIndex
	 * @param cellValue
	 * @throws IOException
	 */
	public void writeDataToExcelSheet(String sheetName, int rowIndex, int colIndex, String cellValue)
			throws IOException {
		FileInputStream fis = new FileInputStream(IConstantsUtility.ExcelFilePath);
		Workbook wb = WorkbookFactory.create(fis);
		wb.getSheet(sheetName).getRow(rowIndex).createCell(colIndex).setCellValue(cellValue);

		FileOutputStream fos = new FileOutputStream(IConstantsUtility.ExcelFilePath);
		wb.write(fos);
	}

	/**
	 * This methods returns table data in form of 2-d Object array used with DataProviders
	 * @param sheetName
	 * @return
	 * @throws IOException
	 */
	public Object[][] readMultipleDataFromExcelSheet(String sheetName) throws IOException {
		FileInputStream fis = new FileInputStream(IConstantsUtility.ExcelFilePath);
		Workbook wb = WorkbookFactory.create(fis);
		Sheet sh = wb.getSheet(sheetName);

		int rowCount = sh.getLastRowNum(); // if 10 rows, last row index=9
		int colCount = sh.getRow(0).getLastCellNum(); // if 2 columns, returns 2

		Object[][] data = new Object[rowCount][colCount];

		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				data[i][j] = sh.getRow(i + 1).getCell(j).getStringCellValue();
			}
		}

		return data;
	}
}

