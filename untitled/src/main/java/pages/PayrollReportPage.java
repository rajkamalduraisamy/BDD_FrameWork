package pages;

import base.BasePage;
import constants.AppConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utilities.DownloadUtil;

/**
 * PayrollReportPage handles report selection, execution, and download.
 */
public class PayrollReportPage extends BasePage {

    private static final Logger log = LogManager.getLogger(PayrollReportPage.class);

    // ── Locators ──────────────────────────────────────────────────────────────
    @FindBy(css = "h2.report-title, .payroll-report-header")
    private WebElement reportPageHeader;

    @FindBy(id = "reportNameDropdown")
    private WebElement reportNameDropdown;

    @FindBy(id = "reportMonthDropdown")
    private WebElement reportMonthDropdown;

    @FindBy(id = "includeAdjustmentsCheckbox")
    private WebElement includeAdjustmentsCheckbox;

    @FindBy(id = "executeReportBtn")
    private WebElement executeButton;

    @FindBy(css = ".report-loading-spinner, .loading-indicator")
    private WebElement loadingSpinner;

    @FindBy(id = "downloadReportBtn")
    private WebElement downloadButton;

    @FindBy(css = ".report-results-table, .report-data-container")
    private WebElement reportResultsContainer;

    @FindBy(css = ".report-status-message")
    private WebElement reportStatusMessage;

    private static final By LOADING_SPINNER_LOCATOR =
            By.cssSelector(".report-loading-spinner, .loading-indicator");
    private static final By DOWNLOAD_BTN_LOCATOR =
            By.id("downloadReportBtn");

    public PayrollReportPage(WebDriver driver) {
        super(driver);
    }

    public boolean isReportPageLoaded() {
        try {
            waitForVisible(reportPageHeader);
            return true;
        } catch (Exception e) {
            log.error("Payroll Report page not loaded: {}", e.getMessage());
            return false;
        }
    }

    public void selectReportName(String reportName) {
        log.info("Selecting report name: {}", reportName);
        selectDropdownByVisibleText(reportNameDropdown, reportName);
    }

    public void selectReportMonth(String month) {
        log.info("Selecting report month: {}", month);
        selectDropdownByVisibleText(reportMonthDropdown, month);
    }

    public void selectIncludeAdjustmentsCheckbox() {
        log.info("Selecting 'Include Adjustments' checkbox");
        selectCheckbox(includeAdjustmentsCheckbox);
    }

    public void clickExecute() {
        log.info("Clicking Execute button");
        click(executeButton);
    }

    /** Waits for the loading spinner to disappear, then waits for results. */
    public void waitForReportToLoad() {
        log.info("Waiting for report to load...");
        try {
            waitForInvisibility(LOADING_SPINNER_LOCATOR);
        } catch (Exception e) {
            log.debug("Spinner not found or already gone");
        }
        waitForVisible(reportResultsContainer);
        log.info("Report loaded successfully");
    }

    /**
     * Clicks Download if the button is visible; throws with a clear message otherwise.
     */
    public void clickDownloadIfVisible() {
        if (isElementPresent(DOWNLOAD_BTN_LOCATOR)) {
            waitForClickable(downloadButton);
            click(downloadButton);
            log.info("Download button clicked");
        } else {
            throw new RuntimeException(AppConstants.MSG_DOWNLOAD_BTN_NOT_FOUND);
        }
    }

    /**
     * Waits for the Excel file to appear in the downloads folder and returns its path.
     */
    public String waitForExcelDownload() {
        return DownloadUtil.waitForDownload(".xlsx");
    }

    /** Full report execution flow: select → checkbox → execute → wait → download. */
    public String executeAndDownloadReport(String reportName, String month) {
        selectReportName(reportName);
        selectReportMonth(month);
        selectIncludeAdjustmentsCheckbox();
        clickExecute();
        waitForReportToLoad();
        clickDownloadIfVisible();
        return waitForExcelDownload();
    }
}
