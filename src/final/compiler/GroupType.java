import java.util.List;

public class GroupType extends Type {
    private Type subType;
    private List<Symbol> symbols;

    public Type getSubType() {
        return subType;
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public GroupType() {
        super("group");
    }

    public GroupType(Type subType, List<Symbol> symbols) {
        super("group");
        this.subType = subType;
        this.symbols = symbols;
    }

    public boolean isNumeric() {
        return false;
    }

}