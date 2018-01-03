/**
 * 
 */
package PageOModel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 * @author JMukherjee
 *
 */
public class Login_Page_UAT {

	WebDriver driver;

	public Login_Page_UAT(WebDriver pdriver) {
		this.driver = pdriver;

	}

	@FindBy(id = "username")
	@CacheLookup
	WebElement username;

	@FindBy(how = How.ID, using = "password")
	@CacheLookup
	WebElement password;

	@FindBy(id= "SubmitCreds")
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
//		Actions act=new Actions(driver);
		username.sendKeys(uid);
		password.sendKeys(pass);		
//		act.sendKeys(Keys.TAB);
//		act.sendKeys(Keys.TAB);
//		act.sendKeys(Keys.RETURN);
//		act.sendKeys(Keys.ENTER);
		
//		act.moveToElement(submit_button).build().perform();
//		act.click(submit_button).build().perform();
//		submit_button.sendKeys(Keys.ENTER);
		//JavascriptExecutor js = (JavascriptExecutor)driver;				
		//js.executeScript("arguments[0].click();",submit_button);//clkLgn()");
		//js.executeScript("clkLgn()");
		submit_button.click();		
	}
}
