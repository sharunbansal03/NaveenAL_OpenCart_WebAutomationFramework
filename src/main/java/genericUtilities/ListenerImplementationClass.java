package genericUtilities;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

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

	public void onTestStart(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		ExtentTest test = ExtentManagerUtility.report.createTest(methodName);
		
		// add test to threadlocal ExtentTest pool
		extentTestThreadSafe.set(test);
	
		// log execution environment info
		extentTestThreadSafe.get().info("Browser: " + result.getAttribute("browserName") + "; BrowserVersion: "
				+ result.getAttribute("browserVersion") + "; OS/Platform: " + result.getAttribute("platform"));
		
		extentTestThreadSafe.get().log(Status.INFO, "Test Execution Started: " + methodName);
	}

	public void onTestSuccess(ITestResult result) {
		// log as pass
		extentTestThreadSafe.get().log(Status.PASS, "Test passed: " + result.getMethod().getMethodName());
	}

	public void onTestFailure(ITestResult result) {
		JavaUtility jUtils = new JavaUtility();
		WebDriverUtility wUtils = new WebDriverUtility();

		// TODO Auto-generated method stub
		// log as fail and attach screenshot
		String methodName = result.getMethod().getMethodName();
		extentTestThreadSafe.get().log(Status.FAIL, "Test Script failed - " + methodName);
		extentTestThreadSafe.get().log(Status.FAIL, result.getThrowable());

		String screenshotName = methodName + "-" + jUtils.getSystemDataAndTimeInFormat();

		try {
			// executing from jenkins
			if (System.getProperty("server") != null) {

				/* Creates screenshot in 'Screenshots' folder of project */
				wUtils.takeScreenshot(BaseClass.sDriver, screenshotName);

				/*
				 * Extracts name of jenkins job since System.getProperty("user.dir") when
				 * executing from Jenkins returns local directory path like
				 * 'C:\ProgramData\Jenkins\.jenkins\workspace\WCSM23-SmokeSuite'
				 */
				String jenkinsJobName = System.getProperty("user.dir")
						.substring(System.getProperty("user.dir").lastIndexOf("\\") + 1);

				/* Creates path to captured screenshot in current jenkins job's workspace */
				String pathToScreenshotInJob = "/job/" + jenkinsJobName + "/ws/Screenshots/" + screenshotName + ".png";
				System.out.println(pathToScreenshotInJob);

				/*
				 * Attaches screenshot captured from Jenkins job's workspace to the extent
				 * report
				 */
				extentTestThreadSafe.get().addScreenCaptureFromPath(pathToScreenshotInJob);
			} else {
				// executing from local machine

				String path = wUtils.takeScreenshot(BaseClass.sDriver, screenshotName);
				extentTestThreadSafe.get().addScreenCaptureFromPath(path);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onTestSkipped(ITestResult result) {
		JavaUtility jUtils = new JavaUtility();

		WebDriverUtility wUtils = new WebDriverUtility();

		// TODO Auto-generated method stub
		// log as skip and attach screenshot
		String methodName = result.getMethod().getMethodName();
		extentTestThreadSafe.get().log(Status.SKIP, "Test Script skipped - " + methodName);
		extentTestThreadSafe.get().log(Status.SKIP, result.getThrowable());

		String screenshotName = methodName + "-" + jUtils.getSystemDataAndTimeInFormat();
		try {
			// executing from jenkins
			if (System.getProperty("server") != null) {

				/* Creates screenshot in 'Screenshots' folder of project */
				wUtils.takeScreenshot(BaseClass.sDriver, screenshotName);

				/*
				 * Extracts name of jenkins job since System.getProperty("user.dir") when
				 * executing from Jenkins returns local directory path like
				 * 'C:\ProgramData\Jenkins\.jenkins\workspace\WCSM23-SmokeSuite'
				 */
				String jenkinsJobName = System.getProperty("user.dir")
						.substring(System.getProperty("user.dir").lastIndexOf("\\") + 1);

				/* Creates path to captured screenshot in current jenkins job's workspace */
				String pathToScreenshotInJob = "/job/" + jenkinsJobName + "/ws/Screenshots/" + screenshotName + ".png";
				System.out.println(pathToScreenshotInJob);

				/*
				 * Attaches screenshot captured from Jenkins job's workspace to the extent
				 * report
				 */
				extentTestThreadSafe.get().addScreenCaptureFromPath(pathToScreenshotInJob);
			} else {

				// executing from local machine
				String path = wUtils.takeScreenshot(BaseClass.sDriver, screenshotName);
				extentTestThreadSafe.get().addScreenCaptureFromPath(path);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	public void onTestFailedWithTimeout(ITestResult result) {

	}

	public void onStart(ITestContext context) {
	}

	public void onFinish(ITestContext context) {
	}

}
