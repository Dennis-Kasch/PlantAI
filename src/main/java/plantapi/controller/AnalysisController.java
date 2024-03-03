package plantapi.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.cdimascio.dotenv.Dotenv;

@RestController
public class AnalysisController {

    private ConnectionHandler connection;
    private Dotenv dotenv;

    public AnalysisController() {
        // Create connection to chatGPT
        dotenv = Dotenv.configure().load();
        this.connection = new ConnectionHandler(dotenv.get("GPT_API_KEY"), dotenv.get("VISUAL_MODEL"));
        String keyResponse = this.connection.checkApiKey();
        if (keyResponse.contains("ERROR")) throw new RuntimeException("Shutting down backend due to runtime exception.");
        else System.out.println(keyResponse);
    }

    @PostMapping("/analysePlantImage")
    public String analysePlantImage(
        @RequestParam(value = "image") MultipartFile image
    ) {
        String uploadAnswer = connection.uploadImage(image);
        String imageUrl = null;
        if (uploadAnswer.toLowerCase().contains("error")) return uploadAnswer;
        else imageUrl = uploadAnswer.replace("Image uploaded successfully. Hosted at: ","");
        System.out.println("Analyzing "+imageUrl+" for its health status");

        String systemMessage = connection.readPromptFromFile("./src/main/resources/analysisPrompt.txt");
        String gptAnswer = connection.getAnswerByUrl(systemMessage, "empty", imageUrl, "300");
        System.out.println(gptAnswer);
        return gptAnswer;
    }

}