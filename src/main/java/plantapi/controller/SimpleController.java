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
public class SimpleController {

    private ChatGPTConnection connection;
    private Cloudinary cloudinary;
    private Dotenv dotenv;

    public SimpleController() {
        // Create connection to chatGPT
        dotenv = Dotenv.configure().load();
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", dotenv.get("CLOUDINARY_NAME"),
            "api_key", dotenv.get("CLOUDINARY_KEY"),
            "api_secret", dotenv.get("CLOUDINARY_SECRET")));
        this.connection = new ChatGPTConnection(dotenv.get("GPT_API_KEY"), dotenv.get("VISUAL_MODEL"));
        String keyResponse = this.connection.checkApiKey();
        if (keyResponse.contains("ERROR")) throw new RuntimeException("Shutting down backend due to runtime exception.");
        else System.out.println(keyResponse);
    }

    @SuppressWarnings("unchecked")
    private String uploadToImageHoster(File file) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload image";
        }
    }

    @GetMapping("/")
    public String base() {
        String answer = "Server is running";
        System.out.println(answer);
        return answer;
    }

    private String uploadImage(MultipartFile image) {
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

    @GetMapping("/checkApiKey")
    public String checkApiKey() {
        return null;
    }

    private String readPromptFromFile(String filePath) {
        try {
            List<String> allLines = Files.readAllLines(Paths.get(filePath));
            String analysisPrompt = String.join("\n", allLines);
            return analysisPrompt;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/analysePlantImage")
    public String analysePlantImage(
        @RequestParam(value = "image") MultipartFile image
    ) {
        String uploadAnswer = uploadImage(image);
        String imageUrl = null;
        if (uploadAnswer.toLowerCase().contains("error")) return uploadAnswer;
        else imageUrl = uploadAnswer.replace("Image uploaded successfully. Hosted at: ","");
        System.out.println("Analyzing "+imageUrl+" for its health status");

        String systemMessage = readPromptFromFile("./src/main/resources/analysisPrompt.txt");
        String gptAnswer = connection.getAnswerByUrl(systemMessage, "empty", imageUrl, "300");
        System.out.println(gptAnswer);
        return gptAnswer;
    }

}