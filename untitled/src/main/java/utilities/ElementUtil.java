package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ElementUtil provides static helpers for element interactions
 * that complement BasePage instance methods.
 */
public class ElementUtil {

    private static final Logger log = LogManager.getLogger(ElementUtil.class);

    private ElementUtil() {}

    /** Returns all text values from a list of elements. */
    public static List<String> getTexts(List<WebElement> elements) {
        return elements.stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());
    }

    /** Finds an element in a list whose text matches the given value. */
    public static WebElement findByText(List<WebElement> elements, String text) {
        return elements.stream()
                .filter(e -> e.getText().trim().equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No element with text: " + text));
    }

    /** Checks if any element in the list has the given text. */
    public static boolean containsText(List<WebElement> elements, String text) {
        return elements.stream().anyMatch(e -> e.getText().trim().equalsIgnoreCase(text));
    }

    /** Waits for a stale element to refresh and returns the new reference. */
    public static WebElement refreshElement(WebDriver driver, By locator) {
        int timeout = ConfigReader.getInstance().getTimeout();
        return new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /** Highlights an element with a red border for debugging. */
    public static void highlight(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].style.border='3px solid red'", element);
    }

    /** Sets value via JavaScript (useful for read-only or custom inputs). */
    public static void setValueByJs(WebDriver driver, WebElement element, String value) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].value=arguments[1];", element, value);
        log.debug("Set value '{}' via JS", value);
    }

    /** Triggers a change event via JavaScript (for React/Angular inputs). */
    public static void triggerChangeEvent(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].dispatchEvent(new Event('change'));", element);
    }
}
