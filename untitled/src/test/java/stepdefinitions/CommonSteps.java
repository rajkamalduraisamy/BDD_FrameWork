package stepdefinitions;

import hooks.ScenarioContext;
import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.Assertions;
import pages.HomePage;
import pages.LoginPage;
import utilities.ConfigReader;
import utilities.ReportUtil;

/**
 * CommonSteps contains step definitions shared across multiple feature files.
 * Business logic is delegated to Page Objects.
 */
public class CommonSteps {

    private static final Logger log = LogManager.getLogger(CommonSteps.class);
    private final ScenarioContext context;
    private final LoginPage loginPage;
    private final HomePage homePage;

    public CommonSteps(ScenarioContext context) {
        this.context   = context;
        this.loginPage = new LoginPage(context.getDriver());
        this.homePage  = new HomePage(context.getDriver());
    }

    @Given("I am logged in as {string}")
    public void iAmLoggedInAs(String role) {
        ConfigReader config = ConfigReader.getInstance();
        String username, password;

        if (role.equalsIgnoreCase("admin")) {
            username = config.getAdminUsername();
            password = config.getAdminPassword();
        } else if (role.equalsIgnoreCase("employee")) {
            username = config.getEmployeeUsername();
            password = config.getEmployeePassword();
        } else {
            throw new IllegalArgumentException("Unknown role: '" + role + "'. Use 'admin' or 'employee'");
        }

        loginPage.login(username, password);
        Assertions.assertThat(homePage.isHomePageLoaded())
                .as("Home page should load after " + role + " login")
                .isTrue();
        ReportUtil.logInfo("Logged in as " + role + " successfully");
        log.info("Login verified for role: {}", role);
    }

    @Then("the Home page should be displayed")
    public void theHomePageShouldBeDisplayed() {
        Assertions.assertThat(homePage.isHomePageLoaded())
                .as("Home page should be displayed after login")
                .isTrue();
        ReportUtil.logPass("Home page displayed successfully");
    }

}
