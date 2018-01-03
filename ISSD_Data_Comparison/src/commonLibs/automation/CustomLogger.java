package commonLibs.automation;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import AutomationTests.AutomationTest_ISSD;

public class CustomLogger extends TestListenerAdapter {

	Logger Application_Logs;

	@Override
	public void onTestFailure(ITestResult tr) {
		String file = null;
		WebDriver driver = ((AutomationTest_ISSD) tr.getInstance()).getDriver();

		Application_Logs = Logger.getLogger(tr.getInstanceName());
		if (driver != null) {
			Reporter.setCurrentTestResult(tr);
			file = new userAutomationLibrary().captureScreen(driver, tr.getInstanceName(), "FAIL", tr.getName());

			Reporter.log("<br> <img src=." + file + " /> <br>");
			Reporter.setCurrentTestResult(null);
		}
		Application_Logs.error(tr.getName() + "--Test method failed");
	}

	@Override
	public void onTestSkipped(ITestResult tr) {

		Application_Logs = Logger.getLogger(tr.getInstanceName());
		Application_Logs.warn(tr.getName() + "--Test method skipped\n");

	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		String file = null;
		WebDriver driver = ((AutomationTest_ISSD) tr.getInstance()).getDriver();

		Application_Logs = Logger.getLogger(tr.getInstanceName());

		if (driver != null) {
			Reporter.setCurrentTestResult(tr);

			file = new userAutomationLibrary().captureScreen(driver, tr.getInstanceName(), "PASS", tr.getName());
			Reporter.log("<br> <img src=." + file + " /> <br>");
			Reporter.setCurrentTestResult(null);
		}
		Application_Logs.info(tr.getName() + "--Test method success\n");

	}

}
