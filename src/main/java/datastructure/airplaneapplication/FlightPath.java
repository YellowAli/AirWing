package datastructure.airplaneapplication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class FlightPath implements Factory
{
    private int position;
    private ArrayList<Flight> completePath;
    private String refId;
    private Flight departingFlight;
    private Flight returningFlight;
    private String planeRefId;
    private String owner;
    private String frequencyFlightPath;

    public FlightPath()
    {

    }

    public FlightPath(String refId, Flight departingFlight, Flight returningFlight,String owner, String frequencyFlightPath)
    {
        this.refId = refId;
        this.departingFlight = departingFlight;
        this.returningFlight = returningFlight;
        this.planeRefId = retrieverPlaneRefId();
        this.owner = owner;
        this.frequencyFlightPath = frequencyFlightPath;
    }

    public void setRefId(String refId)
    {
        this.refId = refId;
    }

    public void setDepartingFlight(Flight returningFlight)
    {
        this.returningFlight = returningFlight;
    }

    public LocalDateTime retrieveDateOfDeparture() {
        return departingFlight.retrieveDateOfDeparture();
    }

    public LocalDateTime retrieveDateOfArrival() {
        return returningFlight.retrieveDateOfArrival();
    }

    public LocalTime retrieveTimeOfArrival()
    {
        return returningFlight.retrieveTimeOfArrival();
    }

    public LocalTime retrieveTimeOfDeparture()
    {
        return departingFlight.retrieveTimeOfDeparture();
    }

    public String retrieveDepartingFlightRef()
    {
        return departingFlight.retrieveRefId();
    }

    public String retrieveArrivingFlightRefId()
    {
        return returningFlight.retrieveRefId();
    }
    public String retrieveFrequencyFlight()
    {
        return frequencyFlightPath;
    }


    public String retrieverPlaneRefId()
    {
        return departingFlight.retrievePlane().getRefId().get();
    }

    public String retrieverRefId()
    {
        return refId;
    }
    public String retrieveOwner(){return  owner;}
    public Flight retrieveDepartingFlight(){return departingFlight;};
    public Flight retrieveReturningFlight()
    {
        return returningFlight;
    }

    public void setCompleteFlightPath(ArrayList<Flight>completePath,int position)
    {
        this.completePath = completePath;
        this.position = position;
    }

    public ArrayList<Flight> retrieveCompleteFlightPath()
    {
        return completePath;
    }

    public int retrievePosition()
    {
        return position;
    }

    @Override
    public Factory createNewRecord(ResultSet set) throws SQLException
    {
        String[] info = {
                "FLIGHTREFERENCEID VARCHAR(1000)", "DepartureLocation VARCHAR(1000)", "DateOfDeparture TIMESTAMP",
                "ArrivalLocation VARCHAR(1000)", "Frequency VARCHAR(1000)",
                "Plane VARCHAR(1000) NOT NULL REFERENCES OWNEDPLANES(PlaneReferenceID) DEFAULT 'default_value'", "DateOfArrival TIMESTAMP", "StatusOfFlight VARCHAR(1000)","NextFlight VARCHAR(1000) REFERENCES Flights(FLIGHTREFERENCEID) DEFAULT 'default_value'","TimeToArrival VARCHAR(1000)",
        };
        GeneralAdapter adapter = new GeneralAdapter(false,"Flights",info);
        LinkedHashMap<String,Object> map = new LinkedHashMap();
        map.put("FLIGHTREFERENCEID = ", set.getString(2));
        Flight departingFlight ;
        departingFlight  = adapter.findRecords(map,new Flight()).getFirst();

        map.clear();

        map.put("FLIGHTREFERENCEID = ", set.getString(3));
        Flight returningFlight;
        returningFlight = adapter.findRecords(map,new Flight()).getFirst();


        return new FlightPath(set.getString(1),departingFlight,returningFlight,set.getString(4),set.getString(5));
    }
}
