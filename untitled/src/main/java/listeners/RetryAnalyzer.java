package listeners;

import constants.AppConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utilities.ConfigReader;

/**
 * RetryAnalyzer retries failed tests up to the configured retry count.
 * Attach via @Test(retryAnalyzer = RetryAnalyzer.class) or via TestNG listener.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger log = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;
    private final int maxRetry = ConfigReader.getInstance()
            .getInt(AppConstants.RETRY_COUNT, 1);

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetry) {
            retryCount++;
            log.warn("Retrying test '{}' — attempt {}/{}", result.getName(), retryCount, maxRetry);
            return true;
        }
        return false;
    }
}
