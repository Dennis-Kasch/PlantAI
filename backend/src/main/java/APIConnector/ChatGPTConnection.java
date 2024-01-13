package APIConnector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.github.cdimascio.dotenv.Dotenv;

public class ChatGPTConnection {

    public static String getChatGPTAnswer(String userMessage, String apiKey, String model, String imageUrl) {
        String endpointUrl = "https://api.openai.com/v1/chat/completions";

        try {
            URL url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);

            //String requestBody = "{'model': '" + model + "', 'messages': [{'role': 'user', 'content': '" + userMessage + "'}]}";
            String requestBody = String.format("""
                {
                    "model": "%s",
                    "messages": [
                        {
                            "role": "user",
                            "content": [
                                {"type": "text", "text": "%s"},
                                {
                                    "type": "image_url",
                                    "image_url": {"url": "%s"}
                                }
                            ]
                        }
                    ]
                }""", model, userMessage, imageUrl);

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
            e.printStackTrace();
            return "Error in API request";
        }
    }

    private static String extractAnswerFromJSON(String jsonResponse) {
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


    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        String apiKey = dotenv.get("API_KEY");
        String model = dotenv.get("MODEL");
        String imageUrl = dotenv.get("IMAGE_URL");
        String userMessage = "What is shown in this picture?";

        String answer = getChatGPTAnswer(userMessage, apiKey, model, imageUrl);
        System.out.println("User: " + userMessage);
        System.out.println("ChatGPT Answer: " + answer);
    }
}

