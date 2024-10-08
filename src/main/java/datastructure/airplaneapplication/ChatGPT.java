package datastructure.airplaneapplication;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ChatGPT
{
    private static final String API_KEY = System.getenv("OPENAI_API_KEY");

    public static String chat(String passage, String planeModel,int key) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = API_KEY;
        String model = "gpt-4o";
        String role = "";

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // The request body
            StringBuilder body = new StringBuilder();
            body.append("{");
            body.append("\"model\": \"").append(model).append("\",");
            body.append("\"messages\": [");
            body.append("{\"role\": ").append("\"system\", ").append("\"content\": ").append("\"You are an assistant which extracts the requested information from passages and returns it in the requested format.\"").append("},");
            if(key==1)
            {
                body.append("{\"role\": ").append("\"user\", ").append("\"content\": ").append("\"For this passage based on the key model extract, its  sum of (pilot + flight) crew capacity and the capacity of passengers. Format it in the following way:model/sum of pilot and flight crew capacity /passenger capacity. Only complete this process for the key plane model referenced, if the key plane model is not referenced in the passage assume the passage is in reference to the key model. In the scenario where a capacity is given as an or statement, for example 3/4 people, choose the highest number. Do not include words in capacity. Do not include any symbols like addition only the final sum value. If you cannot find a value simply put 0, if it does not specify who the seats are for allocate one to pilot capacity and the rest to passengers. The key model: ").append(planeModel).append(" The passage: ").append(passage).append("\"}],");
            }
            else
            {
                body.append("{\"role\": ").append("\"user\", ").append("\"content\": ").append("\"For this passage based on the key model extract, its max cruising speed.  Only complete this process for the key plane model referenced, if the key plane model is not referenced in the passage assume the passage is in reference to the key model. Do not include words in output, only include numerical value. Do not include any symbols like addition only the final value. If you cannot find a value simply put any cruising speed value you can find, if that cant be found put any speed value you find which is reasonable. The key model: ").append(planeModel).append(" The passage: ").append(passage).append("\"}],");
            }
            body.append("\"temperature\": ").append("0.2");
            body.append("}");


            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body.toString());
            writer.flush();
            writer.close();

            // Response from ChatGPT
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuffer response = new StringBuffer();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            System.out.println(extractMessageFromJSONResponse(response.toString()));

            // calls the method to extract the message.
            return extractMessageFromJSONResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractMessageFromJSONResponse(String response) {
        JSONObject theResponse = new JSONObject(response);
        return theResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
    }
}
