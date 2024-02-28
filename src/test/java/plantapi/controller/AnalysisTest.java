package plantapi.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.function.Executable;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;



import io.github.cdimascio.dotenv.Dotenv;


public class AnalysisTest {

    private static Dotenv dotenv;
    private static ChatGPTConnection connection;
    private static String systemMessage;
    private List<JSONObject> getJsonArray(String jsonPath, String key) {
        try {
            // Read the content of the file into a String
            String content = new String(Files.readAllBytes(Paths.get(jsonPath)));
            // Create a JSONObject from the content
            JSONObject jsonObject = new JSONObject(content);
            // Navigate through the JSONObject to get the array with the given key
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            // Convert JSONArray to List<JSONObject>
            List<JSONObject> jsonObjects = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjects.add(jsonArray.getJSONObject(i));
            }
            return jsonObjects;
    
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String readPromptFromFile(String filePath) {
        try {
            List<String> allLines = Files.readAllLines(Paths.get(filePath));
            String analysisPrompt = String.join("\n", allLines);
            return analysisPrompt;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getAttributeFromAnswer(String answer, String attribute) {
        // Regular expression to match the plant name line and capture the name
    Pattern pattern = Pattern.compile(attribute+": (.+?)(?:\n|$)");
    Matcher matcher = pattern.matcher(answer);
        if (matcher.find()) {
            String plantName = matcher.group(1); // Group 1 contains the captured plant name
            return plantName;
        } else {
            return "ERROR: attribute value for "+attribute+" not found";
        }
    }

    public void runPlantTest(String jsonPath, String testArrayKey, String plantName) {
        String imageUrl, gptAnswer;
        List<JSONObject> testArray = getJsonArray(jsonPath, testArrayKey);
        int totalCount = testArray.size();
        int plantAssertCounter=0, statusAssertCounter=0, diseaseAssertCounter=0;
        for (JSONObject appleObject: testArray) {
            String id = appleObject.getString("id");
            System.out.println("plant id:"+id);
            // get analysis from chatgpt
            imageUrl = appleObject.getString("image_url");
            gptAnswer = connection.getAnswerByUrl(systemMessage, "The image shows a "+plantName+" plant or leaves of it.", imageUrl, "300");
            // check if analysis matches expected values
            String expectedPlant = appleObject.getString("plant");
            String expectedStatus = appleObject.getString("status");
            String expectedDisease = appleObject.getString("diseases");
            String gptPlant = getAttributeFromAnswer(gptAnswer, "plant_name").toLowerCase();
            String gptStatus = getAttributeFromAnswer(gptAnswer, "health_status").toLowerCase();
            String gptDiseases = getAttributeFromAnswer(gptAnswer, "disease_list").toLowerCase();
            System.out.println(gptAnswer);
            System.out.println("-------------------------");
            if (gptPlant.contains(expectedPlant)) plantAssertCounter++;
            if (gptStatus.equals(expectedStatus)) statusAssertCounter++;
            if (gptDiseases.contains(expectedDisease)) diseaseAssertCounter++;
        }
        List<Executable> assertions = new ArrayList<>();
        final float plantPercentage = (float) plantAssertCounter/totalCount;
        final float statusPercentage = (float) statusAssertCounter/totalCount;
        final float diseasePercentage = (float) diseaseAssertCounter/totalCount;
        System.out.println("plants:"+plantPercentage+", statuses:"+statusPercentage+", diseases:"+diseasePercentage);
        // make sure at least 80% of plants are diagnosed correctly
        assertions.add(() -> assertTrue(statusPercentage>=0.8, "At least 80% of health statuses must be correct."));
        assertAll("Analysis validations", assertions);
    }

    @BeforeAll
    public static void initializeConnector() {
        dotenv = Dotenv.configure().load();
        connection = new ChatGPTConnection(dotenv.get("GPT_API_KEY"), dotenv.get("VISUAL_MODEL"));
        systemMessage = readPromptFromFile("./src/test/resources/analysisPrompt.txt");
    }

    @Test
    public void healthyApplesTest() {
        String jsonPath = "./src/test/resources/analysis-testing/apple_tests.json";
        String testArrayKey = "apples_healthy";
        runPlantTest(jsonPath, testArrayKey, "apple");
    }

    @Test
    public void scabApplesTest() {
        String jsonPath = "./src/test/resources/analysis-testing/apple_tests.json";
        String testArrayKey = "apples_scab";
        runPlantTest(jsonPath, testArrayKey, "apple");
    }
    
    @Test
    public void rustApplesTest() {
        String jsonPath = "./src/test/resources/analysis-testing/apple_tests.json";
        String testArrayKey = "apples_rust";
        runPlantTest(jsonPath, testArrayKey, "apple");
    }

    @Test
    public void blightCornTest() {
        String jsonPath = "./src/test/resources/analysis-testing/corn_tests.json";
        String testArrayKey = "corn_blight";
        runPlantTest(jsonPath, testArrayKey, "corn");
    }

    @Test
    public void rustCornTest() {
        String jsonPath = "./src/test/resources/analysis-testing/corn_tests.json";
        String testArrayKey = "corn_rust";
        runPlantTest(jsonPath, testArrayKey, "corn");
    }

    @Test
    public void grayCornTest() {
        String jsonPath = "./src/test/resources/analysis-testing/corn_tests.json";
        String testArrayKey = "corn_gray";
        runPlantTest(jsonPath, testArrayKey, "corn");
    }

    @Test
    public void healthyTomatoTest() {
        String jsonPath = "./src/test/resources/analysis-testing/tomato_tests.json";
        String testArrayKey = "tomato_healthy";
        runPlantTest(jsonPath, testArrayKey, "tomato");
    }

    @Test
    public void yellowVirusTomatoTest() {
        String jsonPath = "./src/test/resources/analysis-testing/tomato_tests.json";
        String testArrayKey = "tomato_yellow_virus";
        runPlantTest(jsonPath, testArrayKey, "tomato");
    }

    @Test
    public void mosaicVirusTomatoTest() {
        String jsonPath = "./src/test/resources/analysis-testing/tomato_tests.json";
        String testArrayKey = "tomato_mosaic_virus";
        runPlantTest(jsonPath, testArrayKey, "tomato");
    }

}
