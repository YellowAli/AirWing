package datastructure.airplaneapplication;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.*;

public class PlaneController implements MainTabs
{
    @FXML
    private TextField CruisingSpeed;

    @FXML
    private TextArea Dimension;

    @FXML
    private TextArea Performance;

    @FXML
    private TextField SearchBar;

    @FXML
    private ListView<Object> SearchList;

    @FXML
    private TextArea Type;

    @FXML
    private TextArea Weight;

    @FXML
    private TextField flightCrewCapacity;

    @FXML
    private TextField passengerCapacity;

    private String refOwner;


    private String key;

    private GeneralAdapter adapter;
    private GeneralAdapter newAdapter;
    private String tableName = "planeDB";
    private  String newTableName = "ownedPlanes";

    public PlaneController() {
        initDB();
    }

    private void initDB()
    {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String[] info = {
                        "ReferenceID VARCHAR(1000)", "Type VARCHAR(1000)", "Performance VARCHAR(1000)",
                        "Weight VARCHAR(1000)", "Dimension VARCHAR(1000)", "FlightCrewCapacity DOUBLE",
                        "PassengerCapacity DOUBLE", "CruisingSpeed DOUBLE"
                };
                adapter = new GeneralAdapter(false, tableName, info);

                ArrayList<Factory> lis = adapter.getAllRecords(new Plane());
                if(lis.isEmpty())
                {
                    FetchingPlaneInformation fetch = new FetchingPlaneInformation();
                    fetch.setAdapter(adapter);
                    fetch.setTableName(tableName);
                    fetch.findInfo();

                }

                return null;
            }
        };

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            System.err.println("Failed to initialize database: " + ex.getMessage());
            // Handle the exception (e.g., show alert to the user)
        });

        new Thread(task).start();
    }

    public void onSearch() throws SQLException
    {
        HashMap<String, Object> map = new HashMap<>();
        map.put("UPPER(ReferenceID) LIKE ","%"+SearchBar.getText().toUpperCase()+"%");

        ArrayList<Object> list = adapter.findOneColumnRestricted("ReferenceID",map);



        ObservableList<Object> observableList = FXCollections.observableArrayList(list);
        int LIST_CELL_HEIGHT = 26;

        SearchList.prefHeightProperty().bind(Bindings.size(observableList).multiply(LIST_CELL_HEIGHT));
        SearchList.getItems().setAll(observableList);
    }

    public void onClick() throws SQLException
    {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();

        ObservableList<Object> x = SearchList.getSelectionModel().getSelectedItems();
        Object[] xObject = x.toArray(new Object[0]);

        try
        {
            linkedHashMap.put("ReferenceID =",xObject[0].toString());

            Plane plane = new Plane();

            ArrayList<Factory> list = adapter.findRecords(linkedHashMap,plane);
            System.out.println(list.size());

            Type.setText(((Plane)list.getFirst()).getType());
            Dimension.setText(((Plane)list.getFirst()).getDimensions());
            Performance.setText(((Plane)list.getFirst()).getPerformance());
            Weight.setText(((Plane)list.getFirst()).getWeight());
            flightCrewCapacity.setText(Double.toString(((Plane)list.getFirst()).getFlightCrewCapacity()));
            passengerCapacity.setText(Double.toString(((Plane)list.getFirst()).getPassengerCapacity()));
            CruisingSpeed.setText(Double.toString(((Plane)list.getFirst()).getCruisingSpeed()));

            SearchBar.setText(xObject[0].toString());
            SearchList.getItems().clear();
        }
        catch (Exception ex)
        {

        }


    }

    public void buttonClicked() throws SQLException {
        String[] info = {
                "PlaneReferenceID VARCHAR(1000)", "Type VARCHAR(1000)", "Performance VARCHAR(1000)",
                "Weight VARCHAR(1000)", "Dimension VARCHAR(1000)", "FlightCrewCapacity DOUBLE",
                "PassengerCapacity DOUBLE", "CruisingSpeed DOUBLE","ReferenceID VARCHAR(30) NOT NULL REFERENCES Account(REFERENCEID) DEFAULT 'default_value'"
        };
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        newAdapter =new GeneralAdapter(false,newTableName,info);

        HashMap<String,Object> Hashmap = new HashMap<>();
        Hashmap.put("UPPER(PlaneReferenceID) LIKE ",SearchBar.getText().toUpperCase()+"%");
        ArrayList<Object> lis = newAdapter.findOneColumnRestricted("PlaneReferenceID",Hashmap);
        System.out.println(lis.size()+"so its here");


        map.put("PlaneReferenceID",returnKey(lis,SearchBar.getText()+"-") );
        map.put("Type", Type.getText());
        map.put("Performance", Performance.getText());
        map.put("Weight",Weight.getText());
        map.put("Dimension",Dimension.getText());
        map.put("FlightCrewCapacity",Double.parseDouble(flightCrewCapacity.getText()));
        map.put("PassengerCapacity",Double.parseDouble(passengerCapacity.getText()));
        map.put("CruisingSpeed",Double.parseDouble(CruisingSpeed.getText()));
        map.put("ReferenceID",refOwner);
        System.out.println(refOwner);
        newAdapter.addRecord(map);
    }

    public String returnKey(ArrayList<Object> lis,String planeModel)
    {
        String returnedTing;
        System.out.println(planeModel+"!!!!!");

        String j = "1";
        String missingKey = "z";
        int max = 0;
        ArrayList<String> listOfSameModelRefNumbersTaken = new ArrayList<>();
        for (int i = 0; i < lis.size(); i++) //lis is a list of all referenceIds with the same reference keyword before the number
        {
            String[] refSplit = ((String) lis.get(i)).split("-"); // splitting the entry from the database list (lis) at the '-' point for example:
            // AASI Jetcruzer-1 will be populated into an array as AASI Jetcruzer, 1

            String ref = refSplit[1]; //ref = the number in my above example that would be 1
            System.out.println(refSplit[0]+"found u bich");
            if(refSplit[0].concat("-").equals(planeModel)) // refSplit[0] or AASI Jetcruzer is equal to the inputted plane model
            //then the ref from earlier will be added to a list which holds all ref numbers in use for this specific plane model
                listOfSameModelRefNumbersTaken.add(ref);
        }
        for (int i = 0; i < listOfSameModelRefNumbersTaken.size(); i++)
        {

            if (!listOfSameModelRefNumbersTaken.contains(j)) //if listOfSameModelRefNumbersTaken does not contain the calculated missing key then that's the missing ref, the missing key is incremented in each iteration
            {
                System.out.println("u messing me up foo");
                missingKey = j;
                break;
            }


            if(max < Integer.parseInt(listOfSameModelRefNumbersTaken.get(i))) //finding the max ref
                max = Integer.parseInt(listOfSameModelRefNumbersTaken.get(i));
            System.out.println(max+"yerrr");
            int convert = Integer.parseInt(j);
            convert++;
            j = Integer.toString(convert);
        }

        if(!missingKey.equals("z"))
        {
            returnedTing = planeModel+ missingKey;
            System.out.println(returnedTing+"!!!!!!!@@@@");
            return returnedTing;
        }
        else
        {
            returnedTing = planeModel+ ++max;
            System.out.println(returnedTing+"!!!!!!!");
            return returnedTing;
        }



    }

    @Override
    public void setRefID(String refID)
    {
        this.refOwner = refID;

    }
}