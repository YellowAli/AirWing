package datastructure.airplaneapplication;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;

public class Flight implements Factory
{
    private StringProperty timeTillReturn = new SimpleStringProperty();
    private ObjectProperty<LocalDate> dateOnly = new SimpleObjectProperty<>();
    private StringProperty timeOnly = new SimpleStringProperty();

    private StringProperty departureLocation  = new SimpleStringProperty();;
    private ObjectProperty<LocalDateTime> dateOfDeparture = new SimpleObjectProperty<>();

    private StringProperty arrivalLocation = new SimpleStringProperty();;

    private StringProperty frequency = new SimpleStringProperty();;

    private ObjectProperty<OwnedPlane> plane = new SimpleObjectProperty<>();;

    private ObjectProperty<LocalDateTime> dateOfArrival = new SimpleObjectProperty<>();

    private Flight nextFlight;
    private String statusOfFlight;
    private String timeOfArrival;
    private StringProperty refId = new SimpleStringProperty();
    private String owner;

    private String returningFlightListRef;

    public Flight()
    {

    }

    public void setTimeOfArrival(String timeOfArrival)
    {
        this.timeOfArrival = timeOfArrival;
    }



    public Flight(String refId, String departureLocation, LocalDateTime dateOfDeparture, String arrivalLocation, String frequency, OwnedPlane plane, LocalDateTime dateOfArrival, String statusOfFlight, Flight nextFlight, String timeOfArrival, String owner, String returningFlightListRef)
    {
        this.refId.set(refId);
        this.departureLocation.set(departureLocation);

        this.dateOfDeparture.set(dateOfDeparture);
        this.arrivalLocation.set(arrivalLocation);
        this.frequency.set(frequency);
        this.plane.set(plane);
        this.dateOfArrival.set(dateOfArrival);
        this.statusOfFlight = statusOfFlight;
        this.nextFlight = nextFlight;
        this.timeOfArrival = timeOfArrival;
        this.owner = owner;
        this.returningFlightListRef = returningFlightListRef;

    }

    public Flight(String refId, String departureLocation, LocalDateTime dateOfDeparture, String arrivalLocation, String frequency, OwnedPlane plane, LocalDateTime dateOfArrival, String statusOfFlight, Flight nextFlight, String timeOfArrival, String timeTillReturn, String owner,int toDifferentiateConstructors)
    {
        this.refId.set(refId);
        this.departureLocation.set(departureLocation);

        this.dateOfDeparture.set(dateOfDeparture);
        this.arrivalLocation.set(arrivalLocation);
        this.frequency.set(frequency);
        this.plane.set(plane);
        this.dateOfArrival.set(dateOfArrival);
        this.statusOfFlight = statusOfFlight;
        this.nextFlight = nextFlight;
        this.timeOfArrival = timeOfArrival;
        this.timeTillReturn.set(timeTillReturn);
        this.owner = owner;

    }

    public void setDepartureLocation(String departureLocation)
    {
        this.departureLocation.set(departureLocation);
    }

    public StringProperty getDepartureLocation()
    {
        return departureLocation;
    }

    public void setDateOfDeparture(LocalDateTime dateOfDeparture)
    {
        this.dateOfDeparture.set(dateOfDeparture);
    }

    public ObjectProperty<LocalDateTime> getDateOfDeparture()
    {
        return dateOfDeparture;
    }

    public ObjectProperty<LocalDate> getOnlyDate()
    {
        LocalDate l = dateOfDeparture.get().toLocalDate();
        dateOnly.set(l);
        return dateOnly;
    }

    public StringProperty getOnlyTime() throws ParseException {
        LocalTime localTime = dateOfDeparture.get().toLocalTime();
        timeOnly.set(ArrivalEstimator.convertTo12HrTime(localTime.toString()));
        return  timeOnly;

    }



    public void setArrivalLocation(String arrivalLocation)
    {
        this.arrivalLocation.set(arrivalLocation);
    }

    public StringProperty getArrivalLocation()
    {
        return arrivalLocation;
    }

    public void setFrequency(String frequency)
    {
        this.frequency.set(frequency);
    }

    public StringProperty getFrequency()
    {
        return frequency;
    }

    public StringProperty getTimeTillReturn()
    {
        return timeTillReturn;
    }

    public void setPlane(OwnedPlane plane)
    {
        this.plane.set(plane);
    }

    public ObjectProperty<OwnedPlane> getPlane()
    {
        return plane;
    }

    public void setDateOfArrival(LocalDateTime dateOfArrival)
    {
        this.dateOfArrival.set(dateOfArrival);
    }

    public ObjectProperty<LocalDateTime> getDateOfArrival()
    {
        return dateOfArrival;
    }


    public String retrieveDepartureLocation() {
        return departureLocation.get();
    }

    public void updateDepartureLocation(String departureLocation) {
        this.departureLocation.set(departureLocation);
    }

    public LocalDateTime retrieveDateOfDeparture() {
        return dateOfDeparture.get();
    }

    public void updateDateOfDeparture(LocalDateTime dateOfDeparture) {
        this.dateOfDeparture.set(dateOfDeparture);
    }


    public String retrieveArrivalLocation() {
        return arrivalLocation.get();
    }

    public void updateArrivalLocation(String arrivalLocation) {
        this.arrivalLocation.set(arrivalLocation);
    }

    public String retrieveFrequency() {
        return frequency.get();
    }

    public void updateFrequency(String frequency) {
        this.frequency.set(frequency);
    }

    public OwnedPlane retrievePlane() {
        return plane.get();
    }


    public void updatePlane(OwnedPlane plane) {
        this.plane.set(plane);
    }

    public LocalDateTime retrieveDateOfArrival() {
        return dateOfArrival.get();
    }

    public LocalTime retrieveTimeOfArrival()
    {
        return dateOfArrival.get().toLocalTime();
    }

    public LocalTime retrieveTimeOfDeparture()
    {
        return dateOfDeparture.get().toLocalTime();
    }

    public void updateDateOfArrival(LocalDateTime dateOfArrival) {
        this.dateOfArrival.set(dateOfArrival);
    }

    public Flight retrieveNextFlight() {
        return nextFlight;
    }

    public void updateNextFlight(Flight nextFlight) {
        this.nextFlight = nextFlight;
    }

    public String retrieveStatusOfFlight() {
        return statusOfFlight;
    }

    public void updateStatusOfFlight(String statusOfFlight) {
        this.statusOfFlight = statusOfFlight;
    }

    public String retrieveTimeOfArrivalString() {
        return timeOfArrival;
    }

    public void updateTimeToArrival(String timeToArrival) {
        this.timeOfArrival = timeToArrival;
    }

    public String retrieveRefId() {
        return refId.get();
    }

    public String retrieveFlightOwner()
    {
        return owner;
    }

    public String retrieveReturningFlightListRef(){return returningFlightListRef;}

    public void updateRefId(String refId)
    {
        this.refId.set(refId);
    }

    public StringProperty getRefid(){return this.refId;}



    @Override
    public Factory createNewRecord(ResultSet set) throws SQLException
    {
        String[] info = {
                "PlaneReferenceID VARCHAR(1000)", "Type VARCHAR(1000)", "Performance VARCHAR(1000)",
                "Weight VARCHAR(1000)", "Dimension VARCHAR(1000)", "FlightCrewCapacity DOUBLE",
                "PassengerCapacity DOUBLE", "CruisingSpeed DOUBLE","ReferenceID VARCHAR(30) NOT NULL REFERENCES Account(REFERENCEID) DEFAULT 'default_value'"
        };
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        GeneralAdapter adapter =new GeneralAdapter(false,"OwnedPlanes",info);
        map.put("PlaneReferenceId = ",set.getString(6));
        OwnedPlane x = adapter.findRecords(map,new OwnedPlane()).getFirst();

        String[] info2 = {
                "FLIGHTREFERENCEID VARCHAR(1000)", "DepartureLocation VARCHAR(1000)", "DateOfDeparture TIMESTAMP",
                "ArrivalLocation VARCHAR(1000)", "Frequency VARCHAR(1000)",
                "Plane VARCHAR(1000) NOT NULL REFERENCES OWNEDPLANES(PlaneReferenceID) DEFAULT 'default_value'", "DateOfArrival TIMESTAMP", "StatusOfFlight VARCHAR(1000)","NextFlight VARCHAR(1000) NOT NULL REFERENCES Flights(ReferenceID) DEFAULT 'default_value ","TimeToArrival VARCHAR(1000)","FLIGHTOWNER VARCHAR(30) NOT NULL REFERENCES ACCOUNT(REFERENCEID) DEFAULT 'default_value'","RETURNINGFLIGHTLISTREF VARCHAR(1000)"
        };
        LinkedHashMap<String,Object> map2 = new LinkedHashMap<>();
        GeneralAdapter adapter2 =new GeneralAdapter(false,"Flights",info2);
        map2.put("ReferenceID = ",set.getString(9));
        Flight y;
        try
        {
            y = adapter2.findRecords(map2,new Flight()).getFirst();
        }
        catch (Exception ex)
        {
            y = null;
        }

        System.out.println(((Timestamp)set.getObject(3)).toLocalDateTime());
        return new Flight(set.getString(1),set.getString(2), ((Timestamp)set.getObject(3)).toLocalDateTime(),set.getString(4),set.getString(5),x                , ((Timestamp)set.getObject(7)).toLocalDateTime(),set.getString(8),y,                 set.getString(10),set.getString(11), set.getString(12));
        //               (String refId,               String departureLocation,     LocalDateTime dateOfDeparture,             String arrivalLocation,      String frequency,           OwnedPlane plane, LocalDateTime dateOfArrival,                                                                       String statusOfFlight,      Flight nextFlight,                                                        String timeToArrival)
    }


}
