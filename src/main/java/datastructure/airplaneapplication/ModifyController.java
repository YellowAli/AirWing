package datastructure.airplaneapplication;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static datastructure.airplaneapplication.KeyCreation.returnKeyFrequencyFlight;

public class ModifyController implements Initializable
{
    private Boolean flightAway ;
    private Boolean changeDateAndTimeOfArrival = false;
    private Boolean arrivalLocationReturnChanged = false;
    private Boolean planeReturnChanged = false;
    private ObservableList<FlightPath> observableList;
    private Iterator<FlightPath> pathIterator;
    private ObservableList<Flight> flightData = FXCollections.observableArrayList();
    private ObservableList<FlightPath> flightPath  = FXCollections.observableArrayList();
    private ArrayList<String> refs = new ArrayList<>();
    private boolean safeToAdd = false;
    private FlightPath OGFlightPathComeBack;
    private boolean check1 = false;
    private Flight planeChangedBringFlight;
    private AnchorPane root;
    private boolean confirmation;
    private GeneralAdapter flightPathAdapter;
    private final String flightPathTableName = "FLIGHTPATH";
    private final String[] flightPathTableInfo = {
            "FlightPathRefID VARCHAR(1000)", "DepartingFlight Varchar(1000) NOT NULL REFERENCES Flights(FLIGHTREFERENCEID) DEFAULT 'default_value'", "ReturningFlight Varchar(1000) NOT NULL REFERENCES Flights(FLIGHTREFERENCEID) DEFAULT 'default_value'", "Plane VARCHAR(1000) NOT NULL REFERENCES OWNEDPLANES(PlaneReferenceID) DEFAULT 'default_value'", "FLIGHTPATHOWNER VARCHAR(30) NOT NULL REFERENCES ACCOUNT(REFERENCEID) DEFAULT 'default_value'", "FREQUENCYFLIGHTPATH VARCHAR(30)"

    };


    private GeneralAdapter flightAdapter;
    private final String[] flightInfo = {
            "FLIGHTREFERENCEID VARCHAR(1000)", "DepartureLocation VARCHAR(1000)", "DateOfDeparture TIMESTAMP",
            "ArrivalLocation VARCHAR(1000)", "Frequency VARCHAR(1000)",
            "Plane VARCHAR(1000) NOT NULL REFERENCES OWNEDPLANES(PlaneReferenceID) DEFAULT 'default_value'", "DateOfArrival TIMESTAMP", "StatusOfFlight VARCHAR(1000)", "NextFlight VARCHAR(1000) REFERENCES Flights(FLIGHTREFERENCEID) DEFAULT 'default_value'", "TimeOFArrival VARCHAR(1000)", "FLIGHTOWNER VARCHAR(30) NOT NULL REFERENCES ACCOUNT(REFERENCEID) DEFAULT 'default_value'"
    };
    private final String flightTableName = "Flights";

    private GeneralAdapter ownedPlaneAdapter;
    private final String ownedPlaneTableName = "OWNEDPLANES";
    private final String[] ownedPlaneTableInfo = {"PlaneReferenceID VARCHAR(1000)", "Type VARCHAR(1000)", "Performance VARCHAR(1000)", //OwnedPlaneInfo is an Array of Strings which holds the specifications of each column in the OwnedPlanes DB,
            "Weight VARCHAR(1000)", "Dimension VARCHAR(1000)", "FlightCrewCapacity DOUBLE",      // a necessary entry in my GeneralAdapter constructor
            "PassengerCapacity DOUBLE", "CruisingSpeed DOUBLE", "ReferenceID VARCHAR(30) NOT NULL REFERENCES Account(REFERENCEID) DEFAULT 'default_value'"
    };
    ObservableList<String> severityOfChange = FXCollections.observableArrayList();
    ObservableList<OwnedPlane> ownedPlanes = FXCollections.observableArrayList();
    ObservableList<String> statusOfFlight = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> arrivalLocationCombo;

    @FXML
    private DatePicker dateOfArrivalText;

    @FXML
    private DatePicker dateOfDepartureDatePicker;

    @FXML
    private ComboBox<String> departureLocationCombo;

    @FXML
    private TextField flightRefIdField;

    @FXML
    private TextField nextFlightText;

    @FXML
    private ComboBox<OwnedPlane> planeCombo;

    @FXML
    private ComboBox<String> severityOfChangeCombo;

    @FXML
    private ComboBox<String> statusOfFlightCombo;

    @FXML
    private ComboBox<String > timeOfArrivalText;

    @FXML
    private ComboBox<String> timeOfDepartureComboBox;
    @FXML
    private CheckBox PmOrAm;

    @FXML
    private CheckBox PmOrAm1;

    private Flight flightToBeModified;

    public ModifyController() throws SQLException
    {
        flightPathAdapter = new GeneralAdapter(false, flightPathTableName, flightPathTableInfo);
        flightAdapter = new GeneralAdapter(false, flightTableName, flightInfo);
    }


    public void setFlight(Flight flight) throws ParseException //Method used by flightsController to set this classes flight which will be modified
    {
        flightToBeModified = flight; // This classes attribute flightToBeModified references the flight obtained from this methods argument
        buildData();// Now that flightToBeModified si declared and initialized buildData is called to initialize all the UI components of the Modify-View
    }

    public void buildData() throws ParseException //initializes all the UI components of Modify-View
    {
        flightRefIdField.setText(flightToBeModified.retrieveRefId());
        dateOfDepartureDatePicker.setValue(flightToBeModified.retrieveDateOfDeparture().toLocalDate());
        dateOfArrivalText.setValue(flightToBeModified.retrieveDateOfArrival().toLocalDate());

        //nextFlightText.setText(flightToBeModified.retrieveNextFlight().retrieveRefId());

        String[] lis = {"1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00","10:00","11:00","12:00"};
        ArrayList<String> TimeList = new ArrayList<>(List.of(lis));
        ObservableList<String> TimeListObservable = FXCollections.observableArrayList();
        TimeListObservable.addAll(TimeList);
        timeOfDepartureComboBox.setItems(TimeListObservable);
        ArrivalEstimator.convertTo12HrTime(flightToBeModified.retrieveTimeOfDeparture().toString());
        timeOfDepartureComboBox.getSelectionModel().select(ArrivalEstimator.convertTo12HrTime(flightToBeModified.retrieveTimeOfDeparture().toString()));
        if(flightToBeModified.retrieveTimeOfDeparture().getHour() >= 12)
            PmOrAm.setSelected(true);
        else
            PmOrAm.setSelected(false);


        timeOfArrivalText.setItems(TimeListObservable);
        ArrivalEstimator.convertTo12HrTime(flightToBeModified.retrieveTimeOfArrival().toString());
        timeOfArrivalText.getSelectionModel().select(ArrivalEstimator.convertTo12HrTime(flightToBeModified.retrieveTimeOfArrival().toString()));
        if(flightToBeModified.retrieveTimeOfArrival().getHour() >= 12)
            PmOrAm1.setSelected(true);
        else
            PmOrAm1.setSelected(false);

        ArrayList<String> preSetArrivalLocationList = new ArrayList<>();
        preSetArrivalLocationList.add(flightToBeModified.retrieveArrivalLocation());
        ObservableList<String> preSetArrivalLocation = FXCollections.observableArrayList(preSetArrivalLocationList);
        arrivalLocationCombo.setItems(preSetArrivalLocation);
        arrivalLocationCombo.getSelectionModel().select(0);

        ArrayList<String> preSetDepartureLocationList = new ArrayList<>();
        preSetDepartureLocationList.add(flightToBeModified.retrieveDepartureLocation());
        ObservableList<String> preSetDepartureLocation = FXCollections.observableArrayList(preSetDepartureLocationList);
        departureLocationCombo.setItems(preSetDepartureLocation);
        departureLocationCombo.getSelectionModel().select(0);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) //initializes the remaining UI components not set in buildData
    {
        //*************** Setting up the severityOfChange Combo Box ***************
        //*************************************************************************

        ArrayList<String> severity = new ArrayList<>();
        severity.add("Only This Flight");
        severity.add("This Flights And All Following Flights");
        severityOfChange.addAll(severity);
        severityOfChangeCombo.setItems(severityOfChange);

        //*************************************************************************
        //*************************************************************************


        //******************************************* Setting Up remaining UI elements which depend on information received after initialization Therefore Platform.runlater() must be used *******************************************
        //*****************************************************************************************************************************************************************************************************************************

        Platform.runLater(() ->
        {

            //*************** Setting up the Planes Combo Box *************************
            //*************************************************************************

            ArrayList<OwnedPlane> planes;

            try {
                ownedPlaneAdapter = new GeneralAdapter(false, ownedPlaneTableName, ownedPlaneTableInfo);
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();

                map.put("REFERENCEID = ", flightToBeModified.retrieveFlightOwner());

                planes = ownedPlaneAdapter.findRecords(map, new OwnedPlane());

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            ownedPlanes.addAll(planes);
            planeCombo.setItems(ownedPlanes);
            planeCombo.getSelectionModel().select(flightToBeModified.retrievePlane());

            //*************************************************************************
            //*************************************************************************


            //*************** Setting up the Status Of Flight Combo Box *************************
            //***********************************************************************************

            ArrayList<String> statusOfFlightList = new ArrayList<>();
            statusOfFlightList.add("Establishing");
            statusOfFlightList.add("Established");
            statusOfFlightList.add("Cancelled");
            statusOfFlightList.add("Delayed");
            statusOfFlightList.add("Arrived");
            statusOfFlight.addAll(statusOfFlightList);
            statusOfFlightCombo.setItems(statusOfFlight);
            statusOfFlightCombo.getSelectionModel().select(flightToBeModified.retrieveStatusOfFlight());

            //***********************************************************************************
            //***********************************************************************************


            //*****************************************************************************************************************************************************************************************************************************
            //*****************************************************************************************************************************************************************************************************************************


        });


    }

    public void onSave() throws SQLException, IOException, ParseException //Checking Safety of changes made before committing them
    {
        //************************** Checking What Has Been Changed To React Accordingly **************************
        //*********************************************************************************************************

        ArrayList<Flight> flightsToBeModified = new ArrayList<>();
        ArrayList<FlightPath> flightPathsToBeModified = new ArrayList<>();


        //************************** Declaring And/Or Instantiating All Necessary tools **************************
        //********************************************************************************************************

        ArrivalEstimator arrivalEstimator = new ArrivalEstimator(planeCombo.getValue(), arrivalLocationCombo.getValue(), departureLocationCombo.getValue(), ownedPlaneAdapter); //Class with methods to calculate departure and arrival times


        // If this is away flight, changes made to it may also affect the returning flight
        flightAway = flightToBeModified.retrieveNextFlight() != null;

        Flight flight = null;

        //********************************************************************************************************
        //********************************************************************************************************


        //******************* Checking Exceptional Cases Which May Affect Severity Of Change *******************
        //******************************************************************************************************

        boolean proceed;
        proceed = ExceptionalScenarios();

        if(!proceed)
            return;

        //******************************************************************************************************
        //******************************************************************************************************


        //************************* Creating Array Of Flights That Must Be Changed Based On Chosen Severity Of Change *************************
        //*************************************************************************************************************************************

            ArrayList<FlightPath> sameFrequencyFlightPaths = new ArrayList<>(); // Array List that holds FlightPaths in same frequency list
            ArrayList<Flight> sameFrequencyFlight = new ArrayList<>(); //Array List that holds Flights from same Frequency List

            sameFrequencyFlightPaths = CreatingArrayOfFlights(flightAway,sameFrequencyFlightPaths,sameFrequencyFlight);


        //*************************************************************************************************************************************
        //*************************************************************************************************************************************


        //****************************************** Calculating Date And Time Of Arrival/Departure ******************************************
        //************************************************************************************************************************************
        LocalDateTime dateAndTimeOfDeparture = arrivalEstimator.getDateAndTimeOfDeparture(PmOrAm, timeOfDepartureComboBox, dateOfDepartureDatePicker); //date and time of departure chosen by user is calculated
        //************************************************************************************************************************************
        //************************************************************************************************************************************


        //****************************************** Retrieving Complete Flight Path for this Plane ******************************************
        //************************************************************************************************************************************

        Iterator<FlightPath> sameFrequencyFlightPathsIterator = sameFrequencyFlightPaths.iterator(); // an iterator for flight paths
        while(sameFrequencyFlightPathsIterator.hasNext())
        {
            FlightPath path = sameFrequencyFlightPathsIterator.next();
            Flight returning = path.retrieveReturningFlight();

            String ReferenceFirstHalfKey = returning.retrieveReturningFlightListRef().split("-")[0];
            LinkedHashMap<String,Object> map = new LinkedHashMap<>();
            map.put("RETURNINGFLIGHTLISTREF =",ReferenceFirstHalfKey + "-%");

            ArrayList<Flight> arrayListOfFlights = flightAdapter.findRecords(map,new Flight());
            arrayListOfFlights.add(path.retrieveDepartingFlight());

            int position = FlightValidator.insertionSortAdjusted2(arrayListOfFlights,flightToBeModified);

            path.setCompleteFlightPath(arrayListOfFlights,position);

        }

        //************************************************************************************************************************************
        //************************************************************************************************************************************

        sameFrequencyFlightPathsIterator = sameFrequencyFlightPaths.iterator();

        for (int i = 0; i < sameFrequencyFlight.size(); i++) // iterating through all the flights of same frequency
        {
            //**************** Retrieving Information From flights Matching Flight Path ****************
            //******************************************************************************************

            boolean canContinue = false;
            Flight prevFlight = null;
            Flight nextFlight = null;

            String flightPathRefId = null;
            String ownerOfFlightPath = null;
            String frequencyOfFlightPath = null;
            Flight returningFlightDepartingFlight = null;

            if(sameFrequencyFlightPathsIterator.hasNext())
            {
                FlightPath thisFlightPath = sameFrequencyFlightPathsIterator.next();
                flightPathRefId = thisFlightPath.retrieverRefId();
                ownerOfFlightPath = thisFlightPath.retrieveOwner();
                frequencyOfFlightPath = thisFlightPath.retrieveFrequencyFlight();
                returningFlightDepartingFlight = thisFlightPath.retrieveDepartingFlight();


                if(thisFlightPath.retrievePosition()!=0)
                {
                    prevFlight = thisFlightPath.retrieveCompleteFlightPath().get(thisFlightPath.retrievePosition()-1);
                }

                if(thisFlightPath.retrievePosition()!=thisFlightPath.retrieveCompleteFlightPath().size()-1)
                {
                    nextFlight = thisFlightPath.retrieveCompleteFlightPath().get(thisFlightPath.retrievePosition()+1);
                }

                canContinue = true;
            }
            else
            {

                String ReferenceFirstHalfKey = flight.retrieveReturningFlightListRef().split("-")[0];
                LinkedHashMap<String,Object> map = new LinkedHashMap<>();
                map.put("RETURNINGFLIGHTLISTREF =",ReferenceFirstHalfKey + "-%");

                ArrayList<Flight> arrayListOfFlights = flightAdapter.findRecords(map,new Flight());

                FlightValidator.insertionSortAdjusted2(arrayListOfFlights,flightToBeModified);

                LinkedHashMap<String,Object> map1 = new LinkedHashMap<>();
                map1.put("RETURNINGFLIGHT = ",arrayListOfFlights.get(arrayListOfFlights.size()-1));
                Flight departing = flightPathAdapter.findRecords(map1,new FlightPath()).getFirst().retrieveDepartingFlight();

                arrayListOfFlights.add(departing);
                int position = FlightValidator.insertionSortAdjusted2(arrayListOfFlights,flightToBeModified);

                if(position!=0)
                {
                    prevFlight = arrayListOfFlights.get(position-1);
                }

                if(position!= arrayListOfFlights.size()-1)
                {
                    nextFlight = arrayListOfFlights.get(position+1);
                }


            }



            //******************************************************************************************
            //******************************************************************************************


            long intervalToAdd = 0; //this will later be initialized to the number of days between flights, for example 7,30, etc.
            Flight departingFlightReturningFlight = new Flight(); // the returning flight if the away flight is being modified


            //************* Instantiating Flights that Will Be Modified *************
            //***********************************************************************

            Flight flightToBeModifiedInfo = sameFrequencyFlight.get(i); //The flightToBeModified

            if (flightAway)  // if this flight Away I instantiate the Flight object that will be changed i.e. 'flight' along with a returning flight object as it may be affected
            {
                flight = new Flight(flightToBeModifiedInfo.retrieveRefId(), flightToBeModifiedInfo.retrieveDepartureLocation(), flightToBeModifiedInfo.retrieveDateOfDeparture(), flightToBeModifiedInfo.retrieveArrivalLocation(), flightToBeModifiedInfo.retrieveFrequency(), flightToBeModifiedInfo.retrievePlane(), flightToBeModifiedInfo.retrieveDateOfArrival(), flightToBeModifiedInfo.retrieveStatusOfFlight(), flightToBeModifiedInfo.retrieveNextFlight(), flightToBeModifiedInfo.retrieveTimeOfArrivalString(),null, flightToBeModifiedInfo.retrieveFlightOwner(), 1); //The flight I will make the requested changes to and check if the changes are safe, (flight away)

                Flight flightTobeModifiedNext = flightToBeModifiedInfo.retrieveNextFlight();
                departingFlightReturningFlight = new Flight(flightTobeModifiedNext.retrieveRefId(), flightTobeModifiedNext.retrieveDepartureLocation(), flightTobeModifiedNext.retrieveDateOfDeparture(), flightTobeModifiedNext.retrieveArrivalLocation(), flightTobeModifiedNext.retrieveFrequency(), flightTobeModifiedNext.retrievePlane(), flightTobeModifiedNext.retrieveDateOfArrival(), flightTobeModifiedNext.retrieveStatusOfFlight(), flightTobeModifiedNext.retrieveNextFlight(), flightTobeModifiedNext.retrieveTimeOfArrivalString(), flightTobeModifiedNext.retrieveFlightOwner(), flightTobeModifiedNext.retrieveReturningFlightListRef());

            }
            else
            {

                flight = new Flight(flightToBeModifiedInfo.retrieveRefId(), flightToBeModifiedInfo.retrieveDepartureLocation(), flightToBeModifiedInfo.retrieveDateOfDeparture(), flightToBeModifiedInfo.retrieveArrivalLocation(), flightToBeModifiedInfo.retrieveFrequency(), flightToBeModifiedInfo.retrievePlane(), flightToBeModifiedInfo.retrieveDateOfArrival(), flightToBeModifiedInfo.retrieveStatusOfFlight(), flightToBeModifiedInfo.retrieveNextFlight(), flightToBeModifiedInfo.retrieveTimeOfArrivalString(), flightToBeModifiedInfo.retrieveFlightOwner(), flightToBeModifiedInfo.retrieveReturningFlightListRef()); //The flight I will make the requested changes to and check if the changes are safe, (returning flight)

            }

            //***********************************************************************
            //***********************************************************************


            //************* Calculating Interval To Add To Dates *************
            //****************************************************************

            if (i != 0)
            {
                intervalToAdd = ChronoUnit.DAYS.between(flightToBeModified.retrieveDateOfDeparture(), flight.retrieveDateOfDeparture()); //The interval to add is the difference between the original flight
                // and the flight we are examining from the frequency list
            }

            //****************************************************************
            //****************************************************************


            //************************** Making Requested Changes  ****************************************************************************************************************************************************************************************
            //*********************************************************************************************************************************************************************************************************************************************


            //***************** Date And Time Of Departure Is Changed *****************

            if (dateAndTimeOfDeparture.plusDays(intervalToAdd).isEqual(flightToBeModifiedInfo.retrieveDateOfDeparture())) //comparing dateAndTimeOfDeparture chosen by user to that of the flight to be modified
            {

                if(prevFlight == null|| (ChronoUnit.HOURS.between(prevFlight.retrieveDateOfArrival(),dateAndTimeOfDeparture.plusDays(intervalToAdd)) >=2))
                {

                    flight.setDateOfDeparture(dateAndTimeOfDeparture.plusDays(intervalToAdd)); // if the user changed the date and time of departure that change is made to flight
                    changeDateAndTimeOfArrival = true; //date and time of arrival must be changed
                    //Make sure if this is a flight away that the date of departure of the returning flight is adjusted

                    proceed = true;
                }
                else
                {
                    openWarning("Date Of Departure must be minimum two hours following the arrival of previous flight");
                    proceed = false;
                }

                if(!proceed)
                    return;

            }

            //*************************************************************************

            //***************** Arrival Location Changed *****************

            changeDateAndTimeOfArrival = ArrivalLocationChanged(flightAway,flight,flightToBeModifiedInfo,nextFlight);

            //***********************************************************

            //********************** Plane Is Changed **********************

            changeDateAndTimeOfArrival = PlaneChanged(flightAway,flightToBeModifiedInfo,flight,departingFlightReturningFlight);

            //**************************************************************



            //************************** Changing Date and Time Of Arrival Of Flight if Boolean Flag Became True **************************
            //****************************************************************************************************************************
            if(changeDateAndTimeOfArrival)
            {
                proceed = changeDateAndTimeOfArrival(intervalToAdd,flight,dateAndTimeOfDeparture,arrivalEstimator, nextFlight);

                if(!proceed)
                    return;
            }

            //****************************************************************************************************************************
            //****************************************************************************************************************************

            //************************** Changing Date and Time Of Arrival Of Flight if Boolean Flag is False ****************************
            //****************************************************************************************************************************
            if(!changeDateAndTimeOfArrival)
            {
                proceed = changeDateAndTimeOfArrival(intervalToAdd,flight,dateAndTimeOfDeparture,arrivalEstimator, nextFlight);

                if(!proceed)
                    return;
            }

            //****************************************************************************************************************************
            //****************************************************************************************************************************

            //*********************************************************************************************************************************************************************************************************************************************
            //*********************************************************************************************************************************************************************************************************************************************


            //********************************** Adding Modified FlightPaths to List Of Flights To Be Modified **********************************
            //***********************************************************************************************************************************
            // Ok so either im changing the away flight, or im changing a random returning flight, or im changing THE returning flight
            if (flightAway && canContinue)
            {
                flightPathsToBeModified.add(new FlightPath(flightPathRefId, flight, departingFlightReturningFlight, ownerOfFlightPath, frequencyOfFlightPath));
            }
            else if (canContinue)
            {
                flightPathsToBeModified.add(new FlightPath(flightPathRefId, returningFlightDepartingFlight, flight, ownerOfFlightPath, frequencyOfFlightPath));

            }
            flightsToBeModified.add(flight);
            //***********************************************************************************************************************************
            //***********************************************************************************************************************************


        }

        //*******************************************************************************
        //*******************************************************************************

        //************************** Removing FlightPath Which Will Be Modified **************************
        removingModifiedFlightPath();
        //************************************************************************************************

        //*************** Make necessary Flights If Return Flight And Arrival Change ***************
        if (arrivalLocationReturnChanged)
        {

           reactingToArrivalLocationChanged(flight,flightsToBeModified,flightPathsToBeModified);

        }
        //******************************************************************************************

        //*************** Make necessary Flights If Return Flight And Plane Change ***************
        if (planeReturnChanged)
        {
            reactingToPlaneChange(flight,flightsToBeModified,flightPathsToBeModified);

        }
        //****************************************************************************************

//
//        //*************** Check If Flight Is Safe To Add and Add ***************
//        //**********************************************************************
            pathIterator = flightPathsToBeModified.iterator();
            FlightValidator validator = new FlightValidator(); //Creating an instance of the FlightValidator Class to Check the safety of the changes made, change validator so the observableList passed is all thats needed;
            boolean safeToChange = false;
            boolean permissionGiven = false;

            while (pathIterator.hasNext())
            {
                FlightPath flightPath = pathIterator.next();
                safeToChange = validator.checkIfSafeToAdd(observableList,flightPath);

                if(!safeToChange)
                {
                    //get permission
                    break;
                }
                
            }

            if(safeToChange || permissionGiven)
            {

            }



//
//        //**************************************************************
//        //**************************************************************
    }



    public void setConfirmation(boolean confirmation)
    {
        this.confirmation = confirmation;
    }

    public void openWarning(String warning) throws IOException
    {
        FXMLLoader fxmlLoader = FXMLLoader.load(AirWingApplication.class.getResource("Warning-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        WarningController warningController = fxmlLoader.getController();
        warningController.setErrorMessage(warning);
        warningController.setAccessingController(this);
        stage.show();

    }

    public CreatePlaneExtensionsFlightController createNecessaryFlights() throws IOException
    {
        FXMLLoader fxmlLoader = FXMLLoader.load(AirWingApplication.class.getResource("CreatePlaneExtensionFlights-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        return fxmlLoader.getController();
    }

    public void setCheck1(boolean check1,Flight flight)
    {
        this.check1 = check1;
        planeChangedBringFlight = flight;
    }

    public void setSafeToAdd(boolean safeToAdd, FlightPath flightPath)
    {
        this.safeToAdd = safeToAdd;
        OGFlightPathComeBack = flightPath;
    }

    public boolean changeDateAndTimeOfArrival( long intervalToAdd, Flight flight,  LocalDateTime dateAndTimeOfDeparture, ArrivalEstimator arrivalEstimator , Flight nextFlight) throws SQLException, IOException, ParseException
    {
        boolean proceed;

        LocalDateTime dateOfArrival = arrivalEstimator.getDateOfArrival(dateAndTimeOfDeparture.plusDays(intervalToAdd)); // date and time of arrival based on user chosen date of departure is calculated


        //Ask user if they would like this to be the new date of arrival, in case they already changed the date
        confirmation = false;
        openWarning("Would You Like Arrival Date To Be Set To Computed Arrival Time");
        if(confirmation)
        {
            if(nextFlight == null|| (ChronoUnit.HOURS.between(dateOfArrival,nextFlight.retrieveDateOfDeparture()) >=2))
            {
                flight.setDateOfArrival(dateOfArrival);
                flight.setTimeOfArrival(ArrivalEstimator.convertTo12HrTime(dateOfArrival.toString()));
                proceed = true;
            }
            else
            {
                AlertController alertController = new AlertController();
                alertController.setErrorMessage("The change in this flights arrival time does not meet the requirements of being a minimum of two hours prior to the next flights departure");
                proceed = false;
            }

        }
        else
        {
            if( (ChronoUnit.HOURS.between(dateOfArrival,nextFlight.retrieveDateOfDeparture()) >=2))
            {
                LocalDateTime arrivalDatetIme = arrivalEstimator.getDateAndTimeOfDeparture(PmOrAm1,timeOfArrivalText,dateOfArrivalText);
                flight.setDateOfArrival(arrivalDatetIme);
                flight.setTimeOfArrival(ArrivalEstimator.convertTo12HrTime(arrivalDatetIme.toLocalTime().toString()));
                proceed = true;
            }
            else
            {
                AlertController alertController = new AlertController();
                alertController.setErrorMessage("The change in this flights arrival time does not meet the requirements of being a minimum of two hours prior to the next flights departure");
                proceed = false;
            }

        }


        return proceed;



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
            map.put("FlightOwner",flights[i].retrieveFlightOwner());
            flightAdapter.addRecord(map);
            map.clear();
        }
        saveFlightPath();


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
            map.put("FLIGHTPATHOWNER",flightPathArray[i].retrieverRefId());
            map.put("FREQUENCYFLIGHTPATH",flightPathArray[i].retrieveFrequencyFlight());


            flightPathAdapter.addRecord(map);
            map.clear();
        }

    }

    public void reactingToPlaneChange(Flight flight, ArrayList<Flight> flightsToBeModified,ArrayList<FlightPath> flightPathsToBeModified) throws IOException
    {
        flight = flightsToBeModified.getFirst();
        CreatePlaneExtensionsFlightController controller = createNecessaryFlights();
        Flight returningFlightDepartingFlight = flightPathsToBeModified.getFirst().retrieveDepartingFlight();

        //************* Creating ReferenceLis Ids for Returning Flight *************
        //**************************************************************************

        String ReferenceFirstHalfKey = flight.retrieveReturningFlightListRef().split("-")[0];

        String returningFlightListRef = KeyCreation.returnKey(refs, ReferenceFirstHalfKey + "-"); //initializing the key of the return flight

        refs.add(returningFlightListRef);

        //**************************************************************************
        //**************************************************************************


        controller.setInfo(returningFlightDepartingFlight.retrieveDepartureLocation(), flight.retrieveDepartureLocation(), flight.retrievePlane(), returningFlightListRef, flight.retrieveFlightOwner(), flight, this);
        controller.setReturningFlightDepartingFlight(returningFlightDepartingFlight);
    }

    public void reactingToArrivalLocationChanged(Flight flight, ArrayList<Flight> flightsToBeModified,ArrayList<FlightPath> flightPathsToBeModified) throws IOException
    {
        flight = flightsToBeModified.getFirst();
        Flight returningFlightDepartingFlight = flightPathsToBeModified.getFirst().retrieveDepartingFlight();

        //************* Creating ReferenceLis Ids for Returning Flight *************
        //**************************************************************************

        String ReferenceFirstHalfKey = flight.retrieveReturningFlightListRef().split("-")[0];

        String returningFlightListRef = KeyCreation.returnKey(refs, ReferenceFirstHalfKey + "-"); //initializing the key of the return flight

        refs.add(returningFlightListRef);

        //**************************************************************************
        //**************************************************************************

        CreatePlaneExtensionsFlightController extensionFlightController = createNecessaryFlights();
        extensionFlightController.setInfo(flight.retrieveArrivalLocation(), returningFlightDepartingFlight.retrieveDepartureLocation(), flight.retrievePlane(), refs.getLast(), flight.retrieveFlightOwner(), flight, this);


        if (safeToAdd)
        {
            OGFlightPathComeBack.setRefId(flightPathsToBeModified.getFirst().retrieverRefId());
            flightPathsToBeModified.set(0, OGFlightPathComeBack);
        } else {
            return;
            //return error message saying you failed to change arrival location
        }


        safeToAdd = false;
    }

    public void removingModifiedFlightPath() throws SQLException
    {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        //linkedHashMap.put("FLIGHTPATHOWNER =",owner); I think I should find flightpaths with same plane
        linkedHashMap.put("PLANE = ", flightToBeModified.retrievePlane());
        ArrayList<FlightPath> listOfFlightPaths = flightPathAdapter.findRecords(linkedHashMap, new FlightPath());
        observableList = FXCollections.observableArrayList(listOfFlightPaths);

        pathIterator = observableList.iterator();



        while (pathIterator.hasNext()) {
            FlightPath flightPath = pathIterator.next();

            if ((Objects.equals(flightPath.retrieveArrivingFlightRefId(), flightToBeModified.retrieveRefId()) || Objects.equals(flightPath.retrieveDepartingFlightRef(), flightToBeModified.retrieveRefId()))) {
                pathIterator.remove();
            }

        }

    }


    public boolean PlaneChanged(boolean flightAway, Flight flightToBeModifiedInfo, Flight flight, Flight departingFlightReturningFlight)
    {
        flight.setPlane(planeCombo.getValue()); //flights plane is changed
        changeDateAndTimeOfArrival = true; //date and time of arrival has to be recalculated because planes velocity may be different

        if (flightAway && !(flightToBeModifiedInfo.retrievePlane().getRefId().get().equals(planeCombo.getValue().getRefId().get()))) //In the Event the Plane Chosen For Flight Changes and it's an Away Flight
        {

            departingFlightReturningFlight.setPlane(planeCombo.getValue()); //returning flights plane is changed

        }
        else if (!(flight.retrievePlane().getRefId().get().equals(planeCombo.getValue().getRefId().get()))) //In the Event the Plane Chosen For Flight Changes, and it's not an Away Flight
        {
            planeReturnChanged = true;

        }

        return changeDateAndTimeOfArrival;
    }

    public boolean ArrivalLocationChanged(boolean flightAway, Flight flight, Flight flightToBeModifiedInfo, Flight departingFlightReturningFlight)
    {
        flight.setArrivalLocation(arrivalLocationCombo.getValue()); //arrival location for flight changed
        changeDateAndTimeOfArrival = true; //boolean flag to re-calculate date and time of arrival

        if (flightAway && !Objects.equals(arrivalLocationCombo.getValue(), flightToBeModifiedInfo.retrieveArrivalLocation())) //If arrival location is changed and this is an away flight
        {

            // For now i'm going to have returning flight go straight back to hub
            departingFlightReturningFlight.setDepartureLocation(arrivalLocationCombo.getValue()); //returning flights departure location changed

            //Make sure the date and time of arrival of both departing and returning flight are changed

        }
        else if(!Objects.equals(arrivalLocationCombo.getValue(), flightToBeModifiedInfo.retrieveArrivalLocation()))//If arrival location is changed and this is a returning flight
        {
            arrivalLocationReturnChanged = true;
        }

        return  changeDateAndTimeOfArrival;
    }

    public boolean ExceptionalScenarios() throws IOException
    {
        if (!flightAway && !Objects.equals(arrivalLocationCombo.getValue(), flightToBeModified.retrieveArrivalLocation())) //if this is a returning flight and its arrival location is changed
        {
            openWarning("Changing Arrival Location Of Returning Flight Is Only To Be Done in Extreme Circumstances And Will Only Affect This Flight");//If arrival location is changed I will give them a notification saying this CANNOT be a "This Flights And All Following Flights" change and will check if theyre sure they want to do this
            if (confirmation) //If they confirm to continue despite warning
            {
                arrivalLocationReturnChanged = true; //boolean set that arrival Location for Return Flight Has Changed
                severityOfChangeCombo.getSelectionModel().selectFirst(); //Only this flight will be affected
            }
            else
            {
                return false;
            }
        }

        if (!flightAway && !(flightToBeModified.retrievePlane().getRefId().get().equals(planeCombo.getValue().getRefId().get()))) {
            openWarning("Changing Plane Of Returning Flight Is Only To Be Done in Extreme Circumstances And Will Only Affect This Flight");//If arrival location is changed I will give them a notification saying this CANNOT be a "This Flights And All Following Flights" change and will check if theyre sure they want to do this
            if (confirmation) //If they confirm to continue despite warning
            {
                planeReturnChanged = true; //boolean set that arrival Location for Return Flight Has Changed
                severityOfChangeCombo.getSelectionModel().selectFirst(); //Only this flight will be affected
            }
            else
            {
                return false;
            }

        }

        return true;
    }

    public ArrayList<FlightPath> CreatingArrayOfFlights(boolean flightAway, ArrayList<FlightPath> sameFrequencyFlightPaths , ArrayList<Flight> sameFrequencyFlight) throws SQLException {

        String mapCriteria; // map Criteria will hold the type of flights that will be held in sameFrequencyFlight; departingFlight or returningFlight

        if (flightAway)
            mapCriteria = "DEPARTINGFLIGHT ="; //if flight away the mapCriteria we are search for is Departing Flight
        else
            mapCriteria = "RETURNINGFLIGHT ="; // else it is returning Flight


        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put(mapCriteria, flightRefIdField.getText());
        ArrayList<FlightPath> tempFlightPathList = flightPathAdapter.findRecords(map, new FlightPath());

        FlightPath mainFlightPath = null;
        if (!tempFlightPathList.isEmpty())
        {
            mainFlightPath = tempFlightPathList.getFirst(); //searching for the flightpath which holds the flight To Be Modified
        }
        else
        {
            severityOfChangeCombo.getSelectionModel().selectFirst();
        }



        if (severityOfChangeCombo.getValue().equals("This Flights And All Following Flights")) //If Change is requested to be made for this flight and all following flights
        {

            String frequencyRef = mainFlightPath.retrieveFrequencyFlight(); //retrieve and storing the frequencyRef of that flightPath

            String frequencyRefFirstDigit = frequencyRef.split("-")[0]; // splitting the frequency ref at the "-"; 1-1 would be split into 1,1... storing the first digit

            map.clear();
            map.put("FREQUENCYFLIGHTPATH LIKE", frequencyRefFirstDigit + "-%");
            sameFrequencyFlightPaths = flightPathAdapter.findRecords(map, new FlightPath()); //Finding all flight paths in this particular frequency list... populating the earlier created << sameFrequencyFlightPaths >>

            for (FlightPath path : sameFrequencyFlightPaths)
            {
                if (flightAway)
                   sameFrequencyFlight.add( path.retrieveDepartingFlight()); //if we are modifying the away flights that will be added to same frequency Flight list
                else
                    sameFrequencyFlight.add(path.retrieveReturningFlight()); //if we are modifying the returning flights that will be added to same frequency Flight list

            }

        }
        else
        {
            sameFrequencyFlightPaths.add(mainFlightPath);
            sameFrequencyFlight.add(flightToBeModified);
        }

        return sameFrequencyFlightPaths;
    }







}

