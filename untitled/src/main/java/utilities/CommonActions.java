package utilities;

import base.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * CommonActions holds shared multi-step workflows used across step definitions.
 * Keeps step definitions thin and business logic in one place.
 */
public class CommonActions {

    private static final Logger log = LogManager.getLogger(CommonActions.class);

    private CommonActions() {}

    /** Navigates to the application URL. */
    public static void navigateToApp() {
        WebDriver driver = DriverFactory.getDriver();
        String url = ConfigReader.getInstance().getAppUrl();
        driver.get(url);
        WaitUtil.waitForPageLoad(driver);
        log.info("Navigated to: {}", url);
    }

    /** Navigates to a specific path appended to the base URL. */
    public static void navigateTo(String path) {
        WebDriver driver = DriverFactory.getDriver();
        String url = ConfigReader.getInstance().getAppUrl() + path;
        driver.get(url);
        WaitUtil.waitForPageLoad(driver);
        log.info("Navigated to: {}", url);
    }

    /** Refreshes the current page. */
    public static void refreshPage() {
        DriverFactory.getDriver().navigate().refresh();
        WaitUtil.waitForPageLoad(DriverFactory.getDriver());
        log.info("Page refreshed");
    }

    /** Returns the current page title. */
    public static String getPageTitle() {
        return DriverFactory.getDriver().getTitle();
    }
}
