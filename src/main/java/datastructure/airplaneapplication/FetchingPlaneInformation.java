package datastructure.airplaneapplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class FetchingPlaneInformation
{
    private static GeneralAdapter adapter;
    private static String tableName;
    public  ArrayList<Plane> findInfo() throws IOException, SQLException
    {
        Elements byClass = null;
        try
        {
            Document doc = Jsoup.connect("https://www.airliners.net/aircraft-data").get();
             byClass = doc.getElementsByClass("aircraftList flc");

        }
        catch (Exception ex)
        {
            System.out.println("failed to process data"+ex);
        }

        ArrayList<Plane> planes = new ArrayList<>();

        assert byClass != null;
        for(Element deep : byClass)
        {
            LinkedHashMap<String,String> aircraftProperty = null;
            try
            {
                aircraftProperty = processingData(deep);
            }
            catch (Exception ex)
            {
                System.out.println("failed to complete request:"+ex);
            }
            LinkedHashMap<String ,Object> map = new LinkedHashMap<>();


            try{
                String[]lik = aircraftProperty.get("Model").split("/");
                String[]lik2 = aircraftProperty.get("Model").split("&");

                if(lik.length >1)
                {
                    work(aircraftProperty,lik,map,planes,true);
                }
                else
                {
                    work(aircraftProperty,lik2,map,planes,false);

                }
            }
            catch(Exception ex)
            {
                System.out.println("error");
            }

        }
        return planes;


    }

    public void work(LinkedHashMap<String,String> aircraftProperty,String[] lik,LinkedHashMap<String ,Object> map, ArrayList<Plane> planes, boolean Notand) throws SQLException {
        String model;
        for(int j = 0; j < lik.length; j++)
        {
                if (lik[0].contains("-") && j!=0 && Notand)
                {
                    String[] lik3 = lik[0].split("-");
                    model = lik3[0] + "-" + lik[j];
                }
                else
                {
                    model = lik[j];

                }


            String input = ChatGPT.chat(aircraftProperty.get("Capacity"), model, 1);
            String[] inputSliced = input.split("/");

            String input2 = ChatGPT.chat(aircraftProperty.get("Performance"), model, 2);
            planes.add(new Plane(model, aircraftProperty.get("Type"), aircraftProperty.get("Performance"), aircraftProperty.get("Weight"), aircraftProperty.get("Dimensions"), Double.parseDouble(inputSliced[1]), Double.parseDouble(inputSliced[2]), Double.parseDouble(input2)));

            map.put("ReferenceID",model);
            map.put("FlightCrewCapacity",Double.parseDouble(inputSliced[1]));
            map.put("PassengerCapacity",Double.parseDouble(inputSliced[2]));
            map.put("CruisingSpeed",Double.parseDouble(input2));
            map.put("Type",aircraftProperty.get("Type"));
            map.put("Performance",aircraftProperty.get("Performance"));
            map.put("Weight",aircraftProperty.get("Weight"));
            map.put("Dimension",aircraftProperty.get("Dimensions"));

            adapter.addRecord(map);
            map.clear();


        }

    }

    public LinkedHashMap<String, String> processingData(Element deep) throws IOException
    {
        String link = deep.getElementsByTag("a").attr("href");

        Document doc2 = Jsoup.connect("https://www.airliners.net"+link).get();

        Elements aircraftProperties = doc2.getElementsByClass("aircraftProperty");
        LinkedHashMap<String,String> aircraftProperty = new LinkedHashMap<>();

        String[] list = {"Empty0","Type","Empt2y","Empty3","Performance","Weight","Dimensions","Capacity","Empty8","Model"};

        int i = 0;
        for (Element x : aircraftProperties)
        {
            aircraftProperty.put(list[i],x.getElementsByClass("description").text());
            i++;
        }

        return aircraftProperty;

    }

    public  void setAdapter(GeneralAdapter adapters)
    {
        adapter = adapters;
    }

    public  void setTableName(String tableNames)
    {
        tableName = tableNames;
    }

}
