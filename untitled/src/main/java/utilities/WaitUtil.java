package utilities;

import constants.AppConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.NoSuchElementException;

/**
 * WaitUtil provides static wait helpers for scenarios not covered by BasePage.
 * All waits are explicit — no Thread.sleep().
 */
public class WaitUtil {

    private static final Logger log = LogManager.getLogger(WaitUtil.class);

    private WaitUtil() {}

    /** Fluent wait with custom polling and ignoring NoSuchElementException. */
    public static WebElement fluentWait(WebDriver driver, By locator, int timeoutSec) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSec))
                .pollingEvery(Duration.ofMillis(AppConstants.POLLING_INTERVAL_MS))
                .ignoring(NoSuchElementException.class);
        log.debug("Fluent wait for locator: {}", locator);
        return fluentWait.until(d -> d.findElement(locator));
    }

    /** Wait for page to fully load (document.readyState == complete). */
    public static void waitForPageLoad(WebDriver driver) {
        int timeout = ConfigReader.getInstance().getPageLoadTimeout();
        new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until((ExpectedCondition<Boolean>) d -> {
                    assert d != null;
                    return ((org.openqa.selenium.JavascriptExecutor) d)
                            .executeScript("return document.readyState").equals("complete");
                });
        log.debug("Page fully loaded");
    }

    /** Wait for URL to contain a specific fragment. */
    public static void waitForUrlContains(WebDriver driver, String urlFragment) {
        int timeout = ConfigReader.getInstance().getTimeout();
        new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until(ExpectedConditions.urlContains(urlFragment));
        log.debug("URL now contains: {}", urlFragment);
    }

    /** Wait for element text to be non-empty. */
    public static void waitForNonEmptyText(WebDriver driver, WebElement element) {
        int timeout = ConfigReader.getInstance().getTimeout();
        new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until(d -> !element.getText().trim().isEmpty());
        log.debug("Element text is now non-empty");
    }

    /** Wait for a specific number of windows/tabs to be open. */
    public static void waitForNumberOfWindows(WebDriver driver, int count) {
        int timeout = ConfigReader.getInstance().getTimeout();
        new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until(ExpectedConditions.numberOfWindowsToBe(count));
    }

    /** Wait for element attribute to contain a value. */
    public static void waitForAttributeContains(WebDriver driver, WebElement element,
                                                 String attribute, String value) {
        int timeout = ConfigReader.getInstance().getTimeout();
        new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until(ExpectedConditions.attributeContains(element, attribute, value));
    }
}
