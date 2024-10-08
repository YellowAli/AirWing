package datastructure.airplaneapplication;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Factory
{
    public Factory createNewRecord(ResultSet set) throws SQLException;
}
