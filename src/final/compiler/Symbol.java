public class Symbol {
    
    private String name;
    private Type type;
    private String value;

    public Symbol(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Symbol(Type type) {
        this.type = type;
    }

    public String internalName() {
        return this.name;
    }

    public String getName() {
        return this.value != null ? this.value : this.name;
    }

    public Type getType() {
        return this.type;
    }

    public void setValue(String v) {
        this.value = v;
    }

    public String getValue(){
        return this.value;
    }
    
}
