package plantapi.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import io.github.cdimascio.dotenv.Dotenv;

@RestController
public class AnalysisController {

    private ChatGPTConnection connection;
    private Dotenv dotenv;

    public AnalysisController() {
        // Create connection to chatGPT
        dotenv = Dotenv.configure().load();
        this.connection = new ChatGPTConnection(dotenv.get("GPT_API_KEY"), dotenv.get("VISUAL_MODEL"));
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