package datastructure.airplaneapplication;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

public class ArrivalEstimator
{
    private OwnedPlane Plane;
    private String ArrivalTo;
    private String DepartureFrom;
    private GeneralAdapter OwnedPlanesAdapter;



    public ArrivalEstimator(OwnedPlane Plane, String ArrivalTo, String DepartureFrom, GeneralAdapter OwnedPlanesAdapter  ) throws SQLException
    {
        this.OwnedPlanesAdapter = OwnedPlanesAdapter;
        this.Plane = Plane;
        this.ArrivalTo = ArrivalTo;
        this.DepartureFrom = DepartureFrom;


    }
    public double calcTimeToArrival() throws SQLException, IOException //method that calculates the requested flights time to arrival
    {
        double cruisingSpeed = cruisingSpeed(); //The declaration and initialization of a double which defines the cruising speed of the Requested Flight, value obtained from crusingSpeed() method
        double distance = calcDistance(); //The declaration and initialization of a double which defines the distance between the requested Flights departure and arrival points, value obtained for calcDistance() method
        return 1/(cruisingSpeed/distance); // The timeToArrival is calculated and returned

    }

    public double cruisingSpeed() throws SQLException
    {
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        map.put("PLANEREFERENCEID=",Plane);
        ArrayList<OwnedPlane> ownedPlanes = OwnedPlanesAdapter.findRecords(map,new OwnedPlane());

        Double cruisingSpeed = ownedPlanes.getFirst().getCruisingSpeed();
        if(cruisingSpeed < 10.0)
        {
            cruisingSpeed = cruisingSpeed*1012.536;
        }
        System.out.println("The cruising speed is: "+cruisingSpeed);
        return cruisingSpeed;
    }

    public double calcDistance() throws IOException //a method which calculates the distance between the requested flights departure and arrival point
    {
        //**** Calculating Distance ****
        String latAndLongOrigin = (AirportLocation.getCityInfo(DepartureFrom,2)).getFirst(); //Declaring and initializing a string which will hold the latitude and longitude of the point of departure,the value is obtained from the static method getCityInfo from the AirportLocation class

        String latAndLongDestination = (AirportLocation.getCityInfo(ArrivalTo,2)).getFirst(); //Declaring and initializing a string which will hold the latitude and longitude of the point of arrival,the value is obtained from the static method getCityInfo from the AirportLocation class

        double distance = DistanceBetweenLocations.calculateDistance(latAndLongOrigin,latAndLongDestination)/1000; //declaring and initializing a double which will hold the distance between the departure point and arrival point,this value is obtained from the static method calculateDistance from the Distance Between locations Class, the value is divided by 1000 as its in meters

        return distance; //the calculated distance is returned
        //******************************
    }

    public LocalDateTime getDateOfArrival(LocalDateTime dateOfDeparture) throws SQLException, IOException // A method which returns the Requested Flights Date of Arrival
    {
        //**** Calculating Time To Arrival ****
        ArrivalEstimator arrivalEstimator = new ArrivalEstimator(Plane,ArrivalTo,DepartureFrom,OwnedPlanesAdapter);
        double timeToArrival = arrivalEstimator.calcTimeToArrival(); //timeToArrival is declared and initialized with the value returned from the calcTimeToArrival() method, it holds the time it will take for the Requested Flight to arrive to its destination
        //*************************************


        //**** Extracting Hours and Minutes ****
        int hours = (int)(timeToArrival); //Extracting only the hours from timeToArrival,
        int minutes =(int) (((timeToArrival%1))*60); //Extracting only the minutes from timeToArrival, by finding the decimal amount of timeToArrival and then converting it to minutes (multiplying by 60) because it's initially a decimal of the hour unit
        //**************************************


        //***** Returning The LocalDateTime Of Arrival *****
        LocalDateTime dateOfArrival = dateOfDeparture.plusHours(hours); // Initializing a dateOfArrival variable with a LocalDateTime copy of dateOfDeparture plus the hours of timeToArrival
        dateOfArrival = dateOfArrival.plusMinutes(minutes); //adding the minutes of timeToArrival to my adjusted dateOfArrival

        return dateOfArrival; //returning the dateOfArrival
        //**************************************************
    }

    public String convertTo24HrTime(String time) throws ParseException
    {
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        Date date = parseFormat.parse(time);

        return displayFormat.format(date);

    }

    public static String convertTo12HrTime(String time) throws ParseException //A method which accepts a String displaying a time value in 24-hour format and converts it to 12-hour format
    {
        SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm"); //Creating a SimpleDateFormat object which is a class that allows for formatting and parsing date -> text or text -> date, As can be seen it has been initialized with a 24 hr time pattern for date-formatting
        SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a",Locale.US); //Creating a second SimpleDataFormat object with a 12 hr time pattern for data-formatting
        Date date = parseFormat.parse(time); //parses the String argument into a Date from the pattern from parseFormat
        return displayFormat.format(date); // returns a String formatted in the displayFormat pattern from the date received above

    }

    public LocalDateTime getDateAndTimeOfDeparture(CheckBox PmOrAm, ComboBox<String> TimeOfDeparture, DatePicker DODeparture) throws ParseException // a method which returns the Requested Flights time of departure in 12-hour clock time
    {


        //**** Time of Departure in 12-hour clock time ****
        //**************************************************
        String timeOfDeparture12Hourformat; // the declaration of a string which will hold the requested flights time of departure

        //The flights-view FXML file has a TimeOfDeparture Combo Box which is populated with values 1:00 - 12:00, the airline will choose one of these values for their Requested Flights departure time and will select a Pm checkbox to define whether the flight will be post meridiem or after midday
        //an if-else block which will Initialize timeOfDeparture12Hourformat with a time in 12-hour clock format + Pm or Am, depending on the checkbox selection
        if(PmOrAm.isSelected()) //The Pm box is selected
        {
            timeOfDeparture12Hourformat = TimeOfDeparture.getValue()+" PM"; //because the Pm box is selected timeOfDeparture12Hourformat is initialized with the value the airline chose from the TimeOfDeparture combobox + PM
        }
        else //The Pm box isn't selected
        {
            timeOfDeparture12Hourformat = TimeOfDeparture.getValue()+" AM"; //because the Pm box isn't selected timeOfDeparture12Hourformat is initialized with the value the airline chose from the TimeOfDeparture combobox + AM
        }
        //**************************************************
        //**************************************************




        //****** Converting Time to 24-hour Clock Time *****
        //**************************************************

        String timeOfDeparture24HourFormat; //The declaration of a string which will hold the time of departure in 24-hour format

        //The convertTo24HrTime() method used below requires the time to be in this format"00:00"" however some of the time values TimeOfDeparture combo box is populated with are not in that format -> continue
        // for example; 1:00 should be 01:00, the if-else block confirms  the number of characters from the time selected from TimeOfDeparture, if it's less than 5 (i.e. not 11:00 etc.) it deals with it accordingly
        if(TimeOfDeparture.getSelectionModel().getSelectedItem().length()!=5) //The number of characters in the time selected from the TimeOfDeparture combo box is less than 5
        {
            //The argument passed into convertTo24HrTime() has a 0 added to the front of it
            timeOfDeparture24HourFormat = convertTo24HrTime("0"+timeOfDeparture12Hourformat); //The timeOfDeparture24HourFormat is initialized with the 24-hour Time returned from the convertTo24HrTime() method
        }
        else //The number of characters in the time selected from the TimeOfDeparture combo box is equal to 5
        {
            timeOfDeparture24HourFormat = convertTo24HrTime(timeOfDeparture12Hourformat); //The timeOfDeparture24HourFormat is initialized with the 24-hour Time returned from the convertTo24HrTime() method
        }
        //**************************************************
        //**************************************************




        //**** Returning The LocalDateTime Of Departure ****
        //**************************************************

        String[] timeSplit = timeOfDeparture24HourFormat.split(":"); //Populating a timeSplit array with the seperated hours and minutes of timeOfDeparture24HourFormat
        return  (DODeparture.getValue()).atTime(Integer.parseInt(timeSplit[0]),Integer.parseInt(timeSplit[1])); //Getting the Date of Departure Value from the Datepicker and using the atTime() method which Combines the date with a time to create a LocalDateTime object.
        //For the arguments of the atTime() method the hours stored in the timeSplit method were inputted then the minutes (After being parsed into integers)
        //**************************************************
        //**************************************************
    }


}
