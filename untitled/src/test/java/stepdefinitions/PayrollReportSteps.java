package stepdefinitions;

import constants.AppConstants;
import hooks.ScenarioContext;
import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import pages.HomePage;
import pages.PayrollReportPage;
import utilities.ConfigReader;
import utilities.ExcelUtil;
import utilities.ReportUtil;

/**
 * PayrollReportSteps implements all steps for the Payroll Report Generation feature.
 * All business logic is inside PayrollReportPage — steps are thin orchestrators.
 */
public class PayrollReportSteps {

    private static final Logger log = LogManager.getLogger(PayrollReportSteps.class);
    private final ScenarioContext context;
    private final HomePage homePage;
    private final PayrollReportPage reportPage;

    public PayrollReportSteps(ScenarioContext context) {
        this.context    = context;
        this.homePage   = new HomePage(context.getDriver());
        this.reportPage = new PayrollReportPage(context.getDriver());
    }

    @When("I navigate to Admin Payroll Reports")
    public void iNavigateToPayrollReports() {
        homePage.navigateToPayrollReports();
        org.assertj.core.api.Assertions.assertThat(reportPage.isReportPageLoaded())
                .as("Payroll Report page should load")
                .isTrue();
        ReportUtil.logInfo("Navigated to Payroll Reports page");
    }

    @When("I select report {string} for month {string} and download the report")
    public void iSelectReportAndDownload(String reportName, String month) {
        reportPage.selectReportName(reportName);
        reportPage.selectReportMonth(month);
        reportPage.selectIncludeAdjustmentsCheckbox();
        reportPage.clickExecute();
        reportPage.waitForReportToLoad();
        reportPage.clickDownloadIfVisible();
        String filePath = reportPage.waitForExcelDownload();
        context.setDownloadedFilePath(filePath);
        ReportUtil.logInfo("Report selected, executed and downloaded: " + filePath);
    }

    @Then("the report file should be downloaded")
    public void theReportFileShouldBeDownloaded() {
        String filePath = reportPage.waitForExcelDownload();
        context.setDownloadedFilePath(filePath);
        org.assertj.core.api.Assertions.assertThat(filePath)
                .as("Downloaded file path should not be empty")
                .isNotEmpty();
        ReportUtil.logPass("File downloaded: " + filePath);
        log.info("File downloaded to: {}", filePath);
    }

    @Then("the Excel report should contain exactly {int} data row(s)")
    public void theExcelShouldContainExactlyRows(int expectedRows) {
        String filePath = context.getDownloadedFilePath();
        int actualRows  = ExcelUtil.getDataRowCount(filePath);
        org.assertj.core.api.Assertions.assertThat(actualRows)
                .as(AppConstants.MSG_ROW_COUNT_MISMATCH + " Expected: " + expectedRows + ", Actual: " + actualRows)
                .isEqualTo(expectedRows);
        ReportUtil.logPass("Excel row count verified: " + actualRows);
        log.info("Excel row count: expected={}, actual={}", expectedRows, actualRows);
    }

    @When("I execute and download the payroll report")
    public void iExecuteAndDownloadReport() {
        ConfigReader config = ConfigReader.getInstance();
        String filePath = reportPage.executeAndDownloadReport(
                config.getReportName(), config.getReportMonth());
        context.setDownloadedFilePath(filePath);
        ReportUtil.logInfo("Report executed and downloaded: " + filePath);
    }
}
