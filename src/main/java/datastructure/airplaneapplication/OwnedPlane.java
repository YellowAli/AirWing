package datastructure.airplaneapplication;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OwnedPlane extends Plane
{
    private String referenceOwner;

    public OwnedPlane()
    {

    }

    public OwnedPlane(String refId, String type, String performance, String weight, String dimensions, double flightCrewCapacity, double passengerCapacity,double cruisingSpeed, String referenceOwner)
    {
        super(refId,type,performance,weight,dimensions,flightCrewCapacity,passengerCapacity,cruisingSpeed);
        this.referenceOwner = referenceOwner;

    }

    public void setReferenceOwner(String referenceOwner)
    {
        this.referenceOwner = referenceOwner;
    }

    public String getReferenceOwner()
    {
        return referenceOwner;
    }

    @Override
    public String toString()
    {
        return getRefId().get();
    }

    @Override
    public  Factory createNewRecord(ResultSet set) throws SQLException {
        return new OwnedPlane(set.getString(1),set.getString(2),set.getString(3),set.getString(4),set.getString(5),set.getDouble(6),set.getDouble(7),set.getDouble(8),set.getString(9));

    }

}
