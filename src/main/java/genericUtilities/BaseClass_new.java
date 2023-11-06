package genericUtilities;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import ObjectRepository.HomePage;
import ObjectRepository.LoginPage;

/**
 * This class contains basic configuration annotations
 * 
 * @author sharu
 *
 */

public class BaseClass_new {

	public PropertiesFileUtility pUtils = new PropertiesFileUtility();
	public WebDriverUtility wUtils = new WebDriverUtility();
	public JavaUtility jUtils = new JavaUtility();
	TestConfigUtility tUtils = new TestConfigUtility();
	public WebDriver driver = null;
	public static WebDriver sDriver = null; // sDriver used for taking screenshots

	/**
	 * This method will execute before Suite and creates an new Extent Report for
	 * the suite
	 */
	@BeforeSuite
	public void createExtentReport_bsConfig() {
		System.out.println("======= Suite execution started=============");
		ExtentManagerUtility.setUpExtentReport();
	}

	/**
	 * This method will execute once after suite execution and will flush all
	 * execution tests to Extent Report
	 */
	@AfterSuite
	public void flushExtentReport_asConfig() {
		System.out.println("======= Suite execution finished=============");
		ExtentManagerUtility.flushReport();
	}

	@Parameters({ "configFile", "environment" })
	@BeforeMethod(alwaysRun = true)
	public void setUpDriverAndLogin_bmConfig(@Optional String configFile, @Optional String environment, Method m,
			ITestResult result) throws IOException {
		String ConfigurationFile = null;
		String environmentInfo = null;

		// if "config-file" information is not passed from "suite.xml" file or from
		// command line{mvn comd},
		// pick config file from "commonData.properties" file

		if (System.getProperty("configFile") != null) {
			ConfigurationFile = System.getProperty("configFile");
			System.out.println(System.getProperty("configFile"));
		} else if (configFile != null) {
			ConfigurationFile = configFile;
		} else
			ConfigurationFile = pUtils.readFromPropertiesFile("configFile");

		// if environment is not provided from "suite.xml" file or mvn command, pick
		// environment from "commonData.properties"
		// if "environment" information is not passed from "suite.xml" file or from
		// command line{mvn comd},
		// pick environment information from "commonData.properties" file
		if (System.getProperty("environment") != null) {
			environmentInfo = System.getProperty("environment");
			System.out.println(System.getProperty("environment"));
		} else if (environment != null) {
			environmentInfo = environment;
		} else
			environmentInfo = pUtils.readFromPropertiesFile("environment");

		// Set up driver - local or remote based on info in config file
		_setUpDriver(ConfigurationFile, environmentInfo, m);

		wUtils.maximizeWindow(driver);
		wUtils.waitForPageToLoad(driver);

		// To attach info to test in extent report configuration in
		// ListenerImplementationClass's onTestStart()
		setITestResultAttributes(result, ConfigurationFile, environmentInfo);

		/**
		 * Launch app and login
		 */
		// launch app
		driver.get(pUtils.readFromPropertiesFile("app_url"));

		// Login Into App
		String emailAddress = pUtils.readFromPropertiesFile("email");
		String password = pUtils.readFromPropertiesFile("password");

		HomePage homePage = new HomePage(driver);
		homePage.clickMyAccountAndLoginLink();

		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginToApp(emailAddress, password);
	}

	private void _setUpDriver(String configurationFile, String environmentInfo, Method m) {
		String configFilePath = IConstantsUtility.configFilePath + configurationFile;

		// get "driver" key value - local or remote from given config file
		JSONObject configuration = tUtils.getTestConfiguration(configFilePath);
		String driverInJsonFile = tUtils.determineLocalOrRemoteDriver(configuration);

		if (driverInJsonFile.equalsIgnoreCase("local")) {
			setUpLocalWebDriver(configuration, environmentInfo);
		} else if (driverInJsonFile.equalsIgnoreCase("remote")) {
			setUpRemoteWebDriver(configuration, environmentInfo);
		} else if (driverInJsonFile.equalsIgnoreCase("saucelabs")) {
			setUpSauceLabsDriver(configuration, environmentInfo, m);
		} else if (driverInJsonFile.equalsIgnoreCase("browserstack")) {
			setUpBrowserStackDriver(configuration, environmentInfo, m);
		}
	}

	private void setUpBrowserStackDriver(JSONObject configuration, String environment, Method m) {
		String server = tUtils.getBrowserStackServer(configuration);
		String username = tUtils.getBrowserStackUserName(configuration);
		String accessKey = tUtils.getBrowserStackKey(configuration);
	

		String url = "https://" + username + ":" + accessKey + "@" + server + "/wd/hub";

		String BROWSER_NAME = tUtils.getBrowser(configuration, environment);
		String browser_Version = tUtils.getBrowserVersion(configuration, environment);
		String platformName = tUtils.getPlatform(configuration, environment);

		// Set Selenium w3c Capabilities
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("browserName", BROWSER_NAME);
		capabilities.setCapability("browserVersion", browser_Version);
		capabilities.setCapability("platformName", platformName);
		capabilities.setCapability("acceptInsecureCerts", true);

		// attach browser stack custom capabilities
		HashMap<String, Object> browserstackOptions = setBrowserStackGenericCapabilities(configuration);
		
		if(System.getenv("BROWSERSTACK_BUILD_NAME")!=null)
		browserstackOptions.put("buildName", System.getenv("BROWSERSTACK_BUILD_NAME"));
		
		capabilities.setCapability("bstack:options", browserstackOptions);

		try {
			driver = new RemoteWebDriver(new URL(url), capabilities);
			Reporter.log("******* Launched " + capabilities.getBrowserName() + " browser; Version: "
					+ capabilities.getCapability("browserVersion") + " Platform: " + capabilities.getPlatformName()
					+ " on 'BrowserStack'***********", true);
			sDriver = driver;
		} catch (Exception e) {
			e.printStackTrace();
		}

		setTestNameOnBrowserStack(driver, m.getName());
	}

	private void setTestNameOnBrowserStack(WebDriver driver2, String name) {
		final JavascriptExecutor jse = (JavascriptExecutor) driver;
		JSONObject executorObject = new JSONObject();
		JSONObject argumentsObject = new JSONObject();

		argumentsObject.put("name", name);
		executorObject.put("action", "setSessionName");
		executorObject.put("arguments", argumentsObject);

		jse.executeScript(String.format("browserstack_executor: %s", executorObject));
	}

	private void setUpSauceLabsDriver(JSONObject configuration, String environmentInfo, Method m) {
		String hubUrl = tUtils.getRemoteHubURL(configuration);

		Map<String, String> sauceCap = setSauceOptionsCapabilities(configuration, m);
		DesiredCapabilities cap = setCapabilities(configuration, environmentInfo);
		cap.setCapability("sauce:options", sauceCap);

		if (environmentInfo.equals("IE")) {
			cap.setCapability("edgechromium", true);
		}

		try {
			driver = new RemoteWebDriver(new URL(hubUrl), cap);
			Reporter.log("******* Launched " + cap.getBrowserName() + " browser; Version: "
					+ cap.getCapability("browserVersion") + " Platform: " + cap.getPlatformName() + " ***********",
					true);
			sDriver = driver;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Map<String, String> setSauceOptionsCapabilities(JSONObject configuration, Method m) {
		Map<String, String> sauceCap = (Map<String, String>) tUtils.getSauceLabsCapabilities(configuration);
		sauceCap.put("name", m.getName());
		return sauceCap;
	}

	private HashMap<String, Object> setBrowserStackGenericCapabilities(JSONObject configuration) {
		HashMap<String, Object> bsCap = (HashMap<String, Object>) tUtils
				.getBrowserStackGeneralCapabilities(configuration);
		String buildName = bsCap.get("buildName").toString() + jUtils.getSystemDataAndTimeInFormat();
		bsCap.put("buildName", buildName);
		
		return bsCap;
	}

	private void setUpRemoteWebDriver(JSONObject configuration, String environmentInfo) {
		// RWD(hubURL,dc)
		String hubUrl = tUtils.getRemoteHubURL(configuration);
		DesiredCapabilities cap = setCapabilities(configuration, environmentInfo);

		try {
			driver = new RemoteWebDriver(new URL(hubUrl), cap);
			sDriver = driver;
			Reporter.log("******* Launched " + cap.getBrowserName()+" ***********", true);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method will launch browser on local machine
	 * 
	 * @param BROWSER_NAME
	 * @param BROWSER_VERSION
	 */
	public void setUpLocalWebDriver(JSONObject configuration, String environment) {
		String BROWSER_NAME = tUtils.getBrowser(configuration, environment);
		DesiredCapabilities cap = setCapabilities(configuration, environment);

		if (BROWSER_NAME.equalsIgnoreCase("chrome")) {
			driver = setUpChromeDriver(cap);
			sDriver = driver;

		} else if (BROWSER_NAME.equalsIgnoreCase("firefox")) {
			driver = setUpFirefoxDriver(cap);
			sDriver = driver;
		} else if (BROWSER_NAME.equalsIgnoreCase("edge")) {
			driver = setUpEdgeDriver(cap);
			sDriver = driver;

		} else if (BROWSER_NAME.equalsIgnoreCase("internet explorer")) {
			driver = setUpIEDriver(cap);
			sDriver = driver;

		} else
			Reporter.log("Incorrect browser provided", true);
	}

	public DesiredCapabilities setCapabilities(JSONObject configuration, String environment) {
		Map<String, String> caps = (Map<String, String>) tUtils.getEnvCapabilities(configuration, environment);
		DesiredCapabilities cap = new DesiredCapabilities();

		for (Map.Entry<String, String> entry : caps.entrySet()) {
			cap.setCapability(entry.getKey(), entry.getValue());
		}
		return cap;
	}

	public WebDriver setUpChromeDriver(DesiredCapabilities cap) {
		ChromeOptions cOptions = new ChromeOptions();
		cOptions.merge(cap);
		WebDriver driver = new ChromeDriver(cOptions);
		Reporter.log(
				"******* Launched 'Chrome' browser; Version: " + cap.getCapability("browserVersion") + " ***********",
				true);
		return driver;
	}

	public WebDriver setUpFirefoxDriver(DesiredCapabilities cap) {
		FirefoxOptions fOptions = new FirefoxOptions();
		fOptions.merge(cap);
		WebDriver driver = new FirefoxDriver(fOptions);
		Reporter.log(
				"******* Launched 'Firefox' browser; Version: " + cap.getCapability("browserVersion") + " ***********",
				true);
		return driver;
	}

	public WebDriver setUpEdgeDriver(DesiredCapabilities cap) {
		EdgeOptions eOptions = new EdgeOptions();
		eOptions.merge(cap);
		WebDriver driver = new EdgeDriver(eOptions);
		Reporter.log("******* Launched 'Microsoft Edge' browser; Version: " + cap.getCapability("browserVersion")
				+ " ***********", true);
		return driver;
	}

	public WebDriver setUpIEDriver(DesiredCapabilities cap) {
		InternetExplorerOptions ieOptions = new InternetExplorerOptions();
		ieOptions.merge(cap);
		ieOptions.attachToEdgeChrome();
		WebDriver driver = new InternetExplorerDriver(ieOptions);
		Reporter.log("******* Launched 'Internet Explorer' browser; Version: " + cap.getCapability("browserVersion")
				+ " ***********", true);
		return driver;
	}

	private void setITestResultAttributes(ITestResult result, String configurationFile, String environmentInfo) {
		String configFilePath = IConstantsUtility.configFilePath + configurationFile;
		JSONObject configuration = tUtils.getTestConfiguration(configFilePath);

		String driver = tUtils.determineLocalOrRemoteDriver(configuration);

		if (driver.equalsIgnoreCase("local")) {
			result.setAttribute("browserName", tUtils.getBrowser(configuration, environmentInfo));
			result.setAttribute("platform", "Windows");
			result.setAttribute("driver", "local");
		} else if (driver.equalsIgnoreCase("remote")) {
			result.setAttribute("browserName", tUtils.getBrowser(configuration, environmentInfo));
			result.setAttribute("platform", "Windows");
			result.setAttribute("driver", "remote");
		} else if (driver.equalsIgnoreCase("saucelabs")) {
			result.setAttribute("browserName", tUtils.getBrowser(configuration, environmentInfo));
			result.setAttribute("browserVersion", tUtils.getBrowserVersion(configuration, environmentInfo));
			result.setAttribute("platformName", tUtils.getPlatform(configuration, environmentInfo));
			result.setAttribute("driver", "saucelabs");
		} else if (driver.equalsIgnoreCase("browserstack")) {
			result.setAttribute("browserName", tUtils.getBrowser(configuration, environmentInfo));
			result.setAttribute("browserVersion", tUtils.getBrowserVersion(configuration, environmentInfo));
			result.setAttribute("platformName", tUtils.getPlatform(configuration, environmentInfo));
			result.setAttribute("driver", "browserstack");
		}
	}

	/**
	 * This method will execute once after every @Test method This method will quit
	 * the current Driver session
	 * 
	 * @throws IOException
	 * 
	 */
	@AfterMethod(alwaysRun = true)
	public void quitDriver_amConfig(ITestResult result) throws IOException {
		// if execution on remote(browserstack cloud), mark test cases as pass/fail
		if (result.getAttribute("driver").toString().equalsIgnoreCase("browserstack")) {
			final JavascriptExecutor jse = (JavascriptExecutor) driver;
			JSONObject executorObject = new JSONObject();
			JSONObject argumentsObject = new JSONObject();
			argumentsObject.put("status", result.isSuccess() ? "passed" : "failed");
			executorObject.put("action", "setSessionStatus");
			executorObject.put("arguments", argumentsObject);
			jse.executeScript(String.format("browserstack_executor: %s", executorObject));
		}

		driver.quit();
	}

}
