package hooks;

import org.openqa.selenium.WebDriver;
import base.DriverFactory;

/**
 * ScenarioContext is injected via PicoContainer into all step definition classes.
 * It acts as a shared state container for a single scenario execution.
 * This enables dependency injection without static fields.
 */
public class ScenarioContext {

    // Shared data between step definitions within the same scenario
    private String downloadedFilePath;
    private String currentEmployeeNumber;
    private String currentMainBenefit;
    private String currentChildBenefit;

    public WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    public String getDownloadedFilePath() { return downloadedFilePath; }
    public void setDownloadedFilePath(String path) { this.downloadedFilePath = path; }

    public String getCurrentEmployeeNumber() { return currentEmployeeNumber; }
    public void setCurrentEmployeeNumber(String num) { this.currentEmployeeNumber = num; }

    public String getCurrentMainBenefit() { return currentMainBenefit; }
    public void setCurrentMainBenefit(String amount) { this.currentMainBenefit = amount; }

    public String getCurrentChildBenefit() { return currentChildBenefit; }
    public void setCurrentChildBenefit(String amount) { this.currentChildBenefit = amount; }
}
