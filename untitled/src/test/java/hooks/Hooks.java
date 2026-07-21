package hooks;

import base.DriverFactory;
import io.cucumber.java.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.CommonActions;
import utilities.DownloadUtil;
import utilities.ReportUtil;
import utilities.ScreenshotUtil;

/**
 * Hooks manages the Cucumber test lifecycle.
 *
 * @Before  — launches browser, navigates to app
 * @After   — captures screenshot on failure, quits browser
 * @BeforeStep — logs step name
 * @AfterStep  — captures screenshot on step failure
 */
public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);
    private final ScenarioContext context;

    public Hooks(ScenarioContext context) {
        this.context = context;
    }

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        log.info("========== Starting Scenario: {} ==========", scenario.getName());
        DownloadUtil.clearDownloadDirectory();
        DriverFactory.initDriver();
        CommonActions.navigateToApp();
        ReportUtil.createTest(scenario.getName(), scenario.getId());
        log.info("Browser launched and navigated to app URL");
    }

    @BeforeStep
    public void beforeStep(Scenario scenario) {
        log.debug("--- Step starting in scenario: {}", scenario.getName());
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed()) {
            log.warn("Step FAILED in scenario: {}", scenario.getName());
            try {
                String base64 = ScreenshotUtil.captureAsBase64(DriverFactory.getDriver());
                if (!base64.isEmpty()) {
                    scenario.attach(
                            java.util.Base64.getDecoder().decode(base64),
                            "image/png",
                            "Step Failure Screenshot"
                    );
                    ReportUtil.logScreenshot(DriverFactory.getDriver(), "Step Failure");
                }
            } catch (Exception e) {
                log.warn("Could not attach step failure screenshot: {}", e.getMessage());
            }
        }
    }

    @After(order = 0)
    public void tearDown(Scenario scenario) {
        log.info("Scenario status: {}", scenario.getStatus());

        // Always capture screenshot at end of every scenario
        try {
            String screenshotName = scenario.getStatus().name() + "_" + scenario.getName();
            String screenshotPath = ScreenshotUtil.capture(DriverFactory.getDriver(), screenshotName);
            String base64 = ScreenshotUtil.captureAsBase64(DriverFactory.getDriver());
            if (!base64.isEmpty()) {
                scenario.attach(
                        java.util.Base64.getDecoder().decode(base64),
                        "image/png",
                        scenario.getStatus().name() + " Screenshot"
                );
                ReportUtil.logScreenshot(DriverFactory.getDriver(), screenshotName);
            }
            log.info("Screenshot captured: {}", screenshotPath);
        } catch (Exception e) {
            log.warn("Could not capture screenshot: {}", e.getMessage());
        }

        // Always logout before quitting
        try {
            new pages.HomePage(DriverFactory.getDriver()).logout();
            log.info("Logout completed in @After hook");
        } catch (Exception e) {
            log.warn("Logout skipped (page may not be in logged-in state): {}", e.getMessage());
        }

        if (scenario.isFailed()) {
            ReportUtil.logFail("Scenario FAILED: " + scenario.getName());
        } else {
            ReportUtil.logPass("Scenario PASSED: " + scenario.getName());
        }

        ReportUtil.flush();
        DriverFactory.quitDriver();
        log.info("========== Scenario Ended: {} ==========", scenario.getName());
    }
}
