/**
 * 
 */
package PageOModel;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

/**
 * @author JMukherjee
 *
 */
public class filterControls {

//	WebDriver driver1;
	
	WebElement driver;

	public filterControls(WebElement pdriver) {
		this.driver= pdriver;
		
//		this.rootElement=rootElement;

	}

	/**
	 * capture filterName
	 */

//	@FindBy(xpath = ".//span[@class='FilterLabel']")
	
	WebElement filterName;

	/**
	 * capture filter item container
	 */

	//@FindBy(xpath = ".//ul[@class='dropdown-filter-items-container']")
	
	WebElement itemContainer;

	/**
	 * capture all list of elements of items of that filter available
	 */
	@FindAll({ @FindBy(tagName = "li") })
	
	List<WebElement> itemsFilter;

	/**
	 * capture all list of elements of items with values of that filter
	 * available
	 */
//	@FindAll({ @FindBy(xpath = ".//span[contains(@class,'fn-title')]") }) 
//	
//	List<WebElement> itemsValue;

	/**
	 * capture selected item in drop down
	 */

	@FindBy(xpath = ".//span[contains(@class,'selected') and (@data-changeonsubmit='True')]")
	
	WebElement itemSelected;

	/**
	 * capture open drop down caret
	 */

//	@FindBy(xpath = ".//button[@type='button']")
//	
//	WebElement buttonCaret;

	List<WebElement> itemsValue;
	/**
	 * select the filter with the selectionName
	 * @param buttonCaret
	 *            the buttonCaret to set
	 * @throws InterruptedException 
	 */
	public void selectFilter(String selectionName) throws InterruptedException {
		if(selectionName.equalsIgnoreCase("-"))
			return;
		driver.findElement(By.xpath(".//button[@type='button']")).click();
		Thread.sleep(1900);
		List<WebElement> itemCheckbox = null;
		itemContainer=driver.findElement(By.xpath(".//ul[(@class='dropdown-filter-items-container') or (@class='multi-select-filter-items-container')]"));
		if(itemContainer.getAttribute("class").contains("multi-select"))
			itemCheckbox=itemContainer.findElements(By.xpath(".//input[@type='checkbox']"));
		itemsValue=itemContainer.findElements(By.xpath(".//span[contains(@class,'fn-title')]"));
		for (WebElement temp : itemsValue) {
//			Actions act = new Actions(driver);
//			act.moveToElement(temp);
			
			if(temp.getText().equalsIgnoreCase(selectionName)){
				if(itemContainer.getAttribute("class").contains("multi-select"))
				{
					for(WebElement tempCheck:itemCheckbox){
						if(tempCheck.getAttribute("data-text").equalsIgnoreCase(selectionName)){
//							if(!tempCheck.isSelected())
								tempCheck.click();
						}
					}
				}else
					temp.click();
				break;
			}
		}
	}

	/**
	 * Name of the filter
	 * @return
	 */
	public String getFilterName() {
		filterName=driver.findElement(By.xpath(".//span[@class='FilterLabel']"));
		return filterName.getText();
	}

	public WebElement getItemContainer() {
		return itemContainer;
	}

	public List<WebElement> getItemsFilter() {
		return itemsFilter;
	}

	/**
	 * List of filter values
	 * @return List of string
	 */
	public List<String> getItemsValue() {
		return itemsValue.stream()
				.map(e->e.getText())
				.collect(Collectors.toList());
	}

	/**
	 * the item selected currently in the filter
	 * @return
	 */
	public WebElement getItemSelected() {
		return itemSelected;
	}

}
