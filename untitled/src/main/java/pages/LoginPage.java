package pages;

import base.BasePage;
import constants.AppConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage encapsulates all login-related interactions.
 * Locators use @FindBy — no hardcoded strings in methods.
 */
public class LoginPage extends BasePage {

    private static final Logger log = LogManager.getLogger(LoginPage.class);

    // ── Locators ──────────────────────────────────────────────────────────────
    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "loginBtn")
    private WebElement loginButton;

    @FindBy(css = ".error-message, .alert-danger")
    private WebElement errorMessage;

    @FindBy(css = ".login-logo, .brand-logo")
    private WebElement loginLogo;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Performs a full login with the given credentials.
     * Waits for the login form to be visible before interacting.
     */
    public void login(String username, String password) {
        log.info("Logging in as: {}", username);
        waitForVisible(usernameField);
        enterText(usernameField, username);
        enterText(passwordField, password);
        click(loginButton);
        log.info("Login form submitted");
    }

    /** Convenience method using credentials from config.properties. */
    public void loginAsAdmin() {
        String username = utilities.ConfigReader.getInstance().getAdminUsername();
        String password = utilities.ConfigReader.getInstance().getAdminPassword();
        login(username, password);
    }

    public boolean isLoginPageDisplayed() {
        return isElementVisible(loginLogo);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isElementVisible(errorMessage);
    }
}
