package pages;

import base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * AdjustmentFormPage handles Form One benefit adjustment interactions.
 */
public class AdjustmentFormPage extends BasePage {

    private static final Logger log = LogManager.getLogger(AdjustmentFormPage.class);

    // ── Locators ──────────────────────────────────────────────────────────────
    @FindBy(css = "a.form-one-link, button#formOneBtn")
    private WebElement formOneLink;

    @FindBy(id = "goBtn")
    private WebElement goButton;

    @FindBy(id = "adjustmentTypeDropdown")
    private WebElement adjustmentTypeDropdown;

    @FindBy(id = "mainBenefitInput")
    private WebElement mainBenefitInput;

    @FindBy(id = "childBenefitInput")
    private WebElement childBenefitInput;

    @FindBy(id = "saveChangesBtn")
    private WebElement saveButton;

    @FindBy(css = ".success-toast, .save-success-message")
    private WebElement successMessage;

    @FindBy(css = "a.back-to-payroll, button#backToReportsBtn")
    private WebElement backToPayrollLink;

    @FindBy(css = "h2.form-title, .adjustment-form-header")
    private WebElement formHeader;

    public AdjustmentFormPage(WebDriver driver) {
        super(driver);
    }

    public boolean isFormLoaded() {
        try {
            waitForVisible(formHeader);
            return true;
        } catch (Exception e) {
            log.error("Adjustment Form not loaded: {}", e.getMessage());
            return false;
        }
    }

    public void openFormOne() {
        log.info("Opening Form One");
        click(formOneLink);
    }

    public void clickGo() {
        log.info("Clicking Go button");
        click(goButton);
    }

    public void selectAdjustmentType(String type) {
        log.info("Selecting adjustment type: {}", type);
        selectDropdownByVisibleText(adjustmentTypeDropdown, type);
    }

    public void enterMainBenefit(String amount) {
        log.info("Entering Main Benefit amount: {}", amount);
        clearAndType(mainBenefitInput, amount);
    }

    public void enterChildBenefit(String amount) {
        log.info("Entering Child Benefit amount: {}", amount);
        clearAndType(childBenefitInput, amount);
    }

    public void saveChanges() {
        log.info("Saving changes");
        click(saveButton);
        waitForVisible(successMessage);
        log.info("Changes saved. Message: {}", getText(successMessage));
    }

    public void navigateBackToPayrollReports() {
        log.info("Navigating back to Payroll Reports");
        click(backToPayrollLink);
    }

    /**
     * Full adjustment workflow: open form → go → select type → enter amounts → save.
     */
    public void performBenefitAdjustment(String adjustmentType, String mainBenefit, String childBenefit) {
        openFormOne();
        clickGo();
        selectAdjustmentType(adjustmentType);
        enterMainBenefit(mainBenefit);
        enterChildBenefit(childBenefit);
        saveChanges();
    }
}
