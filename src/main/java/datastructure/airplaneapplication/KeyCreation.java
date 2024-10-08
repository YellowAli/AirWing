package datastructure.airplaneapplication;

import java.util.ArrayList;

public class KeyCreation
{

    public static String returnKey(ArrayList<String> lis, String flight)
    {
        String returnedTing;

        String j = "1";
        String missingKey = "z";
        int max = 0;
        ArrayList<String> listOfSamePlaneRefNumbersTaken = new ArrayList<>();


        for (int i = 0; i < lis.size(); i++) //lis is a list of all referenceIds with the same reference keyword before the number
        {
            String[] refSplit = ((String) lis.get(i)).split("-"); // splitting the entry from the database list (lis) at the '-' point for example:
            // AASI Jetcruzer-1 will be populated into an array as AASI Jetcruzer, 1

            String ref = refSplit[1]; //ref = the number in my above example that would be 1

            if(refSplit[0].concat("-").equals(flight)) // refSplit[0] or AASI Jetcruzer is equal to the inputted plane model
                //then the ref from earlier will be added to a list which holds all ref numbers in use for this specific plane model
                listOfSamePlaneRefNumbersTaken.add(ref);
        }
        for (int i = 0; i < listOfSamePlaneRefNumbersTaken.size(); i++)
        {

            if (!listOfSamePlaneRefNumbersTaken.contains(j)) //if listOfSamePlaneRefNumbersTaken does not contain the calculated missing key then that's the missing ref, the missing key is incremented in each iteration
            {
                System.out.println("u messing me up foo");
                missingKey = j;
                break;
            }


            if(max < Integer.parseInt(listOfSamePlaneRefNumbersTaken.get(i))) //finding the max ref
                max = Integer.parseInt(listOfSamePlaneRefNumbersTaken.get(i));
            System.out.println(max+"yerrr");
            int convert = Integer.parseInt(j);
            convert++;
            j = Integer.toString(convert);
        }

        if(!missingKey.equals("z"))
        {
            returnedTing = flight+ missingKey;
            System.out.println(returnedTing+"!!!!!!!@@@@");
            return returnedTing;
        }
        else
        {
            returnedTing = flight+ ++max;
            System.out.println(returnedTing+"!!!!!!!");
            return returnedTing;
        }



    }

    public static String returnKeyFrequencyFlight (ArrayList<String> lis)
    {


        String returnedTing;

        String j = "1";
        String missingKey = "z";
        int max = 0;
        ArrayList<String> listOfSamePlaneRefNumbersTaken = new ArrayList<>();

        for (int i = 0; i < lis.size(); i++) //lis is a list of all referenceIds with the same reference keyword before the number
        {
            String[] refSplit = ((String) lis.get(i)).split("-"); // splitting the entry from the database list (lis) at the '-' point for example:
            // 1-9 will be populated into an array as 1,9

            String ref = refSplit[0]; //ref = the number in my above example that would be 1
            listOfSamePlaneRefNumbersTaken.add(ref);

        }




        for (int k = 0; k < listOfSamePlaneRefNumbersTaken.size(); k++)
        {

            if (!listOfSamePlaneRefNumbersTaken.contains(j)) //if listOfSamePlaneRefNumbersTaken does not contain the calculated missing key then that's the missing ref, the missing key is incremented in each iteration
            {
                System.out.println("u messing me up foo");
                missingKey = j;
                break;
            }


            if(max < Integer.parseInt(listOfSamePlaneRefNumbersTaken.get(k))) //finding the max ref
                max = Integer.parseInt(listOfSamePlaneRefNumbersTaken.get(k));

            int convert = Integer.parseInt(j);
            convert++;
            j = Integer.toString(convert);
        }

        if(!missingKey.equals("z"))
        {
            returnedTing = missingKey;
            return returnedTing;
        }
        else
        {
            returnedTing = String.valueOf(++max);
            return returnedTing;
        }



    }


}
