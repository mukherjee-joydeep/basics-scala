/**
 * 
 */
package AutomationTests;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import PageOModel.BrowserFactory;
import PageOModel.GM_First_Page;
import PageOModel.GM_Molecule_drill_Page;
import PageOModel.Login_Page;
import PageOModel.Menu_Page;
import commonLibs.automation.excelFileUpdate;
import commonLibs.automation.userAutomationLibrary;

/**
 * @author JMukherjee
 *
 */
public class ReleaseTest_POM extends AutomationTest_ISSD{

	private WebDriver driver;
	String page1, page2;
	String menuitem;
	String menuPage;
	String[] filterControls;
	private String sheetName;

	@BeforeTest
	public void beforeSetUp()  {

	}

	public WebDriver getDriver() {
		return driver;
	}

	@DataProvider(name = "menu_name")
	public static Object[] moduleNames() {
		return new Object[] { "Rx Brands"};//"OTC Categories",, "Generic Suppliers",  "Generic Molecules" 
	}

	@Parameters({ "browser", "RTurl" })
	@Test(priority = 5)
	public void OpenReleaseTest_env(String browser, String url) throws InterruptedException   {

		try {
			this.driver = BrowserFactory.startBrowser(browser, url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread.sleep(1000);
	}

	@Parameters({ "username", "password","sheetName" })
	@Test(priority = 6, timeOut = 290000, dependsOnMethods = { "OpenReleaseTest_env" })
	public void login(String username, String password,String sheetName) {
		this.sheetName=sheetName;
		try {
			Login_Page login_page = PageFactory.initElements(driver, Login_Page.class);
			new userAutomationLibrary().captureScreen(driver, "ReleaseTest", "PASS", "OpenURL");
			login_page.login_check(username, password);
			// Thread.sleep(18000);
		} catch (Exception e) {
			new userAutomationLibrary().captureScreen(driver, "ReleaseTest", "FAIL", "OpenURL");
		}
	}

	@Test(priority = 7, timeOut = 49000000,dependsOnMethods = { "login" }, dataProvider = "menu_name")
	public void menuSelect(String menu) throws Exception {
		this.menuitem = menu;
		this.menuPage = this.menuitem.concat(" Page 1");
		try {
			driver.manage().timeouts().pageLoadTimeout(290, TimeUnit.MINUTES);
			driver.manage().timeouts().implicitlyWait(290, TimeUnit.MINUTES);
			new userAutomationLibrary().captureScreen(driver, "ReleaseTest", "PASS", "First page");
		} catch (Exception e) {
			new userAutomationLibrary().captureScreen(driver, "ReleaseTest", "FAIL", "First page");
		}
		Menu_Page menu_page = PageFactory.initElements(driver, Menu_Page.class);
		// menu_page.menu_click("Performance");

		menu_page.manf_menu_click(menu);
		Thread.sleep(35000);

		Assert.assertTrue(menu_page.getPage_title().getText().contains(menu),
				"clicked menu and page came are *NOT* same");

		GMdrillDownPages(menu);

	}

	// @Test(timeOut = 290000)
	public void GMdrillDownPages(String menu) throws Exception {
		driver.manage().timeouts().pageLoadTimeout(290, TimeUnit.MINUTES);
		driver.manage().timeouts().implicitlyWait(290, TimeUnit.MINUTES);

		

		/**
		 * Invoking GM first page for further GM molecule drill down
		 */
		GM_First_Page gm_fpage = PageFactory.initElements(driver, GM_First_Page.class);
		Assert.assertTrue(gm_fpage.getPage_title().getText().contains(menu), "First page is *NOT* same");
		HashMap<String, Object[]> pageFilters = new excelFileUpdate().getExcelData(this.sheetName);
		if (pageFilters.containsKey("Filters")) {
			this.filterControls = (String[]) pageFilters.get("Filters");
		} 
		Set<String> filter_page = pageFilters.keySet();
		String matchKey = null;
		String menu_page = menu.concat(" Page 1");
		for (String temp : filter_page) {
			if (temp.trim().equalsIgnoreCase(menu_page)) {
				matchKey = temp;
				// System.out.println("Matched");
			}
		}

		if (matchKey != null) {
			String[] filterNames = (String[]) pageFilters.get(matchKey);
			gm_fpage.openOptions();
			Thread.sleep(500);
			for (int i = 0; i < filterNames.length; i++) {
				Thread.sleep(100);
				gm_fpage.selectFilters(filterControls[i], filterNames[i]);
				Thread.sleep(500);
			}
			Assert.assertTrue(gm_fpage.updateButton.isEnabled(), "Filters are *NOT* applied");
			gm_fpage.updateFilters();
			Thread.sleep(25000);
			gm_fpage.openExpanders();
			Thread.sleep(500);

		} else {
			System.out.println("Key *NOT* found");
		}
		csvSaveFile(menu_page, gm_fpage.valueData());

		/**
		 * fetching top 20 molecule names displayed on GM first page
		 */
		List<String> molecules = gm_fpage.getGM_molecules();
		molecules.parallelStream().forEach(Logger.getLogger(this.getClass())::info);
		/**
		 * Iterating through each molecule and enter molecule drill down
		 */
		for (String molecule : molecules) {
			gm_fpage = PageFactory.initElements(driver, GM_First_Page.class);
			gm_fpage.Molecule_drillDown(molecule);
			Thread.sleep(25000);
			drillPages(menu, molecule);
		}
	}

	// @Test(timeOut = 290000)
	public void csvSaveFile(String menu, List<String> dataRows) throws Exception {
		/**
		 * Saving page to CSV file
		 */
		Thread.sleep(100);
		StringBuilder sb1 = new StringBuilder();
		dataRows.stream().forEach(sb1::append);
		new userAutomationLibrary().csvFileWrite("ReleaseTest_" + menu, sb1);
	}

	public void csvSaveFile(String menu) throws Exception {
		/**
		 * Saving page to CSV file
		 */
		Thread.sleep(8000);
		String page1 = driver.findElement(By.tagName("body")).getText();
		StringBuilder sb1 = new StringBuilder(page1);
		new userAutomationLibrary().csvFileWrite("ReleaseTest_" + menu, sb1);
	}
	public void drillPages(String menu, String molecule) throws Exception {

		
//		Thread.sleep(1000);
		driver.manage().timeouts().pageLoadTimeout(290, TimeUnit.MINUTES);
		driver.manage().timeouts().implicitlyWait(290, TimeUnit.MINUTES);
		/**
		 * waiting for GM molecule page to load
		 */
		GM_Molecule_drill_Page drilldownPage = PageFactory.initElements(driver, GM_Molecule_drill_Page.class);
		Assert.assertTrue(drilldownPage.getPage_title().getText().equalsIgnoreCase(molecule),
				"Drill down page is *NOT* same");

		drilldownPage.openOptions();
		Thread.sleep(500);

		String menu_page = menu.concat(" Page 2");

		HashMap<String, Object[]> pageFilters_d = new excelFileUpdate().getExcelData(this.sheetName);
		if (pageFilters_d.containsKey("Filters")) {
			this.filterControls = (String[]) pageFilters_d.get("Filters");
		}
		Set<String> filter_page = pageFilters_d.keySet();
		String matchKey = null;
		for (String temp : filter_page) {
			if (temp.trim().equalsIgnoreCase(menu_page)) {
				matchKey = temp;
			}
		}
		if (matchKey != null) {
			String[] filterNames = (String[]) pageFilters_d.get(matchKey);
			for (int i = 0; i < filterNames.length; i++) {
				Thread.sleep(100);
				drilldownPage.selectFilters(filterControls[i], filterNames[i]);
				Thread.sleep(500);
			}
			Assert.assertTrue(drilldownPage.updateButton.isEnabled(), "Filters are *NOT* applied");
			drilldownPage.updateFilters();
			Thread.sleep(25000);
			drilldownPage.openExpanders();
			Thread.sleep(500);
		} else {
			System.out.println("Key *NOT* found");
		}
		csvSaveFile(menu_page+"_"+molecule, drilldownPage.valueData());
		Thread.sleep(1000);
		drilldownPage.getBack(menu);
		Thread.sleep(28000);
	}
	// /**
	// * saving each GM molecule drill down pages in CSV file
	// */
	// String page2 = driver2.findElement(By.tagName("body")).getText();
	// // com.example.test.userAutomationLibrary ual=new
	// // com.example.test.userAutomationLibrary();
	// StringBuilder sb2 = new StringBuilder(page2);
	// ual.csvFileWrite("ReleaseTest_GMpages_" + molecule, sb2);
	//
	// /**
	// * Invoking GM Molecule drill page for further GM brand drill down
	// */
	// GM_Molecule_drill_Page gm_Mpage = PageFactory.initElements(driver2,
	// GM_Molecule_drill_Page.class);
	// /**
	// * fetching top 20 brand names displayed on GM Molecule drill page
	// */
	// Thread.sleep(30000);
	// List<String> gmBrands = gm_Mpage.getGM_brands();
	//
	// /**
	// * Iterating through each brand and enter brand drill down
	// */
	// for (String brand : gmBrands) {
	// gm_Mpage = PageFactory.initElements(driver2,
	// GM_Molecule_drill_Page.class);
	// Thread.sleep(30000);
	// gm_Mpage.Brand_drillDown(brand);
	// /**
	// * waiting for GM brand page to load
	// */
	// Thread.sleep(20000);
	// /**
	// * saving each GM molecule drill down pages in CSV file
	// */
	// String page3 = driver2.findElement(By.tagName("body")).getText();
	// // com.example.test.userAutomationLibrary ual=new
	// // com.example.test.userAutomationLibrary();
	// StringBuilder sb3 = new StringBuilder(page3);
	// ual.csvFileWrite("ReleaseTest_GMpages_" + molecule + "_" + brand, sb3);
	// driver2.navigate().back();
	// Thread.sleep(30000);
	// }
	// driver2.navigate().back();
	// Thread.sleep(30000);
	// }

	/**
	 * 1.tomorrow's task is to segregate them in 3 different tests so that we
	 * can measure the time taken for each of the pages 2.Create drill down for
	 * all the other modules 3.capture each line of source code in HashMap and
	 * compare and create the HTML report or Excel report 4.search for something
	 * to stack each of those drill down pages separately and capture load time
	 */
	// page2=driver2.findElement(By.tagName("body")).getText();
	// com.example.test.userAutomationLibrary ual=new
	// com.example.test.userAutomationLibrary();
	// StringBuilder sb2=new StringBuilder(page2);
	// ual.csvFileWrite("ReleaseTest_pages", sb2);
	// System.out.println(page2);

	@AfterTest
	public void AfterRun() throws Exception {

		// driver.quit();
		Thread.sleep(1000);

		driver.quit();
		
		// Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
	}

	// @AfterSuite
	// public void AfterSuite() throws IOException {
	// /**
	// * reading 2 CSV files
	// */
	// BufferedReader br1 = new BufferedReader(
	// new InputStreamReader(new
	// FileInputStream("./input_data/AutomationTest_Generic Molecules.csv")));
	// BufferedReader br2 = new BufferedReader(
	// new InputStreamReader(new
	// FileInputStream("./input_data/ReleaseTest_Generic Molecules.csv")));
	//
	// // Files.lines(Paths.get("manifest.mf"),
	// // StandardCharsets.UTF_8).forEach(temp1->temp1.concat());
	// //
	// // Files.lines(Paths.get("manifest.mf"),
	// // StandardCharsets.UTF_8).forEach(System.out::println);
	//
	// /**
	// * storing data from first CSV file cell wise
	// */
	// String temp1 = readFile("./input_data/AutomationTest_Generic
	// Molecules.csv", StandardCharsets.UTF_8);
	//
	// String temp2 = readFile("./input_data/ReleaseTest_Generic Molecules.csv",
	// StandardCharsets.UTF_8);
	//
	//
	// StringBuilder sb = new StringBuilder();
	// sb.append(userAutomationLibrary.diffSideBySide(temp1, temp2));
	// new userAutomationLibrary().csvFileWrite("diff", sb);
	//
	// }
	//
	// static String readFile(String path, Charset encoding) throws IOException
	// {
	// byte[] encoded = Files.readAllBytes(Paths.get(path));
	// return new String(encoded, encoding);
	// }
}
