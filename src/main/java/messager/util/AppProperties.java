package messager.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.IllegalFormatException;
import java.util.Properties;
import java.util.UnknownFormatConversionException;

public final class AppProperties {

    private static final String PROPS_PATH = "properties.prop";
    private static AppProperties appProperties;
    private final Properties properties = new Properties();

    private AppProperties() {
        try (FileInputStream fis = new FileInputStream(PROPS_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static AppProperties instance() {
        if (appProperties == null) {
            appProperties = new AppProperties();
        }
        return appProperties;
    }

    public static boolean getShowXml() {
        return instance().getBoolean("showXml");
    }

    String getString(String name) {
        return properties.getProperty(name);
    }

    boolean getBoolean(String name) throws IllegalFormatException {
        String property = properties.getProperty(name);
        if (property.equals("true")) {
            return true;
        }
        if (property.equals("false")) {
            return false;
        }
        throw new UnknownFormatConversionException(name + " = " + property);
    }

}
