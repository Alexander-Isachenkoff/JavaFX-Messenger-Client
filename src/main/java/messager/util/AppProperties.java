package messager.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UnknownFormatConversionException;

public final class AppProperties {

    private static final String PROPS_PATH = "properties.prop";
    private static final Map<String, String> defaultValues;
    private static AppProperties appProperties;

    static {
        defaultValues = new HashMap<>();
        defaultValues.put("serverAddress", "127.0.0.1");
        defaultValues.put("showXml", "true");
        defaultValues.put("serverPort", "11111");
        defaultValues.put("connectionTimeOut", "5000");
        defaultValues.put("responseTimeOut", "5000");
    }

    private final Properties properties = new Properties();

    private AppProperties() {
        try (FileInputStream fis = new FileInputStream(PROPS_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            createDefaultProperties();
        }
        restoreMissingProperties();
    }

    public static AppProperties instance() {
        if (appProperties == null) {
            appProperties = new AppProperties();
        }
        return appProperties;
    }

    private void restoreMissingProperties() {
        for (String key : defaultValues.keySet()) {
            String property = properties.getProperty(key);
            if (property == null) {
                restoreProperty(key);
            }
        }
    }

    public void setProperty(String name, String value) {
        properties.setProperty(name, value);
    }

    public void save() {
        try (FileOutputStream fos = new FileOutputStream(PROPS_PATH)) {
            properties.store(fos, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String restoreProperty(String name) {
        String defaultValue = defaultValues.get(name);
        properties.setProperty(name, defaultValue);
        save();
        return defaultValue;
    }

    private void createDefaultProperties() {
        defaultValues.forEach(properties::setProperty);
        save();
    }

    public String getString(String name) {
        String property = properties.getProperty(name);
        if (property == null) {
            return restoreProperty(name);
        }
        return property;
    }

    public int getInt(String name) {
        try {
            return Integer.parseInt(properties.getProperty(name));
        } catch (RuntimeException ex) {
            restoreProperty(name);
        }
        return getInt(name);
    }

    public boolean getBoolean(String name) throws UnknownFormatConversionException {
        String property = properties.getProperty(name);
        if ("true".equals(property)) {
            return true;
        }
        if ("false".equals(property)) {
            return false;
        }
        restoreProperty(name);
        return getBoolean(name);
    }

}
