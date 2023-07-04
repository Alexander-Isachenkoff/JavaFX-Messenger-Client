import org.junit.jupiter.api.Test;

public class ClientSerializableTest {

    @Test
    void postTest() {
        ClientSerializable client = new ClientSerializable();
        TextMessage message = new TextMessage("Me", "Hello, World!");
        client.post(message);
    }

}