/**
 * 
 */
package PageOModel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author JMukherjee
 *
 */
public class BrowserFactory {

	 static WebDriver driver=null;

	public static WebDriver startBrowser(String browser, String Url) throws Exception {

		if (browser.equalsIgnoreCase("firefox")) {
			/**
			 * Firefox driver code starts here
			 */
			Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability("marionette", true);
			System.setProperty("webdriver.gecko.driver", "./lib/geckodriver.exe");
			driver = new FirefoxDriver(capabilities);

			/**
			 * Firefox driver code ends here
			 */
		}
		else if (browser.equalsIgnoreCase("chrome")) {
			/**
			 * Chrome driver code starts here
			 */
//			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
			prefs.put("download.prompt_for_download", "false");
			System.setProperty("webdriver.chrome.driver", "./lib/chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", prefs);
			options.addArguments("--test-type");
			driver = new ChromeDriver();
			
			/**
			 * Chrome driver code ends here
			 */
			
		}
		else if (browser.equalsIgnoreCase("ie")) {
			/**
			 * IE driver code starts here
			 */
			Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
			System.setProperty(
					"webdriver.ie.driver",
					"./lib/IEDriverServer.exe");

			DesiredCapabilities capab = DesiredCapabilities.internetExplorer();
			 Proxy oProxy = new Proxy();
			
			 oProxy.setProxyType(ProxyType.PAC);
			
			 oProxy.setProxyAutoconfigUrl("http://inblrpac01.internal.imsglobal.com/proxy.pac");
			
			 capab.setCapability(CapabilityType.PROXY, oProxy);
			 capab.setCapability(
			
			 InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
			 true);
			//
			capab.setCapability("ignoreZoomSetting", true);
			driver = new InternetExplorerDriver(capab);
			
			/**
			 * IE driver code starts here
			 */
		}
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(2, TimeUnit.MINUTES);
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.MINUTES);
		driver.manage().window().maximize();
		driver.get(Url);
		return driver;
	}
}
