public abstract class Type {

    public Type(String name) {
        assert name != null;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean isNumeric();

    @Override
    public String toString() {
        return name;
    }

    private final String name;

}
