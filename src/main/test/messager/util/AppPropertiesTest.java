package messager.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppPropertiesTest {

    @BeforeEach
    void setUp() {
        new File("properties.prop").delete();
    }

    @Test
    void getShowXml() {
        boolean showXml = AppProperties.instance().getBoolean("showXml");
        assertTrue(showXml);
    }

    @Test
    void getServerAddress() {
        String address = AppProperties.instance().getString("serverAddress");
        assertEquals("127.0.0.1", address);
    }

    @Test
    void getServerPort() {
        int port = AppProperties.instance().getInt("serverPort");
        assertEquals(11111, port);
    }

    @Test
    void getServerPort_invalid() {
        AppProperties.instance().setProperty("serverPort", "invalid");
        assertEquals("invalid", AppProperties.instance().getString("serverPort"));

        int port = AppProperties.instance().getInt("serverPort");
        assertEquals(11111, port);
    }

    @Test
    void getShowXml_invalid() {
        AppProperties.instance().setProperty("showXml", "invalid");
        assertEquals("invalid", AppProperties.instance().getString("showXml"));

        boolean showXml = AppProperties.instance().getBoolean("showXml");
        assertTrue(showXml);
    }

}