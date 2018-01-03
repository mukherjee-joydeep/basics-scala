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
public class DC_Page_BU_DEV {

	WebDriver driver;

	public DC_Page_BU_DEV(WebDriver pdriver) {
		this.driver = pdriver;

	}

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Release Test")
	@CacheLookup
	WebElement rTest_link;

	@FindBy(how = How.LINK_TEXT, using = "Test")
	@CacheLookup
	WebElement aTest_link;

	public void aTestlink_check() {				
		aTest_link.click();
	}
	
	public void rTestlink_check() {				
		rTest_link.click();
	}
}
