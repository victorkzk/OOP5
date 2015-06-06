package furniture;

import com.victorkzk.furniture.Furniture;

public class Bed extends Furniture {

    public enum Type {
        BUNK, WATERBED, MURPHY, STEIGH, CANOPY
    }

    private Type type;

    public Bed() {

    }

    public Bed(int length, int width, int height) {
        super(length, width, height);
        super.name = "Bed";
    }

    @Override
    public Object[] getTypesList() {
        Type[] types = Type.values();
        return types;
    }

    @Override
    public void setType(Object type) {
        this.type = (Type)type;
    }

    @Override
    public Object getType() {
        return type;
    }
}