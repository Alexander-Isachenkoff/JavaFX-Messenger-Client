import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Имя: ");
        String name = scanner.nextLine();

        Client client = new Client();

        System.out.println("Сообщения:");
        while (true) {
            String s = scanner.nextLine();
            TextMessage message = new TextMessage(name, s);
            client.postTextMessage(message);
        }

    }

}