# Payroll Automation Framework

Enterprise-grade Selenium 4 + Cucumber BDD + TestNG automation framework for Payroll application testing.

---

## Technology Stack

| Tool | Version | Purpose |
|---|---|---|
| Java | 11 | Language |
| Selenium WebDriver | 4.18.1 | Browser automation |
| Cucumber BDD | 7.15.0 | BDD framework |
| TestNG | 7.9.0 | Test runner |
| Extent Reports | 5.1.1 | HTML reporting |
| WebDriverManager | 5.7.0 | Driver management |
| Apache POI | 5.2.5 | Excel read/write |
| Log4j2 | 2.22.1 | Logging |
| Jackson | 2.16.1 | JSON parsing |
| Lombok | 1.18.30 | Boilerplate reduction |
| AssertJ | 3.25.1 | Fluent assertions |
| Java Faker | 1.0.2 | Test data generation |

---

## Project Structure

```
payroll-automation-framework/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── base/
│   │   │   │   ├── DriverFactory.java       # ThreadLocal WebDriver, Chrome/Edge/Headless
│   │   │   │   └── BasePage.java            # All reusable Selenium actions
│   │   │   ├── pages/
│   │   │   │   ├── LoginPage.java
│   │   │   │   ├── HomePage.java
│   │   │   │   ├── PayrollPage.java
│   │   │   │   ├── PayrollReportPage.java
│   │   │   │   ├── EmployeeSearchPage.java
│   │   │   │   └── AdjustmentFormPage.java
│   │   │   ├── utilities/
│   │   │   │   ├── ConfigReader.java        # Singleton config loader
│   │   │   │   ├── WaitUtil.java            # Explicit wait helpers
│   │   │   │   ├── ScreenshotUtil.java      # Screenshot capture
│   │   │   │   ├── ExcelUtil.java           # Apache POI Excel reader
│   │   │   │   ├── DownloadUtil.java        # File download verifier
│   │   │   │   ├── JsonUtil.java            # Jackson JSON reader
│   │   │   │   ├── ElementUtil.java         # Element interaction helpers
│   │   │   │   ├── ReportUtil.java          # Extent Reports manager
│   │   │   │   └── CommonActions.java       # Shared multi-step workflows
│   │   │   ├── constants/
│   │   │   │   └── AppConstants.java        # All constants, no magic strings
│   │   │   └── listeners/
│   │   │       ├── TestNGListener.java      # TestNG lifecycle → Extent Reports
│   │   │       └── RetryAnalyzer.java       # Auto-retry on failure
│   │   └── resources/
│   │       ├── config.properties            # All configuration
│   │       └── log4j2.xml                   # Logging configuration
│   └── test/
│       ├── java/
│       │   ├── hooks/
│       │   │   ├── Hooks.java               # @Before @After @BeforeStep @AfterStep
│       │   │   └── ScenarioContext.java     # PicoContainer DI shared state
│       │   ├── stepdefinitions/
│       │   │   ├── CommonSteps.java         # Login, logout, screenshot steps
│       │   │   ├── PayrollReportSteps.java  # Scenario 1 steps
│       │   │   └── BenefitAdjustmentSteps.java # Scenario 2 steps
│       │   └── runner/
│       │       └── TestRunner.java          # Cucumber + TestNG runner
│       └── resources/
│           ├── features/
│           │   ├── PayrollReport.feature    # Scenario 1 + Outline
│           │   └── BenefitAdjustment.feature # Scenario 2 + Outline
│           ├── testdata/
│           │   └── testdata.json
│           ├── extent.properties
│           └── cucumber.properties
├── reports/                                 # Generated at runtime
├── downloads/                               # Downloaded files
├── testng.xml                               # TestNG suite config
├── pom.xml
├── .gitignore
└── .github/workflows/ci.yml                # GitHub Actions CI
```

---

## Prerequisites

- Java 11+ installed (`java -version`)
- Maven 3.8+ installed (`mvn -version`)
- Chrome or Edge browser installed
- Internet access for WebDriverManager to download drivers

---

## Configuration

Edit `src/main/resources/config.properties` before running:

```properties
app.url=https://your-application-url.com
browser=chrome                    # chrome | edge | headless
admin.username=admin@company.com
admin.password=Admin@123
timeout=20
download.path=downloads
```

All values can be overridden via Maven system properties (see below).

---

## How to Execute

### Run all tests
```bash
mvn clean test
```

### Run smoke tests only
```bash
mvn clean test -Dcucumber.filter.tags="@smoke"
```

### Run on Edge browser
```bash
mvn clean test -Dbrowser=edge
```

### Run headless (CI/CD)
```bash
mvn clean test -Dbrowser=headless
```

### Run specific feature
```bash
mvn clean test -Dcucumber.filter.tags="@payroll and @download"
```

### Run with parallel threads
Parallel execution is configured in `testng.xml` (`thread-count="3"`).
The `@DataProvider(parallel = true)` in `TestRunner` enables concurrent scenarios.

### Override config via CLI
```bash
mvn clean test \
  -Dbrowser=headless \
  -Dapp.url=https://staging.app.com \
  -Dadmin.username=testuser \
  -Dadmin.password=TestPass123
```

---

## Reports

After execution, reports are generated in the `reports/` directory:

| Report | Path |
|---|---|
| Extent HTML Report | `reports/ExtentReport.html` |
| Cucumber HTML Report | `reports/cucumber-html-report/index.html` |
| Cucumber JSON Report | `reports/cucumber-json-report/cucumber.json` |
| JUnit XML Report | `reports/cucumber-junit-report/cucumber.xml` |
| Log file | `reports/logs/automation.log` |
| Screenshots | `reports/screenshots/` |

Open `reports/ExtentReport.html` in any browser to view the full execution report with screenshots.

---

## Design Patterns

| Pattern | Implementation |
|---|---|
| Page Object Model | All `pages/` classes |
| Driver Factory | `DriverFactory` with `ThreadLocal<WebDriver>` |
| Dependency Injection | PicoContainer via `ScenarioContext` |
| Singleton | `ConfigReader`, `ReportUtil` |
| Base Page | `BasePage` — all reusable Selenium actions |
| Data Driven | `Scenario Outline` + `ExcelUtil` + `JsonUtil` |
| Retry Analyzer | `RetryAnalyzer` — configurable retry count |
| Soft Assertions | AssertJ `SoftAssertions` in verification steps |

---

## Importing into IDE

### IntelliJ IDEA
1. File → Open → select the `untitled` folder
2. Maven auto-imports dependencies
3. Enable annotation processing: Settings → Build → Compiler → Annotation Processors → Enable

### Eclipse
1. File → Import → Existing Maven Projects
2. Select the `untitled` folder
3. Install Lombok plugin if not present

---

## Adding New Tests

1. Add locators to the relevant Page Object using `@FindBy`
2. Add business logic methods to the Page Object
3. Write the `.feature` file in `src/test/resources/features/`
4. Implement step definitions in `src/test/java/stepdefinitions/`
5. Tag scenarios with `@smoke` or `@regression`
6. Run with `mvn clean test`

---

## CI/CD

GitHub Actions workflow is at `.github/workflows/ci.yml`.
It runs on every push to `main`/`develop` and uploads reports as artifacts.
