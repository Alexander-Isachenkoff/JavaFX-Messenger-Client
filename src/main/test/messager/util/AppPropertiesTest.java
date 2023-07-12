package messager.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppPropertiesTest {

    @Test
    void getShowXml() {
        boolean showXml = AppProperties.getShowXml();
        assertTrue(showXml);
    }

    @Test
    void getBoolean() {
        boolean showXml = AppProperties.instance().getBoolean("showXml");
        assertTrue(showXml);
    }

}