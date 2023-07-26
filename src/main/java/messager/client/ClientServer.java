package messager.client;

import javafx.application.Platform;
import messager.server.Server;

import java.util.Optional;
import java.util.function.Consumer;

public class ClientServer {

    private final ClientXML client = new ClientXML();
    private final Server server = new Server();

    public <T> Optional<T> tryPostAndAccept(Object postObject, Class<T> acceptClass) {
        if (!client.tryPost(postObject)) {
            return Optional.empty();
        }
        return server.tryAccept(acceptClass);
    }

    public <T, R> void tryPostAndAccept(T request, Class<R> responseClass, Consumer<R> onResponse) {
        new Thread(() -> {
            tryPostAndAccept(request, responseClass).ifPresent(r -> {
                Platform.runLater(() -> onResponse.accept(r));
            });
        }).start();
    }

}
