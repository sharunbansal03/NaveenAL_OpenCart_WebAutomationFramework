package genericUtilities;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

/**
 * This class will implement abstract methods of ITestListener Setup of Extent
 * Report and Capture screenshot on failure
 * 
 * @author sharu
 *
 */
public class ListenerImplementationClass implements ITestListener {

	/**
	 * ThreadLocal object used so that parallely executing threads cannot see the
	 * value of each other. They can set and get different values.
	 */
	ThreadLocal<ExtentTest> extentTestThreadSafe = new ThreadLocal<ExtentTest>();

	public void onTestStart(ITestResult result,ITestContext testInfo) {
		String methodName = result.getMethod().getMethodName();
		ExtentTest test = ExtentManagerUtility.report.createTest(methodName);

		// add test to threadlocal ExtentTest pool
		extentTestThreadSafe.set(test);

		// log execution environment info
		extentTestThreadSafe.get()
				.info("Browser: " + testInfo.getAttribute("browserName") + "; BrowserVersion: "
						+ testInfo.getAttribute("browserVersion") + "; OS/Platform: " + testInfo.getAttribute("platform")
						+ "; Driver: " + testInfo.getAttribute("driver"));

		extentTestThreadSafe.get().log(Status.INFO, "Test Execution Started: " + methodName);
	}

	public void onTestSuccess(ITestResult result) {
		// log as pass
		extentTestThreadSafe.get().log(Status.PASS, "Test passed: " + result.getMethod().getMethodName());
	}


	public void onTestFailure(ITestResult result) {
		JavaUtility jUtils = new JavaUtility();
		// TODO Auto-generated method stub
		// log as fail and attach screenshot
		String methodName = result.getMethod().getMethodName();
		extentTestThreadSafe.get().log(Status.FAIL, "Test Script failed - " + methodName);
		extentTestThreadSafe.get().log(Status.FAIL, result.getThrowable());

		String screenshotName = methodName + "-" + jUtils.getSystemDataAndTimeInFormat();
		
		attachScreenshotToReport(screenshotName);
		
	}


	public void onTestSkipped(ITestResult result) {
		JavaUtility jUtils = new JavaUtility();

		// TODO Auto-generated method stub
		// log as skip and attach screenshot
		String methodName = result.getMethod().getMethodName();
		extentTestThreadSafe.get().log(Status.SKIP, "Test Script skipped - " + methodName);
		extentTestThreadSafe.get().log(Status.SKIP, result.getThrowable());

		String screenshotName = methodName + "-" + jUtils.getSystemDataAndTimeInFormat();

		attachScreenshotToReport(screenshotName);
		
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	public void onTestFailedWithTimeout(ITestResult result) {

	}

	public void onStart(ITestContext context) {
	}

	public void onFinish(ITestContext context) {
	}

	
	/**
	 * This method will attach screenshot to extent test
	 * 
	 * If execution is in jenkins, it will pick screenshot from jenkins job workspace
	 * else, it will pick screenshot from local working directory
	 * 
	 * @param screenshotName
	 */
	private void attachScreenshotToReport(String screenshotName) {
		WebDriverUtility wUtils = new WebDriverUtility();


		/** executing from jenkins, on local/remote/saucelabs/browserstack, pick
		screenshot from jenkins workspace **/
		if (System.getProperty("jenkinsExecution")!=null && System.getProperty("jenkinsExecution").equalsIgnoreCase("yes")) {

			/* Creates screenshot in 'Screenshots' folder of project */
			try {
				wUtils.takeScreenshot(BaseClass_new.sDriver, screenshotName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			 * Extracts name of jenkins job since System.getProperty("user.dir") when
			 * executing from Jenkins returns local directory path like
			 * 'C:\ProgramData\Jenkins\.jenkins\workspace\WCSM23-SmokeSuite'
			 */
			String jenkinsJobName = System.getProperty("user.dir")
					.substring(System.getProperty("user.dir").lastIndexOf("\\") + 1);

			/* Creates path to captured screenshot in current jenkins job's workspace */
			String pathToScreenshotInJob = "/job/" + jenkinsJobName + "/ws/Screenshots/" + screenshotName + ".png";
			Reporter.log("Path to screenshot in jenkins job: "+pathToScreenshotInJob,true);

			/*
			 * Attaches screenshot captured from Jenkins job's workspace to the extent
			 * report
			 */
			extentTestThreadSafe.get().addScreenCaptureFromPath(pathToScreenshotInJob);
		} else {
			
			/** executing from local system, on local/remote/saucelabs/browserstack, pick
			screenshot from absolute path in screenshots folder**/
			
			String path;
			try {
				path = wUtils.takeScreenshot(BaseClass_new.sDriver, screenshotName);
				extentTestThreadSafe.get().addScreenCaptureFromPath(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
