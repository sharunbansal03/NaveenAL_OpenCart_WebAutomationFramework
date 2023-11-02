package genericUtilities;

import java.io.IOException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManagerUtility {

	static ExtentReports report;

	public static void setUpExtentReport() {
		JavaUtility jUtils = new JavaUtility();
		PropertiesFileUtility pUtils = new PropertiesFileUtility();
		String reportPath = IConstantsUtility.ExtentReportsFolderPath + "\\Report_" + jUtils.getSystemDataAndTimeInFormat()
				+ ".html";
		ExtentSparkReporter htmlReport = new ExtentSparkReporter(reportPath);
		htmlReport.config().setDocumentTitle("Amazon Execution Report");
		htmlReport.config().setReportName("Execution report");
		htmlReport.config().setTheme(Theme.DARK);

		report = new ExtentReports();
		report.attachReporter(htmlReport);
		try {
			report.setSystemInfo("Base url", pUtils.readFromPropertiesFile("app_url"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		report.setSystemInfo("Reporter name", "sharun");
	}

	public static void flushReport() {
		report.flush();
	}
}
