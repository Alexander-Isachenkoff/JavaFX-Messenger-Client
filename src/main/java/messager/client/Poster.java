package messager.client;

public interface Poster {
    <T> void post(T object);
}
