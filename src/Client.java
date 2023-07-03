import java.io.*;
import java.net.*;

public class Client {

    public static final String ADDRESS = "127.0.0.1";
    public static final int PORT = 11111;

    public void post(Object object) {
        try {
            Socket socket = new Socket(ADDRESS, PORT);
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            os.writeObject(object);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postTextMessage(TextMessage textMessage) {
        try {
            Socket socket = new Socket(ADDRESS, PORT);
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            os.writeObject(textMessage);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
