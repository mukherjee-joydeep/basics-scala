/**
 * 
 */
package AutomationTests;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import PageOModel.BrowserFactory;
import PageOModel.Login_Page;
import PageOModel.Menu_Page;
import PageOModel.Performance_drill_page;
import PageOModel.Performance_first_page;
import commonLibs.automation.userAutomationLibrary;

/**
 * @author JMukherjee
 *
 */
public class Perf_mod_page1_Test extends AutomationTest_ISSD {

	private WebDriver driver;
	String page1, page2;
	String menuitem;
	String menuPage;
	String[] filterControls;
	private String sheetName;
	static String[] modulePages;

	@BeforeTest
	public void beforeSetUp() {

	}

	public WebDriver getDriver() {
		return driver;
	}

	@DataProvider(name = "menu_name")
	public static Object[] moduleNames() {
		return modulePages;
		// new Object[] { "Rx Brands","Generic Molecules","Generic
		// Suppliers"};//"OTC Categories",, "Generic Suppliers", "Generic
		// Molecules"
	}

	@Parameters({ "browser", "ATurl" })
	@Test(priority = 1)
	public void OpenReleaseTest_env(String browser, String url) throws InterruptedException {

		try {
			this.driver = BrowserFactory.startBrowser(browser, url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(1000);
	}

	@Parameters({ "username", "password", "sheetName", "modulePages" })
	@Test(priority = 2, timeOut = 290000, dependsOnMethods = { "OpenReleaseTest_env" })
	public void login(String username, String password, String sheetName, String modulePages) {
		this.sheetName = sheetName;
		this.modulePages = modulePages.split(",");
		try {
			Login_Page login_page = PageFactory.initElements(driver, Login_Page.class);
			new userAutomationLibrary().captureScreen(driver, "ReleaseTest", "PASS", "OpenURL");
			login_page.login_check(username, password);
			Thread.sleep(18000);
		} catch (Exception e) {
			new userAutomationLibrary().captureScreen(driver, "ReleaseTest", "FAIL", "OpenURL");
		}
	}

	@Test(priority = 3, timeOut = 49000000, dependsOnMethods = { "login" }, dataProvider = "menu_name")
	public void menuSelect(String menu) throws InterruptedException {
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
		menu_page.perf_menu.click();
		// menu_page.manf_menu_click(menu);
		Thread.sleep(45000);
		Assert.assertTrue(menu_page.getPage_title().getText().contains("Country Overview"),
				"clicked menu and page came are *NOT* same");
		try {
			GMdrillDownPages(menu);
		} catch (org.openqa.selenium.UnhandledAlertException e) {
			org.apache.log4j.Logger.getLogger(this.getClass().toGenericString())
					.info("Inside catch of Alert exception");
			driver.switchTo().alert().accept();
			Thread.sleep(30000);
			GMdrillDownPages(menu);
		}

	}

	// @Test(timeOut = 290000)
	public void GMdrillDownPages(String menu) throws InterruptedException {
		driver.manage().timeouts().pageLoadTimeout(290, TimeUnit.MINUTES);
		driver.manage().timeouts().implicitlyWait(290, TimeUnit.MINUTES);
		/**
		 * Invoking GM first page for further GM molecule drill down
		 */
		Performance_first_page gm_fpage = PageFactory.initElements(driver, Performance_first_page.class);
//		Assert.assertTrue(gm_fpage.getPage_title().getText().contains(menu), "First page is *NOT* same");
//		HashMap<String, Object[]> pageFilters = new excelFileUpdate().getExcelData(this.sheetName);
//		if (pageFilters.containsKey("Filters")) {
//			this.filterControls = (String[]) pageFilters.get("Filters");
//		}
//		Set<String> filter_page = pageFilters.keySet();
//		String matchKey = null;
//		String menu_page = menu.concat(" Page 1");
		String menu_page = "Performance Page 1";
		
//		for (String temp : filter_page) {
//			if (temp.trim().equalsIgnoreCase(menu_page)) {
//				matchKey = temp;
//			}
//		}
//
//		if (matchKey != null) {
//			String[] filterNames = (String[]) pageFilters.get(matchKey);
//			gm_fpage.openOptions();
//			Thread.sleep(500);
//			for (int i = 0; i < filterNames.length; i++) {
//				Thread.sleep(100);
//				gm_fpage.selectFilters(filterControls[i], filterNames[i]);
//				Thread.sleep(500);
//			}
//			Assert.assertTrue(gm_fpage.updateButton.isEnabled(), "Filters are *NOT* applied");
//			gm_fpage.updateFilters();
			Thread.sleep(45000);
//			gm_fpage.openExpanders();
//			Thread.sleep(500);
//
//		} else {
//			System.out.println("Key *NOT* found");
//		}
		csvSaveFile(menu_page, gm_fpage.valueData());

		/**
		 * fetching top 20 molecule names displayed on GM first page
		 */
		List<String> molecules = gm_fpage.getCountry_tables();
		molecules.parallelStream().forEach(Logger.getLogger(this.getClass())::info);
		/**
		 * Iterating through each molecule and enter molecule drill down
		 */
		for (String molecule : molecules) {
			gm_fpage.node_drillDown(molecule);
			Thread.sleep(65000);
			drillPages(menu, molecule);
		}
	}

	public void drillPages(String menu, String molecule) throws InterruptedException {

		// Thread.sleep(1000);
		driver.manage().timeouts().pageLoadTimeout(290, TimeUnit.MINUTES);
		driver.manage().timeouts().implicitlyWait(290, TimeUnit.MINUTES);
		/**
		 * waiting for GM molecule page to load
		 */
		Performance_drill_page drilldownPage = PageFactory.initElements(driver, Performance_drill_page.class);
//		Assert.assertTrue(drilldownPage.getPage_title().getText().equalsIgnoreCase(molecule),
//				"Drill down page is *NOT* same");
//		menu="Performance";
		String menu_page = menu.concat(" Page 2");
		csvSaveFile(menu_page + "_" + molecule, drilldownPage.valueData());
		Thread.sleep(1000);
		drilldownPage.getBack(menu);
		Thread.sleep(78000);
	}
	// @Test(timeOut = 290000)
	public void csvSaveFile(String menu, List<String> dataRows) throws InterruptedException {
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



	@AfterClass
	public void AfterRun() throws Exception {

		// driver.quit();
		Thread.sleep(1000);

		driver.quit();
		Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
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
