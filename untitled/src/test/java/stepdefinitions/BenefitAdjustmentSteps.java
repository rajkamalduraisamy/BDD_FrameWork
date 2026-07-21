package stepdefinitions;

import constants.AppConstants;
import hooks.ScenarioContext;
import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import pages.AdjustmentFormPage;
import pages.EmployeeSearchPage;
import pages.HomePage;
import utilities.ConfigReader;
import utilities.ExcelUtil;
import utilities.ReportUtil;

/**
 * BenefitAdjustmentSteps implements all steps for the Payroll Benefit Adjustment feature.
 */
public class BenefitAdjustmentSteps {

    private static final Logger log = LogManager.getLogger(BenefitAdjustmentSteps.class);
    private final ScenarioContext context;
    private final HomePage homePage;
    private final EmployeeSearchPage searchPage;
    private final AdjustmentFormPage adjustmentPage;

    public BenefitAdjustmentSteps(ScenarioContext context) {
        this.context        = context;
        this.homePage       = new HomePage(context.getDriver());
        this.searchPage     = new EmployeeSearchPage(context.getDriver());
        this.adjustmentPage = new AdjustmentFormPage(context.getDriver());
    }

    @When("I navigate to Employee Search")
    public void iNavigateToEmployeeSearch() {
        homePage.navigateToEmployeeSearch();
        org.assertj.core.api.Assertions.assertThat(searchPage.isPageLoaded())
                .as("Employee Search page should load")
                .isTrue();
        ReportUtil.logInfo("Navigated to Employee Search");
    }

    @When("I search for employee number {string}")
    public void iSearchForEmployee(String employeeNumber) {
        context.setCurrentEmployeeNumber(employeeNumber);
        searchPage.searchEmployee(employeeNumber);
        ReportUtil.logInfo("Searched for employee: " + employeeNumber);
    }

    @When("I click View Form")
    public void iClickViewForm() {
        searchPage.clickViewForm();
        ReportUtil.logInfo("View Form clicked");
    }

    @When("I open Form One and click Go")
    public void iOpenFormOneAndClickGo() {
        adjustmentPage.openFormOne();
        adjustmentPage.clickGo();
        ReportUtil.logInfo("Form One opened and Go clicked");
    }

    @When("I select adjustment type {string}")
    public void iSelectAdjustmentType(String type) {
        adjustmentPage.selectAdjustmentType(type);
        ReportUtil.logInfo("Adjustment type selected: " + type);
    }

    @When("I enter Main Benefit amount {string}")
    public void iEnterMainBenefit(String amount) {
        context.setCurrentMainBenefit(amount);
        adjustmentPage.enterMainBenefit(amount);
        ReportUtil.logInfo("Main Benefit entered: " + amount);
    }

    @When("I enter Child Benefit amount {string}")
    public void iEnterChildBenefit(String amount) {
        context.setCurrentChildBenefit(amount);
        adjustmentPage.enterChildBenefit(amount);
        ReportUtil.logInfo("Child Benefit entered: " + amount);
    }

    @When("I save the benefit changes")
    public void iSaveBenefitChanges() {
        adjustmentPage.saveChanges();
        ReportUtil.logInfo("Benefit changes saved");
    }

    @When("I navigate back to Payroll Reports")
    public void iNavigateBackToPayrollReports() {
        adjustmentPage.navigateBackToPayrollReports();
        ReportUtil.logInfo("Navigated back to Payroll Reports");
    }

    @Then("the Excel report should contain the updated benefit values")
    public void theExcelShouldContainUpdatedBenefits() {
        String filePath    = context.getDownloadedFilePath();
        String empNumber   = context.getCurrentEmployeeNumber();
        String mainBenefit = context.getCurrentMainBenefit();
        String childBenefit = context.getCurrentChildBenefit();

        SoftAssertions soft = new SoftAssertions();

        soft.assertThat(ExcelUtil.rowExists(filePath, AppConstants.COL_EMPLOYEE_NUMBER, empNumber))
                .as("Employee number " + empNumber + " should exist in report")
                .isTrue();

        soft.assertThat(ExcelUtil.rowExists(filePath, AppConstants.COL_MAIN_BENEFIT, mainBenefit))
                .as("Main Benefit " + mainBenefit + " should be updated in report")
                .isTrue();

        soft.assertThat(ExcelUtil.rowExists(filePath, AppConstants.COL_CHILD_BENEFIT, childBenefit))
                .as("Child Benefit " + childBenefit + " should be updated in report")
                .isTrue();

        soft.assertAll();

        ReportUtil.logPass("All benefit values verified in Excel report");
        log.info("Benefit values verified: emp={}, main={}, child={}", empNumber, mainBenefit, childBenefit);
    }

    @Then("the Excel report should contain exactly {int} adjustment rows")
    public void theExcelShouldContainAdjustmentRows(int expectedRows) {
        String filePath = context.getDownloadedFilePath();
        int actualRows  = ExcelUtil.getDataRowCount(filePath);
        org.assertj.core.api.Assertions.assertThat(actualRows)
                .as("Expected " + expectedRows + " adjustment rows but found " + actualRows)
                .isEqualTo(expectedRows);
        ReportUtil.logPass("Adjustment row count verified: " + actualRows);
    }

    @When("I perform benefit adjustment with type {string} main {string} child {string}")
    public void iPerformBenefitAdjustment(String type, String main, String child) {
        context.setCurrentMainBenefit(main);
        context.setCurrentChildBenefit(child);
        adjustmentPage.performBenefitAdjustment(type, main, child);
        ReportUtil.logInfo("Benefit adjustment performed: type=" + type + ", main=" + main + ", child=" + child);
    }
}
