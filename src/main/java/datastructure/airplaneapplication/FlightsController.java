package datastructure.airplaneapplication;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import static datastructure.airplaneapplication.KeyCreation.returnKey;
import static datastructure.airplaneapplication.KeyCreation.returnKeyFrequencyFlight;

public class FlightsController implements Initializable,MainTabs
{
    private ArrivalEstimator arrivalEstimator;
    @FXML
    private TabPane tabPane;
    private int indexOfAddedElement;
    private final String flightTableName = "Flights";

    private final String FlightPathTableName = "FlightPath";

    private final String[] infoForFlightPath = {
        "FlightPathRefID VARCHAR(1000)","DepartingFlight Varchar(1000) NOT NULL REFERENCES Flights(FLIGHTREFERENCEID) DEFAULT 'default_value'","ReturningFlight Varchar(1000) NOT NULL REFERENCES Flights(FLIGHTREFERENCEID) DEFAULT 'default_value'", "Plane VARCHAR(1000) NOT NULL REFERENCES OWNEDPLANES(PlaneReferenceID) DEFAULT 'default_value'","FLIGHTPATHOWNER VARCHAR(30) NOT NULL REFERENCES ACCOUNT(REFERENCEID) DEFAULT 'default_value'","FREQUENCYFLIGHTPATH VARCHAR(30)"

    };
    private final String[] flightInfo = {
            "FLIGHTREFERENCEID VARCHAR(1000)", "DepartureLocation VARCHAR(1000)", "DateOfDeparture TIMESTAMP",
            "ArrivalLocation VARCHAR(1000)", "Frequency VARCHAR(1000)",
            "Plane VARCHAR(1000) NOT NULL REFERENCES OWNEDPLANES(PlaneReferenceID) DEFAULT 'default_value'", "DateOfArrival TIMESTAMP", "StatusOfFlight VARCHAR(1000)","NextFlight VARCHAR(1000) NOT NULL REFERENCES Flights(ReferenceID) DEFAULT 'default_value ","TimeToArrival VARCHAR(1000)","FLIGHTOWNER VARCHAR(30) NOT NULL REFERENCES ACCOUNT(REFERENCEID) DEFAULT 'default_value'","RETURNINGFLIGHTLISTREF VARCHAR(1000)"
    };


    @FXML
    private TextField TimeOfArrival;

    @FXML
    private TextField DateOfArrival;

    private String returningFlightListRef;
    private String flightRef;
    private String departureLocation;
    private String arrivalLocation;
    private String frequencyOfFlights;
    private LocalDateTime localDateTimeOfDeparture;
    private LocalDateTime localDateTimeOfArrival;
    private String timeUntilReturn;
    private OwnedPlane planeForFlight;
    private String statusOfFlight;
    private Flight nextFlight;

    private String flightRefForReturningFlight;
    private String returningFlightsDepartureLocation;
    private LocalDateTime returningFlightsDateOfDeparture;
    private String returningFlightsArrivalLocation;
    private LocalDateTime returningFlightsDateOfArrival;
    private String returningFlightTimeOfArrivalIn12HourFormat;

    private String timeOfArrivalIn12HourFormat;

    private String refId;

    private ObservableList<OwnedPlane> OwnedPlanesObservableList = FXCollections.observableArrayList();
    ObservableList<String> timeOfDepartureObservableList = FXCollections.observableArrayList();
    private ObservableList<String> hoursTillReturnObservableList = FXCollections.observableArrayList();
    private ObservableList<String> frequencyObservableList = FXCollections.observableArrayList();
    private ObservableList<Flight> flightData = FXCollections.observableArrayList();

    private ObservableList<FlightPath> flightPath = FXCollections.observableArrayList();

    private final String[] frequencyofFlights = {"One-time","Daily","Weekly","Bi-weekly","Monthly"};
    private String[] hours = {"1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00","10:00","11:00","12:00"};

    private String[] hoursToNext = {"2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00","10:00","11:00","12:00"};

    private ObservableList<String> spoke;
    private ObservableList<String> departure;
    private ObservableList<String> arrival;


    @FXML
    private ComboBox<String> ArrivalTo;

    @FXML
    private TableColumn<Flight,String > ArrivalToColumn;

    @FXML
    private TableColumn<Flight,String > TimeuntilReturnColumn;

    @FXML
    private DatePicker DODeparture;

    @FXML
    private TableColumn<Flight, LocalDate> DateDepartureColumn;

    @FXML
    private TextField DepartureFrom;


    @FXML
    private TableColumn<Flight, String> DepartureColumn;

    @FXML
    private TableView<Flight> FlightsTable;


    @FXML
    private ComboBox<String> Frequency;

    @FXML
    private TableColumn<Flight, String> FrequencyOfTravelColumn;

    @FXML
    private ComboBox<OwnedPlane> Plane;

    @FXML
    private TableColumn<Flight, String> PlaneColumn;
    //*******************************************************************
    //*******************************************************************
    private Flight flightForModification;
    private ObservableList<Flight> currentFlightData = FXCollections.observableArrayList();

    @FXML
    private TextField flightRefText;

    @FXML
    private ComboBox<String> departureLocationCombo;

    @FXML
    private DatePicker dateOfDepartureDatePicker;

    @FXML
    private CheckBox pm$AmCheck;

    @FXML
    private ComboBox<String> timeOfDepartureCombo;

    @FXML
    private ComboBox<String> arrivalToCombo;

    @FXML
    private ComboBox<OwnedPlane> planeCombo;

    //********************************

    @FXML
    private TableView<Flight> tableOfCurrentFlights;
    @FXML
    private TableColumn<Flight, String> flightRefColumn;
    @FXML
    private TableColumn<Flight, String> departureColumn;

    @FXML
    private TableColumn<Flight, LocalDate> dateOfDepartureColumn;

    @FXML
    private TableColumn<Flight, String> timeOfDepartureColumn;

    @FXML
    private TableColumn<Flight, String> arrivalToColumn;

    @FXML
    private TableColumn<Flight, String> planeColumn;


    @FXML
    private CheckBox PmOrAm;

    @FXML
    private TableColumn<Flight, String> TimeDepartureColumn;

    @FXML
    private ComboBox<String> TimeOfDeparture;

    private GeneralAdapter OwnedPlanesAdapter;

    private GeneralAdapter flightAdapter;
    private GeneralAdapter FlightPathAdapter;

    @FXML
    private ComboBox<String> TimeUntilReturn;



    public void buildData() throws SQLException //This method is used to initialize various controls in the Flights-view UI
    {
        //**** Frequency Combo Box ****
        frequencyObservableList.clear(); //an observable listOfOwnedPlanes for the frequency of the Requested Flights occurrence
        frequencyObservableList.addAll(frequencyofFlights); //Adding the respective listOfOwnedPlanes of options to the frequency observable listOfOwnedPlanes
        Frequency.setItems(frequencyObservableList); // assigning the frequency observable listOfOwnedPlanes to the frequency Combo Box
        //***************************************


        //**** Time Until Return Combo Box ****
        hoursTillReturnObservableList.addAll(hoursToNext); //hoursToNext is an Array of Strings which holds all the options for timeOfDepartureObservableList until the Requested Plane returns from its spoke to the hub, hoursToNext is added to the hoursTillReturnObservableList
        TimeUntilReturn.setItems(hoursTillReturnObservableList); //assigning the hoursTillReturnObservableList to the Time Until Return combo box
        //***************************************


        //**** Flight Path Adapter ****
        FlightPathAdapter = new GeneralAdapter(false,FlightPathTableName,infoForFlightPath); //Instantiating a General Adapter object specifically for interacting with the FlightPath Database
        //***************************************


        //**** Flight Adapter ****
        flightAdapter = new GeneralAdapter(false, flightTableName, flightInfo); //Instantiating a General Adapter object specifically for interacting with the Flight Database
        //***************************************

        //**** Owned Plane Adapter ****
        String[] OwnedPlaneInfo = {
                "PlaneReferenceID VARCHAR(1000)", "Type VARCHAR(1000)", "Performance VARCHAR(1000)", //OwnedPlaneInfo is an Array of Strings which holds the specifications of each column in the OwnedPlanes DB,
                "Weight VARCHAR(1000)", "Dimension VARCHAR(1000)", "FlightCrewCapacity DOUBLE",      // a necessary entry in my GeneralAdapter constructor
                "PassengerCapacity DOUBLE", "CruisingSpeed DOUBLE","ReferenceID VARCHAR(30) NOT NULL REFERENCES Account(REFERENCEID) DEFAULT 'default_value'"
        };
        //***************************************



        //**** Planes Combo Box ****
        OwnedPlanesAdapter = new GeneralAdapter(false,"OWNEDPLANES",OwnedPlaneInfo); //Instantiating a General Adapter object specifically for interacting with the OwnedPlanes Database


        LinkedHashMap<String,Object> map = new LinkedHashMap<>(); //A linkedHashMap where each Key will hold a String identifying the DB Table column in question, and each Value will hold the object we are searching for in the fore mentioned Table Column
        map.put("REFERENCEID=",refId); //REFERENCEID is the Column we are searching through, refId is the referenceID of the person logged in currently
        ArrayList<OwnedPlane> listOfOwnedPlanes =  OwnedPlanesAdapter.findRecords(map,new OwnedPlane()); //This findRecords method will search the OWNEDPLANES DB for any records matching the criteria in map, i.e: REFERENCEID = refID
                                                                                                                                 //listOfOwnedPlanes will hold the results of findRecords

        OwnedPlanesObservableList.clear();                      // OwnedPlanesObservableList is an observable list which will hold all the planes owned by the Account Signed in
        OwnedPlanesObservableList.addAll(listOfOwnedPlanes);    // The listOfOwnedPlanes populated earlier will now be added to OwnedPlanesObservableList
        Plane.setItems(OwnedPlanesObservableList);              // assigning the OwnedPlanesObservableList to the Plane combo box
        planeCombo.setItems(OwnedPlanesObservableList);
        //***************************************


        //**** Time Of Departure Combo Box ****
        timeOfDepartureObservableList.addAll(hours); //timeOfeDepartureObservableList is an observable list which holds all the different times a plane may depart from 1-12 with the Am and pm set by a checkbox,here hours holds an array of Strings each with a different time value,
                                                    // hours is added to the time of Departure observable list
        TimeOfDeparture.setItems(timeOfDepartureObservableList); //the timeOfeDepartureObservableList is assigned to the TimeOfDeparture Combo Box
        timeOfDepartureCombo.setItems(timeOfDepartureObservableList);
        //***************************************



    }

    public void onEditOfDepartureLocationSearchBar() throws IOException //This Method populates the Arrival location Search bar with airport cities based on the characters inputted by application users
    {
        //**** AirportLocationArrival Combo Box ****
        ArrayList<String> airportLocations = AirportLocation.getCityInfo(departureLocationCombo.getValue(),1); //The AirportLocation class holds a method getCityInfo which will return an array of relevant airport cities based on the key character inputted by users
        departure = FXCollections.observableArrayList(airportLocations); //spoke is an ObservableList, the airportLocation options determined above are all added to the list as options for spokes in the Hub-Spoke system
        departureLocationCombo.setItems(departure); //The spoke ObservableList is assigned to the ArrivalTo Combo Box
        //***************************************
    }

    public void onEditOfArrivalLocationSearchBar2() throws IOException //This Method populates the Arrival location Search bar with airport cities based on the characters inputted by application users
    {
        //**** AirportLocationArrival Combo Box ****
        ArrayList<String> airportLocations = AirportLocation.getCityInfo(arrivalToCombo.getValue(),1); //The AirportLocation class holds a method getCityInfo which will return an array of relevant airport cities based on the key character inputted by users
        arrival = FXCollections.observableArrayList(airportLocations); //spoke is an ObservableList, the airportLocation options determined above are all added to the list as options for spokes in the Hub-Spoke system
        arrivalToCombo.setItems(arrival); //The spoke ObservableList is assigned to the ArrivalTo Combo Box
        //***************************************
    }



    public void onEditOfArrivalLocationSearchBar() throws IOException //This Method populates the Arrival location Search bar with airport cities based on the characters inputted by application users
    {
        //**** AirportLocationArrival Combo Box ****
        ArrayList<String> airportLocations = AirportLocation.getCityInfo(ArrivalTo.getValue(),1); //The AirportLocation class holds a method getCityInfo which will return an array of relevant airport cities based on the key character inputted by users
        spoke = FXCollections.observableArrayList(airportLocations); //spoke is an ObservableList, the airportLocation options determined above are all added to the list as options for spokes in the Hub-Spoke system
        ArrivalTo.setItems(spoke); //The spoke ObservableList is assigned to the ArrivalTo Combo Box
        //***************************************
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) //This FlightsController class implements the Initializable Interface which requires the implementation of the initialize method
    //The initialize method defines a variety of characteristics the Flights FX will hold upon display
    {

        //********************* Initializing all FXML Fields ****************************
        //*******************************************************************************
        Platform.runLater(() -> //Faced an issue where my I attempted to use refId in my initialize method but refId had a null value. This was because I set refId in airlineMainPageController AFTER FlightsController is created and the initialize method has run.
        {                       //The solution to the above problem is to schedule a provided runnable to be executed on the current thread after the current method execution (initialize in this scenario) has been processed using the runLater() method.
                                //runLater has one parameter Of Runnable type. However, the Runnable class is an interface with a single abstract method and as such I can use a lambda expression to define its method and instead of creating a concrete implementation of Runnable I can use the lambda expression as the argument to the runLater method.

            String[] accountInfo= {"ReferenceID VARCHAR(30)","Username VARCHAR(30)","Passwords VARCHAR(200)","Email VARCHAR(40)","DepartureLoco Varchar(1000)"}; //accountInfo is an Array of Strings which holds the specifications of each column in the Account DB,
            GeneralAdapter accountAdapter; // A General Adapter reference variable specifically for interacting with the Account Database
            ArrayList<Account> accounts; //An arraylist reference variable intended to hold specific accounts retrieved from Account Database


            try // Instantiating accountAdapter, running findRecords() method and buildData method also risk throwing a SQLException which is a checked exception and as such must be handled explicitly
            {
                accountAdapter = new GeneralAdapter(false,"ACCOUNT",accountInfo); //instantiating the accountAdapter
                LinkedHashMap<String,Object> map = new LinkedHashMap<>(); // Instantiating A linkedHashMap where each Key will hold a String identifying the DB Table column in question, and each Value will hold the object we are searching for in the fore mentioned Table Column

                map.put("ReferenceID =",refId); //REFERENCEID is the Column we are searching through, refId is the referenceID of the person logged in currently
                accounts = accountAdapter.findRecords(map,new Account()); ////This findRecords method will search the Account DB for any records matching the criteria in map, i.e: REFERENCEID = refID, there should only be one Account as REFERENCEID is a Primary Key
                DepartureFrom.setText(accounts.getFirst().getDepartureLoco()); //Setting the Text of the DepartureFrom TextField to the Hub Location of the first Account in the accounts ArrayList (As highlighted above there will only be one Account in the ArrayList)

                buildData(); //Calling the buildData() method defined above to initialize multiple FXML fields in the Flights-view FXML file

            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);

            }

            LinkedHashMap<String , Object> map = new LinkedHashMap<>(); // Instantiating A linkedHashMap where each Key will hold a String identifying the DB Table column in question, and each Value will hold the object we are searching for in the fore mentioned Table Column
            map.put("FLIGHTOWNER = ",refId); //FlightOwner is the Column we are searching through, refId is the referenceID of the person logged in currently
            try {
                currentFlightData.addAll(flightAdapter.findRecords(map,new Flight()));  // running findRecords() method risks throwing a SQLException which is a checked exception and as such must be handled explicitly
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        });
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
        FrequencyOfTravelColumn.setCellValueFactory(cellData -> cellData.getValue().getFrequency()); //described above
        PlaneColumn.setCellValueFactory(cellData -> cellData.getValue().getPlane().getValue().getRefId()); //described above
        TimeuntilReturnColumn.setCellValueFactory(cellData -> cellData.getValue().getTimeTillReturn()); //described above

        FlightsTable.setItems(flightData); //The FlightsTable will be populated with all the objects in flightData and since flightData is an ObservableList if its contents are changed that is automatically reflected in FlightsTable
        //*******************************************************************************
        //*******************************************************************************

        //********************* Setting Up Table Of Current Flights ********************************
        //******************************************************************************************
        flightRefColumn.setCellValueFactory(cellData -> cellData.getValue().getRefid()); //described above
        departureColumn.setCellValueFactory(cellData -> cellData.getValue().getDepartureLocation()); //described above
        dateOfDepartureColumn.setCellValueFactory(cellData -> cellData.getValue().getOnlyDate()); //described above
        arrivalToColumn.setCellValueFactory(cellDate -> cellDate.getValue().getArrivalLocation()); //described above
        planeColumn.setCellValueFactory(cellDate -> cellDate.getValue().getPlane().getValue().getRefId()); //described above
        timeOfDepartureColumn.setCellValueFactory(cellData -> {
            try {
                System.out.println("heresy+9999999999999999999999999");
                return cellData.getValue().getOnlyTime();
            } catch (ParseException e) {
                System.out.println("Are you throwing an Exception + 9999999999999999999999999");
                throw new RuntimeException(e);
            }
        });

        tableOfCurrentFlights.setRowFactory(tableView ->// In this line, I am defining how each row of the EarningsTable will be created and managed
        // To customize the creation of rows, I use the method .setRowFactory which has a parameter of type Callback<TableView<S>, TableRow<S>>
        // S is a generic type which in this case is defined as Earning, so S = Earning
        // Now to be more specific, Callback is a functional interface (meaning it has exactly one abstract method), for that reason I can use a lambda expression which defines its abstract method as the argument to setRowFactory()
        // The signature of Callback is "Public interface Callback<P, R>"
       // As explained earlier, Callback has an abstract method, the signature of that abstract method is "R call(P param)"
       // R and P are generic types and as stated earlier, .setRowFactory's parameter is Callback<TableView<S>, TableRow<S>>, so P = TableView<Earning> and R = TableRow<Earning>
       // So quick summary, the call method will have a parameter of type P = TableView<Earning> and will return R = TableRow<Earning>
       // In conclusion, for .setRowFactory() my argument is a lambda expression meeting the criteria of the call method signature, the lambda expression has a parameter of type TableView<Earning>
       // The body of my expression will first create a new TableRow<Earning>, then set an event handler to perform actions when a row is clicked, and finally return the TableRow<Earning>
        {
            TableRow<Flight> rows = new TableRow<>();
            rows.setOnMouseClicked(event ->
            {
                if(!rows.isEmpty())
                {
                    flightForModification = rows.getItem();
                }

            });
            return rows;
        });
        tableOfCurrentFlights.setItems(currentFlightData);  //The currentFlightsTable will be populated with all the objects in currentFlightData and since currentFlightData is an ObservableList if its contents are changed that is automatically reflected in currentFlightsTable
        //******************************************************************************************
        //******************************************************************************************


    }


    public void add() throws SQLException, IOException, ParseException //This method defines the technicals of making additions to the flightsTable
    {
        //**************************** Ensuring All Required Fields Are Filled ****************************
        //*************************************************************************************************
        if(DODeparture.getValue()==null ||  TimeOfDeparture.getSelectionModel().getSelectedItem() == null || ArrivalTo.getSelectionModel().getSelectedItem()==null || Frequency.getSelectionModel().getSelectedItem() == null || Plane.getSelectionModel().getSelectedItem()==null || TimeUntilReturn.getSelectionModel().getSelectedItem() == null )
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
        else
        {
            arrivalEstimator = new ArrivalEstimator(Plane.getValue(),ArrivalTo.getValue(),DepartureFrom.getText(),OwnedPlanesAdapter);

            boolean permissionGiven = false; //The Declaration and initialization of a boolean which will be taken into account in the scenario flights are added to the table on the basis of frequency but a conflict in flights arises -> continue on next line
            ///in this scenario permissionGiven will be assessed to determine whether the arline would like all the flights prior to the conflict to be added to the table or not

            //**** Initializing all the attributes of the Requested Flight ****
            //*****************************************************************
            departureLocation = DepartureFrom.getText(); // initializing a string which holds the departure location
            localDateTimeOfDeparture = arrivalEstimator.getDateAndTimeOfDeparture(PmOrAm,TimeOfDeparture,DODeparture); //The initialization of a LocalDateTime which holds the requested flights date and time of departure in 24-hour clock time, the value is obtained from the getDateAndTimeOfDeparture() method
            arrivalLocation = ArrivalTo.getValue(); // initializing a string which holds the arrival location
            frequencyOfFlights = Frequency.getValue(); //initializing a string which holds the frequency of flights
            planeForFlight = Plane.getSelectionModel().getSelectedItem(); // initializing a OwnedPlane object which holds the plane for the flight
            localDateTimeOfArrival = arrivalEstimator.getDateOfArrival(localDateTimeOfDeparture); //The initialization of a LocalDateTime which holds the requested flights date and time of arrival in 24-hour clock time, the value is obtained from the getDateOfArrival() method
            statusOfFlight = "Establishing"; // initializing a string which holds the status of flight, at this point when adding that will always be 'Establishing'
            Flight returningFlight = null; //temporarily initializing a null returningFlight
            LocalTime timeOfArrival = localDateTimeOfArrival.toLocalTime(); //Declaring and initializing a LocalTime variable which will exclusively hold the time portion of localDateTimeOFArrival
            timeOfArrivalIn12HourFormat = ArrivalEstimator.convertTo12HrTime(timeOfArrival.toString()); // Initializing a string which holds the timeOfArrival converted to 12 hour time, the value is retrieved from convertTo12hrTime() method
            timeUntilReturn = TimeUntilReturn.getValue();
            //*****************************************************************
            //*****************************************************************

            //**** Calculating the Key for the Frequency Flight Path ***************
            //*****************************************************************
            LinkedHashMap<String ,String> map = new LinkedHashMap<>(); //map which will hold criteria for search
            map.put("FLIGHTPATHOWNER =",refId); //specifies search criteria ; FLIGHTOWNER equals the ref id of the person signed in
            ArrayList<String> listOfFrequencyFlightKeys = FlightPathAdapter.findOneColumnRestricted("FLIGHTPATHOWNER",map); // returning an arrayList of the column FREQUENCYFLIGHT where the above criteria is met
            for(FlightPath flightPath1 : flightPath)
            {
                listOfFrequencyFlightKeys.add(flightPath1.retrieveFrequencyFlight()); //populating the same keys arraylist with all the keys in the table
            }
            String key = returnKeyFrequencyFlight(listOfFrequencyFlightKeys); // setting the String key to the key returned from the method, this key will be the first half of the ref id for frequency flights of this addition
            //*****************************************************************
            //*****************************************************************



            //**** Initializing all the attributes of the Returning Flight ****
            //*****************************************************************
            returningFlightsDepartureLocation = arrivalLocation;

            String[] timeuntilReturn = TimeUntilReturn.getValue().split(":");  // declaring and initializing a variable that holds the amount of time after the requested Flights Arrival the plane will stay parked before departing bakc to the hub
            Long timeToAdd = Long.valueOf(timeuntilReturn[0]); //converting the timeUntilReturn hour value to a long so that I can add it to a dateTime variable
            returningFlightsDateOfDeparture = localDateTimeOfArrival.plusHours(timeToAdd); //adding the timeToAdd long to the localDateTimeOfArrival of the requested Flight to determine the returning flights departure time
            returningFlightsArrivalLocation = departureLocation;
            returningFlightsDateOfArrival = arrivalEstimator.getDateOfArrival(localDateTimeOfDeparture); //initializing the returning flights date of arrival back at the hub, value obtained from getDateOfArrival method

            LocalTime returningFlightTimeOfArrival = returningFlightsDateOfArrival.toLocalTime();  ////Declaring and initializing a LocalTime variable which will exclusively hold the time portion of returningFlightsDateOfArrival
            returningFlightTimeOfArrivalIn12HourFormat = ArrivalEstimator.convertTo12HrTime(returningFlightTimeOfArrival.toString()); // Initializing a string which holds the timeOfArrival converted to 12 hour time, the value is retrieved from convertTo12hrTime() method
            //*****************************************************************
            //*****************************************************************


            //**** Creating Reference ids for Requested Flight and Returning Flight ****
            //**************************************************************************
            ArrayList<String> keys = flightAdapter.findOneColumn("FLIGHTREFERENCEID","FLIGHTREFERENCEID"); // populating an arraylist with all the keys stored in the database

            for(Flight flight : flightData)
            {
                keys.add(flight.retrieveRefId()); //populating the same keys arraylist with all the keys in the table
            }

            flightRef = returnKey(keys,"Flight-"); //initializing the key of the requested flight
            keys.add(flightRef); //adding that key to the arraylist of keys so that the nextflight doesn't have a duplicate refId

            flightRefForReturningFlight = returnKey(keys,"Flight-"); //initializing the key of the return flight
            keys.add(flightRefForReturningFlight); //adding that key to the arraylist of keys so that the nextflight doesn't have a duplicate refId
            //**************************************************************************
            //**************************************************************************


            //**** Creating ReferenceLis Ids for Returning Flight ****
            //**************************************************************************
            ArrayList<String> listRefKey = flightAdapter.findOneColumn("RETURNINGFLIGHTLISTREF","RETURNINGFLIGHTLISTREF"); // populating an arraylist with all the keys stored in the database

            for(Flight flight : flightData)
            {
                listRefKey.add(flight.retrieveReturningFlightListRef()); //populating the same keys arraylist with all the keys in the table
            }

            String ReferenceFirstHalfKey = returnKeyFrequencyFlight(listRefKey);
            
            returningFlightListRef = returnKey(listRefKey,ReferenceFirstHalfKey+"-"); //initializing the key of the return flight
            listRefKey.add(returningFlightListRef); //adding that key to the arraylist of keys so that the next flight doesn't have a duplicate refId

            //**************************************************************************
            //**************************************************************************


            //**** Creating Flight Objects ****
            //*********************************

            returningFlight = new Flight(flightRefForReturningFlight,returningFlightsDepartureLocation,returningFlightsDateOfDeparture,returningFlightsArrivalLocation,frequencyOfFlights,planeForFlight,returningFlightsDateOfArrival,statusOfFlight,null,returningFlightTimeOfArrivalIn12HourFormat,refId,returningFlightListRef);
            nextFlight = returningFlight; // initializing a Flight object which holds the next flight, in this case that would be the returning Flight.
            Flight flightAway = new Flight(flightRef,departureLocation,localDateTimeOfDeparture, arrivalLocation,frequencyOfFlights,planeForFlight,localDateTimeOfArrival,statusOfFlight,nextFlight,timeOfArrivalIn12HourFormat,timeUntilReturn,refId,1);

            Flight[] arrayOfFlights = createMultiple(flightAway,returningFlight, keys,listRefKey); // returning an arrayOfFlights based on the frequency on which the airline chose this flight to repeat

            //*********************************
            //*********************************


            //**** Checking if it's safe to add flights ********************************************************************
            //**************************************************************************************************************

            boolean safeToAdd = false; // a boolean which states whether it's safe to add the flights


            ArrayList<Flight> safeToAddFlights = new ArrayList<>(); // declaring an array of flights that will hold all flights that are safe to add
            ArrayList<FlightPath> safeToAddFlightPaths = new ArrayList<>(); //declaring an array of flight paths that will hold all flight paths that are safe to add


            ArrayList<String> flightPathKeys = FlightPathAdapter.findOneColumn("FLIGHTPATHREFID","FLIGHTPATHREFID"); //// populating an arraylist with all the flightpath keys stored in the database
            for(FlightPath flightPath : flightPath)
            {
                flightPathKeys.add(flightPath.retrieverRefId()); //populating the same keys arraylist with all the keys in the table
            }


            for(int i =0; i<arrayOfFlights.length; i++) //A loop that iterates over all the flights and checks if they're safe to add
            {
                String completeKey = returnKey(listOfFrequencyFlightKeys,key+"-"); // returns a complete Key for the first flight

                String flightPathRef = returnKey(flightPathKeys,"FlightPath-"); ////initializing the key of the flightPath

                flightAway = arrayOfFlights[i]; //the flightAway references a flight from the array of flights
                i++;
                returningFlight = arrayOfFlights[i]; //after incrementing the index, return flights is initialized to the next flight


                FlightPath path = new FlightPath(flightPathRef,flightAway,returningFlight,refId,completeKey); // A FlightPath object is instantiated

                FlightValidator validator = new FlightValidator();
                safeToAdd = validator.checkIfSafeToAdd(flightPath,path); //the boolean safeToAdd is initialized to the value received from the checkIfSafeToAdd method

                System.out.println("Ok so this is the alleged flight away "+flightAway.retrieveRefId());
                System.out.println("Ok so this is the alleged flight returning "+returningFlight.retrieveRefId());
                if(!safeToAdd) //if the flight is not safe to add the error is returned and the airline is asked whether they would like to add all safe flights up to this point
                {
                    System.out.println("I Will come Back and Write Appropriate Line");
                    break;
                }

                safeToAddFlights.add(flightAway); //if this point is reached the flight is safe to add and is added to the safeToAdd list
                safeToAddFlights.add(returningFlight); //if this point is reached the returning flight is safe to add and is added to the safeToAdd list
                safeToAddFlightPaths.add(path); //if this point is reached the flight path is safe to add and is added to the safeToAdd list
                flightPathKeys.add(flightPathRef); //since the flight path is safe to add its ref key has been used and as such must be added to the flightPathKey list so its not reused
                listOfFrequencyFlightKeys.add(completeKey); //adding the complete Key to the list

            }


            if(safeToAdd || permissionGiven) // if all flights are safeToAdd or permission is given to add all flights before conflict the flighst are aded to the table
            {
                flightPath.addAll(safeToAddFlightPaths); //flightPaths are added to the Observable list
                flightData.addAll(safeToAddFlights); //flights are added to the table
            }
            else
            {
                System.out.println("we got an issue champ");
            }

            keys.clear(); //The keys are cleared so there are no duplicates on next addition
            flightPathKeys.clear();

        }



    }

    public Flight[] createMultiple(Flight requestedFlight , Flight returnFlight, ArrayList<String> keys, ArrayList<String> secondSetOfKeys) throws SQLException // a method which creates multiple flights based on chosen frequency
    {

        //**** Declaring variables whose value will be instantiated differently depending on if block it lands inside ****
        String flightRef; //the refId of the flight
        String returnFlightListRefId; // refId for returning flight list
        String frequency = requestedFlight.retrieveFrequency(); //the frequency will stay the same for all flights and is retrieved from the requested flight
        Flight[] flights; //an array which will be returned at the end of the method which holds all the flights which could potentially be added
        long increment;  // a variable which depending on the frequency chosen will determine the days between each flight
        //***************************************************************************************************************


        //**************** Creating Flight Objects ***************
        //********************************************************
        if (frequency.equals(frequencyofFlights[0])) //frequency is only a one-time flight
        {

            Flight[] singleFlightPath = {requestedFlight, returnFlight}; //A temporary array to hold the original requestedFlight and returnFlight
            flights = singleFlightPath; //the flights array from earlier where reference the temporary array
        }
        else //frequency is not only a one-time flight 
        {


            if(frequency.equals(frequencyofFlights[1])) //frequency of flight is daily
            {
                increment = 1; //a day between each departure date;
                Flight[] tempFlightPath = new Flight[660]; //an array of flights which dictates how many flights will be created, since you will be able to book 330 days in advance there will be 330 flights and 330 returning flights
                flights = tempFlightPath; //the flights array we created earlier will reference the temp one created above
            }
            else if (frequency.equals(frequencyofFlights[2])) //frequency of flight is weekly
            {
                increment = 7; // a week between each departure date
                Flight[] tempFlightPath = new Flight[84]; //same logic as above
                flights = tempFlightPath;
            }
            else if (frequency.equals(frequencyofFlights[3])) //frequency of flight is Bi-weekly
            {//same logic as above
                increment = 14;
                Flight[] tempFlightPath = new Flight[46];
                flights = tempFlightPath;
            }
            else//frequency of flight is monthly
            {//same logic as above
                increment = 30;
                Flight[] tempFlightPath = new Flight[22];
                flights = tempFlightPath;
            }


            for(int i = 0; i<flights.length; i++) //a floor loop that will iterate through the flight.length array and make the necessary adjustments to refId, departure date, and arrival date
            {

                if(i==0) // for the first flight in the array the original requested flight and returning flights are added, no adjustments are made to them
                {
                    flights[i] = requestedFlight;
                    i++;
                    flights[i] = returnFlight;

                }
                else //for all flights which are not the first requested flight
                {
                    flightRef = returnKey(keys,"Flight-");
                    // Setting temp flight to a flight an index two degrees behind will retrieve the previous departing flight
                    Flight tempFlight = flights[i-2];

                    //through each iteration two Flights are added; 1. The flight departing the hub, 2. the Flight returning to the hub
                    flights[i] = new Flight(tempFlight.retrieveRefId(),tempFlight.retrieveDepartureLocation(),tempFlight.retrieveDateOfDeparture(),tempFlight.retrieveArrivalLocation(),tempFlight.retrieveFrequency(),tempFlight.retrievePlane(),tempFlight.retrieveDateOfArrival(),tempFlight.retrieveStatusOfFlight(),tempFlight.retrieveNextFlight(),tempFlight.retrieveTimeOfArrivalString(),refId,null,1);

                    flights[i].setDateOfDeparture(tempFlight.retrieveDateOfDeparture().plusDays(increment)); //adding the proper increment to the departure of the requestedFlight
                    flights[i].setDateOfArrival(tempFlight.retrieveDateOfArrival().plusDays(increment)); // adding the proper increment to the arrival of requested flight for the same reason as above
                    flights[i].updateRefId(flightRef); //updating reference id
                    keys.add(flightRef);
                    System.out.println("Hereeeees the flight ref"+flights[i].retrieveRefId());
                    i++; //incrementing i to set the returning flight of my next requestedFlight

                    flightRef = returnKey(keys,"Flight-");
                    returnFlightListRefId = returnKey(secondSetOfKeys,"Return-");
                    Flight tempReturnFlight = flights[i-2]; //with i incremented above when the index is deducted by two degrees, we retrieve the previous returning flight
                    flights[i] = new Flight(tempReturnFlight.retrieveRefId(),tempReturnFlight.retrieveDepartureLocation(),tempReturnFlight.retrieveDateOfDeparture(),tempReturnFlight.retrieveArrivalLocation(),tempReturnFlight.retrieveFrequency(),tempReturnFlight.retrievePlane(),tempReturnFlight.retrieveDateOfArrival(),tempReturnFlight.retrieveStatusOfFlight(),tempReturnFlight.retrieveNextFlight(),tempReturnFlight.retrieveTimeOfArrivalString(),refId,returnFlightListRefId); //setting next returnFlight to tempFlight
                    flights[i].setDateOfArrival(tempReturnFlight.retrieveDateOfArrival().plusDays(increment)); //adding the proper increment to the departure of the return Flight
                    flights[i].setDateOfDeparture(tempReturnFlight.retrieveDateOfDeparture().plusDays(increment)); // adding the proper increment to the arrival of return flight for the same reason as above
                    flights[i].updateRefId(flightRef); //updating reference id

                    flights[i-1].updateNextFlight(flights[i]); //now that returningFlight has been instantiated the next flight of flight away can be set

                    keys.add(flightRef);
                    secondSetOfKeys.add(returnFlightListRefId);
                    System.out.println("Hereeeees the flight ref"+flights[i].retrieveRefId());


                }
            }



        }


        return flights;
        //********************************************************
        //********************************************************

    }

    public void onSearch() throws ParseException, SQLException {

        //**************** Searching DataBase ************************************************************************************************************************************************************************
        //************************************************************************************************************************************************************************************************************

        //**********************Setting Up Initial Search Requirements*********************
        //*********************************************************************************

        boolean dateWithNoTime = false; // a boolean which states whether the date search criteria has been inputted along with time
        boolean timeWithNoDate = false; // // a boolean which states whether the time search criteria has been inputted along with date
        LocalDateTime dateAndTimeOfDeparture; // A localDateTime variable which holds the dateAndTimeOfDeparture as that variables initialization changes depending on circumstance
        LinkedHashMap<String,Object> map = new LinkedHashMap<>(); // a map object for the findRecords() method

        String flightRef = flightRefText.getText(); // A String that holds inputted flight reference
        String departureLoco = departureLocationCombo.getValue(); // A String that holds the departure location

        String arrivalLocation = arrivalToCombo.getValue(); // A String that holds the arrival location
        OwnedPlane plane =planeCombo.getValue(); // A Owned Plane Object that holds the plane-reference

        if(timeOfDepartureCombo.getSelectionModel().getSelectedItem()==null && dateOfDepartureDatePicker.getValue() == null) //In the scenario neither time nor date is set, dateAndTimeOfDeparture is set to null
            dateAndTimeOfDeparture = null;
        else if(timeOfDepartureCombo.getSelectionModel().getSelectedItem()==null && dateOfDepartureDatePicker.getValue() != null) // In this scenario time is not set but the date is, dateAndTimeOfDeparture is set to the Date at the start of the day
        {
            dateAndTimeOfDeparture = dateOfDepartureDatePicker.getValue().atStartOfDay();
            dateWithNoTime = true;
        }
        else if(timeOfDepartureCombo.getSelectionModel().getSelectedItem()!=null && dateOfDepartureDatePicker.getValue() == null)
        {
            dateOfDepartureDatePicker.setValue(LocalDate.now()); //setting the date to today's date, doesn't matter as when setting criteria in teh search we will only search for matching time values disregarding date
            dateAndTimeOfDeparture = arrivalEstimator.getDateAndTimeOfDeparture(pm$AmCheck,timeOfDepartureCombo,dateOfDepartureDatePicker); // In this scenario time is not set but the date is, dateAndTimeOfDeparture is set to the result of getDateAndTimeOfDeparture(),this method returns the date and time of departure in a LocalDateTime object , 24-hour format
            timeWithNoDate = true;
        }
        else
        {
            dateAndTimeOfDeparture = arrivalEstimator.getDateAndTimeOfDeparture(pm$AmCheck,timeOfDepartureCombo,dateOfDepartureDatePicker); // In this scenario time is set and so is the date, dateAndTimeOfDeparture is set to the result of getDateAndTimeOfDeparture(),this method returns the date and time of departure in a LocalDateTime object , 24-hour format
            System.out.println("im getting playd so hard");
        }

        map.put("FLIGHTOWNER = ",refId); ////FlightOwner is the Column we are searching through, refId is the referenceID of the person logged in currently
        map.put("AND FLIGHTREFERENCEID = ",flightRef); //FlightReferenceID is the Column we are searching through, flight ref is the inputted flight reference to be searched for
        map.put("AND DEPARTURELOCATION = ",departureLoco); //DEPARTURELOCATION is the Column we are searching through, departureLoco is the inputted departure location to be searched for
        map.put("AND ARRIVALLOCATION = ",arrivalLocation); //ARRIVALLOCATION is the Column we are searching through, arrivalLocation is the inputted arrival location to be searched for
        try
        {
            map.put("AND PLANE = ",plane.toString()); //PLANE is the Column we are searching through, plane is the inputted plane-ref to be searched for
        }
        catch (Exception ex)
        {
        }

        //*********************************************************************************
        //*********************************************************************************


        //********************** Adjusting Search Requirements Based On the Items the User is Searching For *********************
        //***********************************************************************************************************************


        if( dateWithNoTime && !timeWithNoDate) // scenario where date is inputted with no time, must be dealt with differently as I cast all timestamps into date format, so I can compare with their date only and disregard time
        {
            System.out.println("Date cast");
            map.put("AND CAST( DATEOFDEPARTURE AS Date ) = ",Date.from(dateAndTimeOfDeparture.atZone(ZoneId.systemDefault()).toInstant()));
        }
        else if( timeWithNoDate && !dateWithNoTime) // scenario where time is inputted with no dare, must be dealt with differently as I cast all timestamps into time format, so I can compare with their time only and disregard date
        {
            map.put("AND CAST( DATEOFDEPARTURE AS TIME ) = ", Time.valueOf(dateAndTimeOfDeparture.toLocalTime()));
            System.out.println("Time Cast");
        }
        else if(!dateWithNoTime && !timeWithNoDate) // scenario where date is inputted with time or no time or date is inputted
        {
            System.out.println("No Cast");

            try //Exception is thrown if no time or date is inputted
            {
                map.put("AND DATEOFDEPARTURE = ",Timestamp.valueOf(dateAndTimeOfDeparture));
            }
            catch (Exception ex)
            {

            }
        }

        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while ( iterator.hasNext()) //iterating through all the map entries
        {
            Map.Entry<String, Object> entry = iterator.next();

            if(entry.getValue() == null)  // if any of the entries values are null the entire entry is removed as the user placed no restriction on that attribute for search
            {
                iterator.remove(); // remove the checking requirement as the user put no search criteria
            }
            else if( entry.getValue() instanceof String) //if the entry is not null I have to check if it's just a bunch of whitespaces, I do this differently based on the object type i.e. String in this case
            {
                if(((String)entry.getValue()).isBlank() || ((String)entry.getValue()).isEmpty()) //Checks if the input is made of white spaces or is empty
                {
                    iterator.remove(); // remove the checking requirement as the user put no search criteria
                }
            }
            else if( entry.getValue() instanceof LocalDate || entry.getValue() instanceof LocalDateTime || entry.getValue() instanceof LocalTime) //if the entry is not null I have to check if it's just a bunch of whitespaces, I do this differently based on the object type i.e. LocalDate,LocalDateTime, or LocalTome
            {
                if(((entry.getValue()).toString().isEmpty() ||(entry.getValue()).toString().isBlank() )) //Checks if the input is made of white spaces or is empty
                {
                    iterator.remove();// remove the checking requirement as the user put no search criteria
                }
            }

        }

        //***********************************************************************************************************************
        //***********************************************************************************************************************

        ArrayList<Flight> listOfMatchedFlights = flightAdapter.findRecords(map,new Flight());
        currentFlightData.clear();
        currentFlightData.addAll(listOfMatchedFlights);

        //************************************************************************************************************************************************************************
        //************************************************************************************************************************************************************************

    }

    public void onReset() //resetting the search criteria to no selected values
    {
        //********* re-setting the UI controls ********
        //*********************************************

        flightRefText.setText(null);
        departureLocationCombo.getSelectionModel().clearSelection();
        dateOfDepartureDatePicker.setValue(null);
        timeOfDepartureCombo.getSelectionModel().clearSelection();
        arrivalToCombo.getSelectionModel().clearSelection();
        planeCombo.getSelectionModel().clearSelection();
        pm$AmCheck.setSelected(false);

        //*********************************************
        //*********************************************

        //********** re-setting table *****************
        //*********************************************

        currentFlightData.clear();

        LinkedHashMap<String , Object> map = new LinkedHashMap<>(); // Instantiating A linkedHashMap where each Key will hold a String identifying the DB Table column in question, and each Value will hold the object we are searching for in the fore mentioned Table Column
        map.put("FLIGHTOWNER = ",refId); //FlightOwner is the Column we are searching through, refId is the referenceID of the person logged in currently
        try {
            currentFlightData.addAll(flightAdapter.findRecords(map,new Flight()));  // running findRecords() method risks throwing a SQLException which is a checked exception and as such must be handled explicitly
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //*********************************************
        //*********************************************



    }

    public void onModify() throws IOException, ParseException, SQLException //method for when modify button is clicked
    {

        //**************** Opening Modify-view ***************
        //****************************************************
        String reference = "Modify-view.fxml"; // file name for FXML file we are opening

        FXMLLoader fxmlLoader = new FXMLLoader(AirWingApplication.class.getResource(reference)); //declaring and instantiating an FXMLLoader class object which parses FXML content ->
        // and initializes the controller of a JAVAFX view
        // initialized with the location of the FXML file being parsed, in this scenario AirWingApplication.class returns the class object of AirWingApplication ->
        // and .getResource(reference) returns a URL object that points to the resource specified by the reference string
        Stage stage= new Stage(); // Stage represents a window in the JavaFX desktop application
        Scene scene = new Scene(fxmlLoader.load()); // Scene holds the content that will be displayed in the Stage, fxmlLoader.load() will parse through the FXML file, instantiate defined UI components, and initialize its controller
        // fxmlLoader.load() will ultimately construct the scene graph based on the properties defined in the FXML file, it will also return the root node of said scene graph
        ((ModifyController)fxmlLoader.getController()).setFlight(flightForModification);
        stage.setScene(scene); // the scene of the stage is set
        stage.initModality(Modality.APPLICATION_MODAL); //defines interactions allowed with other windows while this one is open, APPLICATION_MODAL means other windows cant be interacted with while this oen is open
        stage.show(); //Shows the window in the JavaFX desktop application
        //****************************************************
        //****************************************************
    }



    public void save() throws SQLException
    {
        Flight[] flights = flightData.toArray(new Flight[0]);
        List<Flight> flightsList = new ArrayList<>(Arrays.asList(flights));
        Collections.reverse(flightsList);
        flights = flightsList.toArray(new Flight[0]);

        LinkedHashMap<String ,Object> map = new LinkedHashMap<>();

        for(int i =0 ; i<flights.length;i++)
        {
            Timestamp dum = Timestamp.valueOf(flights[i].retrieveDateOfDeparture());
            Timestamp dum2 = Timestamp.valueOf(flights[i].retrieveDateOfArrival());

            map.put("FLIGHTREFERENCEID ",flights[i].retrieveRefId());
            map.put("DepartureLocation ", flights[i].retrieveDepartureLocation());
            map.put("DateOfDeparture ",dum);
            map.put("ArrivalLocation ",flights[i].retrieveArrivalLocation());
            map.put("Frequency",flights[i].retrieveFrequency());
            map.put("Plane",flights[i].retrievePlane().getRefId().get());
            map.put("DateOfArrival ",dum2);
            map.put("StatusOfFlight ",flights[i].retrieveStatusOfFlight());
            if(flights[i].retrieveNextFlight()==null)
            {
                map.put("NextFlight ",null);
                System.out.println("yupo");
            }
            else
            {
                map.put("NextFlight ",flights[i].retrieveNextFlight().retrieveRefId());
                System.out.println("cappary");
            }
            map.put("TimeOfArrival",flights[i].retrieveTimeOfArrivalString());
            map.put("FlightOwner",refId);
            flightAdapter.addRecord(map);
            map.clear();
        }
        saveFlightPath();

        //************* Updating Table of current flights for Modify/Edit *************
        //*****************************************************************************
        currentFlightData.addAll(flightsList); //adding the list of new flights
        //*****************************************************************************
        //*****************************************************************************
        flightData.clear();


    }

    public void saveFlightPath() throws SQLException
    {

        FlightPath[] flightPathArray = flightPath.toArray(new FlightPath[0]);
        List<FlightPath> flightPathList = new ArrayList<>(Arrays.asList(flightPathArray));
        Collections.reverse(flightPathList);
        flightPathArray = flightPathList.toArray(new FlightPath[0]);

        LinkedHashMap<String ,Object> map = new LinkedHashMap<>();

        for(int i =0 ; i<flightPathArray.length;i++)
        {
            map.put("FlightPathRefID",flightPathArray[i].retrieverRefId());
            map.put("DepartingFlight",flightPathArray[i].retrieveDepartingFlightRef());
            map.put("ReturningFlight",flightPathArray[i].retrieveArrivingFlightRefId());
            map.put("Plane",flightPathArray[i].retrieverPlaneRefId());
            map.put("FLIGHTPATHOWNER",refId);
            map.put("FREQUENCYFLIGHTPATH",flightPathArray[i].retrieveFrequencyFlight());


            FlightPathAdapter.addRecord(map);
            map.clear();
        }

    }




    @Override
    public void setRefID(String refID)
    {
        this.refId = refID;

    }







    public void setTab()
    {
        tabPane.getSelectionModel().select(1); //set tab to Modify/Edit
    }


}
