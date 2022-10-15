public class BooleanType extends Type{

    public BooleanType() {
        super("boolean");
    }

    public boolean isNumeric() {
        return false;
    }
    
}
