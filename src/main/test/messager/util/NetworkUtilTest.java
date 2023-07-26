package messager.util;

import org.junit.jupiter.api.Test;

class NetworkUtilTest {

    @Test
    void getAllIp() {
        NetworkUtil.findServerIp().ifPresent(ip -> {
            System.out.println("serverIp = " + ip);
        });
    }

}