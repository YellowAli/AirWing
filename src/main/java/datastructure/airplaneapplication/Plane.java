package datastructure.airplaneapplication;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Plane implements Factory {
    private String type;
    private String performance;
    private String weight;
    private String dimensions;
    private double flightCrewCapacity;
    private double passengerCapacity;
    private StringProperty refId;

    private double cruisingSpeed;

    // Constructor with parameters
    public Plane(String refId, String type, String performance, String weight, String dimensions, double flightCrewCapacity, double passengerCapacity,double cruisingSpeed) {
        this.refId = new SimpleStringProperty();
        this.cruisingSpeed = cruisingSpeed;
        this.refId.set(refId);
        this.type = type;
        this.performance = performance;
        this.weight = weight;
        this.dimensions = dimensions;
        this.flightCrewCapacity = flightCrewCapacity;
        this.passengerCapacity = passengerCapacity;
    }

    // Default constructor
    public Plane() {
        this.cruisingSpeed = 0;
        this.refId = null;
        this.type = null;
        this.performance = null;
        this.weight = null;
        this.dimensions = null;
        this.flightCrewCapacity = 0;
        this.passengerCapacity = 0;
    }

    // Getters
    public StringProperty getRefId() {
        return refId;
    }


    public String getType() {
        return type;
    }

    public String getPerformance() {
        return performance;
    }

    public String getWeight() {
        return weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public double getFlightCrewCapacity() {
        return flightCrewCapacity;
    }

    public double getPassengerCapacity() {
        return passengerCapacity;
    }

    // Setters
    public void setRefId(String refId) {
        this.refId.set(refId);
    }


    public void setType(String type) {
        this.type = type;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setCruisingSpeed(double cruisingSpeed)
    {
        this.cruisingSpeed = cruisingSpeed;
    }

    public double getCruisingSpeed()
    {
        return cruisingSpeed;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public void setFlightCrewCapacity(double flightCrewCapacity) {
        this.flightCrewCapacity = flightCrewCapacity;
    }

    public void setPassengerCapacity(double passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }


@Override
    public  Factory createNewRecord(ResultSet set) throws SQLException {
        return new Plane(set.getString(1),set.getString(2),set.getString(3),set.getString(4),set.getString(5),set.getDouble(6),set.getDouble(7),set.getDouble(8));

    }


}
