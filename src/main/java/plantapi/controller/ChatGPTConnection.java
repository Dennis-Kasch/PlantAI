package plantapi.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatGPTConnection {

    private String apiKey, visualModel;

    public ChatGPTConnection(String apiKey, String visualModel) {
        this.apiKey = apiKey;
        this.visualModel = visualModel;
    }

    private String createUrlRequestBody(String systemText, String userText, String imageUrl, String tokenLimit) {

        JSONArray contentArray;
        JSONObject contentItem, urlContentItem;

        // create system message
        JSONObject system = new JSONObject();
        system.put("role", "system");
        contentArray = new JSONArray();
        contentItem = new JSONObject();
        contentItem.put("type", "text");
        contentItem.put("text", systemText);
        contentArray.put(contentItem);
        system.put("content", contentArray);

        // create user message
        JSONObject user = new JSONObject();
        user.put("role", "user");
        contentArray = new JSONArray();
        contentItem = new JSONObject();
        urlContentItem = new JSONObject();
        urlContentItem.put("url", imageUrl);
        contentItem.put("type", "image_url");
        contentItem.put("image_url", urlContentItem);
        contentArray.put(contentItem);
        user.put("content", contentArray);

        // create complete body
        JSONObject body = new JSONObject();
        body.put("model", visualModel);
        contentArray = new JSONArray();
        contentArray.put(system);
        contentArray.put(user);
        body.put("messages", contentArray);
        body.put("max_tokens", tokenLimit);

        return body.toString();
    }

    public String checkApiKey() {
        System.out.println("Checking if API key is valid and has access to visual model "+visualModel);
        String requestUrl = "https://api.openai.com/v1/models";
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);

            int responseCode = connection.getResponseCode();
            //System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray models = jsonResponse.getJSONArray("data"); // Assuming the key "data" contains the models array

                for (int i = 0; i < models.length(); i++) {
                    JSONObject model = models.getJSONObject(i);
                    String modelId = model.getString("id");
                    // Check if the model ID matches the visual model
                    if (visualModel.equals(modelId)) {
                        return "API key has access to visual model.";
                    }
                }
                return "ERROR: It seems the given API key has no access to the visual model. The server will not be started.";
            }
            else {
                return "ERROR: It seems like the given API key is invalid (response = "+responseCode+"). The server will not be started.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: It seems like there is a problem with the connection to GPT api. The server will not be started."; // Error occurred, assume the model not found
        }
    }

    public String getAnswerByUrl(String systemMessage, String userMessage, String imageUrl, String tokenLimit) {
        String endpointUrl = "https://api.openai.com/v1/chat/completions";

        HttpURLConnection connection = null;
        try {
            URL url = new URL(endpointUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer "+this.apiKey);
            connection.setDoOutput(true);

            String requestBody = createUrlRequestBody(systemMessage, userMessage, imageUrl, tokenLimit);
            //System.err.println(requestBody);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestBody.getBytes());
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                return extractAnswerFromJSON(response.toString());
            }

        } catch (IOException e) {
            if (connection != null) {
                try {
                    int responseCode = connection.getResponseCode();
                    String responseMessage = connection.getResponseMessage();
                    return "HTTP Error Code: " + responseCode + " - " + responseMessage;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return "Error in retrieving error information";
                }
            }
            e.printStackTrace();
            return "Error in API request";
        }
    }

    private String extractAnswerFromJSON(String jsonResponse) {
        try {
            // Assuming the answer is in the "choices" array
            JsonNode jsonNode = new ObjectMapper().readTree(jsonResponse);
            JsonNode choicesNode = jsonNode.get("choices");

            if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode firstChoice = choicesNode.get(0);
                JsonNode contentNode = firstChoice.get("message").get("content");

                if (contentNode != null && contentNode.isTextual()) {
                    return contentNode.asText();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unable to extract answer from JSON response";
    }

}

