package datastructure.airplaneapplication;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account implements Factory
{
    private String id;
    private String userName;
    private String password;
    private String departureLoco;



    private String email;

    public Account()
    {
        this.id = null;
        this.userName = null;
        this.password = null;
        this.email = null;
        this.departureLoco = null;

    }

    public Account(String id, String userName, String password, String email,String departureLoco)
    {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.departureLoco =departureLoco;
    }

    @Override
    public  Factory createNewRecord(ResultSet set) throws SQLException {
        return new Account(set.getString(1),set.getString(2),set.getString(3),set.getString(4),set.getString(5));

    }


    public String getId() {
        return id;
    }

    public String getEmail(){return  email;}

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getDepartureLoco()
    {
        return departureLoco;
    }

    // Setters


    public void setId(String id) {
        this.id = id;
    }
    public void setDepartureLoco(String departureLoco)
    {
        this.departureLoco = departureLoco;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
