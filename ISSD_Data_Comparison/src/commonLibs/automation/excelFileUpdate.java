/**
 * 
 */
package commonLibs.automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author AGARWBI1
 * 
 */
public class excelFileUpdate {

	private Logger Application_logs = Logger.getLogger("excelFileUpdate");
	private String filePath = null;
	private int startRow = 0;
	// private HashMap readExceldata=new HashMap();
	private HashMap<String, Double> writeExceldata = new HashMap<String, Double>();
	String[] name_pair;
	private double[] value_pair_Exported;
	double[] value_pair_DB;

	@SuppressWarnings("deprecation")
	public void excelAddData_NewWay(String filePath, Map<String, Object[]> data,String sheet_name) {
		try {
			// FileInputStream file = new FileInputStream(new File(filePath));

			// Get the workbook instance for XLS file
			XSSFWorkbook workbook = new XSSFWorkbook();// file

			// Get first sheet from the workbook
			// XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFSheet sheet = workbook.createSheet(sheet_name);
			// Set to Iterate and add rows into XLS file
			Set<String> newRows = data.keySet();

			// get the last row number to append new data
			// int rownum = sheet.getLastRowNum();

			// get the first row number
			int rownum = sheet.getFirstRowNum();
			System.out.println("newRows size = "+(newRows.size()-1));
			
			
			for (int i = 0; i < newRows.size(); i++) {
				// for (String key : newRows) {

				// Creating a new Row in existing XLSX sheet
				XSSFRow row = sheet.createRow(rownum++);
				XSSFCellStyle style;
				  style= workbook.createCellStyle();
				Object[] objArr = null;
				objArr=data.get(String.valueOf(i));
//				System.out.println("i = "+i);
				int cellnum = 0;
				for (Object obj : objArr) {
					Cell cell = row.createCell(cellnum++);
					
					//cell.setCellValue(obj.toString());
//					System.out.println("object = "+obj.toString());
					String[] values = ((String) obj).split(",");
					for (String temp_String : values) {
//						System.out.println("values:"+temp_String);
						
						
						if(temp_String.equalsIgnoreCase("FAIL"))
							{
							cell.setCellValue(temp_String);
								style.setFillForegroundColor(IndexedColors.RED.getIndex());
								style.setFillPattern(CellStyle.SOLID_FOREGROUND);
								cell.setCellStyle(style);
								
							}
							else if(temp_String.equalsIgnoreCase("PASS"))
							{
								cell.setCellValue(temp_String);
								//style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 128, 0)));
								style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
								style.setFillPattern(CellStyle.SOLID_FOREGROUND);
								
								cell.setCellStyle(style);
								
								
							}
							else {
								Scanner lineScanner = new Scanner(temp_String);
						        lineScanner.useDelimiter(",");
						       
						            // While there is more text to get it will loop.
						            while(lineScanner.hasNext()) {
						            	 String output ="";
						            	cell = row.createCell(cellnum++);
						                 output = lineScanner.next();
						                // Write the output to that cell.
						                cell.setCellValue(output);						                
						            }
							}
						
//						if (temp_String instanceof String) {
//							cell.setCellValue((String) temp_String);
//							if(temp_String.equalsIgnoreCase("FAIL"))
//							{
//								style.setFillForegroundColor(IndexedColors.RED.getIndex());
//								style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//							}
//							else if(temp_String.equalsIgnoreCase("PASS"))
//							{
//								//style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 128, 0)));
//								style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
//								style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//							}
//						} else
//							;
//						
					}
				}
			}

			
			FileOutputStream outFile = new FileOutputStream(filePath);// new
																		// File(filePath)
			// outFile = new FileOutputStream(
			workbook.write(outFile);
			outFile.close();
			
		} catch (FileNotFoundException e) {
			Application_logs.error(e);

		} catch (IOException e) {
			Application_logs.error(e);

		}

	}
	
	public void excelAddData_NewWay(String filePath, Map<String, Object[]> data) {
		try {
			// FileInputStream file = new FileInputStream(new File(filePath));

			// Get the workbook instance for XLS file
			XSSFWorkbook workbook = new XSSFWorkbook();// file

			// Get first sheet from the workbook
			// XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFSheet sheet = workbook.createSheet("sheet1");
			// Set to Iterate and add rows into XLS file
			Set<String> newRows = data.keySet();

			// get the last row number to append new data
			// int rownum = sheet.getLastRowNum();

			// get the first row number
			int rownum = sheet.getFirstRowNum();

			for (int i = 0; i < newRows.size(); i++) {
				// for (String key : newRows) {

				// Creating a new Row in existing XLSX sheet
				XSSFRow row = sheet.createRow(rownum++);
				Object[] objArr = data.get(String.valueOf(i));
				int cellnum = 0;
				for (Object obj : objArr) {
					String[] values = ((String) obj).split(",");
					for (String temp_String : values) {
						Cell cell = row.createCell(cellnum++);
						if (temp_String instanceof String) {
							cell.setCellValue((String) temp_String);
						} else
							;
//						else if (obj instanceof Boolean) {
//							cell.setCellValue((Boolean) obj);
//						} else if (obj instanceof Date) {
//							cell.setCellValue((Date) obj);
//						} else if (obj instanceof Double) {
//							cell.setCellValue((Double) obj);
//						}
					}
				}
			}

			FileOutputStream outFile = new FileOutputStream(filePath);// new
																		// File(filePath)
			// outFile = new FileOutputStream(
			workbook.write(outFile);
			outFile.close();
		} catch (FileNotFoundException e) {
			Application_logs.error(e);

		} catch (IOException e) {
			Application_logs.error(e);

		}

	}

	public void excelAddData(String filePath, int column, int count,
			String[] value) {
		try {
			FileInputStream file = new FileInputStream(new File(filePath));

			// Get the workbook instance for XLS file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Row row = sheet.getFirstRowNum();
			System.out.println(sheet.getFirstRowNum());
			for (int i = 0; i < count; i++) {
				// XSSFRow row_temp = sheet.createRow((sheet.getFirstRowNum() +
				// i));
				XSSFRow row_temp = sheet.getRow((sheet.getFirstRowNum() + i));
				System.out.println(row_temp.getRowNum() + "\t" + value[i]);
				// for (int j = 1; j < 3; j++) {
				// XSSFCell cell = row_temp.createCell(column);
				// XSSFCell cell = row_temp.getFirstCellNum();

				Iterator<Cell> cell_iter = row_temp.cellIterator();
				while (cell_iter.hasNext()) {
					Cell cell = cell_iter.next();
					cell.setCellValue(value[i]);
					System.out.println(cell.getStringCellValue());
				}
				// value[i]
				// cell.setAsActiveCell();
				// cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				// cell.setCellValue("Apple");
				// cell.setCellValue(value[i]);

			}
			// row=sheet.getRow(startRow-3);
			// Cell cell=row.getCell(3);
			file.close();
			FileOutputStream outFile = new FileOutputStream(new File(filePath));
			// outFile = new FileOutputStream(
			workbook.write(outFile);
			outFile.close();
		} catch (FileNotFoundException e) {
			Application_logs.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			Application_logs.error(e);
			e.printStackTrace();
		}
	}

	public void connectWithDB(String moduleName, String metricName,
			String level, String[] param_values) {
		WareHouseDBConnect obj1 = new WareHouseDBConnect();
		/*
		 * try { Thread.sleep(400); } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 */
		// System.out.println(level);
		obj1.executeQueryDB(moduleName, metricName, level, param_values);
		for (int i = 0; i < this.name_pair.length; i++) {
			for (int j = 0; j < obj1.names_DB.length; j++) {
				if (this.name_pair[i].equals(obj1.names_DB[j])) {
					Application_logs.info("matches :" + value_pair_DB[i] + "\t"
							+ obj1.values_DB[j]);
					// writeExceldata.put(this.name_pair[i],obj.values_DB[j]);
					value_pair_DB[i] = obj1.values_DB[j];
				}

			}
		}
	}

	public void compareMaps() {

	}

	// star row for reading is 8 and count for
	
	public void excelFileOpen(String filePath, int startRow, int count) {
		this.filePath = filePath;
		this.startRow = startRow;
		try {
			FileInputStream file = new FileInputStream(new File(filePath));

			// Get the workbook instance for XLS file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			XSSFRow row = sheet.getRow(startRow);
			name_pair = new String[count];
			value_pair_DB = new double[count];
			value_pair_Exported = new double[count];

			for (int i = 0; i < count; i++) {
				XSSFRow row_temp = sheet.getRow((row.getRowNum() + i));
				for (int j = 1; j < 3; j++) {
					XSSFCell cell = row_temp.getCell(j);

					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_BOOLEAN:
						Application_logs.info(cell.getBooleanCellValue()
								+ "\t\t");
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						Application_logs.info(cell.getNumericCellValue()
								+ "\t\t");
						System.out.print((int) Math.round(cell
								.getNumericCellValue()) + "\t\t");
						// cell.getNumericCellValue();
						value_pair_Exported[i] = cell.getNumericCellValue();
						break;
					case XSSFCell.CELL_TYPE_STRING:
						Application_logs.info(cell.getStringCellValue()
								+ "\t\t");
						System.out.print(cell.getStringCellValue() + "\t\t");
						// name_pair[i]=cell.getStringCellValue();
						break;
					}
				}
				System.out.println();
				Application_logs.info("");
			}
			Application_logs.info("Row numbers in sheet :"
					+ sheet.getLastRowNum());
			/*
			 * // Iterate through each rows from first sheet Iterator<Row>
			 * rowIterator = sheet.iterator();
			 * 
			 * while (rowIterator.hasNext()) { Row row = rowIterator.next();
			 * 
			 * // For each row, iterate through each columns Iterator<Cell>
			 * cellIterator = row.cellIterator(); while (cellIterator.hasNext())
			 * {
			 * 
			 * Cell cell = cellIterator.next();
			 * 
			 * switch (cell.getCellType()) { case Cell.CELL_TYPE_BOOLEAN:
			 * System.out.print(cell.getBooleanCellValue() + "\t\t"); break;
			 * case Cell.CELL_TYPE_NUMERIC:
			 * System.out.print(cell.getNumericCellValue() + "\t\t"); break;
			 * case Cell.CELL_TYPE_STRING:
			 * System.out.print(cell.getStringCellValue() + "\t\t"); break; } }
			 * System.out.println(""); }
			 */
		} catch (FileNotFoundException e) {
			Application_logs.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			Application_logs.error(e);
			e.printStackTrace();
		}
	}
	
	
	
	@SuppressWarnings({ "resource", "unchecked" })

	public HashMap<String,Object[]> getExcelData(String sheetName)  {
		String fileName="./input_data/GM.xlsx";
		
		HashMap<String,Object[]>  data1 = new HashMap<String,Object[]>();
//		System.out.println("File Name from Data provider : "+fileName+" Sheet Name : "+sheetName);
	
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(fileName));
		
//		InputStream is = new FileInputStream(new File(fileName));
//		Workbook workbook = StreamingReader.builder()
//		        .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
//		        .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
//		        .open(is);            // InputStream or File for XLSX file (required)
		
		String [][] data;
//		for (Sheet sheet : workbook){
		XSSFSheet sheet = workbook.getSheet(sheetName);	
			int noOfRows=sheet.getPhysicalNumberOfRows();
			data=new String[noOfRows][];
			
			  for (int i=0;i<noOfRows;i++) {
				  
				  XSSFRow r =sheet.getRow(i);
				  int noOfColumns=r.getPhysicalNumberOfCells();				  
					  
					  data[i]=new String[noOfColumns];
				  String temp_header=null;
			    for (int j=0;j<noOfColumns;j++) {
			    	XSSFCell c=r.getCell(j);
			    	if(j==0){
			    		temp_header=c.getStringCellValue();
//			    	System.out.println(temp_header+"\n");
			    	}
			    	
			        	data[i][j]=c.getStringCellValue();
//			        	System.out.print(data[i][j]);
			    }
			    data1.put(temp_header,data[i]);
			  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//			}
			  
			  return data1;
	}
}
