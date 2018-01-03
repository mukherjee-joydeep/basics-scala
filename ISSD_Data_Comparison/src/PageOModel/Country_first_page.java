/**
 * 
 */
package PageOModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

/**
 * @author JMukherjee
 *
 */
public class Country_first_page {

	WebDriver driver;

	public Country_first_page(WebDriver pdriver) {
		this.driver = pdriver;

	}

	/**
	 * capture page title
	 */

	@FindBy(xpath = ".//span[contains(@class,'nav-title')]")

	WebElement page_title;

	/**
	 * capture page filter options button
	 */

	@FindBy(xpath = ".//span[@id='toggle-panel-2']")

	WebElement optionsButton;

	/**
	 * capture filter table box
	 * 
	 */
	@FindBy(xpath = ".//table[@class='filter-container-layout-table']")

	WebElement filterTable;

	/**
	 * capture all the filters available
	 */
	@FindAll({ @FindBy(xpath = ".//div[@class='dropdown-filter-items']") })

	List<WebElement> filters;
	
	/**
	 * capture all the expanders available
	 */
	//ExpandCollapseTable.toggle(this)
	@FindAll({ @FindBy(xpath = ".//div[@onclick='ExpandCollapseTable.toggle(this)']") })

	List<WebElement> expanders;
	
	public void openExpanders() {
		expanders.stream().forEach(e->e.click());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * @return the filters
	 */
	public List<WebElement> getFilters() {
		return filters;
	}

	/**
	 * @return the filters
	 */
	public List<String> getFiltersID() {
		return filters.stream().map(e -> e.getAttribute("id")).collect(Collectors.toList());
	}

	/**
	 * capture Update button in filter box
	 * 
	 */
	@FindBy(xpath = ".//input[(@type='button')and(@value='Update')]")

	WebElement updateButton;

	/**
	 * capture Defaults button in filter box
	 * 
	 */
	@FindBy(xpath = ".//input[(@type='button')and(@value='Defaults')]")

	WebElement defaultsButton;

	/**
	 * Each Country data is available as table
	 */
	@FindAll({ @FindBy(xpath = ".//table[contains(@class,'region-table-onmap')]") })

	List<WebElement> countryTable;
	
	/**
	 * Bubble chart root of the LoE first page
	 */

	@FindBy(xpath = ".//div[@id='bubbleChart']")
	
	WebElement bchartRoot;
	
	public void openOptions() {
		optionsButton.click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void selectFilters(String filterID, String selectionName) throws InterruptedException {
		String filterID_mapped = null;
		switch (filterID) {
		case "Currency":
			filterID_mapped = "filter-ManufacturerCurrency-control";
			break;
		case "Volume":
			filterID_mapped = "filter-ManufacturerVolume-control";
			break;
		case "ATCSet":
			filterID_mapped = "filter-ATCSet-control";
			break;
		case "Period":
			filterID_mapped = "filter-ManufacturerPeriod-control";
			break;
		case "AcuteChronic":
			filterID_mapped = "filter-AcuteChronic-control";
			break;
		case "PriceLevel":
			filterID_mapped = "filter-ManufacturerPriceLevel-control";
			break;
		case "PrimarySecondary":
			filterID_mapped = "filter-PrimarySecondary-control";
			break;
		case "Comparison":
			filterID_mapped = "filter-ManufacturerComparison-control";
			break;
		case "SpecialtyNonSpecialty":
			filterID_mapped = "filter-SpecialtyNonSpecialty-control";
			break;
		}

//		System.out.println("Filter ID : " + filterID_mapped);
		for (WebElement temp : filters) {
			if (temp.getAttribute("id").equalsIgnoreCase(filterID_mapped)) {//
				filterControls filterAbove = new filterControls(temp);

				// System.out.println("Filter Name is :
				// "+filterAbove.getFilterName());
				filterAbove.selectFilter(selectionName);

				break;
			}
		}

	}

	public void updateFilters() {

		updateButton.click();
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public WebElement getPage_title() {
		return page_title;
	}

	public void node_drillDown(String nodeName) throws InterruptedException {
		Actions act = new Actions(driver);
		Thread.sleep(10000);

		countryTable.stream()
			.filter(e->e.findElement(By.xpath(".//thead/tr[@class='table-header-row']/td/div[not(contains(@class,'second-line'))]")).getText().equalsIgnoreCase(nodeName))
			.forEach(e->e.click());
			
//		for (WebElement node : nodes) {
//			if (node.getText().equalsIgnoreCase(nodeName)) {
//				act.click(node).build().perform();
//				Thread.sleep(6000);
//				break;
//			}
//		}
	}

	public List<String> getCountry_tables() {
		List<String> nodeNames = 
		countryTable.stream()
			.map(e->e.findElement(By.xpath(".//thead/tr[@class='table-header-row']/td/div[not(contains(@class,'second-line'))]")).getText())
			.collect(Collectors.toList());
		
		return nodeNames;
	}
	
	public List<String> valueData() {

		List<String> tempData = new ArrayList<String>();
	
		countryTable.stream()
		.forEach(e->{
			String filterName=e.getText();
			tempData.add(filterName+"\n--------------------------------------------------------------------------------------\n");		
			
		});
		
		
		return tempData;

	}

}
