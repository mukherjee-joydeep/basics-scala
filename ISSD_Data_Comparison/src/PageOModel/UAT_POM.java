/**
 * 
 */
package PageOModel;

import java.util.List;
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

/**
 * @author JMukherjee
 *
 */
public class UAT_POM {

	WebDriver driver1, driver2;
	String page1, page2;

	@BeforeTest
	public void beforeSetUp() throws Exception {

	}	

	@DataProvider(name = "menu_name")
	public static Object[] moduleNames() {
		return new Object[] { "Generic Molecules", "Generic Suppliers", "Rx Brands", "Performance", "OTC Categories" };
	}

	@Parameters({ "browser", "url" })
	@Test(priority = 1)
	public void OpenReleaseTest_env(String browser, String url) throws Exception {

		this.driver2 = BrowserFactory.startBrowser(browser, url);
		Thread.sleep(1000);
	}

	@Parameters({ "username", "password" })
	@Test(priority = 2)
	public void login_UAT(String username, String password) throws Exception {
		Login_Page_UAT login_page = PageFactory.initElements(driver2, Login_Page_UAT.class);
		login_page.login_check(username, password);
		Thread.sleep(18000);
	}

	@Test(priority = 3)
	public void dcPage_UAT() throws Exception {
		driver2.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver2.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		DC_Page_UAT dc_page = PageFactory.initElements(driver2, DC_Page_UAT.class);
		dc_page.link_check();
		Thread.sleep(30000);
		driver2.switchTo().frame(driver2.findElement(By.xpath("//iframe[@id='MSOPageViewerWebPart_WebPartWPQ2']")));
	}

	// @Parameters({"menu"})
	@Test(priority = 4, dataProvider = "menu_name")
	public void menuSelect(String menu) throws Exception {

		driver2.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		driver2.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		Menu_Page menu_page = PageFactory.initElements(driver2, Menu_Page.class);
		// menu_page.menu_click("Performance");

		menu_page.manf_menu_click(menu);
		Thread.sleep(48000);
		
		Assert.assertTrue(menu_page.getPage_title().getText().contains(menu), "clicked menu and page came are *NOT* same");
		
		// Logout_Page logout_page = PageFactory.initElements(driver,
		// Logout_Page.class);
		// logout_page.logout_check();

	}

	@Parameters({ "menu" })
	@Test(priority = 5)
	public void csvSaveFile(String menu) throws Exception {
		/**
		 * Saving page to CSV file
		 */
		Thread.sleep(18000);
		String page1 = driver2.findElement(By.tagName("body")).getText();
		commonLibs.automation.userAutomationLibrary ual = new commonLibs.automation.userAutomationLibrary();
		StringBuilder sb1 = new StringBuilder(page1);
		ual.csvFileWrite("ReleaseTest_" + menu, sb1);
	}

	@Parameters({ "menu" })
	@Test(priority = 6)
	public void drillDownPages(String menu) throws Exception {
		driver2.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
		driver2.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		/**
		 * Invoking GM first page for further GM molecule drill down
		 */
		GM_First_Page gm_fpage = PageFactory.initElements(driver2, GM_First_Page.class);
		
		Assert.assertTrue(gm_fpage.getPage_title().getText().contains(menu), "First page is *NOT* same");
		
		/**
		 * fetching top 20 molecule names displayed on GM first page
		 */
		List<String> molecules = gm_fpage.getGM_molecules();

		/**
		 * Iterating through each molecule and enter molecule drill down
		 */
		for (String molecule : molecules) {
			
			gm_fpage = PageFactory.initElements(driver2, GM_First_Page.class);
			/**
			 * waiting for GM molecule page to load
			 */
			Assert.assertTrue(gm_fpage.getPage_title().getText().contains(menu), "First page is *NOT* same");
			gm_fpage.Molecule_drillDown(molecule);

			Thread.sleep(50000);
			
			GM_Molecule_drill_Page drilldownPage=PageFactory.initElements(driver2, GM_Molecule_drill_Page.class);
			Assert.assertTrue(drilldownPage.getPage_title().getText().contains(molecule), "Drill down page is *NOT* same");
			csvSaveFile(menu + molecule);

			driver2.navigate().back();
			Thread.sleep(50000);
		}
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

		// Assert.assertEquals(page1, page2);
		// if(page1.equalsIgnoreCase(page2)){
		// System.out.println("Matching pages");
		// }
		// else
		// System.out.println("Not matching");
		//

		// driver.quit();
		Thread.sleep(1000);
		Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
	}
}
