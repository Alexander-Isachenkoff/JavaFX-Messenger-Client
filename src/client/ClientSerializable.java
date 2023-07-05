package client;

import java.io.ObjectOutputStream;

public class ClientSerializable extends Client {

    @Override
    public <T> void post(T object) {
        try (ObjectOutputStream os = new ObjectOutputStream(getSocket().getOutputStream())) {
            os.writeObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
