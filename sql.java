import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class sql
{
    public static select_array querey_to_select_array(ResultSet rs) throws SQLException {
        ArrayList<row> arr = new ArrayList<row>();
        ResultSetMetaData rsmd = rs.getMetaData();
        Logger log = LoggerFactory.getLogger(util.class);
        int i;
        while(rs.next())
        {
            HashMap<String,cell> cur_row=new HashMap<String,cell>();
            for(i=1;i<=rsmd.getColumnCount();i++)
            {
                String name=rsmd.getColumnName(i);
                int sqlTypes =rsmd.getColumnType(i);

                switch (sqlTypes)
                {
                    case Types.VARCHAR:
                        cur_row.put(name.toLowerCase(),new cell(1,rs.getString(name)));

                        break;
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                        String str_val=rs.getString(name);
                        if(str_val!=null && rs.getString(name).contains("."))
                        {
                            str_val=str_val.substring(0,str_val.lastIndexOf('.'));
                        }
                        cur_row.put(name.toLowerCase(),new cell(1,str_val));
                        break;

                    case Types.NULL:
                        cur_row.put(name.toLowerCase(),null);
                        break;
                    case Types.FLOAT:
                    case Types.DOUBLE:
                        double d_val = rs.getDouble(name);
                        if (rs.wasNull())
                        {
                            cur_row.put(name.toLowerCase(),new cell(4,sqlTypes));
                        }
                        else
                            cur_row.put(name.toLowerCase(),new cell(2,d_val));
                        break;

                    case Types.DECIMAL:
                    case Types.SMALLINT:
                    case Types.BIGINT:
                    case Types.INTEGER:
                    case Types.NUMERIC:
                        long l_val = rs.getLong(name);
                        if (rs.wasNull())
                        {
                            cur_row.put(name.toLowerCase(),new cell(4,sqlTypes));
                        }
                        else
                            cur_row.put(name.toLowerCase(),new cell(3,l_val));
                        break;
                }

            }
            arr.add(new row(cur_row));
        }
        return new select_array(arr);
    }

    public static select_array select(String query,Connection con)
    {
        Logger log = LoggerFactory.getLogger(util.class);
        try
        {
            try (Statement stmt = con.createStatement())
            {
                try (ResultSet rs = stmt.executeQuery(query))
                {
                    return querey_to_select_array(rs);
                }

            }
        }
        catch (SQLException e)
        {
            log.error("error sql.select:"+e.getMessage());
            return new select_array();
        }

    }

    public static int execute_update(String query,Connection con)
    {
        Logger log = LoggerFactory.getLogger(util.class);
        try
        {
            try (Statement stmt = con.createStatement())
            {
                return stmt.executeUpdate(query);
            }
        }
        catch (SQLException e)
        {
            log.error("error sql.select:"+e.getMessage());
            return -1;
        }

    }

    public static int execute_batch(String query,Connection con,select_array arr,String[] columns)
    {
        int[] updateCounts=new int[0];
        Logger log = LoggerFactory.getLogger(util.class);
        try
        {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(query))
            {
                int i;
                for (row cur_row:arr.get())
                {
                    i=1;
                    if(columns==null)
                    {
                        for (Map.Entry col : cur_row.get().entrySet())
                        {
                            int col_type=((cell)col.getValue()).getType();
                            Object val=((cell) col.getValue()).getVal();
                            switch (col_type)
                            {
                                case 1:
                                    ps.setString(i,(String)val);
                                    break;
                                case 2:
                                    ps.setDouble(i,(double)val);
                                    break;
                                case 3:
                                    ps.setLong(i,(long)val);
                                    break;
                                case 4:
                                    ps.setNull(i,(int)val);
                                    break;
                            }
                            i++;
                        }
                    }
                    else
                    {
                        for (int j=0;j<columns.length;j++)
                        {
                            int col_type=cur_row.get_type(columns[j]);
                            Object val=cur_row.get(columns[j]);
                            switch (col_type)
                            {
                                case 1:
                                    ps.setString(i,(String)val);
                                    break;
                                case 2:
                                    ps.setDouble(i,(double)val);
                                    break;
                                case 3:
                                    ps.setLong(i,(long)val);
                                    break;
                                case 4:
                                    ps.setNull(i,(int)val);
                                    break;
                            }
                            i++;
                        }
                    }

                    ps.addBatch();

                }
                updateCounts=ps.executeBatch();
                con.commit();
                return updateCounts.length;
            }
        }

        catch (SQLException e)
        {
            log.error("error in execute_batch:"+e.getMessage());
            return -1;
        }

    }
}
