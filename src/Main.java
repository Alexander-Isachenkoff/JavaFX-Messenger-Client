import entities.TextMessage;
import entities.User;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Client client = new ClientXML();
        postMessages(client);
    }

    private static void postMessages(Poster poster) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Имя: ");
        String name = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        User user = new User(name, password);

        poster.post(user);

        System.out.println("Сообщения:");
        while (true) {
            String s = scanner.nextLine();
            if (s.trim().isEmpty()) {
                break;
            }
            TextMessage message = new TextMessage(user, s);
            poster.post(message);
        }
    }

}