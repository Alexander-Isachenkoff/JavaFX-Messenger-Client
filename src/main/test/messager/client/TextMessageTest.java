package messager.client;

import messager.entities.TextMessage;
import messager.entities.User;
import org.junit.jupiter.api.Test;

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
        Assertions.assertEquals(expected, message.toString());
    }
}