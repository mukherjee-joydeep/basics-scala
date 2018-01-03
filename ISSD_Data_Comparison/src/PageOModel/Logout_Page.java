/**
 * 
 */
package PageOModel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

/**
 * @author JMukherjee
 *
 */
public class Logout_Page {

	WebDriver driver;

	public Logout_Page(WebDriver pdriver) {
		this.driver = pdriver;

	}


	@FindAll(value = { @FindBy(className="logout-link") })
	@CacheLookup
	WebElement [] logout_span_tag;

	

	/**
	 * @throws InterruptedException 
	 * 
	 */

	public void logout_check() throws InterruptedException {		
		for(WebElement temp:logout_span_tag)
			System.out.println(temp.getText());
		logout_span_tag[0].findElement(By.tagName("span")).click();
		Thread.sleep(2000);
		driver.switchTo().alert().accept();
	}
}
