package genericUtilities;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
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

public class BaseClass {

	public PropertiesFileUtility pUtils = new PropertiesFileUtility();
	public WebDriverUtility wUtils = new WebDriverUtility();
	public WebDriver driver = null;
	public static WebDriver sDriver = null;

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

	/**
	 * This method will execute before every @Test method This method will set up
	 * WebDriver session Driver will be RemoteWebDriver if server=remote, on
	 * SauceLabs cloud Driver will be respective Browser Driver if server=local
	 * 
	 * browserName - Can come from TestngXmlFile or Jenkins Parameters or Local
	 * CommonData.properties file browserVersion - Can come from TestngXmlFile or
	 * Jenkins Parameters or Local CommonData.properties file platformName - Can
	 * come from TestngXmlFile or Jenkins Parameters or Local CommonData.properties
	 * file
	 * 
	 * @throws IOException
	 * 
	 */
	@Parameters({ "browserName", "browserVersion", "platformName" })
	@BeforeMethod
	public void setUpDriverAndLogin_bmConfig(@Optional String browserName, @Optional String browserVersion,
			@Optional String platformName, Method m,ITestResult result) throws IOException {
		String SERVER = null;
		String BROWSER_NAME = null;
		String BROWSER_VERSION = null;
		String PLATFORM_NAME = null;

		/**
		 * Set BROWSER_NAME
		 */
		if (browserName != null) {
			BROWSER_NAME = browserName;
		} else if (System.getProperty("browserName") != null) {
			BROWSER_NAME = System.getProperty("browserName");
		} else
			BROWSER_NAME = pUtils.readFromPropertiesFile("browserName");

		/**
		 * Set BROWSER_VERSION
		 */
		if (browserVersion != null) {
			BROWSER_VERSION = browserVersion;
		} else if (System.getProperty("browserVersion") != null) {
			BROWSER_VERSION = System.getProperty("browserVersion");
		} else
			BROWSER_VERSION = pUtils.readFromPropertiesFile("browserVersion");

		/**
		 * Set platformName
		 */
		if (platformName != null) {
			PLATFORM_NAME = platformName;
		} else if (System.getProperty("platformName") != null) {
			PLATFORM_NAME = System.getProperty("platformName");
		} else
			PLATFORM_NAME = pUtils.readFromPropertiesFile("platformName");

		/**
		 * Get SERVER - local or remote
		 */
		if (System.getProperty("server") != null) {
			SERVER = System.getProperty("server");
		} else
			SERVER = pUtils.readFromPropertiesFile("server");

		if (SERVER.equalsIgnoreCase("local")) {
			setUpLocalWebDriver(BROWSER_NAME, BROWSER_VERSION);
		} else if (SERVER.equalsIgnoreCase("remote")) {
			setUpRemoteWebDriver(BROWSER_NAME, BROWSER_VERSION, PLATFORM_NAME, m.getName());
		}

		// sDriver used for taking screenshots
		sDriver = driver;

		// maximize browser
		wUtils.maximizeWindow(driver);

		// set implicit wait
		wUtils.waitForPageToLoad(driver);

		// launch app
		driver.get(pUtils.readFromPropertiesFile("app_url"));

		// Login Into App
		String emailAddress = pUtils.readFromPropertiesFile("email");
		String password = pUtils.readFromPropertiesFile("password");

		HomePage homePage = new HomePage(driver);
		homePage.clickMyAccountAndLoginLink();

		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginToApp(emailAddress, password);
		
		result.setAttribute("browserName", BROWSER_NAME);
		result.setAttribute("browserVersion", BROWSER_VERSION);
		result.setAttribute("platform", PLATFORM_NAME);
	}

	/**
	 * This method will launch required Driver configuration on SauceLabs cloud
	 * 
	 * @param BROWSER_NAME
	 * @param BROWSER_VERSION
	 * @param PLATFORM_NAME
	 * @param methodName
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void setUpRemoteWebDriver(String BROWSER_NAME, String BROWSER_VERSION, String PLATFORM_NAME,
			String methodName) throws MalformedURLException, IOException {

		Map<String, String> sauceOptions = new HashMap<String, String>();
		sauceOptions.put("username", "oauth-sharun.bansal03-bef98");
		sauceOptions.put("accessKey", "3009e83f-d087-492b-b038-3ee66232ad5d");
		sauceOptions.put("name", methodName);

		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("platformName", PLATFORM_NAME);
		cap.setCapability("browserName", BROWSER_NAME);
		cap.setCapability("browserVersion", BROWSER_VERSION);
		cap.setCapability("sauce:options", sauceOptions);
		driver = new RemoteWebDriver(new URL(pUtils.readFromPropertiesFile("hub_url")), cap);

		Reporter.log("******* Launched " + BROWSER_NAME + "; Version: " + BROWSER_VERSION + "; Platform: "
				+ PLATFORM_NAME + " ***********", true);
	}

	/**
	 * This method will launch browser on local machine
	 * 
	 * @param BROWSER_NAME
	 * @param BROWSER_VERSION
	 */
	public void setUpLocalWebDriver(String BROWSER_NAME, String BROWSER_VERSION) {
		if (BROWSER_NAME.equalsIgnoreCase("chrome")) {
			ChromeOptions chOptions = new ChromeOptions();
			chOptions.setBrowserVersion(BROWSER_VERSION);

			driver = new ChromeDriver(chOptions);
			Reporter.log("******* Launched " + BROWSER_NAME + "; Version: " + BROWSER_VERSION + " ***********", true);
		} else if (BROWSER_NAME.equalsIgnoreCase("firefox")) {
			FirefoxOptions ffOptions = new FirefoxOptions();
			ffOptions.setBrowserVersion(BROWSER_VERSION);

			driver = new FirefoxDriver(ffOptions);
			Reporter.log("******* Launched " + BROWSER_NAME + "; Version: " + BROWSER_VERSION + " ***********", true);
		} else if (BROWSER_NAME.equalsIgnoreCase("MicrosoftEdge")) {
			EdgeOptions edgeOptions = new EdgeOptions();
			edgeOptions.setBrowserVersion(BROWSER_VERSION);

			driver = new EdgeDriver(edgeOptions);
			Reporter.log("******* Launched " + BROWSER_NAME + "; Version: " + BROWSER_VERSION + " ***********", true);
		} else
			Reporter.log("Incorrect browser provided", true);
	}

	/**
	 * This method will execute once after every @Test method This method will quit
	 * the current Driver session
	 * 
	 * @throws IOException
	 * 
	 */
	@AfterMethod
	public void quitDriver_amConfig(ITestResult result) throws IOException {
		// if execution on remote(saucelabs cloud), mark test cases as pass/fail
		if ((System.getProperty("server") != null && System.getProperty("server").equalsIgnoreCase("remote"))
				|| pUtils.readFromPropertiesFile("server").equalsIgnoreCase("remote"))
			((JavascriptExecutor) driver)
					.executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));

		driver.quit();
	}

}
