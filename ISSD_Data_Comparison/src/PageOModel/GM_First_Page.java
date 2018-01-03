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
public class GM_First_Page {

	WebDriver driver;

	public GM_First_Page(WebDriver pdriver) {
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
	@FindAll({ @FindBy(xpath = ".//div[(@class='dropdown-filter-items') or (@class='multiselect-filter-items')]") })

	List<WebElement> filters;

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
			dataHeads.stream().map(e -> e.getText()).filter(e -> (!Objects.equal(e, null))).filter(e -> (!e.isEmpty()))
					.filter(e -> (!Objects.equal(e, ""))).map(e -> e.concat(",")).forEach(sbData::append);
			/**
			 * capturing each data cells and their values
			 */
			List<WebElement> dataCells = dataRow.findElements(By.tagName("td"));

			for (WebElement dataCell : dataCells) {
				if (dataCell.getAttribute("class").contains("multi-chart-col")) {
					WebElement multi_chartData = dataCell.findElement(By.xpath(
							".//*[contains(@class,'bar-chart') or contains(@class,'hsb') or contains(@class,'sparkline-bar')]"));
					if (multi_chartData.getAttribute("class").contains("bar-chart")) {
						sbData.append(multi_chartData.getAttribute("data-value"));
						// if (isAttribtuePresent(multi_chartData,"tooltip"))
						sbData.append("," + multi_chartData.getAttribute("tooltip"));
					} else if (multi_chartData.getAttribute("class").contains("sparkline-bar"))
						sbData.append(multi_chartData.getAttribute("data-absvalue"));
					else if (multi_chartData.getAttribute("class").contains("hsb"))
						sbData.append(multi_chartData.getAttribute("data-absvalue"));
				} else if (dataCell.getAttribute("class").contains("col")) {
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
		return filters.stream().map(e -> e.getAttribute("id")).collect(Collectors.toList());
	}

	/**
	 * capture Update button in filter box
	 * 
	 */
	@FindBy(xpath = ".//input[(@type='button')and(@value='Update')]")
	public WebElement updateButton;

	/**
	 * capture Defaults button in filter box
	 * 
	 */
	@FindBy(xpath = ".//input[(@type='button')and(@value='Defaults')]")

	WebElement defaultsButton;

	/**
	 * Each Molecules which can lead to GM Molecule drill down pages
	 */
	@FindAll({ @FindBy(xpath = "//th[contains(@class,'first')]//div[contains(@class,'clickable-label')]") })

	List<WebElement> GM_molecules;

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
	 * capture all the filters available
	 */
	@FindAll({
			@FindBy(xpath = ".//div[contains(@class,'btnFilterContainer') and contains(@id,'button-filter-container-')]") })

	List<WebElement> btnfilters;

	public void selectFilters(String filterID, String selectionName) throws InterruptedException {
		String filterID_mapped = null;
		boolean volumeClicked=false;
		if (filterID.startsWith("btn")) {
			filterID_mapped = filterID.substring(3);
			for (WebElement temp : btnfilters) {				
				if (!((Objects.equal(temp.getAttribute("textContent"), ""))
						|| (Objects.equal(temp.getAttribute("textContent"), null))
						|| (temp.getAttribute("textContent").isEmpty()))) {
					if (temp.findElement(By.xpath(".//span[@class='btnFilterTitle ']")).getAttribute("textContent")
							.equalsIgnoreCase(filterID_mapped+":")) {	
						
						ButtonfilterControls filterAbove = new ButtonfilterControls(temp);
						filterAbove.selectFilter(selectionName);
						if(selectionName.equalsIgnoreCase("Volume")){
							volumeClicked=true;
							Thread.sleep(9800);
							this.openOptions();
							Thread.sleep(500);
						}
						else
							volumeClicked=false;
						
						break;
					}
				}
			}
		} else {
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
			case "CountrySet":
				filterID_mapped = "filter-CountrySet-control";
				break;
			}
			// try{
			// final Predicate<? super WebElement> valueNotNullOrEmpty
			// = e->((WebElement) e).getAttribute("id").equalsIgnoreCase(filterID_mapped);
			// filters.stream()
			// .filter(valueNotNullOrEmpty)
			// .map(e->new filterControls(e))
			// .forEach(e->{
			// try {
			// e.selectFilter(selectionName);
			// } catch (InterruptedException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// });
			// }catch(Exception e){
			// e.printStackTrace();
			// }
			filters=driver.findElements(By.xpath(".//div[(@class='dropdown-filter-items') or (@class='multiselect-filter-items')]"));
			for (WebElement temp : filters) {
				if (temp.getAttribute("id").equalsIgnoreCase(filterID_mapped)) {
					filterControls filterAbove = new filterControls(temp);
					filterAbove.selectFilter(selectionName);
					break;
				}
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

	public void Molecule_drillDown(String moleculeName) throws InterruptedException {
		// Actions act = new Actions(driver);
		// Thread.sleep(10000);

		WebElement molecule = GM_molecules.stream().filter(e -> (e.getText().equalsIgnoreCase(moleculeName)))
				.findFirst().orElse(null);
		org.apache.log4j.Logger.getLogger(this.getClass().toGenericString()).info("Clicking on : " + moleculeName);
		if (molecule != null) {
			try {
				molecule.click();
			} catch (org.openqa.selenium.ElementNotVisibleException e) {
				this.openExpanders();
				Thread.sleep(500);
				molecule.click();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (molecule == null) {
			try {
				org.apache.log4j.Logger.getLogger(this.getClass().toGenericString())
						.info("Inside try on : " + moleculeName);
				driver.findElement(
						By.xpath(".//div[contains(@class,'clickable-label') " + "and @title='" + moleculeName + "']"))
						.click();
			} catch (org.openqa.selenium.ElementNotVisibleException e) {
				org.apache.log4j.Logger.getLogger(this.getClass().toGenericString())
						.info("Inside catch on : " + moleculeName);
				this.openExpanders();
				Thread.sleep(500);
				driver.findElement(
						By.xpath(".//div[contains(@class,'clickable-label') " + "and @title='" + moleculeName + "']"))
						.click();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Thread.sleep(6000);

		// for (WebElement molecule : GM_molecules) {
		// if (molecule.getText().equalsIgnoreCase(moleculeName)) {
		//// act.click(molecule).build().perform();
		// Thread.sleep(6000);
		// break;
		// }
		// }
	}

	public List<String> getGM_molecules() {
		List<String> moleculeNames = GM_molecules.stream().map(e -> e.getText()).filter(e -> (!Objects.equal(e, null)))
				.filter(e -> (!e.isEmpty())).filter(e -> (!Objects.equal(e, ""))).distinct()
				.collect(Collectors.toList());
		// new ArrayList<>();
		// for (WebElement molecule : GM_molecules) {
		// moleculeNames.add(molecule.getText());
		// }
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
			e.printStackTrace();
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
	private boolean isElementPresent(WebElement driver, By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
	}
}
