package furniture;

import com.victorkzk.furniture.Furniture;

public class Cabinetry extends Furniture {

    public enum Type {
        CLOSET, CUPBOARD, HUTCH, PANTRY, KITCHEN, HOOSIER
    }

    private Type type;

    public Cabinetry() {

    }

    public Cabinetry(int length, int width, int height) {
        super(length, width, height);
        super.name = "Cabinetry";
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