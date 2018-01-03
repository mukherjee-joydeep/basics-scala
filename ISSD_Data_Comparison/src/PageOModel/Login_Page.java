/**
 * 
 */
package PageOModel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import commonLibs.automation.userAutomationLibrary;

/**
 * @author JMukherjee
 *
 */
public class Login_Page {

	WebDriver driver;

	public Login_Page(WebDriver pdriver) {
		this.driver = pdriver;

	}

	@FindBy(id = "txtUserName")
	@CacheLookup
	WebElement username;
	
	@FindBy(how = How.ID, using = "Password")
	@CacheLookup
	WebElement password;

	@FindBy(how = How.ID, using = "Submit1")
	@CacheLookup
	WebElement submit_button;

	/**
	 * @FindBy(how=How.XPATH,using=".//*[@id='wp-submit']") @CacheLookup
	 *                                                      WebElement
	 *                                                      submit_button;
	 * 
	 * @FindBy(how=How.LINK_TEXT,using="Lost your password?") @CacheLookup
	 *                                       WebElement forget_password_link;
	 */

	public void login_check(String uid, String pass) {
		Actions act=new Actions(driver);
		try{
			
			act.sendKeys(username,uid).build().perform();
			act.sendKeys(password,pass).build().perform();
//		username.sendKeys(uid);
//		password.sendKeys(pass);
		new userAutomationLibrary().captureScreen(driver, "Before Login", "PASS", "Typed Login");
		submit_button.click();
		}catch(Exception e){
			new userAutomationLibrary().captureScreen(driver, "Before Login", "FAIL", "Typed Login");
		}
	}
}
