package furniture;

import com.victorkzk.furniture.Furniture;

public class Chair extends Furniture{

    public enum Type {
        ROCKING, WATCHMANS, WINDSOR, WINGBACK
    }

    private Type type;

    public Chair() {

    }

    public Chair(int length, int width, int height) {
        super(length, width, height);
        super.name = "Chair";
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
