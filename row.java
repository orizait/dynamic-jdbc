import java.util.HashMap;

public class row
{
    private HashMap<String,cell> data;

    public row(HashMap<String, cell> data) {
        this.data = data;
    }

    public row() {
        this.data = new HashMap<String,cell>();
    }

    public Object get(String col_name)
    {
        return data.get(col_name).getVal();
    }

    public HashMap<String,cell> get()
    {
        return data;
    }

    public cell get_cell(String col_name)
    {
        return data.get(col_name);
    }

    public int get_type(String col_name)
    {
        return data.get(col_name).getType();
    }

    public boolean containsKey(String col_name)
    {
        return data.containsKey(col_name);
    }

    public void add(String col_name ,cell cell_val)
    {
        data.put(col_name,cell_val);
    }
}
