package messager.util;

import messager.client.ClientXML;
import messager.requests.Request;
import messager.requests.TransferableObject;
import messager.response.CheckAccessResponse;
import messager.server.Server;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

public class NetworkUtil {

    public static List<String> getAllIp() {
        ArrayList<String> result = new ArrayList<>();
        Enumeration<NetworkInterface> nis;
        try {
            nis = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
        while (nis.hasMoreElements()) {
            NetworkInterface ni = nis.nextElement();
            Enumeration<InetAddress> ias = ni.getInetAddresses();
            while (ias.hasMoreElements()) {
                InetAddress ia = ias.nextElement();
                result.add(ia.getHostAddress());
            }
        }
        return result;
    }

    public static Optional<String> findServerIp() {
        ClientXML clientXML = new ClientXML();
        Server server = new Server();

        List<String> allIp = getAllIp();
        for (String ip : allIp) {
            try {
                clientXML.post(new Request("checkServerAccess", new TransferableObject()));
            } catch (JAXBException | IOException e) {
                continue;
            }
            try {
                CheckAccessResponse accept = server.accept(CheckAccessResponse.class);
                return Optional.of(ip);
            } catch (IOException ignored) {
            }
        }
        return Optional.empty();
    }
}
