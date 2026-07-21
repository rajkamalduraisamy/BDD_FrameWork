package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtil captures screenshots and saves them to the configured path.
 */
public class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtil() {}

    /**
     * Captures a screenshot and returns the absolute file path.
     *
     * @param driver WebDriver instance
     * @param name   Descriptive name for the screenshot file
     * @return absolute path of saved screenshot, or empty string on failure
     */
    public static String capture(WebDriver driver, String name) {
        try {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String fileName  = sanitize(name) + "_" + timestamp + ".png";
            String dirPath   = ConfigReader.getInstance().getScreenshotPath();

            Path dir = Paths.get(dirPath);
            Files.createDirectories(dir);

            File srcFile  = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destPath = dir.resolve(fileName);
            Files.copy(srcFile.toPath(), destPath);

            log.info("Screenshot saved: {}", destPath.toAbsolutePath());
            return destPath.toAbsolutePath().toString();

        } catch (IOException e) {
            log.error("Failed to capture screenshot '{}': {}", name, e.getMessage());
            return "";
        }
    }

    /**
     * Returns screenshot as Base64 string (used for embedding in Extent Reports).
     */
    public static String captureAsBase64(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            log.error("Failed to capture Base64 screenshot: {}", e.getMessage());
            return "";
        }
    }

    private static String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9_\\-]", "_");
    }
}
