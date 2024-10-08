package datastructure.airplaneapplication;

import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;

public class FlightValidator
{
    private int indexOfAddedElement;
    private GeneralAdapter FlightPathAdapter;
    private String tableName = "FlIGHTPATH";

    private final String[] infoForFlightPath = {
            "FlightPathRefID VARCHAR(1000)","DepartingFlight Varchar(1000) NOT NULL REFERENCES Flights(FLIGHTREFERENCEID) DEFAULT 'default_value'","ReturningFlight Varchar(1000) NOT NULL REFERENCES Flights(FLIGHTREFERENCEID) DEFAULT 'default_value'", "Plane VARCHAR(1000) NOT NULL REFERENCES OWNEDPLANES(PlaneReferenceID) DEFAULT 'default_value'","FLIGHTPATHOWNER VARCHAR(30) NOT NULL REFERENCES ACCOUNT(REFERENCEID) DEFAULT 'default_value'"

    };

    public FlightValidator() throws SQLException
    {
        FlightPathAdapter = new GeneralAdapter(false,tableName,infoForFlightPath);
    }

    public boolean nextFlightSafe(Flight flight, Flight nextFlight)
    {
        boolean nextFlighTSafe;
        ArrayList<FlightPath> tempList = new ArrayList<>();

        FlightPath tempPrevFlightPath = new FlightPath("temp",null,flight,"temp","temp");
        FlightPath tempRequestedFlightPath = new FlightPath("temp",nextFlight,null,"temp","temp");
        tempList.add(tempPrevFlightPath);
        tempList.add(tempRequestedFlightPath);

        indexOfAddedElement=0;


        if (scenario2Check(tempList,tempRequestedFlightPath))
        {
            nextFlighTSafe = scenario3Check(tempList,tempRequestedFlightPath);
        }
        else
        {
            nextFlighTSafe = scenario4Check(tempList,tempRequestedFlightPath);
        }


        return nextFlighTSafe;


    }

    public boolean checkIfSafeToAdd(ObservableList<FlightPath> data, FlightPath requestedFlight) throws SQLException //This method checks if it is safe to add the requested flight
    {
        //**************** Declaring All possible scenarios when checking whether it is safe to add a flight ***************
        //******************************************************************************************************************

        boolean scenario1 = true; // If true, this is the first addition of this plane model
        boolean scenario1$2; //Scenario1 is false as in there is an addition of the plane model in the system;If this scenario is true there are no flights before our REQUESTED FLIGHT, if false its vice versa as in there are flights before our REQUESTED FLIGHT.
        boolean scenario2; //If true, Scenario1$2 is false; as in there is an addition of this plane model in the system before te requested Flight, but the closest Arrival Date before our ATTEMPTED FLIGHT is not on the same day
        boolean scenario3; //If true, Scenario2 is true, but the that addition of the plane Model arrived less than two hours before our intended departure Time_______If false, Scenario2 is true, And that addition of the plane Model arrived more than two hours before our intended departure Time
        boolean scenario4; //If true, Scenario1$2 is false and Scenario2 is false; as in there is an addition of the plane model in the system BUT IT IS ON THE SAME DAY, but that addition of the plane Model arrived less than two hours before our intended departure Time_______If false, Scenario2 is false; as in there is an addition of the plane model in the system BUT IT IS ON THE SAME DAY, And that addition of the plane Model arrived more than two hours before our intended departure Time
        boolean scenario5; //If this is true Either scenario4 or scenario3 are false OR scenario1$2 is true; meaning the flight preceding the REQUESTED FLIGHT fits the criteria of arriving 2 hours or more before the REQUESTED FLIGHTS departure, if false either scenario4 or scenario3 don't fit the criteria of arriving 2 hours or more before the REQUESTED FLIGHTS departure
        boolean scenario5$2; //we must now check the departure of the Flight following the REQUESTED FLIGHT. If scenario5$2 is true there are no flights following this flight. If scenario5$2 is false there is a flight following this flight.
        boolean scenario6; //In this scenario Scenario5$2 is false. If this scenario6 is true then the flight following the REQUESTED FLIGHT is departing on a different day. If this scenario is false the flight following our requested flight is on the same day.
        boolean scenario7; //If false, Scenario6 is true, but the that addition of the REQUESTED FLIGHT arrived less than two hours before our the next flights departure Time_______If true, Scenario6 is true, And that addition of the plane Model arrived more than two hours before our intended departure Time
        boolean scenario8; //If false, Scenario5$2 is false and Scenario6 is false; as in there is an addition of the plane model in the system BUT IT IS ON THE SAME DAY, but that addition of the plane Model is departing less than two hours before our intended arrival Time_______If true, that addition of the plane Model departs more than two hours before our intended arrival Time

        //******************************************************************************************************************
        //******************************************************************************************************************


        //************** Finding All Current Flight Paths **************
        //**************************************************************

        LinkedHashMap<String,Object> map = new LinkedHashMap<>(); //declaring a LinkedHashMap variable which is a required argument for the findRecords method
        map.put("Plane = ",requestedFlight.retrieverPlaneRefId()); //Plane is the Column we are searching through, requestedFlight.retrievePlaneRedId() is the referenceID of the plane being used for the requested flight
        ArrayList<FlightPath> listOfFlightPaths; //declaration of an arrayList which will hold all the flight paths of the plane being used for the requested flight
        ArrayList<FlightPath> list2;
        try {
            listOfFlightPaths = FlightPathAdapter.findRecords(map,new FlightPath()); // listOfFlightPaths will be populated with all flight paths of the plane chosen to be used for the requested flight
        }
        catch (Exception ex) //if findRecords cant find any flightpath an exception is thrown
        {
            listOfFlightPaths = new ArrayList<>(); //in the scenario above listOfFlightPaths is initialized as an empty list instead
        }

        FlightPath[] flightPathsInTable = data.toArray(new FlightPath[0]); //declaring and initializing an array with all flight path in the table

        for (FlightPath x: flightPathsInTable)  //Iterating through all the flight paths in the table, if any have the same reference id as the plane being used for the requested Flight they are added to listOfFlightPaths
        {
            if( x.retrieverPlaneRefId().equals(requestedFlight.retrieverPlaneRefId()))
            {
                listOfFlightPaths.add(x);
            }

        }

        //**************************************************************
        //**************************************************************

        list2 = listOfFlightPaths; // as of rn I don't know what this is for

        //************** Checking Scenario 1 **************
        //*************************************************
        System.out.println("Checking Scenario 1...");
        if(listOfFlightPaths.isEmpty()) //if after looking for all current flight paths the list is still empty that means Scenario1 is true and this is the first flightPath for this plane
        {
            System.out.println("Scenario 1 is true");
            return scenario1; //Scenario1 is already initialized as true and is returned, signifying it is safe to add this flightpath
        }
        //*************************************************
        //*************************************************



        //If my code continues to be traversed that means scenario 1 is false, Scenario 1$2 must now be checked

        //************** Checking Scenario 1$2 *************************************************************************1$2
        //**************************************************************************************************************1$2
        System.out.println("Checking Scenario 1$2");
        listOfFlightPaths.add(requestedFlight); //adding the requested flight to the list of flight paths in order to sort the list and determine the flight before and after the requested flight
        scenario1$2 = scenario1$2Check(listOfFlightPaths); // the result of scenario1$2 will be retrieved from the scenario1$2Check() method and saved in the earlier declared scenario1$2 boolean

        if(!scenario1$2) //if scenario1$2 is false then scenario2 is checked
        {
            System.out.println("Scenario 1$2 is False ");
            //************** Checking Scenario 2 **********************************************2
            //*********************************************************************************2
            scenario2 = scenario2Check(listOfFlightPaths,requestedFlight); //method that checks scenario2
            System.out.println("checking Scenario 2...");
            if(scenario2) //if scenario2 is true scenario3 is checked
            {
                System.out.println("Scenario 2 is True");
                //************** Checking Scenario 3 ***************************3
                //**************************************************************3
                System.out.println("Checking Scenario 3...");
                scenario3 = scenario3Check(listOfFlightPaths,requestedFlight); //teh result of scenario3 is retrieved from scenario3Check() method
                if(!scenario3) //if scenario3 is false, scenario 5$2 is checked
                {
                    System.out.println("Scenario 3 is false");
                    //************** Checking Scenario 5$2 **************5$2
                    //***************************************************5$2
                    System.out.println("Checking Scenario5$2");
                    scenario5$2 = scenario5$2Check(listOfFlightPaths); //checking scenario5$2 as scenario 5 is true
                    //***************************************************5$2
                    //***************************************************5$2
                }
                else //if scenario3 is true then the flight is not safe to add
                {
                    System.out.println("Scenario 3 is true ");
                    System.out.println("Checking Scenario 5...");
                    System.out.println("Scenario 5 is false");
                    scenario5 = false;//criteria of flight prior to REQUESTED FLIGHT arriving 2 hours before REQUESTED FLIGHTS departure not met,
                    return scenario5;
                }
                //**************************************************************3
                //**************************************************************3


            }
            else //if scenario2 is false scenario4 is checked
            {
                System.out.println("Scenario 2 is false");
                //************** Checking Scenario 4 ***************************4
                //**************************************************************4
                System.out.println("Checking Scenario 4...");
                scenario4 = scenario4Check(listOfFlightPaths,requestedFlight); //checking scenario4 using scenario4Check()
                if(!scenario4) // if scenario4 is false scenario5$2 is checked
                {
                    System.out.println("Scenario 4 is false");
                    System.out.println("Checking Scenario 5$2");
                    scenario5$2 = scenario5$2Check(listOfFlightPaths); //checking scenario5$2 as scenario 5 is true
                }
                else //if scenario4 is true then the flight is not safe to add
                {
                    System.out.println("Scenario 4 is true");
                    System.out.println("Scenario 5 is false");
                    scenario5 = false;//criteria of flight prior to REQUESTED FLIGHT arriving 2 hours before REQUESTED FLIGHTS departure not met,
                    return scenario5;
                }
                //**************************************************************4
                //**************************************************************4
            }

            //*********************************************************************************2
            //*********************************************************************************2

        }
        else //if scenario1$2 is true, then all previous scenarios can be skipped and scenario5$2 is checked
        {
            System.out.println("Scenario 1$2 is true");
            System.out.println("Checking Scenario 5$2");
            scenario5$2 = scenario5$2Check(list2); //checking scenario5$2
        }

        //**************************************************************************************************************1$2
        //**************************************************************************************************************1$2

        if(scenario5$2) // if sceanrio5$2 is true then the criteria of all flights prior to requested flight are met, and there are no flights after requested flight
        {
            System.out.println("Scenario 5$2 is true");
            return scenario5$2; //Safe to add REQUESTED FLIGHT as it fits criteria of flight prior arriving 2 hours before REQUESTED FLIGHTS departure & there are no flights post requested flight;
        }
        else // if sceanrio5$2 is false then the criteria of all flights prior to requested flight are met, but there are flights after requested flight
        {
            //************** Checking Scenario 6 ********************************************************6
            //*******************************************************************************************6
            System.out.println("Checking Scenario 6...");
            scenario6 = scenario6Check(list2,requestedFlight); //checking scenario 6

            if(scenario6) //if the plane post-requested flight is departing on a different day check scenario 7
            {
                System.out.println("Scenario 6 is true");
                //************** Checking Scenario 7 ***************************7
                //**************************************************************7
                System.out.println("Checking Scenario 7...");
                scenario7 = scenario7Check(list2,requestedFlight);
                return scenario7; //defines whether the requested flight meets ALL the defined conditions or not
                //**************************************************************7
                //**************************************************************7

            }
            else // plane post-requested flight is departing on the same day
            {
                //************** Checking Scenario 8 ***************************8
                //**************************************************************8
                System.out.println("Checking Scenario 8...");
                scenario8 = scenario8Check(list2,requestedFlight);
                return scenario8; //defines whether the requested flight meets ALL the defined conditions or not
                //**************************************************************8
                //**************************************************************8
            }
            //*******************************************************************************************6
            //*******************************************************************************************6
        }
    }




    public boolean scenario1$2Check(ArrayList<FlightPath> list ) // A method that checks scenario1$2
    {
        indexOfAddedElement = insertionSortAdjusted(list); //insertionSortAdjusted sorts the list of FlightPaths so the flight before after the flightpath can be assessed, it also returns the index at which the requested flight path is so that the previous and next fight path can also eb accessed,
        if(indexOfAddedElement==0) //if the added element is in the first index then scenario1$2 is true as in: there are no flights before our REQUESTED FLIGHT
        {
            return true;
        }//if the added element is not in the first index then scenario1$2 is false as in: there areflights before our REQUESTED FLIGHT
        else
        {
            return false;
        }
    }

    public boolean scenario2Check(ArrayList<FlightPath> list , FlightPath requestedFlight) //method that checks scenario2
    {
        boolean scenario2;

        if(list.get(indexOfAddedElement-1).retrieveDateOfArrival().isBefore(requestedFlight.retrieveDateOfDeparture()))
        {
            scenario2 = true;//there is an addition of this plane model before our flight path in the system, but the closest Arrival Date before our ATTEMPTED FLIGHT is not on the same day
        }
        else
        {
            scenario2 = false; //there is an addition of this plane model before our flight path in the system, but the closest Arrival Date before our ATTEMPTED FLIGHT is on the same day
        }
        return scenario2;
    }

    public boolean scenario3Check(ArrayList<FlightPath> list , FlightPath requestedFlight) // checking if scenario3 is true or false
    {
        //************ Declaring and Initializing necessary variables ************
        //************************************************************************
        boolean scenario3;

        LocalDateTime flightBeforeReqFlight = list.get(indexOfAddedElement-1).retrieveDateOfArrival(); // the date of arrival of the flight before the requested flight path
        LocalDateTime requestedFlightDate = requestedFlight.retrieveDateOfDeparture(); // the date of departure of the requested flight

        LocalTime flightBeforeReqFlightTime = flightBeforeReqFlight.toLocalTime(); // the time of arrival of the flight before the requested flight
        LocalTime requestedFlightTime = requestedFlight.retrieveTimeOfDeparture(); // the time of departure for the flight before teh requested flight
        //************************************************************************
        //************************************************************************

        //************ Checking Scenario 3 ************
        //*********************************************
        if(flightBeforeReqFlight.getYear()==requestedFlightDate.getYear() && flightBeforeReqFlight.getMonth() == requestedFlightDate.getMonth() && (requestedFlightDate.getDayOfMonth() - flightBeforeReqFlight.getDayOfMonth()) == 1 && ((requestedFlightTime.getHour()+24 - flightBeforeReqFlightTime.getHour()) < 2 || ( (requestedFlightTime.getHour()+24 - flightBeforeReqFlightTime.getHour())== 2 && (requestedFlightTime.getMinute() < flightBeforeReqFlightTime.getMinute()))) )
        {   // The flight BEFORE the requested flight is the day before, however, two situations may arise which makes the requested flight NOT SAFE TO ADD. Scenario 1: The difference in the hour is less than 2; for example 11:00 and 12:00.
            // Scenario 2: The difference in the hour is 2 but the minutes of the REQUESTED FLIGHTS departure is NOT equal to or greater than the preceding flights arrival resulting in a net timeOfDepartureObservableList difference less than two hours. For example, 11:30 and 1:20
            scenario3 = true; // the plane Model arrived less than two hours before our intended departure Time
        }
        else // in the else scenario , either the preceding flight is in an earlier year,month,or day...
        // OR the hour difference is greater than 2; for example 11:00 and 3:00 OR the hour difference is equal to 2 and the REQUESTED FLIGHTS minutes is greater or equal to that of the preceding flights arrival; For example, 11:30 and 1:30
        {
            scenario3 = false; //// the plane Model arrived more than two hours before our intended departure Time
        }
        //*********************************************
        //*********************************************

        return scenario3;
    }



    public boolean scenario4Check(ArrayList<FlightPath> list, FlightPath requestedFlight) //checking scenario4 which is whether this plane models previous flight path which arrived at the same day arrived 2 hours before our intended departure or not
    {
        //************ Declaring and Initializing necessary variables ************
        //************************************************************************
        boolean scenario4;

        LocalDateTime dateOfPreRequestedFlight = list.get(indexOfAddedElement-1).retrieveDateOfArrival(); //date of arrival of flight before requested flight
        LocalTime timeOfPreRequestedFlight = dateOfPreRequestedFlight.toLocalTime(); // time of arrival of flight before requested flight
        LocalTime requestedFlightTime = requestedFlight.retrieveTimeOfDeparture(); //Time of departure of requested flight
        //************ Declaring and Initializing necessary variables ************
        //************************************************************************

        if((requestedFlightTime.getHour() - timeOfPreRequestedFlight.getHour()) < 2 || ( (requestedFlightTime.getHour() - timeOfPreRequestedFlight.getHour())== 2 && (requestedFlightTime.getMinute() < timeOfPreRequestedFlight.getMinute())) ) //
        {
            scenario4 = true; //Scenario1 is false and Scenario2 is false; as in there is an addition of the plane model in the system BUT IT IS ON THE SAME DAY, but that addition of the plane Model arrived less than two hours before our intended departure Time
        }
        else
        {
            scenario4 = false; //If false, Scenario2 is false, And that addition of the plane Model arrived more than two hours before our intended departure Time
        }
        return scenario4;
    }

    public boolean scenario5$2Check(ArrayList<FlightPath> list) // checking scenario5$2 which is whether there is a flight after our requested flight
    {
        boolean scenario5$2;
        if(list.size()-1 == indexOfAddedElement) // our requested flight is the last index so there are no flights after our requested flight
        {
            scenario5$2 = true;
        }
        else // there is a flight after our requested flight
        {
            scenario5$2 = false;
        }
        return scenario5$2;

    }

    public boolean scenario6Check(ArrayList<FlightPath> list, FlightPath requestedFlight) //checking scenario 6: Whether the flight departing after the requested flight is departing on the same day or not
    {
        boolean scenario6;


        if(list.get(indexOfAddedElement+1).retrieveDateOfDeparture().isAfter(requestedFlight.retrieveDateOfArrival())) //Checking whether the date of departure of the flight post-requested flight is after the requested flights date of arrival
        {
            scenario6 = true;// the closest Departure Date after our ATTEMPTED FLIGHT Arrival is not on the same day
        }
        else
        {
            scenario6 = false; //the closest Departure Date after our ATTEMPTED FLIGHT Arrival is on the same day
        }
        return scenario6;

    }

    public boolean scenario7Check(ArrayList<FlightPath> list, FlightPath requestedFlight) //checking scenario 7 which is: whether the plane departing after our requested flights arrival is departing 2 hours or more after our requested flights arrival
    {
        //************ Declaring and Initializing necessary variables ************
        //************************************************************************
        boolean scenario7;

        LocalDateTime flightPostReqDate = list.get(indexOfAddedElement+1).retrieveDateOfDeparture(); // date of departure for flight post requested flight
        LocalDateTime requestedFlightDate = requestedFlight.retrieveDateOfArrival(); // date fo arrival of requested flight

        LocalTime flightPostReqTime = flightPostReqDate.toLocalTime(); // time of departure for flight post requested flight
        LocalTime requestedFlightTime = requestedFlight.retrieveTimeOfArrival(); // time of arrival for requested flight
        //************ Declaring and Initializing necessary variables ************
        //************************************************************************


        //************ Checking Scenario 7 ************
        //*********************************************
        if(flightPostReqDate.getYear()==requestedFlightDate.getYear() && flightPostReqDate.getMonth() == requestedFlightDate.getMonth() && (flightPostReqDate.getDayOfMonth() - requestedFlightDate.getDayOfMonth()) == 1 && (( flightPostReqTime.getHour()+24 - requestedFlightTime.getHour()) < 2 || ( (flightPostReqTime.getHour()+24 - requestedFlightTime.getHour() )== 2 && ( flightPostReqTime.getMinute() < requestedFlightTime.getMinute()  )) ))
        {
            // The flight BEFORE the requested flight is the day before, however, two situations may arise which makes the requested flight NOT SAFE TO ADD. Scenario 1: The difference in the hour is less than 2; for example 11:00 and 12:00.
            // Scenario 2: The difference in the hour is 2 but the minutes of the REQUESTED FLIGHTS departure is NOT equal to or greater than the preceding flights arrival resulting in a net timeOfDepartureObservableList difference less than two hours. For example, 11:30 and 1:20

            scenario7 = false;
        }
        else // in the else scenario , either the preceding flight is in an earlier year,month,or day...
        // OR the hour difference is greater than 2; for example 11:00 and 3:00 OR the hour difference is equal to 2 and the REQUESTED FLIGHTS minutes is greater or equal to that of the preceding flights arrival; For example, 11:30 and 1:30
        {
            scenario7 = true;
        }

        return scenario7;
        //*********************************************
        //*********************************************
    }

    public boolean scenario8Check(ArrayList<FlightPath> list, FlightPath requestedFlight) //checking scenario8 which is: The plane departing post-req flight is departing on the same day of our req flights arrival, this checks whether it departs at least 2-hours post arrival of our requested flight
    {
        boolean scenario8;

        LocalDateTime date = list.get(indexOfAddedElement+1).retrieveDateOfDeparture();
        LocalTime dateTime = date.toLocalTime();
        LocalTime requestedFlightTime = requestedFlight.retrieveTimeOfArrival();

        if((dateTime.getHour() - requestedFlightTime.getHour()) < 2 || ( (dateTime.getHour() - requestedFlightTime.getHour())== 2 && (dateTime.getMinute() < requestedFlightTime.getMinute())) ) //
        {
            scenario8 = false; //Scenario1 is false and Scenario2 is false; as in there is an addition of the plane model in the system BUT IT IS ON THE SAME DAY, but that addition of the plane Model arrived less than two hours before our intended departure Time
        }
        else
        {
            scenario8 = true; //If false, Scenario2 is true, And that addition of the plane Model arrived more than two hours before our intended departure Time
        }
        return scenario8;
    }

    public static int insertionSortAdjusted(ArrayList<FlightPath> flights) // A sorting algorithm to sort the list of flight paths in order of departure from earliest to latest
    {
        String requestedFlightPathRefId = flights.getLast().retrieverRefId(); //keeping track of the requestedFlightsRefId so I can retrieve that requested flight later

        //************** Insertion Sort Method **************
        //***************************************************

        int i, j;

        for(i = 1; i < flights.size();i++) //number of iterations through the flight list
        {

            FlightPath key = flights.get(i); //The key flight that is compared against others
            LocalDateTime keyDate = flights.get(i).retrieveDateOfDeparture(); //the key flights comparison detail
            LocalTime keyTime = flights.get(i).retrieveTimeOfDeparture();


            for (j = i - 1; j >= 0 && (flights.get(j).retrieveDateOfDeparture().isAfter(keyDate) || flights.get(j).retrieveDateOfDeparture().isEqual(keyDate) && flights.get(j).retrieveTimeOfDeparture().isAfter(keyTime)); j--)
            // The loop starts at index 'i - 1' and continues as long as 'j' is greater than or equal to 0 and the departure date of the flight at index 'j' is after 'keyDate'.
            {
                // If the flight at index 'j' has a departure date later than 'keyDate', it means this flight should be moved one position to the right to make room for the key flight.
                flights.set(j + 1, flights.get(j));
                // This sets the flight at index 'j + 1' to be the same as the flight at index 'j'.
                // Essentially, it shifts the flight at index 'j' to the right.
                //However, note that the flight at index j isn't changed, this meaning the index j and j+1 hold the same flight, and the overall list is now missing the key flight...we have to find the correct place to insert key !!
            }
            // When the loop ends, either 'j' is less than 0 (meaning the key flight has the earliest departure date), or we have found a flight with a departure date earlier than or equal to 'keyDate' ( the key flight should be inserted in the space after this flight)
            // At the point when j is an index  less than 0 or  j is an index where a flight is before key date
            //also the two indexes after j hold the same flight, we essentially now have a space to insert keyDate
            // so we need to increment j back up by 1 to insert the key flight into its correct position
            flights.set(j + 1, key);
            // This ensures that the key flight is inserted in the correct position in the sorted order, while all flights with later departure dates are shifted one position to the right.

            //Visual Example below
            //24...3
            //24...4
            //23...4

        }

        //***************************************************
        //***************************************************

        //************** Searching for Requested Flight Path **************
        //*****************************************************************

        for(int k = 0; k < flights.size();k++)
        {
            if(flights.get(k).retrieverRefId().equals(requestedFlightPathRefId))
                return k;
        }

        //*****************************************************************
        //*****************************************************************

        return 0; //the 0 is a placeholder the ref id is in the list so k will always be returned

    }

    public static int insertionSortAdjusted2(ArrayList<Flight> flights, Flight flight) // A sorting algorithm to sort the list of flight paths in order of departure from earliest to latest
    {

        String requestedFlightRefId = flight.retrieveRefId(); //keeping track of the requestedFlightsRefId so I can retrieve that requested flight later


        //************** Insertion Sort Method **************
        //***************************************************

        int i, j;

        for(i = 1; i < flights.size();i++) //number of iterations through the flight list
        {

            Flight key = flights.get(i); //The key flight that is compared against others
            LocalDateTime keyDate = flights.get(i).retrieveDateOfDeparture(); //the key flights comparison detail
            LocalTime keyTime = flights.get(i).retrieveTimeOfDeparture();


            for (j = i - 1; j >= 0 && (flights.get(j).retrieveDateOfDeparture().isAfter(keyDate) || flights.get(j).retrieveDateOfDeparture().isEqual(keyDate) && flights.get(j).retrieveTimeOfDeparture().isAfter(keyTime)); j--)
            // The loop starts at index 'i - 1' and continues as long as 'j' is greater than or equal to 0 and the departure date of the flight at index 'j' is after 'keyDate'.
            {
                // If the flight at index 'j' has a departure date later than 'keyDate', it means this flight should be moved one position to the right to make room for the key flight.
                flights.set(j + 1, flights.get(j));
                // This sets the flight at index 'j + 1' to be the same as the flight at index 'j'.
                // Essentially, it shifts the flight at index 'j' to the right.
                //However, note that the flight at index j isn't changed, this meaning the index j and j+1 hold the same flight, and the overall list is now missing the key flight...we have to find the correct place to insert key !!
            }
            // When the loop ends, either 'j' is less than 0 (meaning the key flight has the earliest departure date), or we have found a flight with a departure date earlier than or equal to 'keyDate' ( the key flight should be inserted in the space after this flight)
            // At the point when j is an index  less than 0 or  j is an index where a flight is before key date
            //also the two indexes after j hold the same flight, we essentially now have a space to insert keyDate
            // so we need to increment j back up by 1 to insert the key flight into its correct position
            flights.set(j + 1, key);
            // This ensures that the key flight is inserted in the correct position in the sorted order, while all flights with later departure dates are shifted one position to the right.

            //Visual Example below
            //24...3
            //24...4
            //23...4

        }

        //***************************************************
        //***************************************************

        //************** Searching for Requested Flight Path **************
        //*****************************************************************

        for(int k = 0; k < flights.size();k++)
        {
            if(flights.get(k).retrieveRefId().equals(requestedFlightRefId))
                return k;
        }

        //*****************************************************************
        //*****************************************************************

        return 0; //the 0 is a placeholder the ref id is in the list so k will always be returned


    }

}
