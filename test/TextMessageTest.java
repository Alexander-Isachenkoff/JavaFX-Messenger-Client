import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextMessageTest {

    @Test
    void testToString() {
        TextMessage message = new TextMessage("Me", "Hello, World!");
        String expected =
                "TextMessage\n" +
                "{\n" +
                "    name = 'Me',\n" +
                "    message = 'Hello, World!'\n" +
                "}";
        assertEquals(expected, message.toString());
    }
}