package plantapi.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class ConnectionHandler {

    private String apiKey, visualModel;
    private Cloudinary cloudinary;
    private Dotenv dotenv;

    public ConnectionHandler(String apiKey, String visualModel) {
        dotenv = Dotenv.configure().load();
        this.apiKey = apiKey;
        this.visualModel = visualModel;
                this.cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", dotenv.get("CLOUDINARY_NAME"),
            "api_key", dotenv.get("CLOUDINARY_KEY"),
            "api_secret", dotenv.get("CLOUDINARY_SECRET")));

    }

    public String uploadImage(MultipartFile image) {
        try {
            if (image.isEmpty()) {
                return "Error: File is empty";
            }
            else {
                BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                if (width > Integer.parseInt(dotenv.get("IMAGE_MAX_WIDTH")) || height > Integer.parseInt(dotenv.get("IMAGE_MAX_HEIGHT"))) {
                    return "Error: Image must be 800x600 pixels or smaller";
                }
                else {
                    String fileName = image.getOriginalFilename();
                    Path filePath = Paths.get(dotenv.get("LOCAL_IMAGE_DIR"), fileName);
                    System.out.println("Saving image to " + filePath.toString());
                    image.transferTo(filePath.toFile());
                    System.out.println("Uploading image to filehoster");
                    String fileHosterUrl = uploadToImageHoster(filePath.toFile());
                    String answer = "Image uploaded successfully. Hosted at: " + fileHosterUrl;
                    System.out.println(answer);
                    return answer;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Failed to upload image";
        }
    }

    @SuppressWarnings("unchecked")
    public String uploadToImageHoster(File file) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload image";
        }
    }

    public String readPromptFromFile(String filePath) {
        try {
            List<String> allLines = Files.readAllLines(Paths.get(filePath));
            String analysisPrompt = String.join("\n", allLines);
            return analysisPrompt;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private String createUrlRequestBody(String systemText, String userText, String imageUrl, String tokenLimit) {

        JSONArray contentArray;
        JSONObject contentItem, urlContentItem, textContentItem;

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

        // add user text message
        textContentItem = new JSONObject();
        textContentItem.put("type", "text");
        textContentItem.put("text", userText);
        contentArray.put(textContentItem);

        // add user image message
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

