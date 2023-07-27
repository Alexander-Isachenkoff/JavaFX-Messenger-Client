package messager.network;

import messager.view.AlertUtil;

import java.net.SocketTimeoutException;

abstract class Client {

    public boolean tryPost(Object object) {
        String errorText;
        try {
            post(object);
            return true;
        } catch (SocketTimeoutException e) {
            errorText = "Превышено время ожидания подключения к серверу!";
        } catch (Exception e) {
            e.printStackTrace();
            errorText = e.getMessage();
        }
        AlertUtil.showErrorAlert(errorText);
        return false;
    }

    public boolean postSilent(Object object) {
        try {
            post(object);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public abstract void post(Object object) throws Exception;

}
