import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PropertyTable {
    private HashMap<String, String> table;
    private static String[] argumentOrder = {"angle", "color", "position", "thickness"};

    public PropertyTable() {
        table = new HashMap<>();
    }

    public void put(String k, String val) {
        table.put(k, val);
    }

    public String get(String k) {
        return table.get(k);
    }

    public String getAll() {
        List<String> res = new ArrayList<>();
        for (String arg : argumentOrder) {
            if (table.getOrDefault(arg, null) != null)
                res.add("." + arg + " = " + table.get(arg));
        }

        return String.join(", ", res);
    }
    
}
