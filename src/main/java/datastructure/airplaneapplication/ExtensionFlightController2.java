package datastructure.airplaneapplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ExtensionFlightController2 implements Initializable,InformationStackPane
{

    private ModifyController modifyController;
    private ModificationsBaseController modificationsBaseController;
    @FXML
    private StackPane root;
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
    private TableColumn<Flight, String> TimeDepartureColumn;


    @FXML
    private TextField TimeOfArrival;


    @FXML
    private ComboBox<String> TimeOfDeparture;


    @FXML
    private TextField arrivalTo;


    @FXML
    private AnchorPane screen1;

    @FXML
    private AnchorPane screen2;


    private ObservableList<Flight> flightData = FXCollections.observableArrayList();

    private Flight flight;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        screen1.setVisible(true);
        screen2.setVisible(false);


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



        try {
            this.modificationsBaseController = new ModificationsBaseController(root,ArrivalToColumn,DODeparture,DateDepartureColumn,DateOfArrival,DepartureColumn,DepartureFrom,FlightsTable,Plane,PlaneColumn,PmOrAm,TimeDepartureColumn,TimeOfDeparture,arrivalTo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onNext1()
    {
        screen1.setVisible(false);
        screen2.setVisible(true);

    }

    public void onAdd1() throws SQLException, IOException, ParseException
    {
        this.flight = this.modificationsBaseController.add();

        if (flight != null)
        {
            flightData.add(flight);

            Add.setDisable(true);
            Add.setDisable(false);

            modifyController.setCheck1(true,flight);
        }

    }

    @Override
    public void setInfo(String departureLocation,String arrivalToInfo, OwnedPlane plane, Flight nextFlight, String owner, ModifyController modifyController)
    {
        this.modificationsBaseController.setInfo(departureLocation,arrivalToInfo,plane,nextFlight,owner,this);
        this.modifyController = modifyController;

    }








}

