package datastructure.airplaneapplication;

import java.sql.*;
import java.util.*;

public class GeneralAdapter
{
    private String DB_URL = "jdbc:derby://localhost:1527/AirplaneApplicationDB;create = true";
    private Connection connection = DriverManager.getConnection(DB_URL);
    private Statement stmt = connection.createStatement();
    private String tableName;
    private String[] info;

    public GeneralAdapter(Boolean reset, String tableName, String[] info) throws SQLException
    {

        if(reset)
        {
            String execute = "DROP TABLE "+tableName;
            stmt.execute(execute);
        }

        try
        {
            String execute = "CREATE TABLE "+tableName+"( "+info[0]+" NOT NULL PRIMARY KEY)";
            stmt.execute(execute);

            for(int i = 1; i< info.length;i++)
            {
                execute = "ALTER TABLE "+tableName+" ADD COLUMN "+info[i];
                System.out.println(execute);
                stmt.execute(execute);
                System.out.println("did it work?");
            }
        }
        catch(Exception ex)
        {
            System.out.println("Table Already Exists");
        }

        this.tableName = tableName;
        this.info = info;

    }

    public void addRecord(LinkedHashMap<String,Object> resource) throws SQLException {
        String executeStatement = "INSERT INTO "+tableName+" (";
        int i = 1;
        for(Map.Entry<String,Object> e: resource.entrySet())
        {
            if(i<resource.size())
                executeStatement=executeStatement+e.getKey()+", ";
            else
                executeStatement=executeStatement+e.getKey()+")";
            i++;
        }

        executeStatement =  executeStatement+" VALUES (";
        i=1;

        for(Map.Entry<String,Object> e: resource.entrySet())
        {
            if(i<resource.size())
                executeStatement=executeStatement+"?, ";
            else
                executeStatement=executeStatement+"?)";
            i++;
        }

        System.out.println(executeStatement);
        PreparedStatement preparedStatement = connection.prepareStatement(executeStatement);
        i=1;
        for(Map.Entry<String,Object> e: resource.entrySet())
        {
            if(e.getValue()==null)
                preparedStatement.setNull(i++,Types.VARCHAR);
            else
                preparedStatement.setObject(i++,e.getValue());
            System.out.println(i);
        }
        preparedStatement.execute();
        connection.commit();
    }


    public  <T extends Factory> ArrayList<T> findRecords(LinkedHashMap<String,Object> resource, T factory) throws SQLException {
        String executeStatement = "SELECT * FROM "+tableName+" WHERE ";
        int i = 1;
        for(Map.Entry<String,Object> e: resource.entrySet())
        {
            if(i<=resource.size())
                executeStatement=executeStatement+e.getKey()+"? ";
            i++;
        }
        System.out.println(executeStatement);
        PreparedStatement preparedStatement = connection.prepareStatement(executeStatement);
        i=1;
        for(Map.Entry<String,Object> e: resource.entrySet())
        {
            System.out.println(e);
            System.out.println("eeeeeeeeeeeee");
                preparedStatement.setObject(i++,e.getValue());
        }
        ResultSet set = preparedStatement.executeQuery();
        connection.commit();

        ArrayList<T> list = new ArrayList<>();

        while(set.next())
        {

            list.add((T) factory.createNewRecord(set));
        }
        return list;


    }

    public  <T extends Factory>ArrayList<T> getAllRecords ( T factory) throws SQLException {
        String executeStatement = "SELECT * FROM "+tableName;

        Statement stmnt = connection.createStatement();
        ResultSet set = stmnt.executeQuery(executeStatement);

        connection.commit();

        ArrayList<T> list = new ArrayList<>();

        while(set.next())
        {

            list.add((T)factory.createNewRecord(set));
        }
        return list;


    }

    public <T> ArrayList<T> findOneColumnRestricted(String select, HashMap<String,T> resource) throws SQLException
    {
        String executeStatement = "SELECT "+select+" FROM "+tableName+" WHERE ";
        int i = 1;
        for(Map.Entry<String,T> e: resource.entrySet())
        {
            if(i<=resource.size())
                executeStatement=executeStatement+e.getKey()+"? ";
            i++;
        }

        PreparedStatement preparedStatement = connection.prepareStatement(executeStatement);
        i=1;
        for(Map.Entry<String,T> e: resource.entrySet())
        {
            preparedStatement.setObject(i++,e.getValue());
        }
        ResultSet set = preparedStatement.executeQuery();

        ArrayList<T> list = new ArrayList<>();


        while(set.next())
        {

            list.add((T)set.getObject(1));
        }
        return list;

    }

    public <T> ArrayList<T> findOneColumn(String select,String orderBy) throws SQLException {
        String executeStatement = "SELECT "+select+" FROM "+tableName+" ORDER BY "+orderBy;

        ResultSet set = stmt.executeQuery(executeStatement);

        ArrayList<T> list = new ArrayList<>();

        while(set.next())
        {

            list.add((T)set.getObject(1));
        }
        return list;

    }

    public  void updateTable( LinkedHashMap<String,Object> resource) throws SQLException {
        String executeStatement = "UPDATE "+tableName+" SET ";
        int i = 1;
        for(Map.Entry<String,Object> e: resource.entrySet())
        {
            if(i<=resource.size())
                executeStatement=executeStatement+e.getKey()+" ? ";
            i++;
        }

        System.out.println(executeStatement);

        PreparedStatement preparedStatement = connection.prepareStatement(executeStatement);
        i=1;
        for(Map.Entry<String,Object> e: resource.entrySet())
        {
            System.out.println(i);
            preparedStatement.setObject(i++,e.getValue());
        }

        preparedStatement.executeUpdate();
        connection.commit();


    }



}
