package genericUtilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManagerUtility {

	static ExtentReports report;

	public static void setUpExtentReport() {
		JavaUtility jUtils = new JavaUtility();
		String reportPath = IConstantsUtility.ExtentReportsFolderPath + "\\Report_" + jUtils.getSystemDataAndTimeInFormat()
				+ ".html";
		ExtentSparkReporter htmlReport = new ExtentSparkReporter(reportPath);
		htmlReport.config().setDocumentTitle("Amazon Execution Report");
		htmlReport.config().setReportName("Execution report");
		htmlReport.config().setTheme(Theme.DARK);

		report = new ExtentReports();
		report.attachReporter(htmlReport);
		report.setSystemInfo("Base url", "https://www.amazon.in/");
		report.setSystemInfo("Reporter name", "sharun");
	}

	public static void flushReport() {
		report.flush();
	}
}
