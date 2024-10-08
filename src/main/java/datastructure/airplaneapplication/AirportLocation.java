package datastructure.airplaneapplication;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AirportLocation
{



    public static String securityKey() throws IOException
    {

        String url ="https://test.api.amadeus.com/v1/security/oauth2/token";
        URL urls = URI.create(url).toURL();
        HttpURLConnection connection = (HttpURLConnection) urls.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        String urlParam = "grant_type=client_credentials&client_id=PiXOJwAzcAIXALGdhbXn5dOTLqJd18vD&client_secret=O5xOBddBmy2k3bbp";
        byte[] postData = urlParam.getBytes(StandardCharsets.UTF_8);
        System.setProperty("javax.net.debug", "all");
        OutputStream stream = connection.getOutputStream();
        stream.write(postData);
        stream.flush();
        stream.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;

        StringBuffer response = new StringBuffer();

        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();
        System.out.println(response);
        JSONObject res = new JSONObject(response.toString());
        return (res.getString("access_token"));

    }

    public static ArrayList<String> getCityInfo (String city,int input) throws IOException
    {
        String inputFor2 = null;
        if(input!=1)
        {
            String[] inputFor2Split = city.split(",");
            inputFor2 = inputFor2Split[1];

            if(inputFor2.contains(" "))
            {
                String[] finalSpace = inputFor2.split(" ");
                inputFor2 = finalSpace[0]+"%20"+finalSpace[1];
            }
            
        }


        if(city.contains(",")&&input==1)
        {
            ArrayList<String> x = new ArrayList<>();
            x.add(city);
            return x;
        }
        else
        {
            URL urls;
            HttpURLConnection connection = null;
            String url;
            if(input == 1)
            url ="https://test.api.amadeus.com/v1/reference-data/locations?subType=CITY&keyword="+city;
            else
                url ="https://test.api.amadeus.com/v1/reference-data/locations?subType=CITY&keyword="+inputFor2+"&view=FULL";
            urls = URI.create(url).toURL();
            connection = (HttpURLConnection) urls.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization","Bearer "+securityKey());
            System.out.println(securityKey());


            StringBuffer response = new StringBuffer();
//            try
//            {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;


                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

//            }
//            catch (Exception ex)
//            {
//                System.out.println("hmm");
//            }

            if(input == 1)
            {
                try
                {
                    System.out.println("why we here");
                    System.out.println(response.toString());
                    return jsonObjectConfigured(response.toString());
                }
                catch (Exception ex)
                {
                    System.out.println("caught you bitch");
                    ArrayList<String> x = new ArrayList<>();
                    return x;
                }

            }
            else
            {
                System.out.println("no way we here");
                return latAndLong(response.toString());

            }


        }


    }

    public static ArrayList<String> jsonObjectConfigured(String response)
    {
        String city;
        String country;
        ArrayList<String > lis = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(response);
        JSONArray values  = jsonObject.getJSONArray("data");
        for(int i = 0; i < values.length(); i++)
        {
            city = values.getJSONObject(i).getJSONObject("address").getString("cityName");
            country = values.getJSONObject(i).getJSONObject("address").getString("countryName");
            lis.add(country+","+city);
            System.out.println("buddy u make it in ?");
        }
        System.out.println("Why u not print lis :(");
        System.out.println(lis);
        return lis;
    }

    public static ArrayList<String> latAndLong(String response)
    {
        System.out.println(response);
        double lat;
        double longitude;
        ArrayList<String > lis = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(response);
        JSONArray values  = jsonObject.getJSONArray("data");

            lat = values.getJSONObject(0).getJSONObject("geoCode").getDouble("latitude");
            longitude = values.getJSONObject(0).getJSONObject("geoCode").getDouble("longitude");
            lis.add(lat+","+longitude);
        System.out.println(lis.getFirst()+"damn ");
        return lis;

    }

}
