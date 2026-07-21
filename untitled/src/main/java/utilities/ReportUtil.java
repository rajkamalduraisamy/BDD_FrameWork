package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import constants.AppConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * ReportUtil manages the ExtentReports singleton and per-thread ExtentTest instances.
 * Thread-safe for parallel execution.
 */
public class ReportUtil {

    private static final Logger log = LogManager.getLogger(ReportUtil.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();

    private ReportUtil() {}

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = ConfigReader.getInstance().get(
                    AppConstants.EXTENT_REPORT_PATH, "reports/ExtentReport.html");

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle(AppConstants.REPORT_TITLE);
            spark.config().setReportName(AppConstants.REPORT_NAME_LABEL);
            spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
            spark.config().setEncoding("UTF-8");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
            extent.setSystemInfo("Browser", ConfigReader.getInstance().getBrowser());
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Author", "Automation Team");

            log.info("ExtentReports initialised at: {}", reportPath);
        }
        return extent;
    }

    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        testThreadLocal.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return testThreadLocal.get();
    }

    public static void removeTest() {
        testThreadLocal.remove();
    }

    public static void logInfo(String message) {
        ExtentTest test = getTest();
        if (test != null) test.info(message);
    }

    public static void logPass(String message) {
        ExtentTest test = getTest();
        if (test != null) test.pass(message);
    }

    public static void logFail(String message) {
        ExtentTest test = getTest();
        if (test != null) test.fail(message);
    }

    public static void logScreenshot(WebDriver driver, String title) {
        try {
            String base64 = ScreenshotUtil.captureAsBase64(driver);
            ExtentTest test = getTest();
            if (test != null && !base64.isEmpty()) {
                test.info(title, MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
            }
        } catch (Exception e) {
            log.warn("Could not attach screenshot to report: {}", e.getMessage());
        }
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
            log.info("ExtentReports flushed");
        }
    }
}
