package base;

import constants.AppConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import utilities.ConfigReader;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * DriverFactory manages WebDriver lifecycle using ThreadLocal for parallel execution.
 * Supports Chrome only.
 */
public class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {}

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Initialises the WebDriver based on the browser specified in config or system property.
     */
    public static void initDriver() {
        ConfigReader config = ConfigReader.getInstance();
        String browser = System.getProperty("browser", config.getBrowser()).toLowerCase();
        String downloadPath = new File(config.getDownloadPath()).getAbsolutePath();
        int pageLoadTimeout = config.getPageLoadTimeout();
        int implicitWait = config.getTimeout();

        log.info("Initialising Chrome browser");
        WebDriver driver = createChromeDriver(downloadPath);

        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // use explicit waits only

        driverThreadLocal.set(driver);
        log.info("WebDriver initialised successfully for thread: {}", Thread.currentThread().getId());
    }

    private static WebDriver createChromeDriver(String downloadPath) {
        setChromeDriverPath();
        return new ChromeDriver(buildChromeOptions(downloadPath));
    }

    /**
     * Sets the chromedriver executable path via System property.
     * Path is read from config.properties key: chromedriver.path
     * Example value: drivers/chromedriver  (Mac/Linux) or drivers/chromedriver.exe (Windows)
     */
    private static void setChromeDriverPath() {
        String path = ConfigReader.getInstance().get(AppConstants.CHROMEDRIVER_PATH);
        System.setProperty("webdriver.chrome.driver", path);
        log.info("ChromeDriver path set to: {}", path);
    }

    private static ChromeOptions buildChromeOptions(String downloadPath) {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", buildDownloadPrefs(downloadPath));
        options.addArguments("--disable-notifications", "--no-sandbox",
                "--disable-dev-shm-usage", "--disable-popup-blocking",
                "--disable-extensions", "--remote-allow-origins=*");
        return options;
    }

    private static Map<String, Object> buildDownloadPrefs(String downloadPath) {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadPath);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true);
        prefs.put("plugins.always_open_pdf_externally", true);
        return prefs;
    }

    /** Quits the driver and removes it from ThreadLocal. */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                log.info("WebDriver quit for thread: {}", Thread.currentThread().getId());
            } catch (Exception e) {
                log.warn("Exception while quitting driver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
