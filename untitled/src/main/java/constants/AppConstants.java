package constants;

/**
 * Centralized constants for the entire framework.
 * All locators, messages, timeouts, and config keys live here.
 */
public final class AppConstants {

    private AppConstants() {}

    // ── Config Keys ──────────────────────────────────────────────────────────
    public static final String APP_URL            = "app.url";
    public static final String BROWSER            = "browser";
    public static final String TIMEOUT            = "timeout";
    public static final String PAGE_LOAD_TIMEOUT  = "page.load.timeout";
    public static final String ADMIN_USERNAME     = "admin.username";
    public static final String ADMIN_PASSWORD     = "admin.password";
    public static final String EMPLOYEE_USERNAME  = "employee.username";
    public static final String EMPLOYEE_PASSWORD  = "employee.password";
    public static final String DOWNLOAD_PATH      = "download.path";
    public static final String SCREENSHOT_PATH    = "screenshot.path";
    public static final String EXTENT_REPORT_PATH = "extent.report.path";
    public static final String RETRY_COUNT        = "retry.count";
    public static final String CHROMEDRIVER_PATH  = "chromedriver.path";
    public static final String EDGEDRIVER_PATH    = "edgedriver.path";

    // ── Test Data Keys ────────────────────────────────────────────────────────
    public static final String EMPLOYEE_NUMBER      = "employee.number";
    public static final String REPORT_NAME          = "report.name";
    public static final String REPORT_MONTH         = "report.month";
    public static final String MAIN_BENEFIT_AMOUNT  = "main.benefit.amount";
    public static final String CHILD_BENEFIT_AMOUNT = "child.benefit.amount";

    // ── Browser Types ─────────────────────────────────────────────────────────
    public static final String CHROME   = "chrome";
    public static final String EDGE     = "edge";
    public static final String HEADLESS = "headless";

    // ── Wait Defaults ─────────────────────────────────────────────────────────
    public static final int DEFAULT_TIMEOUT     = 20;
    public static final int POLLING_INTERVAL_MS = 500;
    public static final int DOWNLOAD_WAIT_SEC   = 30;

    // ── Excel Column Headers ──────────────────────────────────────────────────
    public static final String COL_EMPLOYEE_NUMBER = "Employee Number";
    public static final String COL_MAIN_BENEFIT    = "Main Benefit";
    public static final String COL_CHILD_BENEFIT   = "Child Benefit";

    // ── Messages ──────────────────────────────────────────────────────────────
    public static final String MSG_DOWNLOAD_BTN_NOT_FOUND = "Download button is not visible. Report may not have loaded.";
    public static final String MSG_FILE_NOT_DOWNLOADED    = "Expected file was not downloaded within timeout.";
    public static final String MSG_ROW_COUNT_MISMATCH     = "Expected row count does not match actual row count in Excel.";
    public static final String MSG_VALUE_MISMATCH         = "Expected value does not match actual value in Excel report.";
    public static final String MSG_LOGIN_FAILED           = "Login failed. Home page was not loaded after login.";

    // ── Report Labels ─────────────────────────────────────────────────────────
    public static final String REPORT_TITLE       = "Payroll Automation Report";
    public static final String REPORT_NAME_LABEL  = "Automation Execution Report";
    public static final String SCREENSHOT_PASS    = "Screenshot on Pass";
    public static final String SCREENSHOT_FAIL    = "Screenshot on Failure";
}
