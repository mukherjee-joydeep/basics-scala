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
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

/**
 * @author JMukherjee
 *
 */
public class Country_QA1_first_page {

	private WebDriver driver;

	public Country_QA1_first_page(WebDriver pdriver) {
		this.driver = pdriver;

	}

	/**
	 * capture page title
	 */

	@FindBy(xpath = ".//span[contains(@class,'nav-title')]")

	private WebElement page_title;

	/**
	 * capturing filter country KPI controls,
	 */
	
	@FindAll({ @FindBy(xpath = ".//div[@id='filter-CtyKpi-control']/div[contains(@class,'btnFilter')]") })

	private List<WebElement> filterCountryKPI;
	
	/**
	 * Get list of Country KPI filter values
	 * @return List of KPI Strings
	 */
	public List<String> countryKPIvalues(){
		return filterCountryKPI.stream()
				.map(e->e.getText())
				.filter(e->!(e.isEmpty()))
				.collect(Collectors.toList());
	}
	
	/**
	 * Select a Country KPI in the filter Country KPI controls
	 * @param KPIName
	 */
	public void selectCountryKPI(String KPIName){
		
		filterCountryKPI.stream()
				.filter(e->e.getText().equalsIgnoreCase(KPIName))
				.forEach(e->e.click());
	}
	
	/**
	 * capture each of the table rows from Country View widget
	 */
	
	@FindAll({ @FindBy(xpath = ".//div[@id='widget-country-view']//tr[contains(@class,'parent-id-')]") })

	private List<WebElement> countryViewTRows;
	
	/**
	 * capture data from country view widget
	 */
	public List<String> getWidgetData(){
		return countryViewTRows.stream()
							.map(e->e.findElement(By.xpath(".//div[@data-key='"
							+e.findElement(By.xpath(".//th/div")).getAttribute("title")+
							"']")).getAttribute("title"))
							.collect(Collectors.toList());	
	}
	/**
	 * segment table data rows
	 */
	@FindAll({ @FindBy(xpath = ".//div[@id='top-right-container']//tr[contains(@class,'parent-id-')]") })
	
	private List<WebElement> segmentTRows;
	/**
	 * segment table data capture
	 */
	
	public List<String> getSegmentData(){
		List<String> segData = new ArrayList<>();
		for(WebElement segmentTRow:segmentTRows){
			StringBuilder tempS=new StringBuilder();
			tempS.append(segmentTRow.findElement(By.xpath(".//th/div")).getText()+",");
			List<WebElement> tdoc=segmentTRow.findElements(By.tagName("td"));
			for(WebElement tempdoc:tdoc){
				if(tempdoc.getAttribute("class").contains("max-min-col")){
					tempS.append(tempdoc.findElement(By.xpath(".//div[contains(@class,'max')]")).getText());
					tempS.append(","+tempdoc.findElement(By.xpath(".//div[contains(@class,'min')]")).getText());
				}
				else if(tempdoc.getAttribute("class").contains("multi-chart-col")){
					tempS.append(tempdoc.findElement(By.xpath(".//span[@class='sparkline-line']")).getAttribute("data-absvalue"));
				}
				else{
					tempS.append(tempdoc.getText());
				}
				tempS.append(",");
			}
			segData.add(tempS.toString());
		}
		
		return segData;
	}
	/**
	 * capture page filter options button
	 */

	@FindBy(xpath = ".//span[@id='toggle-panel-2']")

	private WebElement optionsButton;

	/**
	 * capture filter table box
	 * 
	 */
	@FindBy(xpath = ".//table[@class='filter-container-layout-table']")

	private WebElement filterTable;

	/**
	 * capture all the filters available
	 */
	@FindAll({ @FindBy(xpath = ".//div[@class='dropdown-filter-items']") })

	private List<WebElement> filters;

	/**
	 * capture all the data rows available
	 */

	@FindAll({ @FindBy(xpath = ".//div[contains(@class,'global-perf-table-container') "
			+ "or contains(@class,'kpi-details-table-container')]") })

	private List<WebElement> data_Tables;

	public List<String> valueData() {

		List<String> tempData = new ArrayList<String>();
	
		kpiControls.stream()
		.forEach(e->{
			String filterName=e.getText();
			tempData.add(filterName+"\n--------------------------------------------------------------------------------------\n");
			e.click();
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			tempData.addAll(getfirstBarChart1Tooltips(filterName));
			tempData.addAll(getsecondBarChart1Tooltips(filterName));
			tempData.addAll(getsideBarChart1Tooltips(filterName));
			tempData.add("\n");
			tempData.addAll(getTabChartdata());
			tempData.add("\n--------------------------------------------------------------------------------------\n");
		});
		
		
		return tempData;

	}

	/**
	 * collect all the KPI controls
	 */
	@FindAll({ @FindBy(xpath = "//div[@id='filter-CtyKpi-control']//div[contains(@class,'btnFilter')]") })
	private List<WebElement> kpiControls;
	
	
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

	private WebElement updateButton;

	/**
	 * capture Defaults button in filter box
	 * 
	 */
	@FindBy(xpath = ".//input[(@type='button')and(@value='Defaults')]")

	private WebElement defaultsButton;

	/**
	 * Each Country data is available as table in Performance first page
	 */
	@FindAll({ @FindBy(xpath = ".//div[@id='widget-CountryModSecondNavRightMultiChartWidget-view']//div[@class='widget-reports']") })

	private List<WebElement> countryTable;

	/**
	 * Country tables root of the Performance first page
	 */

	@FindBy(xpath = ".//div[@id='widget-Performance-view']")

	private WebElement cperfRoot;

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

	/**
	 * click on each of the continents graphs to drill down to next page
	 * @param countryName
	 * @throws InterruptedException
	 */
	public void node_drillDown(String countryName) throws InterruptedException {
		//Actions act = new Actions(driver);
		countryTable.stream()
				.filter(e -> e.getAttribute("onclick").contains(countryName))
				.forEach(e->e.click());
		Thread.sleep(10000);

	}

//	/**
//	 * Each Country data is available as table in Performance first page
//	 */
//	@FindAll({ @FindBy(xpath = ".//div[(@class='table-top-header') or (@class='kpi-details-header')]") })
//
//	List<WebElement> countryTable_country;
	
	public List<String> getCountry_tables() {
		List<String> nodeNames = countryTable.stream()
				.map(e->e.findElement(By.xpath(".//div[contains(@class,'countryModmultiChartTitle')]")).getText())
				.collect(Collectors.toList());

		return nodeNames;
	}
	/**
	 * capture page breadCrumbHolder
	 */

	@FindBy(xpath = ".//div[@class='breadCrumbHolder']")
	@CacheLookup
	private WebElement page_breadCrumb;

	public void getBack(String menu) {
		page_breadCrumb.findElement(By.linkText(menu)).click();
	}
	
	/**
	 * Each Country data is available as tab chart in QA 1 hierarchy first page
	 */
	@FindAll({ @FindBy(xpath = ".//div[@id='RootDiv']//div[@class='innerGreen']") })

	private List<WebElement> tabChartdata;
	
	/**
	 * get data text from tablet chart
	 * 
	 * 
	 * @return List<String> List of Strings which are data text
	 *  when we hover over each inner green of tablet chart
	 */
	private List<String> getTabChartdata(){
		return tabChartdata.stream()
			.map(e->e.getAttribute("title"))
			.map(e -> e.concat("\n"))
			.collect(Collectors.toList());
	}
	
	/**
	 * capturing first bar graph each bars details
	 */
	
	@FindAll({ @FindBy(xpath = "//span[@id='obj_CountrySecondTopLeft']//*[name()='svg']/*[name()='g' and contains(@class,'red-hot-')]/*[name()='rect']") })
	private List<WebElement> firstBarChart1data;
	
	private List<String> getfirstBarChart1Tooltips(String filterName){
		Actions act = new Actions(driver);
		List<String> tooltipData = new ArrayList<String>();
		tooltipData.add("World - "+filterName+"\n\n");
		
		firstBarChart1data.stream()
		.forEach(e->{
			act.moveToElement(e).perform();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			act.moveToElement(e).perform();
			tooltipData.add(driver.findElement(By.xpath(".//span[@id='fusioncharts-tooltip-element']")).getText()+"\n");
		});
		tooltipData.add("\n");
		return tooltipData;
	}
	
	/**
	 * capturing 2nd bar graph each bars details
	 */
	
	@FindAll({ @FindBy(xpath = "//span[@id='obj_CountrySecondBottomLeft']//*[name()='svg']/*[name()='g' and contains(@class,'red-hot-')]/*[name()='rect']") })
	private List<WebElement> secondBarChart1data;
	
	private List<String> getsecondBarChart1Tooltips(String filterName){
		Actions act = new Actions(driver);
		List<String> tooltipData = new ArrayList<String>();
		tooltipData.add("World Top 20 countries - "+filterName+"\n\n");
		
		secondBarChart1data.stream()
		.forEach(e->{
			act.moveToElement(e).perform();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			act.moveToElement(e).perform();
			tooltipData.add(driver.findElement(By.xpath(".//span[@id='fusioncharts-tooltip-element']")).getText()+"\n");
		});
		tooltipData.add("\n");
		return tooltipData;
	}
	
	/**
	 * capturing side bar graphs each bars details
	 */
	
	@FindAll({ @FindBy(xpath = "//div[@id='widget-CountryModSecondNavRightMultiChartWidget-view']//*[name()='svg']/*[name()='g' and contains(@class,'red-hot-')]/*[name()='rect']") })
	private List<WebElement> sideBarChart1data;
	
	private List<String> getsideBarChart1Tooltips(String filterName){
		Actions act = new Actions(driver);
		List<String> tooltipData = new ArrayList<String>();
		tooltipData.add("Side Bar charts - "+filterName+"\n\n");
		
		sideBarChart1data.stream()
		.forEach(e->{
			act.moveToElement(e).perform();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			act.moveToElement(e).perform();
			tooltipData.add(driver.findElement(By.xpath(".//span[@id='fusioncharts-tooltip-element']")).getText()+"\n");
		});
		tooltipData.add("\n");
		return tooltipData;
	}
}
