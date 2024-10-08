package datastructure.airplaneapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static datastructure.airplaneapplication.KeyCreation.returnKey;

public class ModificationsBaseController
{
    private StackPane root;
    private InformationStackPane createExtensionsFlightController;
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


    public ModificationsBaseController(StackPane root,TableColumn<Flight, String> ArrivalToColumn,DatePicker DODeparture,TableColumn<Flight, LocalDate> DateDepartureColumn, TextField DateOfArrival,TableColumn<Flight, String> DepartureColumn,TextField DepartureFrom,TableView<Flight> FlightsTable,ComboBox<OwnedPlane> Plane,TableColumn<Flight, String> PlaneColumn,CheckBox PmOrAm,TableColumn<Flight, String> TimeDepartureColumn,ComboBox<String> TimeOfDeparture,TextField arrivalTo) throws SQLException
    {
        this.root = root;
        this.ArrivalToColumn = ArrivalToColumn;
        this.DODeparture = DODeparture;
        this.DateDepartureColumn = DateDepartureColumn;
        this.DateOfArrival = DateOfArrival;
        this.DepartureColumn = DepartureColumn;
        this.DepartureFrom = DepartureFrom;
        this.FlightsTable = FlightsTable;
        this.Plane = Plane;
        this.PlaneColumn = PlaneColumn;
        this.PmOrAm = PmOrAm;
        this.TimeDepartureColumn = TimeDepartureColumn;
        this.TimeOfDeparture = TimeOfDeparture;
        this.arrivalTo = arrivalTo;

        //**** Flight Adapter ****
        flightAdapter = new GeneralAdapter(false, flightTableName, flightInfo); //Instantiating a General Adapter object specifically for interacting with the Flight Database
        //***************************************

        //**** FlightPath Adapter ****
        FlightPathAdapter = new GeneralAdapter(false, FlightPathTableName, infoForFlightPath); //Instantiating a General Adapter object specifically for interacting with the Flight Database
        //***************************************
    }



    public Flight add() throws IOException, ParseException, SQLException
    {
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
            return null;
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
                        success();
                        return flight;

                    }
                    else
                    {
                        AlertController alertController = alert();
                        alertController.setErrorMessage("Unable To Send This Plane To Spoke,check details or choose different Plane");
                        return null;
                    }


                }
                else
                {
                    AlertController alertController = alert();
                    alertController.setErrorMessage("Ensure This flight arrives at least two hours prior to returning flight");
                    return null;

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
                        ((CreatePlaneExtensionsFlightController)createExtensionsFlightController).sendFlightPath(flightPath);
                        success();
                        ((Stage)root.getScene().getWindow()).close();
                        return flight;

                    }
                    else
                    {
                        AlertController alertController = alert();
                        alertController.setErrorMessage("Error Creating this return flight, check details");
                        return null;
                    }


                }
                else
                {
                    AlertController alertController = alert();
                    alertController.setErrorMessage("Ensure This return-flight departs at least two hours subsequent to previous flight");
                    return null;

                }
            }

            //******************************************************************************************************
            //******************************************************************************************************



        }



    }



    public void setInfo(String departureLocation, String arrivalToInfo, OwnedPlane plane, Flight nextFlight, String owner, InformationStackPane createExtensionsFlightController)
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

        this.createExtensionsFlightController = createExtensionsFlightController;

    }

    public void setInfo(String departureLocation,String arrivalToInfo, OwnedPlane plane, String referenceList , String owner, Flight prevFlight , InformationStackPane createPlaneExtensionsFlightController) //if referenceList is not null this is the returnFlight of the OG plane, that means Flight should be the prev flight
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

        this.createExtensionsFlightController = createPlaneExtensionsFlightController;

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
