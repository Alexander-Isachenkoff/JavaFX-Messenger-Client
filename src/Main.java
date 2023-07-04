import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Client client = new ClientXML();
        postMessages(client);
    }

    private static void postMessages(Post post) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Имя: ");
        String name = scanner.nextLine();

        System.out.println("Сообщения:");
        while (true) {
            String s = scanner.nextLine();
            if (s.trim().isEmpty()) {
                break;
            }
            TextMessage message = new TextMessage(name, s);
            post.post(message);
        }
    }

}