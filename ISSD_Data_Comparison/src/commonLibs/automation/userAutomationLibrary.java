/**
 * 
 */
package commonLibs.automation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.log4j.Logger;
import org.incava.diff.Diff;
import org.incava.diff.Difference;
//import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * @author JMukherjee
 * 
 */
public class userAutomationLibrary {

	/** The Application_logs. */
	Logger Application_Logs = Logger.getLogger("SeleniumCISAnalysisPage");

	static String comparison_filename="";
	String file_name="";
	String filename="";
	/** The collector. */
	@Rule
	public ErrorCollector collector = new ErrorCollector();

	public userAutomationLibrary() {
		//Application_logs = Logger.getLogger("userAutomationLibrary");
	}

	
	/**
	 * Menu read.
	 */
	public String[] menuRead() {

		String[] execution_flow = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					"./data/Menu.csv")));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		String line;

		try {
			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				// String[] metrics;
				execution_flow = line.split(",");
			}
		} catch (IOException e) {
			//Application_logs.error(e);

			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			//Application_logs.error(e);
			e.printStackTrace();
		}
		return execution_flow;
	}

	/**
	 * This method is to create CSV file (under /data folder) and write the
	 * contents at once
	 * 
	 * This method takes care of closing the file also
	 * 
	 * @param fileName
	 * @param content
	 * @return
	 */
	public boolean csvFileWrite(String fileName, StringBuilder content) {

		try {
			FileWriter writer = new FileWriter("./output/" + fileName + ".csv",
					false);

			writer.write(content.toString());
			writer.append('\n');
			writer.flush();
			writer.close();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	/**
	 * Highlighting Web Element Function.
	 * 
	 * @param driver
	 *            the driver
	 * @param element
	 *            the element
	 */
	public void FileMove_Screenshot(String filePath) {

		File folderName = new File("./screenshots/" + filePath);
	
		File download_dir = new File("./screenshots/");
		if (download_dir.isDirectory()) {
			File[] downloaded_files = download_dir.listFiles();
			for (File file_temp : downloaded_files) {
				if (file_temp.isFile()) {
					try {
						FileUtils.moveFileToDirectory(file_temp, folderName,
								true);
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	/**
	 * capture screenshot utility
	 * 
	 * @param driver
	 * @param moduleName
	 */
	public String captureScreen(WebDriver driver, String moduleName, String status, String tab_name) { // )

		String fileName = null;
		String FolderName;
		
		//driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		try {
			File source = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			/*
			 * File download_dir = new File( "./screenshots/");
			 */
			if (status.equalsIgnoreCase("PASS")) {
				
				FolderName = "./screenshots/PASS";
				fileName = FolderName
						+"/"
						+ tab_name
						+"/"
						+ moduleName
						+ "/"
						+ (new SimpleDateFormat("yyyy-MM-dd-hh-mm")
								.format(new Date())) + source.getName();
				
				
			} if (status.equalsIgnoreCase("FAIL")){
				FolderName = "./screenshots/FAIL";
				fileName = FolderName
						+"/"
						+ tab_name
						+"/"
						+ moduleName
						+ "/"
						+ (new SimpleDateFormat("yyyy-MM-dd-hh-mm")
								.format(new Date())) + source.getName();
			}

			File Name = new File(fileName);
			FileUtils.copyFile(source, Name);
			
			/*File Tab_Name = new File(FolderName + tab_name);
			//FileMove_Screenshot(tab_name);
			File download_dir = new File(fileName);
			//File Tab_Name = new File(tab_name);
			if (download_dir.isDirectory()) {
				File[] downloaded_files = download_dir.listFiles();
				for (File file_temp : downloaded_files) {
					if (file_temp.isFile()) {
						try {
							FileUtils.moveFileToDirectory(file_temp, Tab_Name,
									true);
						} catch (IOException e) {

							e.printStackTrace();
						}
					}
				}
			}*/
			/*try{
				FileUtils.moveDirectory(Name, Tab_Name);
			}
			catch(Exception e)
			{
				FileUtils.copyFile(Name, Tab_Name);
			}
			*/
			return fileName;
		} catch (IOException e) {

			// add fail entry to the excel sheet
			Application_Logs.error("Taking screenshot failed\n" + e);
			collector.addError(e);
			return null;
		}
		
	}
	
	public static String getDateTimeStamp() {
		// /oTestNG.run();
		// iResult = oTestNG.getStatus();
		Date oDate = new Date();
		String sDate = oDate.toString();
		sDate = sDate.replace(":", "_");
		sDate = sDate.replace(" ", "_");

		return sDate;
	}

	public void highlightElement(WebDriver driver, WebElement element, String Name, String status,String tab_name) throws Exception {
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
		// for (int i = 0; i < 15; i++) {
		String highlight_color = new String();
		
if(status.equalsIgnoreCase("PASS"))
	
	highlight_color ="color: green; border: 3px solid green;";

else if(status.equalsIgnoreCase("FAIL"))
	
	highlight_color ="color: red; border: 3px solid red;";

		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].setAttribute('style', arguments[1]);",

		element, highlight_color);
		/*
		 * try{ if(i==3) {
		 */
		
		captureScreen(driver, Name, status,tab_name);
		
		Thread.sleep(500);
		/*
		 * } } catch(Exception e) { System.out.println("Not captured for " +
		 * element.getText()); }
		 */

		js.executeScript("arguments[0].setAttribute('style', arguments[1]);",

		element, "");

		// }
	}

	/**
	 * checking whether the element is present in the page
	 * 
	 * @param driver
	 * @param by
	 * @return
	 */
	public boolean isElementPresent(WebDriver driver, By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * check whether alert appeared on page
	 * 
	 * @param driver
	 * @return
	 */
	private boolean isAlertPresent(WebDriver driver) {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	/**
	 * Accept the alert and get the text showed inside alert
	 * 
	 * @param driver
	 * @param acceptNextAlert
	 * @return
	 */
	private String closeAlertAndGetItsText(WebDriver driver,
			boolean acceptNextAlert)
	{
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

	/**
	 * Download window of browser can be handled.
	 * 
	 * @param driver
	 *            the driver
	 * @param mainWindow
	 *            the main window
	 */

	public void downloadWindowHandle(WebDriver driver, String mainWindow) {
		// throws Exception {

		/**
		 * Closing new browser window popping up while exporting
		 * 
		 */
		// -->added by ashik
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		for (String winHandle : driver.getWindowHandles()) {
			if (!winHandle.equals(mainWindow)) {
				driver.switchTo().window(winHandle);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.close();
				break;
			}
		}
		driver.switchTo().window(mainWindow);

	}
	/**
	 * comparison of csv files
	 * @throws InterruptedException 
	 */
	public void csvComparison(String Ans_filepath,String Ans_filename) throws InterruptedException
	{
		/*******************************************
		 * clearing hashmap
		 * **************************************
		 */
		  Map<String, Object[]> test_cases_answers = new HashMap<String, Object[]>();
		  Map<String, Object[]> test_cases_dashboard = new HashMap<String, Object[]>();
		  Map<Integer,Integer> test= new HashMap<Integer,Integer>();
		  Map<Integer,Integer> test1=new HashMap<Integer,Integer>();
		  Map<Integer,Integer> test_ans= new HashMap<Integer,Integer>();
		  Map<Integer,Integer> test1_dbd=new HashMap<Integer,Integer>();
		  Map<String,String> results_answers=new HashMap<String,String>();
	  Map<String,String> results1_dbd=new HashMap<String,String>();
		Map<String, Object[]> failed_testcases = new HashMap<String, Object[]>();
		
		
		int TRx_count = 0;
		int Payer_count=0;
		
		/*******************************************
		 * clearing hashmap
		 * **************************************
		 */
		 int row_num=1;
		String Answers_filepath=Ans_filepath;
		String Ans_name=Ans_filename.replace(" ","");
		File DB=new File("./Exported/Dashboard_Exported/"+file_name);
//		if(DB.isDirectory())
//		{
//			try {
//				System.out.println("absolute_PAth for dabod"+DB.getCanonicalPath());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		String Dashboard_filepath = null;
		try {
			Dashboard_filepath = DB.getCanonicalPath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String Dashboard_filename="";
		int k=0,column_count=0;
		int cnt=1;
		int count=1,count1=1;
//		File download_dir = new File(
//				"C:\\Users\\hm\\Documents\\CommonFiles\\EclipsePortable\\Data\\workspace\\PA_Arbor\\Exported\\Dashboard_Exported\\");
		
		File download_dir=new File("./Exported/Dashboard_Exported/"+file_name);
		if (download_dir.isDirectory()) {
			File[] downloaded_files = download_dir.listFiles();
			for (File file_temp : downloaded_files) {
				if (file_temp.isFile()) {
					if((file_temp.getName()).equalsIgnoreCase(Ans_name))
					{
						Dashboard_filename=file_temp.getName();
						System.out.println("Dashboard filename:"+Dashboard_filename);
						Application_Logs.info("Dashboard filename:"+Dashboard_filename);
						break;
					}
				}
			}
		}
//		System.out.println("path and filename:"+Dashboard_filepath+"\\"+Dashboard_filename);
		BufferedReader br3_ans=null;
		BufferedReader br4_dbd=null;
		try
		{
			
		
		 br3_ans = new BufferedReader(new InputStreamReader(new FileInputStream(Answers_filepath)));
		 
		 br4_dbd = new BufferedReader(new InputStreamReader(new FileInputStream(Dashboard_filepath+"\\"+Dashboard_filename)));
	}
		catch (FileNotFoundException ex) 
		{
	      ex.printStackTrace();
		}
		
		String Line="";
		String Line1="";
		String[] name1_ans_1=null;
		String[] name2_dbd_1=null;
		
		try
		{
		// for identifying the headers
		if(((Line=br3_ans.readLine())!=null)&&(Line1=br4_dbd.readLine())!=null)
		{
			name1_ans_1=Line.split(",");
			name2_dbd_1=Line1.split(",");
	
			
		
		for(int u=0 ;u<name1_ans_1.length ;u++)
		{
			if(name1_ans_1[u].contains("﻿"))
			{
				name1_ans_1[u]=name1_ans_1[u].replace("﻿","");

			}
		}
		for(int t=0;t<name2_dbd_1.length;t++)
		{
			if(name2_dbd_1[t].contains("﻿"))
			{
				name2_dbd_1[t]=name2_dbd_1[t].replace("﻿","");

			}
		}
		String Name_P=null;
		
		if(Dashboard_filename.contains("Payer"))
		{
			Name_P="Payer";
		}
		if(Dashboard_filename.contains("Plan"))
		{
			 Name_P="Plan";
		}
		if(Dashboard_filename.contains("Prescriber"))
		{
			 Name_P="Prescriber";
		}
		
		System.out.println("length of dbd:"+name2_dbd_1.length+"length of dbd:"+name1_ans_1.length);
		/**
		 * comparing  the column names in both the files
		 */
		for(int i=0;i<name1_ans_1.length;i++)
		{
			for(int j=0;j<name2_dbd_1.length;j++)
			{		
				
			if(name1_ans_1[i].equalsIgnoreCase(name2_dbd_1[j]))
			{
				if((name1_ans_1[i].equalsIgnoreCase("Week"))||(name1_ans_1[i].equalsIgnoreCase("Month")))
				{
//					System.out.println("value_an:"+name1_ans_1[i]);
//					System.out.println("value_dbd:"+name2_dbd_1[j]);
			test.put(0,i);
			test1.put(0,j);
				}
				else
				{
					if(name1_ans_1[i].equalsIgnoreCase("TRx"))
					{
						TRx_count=cnt;
					}
					
					if(name1_ans_1[i].equalsIgnoreCase(Name_P))
					{
						Payer_count=cnt;
					}
					
//					System.out.println("value_ans_11:"+name1_ans_1[i]);
//					System.out.println("value_dbd_!1:"+name2_dbd_1[j]);
					test.put(cnt,i);
					test1.put(cnt,j);
					cnt++;
				}
				
				
				break;
			}
		}
	}
	k=name2_dbd_1.length;
	if(file_name.equalsIgnoreCase("Payer Influence"))
	{
		k=name1_ans_1.length;
	}
	if(file_name.equalsIgnoreCase("Rx analysis")&&(cnt>name1_ans_1.length))
k=cnt-1;
		System.out.println("value of k:"+k);
		}
		}
		catch(Exception e){
		
		}
		

		int y=0,u=0;
		if((test.get(0)==null)&&(test1.get(0)==null))
		{
			u=1;
			while(y<k)
			{
			test1_dbd.put(y, test1.get(u));
			test_ans.put(y, test.get(u));
			System.out.println("test value="+y+"="+test_ans.get(y)+"_"+test1_dbd.get(y));
//			System.out.println("test value andbd="+y+"="+test.get(y)+"_"+test1.get(y));
			y++;u++;
		
		}
	}
			else
		{
			while(y<k)
			{
			test1_dbd.put(y, test1.get(u));
			test_ans.put(y, test.get(u));
			System.out.println("test value_else="+y+"="+test_ans.get(y)+"_"+test1_dbd.get(y));
//			System.out.println("test value andbd_else="+y+"="+test.get(y)+"_"+test1.get(y));
			y++;u++;
			}
		}
		
		try
		{
			String[] name1_ans=null;
			String[] name2_dbd=null;
		
	while(((Line=br3_ans.readLine())!=null))
	{
				int hash1 = Line.hashCode();
				 

	                if (Line.indexOf(",\"") != -1) {
	                    String temp_begin = Line.substring(0, Line.indexOf(",\""));

	                    String temp_end = (Line.substring(Line.indexOf("\","), Line.length())).replace("\"", "");

	                    String temp1 = (Line.substring(Line.indexOf("\""), Line.indexOf("\","))).replace(",", " ");

	                    Line = temp_begin + "," + temp1.replace("\"", "") + temp_end;
	                }
				name1_ans = Line.split(",",name1_ans_1.length);
				for(int hj=0;hj<name1_ans_1.length;hj++)
				{
//					System.out.println("name1_ans="+name1_ans[hj]);
					
					if(name1_ans[hj].equalsIgnoreCase(""))
					{
//						System.out.println("null is here in answers");
						name1_ans[hj]="0";
					}
				}
//				System.out.println("name1_ans="+name1_ans[0]);
//				System.out.println("name1_ans="+name1_ans[1]);
//				System.out.println("name1_ans="+name1_ans[2]);
//				System.out.println("name1_ans="+name1_ans[3]);
//				System.out.println("name1_ans="+name1_ans[4]);
//				System.out.println("name1_ans="+name1_ans[5]);
				if(k==2)
				{
					test_cases_answers.put(String.valueOf(name1_ans[test_ans.get(0)] + "_" + hash1),
							new Object[] { name1_ans[test_ans.get(0)], name1_ans[test_ans.get(1)]});
			results_answers.put(hash1+","+name1_ans[test_ans.get(0)]+"_"+name1_ans[test_ans.get(1)], "False");
				}
			if(k==3)
			{
		test_cases_answers.put(String.valueOf(name1_ans[test_ans.get(0)] + "_" + hash1),
						new Object[] { name1_ans[test_ans.get(0)], name1_ans[test_ans.get(1)], name1_ans[test_ans.get(2)] });
		results_answers.put(hash1+","+name1_ans[test_ans.get(0)]+"_"+name1_ans[test_ans.get(1)]+"_"+name1_ans[test_ans.get(2)], "False");
			}
			else if(k==4)
			{
				test_cases_answers.put(String.valueOf(name1_ans[test_ans.get(0)] + "_" + hash1),
						new Object[] { name1_ans[test_ans.get(0)], name1_ans[test_ans.get(1)], name1_ans[test_ans.get(2)],name1_ans[test_ans.get(3)] });
				results_answers.put(hash1+","+name1_ans[test_ans.get(0)]+"_"+name1_ans[test_ans.get(1)]+"_"+name1_ans[test_ans.get(2)]+"_"+name1_ans[test_ans.get(3)], "False");	
			}
			else if(k==5)
			{
				test_cases_answers.put(String.valueOf(name1_ans[test_ans.get(0)] + "_" + hash1),
						new Object[] { name1_ans[test_ans.get(0)], name1_ans[test_ans.get(1)], name1_ans[test_ans.get(2)],name1_ans[test_ans.get(3)],name1_ans[test_ans.get(4)] });
				results_answers.put(hash1+","+name1_ans[test_ans.get(0)]+"_"+name1_ans[test_ans.get(1)]+"_"+name1_ans[test_ans.get(2)]+"_"+name1_ans[test_ans.get(3)]+"_"+name1_ans[test_ans.get(4)], "False");	
			}
			
			else if(k==6)
			{
				test_cases_answers.put(String.valueOf(name1_ans[test_ans.get(0)] + "_" + hash1),
						new Object[] { name1_ans[test_ans.get(0)], name1_ans[test_ans.get(1)], name1_ans[test_ans.get(2)],name1_ans[test_ans.get(3)],name1_ans[test_ans.get(4)],name1_ans[test_ans.get(5)] });
				results_answers.put(hash1+","+name1_ans[test_ans.get(0)]+"_"+name1_ans[test_ans.get(1)]+"_"+name1_ans[test_ans.get(2)]+"_"+name1_ans[test_ans.get(3)]+"_"+name1_ans[test_ans.get(4)]+"_"+name1_ans[test_ans.get(5)], "False");	
			}
			else if(k==13)
			{
				test_cases_answers.put(String.valueOf(name1_ans[test_ans.get(0)] + "_" + hash1),
						new Object[] { name1_ans[test_ans.get(0)], name1_ans[test_ans.get(1)], name1_ans[test_ans.get(2)],name1_ans[test_ans.get(3)],name1_ans[test_ans.get(4)],name1_ans[test_ans.get(5)],name1_ans[test_ans.get(6)],name1_ans[test_ans.get(7)],name1_ans[test_ans.get(8)],name1_ans[test_ans.get(9)],name1_ans[test_ans.get(10)],name1_ans[test_ans.get(11)],name1_ans[test_ans.get(12)] });
//				results_answers.put(hash1+","+name1_ans[test_ans.get(0)]+"_"+name1_ans[test_ans.get(1)]+"_"+name1_ans[test_ans.get(2)]+"_"+name1_ans[test_ans.get(3)]+"_"+name1_ans[test_ans.get(4)]+"_"+name1_ans[test_ans.get(5)], "False");	
			}
	}
			
			while((Line1=br4_dbd.readLine())!=null)
			{
				int hash2 = Line1.hashCode();
				if (Line1.indexOf(",\"") != -1) {
                    String temp_begin = Line1.substring(0, Line1.indexOf(",\""));

                    String temp_end = (Line1.substring(Line1.indexOf("\","), Line1.length())).replace("\"", "");

                    String temp1 = (Line1.substring(Line1.indexOf("\""), Line1.indexOf("\","))).replace(",", " ");

                    Line1 = temp_begin + "," + temp1.replace("\"", "") + temp_end;
                }
				name2_dbd = Line1.split(",",name2_dbd_1.length);
				for(int hj1=0;hj1<name2_dbd_1.length;hj1++)
				{
//					System.out.println("name2_dbd="+name2_dbd[hj1]);
					
					if(name2_dbd[hj1].equalsIgnoreCase(""))
					{
//						System.out.println("values is null in dbd");
						name2_dbd[hj1]="0";
					}
				}
//				System.out.println("name2_dbd="+name2_dbd[0]);
//				System.out.println("name2_dbd="+name2_dbd[1]);
//				System.out.println("name2_dbd="+name2_dbd[2]);
//				System.out.println("name2_dbd="+name2_dbd[3]);
//				System.out.println("name2_dbd="+name2_dbd[4]);
				
				if(k==2)
				{
					test_cases_dashboard.put(String.valueOf(name2_dbd[test1_dbd.get(0)] + "_" + hash2),
							new Object[] { name2_dbd[test1_dbd.get(0)], name2_dbd[test1_dbd.get(1)]});
			results1_dbd.put(hash2+","+name2_dbd[test1_dbd.get(0)]+"_"+name2_dbd[test1_dbd.get(1)], "False");	
				}
				
				
			if(k==3)
			{
		test_cases_dashboard.put(String.valueOf(name2_dbd[test1_dbd.get(0)] + "_" + hash2),
						new Object[] { name2_dbd[test1_dbd.get(0)], name2_dbd[test1_dbd.get(1)], name2_dbd[test1_dbd.get(2)] });
		results1_dbd.put(hash2+","+name2_dbd[test1_dbd.get(0)]+"_"+name2_dbd[test1_dbd.get(1)]+"_"+name2_dbd[test1_dbd.get(2)], "False");
			}
			else if(k==4)
			{
				test_cases_dashboard.put(String.valueOf(name2_dbd[test1_dbd.get(0)] + "_" + hash2),
						new Object[] { name2_dbd[test1_dbd.get(0)], name2_dbd[test1_dbd.get(1)], name2_dbd[test1_dbd.get(2)],name2_dbd[test1_dbd.get(3)] });
				results1_dbd.put(hash2+","+name2_dbd[test1_dbd.get(0)]+"_"+name2_dbd[test1_dbd.get(1)]+"_"+name2_dbd[test1_dbd.get(2)]+"_"+name2_dbd[test1_dbd.get(3)], "False");
			}
			else if(k==5)
			{
				test_cases_dashboard.put(String.valueOf(name2_dbd[test1_dbd.get(0)] + "_" + hash2),
						new Object[] { name2_dbd[test1_dbd.get(0)], name2_dbd[test1_dbd.get(1)], name2_dbd[test1_dbd.get(2)],name2_dbd[test1_dbd.get(3)],name2_dbd[test1_dbd.get(4)] });
				results1_dbd.put(hash2+","+name2_dbd[test1_dbd.get(0)]+"_"+name2_dbd[test1_dbd.get(1)]+"_"+name2_dbd[test1_dbd.get(2)]+"_"+name2_dbd[test1_dbd.get(3)]+"_"+name2_dbd
						[test1_dbd.get(4)], "False");
			}
			else if(k==6)
			{
				test_cases_dashboard.put(String.valueOf(name2_dbd[test1_dbd.get(0)] + "_" + hash2),
						new Object[] { name2_dbd[test1_dbd.get(0)], name2_dbd[test1_dbd.get(1)], name2_dbd[test1_dbd.get(2)],name2_dbd[test1_dbd.get(3)],name2_dbd[test1_dbd.get(4)],name2_dbd[test1_dbd.get(5)]});
				results1_dbd.put(hash2+","+name2_dbd[test1_dbd.get(0)]+"_"+name2_dbd[test1_dbd.get(1)]+"_"+name2_dbd[test1_dbd.get(2)]+"_"+name2_dbd[test1_dbd.get(3)]+"_"+name2_dbd[test1_dbd.get(4)]+"_"+name2_dbd[test1_dbd.get(5)], "False");
			}
			else if(k==13)
			{
				test_cases_dashboard.put(String.valueOf(name2_dbd[test1_dbd.get(0)] + "_" + hash2),
						new Object[] { name2_dbd[test1_dbd.get(0)], name2_dbd[test1_dbd.get(1)], name2_dbd[test1_dbd.get(2)],name2_dbd[test1_dbd.get(3)],name2_dbd[test1_dbd.get(4)],name2_dbd[test1_dbd.get(5)],name2_dbd[test1_dbd.get(6)],name2_dbd[test1_dbd.get(7)],name2_dbd[test1_dbd.get(8)],name2_dbd[test1_dbd.get(9)],name2_dbd[test1_dbd.get(10)],name2_dbd[test1_dbd.get(11)],name2_dbd[test1_dbd.get(12)]});
				results1_dbd.put(hash2+","+name2_dbd[test1_dbd.get(0)]+"_"+name2_dbd[test1_dbd.get(1)]+"_"+name2_dbd[test1_dbd.get(2)]+"_"+name2_dbd[test1_dbd.get(3)]+"_"+name2_dbd[test1_dbd.get(4)]+"_"+name2_dbd[test1_dbd.get(5)], "False");
			}
			}
			
		}
		
				catch(Exception e)
				{
					e.printStackTrace();
			    }
		
		String[] key_value=null;
		String[] key_value1=null;
				
				Set<String> key1_dboard = test_cases_dashboard.keySet();
				Set<String> key_answers = test_cases_answers.keySet();
				Set<String> res_key1 = results1_dbd.keySet();
				Set<String> res_key = results_answers.keySet();
				
				
//				while (res_key_iter.hasNext()) {
//					String temp_key1 = res_key_iter.next();
//					 key_value=temp_key1.split(",");
//					 String colon=",";
//					 String Check_value=colon+key_value[1].replace("_",",");
//					 System.out.println("Key : "+Check_value);
//					
//				}
//				for (Entry<String, String> entry : results.entrySet()) {
//				    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//				}
				
		
				Iterator<String> key1_iter_dbd_1 = key1_dboard.iterator();
				System.out.println("Dashboard file size:"+key1_dboard.size());
				System.out.println("Answers File size: "+ key_answers.size());
				
				Application_Logs.info("Dashboard file size:"+key1_dboard.size());
				Application_Logs.info("Answers File size: "+ key_answers.size());
				
	
		
		if(file_name.equalsIgnoreCase("Payer Influenc"))
		{
		
		/**********************************************************************************
		 * ********************************************************************************
		 * Summing up Trx  value in Answers
		 * **********************************************************************************
		 * ********************************************************************************* 
		 */
		
		List<String> list_ans =  new ArrayList<String>();
		Set<String> set_distinct_ans = new HashSet<String>();
		
		Iterator<String> key1_iter_ans = key_answers.iterator();
		while (key1_iter_ans.hasNext()) {
			String temp_key1_ans = key1_iter_ans.next();
			Object[] temp_value_ans = test_cases_answers.get(temp_key1_ans);
			
			list_ans.add(temp_value_ans[Payer_count].toString());
		}
		
//		String Distnt_Payers_Ans[]=new String[1998];
		List<String> Distnt_Payers_Ans =  new ArrayList<String>();
		for(int y11=0;y11<list_ans.size();y11++)
		{
			set_distinct_ans.add(list_ans.get(y11));
			
		}

		
				Iterator<String> payer_dnt_ans = set_distinct_ans.iterator();
				
//				System.out.println("size of distinct Payers:"+set_distinct_ans.size());
				while(payer_dnt_ans.hasNext())
		{
					
					String value_Payer=payer_dnt_ans.next();
			Distnt_Payers_Ans.add(value_Payer);
			
		}
			
		
				
			
				  String[][] data_ans = new String[Distnt_Payers_Ans.size()][2];	
				  
				  

		for(int h1=0;h1<Distnt_Payers_Ans.size();h1++)
		{
//			System.out.println("Distinct Payers:"+Distnt_Payers_Ans[h1]);
			
			data_ans[h1][0]=Distnt_Payers_Ans.get(h1);
			
			Iterator<String> key1_iter_ans_1 = key_answers.iterator();
			int count_Payer=0;
			double sum_Trx = 0;
		while (key1_iter_ans_1.hasNext()) {
			String temp_key1_ans = key1_iter_ans_1.next();
			Object[] temp_value_ans = test_cases_answers.get(temp_key1_ans);
			
			
			if(temp_value_ans[Payer_count].toString().equalsIgnoreCase(Distnt_Payers_Ans.get(h1)))
			{
				String Trx_cnt=temp_value_ans[TRx_count].toString();
				
				 float value_Trx=Float.valueOf(Trx_cnt);
				
				 DecimalFormat df = new DecimalFormat();
				 df.setMaximumFractionDigits(3);
				
						 String value_Trx_1=(df.format(value_Trx)).replace(",","");
//						 System.out.println("TRx Value:"+value_Trx_1);
				 
				sum_Trx=sum_Trx+ Double.valueOf(value_Trx_1);
				count_Payer++;
				
			}
			
		}
		String Sum_TRx_Val=String.format("%.3f", sum_Trx);
		data_ans[h1][1]=Sum_TRx_Val;
		
//		System.out.println("Sum of Trx :"+Sum_TRx_Val);
//		System.out.println("count of Payer :"+count_Payer);
		}
		
//		System.out.println("Array in 2D in Answers");
		for( int hy1=0;hy1<Distnt_Payers_Ans.size();hy1++)
		{
//			System.out.println(data_ans[hy1][0]+" "+data_ans[hy1][1]);
			
		}

		
		/**********************************************************************************
		 * ********************************************************************************
		 * Summing up Trx  value in Answers
		 * **********************************************************************************
		 * ********************************************************************************* 
		 */
		
		
		
		/**********************************************************************************
		 * ********************************************************************************
		 * Summing up Trx  value in Dashboard
		 * **********************************************************************************
		 * ********************************************************************************* 
		 */
		
		List<String> set1 =  new ArrayList<String>();
		Set<String> set_distinct = new HashSet<String>();
		
		
		while (key1_iter_dbd_1.hasNext()) {
			String temp_key1_dbd = key1_iter_dbd_1.next();
			Object[] temp_value_dbd = test_cases_dashboard.get(temp_key1_dbd);
			
			set1.add(temp_value_dbd[Payer_count].toString());
		}
		
//		String Distnt_Payers[]=new String[25];
		List<String> Distnt_Payers =  new ArrayList<String>();
		
		for(int y1=0;y1<set1.size();y1++)
		{
			set_distinct.add(set1.get(y1));
			
		}

		
				Iterator<String> payer_dnt = set_distinct.iterator();
			
//				System.out.println("size of distinct Payers:"+set_distinct.size());
				while(payer_dnt.hasNext())
		{
					
					String value_Payer=payer_dnt.next();
			Distnt_Payers.add(value_Payer);
		
		}
			
		
				
			
				  String[][] data = new String[Distnt_Payers.size()][2];	
				  
				  

		for(int h=0;h<Distnt_Payers.size();h++)
		{
//			System.out.println("Distinct Payers:"+Distnt_Payers[h]);
			
			data[h][0]=Distnt_Payers.get(h);
			
			Iterator<String> key1_iter_dbd_2 = key1_dboard.iterator();
			int count_Payer=0;
			double sum_Trx = 0;
		while (key1_iter_dbd_2.hasNext()) {
			String temp_key1_dbd = key1_iter_dbd_2.next();
			Object[] temp_value_dbd = test_cases_dashboard.get(temp_key1_dbd);
			
			
			if(temp_value_dbd[Payer_count].toString().equalsIgnoreCase(Distnt_Payers.get(h)))
			{
				String Trx_cnt=temp_value_dbd[TRx_count].toString();
				
				 float value_Trx=Float.valueOf(Trx_cnt);
				
				 DecimalFormat df = new DecimalFormat();
				 df.setMaximumFractionDigits(3);
				
						 String value_Trx_1=(df.format(value_Trx)).replace(",","");
//						 System.out.println("TRx Value:"+value_Trx_1);
				 
				sum_Trx=sum_Trx+ Double.valueOf(value_Trx_1);
				count_Payer++;
				
			}
			
		}
		String Sum_TRx_Val=String.format("%.3f", sum_Trx);
		data[h][1]=Sum_TRx_Val;
		
//		System.out.println("Sum of Trx :"+Sum_TRx_Val);
//		System.out.println("count of Payer :"+count_Payer);
		}
		
		
		for( int hy=0;hy<Distnt_Payers.size();hy++)
		{
//			System.out.println(data[hy][0]+" "+data[hy][1]);
		}
		
		/**********************************************************************************
		 * ********************************************************************************
		 * Summing up Trx  value in Dashboard
		 * **********************************************************************************
		 * ********************************************************************************* 
		 */		
			
		////////////////////////////////////////////////////////////////////////////////
		//---------------------------------------------------------------------------------
		/**
		 * comparison
		 */
		for(int gy=0;gy<data.length;gy++)
		{
			boolean value_present=false;
			int count12=0;
//			System.out.println("dbd_value:"+data[gy][0]);
			for(int dy=0;dy<data_ans.length;dy++)
			{
//				System.out.println("dashboard_value:"+data[dy][0]);
				if(data[gy][0].equalsIgnoreCase(data_ans[dy][0]))
				{
					value_present=true;
//					System.out.println("Matched"+data_ans[dy][0]);
					
					
					failed_testcases.put(String.valueOf(row_num), new Object[] {
						data[gy][0]+","+data[gy][1],data_ans[dy][0]+","+data_ans[dy][1],"PASS"});
					row_num++;
					break;
				}
				count12++;
			}
			if(value_present==false)
			{
				String temp_ans1=" ";
				for(int k5=1;k5<k;k5++){
					temp_ans1=temp_ans1.concat(", ");
				}
				failed_testcases.put(String.valueOf(row_num), new Object[] {
					data[gy][0]+","+data[gy][1],temp_ans1,"PASS"});
				row_num++;
//				System.out.println("not matched"+data[gy][0]);
			}
//			System.out.println("count value"+count12);
		}
	////////////////////////////////////////////////////////////////////////////////
	//---------------------------------------------------------------------------------
		String temp_dashb=new String("From Dashboard, ");
		String temp_answ=new String("From Answers");
//		String temp_dashb1=new String();
//		String temp_answ1=new String();
		
		System.out.println("Data : "+temp_dashb+"\n"+temp_answ);
		failed_testcases.put(String.valueOf(0), new Object[] {
			temp_dashb, temp_answ  ,"Status"});
		}
		
		
		
		
		else
		{
		
		 
		 /*******************************************************************
		  * 
		  * first dashboard file and then answers file
		  * 
		  * *****************************************************************
		  */
	Iterator<String> key1_iter_dbd = key1_dboard.iterator();
				while (key1_iter_dbd.hasNext()) {
					boolean check=false;
					String temp_key1_dbd = key1_iter_dbd.next();
//					 System.out.println("Key : "+temp_key1_dbd);
					Iterator<String> key_iter_answers = key_answers.iterator();
				
					String Dashboard_Value = null;
					
					while (key_iter_answers.hasNext()) {
						String temp_key_answers = key_iter_answers.next();
//						 System.out.println("Key 1 : "+temp_key_answers);
//						String temp_temp = temp_key1_dbd.substring(0, 10);
//						String temp_temp1 = temp_key_answers.substring(0, 10);
						String temp_temp[]=null;
						String temp_temp1[]=null;
						 temp_temp = temp_key1_dbd.split("_");
						 temp_temp1 = temp_key_answers.split("_");
						 
//						if(temp_temp1[0].contains("/")||temp_temp[0].contains("/"))
//						{
//						 DateFormat userDateFormat = new SimpleDateFormat("MM/dd/yyyy");
//						 DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-MM-dd");
//						 Date date = null;						
//						try {
//							date = userDateFormat.parse(temp_temp1[0]);
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						
//						temp_temp1[0] = dateFormatNeeded.format(date);
//						 
//						}
						
		
						
//						if ((temp_key1_dbd.substring(0, 10)).equals(temp_key_answers.substring(0,
//								10))) {
							if(temp_temp1[0].equalsIgnoreCase(temp_temp[0]))
							{
//							System.out.println("Temp String  : "+temp_temp1+"\t"+temp_temp[0]);
							Object[] temp_value_answers = test_cases_answers.get(temp_key_answers);
							
							
							
							StringBuilder temp_value_str_answers = null;
							temp_value_str_answers = new StringBuilder();
							for (Object temp : temp_value_answers) {
		
								temp_value_str_answers.append("," + temp.toString());
							}
							Object[] temp1_value_dashboard = test_cases_dashboard.get(temp_key1_dbd);
							StringBuilder temp_value_str1_dbd = null;
							temp_value_str1_dbd = new StringBuilder();
							for (Object temp : temp1_value_dashboard) {
								temp_value_str1_dbd.append("," + temp.toString());
								
//								System.out.println("Dashboard_Value:"+Dashboard_Value);
							}
							Dashboard_Value=temp_value_str1_dbd.toString();
							if (temp_value_str_answers.toString().equals(
									temp_value_str1_dbd.toString())) {
								check=true;
//								 System.out.println("Data match : \t" + "File :"
//										 + temp_value_str_answers.toString() + "\t File 1 :"
//										 + temp_value_str1_dbd.toString());
										 count++;
										 count1++;
//										 Iterator<String> res_key_iter = res_key.iterator();
//											
//								while (res_key_iter.hasNext()) {
//									String temp_key_1 = res_key_iter.next();
//									
//									 key_value=temp_key_1.split(",");
//									
//									 String colon=",";
//									 String Check_value=colon+key_value[1].replace("_",",");
////									 System.out.println("Key : "+Check_value);
////									 System.out.println("Key string: "+temp_value_str.toString());
//									 if((temp_value_str_answers.toString()).equals(Check_value))
//									 {
////										System.out.println("matchd:"+(temp_value_answers.toString()));
//										 results_answers.put(temp_key_1, "True");
////										 failed_testcases.put(String.valueOf(row_num), new Object[] {count,
////											 Check_value,"PASS"});
////										 row_num++;
////										 break;
//									 }
//									 
//								}
//								Iterator<String> res_key_iter_1 = res_key1.iterator();
//									 while (res_key_iter_1.hasNext()) {
//											String temp_key_2 = res_key_iter_1.next();
//											 key_value1=temp_key_2.split(",");
//											 String colon1=",";
//											  Check_value1=colon1+key_value1[1].replace("_",",");
////											 System.out.println("Key : "+Check_value1);
//											 if((temp_value_str1_dbd.toString()).equals(Check_value1))
//											 {
////												 results1_dbd.put(temp_key_2, "True");
////												 failed_testcases.put(String.valueOf(row_num), new Object[] { count1,
////														" ", Check_value1 });
////												 row_num++;
//												 break;
//											 }
//											
//										
//								}
//									 String temp1=temp_value_str1_dbd.toString().replaceAll(",","\n");
//									 System.out.println(temp1);
									 failed_testcases.put(String.valueOf(row_num), new Object[] {
										 temp_value_str1_dbd.toString(),temp_value_str_answers.toString(),"PASS"});
								 row_num++;
								 break;
							} else {
//								 System.out.println("Data NOT match : \t" + "File :"
//								 + temp_value_str.toString() + "\t File 1 :"
//								 + temp_value_str1.toString());
							}
						}
						
					}
					if(check==false)
					{
						String temp_ans1=" ";
						for(int k5=1;k5<k;k5++){
							temp_ans1=temp_ans1.concat(", ");
						}
						 failed_testcases.put(String.valueOf(row_num), new Object[] {
							 Dashboard_Value,temp_ans1,"FAIL"});
						 row_num++;
						Application_Logs.info("Failed Testcase in Dashboard File:"+Dashboard_Value);
					}
					
		
				}
				
				 /*******************************************************************
				  * 
				  * first answers file and then dashboard file
				  * 
				  * *****************************************************************
				  */
				String Answers_value=null;
				Iterator<String> key_iter_answers = key_answers.iterator();
				while (key_iter_answers.hasNext()) {
					String temp_key_answers = key_iter_answers.next();
//					 System.out.println("Key : "+temp_key1_dbd);
					boolean check=false;
				
					
					Iterator<String> key1_iter_dbd_11 = key1_dboard.iterator();
				
						while (key1_iter_dbd_11.hasNext()) {
							
							String temp_key1_dbd = key1_iter_dbd_11.next();
//						 System.out.println("Key 1 : "+temp_key);
//						String temp_temp = temp_key1_dbd.substring(0, 10);
//						String temp_temp1 = temp_key_answers.substring(0, 10);
						String temp_temp[]=null;
						String temp_temp1[]=null;
						 temp_temp = temp_key1_dbd.split("_");
						 temp_temp1 = temp_key_answers.split("_");
		
						
//						if ((temp_key1_dbd.substring(0, 10)).equals(temp_key_answers.substring(0,
//								10))) {
							if(temp_temp[0].equalsIgnoreCase(temp_temp1[0]))
							{
//							System.out.println("Temp String  : "+temp_temp1+"\t"+temp_temp);
							Object[] temp_value_answers = test_cases_answers.get(temp_key_answers);
							StringBuilder temp_value_str_answers = null;
							temp_value_str_answers = new StringBuilder();
							for (Object temp : temp_value_answers) {
		
								temp_value_str_answers.append("," + temp.toString());
								Answers_value=temp_value_str_answers.toString();
							}
							Object[] temp1_value_dashboard = test_cases_dashboard.get(temp_key1_dbd);
							StringBuilder temp_value_str1_dbd = null;
							temp_value_str1_dbd = new StringBuilder();
							for (Object temp : temp1_value_dashboard) {
								temp_value_str1_dbd.append("," + temp.toString());
							
							}
							if (temp_value_str_answers.toString().equals(
									temp_value_str1_dbd.toString())) {
								check=true;
//							
								 break;
							} 
						}
						
					}
					if(check==false)
					{
						String temp_dashb1="";
						for(int k3=2;k3<k;k3++){
						temp_dashb1=temp_dashb1.concat(", ");
						}

						 failed_testcases.put(String.valueOf(row_num), new Object[] {temp_dashb1,
							 Answers_value,"FAIL"});
						 row_num++;
						Application_Logs.info("Failed Testcase in Answers File:"+Answers_value);
					}
					
		
				}
				
				System.out.println("count of matched values in "+Dashboard_filename+"="+(count-1));
				Application_Logs.info("count of matched values in "+Dashboard_filename+"="+(count-1));
//				for (Map.Entry<String, String> entry : results.entrySet()) {
//				    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//				}
				
//				Set<String> key9 = failed_testcases.keySet();
//				System.out.println("values in key9 ="+key9.size());
//				Object[] objArr = failed_testcases.get(String.valueOf(1219));
//				System.out.println("Outside loop objvalue:"+objArr[1]);
//				Iterator<String> key_iter = key9.iterator();
//				 while(key_iter.hasNext())
//                 {
//                	String temp_key = key_iter.next();
//                	 System.out.println("temp_key = "+temp_key);
//                	Object[] temp_value = failed_testcases.get(temp_key);
//                System.out.println("objvalue:"+temp_value[1]);
//                 }
                	
				String temp_dashb=new String("From Dashboard ");
				String temp_answ=new String("From Answers ");
//				String temp_dashb1=new String();
//				String temp_answ1=new String();
				for(int k1=1;k1<k;k1++){
					temp_dashb=temp_dashb.concat(", ");
					
				}
				for(int k2=2;k2<k;k2++){
					temp_answ=temp_answ.concat(", ");
					
				}
				System.out.println("Data : "+temp_dashb+"\n"+temp_answ);
				failed_testcases.put(String.valueOf(0), new Object[] {
					temp_dashb, temp_answ  ,"Status"});
				
		}	
				
				try
				{
					
					
					if(row_num>100000)
					{
						System.out.println("Entered into writing to csv");
						String ResultPath = "./output/Data_comparison_Results/"+file_name+"/"+filename;
						// String sNewFolderName = "Result as on" + ual.getDateTimeStamp();
//						ResultPath = ResultPath + "/Results on " + userAutomationLibrary.getDateTimeStamp();
						
						StringBuilder String_Val = null;
						String_Val = new StringBuilder();
						
						 FileWriter fileWriter = new FileWriter(ResultPath);
//				            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				            for (int i = 0; i < row_num; i++) {
				            Object[] objArr = null;
							objArr=failed_testcases.get(String.valueOf(i));
				            
				            for (Object obj : objArr) {
				            	
				            	 
				            	
								
								//cell.setCellValue(obj.toString());
//								System.out.println("object = "+obj.toString());
								String[] values = ((String) obj).split(",");
								for (String temp_String : values) {
//									System.out.println("values:"+temp_String);
									
									
									if(temp_String.equalsIgnoreCase("FAIL"))
										{
										
										String_Val.append(","+temp_String);
										}
										else if(temp_String.equalsIgnoreCase("PASS"))
										{
											
											String_Val.append(","+temp_String);
											
										}
										else {
											Scanner lineScanner = new Scanner(temp_String);
									        lineScanner.useDelimiter(",");
									        //String output =" ";
									            // While there is more text to get it will loop.
									            while(lineScanner.hasNext()) {						            	
									            	 
									                String output = lineScanner.next();
									                String_Val.append(","+output);
									                // Write the output to that cell.
									               						                
									            }
										}
				           
				           
					}
				            }
				            String_Val.append("\n");
				            }
				            fileWriter.write(String_Val.toString());
				            fileWriter.flush();
				            fileWriter.close();
				            }
					
					else
					{
					System.out.println("entered into writing to excel");
				excelFileUpdate excel_new = new excelFileUpdate();
				String ResultPath = "./output/Data_comparison_Results/"+file_name+"/";
				// String sNewFolderName = "Result as on" + ual.getDateTimeStamp();
				ResultPath = ResultPath + "/Results on " + userAutomationLibrary.getDateTimeStamp();
				/**
				 * to write the test case report to an excel file
				 */
				try
				{
				excel_new.excelAddData_NewWay(
						"./output/Data_comparison_Results/"+file_name+"/"+comparison_filename,failed_testcases,
						userAutomationLibrary.getDateTimeStamp().toString());
				}
				catch(Exception e)
				{
					System.out.println("error in calling the excel add data function");
				}
				File folder = new File(ResultPath);
				File curr_dir = new File("./output/Data_comparison_Results/"+file_name+"/");
				if (curr_dir.isDirectory()) {
					File[] excel_files = curr_dir.listFiles();
					for (File file_temp : excel_files) {
						if (file_temp.isFile()) {
							try {

								FileUtils.moveFileToDirectory(file_temp, folder, true);
								
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
							try
							{
								failed_testcases.clear();
								test_cases_answers.clear();
								test_cases_dashboard.clear();
								test.clear();
								test1.clear();
								test_ans.clear();
								test1_dbd.clear();
								results_answers.clear();
								results1_dbd.clear();
								
							}
							catch(UnsupportedOperationException t)
							{
								System.out.println("Error in  clearing");
							}
						}
					
					}
				}
				
				}
				catch(Exception e)
				{
					Application_Logs.info("error in writing to excel:");
				}
//				excel_new.excelAddData_Hyperlink("./output/Result_Data_comparison_testcases1.xlsx",
//						failed_testcases);
				
	}
	
	
	/************************************************************************************
	 */
	
	public static String diffSideBySide(String fromStr, String toStr){
	    // this is equivalent of running unix diff -y command
	    // not pretty, but it works. Feel free to refactor against unit test.
	    String[] fromLines = fromStr.split("\n");
	    String[] toLines = toStr.split("\n");
	    @SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
		List<Difference> diffs = (new Diff(fromLines, toLines)).diff();

	    int padding = 3;
	    int maxStrWidth = Math.max(maxLength(fromLines), maxLength(toLines)) + padding;

	    StrBuilder diffOut = new StrBuilder();
	    diffOut.setNewLineText("\n");
	    int fromLineNum = 0;
	    int toLineNum = 0;
	    for(Difference diff : diffs) {
	        int delStart = diff.getDeletedStart();
	        int delEnd = diff.getDeletedEnd();
	        int addStart = diff.getAddedStart();
	        int addEnd = diff.getAddedEnd();

	        boolean isAdd = (delEnd == Difference.NONE && addEnd != Difference.NONE);
	        boolean isDel = (addEnd == Difference.NONE && delEnd != Difference.NONE);
	        boolean isMod = (delEnd != Difference.NONE && addEnd != Difference.NONE);

	        //write out unchanged lines between diffs
	        while(true) {
	            String left = "";
	            String right = "";
	            if (fromLineNum < (delStart)){
	                left = fromLines[fromLineNum];
	                fromLineNum++;
	            }
	            if (toLineNum < (addStart)) {
	                right = toLines[toLineNum];
	                toLineNum++;
	            }
	            diffOut.append(StringUtils.rightPad(left, maxStrWidth));
	            diffOut.append("  "); // no operator to display
	            diffOut.appendln(right);

	            if( (fromLineNum == (delStart)) && (toLineNum == (addStart))) {
	                break;
	            }
	        }

	        if (isDel) {
	            //write out a deletion
	            for(int i=delStart; i <= delEnd; i++) {
	                diffOut.append(StringUtils.rightPad(fromLines[i], maxStrWidth));
	                diffOut.appendln("<");
	            }
	            fromLineNum = delEnd + 1;
	        } else if (isAdd) {
	            //write out an addition
	            for(int i=addStart; i <= addEnd; i++) {
	                diffOut.append(StringUtils.rightPad("", maxStrWidth));
	                diffOut.append("> ");
	                diffOut.appendln(toLines[i]);
	            }
	            toLineNum = addEnd + 1; 
	        } else if (isMod) {
	            // write out a modification
	            while(true){
	                String left = "";
	                String right = "";
	                if (fromLineNum <= (delEnd)){
	                    left = fromLines[fromLineNum];
	                    fromLineNum++;
	                }
	                if (toLineNum <= (addEnd)) {
	                    right = toLines[toLineNum];
	                    toLineNum++;
	                }
	                diffOut.append(StringUtils.rightPad(left, maxStrWidth));
	                diffOut.append("| ");
	                diffOut.appendln(right);

	                if( (fromLineNum > (delEnd)) && (toLineNum > (addEnd))) {
	                    break;
	                }
	            }
	        }

	    }

	    //we've finished displaying the diffs, now we just need to run out all the remaining unchanged lines
	    while(true) {
	        String left = "";
	        String right = "";
	        if (fromLineNum < (fromLines.length)){
	            left = fromLines[fromLineNum];
	            fromLineNum++;
	        }
	        if (toLineNum < (toLines.length)) {
	            right = toLines[toLineNum];
	            toLineNum++;
	        }
	        diffOut.append(StringUtils.rightPad(left, maxStrWidth));
	        diffOut.append("  "); // no operator to display
	        diffOut.appendln(right);

	        if( (fromLineNum == (fromLines.length)) && (toLineNum == (toLines.length))) {
	            break;
	        }
	    }

	    return diffOut.toString();
	}

	
	public static HashMap<String,Object[][]> diffSideBySideExcel(String fromStr, String toStr){
	    // this is equivalent of running unix diff -y command
	    // not pretty, but it works. Feel free to refactor against unit test.
	    String[] fromLines = fromStr.split("\n");
	    String[] toLines = toStr.split("\n");
	    @SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
		List<Difference> diffs = (new Diff(fromLines, toLines)).diff();

	    int padding = 3;
	    int maxStrWidth = Math.max(maxLength(fromLines), maxLength(toLines)) + padding;

	    HashMap<String,Object[][]> diffOutHash=new HashMap<String,Object[][]>();
	    StrBuilder diffOut = new StrBuilder();
	    diffOut.setNewLineText("\n");
	    int fromLineNum = 0;
	    int toLineNum = 0;
	    for(Difference diff : diffs) {
	        int delStart = diff.getDeletedStart();
	        int delEnd = diff.getDeletedEnd();
	        int addStart = diff.getAddedStart();
	        int addEnd = diff.getAddedEnd();

	        boolean isAdd = (delEnd == Difference.NONE && addEnd != Difference.NONE);
	        boolean isDel = (addEnd == Difference.NONE && delEnd != Difference.NONE);
	        boolean isMod = (delEnd != Difference.NONE && addEnd != Difference.NONE);

	        //write out unchanged lines between diffs
	        while(true) {
	            String left = "";
	            String right = "";
	            if (fromLineNum < (delStart)){
	                left = fromLines[fromLineNum];
	                fromLineNum++;
	            }
	            if (toLineNum < (addStart)) {
	                right = toLines[toLineNum];
	                toLineNum++;
	            }
	            diffOut.append(StringUtils.rightPad(left, maxStrWidth));
//	            diffOutHash.put(0, null);
	            diffOut.append("  "); // no operator to display
	            diffOut.appendln(right);

	            if( (fromLineNum == (delStart)) && (toLineNum == (addStart))) {
	                break;
	            }
	        }

	        if (isDel) {
	            //write out a deletion
	            for(int i=delStart; i <= delEnd; i++) {
	                diffOut.append(StringUtils.rightPad(fromLines[i], maxStrWidth));
	                diffOut.appendln("<");
	            }
	            fromLineNum = delEnd + 1;
	        } else if (isAdd) {
	            //write out an addition
	            for(int i=addStart; i <= addEnd; i++) {
	                diffOut.append(StringUtils.rightPad("", maxStrWidth));
	                diffOut.append("> ");
	                diffOut.appendln(toLines[i]);
	            }
	            toLineNum = addEnd + 1; 
	        } else if (isMod) {
	            // write out a modification
	            while(true){
	                String left = "";
	                String right = "";
	                if (fromLineNum <= (delEnd)){
	                    left = fromLines[fromLineNum];
	                    fromLineNum++;
	                }
	                if (toLineNum <= (addEnd)) {
	                    right = toLines[toLineNum];
	                    toLineNum++;
	                }
	                diffOut.append(StringUtils.rightPad(left, maxStrWidth));
	                diffOut.append("| ");
	                diffOut.appendln(right);

	                if( (fromLineNum > (delEnd)) && (toLineNum > (addEnd))) {
	                    break;
	                }
	            }
	        }

	    }

	    //we've finished displaying the diffs, now we just need to run out all the remaining unchanged lines
	    while(true) {
	        String left = "";
	        String right = "";
	        if (fromLineNum < (fromLines.length)){
	            left = fromLines[fromLineNum];
	            fromLineNum++;
	        }
	        if (toLineNum < (toLines.length)) {
	            right = toLines[toLineNum];
	            toLineNum++;
	        }
	        diffOut.append(StringUtils.rightPad(left, maxStrWidth));
	        diffOut.append("  "); // no operator to display
	        diffOut.appendln(right);

	        if( (fromLineNum == (fromLines.length)) && (toLineNum == (toLines.length))) {
	            break;
	        }
	    }

//	    return diffOut.toString();
	    return diffOutHash;
	}

	
	private static int maxLength(String[] fromLines) {
	    int maxLength = 0;

	    for (int i = 0; i < fromLines.length; i++) {
	        if (fromLines[i].length() > maxLength) {
	            maxLength = fromLines[i].length();
	        }
	    }
	    return maxLength;
	}
	 
	
}
