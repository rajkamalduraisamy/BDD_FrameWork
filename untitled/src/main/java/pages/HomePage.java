package pages;

import base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * HomePage represents the dashboard after successful login.
 * Provides navigation to all major sections.
 */
public class HomePage extends BasePage {

    private static final Logger log = LogManager.getLogger(HomePage.class);

    // ── Locators ──────────────────────────────────────────────────────────────
    @FindBy(css = ".dashboard-header, h1.welcome-title")
    private WebElement dashboardHeader;

    @FindBy(css = "nav a[href*='admin'], .menu-admin")
    private WebElement adminMenu;

    @FindBy(css = "a[href*='payroll'], .menu-payroll")
    private WebElement payrollMenu;

    @FindBy(css = "a[href*='reports'], .menu-reports")
    private WebElement reportsMenu;

    @FindBy(css = "a[href*='employee'], .menu-employee")
    private WebElement employeeMenu;

    @FindBy(css = ".logout-btn, a[href*='logout']")
    private WebElement logoutButton;

    @FindBy(css = ".user-profile, .logged-in-user")
    private WebElement userProfile;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isHomePageLoaded() {
        try {
            waitForVisible(dashboardHeader);
            log.info("Home page loaded. Title: {}", getPageTitle());
            return true;
        } catch (Exception e) {
            log.error("Home page did not load: {}", e.getMessage());
            return false;
        }
    }

    /** Navigates Admin → Payroll → Reports via top navigation. */
    public void navigateToPayrollReports() {
        log.info("Navigating to Admin > Payroll > Reports");
        click(adminMenu);
        click(payrollMenu);
        click(reportsMenu);
    }

    /** Navigates to Employee Search. */
    public void navigateToEmployeeSearch() {
        log.info("Navigating to Employee Search");
        click(employeeMenu);
    }

    public void logout() {
        log.info("Logging out");
        click(logoutButton);
    }

    public String getWelcomeText() {
        return getText(dashboardHeader);
    }
}
