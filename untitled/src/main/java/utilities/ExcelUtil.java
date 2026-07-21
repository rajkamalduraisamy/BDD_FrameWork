package utilities;

import constants.AppConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * ExcelUtil reads .xlsx files using Apache POI.
 * Provides row-level and cell-level access plus assertion helpers.
 */
public class ExcelUtil {

    private static final Logger log = LogManager.getLogger(ExcelUtil.class);

    private ExcelUtil() {}

    /**
     * Reads all data rows from the first sheet as a list of maps (header → value).
     *
     * @param filePath absolute path to the .xlsx file
     * @return list of row maps; excludes the header row
     */
    public static List<Map<String, String>> readAllRows(String filePath) {
        List<Map<String, String>> rows = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                log.warn("Excel file has no header row: {}", filePath);
                return rows;
            }

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValue(cell));
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowMap.put(headers.get(j), getCellValue(cell));
                }
                rows.add(rowMap);
            }
            log.info("Read {} data rows from: {}", rows.size(), filePath);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
        return rows;
    }

    /**
     * Returns the number of data rows (excluding header) in the first sheet.
     */
    public static int getDataRowCount(String filePath) {
        return readAllRows(filePath).size();
    }

    /**
     * Verifies that a row exists where the given column contains the expected value.
     */
    public static boolean rowExists(String filePath, String columnName, String expectedValue) {
        List<Map<String, String>> rows = readAllRows(filePath);
        boolean found = rows.stream()
                .anyMatch(row -> expectedValue.equalsIgnoreCase(row.get(columnName)));
        log.info("Row with {}={} found: {}", columnName, expectedValue, found);
        return found;
    }

    /**
     * Returns the value of a specific cell by row index (0-based, excluding header) and column name.
     */
    public static String getCellValue(String filePath, int rowIndex, String columnName) {
        List<Map<String, String>> rows = readAllRows(filePath);
        if (rowIndex >= rows.size()) {
            throw new RuntimeException("Row index " + rowIndex + " out of bounds. Total rows: " + rows.size());
        }
        return rows.get(rowIndex).getOrDefault(columnName, "");
    }

    /**
     * Reads test data from a named sheet (for data-driven tests).
     */
    public static List<Map<String, String>> readSheet(String filePath, String sheetName) {
        List<Map<String, String>> rows = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("Sheet not found: " + sheetName);

            Row headerRow = sheet.getRow(0);
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) headers.add(getCellValue(cell));

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowMap.put(headers.get(j), getCellValue(cell));
                }
                rows.add(rowMap);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read sheet '" + sheetName + "' from: " + filePath, e);
        }
        return rows;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }
}
