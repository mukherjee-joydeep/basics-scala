/**
 * 
 */
package PageOModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Objects;

/**
 * @author JMukherjee
 *
 */
public class GM_Molecule_drill_Page {

	WebDriver driver;

	public GM_Molecule_drill_Page(WebDriver pdriver) {
		this.driver = pdriver;

	}

	/**
	 * capture page title
	 */

	@FindBy(xpath = ".//span[contains(@class,'nav-title')]")
	@CacheLookup
	WebElement page_title;

	public WebElement getPage_title() {
		return page_title;
	}

	/**
	 * capture page filter options button
	 */

	@FindBy(xpath = ".//span[@id='toggle-panel-2']")

	WebElement optionsButton;

	/**
	 * Open Options button
	 */
	public void openOptions() {
		optionsButton.click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
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

		// System.out.println("Filter ID : " + filterID_mapped);
		// try{
		// filters.stream()
		// .filter(e->e.getAttribute("id").equalsIgnoreCase(filterID_mapped))
		// .map(e->new filterControls(e))
		// .forEach(filterControls::selectFilter(selectionName));
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		for (WebElement temp : filters) {
			if (temp.getAttribute("id").equalsIgnoreCase(filterID_mapped)) {//
				filterControls filterAbove = new filterControls(temp);				
					filterAbove.selectFilter(selectionName);
				// System.out.println("Filter Name is :
				// "+filterAbove.getFilterName());
				break;
			}
		}

	}

	/**
	 * Update filters button click
	 */
	public void updateFilters() {

		updateButton.click();
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * capture all the expanders available
	 */
	// ExpandCollapseTable.toggle(this)
	@FindAll({ @FindBy(xpath = ".//div[@onclick='ExpandCollapseTable.toggle(this)']") })

	List<WebElement> expanders;

	public void openExpanders() {
		expanders.stream().forEach(e -> e.click());
		try {
			Thread.sleep(500);
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
		return filters.stream()
				.map(e -> e.getAttribute("id"))
				.collect(Collectors.toList());
	}

	/**
	 * capture Update button in filter box
	 * 
	 */
	@FindBy(xpath = ".//input[(@type='button')and(@value='Update')]")
	public	WebElement updateButton;

	/**
	 * capture Defaults button in filter box
	 * 
	 */
	@FindBy(xpath = ".//input[(@type='button')and(@value='Defaults')]")

	WebElement defaultsButton;
	
	/**
	 * capture page breadCrumbHolder
	 */

	@FindBy(xpath = ".//div[@class='breadCrumbHolder']")
	@CacheLookup
	WebElement page_breadCrumb;

	public void getBack(String menu) {
		page_breadCrumb.findElement(By.partialLinkText(menu)).click();
	}
	
	/**
	 * capture all the data rows available
	 */
	// ExpandCollapseTable.toggle(this)
	@FindAll({ @FindBy(xpath = ".//tr[contains(@class,'Row') and not(contains(@class,'blankRow'))]") })

	List<WebElement> data_rows;

	public List<String> valueData() {

		List<String> tempData = new ArrayList<String>();

		for (WebElement dataRow : data_rows) {
			StringBuilder sbData = new StringBuilder();

			/**
			 * capturing data headers' values
			 */
			List<WebElement> dataHeads = dataRow.findElements(By.tagName("th"));
			/**
			 * The block below is for debug purpose only
			 */
//			System.out.println(dataHeads.stream().count());
//			dataHeads.stream()
//					.map(e->e.getAttribute("textContent"))
//					.filter(e->(!Objects.equal(e, null)))
//					.filter(e->(!e.isEmpty()))
//					.filter(e->(!Objects.equal(e, "")))
//					.forEach(System.out::println);
			/**
			 * The above block is for debug purpose only
			 */
			dataHeads.stream()
				.map(e->e.getAttribute("textContent"))
				.filter(e->(!Objects.equal(e, null)))
				.filter(e->(!e.isEmpty()))
				.filter(e->(!Objects.equal(e, "")))	
//				.distinct()
				.map(e->e.concat(","))
				.forEach(sbData::append);
//			for (WebElement dataHead : dataHeads){
//				sbData.append(dataHead.getText());
//				sbData.append(",");
//				}
				
			/**
			 * capturing each data cells and their values
			 */
			List<WebElement> dataCells = dataRow.findElements(By.tagName("td"));
//			dataCells.parallelStream()
//			.filter(e->!e.getAttribute("class").contains("multi-chart-col"))
//			.filter(e->e.getAttribute("class").contains("col"))
//			.map(e->e.getText())
//			.map(e->e.concat(","))
//			.forEach(sbData::append);
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			for (WebElement dataCell : dataCells) {
				if (dataCell.getAttribute("class").contains("multi-chart-col")) {
					try{
						WebElement multi_chartData = dataCell.findElement(By.xpath(
								".//*[contains(@class,'bar-chart') or contains(@class,'hsb') or contains(@class,'sparkline-bar')]"));
						if (multi_chartData.getAttribute("class").contains("bar-chart")) {
							sbData.append(multi_chartData.getAttribute("data-value"));
	//						if (isAttribtuePresent(multi_chartData,"tooltip"))
	//							sbData.append(multi_chartData.getAttribute("tooltip"));
						} else if (multi_chartData.getAttribute("class").contains("sparkline-bar"))
							sbData.append(multi_chartData.getAttribute("data-absvalue"));
						else if (multi_chartData.getAttribute("class").contains("hsb"))
							sbData.append(multi_chartData.getAttribute("data-absvalue"));
					}catch(Exception e){}
				}else if (dataCell.getAttribute("class").contains("both-way-pos-bar-chart-col")) {
					sbData.append(dataCell.findElement(By.xpath(".//div[@class='both-way-pos-bar-chart']/div[@class='left']")).getAttribute("title"));
					sbData.append(","+dataCell.findElement(By.xpath(".//div[@class='both-way-pos-bar-chart']/div[@class='right']")).getAttribute("title"));
				}
				else if (dataCell.getAttribute("class").contains("col")) {
					sbData.append(dataCell.getText());
				}
				sbData.append(",");	
				
			}
			sbData.append("\n");
			/**
			 * add each row data to List of String - tempData
			 */
			tempData.add(sbData.toString());
		}
		return tempData;

	}
	/**
	 * Each Molecules which can lead to GM Molecule drill down pages
	 */
	@FindAll({ @FindBy(xpath = ".//div[contains(@class,'clickable-label')]") })
	@CacheLookup
	List<WebElement> GM_brands;

	public void Brand_drillDown(String moleculeName) throws InterruptedException {
		Actions act = new Actions(driver);	

		for (WebElement brand : GM_brands) {			
			if (brand.getText().equalsIgnoreCase(moleculeName)) {
				act.click(brand).build().perform();
				Thread.sleep(6000);
				break;
			}
		}
	}

	public List<String> getGM_brands() {
		List<String> moleculeNames = GM_brands.stream()
				.map(e -> e.getText())
				.filter(e->(!Objects.equal(e, null)))
				.filter(e->(!e.isEmpty()))
				.filter(e->(!Objects.equal(e, "")))	
				.distinct()
				.collect(Collectors.toList());
		
		return moleculeNames;
	}
	
	private boolean isAttribtuePresent(WebElement element, String attribute) {
		Boolean result = false;
		try {
			String value = element.getAttribute(attribute);
			if (value != null) {
				result = true;
			}
		} catch (Exception e) {
		}

		return result;
	}
	/**
	 * checking whether the element is present in the page
	 * 
	 * @param driver
	 * @param by
	 * @return
	 */
	private boolean isElementPresent(WebElement driver,By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
	}
}
