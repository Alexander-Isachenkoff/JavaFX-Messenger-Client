package messager.network;

import messager.requests.Request;

import java.io.IOException;
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
        thread.start();
    }

    public static ClientServer instance() {
        if (instance == null) {
            instance = new ClientServer();
        }
        return instance;
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

    public synchronized <T, R> void tryPostAndAccept(T request, Class<R> responseClass, Consumer<R> onResponse) {
        tasks.add(() -> tryPostAndAccept(request, responseClass).ifPresent(onResponse));
        notify();
    }

    public synchronized <T, R> void postAndAcceptSilent(T request, Class<R> responseClass, Consumer<R> onResponse) {
        tasks.add(() -> postAndAcceptSilent(request, responseClass).ifPresent(onResponse));
        notify();
    }

    public synchronized <T, R> void postAndAccept(T request, Class<R> responseClass, Consumer<R> onResponse,
                                                  Consumer<Exception> handlePostException,
                                                  Consumer<IOException> handleAcceptIOException) {
        postAndAccept(client, request, responseClass, onResponse, handlePostException, handleAcceptIOException);
    }

    public synchronized <T, R> void postAndAccept(ClientXML clientXML, T request, Class<R> responseClass, Consumer<R> onResponse,
                                                  Consumer<Exception> handlePostException,
                                                  Consumer<IOException> handleAcceptIOException) {
        tasks.add(() -> {
            try {
                clientXML.post(request);
            } catch (Exception e) {
                handlePostException.accept(e);
                return;
            }
            R response;
            try {
                response = server.accept(responseClass);
            } catch (IOException e) {
                handleAcceptIOException.accept(e);
                return;
            }
            onResponse.accept(response);
        });
        notify();
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

    public synchronized void tryPost(Request request, Consumer<Boolean> onPost) {
        tasks.add(() -> onPost.accept(client.tryPost(request)));
        notify();
    }
}
