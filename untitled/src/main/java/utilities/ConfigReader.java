package utilities;

import constants.AppConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton ConfigReader that loads config.properties once and provides
 * typed getters. System properties override file values (enables CLI overrides).
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private final Properties properties = new Properties();

    private ConfigReader() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) throw new RuntimeException("config.properties not found on classpath");
            properties.load(is);
            log.info("config.properties loaded successfully");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) instance = new ConfigReader();
        return instance;
    }

    /** Returns value from System property first, then config file. */
    public String get(String key) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) return sysProp.trim();
        String value = properties.getProperty(key);
        if (value == null) throw new RuntimeException("Missing config key: " + key);
        return value.trim();
    }

    public String get(String key, String defaultValue) {
        try { return get(key); } catch (RuntimeException e) { return defaultValue; }
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public int getInt(String key, int defaultValue) {
        try { return getInt(key); } catch (Exception e) { return defaultValue; }
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    // ── Convenience accessors ─────────────────────────────────────────────────

    public String getAppUrl()          { return get(AppConstants.APP_URL); }
    public String getBrowser()         { return get(AppConstants.BROWSER, AppConstants.CHROME); }
    public int    getTimeout()         { return getInt(AppConstants.TIMEOUT, AppConstants.DEFAULT_TIMEOUT); }
    public int    getPageLoadTimeout() { return getInt(AppConstants.PAGE_LOAD_TIMEOUT, 30); }
    public String getAdminUsername()   { return get(AppConstants.ADMIN_USERNAME); }
    public String getAdminPassword()   { return get(AppConstants.ADMIN_PASSWORD); }
    public String getEmployeeUsername(){ return get(AppConstants.EMPLOYEE_USERNAME); }
    public String getEmployeePassword(){ return get(AppConstants.EMPLOYEE_PASSWORD); }
    public String getDownloadPath()    { return get(AppConstants.DOWNLOAD_PATH); }
    public String getScreenshotPath()  { return get(AppConstants.SCREENSHOT_PATH); }
    public int    getRetryCount()      { return getInt(AppConstants.RETRY_COUNT, 1); }
    public String getEmployeeNumber()  { return get(AppConstants.EMPLOYEE_NUMBER); }
    public String getReportName()      { return get(AppConstants.REPORT_NAME); }
    public String getReportMonth()     { return get(AppConstants.REPORT_MONTH); }
    public String getMainBenefit()     { return get(AppConstants.MAIN_BENEFIT_AMOUNT); }
    public String getChildBenefit()    { return get(AppConstants.CHILD_BENEFIT_AMOUNT); }
}
