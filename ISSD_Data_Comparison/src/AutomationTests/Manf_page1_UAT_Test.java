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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import PageOModel.BrowserFactory;
import PageOModel.DC_Page_UAT;
import PageOModel.GM_First_Page;
import PageOModel.GM_Molecule_drill_Page;
import PageOModel.Login_Page_UAT;
import PageOModel.Menu_Page;
import commonLibs.automation.excelFileUpdate;
import commonLibs.automation.userAutomationLibrary;

/**
 * @author JMukherjee
 *
 */
public class Manf_page1_UAT_Test extends AutomationTest_ISSD {

	private WebDriver driver;
	String page1, page2;
	String menuitem;
	String menuPage;
	String[] filterControls;
	private String sheetName;
	static String [] modulePages;
	@BeforeTest
	public void beforeSetUp() throws Exception {

	}

	public WebDriver getDriver() {
		return driver;
	}

	@DataProvider(name = "menu_name")
	public static Object[] moduleNames() {
		return modulePages;
//		new Object[] { "Rx Brands","Generic Molecules","Generic Suppliers" };// "OTC Categories","Generic Suppliers", "Generic Molecules"
	}

	@Parameters({ "browser", "ATurl" })
	@Test(priority = 1)
	public void OpenAutomationTest_env(String browser, String url) throws Exception {

		this.driver = BrowserFactory.startBrowser(browser, url);
		Thread.sleep(1000);
	}

	@Parameters({ "username1", "password1","sheetName","modulePages" })
	@Test(priority = 2, timeOut = 290000, dependsOnMethods = { "OpenAutomationTest_env" })
	public void login(String username, String password,String sheetName,String modulePages) {
		this.sheetName=sheetName;
		this.modulePages=modulePages.split(",");
		try {
			Login_Page_UAT login_page = PageFactory.initElements(driver, Login_Page_UAT.class);
			new userAutomationLibrary().captureScreen(driver, "UATTest", "PASS", "OpenURL");
			login_page.login_check(username, password);
			 Thread.sleep(38000);
		} catch (Exception e) {
			new userAutomationLibrary().captureScreen(driver, "UATTest", "FAIL", "OpenURL");
		}
		
		DC_Page_UAT dc_page=PageFactory.initElements(driver, DC_Page_UAT.class);
		 dc_page.link_check();
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='MSOPageViewerWebPart_WebPartWPQ2']")));
		 try {
			Thread.sleep(38000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 
	}

	@Test(priority = 3, timeOut = 490000000, dependsOnMethods = { "login" }, dataProvider = "menu_name")
	public void menuSelect(String menu) throws InterruptedException  {
		this.menuitem = menu;
		this.menuPage = this.menuitem.concat(" Page 1");
		try {
			driver.manage().timeouts().pageLoadTimeout(290, TimeUnit.MINUTES);
			driver.manage().timeouts().implicitlyWait(290, TimeUnit.MINUTES);
			new userAutomationLibrary().captureScreen(driver, "UATTest", "PASS", "First page");
		} catch (Exception e) {
			new userAutomationLibrary().captureScreen(driver, "UATTest", "FAIL", "First page");
		}
		
		/**
		 * Menu selection code starts here
		 */
		Menu_Page menu_page = PageFactory.initElements(driver, Menu_Page.class);
		// menu_page.menu_click("Performance");
		menu_page.manf_menu_click(menu);
		Thread.sleep(45000);

		Assert.assertTrue(menu_page.getPage_title().getText().contains(menu),
				"clicked menu and page came are *NOT* same");

		/**
		 * Menu selection code ends here
		 */
		
		try{
			GMdrillDownPages(menu);
		}catch(org.openqa.selenium.UnhandledAlertException e){			
			org.apache.log4j.Logger
			.getLogger(this.getClass().toGenericString())
			.info("Inside catch of Alert exception");
			driver.switchTo().alert().accept();			
			Thread.sleep(30000);
			GMdrillDownPages(menu);
		}
	}
	// @Test(priority = 6, dependsOnMethods = { "GMdrillDownPages" },
	// dataProvider = "menu_name", timeOut = 290000)
	public void csvSaveFile(String menu, List<String> dataRows)  {
		/**
		 * Saving page to CSV file
		 */
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		StringBuilder sb1 = new StringBuilder();
		dataRows.stream().forEach(sb1::append);
		new userAutomationLibrary().csvFileWrite("UATTest_" + menu, sb1);
	}

	public void csvSaveFile(String menu) throws InterruptedException {
		/**
		 * Saving page to CSV file
		 */
		Thread.sleep(100);
		String page1 = driver.findElement(By.tagName("body")).getText();
		StringBuilder sb1 = new StringBuilder(page1);
		new userAutomationLibrary().csvFileWrite("UATTest_" + menu, sb1);
	}

	// @Test(priority = 5, dependsOnMethods = { "menuSelect" }, timeOut =
	// 290000)
	public void GMdrillDownPages(String menu) throws InterruptedException  {
		
		driver.manage().timeouts().pageLoadTimeout(290, TimeUnit.MINUTES);
		driver.manage().timeouts().implicitlyWait(290, TimeUnit.MINUTES);

		/**
		 * Invoking GM first page for further GM molecule drill down
		 */
		GM_First_Page gm_fpage = PageFactory.initElements(driver, GM_First_Page.class);
		Assert.assertTrue(gm_fpage.getPage_title().getText().contains(menu), "First page is *NOT* same");

		gm_fpage.openOptions();
		Thread.sleep(500);

		String menu_page = menu.concat(" Page 1");

		/**
		 * filter selection starts here
		 */
		HashMap<String, Object[]> pageFilters = new excelFileUpdate().getExcelData(this.sheetName);
		if (pageFilters.containsKey("Filters")) {
			this.filterControls = (String[]) pageFilters.get("Filters");
		}
		Set<String> filter_page = pageFilters.keySet();
		String matchKey = null;
		for (String temp : filter_page) {
			if (temp.trim().equalsIgnoreCase(menu_page)) {
				matchKey = temp;
			}
		}
		if (matchKey != null) {
			String[] filterNames = (String[]) pageFilters.get(matchKey);
			for (int i = 0; i < filterNames.length; i++)
				try {
					{
						Thread.sleep(100);
						gm_fpage.selectFilters(filterControls[i], filterNames[i]);
						Thread.sleep(500);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			/**
			 * filter selection ends here
			 */
			Assert.assertTrue(gm_fpage.updateButton.isEnabled(), "Filters are *NOT* applied");
			gm_fpage.updateFilters();
			Thread.sleep(45000);
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

		molecules.parallelStream().forEach(org.apache.log4j.Logger.getLogger(this.getClass().toGenericString())::info);

		/**
		 * Iterating through each molecule and enter molecule drill down
		 */

		for (String molecule : molecules) {			
			gm_fpage.Molecule_drillDown(molecule);
			Thread.sleep(65000);
			drillPages(menu, molecule);			
		}		
	
	}

	public void drillPages(String menu, String molecule) throws InterruptedException  {
		driver.manage().timeouts().pageLoadTimeout(290, TimeUnit.MINUTES);
		driver.manage().timeouts().implicitlyWait(290, TimeUnit.MINUTES);
		/**
		 * waiting for GM molecule page to load
		 */
		GM_Molecule_drill_Page drilldownPage = PageFactory.initElements(driver, GM_Molecule_drill_Page.class);
		Assert.assertTrue(drilldownPage.getPage_title().getText().equalsIgnoreCase(molecule),
				"Drill down page is *NOT* same");
		
		String menu_page = menu.concat(" Page 2");
		csvSaveFile(menu_page + "_" + molecule, drilldownPage.valueData());
		Thread.sleep(1000);
		drilldownPage.getBack(menu);
		Thread.sleep(78000);
	}
	
	@AfterClass
	public void AfterRun() throws Exception {
		Thread.sleep(1000);
		driver.quit();
	//	 Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
		// Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
	}
}
