package messager.requests;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
public class TransferableObject {

    @XmlElement
    private Map<String, TransferableObject> paramsMap;
    @XmlElement
    private Map<String, String> stringMap;
    @XmlElement
    private Map<String, ObjectList> objectListMap;
    @XmlElement
    private Map<String, StringList> stringListMap;

    public void put(String name, int value) {
        put(name, String.valueOf(value));
    }

    public void put(String name, long value) {
        put(name, String.valueOf(value));
    }

    public void put(String name, double value) {
        put(name, String.valueOf(value));
    }

    public void put(String name, boolean value) {
        put(name, String.valueOf(value));
    }

    public void put(String name, String value) {
        if (stringMap == null) {
            stringMap = new HashMap<>();
        }
        stringMap.put(name, value);
    }

    public void put(String name, TransferableObject object) {
        if (paramsMap == null) {
            paramsMap = new HashMap<>();
        }
        paramsMap.put(name, object);
    }

    public TransferableObject getParams(String name) {
        return paramsMap.get(name);
    }

    public String getString(String param) {
        return stringMap.get(param);
    }

    public int getInt(String param) {
        return Integer.parseInt(stringMap.get(param));
    }

    public double getLong(String param) {
        return Double.parseDouble(stringMap.get(param));
    }

    public double getDouble(String param) {
        return Double.parseDouble(stringMap.get(param));
    }

    public boolean getBoolean(String param) {
        return Boolean.parseBoolean(stringMap.get(param));
    }

    public StringList getStringList(String name) {
        return stringListMap.get(name);
    }

    public ObjectList getObjectList(String name) {
        return objectListMap.get(name);
    }

    public void put(String name, StringList list) {
        if (stringListMap == null) {
            stringListMap = new HashMap<>();
        }
        stringListMap.put(name, list);
    }

    public void put(String name, ObjectList list) {
        if (objectListMap == null) {
            objectListMap = new HashMap<>();
        }
        objectListMap.put(name, list);
    }

}
