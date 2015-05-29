package furniture;

import com.victorkzk.furniture.Furniture;

public class Desk extends Furniture{

    public enum Type {
        DAVENPORT, DRAWING, COMPUTER, WRITING
    }

    private Type type;

    public Desk(int length, int width, int height) {
        super(length, width, height);
        super.name = "Desk";
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
