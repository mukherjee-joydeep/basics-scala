/**
 * 
 */
package PageOModel;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import commonLibs.automation.userAutomationLibrary;

/**
 * @author JMukherjee
 *
 */
public class Menu_Page {

	WebDriver driver;

	public Menu_Page(WebDriver pdriver) {
		this.driver = pdriver;

	}
	
	@FindBy(linkText = "Performance")
	@CacheLookup
	public WebElement perf_menu;

	@FindBy(linkText = "Manufacturers")
	@CacheLookup
	WebElement manf_menu;
	
	@FindBy(linkText = "Country")
	@CacheLookup
	WebElement country_menu;

	@FindAll({ @FindBy(xpath = ".//li[contains(@class,'sub-menu-item') and contains(@root-parent,'Manufacturer') or (@root-parent='OtcWholesalerXCatXCorp') or (@root-parent='LossOfExcelXProdXCorp')]") }) //contains(@class,'has-break') and  
	@CacheLookup
	List<WebElement> manf_menu_items;

	@FindAll({ @FindBy(xpath = ".//li[contains(@class,'sub-menu-item') and contains(@root-parent,'CountryPage')]") }) //contains(@class,'has-break') and  
	@CacheLookup
	List<WebElement> country_menu_items;
	/**
	 * capture page title
	 */

	@FindBy(xpath = ".//span[contains(@class,'nav-title')]")
	@CacheLookup
	WebElement page_title;

	public WebElement getPage_title() {
		return page_title;
	}

	public void manf_menu_click(String menu_name) throws InterruptedException {
		Actions act = new Actions(driver);
		Thread.sleep(2000);

		act.moveToElement(manf_menu).build().perform();

		Thread.sleep(500);

		for (WebElement temp : manf_menu_items) {

			
			if (temp.getText().equalsIgnoreCase(menu_name)) {
				try{
					new userAutomationLibrary().captureScreen(driver, "ReleaseTest", "PASS", "Menu select");
					act.moveToElement(temp).perform();
					Thread.sleep(300);
				act.click(temp).perform();
//				Thread.sleep(1000);
				}catch(Exception e){
					new userAutomationLibrary().captureScreen(driver, "ReleaseTest", "FAIL", "Meun select");	
				}
			}
		}
	}
	
	public void country_menu_click(String menu_name) throws InterruptedException {
		Actions act = new Actions(driver);
		Thread.sleep(2000);

		act.moveToElement(country_menu).build().perform();

		Thread.sleep(500);

		for (WebElement temp : country_menu_items) {

			act.moveToElement(temp).build().perform();
			Thread.sleep(300);
			if (temp.getText().equalsIgnoreCase(menu_name)) {
				try{
					new userAutomationLibrary().captureScreen(driver, "ReleaseTest", "PASS", "Menu select");	
				act.click(temp).build().perform();
				Thread.sleep(1000);
				}catch(Exception e){
					new userAutomationLibrary().captureScreen(driver, "ReleaseTest", "FAIL", "Menu select");	
				}
			}
		}
	}
}
