package pages;

import base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * EmployeeSearchPage handles employee lookup and form navigation.
 */
public class EmployeeSearchPage extends BasePage {

    private static final Logger log = LogManager.getLogger(EmployeeSearchPage.class);

    // ── Locators ──────────────────────────────────────────────────────────────
    @FindBy(css = "h2.employee-search-title, .employee-search-header")
    private WebElement pageHeader;

    @FindBy(id = "employeeNumberInput")
    private WebElement employeeNumberInput;

    @FindBy(id = "searchBtn")
    private WebElement searchButton;

    @FindBy(css = ".employee-result-row, .search-result-item")
    private WebElement firstResultRow;

    @FindBy(css = "button.view-form-btn, a.view-form-link")
    private WebElement viewFormButton;

    @FindBy(css = ".no-results-message")
    private WebElement noResultsMessage;

    public EmployeeSearchPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        try {
            waitForVisible(pageHeader);
            return true;
        } catch (Exception e) {
            log.error("Employee Search page not loaded: {}", e.getMessage());
            return false;
        }
    }

    public void searchEmployee(String employeeNumber) {
        log.info("Searching for employee: {}", employeeNumber);
        enterText(employeeNumberInput, employeeNumber);
        click(searchButton);
        waitForVisible(firstResultRow);
    }

    public void clickViewForm() {
        log.info("Clicking View Form");
        click(viewFormButton);
    }

    public boolean isNoResultsDisplayed() {
        return isElementVisible(noResultsMessage);
    }
}
