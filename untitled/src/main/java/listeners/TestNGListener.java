package listeners;

import base.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utilities.ReportUtil;

/**
 * TestNGListener hooks into TestNG lifecycle to update Extent Reports
 * and capture screenshots on failure.
 */
public class TestNGListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestNGListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("Test Suite started: {}", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        ReportUtil.createTest(testName, description != null ? description : testName);
        log.info("Test started: {}", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ReportUtil.logPass("Test PASSED: " + result.getName());
        log.info("Test PASSED: {}", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test FAILED: {} — {}", result.getName(), result.getThrowable().getMessage());
        try {
            ReportUtil.logScreenshot(DriverFactory.getDriver(), "Failure Screenshot");
        } catch (Exception e) {
            log.warn("Could not capture failure screenshot: {}", e.getMessage());
        }
        ReportUtil.logFail("Test FAILED: " + result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ReportUtil.logInfo("Test SKIPPED: " + result.getName());
        log.warn("Test SKIPPED: {}", result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ReportUtil.flush();
        log.info("Test Suite finished: {}", context.getName());
    }
}
