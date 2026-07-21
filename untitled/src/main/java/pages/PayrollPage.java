package pages;

import base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * PayrollPage represents the Payroll section landing page.
 */
public class PayrollPage extends BasePage {

    private static final Logger log = LogManager.getLogger(PayrollPage.class);

    // ── Locators ──────────────────────────────────────────────────────────────
    @FindBy(css = "h2.payroll-title, .payroll-header")
    private WebElement payrollHeader;

    @FindBy(css = "a[href*='payroll-reports'], .payroll-reports-link")
    private WebElement payrollReportsLink;

    @FindBy(css = "a[href*='payroll-run'], .payroll-run-link")
    private WebElement payrollRunLink;

    public PayrollPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPayrollPageLoaded() {
        try {
            waitForVisible(payrollHeader);
            return true;
        } catch (Exception e) {
            log.error("Payroll page not loaded: {}", e.getMessage());
            return false;
        }
    }

    public void goToPayrollReports() {
        log.info("Clicking Payroll Reports link");
        click(payrollReportsLink);
    }
}
