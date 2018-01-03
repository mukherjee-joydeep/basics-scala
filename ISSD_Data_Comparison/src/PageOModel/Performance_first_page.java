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
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Objects;

/**
 * @author JMukherjee
 *
 */
public class Performance_first_page {

	WebDriver driver;

	public Performance_first_page(WebDriver pdriver) {
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
	 * capture all the data rows available
	 */

	@FindAll({ @FindBy(xpath = ".//div[contains(@class,'global-perf-table-container')]") })

	List<WebElement> data_Tables;

	public List<String> valueData() {

		List<String> tempData = new ArrayList<String>();

		for (WebElement dataTable : data_Tables) {
			StringBuilder sbData = new StringBuilder();

			/**
			 * capturing data headers' values
			 */
			List<WebElement> dataHeads = dataTable.findElements(By.xpath(".//thead//td"));
			dataHeads.stream()
				.map(e -> e.getText())
				.filter(e -> (!Objects.equal(e, null)))
				.filter(e -> (!e.isEmpty()))
				.filter(e -> (!Objects.equal(e, "")))
				.map(e -> e.concat(","))
				.forEach(sbData::append);

			List<WebElement> dataRows = dataTable
					.findElements(By.xpath(".//tr[contains(@class,'parent-id-') and not(contains(@class,'blank'))]"));

			for (WebElement dataRow : dataRows) {
				/**
				 * capturing each data cells and their values
				 */
				List<WebElement> dataCells = dataRow.findElements(By.tagName("td"));

				for (WebElement dataCell : dataCells) {
					if (dataCell.getAttribute("class").contains("multi-chart-col")) {
						WebElement multi_chartData = dataCell
								.findElement(By.tagName("div"));
						if (multi_chartData.getAttribute("class").contains("bar-chart")) {
							sbData.append(multi_chartData.getAttribute("data-value"));
						} 
					} else if (dataCell.getAttribute("class").contains("col")) {
						sbData.append(dataCell.getText());
					}
					sbData.append(",");
				}
				sbData.append("\n");
			}
			/**
			 * add each row data to List of String - tempData
			 */
			tempData.add(sbData.toString());
		}
		return tempData;

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
	 * Each Country data is available as table in Performance first page
	 */
	@FindAll({ @FindBy(xpath = ".//div[contains(@class,'global-perf-table-container')]/table[@class='global-perf-table']") })

	List<WebElement> countryTable;

	/**
	 * Country tables root of the Performance first page
	 */

	@FindBy(xpath = ".//div[@id='widget-Performance-view']")

	WebElement cperfRoot;

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

		// System.out.println("Filter ID : " + filterID_mapped);
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
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public WebElement getPage_title() {
		return page_title;
	}

	public void node_drillDown(String countryName) throws InterruptedException {
		//Actions act = new Actions(driver);
		Thread.sleep(10000);

		countryTable.stream()
				.filter(e -> {
					WebElement temp=e.findElement(By.xpath(".//div[@class='table-top-header']/div[@class='country']"));
					String elemName=temp.getText();
					return elemName.equalsIgnoreCase(countryName);
				})						
				.forEach(e-> e.click());
		Thread.sleep(6000);

	}

	/**
	 * Each Country data is available as table in Performance first page
	 */
	@FindAll({ @FindBy(xpath = ".//div[@class='table-top-header']/div[@class='country']") })

	List<WebElement> countryTable_country;
	
	public List<String> getCountry_tables() {
		List<String> nodeNames = countryTable_country.stream()
				.map(e->e.getText())
				.collect(Collectors.toList());

		return nodeNames;
	}
//	/**
//	 * capture page breadCrumbHolder
//	 */
//
//	@FindBy(xpath = ".//div[@class='breadCrumbHolder']")
//	@CacheLookup
//	WebElement page_breadCrumb;
//
//	public void getBack(String menu) {
//		page_breadCrumb.findElement(By.partialLinkText(menu)).click();
//	}
	
}
