import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    @Test
    void postTest() {
        Client client = new Client();
        TextMessage message = new TextMessage("Me", "Hello, World!");
        client.postTextMessage(message);
    }

}