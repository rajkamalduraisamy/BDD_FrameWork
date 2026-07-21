package utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * JsonUtil reads JSON files using Jackson ObjectMapper.
 */
public class JsonUtil {

    private static final Logger log = LogManager.getLogger(JsonUtil.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtil() {}

    /** Deserialises a JSON file into the given class type. */
    public static <T> T readJson(String filePath, Class<T> clazz) {
        try {
            T result = mapper.readValue(new File(filePath), clazz);
            log.info("JSON file read: {}", filePath);
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    /** Reads a JSON file from the classpath resources. */
    public static <T> T readJsonFromClasspath(String resourcePath, Class<T> clazz) {
        try (InputStream is = JsonUtil.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) throw new RuntimeException("Resource not found: " + resourcePath);
            return mapper.readValue(is, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON resource: " + resourcePath, e);
        }
    }

    /** Reads a JSON array file into a List of Maps. */
    public static List<Map<String, Object>> readJsonArray(String filePath) {
        try {
            return mapper.readValue(new File(filePath), new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON array: " + filePath, e);
        }
    }

    /** Serialises an object to a JSON string. */
    public static String toJson(Object obj) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialise object to JSON", e);
        }
    }
}
