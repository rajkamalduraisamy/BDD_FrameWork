package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ConfigReader;
import utilities.ScreenshotUtil;

import java.time.Duration;
import java.util.List;

/**
 * BasePage provides all reusable Selenium actions.
 * Every Page Object extends this class.
 * No Thread.sleep() — explicit waits only.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final Actions actions;
    protected final JavascriptExecutor js;
    private static final Logger log = LogManager.getLogger(BasePage.class);

    protected BasePage(WebDriver driver) {
        this.driver  = driver;
        int timeout  = ConfigReader.getInstance().getTimeout();
        this.wait    = new WebDriverWait(driver, Duration.ofSeconds(timeout),
                                         Duration.ofMillis(500));
        this.actions = new Actions(driver);
        this.js      = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    // ── Click ─────────────────────────────────────────────────────────────────

    public void click(WebElement element) {
        try {
            waitForClickable(element);
            element.click();
            log.debug("Clicked element: {}", element);
        } catch (ElementClickInterceptedException e) {
            log.warn("Normal click intercepted, trying JS click");
            jsClick(element);
        }
    }

    public void jsClick(WebElement element) {
        js.executeScript("arguments[0].click();", element);
        log.debug("JS clicked element: {}", element);
    }

    // ── Text Input ────────────────────────────────────────────────────────────

    public void enterText(WebElement element, String text) {
        waitForVisible(element);
        element.clear();
        element.sendKeys(text);
        log.debug("Entered text '{}' into element: {}", text, element);
    }

    public void clearAndType(WebElement element, String text) {
        waitForVisible(element);
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(text);
        log.debug("Cleared and typed '{}' into element", text);
    }

    // ── Dropdown ──────────────────────────────────────────────────────────────

    public void selectDropdownByVisibleText(WebElement element, String text) {
        waitForVisible(element);
        new Select(element).selectByVisibleText(text);
        log.debug("Selected dropdown option by text: {}", text);
    }

    public void selectDropdownByValue(WebElement element, String value) {
        waitForVisible(element);
        new Select(element).selectByValue(value);
        log.debug("Selected dropdown option by value: {}", value);
    }

    public void selectDropdownByIndex(WebElement element, int index) {
        waitForVisible(element);
        new Select(element).selectByIndex(index);
        log.debug("Selected dropdown option by index: {}", index);
    }

    // ── Checkbox ──────────────────────────────────────────────────────────────

    public void selectCheckbox(WebElement checkbox) {
        waitForVisible(checkbox);
        if (!checkbox.isSelected()) {
            click(checkbox);
            log.debug("Checkbox selected");
        } else {
            log.debug("Checkbox was already selected");
        }
    }

    public void deselectCheckbox(WebElement checkbox) {
        waitForVisible(checkbox);
        if (checkbox.isSelected()) {
            click(checkbox);
            log.debug("Checkbox deselected");
        }
    }

    // ── Waits ─────────────────────────────────────────────────────────────────

    public WebElement waitForVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitForTextPresent(WebElement element, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    // ── Scroll ────────────────────────────────────────────────────────────────

    public void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", element);
        log.debug("Scrolled to element");
    }

    public void scrollToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }

    public void scrollToBottom() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    // ── Upload ────────────────────────────────────────────────────────────────

    public void upload(WebElement fileInput, String absoluteFilePath) {
        fileInput.sendKeys(absoluteFilePath);
        log.info("File uploaded: {}", absoluteFilePath);
    }

    // ── Alerts ────────────────────────────────────────────────────────────────

    public void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).accept();
        log.debug("Alert accepted");
    }

    public void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).dismiss();
        log.debug("Alert dismissed");
    }

    public String getAlertText() {
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }

    // ── Frames ────────────────────────────────────────────────────────────────

    public void switchToFrame(WebElement frameElement) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
        log.debug("Switched to frame");
    }

    public void switchToFrame(String nameOrId) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(nameOrId));
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    // ── Windows ───────────────────────────────────────────────────────────────

    public void switchToNewWindow() {
        String current = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(current)) {
                driver.switchTo().window(handle);
                log.debug("Switched to new window: {}", handle);
                return;
            }
        }
    }

    public void closeCurrentWindowAndSwitch(String parentHandle) {
        driver.close();
        driver.switchTo().window(parentHandle);
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public String getText(WebElement element) {
        waitForVisible(element);
        return element.getText().trim();
    }

    public String getAttributeValue(WebElement element, String attribute) {
        waitForVisible(element);
        return element.getAttribute(attribute);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public List<WebElement> getElements(By locator) {
        return driver.findElements(locator);
    }

    // ── Screenshot ────────────────────────────────────────────────────────────

    public String captureScreenshot(String name) {
        return ScreenshotUtil.capture(driver, name);
    }

    // ── Hover ─────────────────────────────────────────────────────────────────

    public void hoverOver(WebElement element) {
        actions.moveToElement(element).perform();
        log.debug("Hovered over element");
    }

    // ── Highlight (debug helper) ──────────────────────────────────────────────

    public void highlight(WebElement element) {
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }
}
