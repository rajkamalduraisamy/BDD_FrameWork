package utilities;

import constants.AppConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

/**
 * DownloadUtil polls the download directory for a newly downloaded file.
 * No Thread.sleep() — uses polling with Instant-based timeout.
 */
public class DownloadUtil {

    private static final Logger log = LogManager.getLogger(DownloadUtil.class);

    private DownloadUtil() {}

    /**
     * Waits until a file matching the given extension appears in the download folder.
     *
     * @param extension file extension to look for (e.g. ".xlsx")
     * @return absolute path of the downloaded file
     * @throws RuntimeException if file is not found within timeout
     */
    public static String waitForDownload(String extension) {
        String downloadDir = ConfigReader.getInstance().getDownloadPath();
        int timeoutSec     = AppConstants.DOWNLOAD_WAIT_SEC;
        return waitForDownload(downloadDir, extension, timeoutSec);
    }

    public static String waitForDownload(String downloadDir, String extension, int timeoutSec) {
        Path dir = Paths.get(downloadDir);
        Instant deadline = Instant.now().plus(Duration.ofSeconds(timeoutSec));

        log.info("Waiting up to {}s for '{}' file in: {}", timeoutSec, extension, downloadDir);

        while (Instant.now().isBefore(deadline)) {
            Optional<File> file = findLatestFile(dir, extension);
            if (file.isPresent() && isDownloadComplete(file.get())) {
                log.info("Download complete: {}", file.get().getAbsolutePath());
                return file.get().getAbsolutePath();
            }
            try { Thread.sleep(500); } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Download wait interrupted", ie);
            }
        }
        throw new RuntimeException(AppConstants.MSG_FILE_NOT_DOWNLOADED + " Extension: " + extension);
    }

    /**
     * Verifies that a file with the given name exists in the download directory.
     */
    public static boolean verifyFileDownloaded(String fileName) {
        String downloadDir = ConfigReader.getInstance().getDownloadPath();
        File file = new File(downloadDir, fileName);
        boolean exists = file.exists() && file.length() > 0;
        log.info("File '{}' downloaded: {}", fileName, exists);
        return exists;
    }

    /**
     * Returns the most recently modified file with the given extension.
     */
    public static Optional<File> findLatestFile(Path directory, String extension) {
        File dir = directory.toFile();
        if (!dir.exists() || !dir.isDirectory()) return Optional.empty();
        File[] files = dir.listFiles(f -> f.getName().endsWith(extension)
                && !f.getName().endsWith(".crdownload")
                && !f.getName().endsWith(".tmp"));
        if (files == null || files.length == 0) return Optional.empty();
        return Arrays.stream(files)
                .max(Comparator.comparingLong(File::lastModified));
    }

    /** A download is complete when the file exists, is non-empty, and has no temp extension. */
    private static boolean isDownloadComplete(File file) {
        return file.exists() && file.length() > 0
                && !file.getName().endsWith(".crdownload")
                && !file.getName().endsWith(".tmp");
    }

    /** Clears all files in the download directory before a test run. */
    public static void clearDownloadDirectory() {
        String downloadDir = ConfigReader.getInstance().getDownloadPath();
        File dir = new File(downloadDir);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        boolean deleted = f.delete();
                        log.debug("Deleted file {}: {}", f.getName(), deleted);
                    }
                }
            }
        }
        log.info("Download directory cleared: {}", downloadDir);
    }
}
