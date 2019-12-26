public class cell
{
    private int type;
    private Object  val;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }

    public cell(int type, Object val)
    {
        this.type = type;
        this.val = val;
    }
}

