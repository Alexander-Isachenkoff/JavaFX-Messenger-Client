import entities.TextMessage;
import entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextMessageTest {

    @Test
    void testToString() {
        TextMessage message = new TextMessage(new User("Me", "123456"), "Hello, World!");
        String expected =
                "TextMessage\n" +
                        "{\n" +
                        "    name = 'Me',\n" +
                        "    message = 'Hello, World!'\n" +
                        "}";
        assertEquals(expected, message.toString());
    }
}