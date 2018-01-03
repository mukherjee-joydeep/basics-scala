/**
 * 
 */
package PageOModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class LoE_first_page {

	WebDriver driver;

	public LoE_first_page(WebDriver pdriver) {
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

	public WebElement updateButton;

	/**
	 * capture Defaults button in filter box
	 * 
	 */
	@FindBy(xpath = ".//input[(@type='button')and(@value='Defaults')]")

	WebElement defaultsButton;

	/**
	 * Each LoE circles called as node
	 */
	@FindAll({ @FindBy(xpath = "//*[name()='svg']/*[name()='g' and contains(@class,'node')]") })

	List<WebElement> nodes;
	
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
		case "CountrySet":
			filterID_mapped = "filter-CountrySet-control";
			break;	
		}

//		System.out.println("Filter ID : " + filterID_mapped);
		for (WebElement temp : filters) {
			if (temp.getAttribute("id").equalsIgnoreCase(filterID_mapped)) {
				filterControls filterAbove = new filterControls(temp);

				if(selectionName.contains("&")){
					String[] countries=selectionName.split("&");
					Arrays.asList(countries).stream()
					.forEach(arg0 -> {
						try {
							filterAbove.selectFilter(arg0);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
					
				}else{
				// System.out.println("Filter Name is :
				// "+filterAbove.getFilterName());
					filterAbove.selectFilter(selectionName);
				}
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

	public void node_drillDown(String nodeName) {
//		Actions act = new Actions(driver);
	

		nodes.stream()
			.filter(e->{
				String text1=e.findElement(By.xpath(".//*[name()='title']")).getAttribute("value");
				return text1.equalsIgnoreCase(nodeName);
			})
//			.map(e->e.findElement(By.xpath(".//*[name()='circle']")))
			.forEach(e->e.click());
			
	}

	public List<String> getLoE_nodes() {
		List<String> nodeNames = 
		nodes.stream()
			.map(e->e.findElement(By.xpath(".//*[name()='title']")).getAttribute("value"))
			.collect(Collectors.toList());
		
		return nodeNames;
	}
	
	/**
	 * slider moving function
	 */
	
	/**
	 * detect slider element
	 */
	@FindAll({ @FindBy(xpath = ".//div[@id='slider']//a")})
	
	List<WebElement> sliders;
	
	public void sliderMove(){
		for(WebElement tSlider:sliders){
			if(tSlider.getAttribute("style").contains("left: 0%")){
				Actions act=new Actions(driver);
				act.clickAndHold(tSlider).perform();
				act.moveByOffset(10, 10).perform();
				act.release(tSlider).perform();
			}
		}
	}
	
	public List<String> valueData() {
		Actions act=new Actions(driver);
		List<String> valueString=new ArrayList<String>();
		
		nodes.stream().forEach(e -> {
			act.moveToElement(e).perform();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			act.moveToElement(e).perform();
			valueString.add(
					driver.findElement(By.xpath(".//div[@id='bubbleChart']/div[@class='tooltip']")).getText() + "\n");
		});
		return valueString;
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
	/**
	 * capture all the data rows available
	 */

	@FindAll({ @FindBy(xpath = ".//tr[contains(@class,'Row') and not(contains(@class,'blankRow'))]") })

	List<WebElement> data_rows;

	public List<String> nodeValueData() {

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

					WebElement multi_chartData = dataCell.findElement(By.tagName("div"));
					String attr = null;
					try {
						attr = multi_chartData.getAttribute("class");
					} catch (Exception e) {
						System.out.println("Inside catch");
						continue;
					}
					if (attr.isEmpty() || attr == null)
						continue;
					else {
						if (multi_chartData.getAttribute("class").contains("bar-chart")) {
							sbData.append(multi_chartData.getAttribute("data-value"));

							sbData.append("," + multi_chartData.getAttribute("title"));
						}
					}

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
}
