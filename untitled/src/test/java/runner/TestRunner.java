package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * TestRunner wires Cucumber with TestNG.
 * Supports parallel execution via @DataProvider(parallel = true).
 *
 * Run with: mvn clean test
 * Run specific tag: mvn clean test -Dcucumber.filter.tags="@smoke"
 * Run specific browser: mvn clean test -Dbrowser=edge
 */
@CucumberOptions(
        features  = "src/test/resources/features",
        glue      = {"stepdefinitions", "hooks"},
        tags      = "@smoke or @regression",
        plugin    = {
                "pretty",
                "html:reports/cucumber-html-report/index.html",
                "json:reports/cucumber-json-report/cucumber.json",
                "junit:reports/cucumber-junit-report/cucumber.xml",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        publish    = false
)
public class TestRunner extends AbstractTestNGCucumberTests {

    /**
     * Setting parallel = true enables parallel scenario execution.
     * Thread count is controlled in testng.xml.
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
