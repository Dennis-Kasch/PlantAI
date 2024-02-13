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

    private String apiKey, model;

    public ChatGPTConnection(String apiKey, String model) {
        this.apiKey = apiKey;
        this.model = model;
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
        body.put("model", model);
        contentArray = new JSONArray();
        contentArray.put(system);
        contentArray.put(user);
        body.put("messages", contentArray);
        body.put("max_tokens", tokenLimit);

        return body.toString();
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

