package messager.network;

import messager.requests.Request;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;

public class ClientServer {

    private static ClientServer instance;
    private final ClientXML client = new ClientXML();
    private final Server server = new Server();
    private final Queue<Runnable> tasks = new ArrayDeque<>();

    private ClientServer() {
        Thread thread = new Thread(this::runTask);
        thread.setDaemon(true);
        thread.start();
    }

    public static ClientServer instance() {
        if (instance == null) {
            instance = new ClientServer();
        }
        return instance;
    }

    public static void runLater(Runnable runnable) {
        instance().runLater(runnable, true);
    }

    private synchronized void runLater(Runnable runnable, boolean b) {
        tasks.add(runnable);
        notify();
    }

    private synchronized void runTask() {
        while (true) {
            if (!tasks.isEmpty()) {
                Runnable runnable = tasks.poll();
                runnable.run();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    public <T, R> void tryPostAndAccept(T request, Class<R> responseClass, Consumer<R> onResponse) {
        runLater(() -> tryPostAndAccept(request, responseClass).ifPresent(onResponse));
    }

    public <T, R> void postAndAcceptSilent(T request, Class<R> responseClass, Consumer<R> onResponse) {
        runLater(() -> postAndAcceptSilent(request, responseClass).ifPresent(onResponse));
    }

    private <T> Optional<T> tryPostAndAccept(Object postObject, Class<T> acceptClass) {
        if (!client.tryPost(postObject)) {
            return Optional.empty();
        }
        return server.tryAccept(acceptClass);
    }

    private <T> Optional<T> postAndAcceptSilent(Object postObject, Class<T> acceptClass) {
        if (client.postSilent(postObject)) {
            return server.acceptSilent(acceptClass);
        } else {
            return Optional.empty();
        }
    }

    public void tryPost(Request request, Consumer<Boolean> onPost) {
        runLater(() -> onPost.accept(client.tryPost(request)));
    }

}
