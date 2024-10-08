package datastructure.airplaneapplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.awt.Shape;
import org.apache.sis.referencing.CommonCRS;
import org.apache.sis.referencing.GeodeticCalculator;

public class DistanceBetweenLocations
{
    public static double calculateDistance(String origin, String finalDestination) throws IOException
    {
        String [] originSplit = origin.split(",");
        System.out.println(originSplit[0]);
        double lat = Double.parseDouble(originSplit[0]);
        double longitude = Double.parseDouble(originSplit[1]);

        var calculator = GeodeticCalculator.create(CommonCRS.WGS84.geographic());

        calculator.setStartGeographicPoint(lat, longitude);

        String [] finalDestinationSplit = finalDestination.split(",");
        lat = Double.parseDouble(finalDestinationSplit[0]);
         longitude = Double.parseDouble(finalDestinationSplit[1]);

        calculator.setEndGeographicPoint(lat, longitude);

        double d;
        d = calculator.getGeodesicDistance();
        System.out.printf("The distance is %1.2f %s %n", d, calculator.getDistanceUnit());
        return d;




    }
}
