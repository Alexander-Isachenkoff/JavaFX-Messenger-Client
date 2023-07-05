package messager.client;

import messager.entities.TextMessage;
import messager.entities.User;
import org.junit.jupiter.api.Test;

public class ClientSerializableTest {

    @Test
    void postTest() {
        ClientSerializable client = new ClientSerializable();
        TextMessage message = new TextMessage(new User("Me", "123456"), "Hello, World!");
        client.post(message);
    }

}