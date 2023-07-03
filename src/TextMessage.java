import java.io.Serializable;

public class TextMessage implements Serializable {

    private String name;
    private String message;

    public TextMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("TextMessage\n" +
                "{\n" +
                "    name = '%s',\n" +
                "    message = '%s'\n" +
                "}", name, message);
    }

}