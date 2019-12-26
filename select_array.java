import java.util.ArrayList;
import java.util.Objects;

public class select_array
{
    private ArrayList<row> data;

    public select_array(ArrayList<row> data) {
        this.data = data;
    }

    public select_array() {
        this.data = new  ArrayList<row>();
    }

    public void add(row row_val)
    {
        data.add(row_val);
    }

    public void add_columns(select_array other,String[] to_add,String[] where)
    {
        ArrayList<row> other_data=other.get();
        boolean valid=false;
        int i=0;
        for(row cur_row:data)
        {
            if(i>other_data.size())
                break;
            row other_row=other_data.get(i);
            valid=true;
            for(int j=0;j<where.length;j++)
            {
                if(!(cur_row.containsKey(where[j])&& other_row.containsKey(where[j])&& Objects.equals(cur_row.get(where[j]), other_row.get(where[j]))))
                    valid=false;
            }
            if(valid)
            {
                for(String col_name:to_add)
                {
                    cur_row.add(col_name,other_row.get_cell(col_name));
                }
                i++;
            }
        }
    }

    public String select_array_to_string(String col_name)
    {
        String res="";
        int i=0;
        for(;i<data.size()-1;i++)
        {
            res+=data.get(i).get(col_name);
            res+=',';
        }
        res+=data.get(i).get(col_name);
        return res;
    }

    public row get(int i)
    {
        return data.get(i);
    }

    public ArrayList<row> get()
    {
        return data;
    }
}
