import java.util.HashMap;

public class SymbolTable {

    private HashMap<String,Symbol> table = new HashMap<String,Symbol>();
    
    public void addSymbol(String n, Symbol s) {

        table.put(n, s);
    }

    public Symbol getSymbol(String n) {
        return table.get(n);
    }

    public boolean exists(String n) {
        return table.containsKey(n);
    }
}
