package datastructure.airplaneapplication;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import static datastructure.airplaneapplication.KeyCreation.returnKey;

public class ExtensionFlightController implements Initializable
{
    private AnchorPane root;
    private ModifyController modifyController;
    private boolean check1;
    Flight nextFlight;
    private Flight prevFlight;
    private String refIdOfReturningList;
    private String owner;
    private int versionOfSetInfo;
    private ObservableList<Flight> flightData = FXCollections.observableArrayList();


    private GeneralAdapter flightAdapter;
    private final String flightTableName = "Flights";
    private final String[] flightInfo = {
            "FLIGHTREFERENCEID VARCHAR(1000)", "DepartureLocation VARCHAR(1000)", "DateOfDeparture TIMESTAMP",
            "ArrivalLocation VARCHAR(1000)", "Frequency VARCHAR(1000)",
            "Plane VARCHAR(1000) NOT NULL REFERENCES OWNEDPLANES(PlaneReferenceID) DEFAULT 'default_value'", "DateOfArrival TIMESTAMP", "StatusOfFlight VARCHAR(1000)","NextFlight VARCHAR(1000) NOT NULL REFERENCES Flights(ReferenceID) DEFAULT 'default_value ","TimeToArrival VARCHAR(1000)","FLIGHTOWNER VARCHAR(30) NOT NULL REFERENCES ACCOUNT(REFERENCEID) DEFAULT 'default_value'","RETURNINGFLIGHTLISTREF VARCHAR(1000)"
    };


    private GeneralAdapter FlightPathAdapter;
    private final String FlightPathTableName = "FlightPath";

    private final String[] infoForFlightPath = {
            "FlightPathRefID VARCHAR(1000)","DepartingFlight Varchar(1000) NOT NULL REFERENCES Flights(FLIGHTREFERENCEID) DEFAULT 'default_value'","ReturningFlight Varchar(1000) NOT NULL REFERENCES Flights(FLIGHTREFERENCEID) DEFAULT 'default_value'", "Plane VARCHAR(1000) NOT NULL REFERENCES OWNEDPLANES(PlaneReferenceID) DEFAULT 'default_value'","FLIGHTPATHOWNER VARCHAR(30) NOT NULL REFERENCES ACCOUNT(REFERENCEID) DEFAULT 'default_value'","FREQUENCYFLIGHTPATH VARCHAR(30)"

    };



    @FXML
    private Button Add;

    @FXML
    private TableColumn<Flight, String> ArrivalToColumn;

    @FXML
    private DatePicker DODeparture;

    @FXML
    private TableColumn<Flight, LocalDate> DateDepartureColumn;

    @FXML
    private TextField DateOfArrival;

    @FXML
    private TableColumn<Flight, String> DepartureColumn;

    @FXML
    private TextField DepartureFrom;

    @FXML
    private TableView<Flight> FlightsTable;

    @FXML
    private ComboBox<OwnedPlane> Plane;

    @FXML
    private TableColumn<Flight, String> PlaneColumn;

    @FXML
    private CheckBox PmOrAm;

    @FXML
    private Button Save;

    @FXML
    private TableColumn<Flight, String> TimeDepartureColumn;

    @FXML
    private TextField TimeOfArrival;

    @FXML
    private ComboBox<String> TimeOfDeparture;

    @FXML
    private TextField arrivalTo;


    public ExtensionFlightController() throws SQLException
    {

        //**** Flight Adapter ****
        flightAdapter = new GeneralAdapter(false, flightTableName, flightInfo); //Instantiating a General Adapter object specifically for interacting with the Flight Database
        //***************************************

        //**** FlightPath Adapter ****
        FlightPathAdapter = new GeneralAdapter(false, FlightPathTableName, infoForFlightPath); //Instantiating a General Adapter object specifically for interacting with the Flight Database
        //***************************************
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) //This FlightsController class implements the Initializable Interface which requires the implementation of the initialize method
    //The initialize method defines a variety of characteristics the Flights FX will hold upon display
    {

        //********************* Initializing all FXML Fields ****************************
        //*******************************************************************************

        //*******************************************************************************
        //*******************************************************************************


        //********************* Setting Up Flights Table ********************************
        //*******************************************************************************

        DepartureColumn.setCellValueFactory(cellData -> cellData.getValue().getDepartureLocation()); // In this line and all the following ones until line 258 I am defining what will be displayed in each table column of my Flights Table
        // To define what wil be displayed in each Column I have to use the method .setCellValueFactory which has a parameter of type Callback< TableColumn.CellDataFeatures<S, T>, ObservableValue<T> >
        // S and T are generic types which I defined earlier in this class: private TableColumn<Flight, String> DepartureColumn, therefore S = Flight and T = String
        // Now to be more specific Callback is a functional interface ( meaning it has exactly one abstract method ), for that reason I can use a lambda method which defines its abstract method as the argument to setCellValueFactory()
        // The signature of Callback is "Public interface Callback<P,R>"
        // As explained earlier Callback has an abstract Method, the signature of that abstract method is " R call(P param) "
        // R and P are generic types and as stated earlier .setCellValueFactory's parameter is Callback< TableColumn.CellDataFeatures<S, T>, ObservableValue<T> >, so P = TableColumn.CellDataFeatures<S, T> && R = ObservableValue<T>
        // So Quick summary the Call method will have a parameter of type P = TableColumn.CellDataFeatures<S, T> and will return R = ObservableValue<T>
        // In conclusion for each .setCellValueFactory() my argument is a lambda expression meeting the criteria of the Call method signature, the lambda expression has a parameter of type CellDataFeatures<S,T> the body of my expression will first get the S Value of that argument in This being a Flight Object -> continue on Next Line
        // then from that flight object it will return an ObservableValue of type T matching the T type of the CellDataFeatures i.e. a String in this case
        DateDepartureColumn.setCellValueFactory(cellData -> cellData.getValue().getOnlyDate()); //described above
        TimeDepartureColumn.setCellValueFactory(cellData ->
        { //same as above but my getOnlyTime method throws a ParseException which is a checked exception and therefore must be explicitly handled
            try {
                return cellData.getValue().getOnlyTime();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });//Unlike earlier I have to use a code block as my expression doesn't immediately return a value
        ArrivalToColumn.setCellValueFactory(cellData -> cellData.getValue().getArrivalLocation()); //described above
        PlaneColumn.setCellValueFactory(cellData -> cellData.getValue().getPlane().getValue().getRefId()); //described above

        FlightsTable.setItems(flightData); //The FlightsTable will be populated with all the objects in flightData and since flightData is an ObservableList if its contents are changed that is automatically reflected in FlightsTable
        //*******************************************************************************
        //*******************************************************************************

    }

    @FXML
    void add(ActionEvent event) throws IOException, ParseException, SQLException {
        //**************************** Ensuring All Required Fields Are Filled ****************************
        //*************************************************************************************************
        if(DODeparture.getValue()==null ||  TimeOfDeparture.getSelectionModel().getSelectedItem() == null )
        {
            String reference = "Alert-view.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(AirWingApplication.class.getResource(reference));
            Stage stage= new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            AlertController alert = (AlertController) fxmlLoader.getController();
            alert.setErrorMessage("Ensure All Required Fields Are Filled");
        }
        //*************************************************************************************************
        //*************************************************************************************************
        else {
            ArrivalEstimator arrivalEstimator = new ArrivalEstimator(Plane.getValue(), arrivalTo.getText(), DepartureFrom.getText(), OwnedPlanesAdapter);


            //**** Initializing all the attributes of the Requested Flight ****
            //*****************************************************************
            String departureLocation = DepartureFrom.getText(); // initializing a string which holds the departure location
            LocalDateTime localDateTimeOfDeparture = arrivalEstimator.getDateAndTimeOfDeparture(PmOrAm, TimeOfDeparture, DODeparture); //The initialization of a LocalDateTime which holds the requested flights date and time of departure in 24-hour clock time, the value is obtained from the getDateAndTimeOfDeparture() method
            String arrivalLocation = arrivalTo.getText(); // initializing a string which holds the arrival location
            String frequencyOfFlights = "One-time";
            OwnedPlane planeForFlight = Plane.getSelectionModel().getSelectedItem(); // initializing a OwnedPlane object which holds the plane for the flight
            LocalDateTime localDateTimeOfArrival = arrivalEstimator.getDateOfArrival(localDateTimeOfDeparture); //The initialization of a LocalDateTime which holds the requested flights date and time of arrival in 24-hour clock time, the value is obtained from the getDateOfArrival() method
            String statusOfFlight = "Establishing"; // initializing a string which holds the status of flight, at this point when adding that will always be 'Establishing'
            LocalTime timeOfArrival = localDateTimeOfArrival.toLocalTime(); //Declaring and initializing a LocalTime variable which will exclusively hold the time portion of localDateTimeOFArrival
            String timeOfArrivalIn12HourFormat = ArrivalEstimator.convertTo12HrTime(timeOfArrival.toString()); // Initializing a string which holds the timeOfArrival converted to 12 hour time, the value is retrieved from convertTo12hrTime() method
            //*****************************************************************
            //*****************************************************************

            //**** Creating Reference ids for Requested Flight ****
            //*****************************************************
            ArrayList<String> keys = flightAdapter.findOneColumn("FLIGHTREFERENCEID", "FLIGHTREFERENCEID"); // populating an arraylist with all the keys stored in the database

            for (Flight flight : flightData) {
                keys.add(flight.retrieveRefId()); //populating the same keys arraylist with all the keys in the table
            }

            String flightRef = returnKey(keys, "Flight-"); //initializing the key of the requested flight
            keys.add(flightRef); //adding that key to the arraylist of keys so that the nextflight doesn't have a duplicate refId

            //******************************************************
            //******************************************************

            //******** Creating New Flight Path Key ****************
            //******************************************************

            ArrayList<String> flightPathKeys = FlightPathAdapter.findOneColumn("FLIGHTPATHREFID","FLIGHTPATHREFID"); //// populating an arraylist with all the flightpath keys stored in the database

            String flightPathRef = returnKey(flightPathKeys,"FlightPath-"); ////initializing the key of the flightPath

            //******************************************************
            //******************************************************

            //**** Calculating the Key for the Frequency Flight Path **********
            //*****************************************************************
            LinkedHashMap<String ,String> map = new LinkedHashMap<>(); //map which will hold criteria for search

            map.put("FLIGHTPATHOWNER =",owner); //specifies search criteria ; FLIGHTOWNER equals the ref id of the person signed in

            ArrayList<String> listOfFrequencyFlightKeys = FlightPathAdapter.findOneColumnRestricted("FLIGHTPATHOWNER",map); // returning an arrayList of the column FREQUENCYFLIGHT where the above criteria is met

            String key = KeyCreation.returnKeyFrequencyFlight(listOfFrequencyFlightKeys); // setting the String key to the key returned from the method, this key will be the first half of the ref id for frequency flights of this addition

            String completeKey = returnKey(listOfFrequencyFlightKeys,key+"-"); // returns a complete Key for the first flight
            //*****************************************************************
            //*****************************************************************




            //**** Creating Flight Objects ****
            //*********************************
            Flight flight;

            if (refIdOfReturningList != null)
            {
                flight = new Flight(flightRef, departureLocation, localDateTimeOfDeparture, arrivalLocation, frequencyOfFlights, planeForFlight, timeOfArrival, statusOfFlight, null, timeOfArrivalIn12HourFormat, owner, refIdOfReturningList);
            }
            else
            {
                flight = new Flight(flightRef, departureLocation, localDateTimeOfDeparture, arrivalLocation, frequencyOfFlights, planeForFlight, timeOfArrival, statusOfFlight, nextFlight, timeOfArrivalIn12HourFormat,"2:00", owner,1);
            }
            //*********************************
            //*********************************



            //********************************** Checking if it's safe to add flight *******************************
            //******************************************************************************************************

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            //linkedHashMap.put("FLIGHTPATHOWNER =",owner); I think I should find flightpatsh with same plane
            linkedHashMap.put("PLANE = ",flight.retrievePlane());
            ArrayList<FlightPath> listOfFlightPaths = FlightPathAdapter.findRecords(linkedHashMap,new FlightPath());
            ObservableList<FlightPath> observableList = FXCollections.observableArrayList(listOfFlightPaths);

            boolean safeToAdd;
            FlightValidator validator = new FlightValidator();

            if(refIdOfReturningList==null)
            {

                FlightPath flightPath = new FlightPath(flightPathRef,flight,nextFlight,owner,completeKey);

                if(validator.nextFlightSafe(flight,nextFlight))
                {

                    check1 = validator.checkIfSafeToAdd(observableList,flightPath);

                    if(check1)
                    {
                        modifyController.setCheck1(check1,flight);
                        success();
                        ((Stage)root.getScene().getWindow()).close();

                    }
                    else
                    {
                        AlertController alertController = alert();
                        alertController.setErrorMessage("Unable To Send This Plane To Spoke,check details or choose different Plane");
                    }


                }
                else
                {
                    AlertController alertController = alert();
                    alertController.setErrorMessage("Ensure This flight arrives at least two hours prior to returning flight");

                }


            }
            else
            {
                FlightPath flightPath = new FlightPath(flightPathRef, prevFlight,flight,owner,completeKey);

                if(validator.nextFlightSafe(prevFlight,flight))
                {

                    safeToAdd = validator.checkIfSafeToAdd(observableList,flightPath);

                    if(safeToAdd)
                    {
                        modifyController.setSafeToAdd(safeToAdd,flightPath);
                        success();
                        ((Stage)root.getScene().getWindow()).close();

                    }
                    else
                    {
                        AlertController alertController = alert();
                        alertController.setErrorMessage("Error Creating this return flight, check details");
                    }


                }
                else
                {
                    AlertController alertController = alert();
                    alertController.setErrorMessage("Ensure This return-flight departs at least two hours subsequent to previous flight");

                }
            }

            //******************************************************************************************************
            //******************************************************************************************************



        }



    }



    public void setInfo(String departureLocation,String arrivalToInfo, OwnedPlane plane, Flight nextFlight, String owner, ModifyController modifyController)
    {
        DepartureFrom.setText(departureLocation);

        arrivalTo.setText(arrivalToInfo);

        ArrayList<OwnedPlane> ownedPlanes = new ArrayList<>();
        ownedPlanes.add(plane);

        ObservableList<OwnedPlane> observableList = FXCollections.observableArrayList(ownedPlanes);
        Plane.setItems(observableList);

        Plane.getSelectionModel().selectFirst();

        this.nextFlight = nextFlight;

        this.owner = owner;

        this.modifyController = modifyController;

    }

    public void setInfo(String departureLocation,String arrivalToInfo, OwnedPlane plane, String referenceList , String owner, Flight prevFlight , ModifyController modifyController) //if referenceList is not null this is the returnFlight of the OG plane, that means Flight should be the prev flight
    {
        DepartureFrom.setText(departureLocation);

        arrivalTo.setText(arrivalToInfo);

        ArrayList<OwnedPlane> ownedPlanes = new ArrayList<>();
        ownedPlanes.add(plane);

        ObservableList<OwnedPlane> observableList = FXCollections.observableArrayList(ownedPlanes);
        Plane.setItems(observableList);

        Plane.getSelectionModel().selectFirst();

        refIdOfReturningList = referenceList;

        this.prevFlight = prevFlight;

        this.owner = owner;

        this.modifyController = modifyController;

    }



    public AlertController alert() throws IOException
    {
        FXMLLoader fxmlLoader = FXMLLoader.load(AirWingApplication.class.getResource("Alert-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        return fxmlLoader.getController();
    }

    public void success() throws IOException
    {
        FXMLLoader fxmlLoader = FXMLLoader.load(AirWingApplication.class.getResource("Success-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        ((SuccessController)fxmlLoader.getController()).setSuccess("Successful Plane Addition!");
    }



}
