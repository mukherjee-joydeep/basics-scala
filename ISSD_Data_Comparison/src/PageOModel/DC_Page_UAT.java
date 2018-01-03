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
public class DC_Page_UAT {

	WebDriver driver;

	public DC_Page_UAT(WebDriver pdriver) {
		this.driver = pdriver;

	}


	@FindBy(how = How.LINK_TEXT, using = "International Dashboard")
	@CacheLookup
	WebElement dw_link;

	

	public void link_check() {				
		dw_link.click();
	}
}
