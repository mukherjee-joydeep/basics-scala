/**
 * 
 */
package PageOModel;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

/**
 * @author JMukherjee
 *
 */
public class ButtonfilterControls {

	// WebDriver driver1;

	WebElement driver;

	public ButtonfilterControls(WebElement pdriver) {
		this.driver = pdriver;
	}

	/**
	 * capture filterName
	 */

//	@FindBy(xpath = ".//span[@class='btnFilterTitle']")

	WebElement filterName;
//	=driver.findElement(By.xpath(".//span[@class='btnFilterTitle ']"));

	/**
	 * capture filter item container
	 */

//	@FindBy(xpath = ".//div[contains(@class,'btn-filter-items') and starts-with(@id,'filter-') and ends-with(@id,'-control')]")

	WebElement itemContainer;
//	=driver.findElement(By.xpath(".//div[contains(@class,'btn-filter-items')]"));

	/**
	 * capture all list of elements of items with values of that filter available
	 */
	// @FindAll({ @FindBy(xpath = ".//span[contains(@class,'fn-title')]") })

	List<WebElement> itemsValue ;
//	= itemContainer.findElements(By.xpath(".//div[contains(@class,'btnFilter')]"));

	/**
	 * capture selected item in filter
	 */

//	@FindBy(xpath = ".//span[contains(@class,'selected-filter') and contains(@class,'btnFilter')]")

	WebElement itemSelected;
//	=itemContainer.findElement(By.xpath(".//span[contains(@class,'selected-filter') and contains(@class,'btnFilter')]"));

	/**
	 * select the filter with the selectionName
	 * 
	 * @param selectionName
	 * @throws InterruptedException
	 */
	public void selectFilter(String selectionName) throws InterruptedException {
		if (selectionName.equalsIgnoreCase("-"))
			return;
		WebElement itemContainer=driver.findElement(By.xpath(".//div[contains(@class,'btn-filter-items')]"));
		List<WebElement> itemsValue = itemContainer.findElements(By.xpath(".//div[contains(@class,'btnFilter')]"));
		for (WebElement temp : itemsValue) {
			// Actions act = new Actions(driver);
			// act.moveToElement(temp);			
			if (temp.getAttribute("data-text").equalsIgnoreCase(selectionName)) {
				temp.click();
				break;
			}
		}
	}

	/**
	 * Name of the filter
	 * 
	 * @return
	 */
	public String getFilterName() {
		return filterName.getText();
	}

	public WebElement getItemContainer() {
		return itemContainer;
	}

	/**
	 * List of filter values
	 * 
	 * @return List of string
	 */
	public List<String> getItemsValue() {
		return itemsValue.stream().map(e -> e.getText()).collect(Collectors.toList());
	}

	/**
	 * the item selected currently in the filter
	 * 
	 * @return
	 */
	public WebElement getItemSelected() {
		return itemSelected;
	}

}
