package com.jiongsoft.cocit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Excel 解析工具
 * 
 */
public abstract class ExcelUtil {

	@SuppressWarnings("deprecation")
	public static void makeExcel(OutputStream out, List<String[]> excelResult) throws FileNotFoundException, IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		int len = excelResult.size();
		for (int i = 0; i < len; i++) {
			HSSFRow row = sheet.createRow(i);
			String[] rowData = excelResult.get(i);
			for (byte j = 0; j < rowData.length; j++) {
				HSSFCell cell = row.createCell(j);
				cell.setCellValue(rowData[j]);
			}
		}
		workbook.write(out);
	}

	/**
	 * 解析excel文件中的全部sheet，并返回结果集。
	 * 
	 * @param excelFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String[]> parseExcel(File excelFile) throws FileNotFoundException, IOException {
		List<String[]> excelResult = new ArrayList();
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelFile));
		int sheetNumber = workbook.getNumberOfSheets();
		for (int sheetIndex = 0; sheetIndex < sheetNumber; sheetIndex++) {
			HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
			if (sheet != null) {
				excelResult.addAll(parseSheet(sheet));
			}
		}
		return excelResult;
	}

	/**
	 * 解析excel文件中指定索引的sheet，并返回结果集。
	 * 
	 * @param excelFile
	 * @param sheetIndex
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String[]> parseSheet(File excelFile, int sheetIndex) throws FileNotFoundException, IOException {
		List<String[]> excelResult = new ArrayList();
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelFile));
		HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		if (sheet != null) {
			excelResult.addAll(parseSheet(sheet));
		}
		return excelResult;
	}

	private static List<String[]> parseSheet(HSSFSheet sheet) {
		List<String[]> sheetResult = new ArrayList();
		int rowNumber = sheet.getPhysicalNumberOfRows();
		for (int rowIdx = 0; rowIdx < rowNumber; rowIdx++) {
			HSSFRow row = sheet.getRow(rowIdx);
			if (row != null) {
				sheetResult.add(parseRow(row));
			}
		}
		return sheetResult;
	}

	private static String[] parseRow(HSSFRow row) {
		int size = row.getLastCellNum();
		String[] rowResult = new String[size];
		for (int col = 0; col < size; col++) {
			HSSFCell cell = row.getCell(col);
			if (cell != null) {
				try {
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				} catch (Throwable e) {
					//e.printStackTrace();
				}
				// String str = cell.getStringCellValue();
				int type = cell.getCellType();
				switch (type) {
				case HSSFCell.CELL_TYPE_FORMULA:
					// FormulaEvaluator formula=new FormulaEvaluator();
					// return formula.evaluate(cell).getNumberValue();
					rowResult[col] = cell.getCellFormula();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					if (HSSFDateUtil.isCellDateFormatted(cell) || HSSFDateUtil.isCellInternalDateFormatted(cell)) {
						rowResult[col] = DateUtil.format(cell.getDateCellValue());
					} else {
						Integer num = new Integer((int) cell.getNumericCellValue());
						rowResult[col] = String.valueOf(num);
					}
					break;
				case HSSFCell.CELL_TYPE_STRING:
					rowResult[col] = cell.getRichStringCellValue().toString();
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					rowResult[col] = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_ERROR:
					break;
				}
			}
		}
		return rowResult;
	}
}
