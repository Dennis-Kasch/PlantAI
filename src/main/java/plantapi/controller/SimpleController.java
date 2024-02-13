package plantapi.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        this.connection = new ChatGPTConnection(dotenv.get("GPT_API_KEY"), dotenv.get("MODEL"));
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", dotenv.get("CLOUDINARY_NAME"),
            "api_key", dotenv.get("CLOUDINARY_KEY"),
            "api_secret", dotenv.get("CLOUDINARY_SECRET")));
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

    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            if (image.isEmpty()) {
                return "Error: File is empty";
            }
            else {
                BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                if (width > 800 || height > 600) {
                    return "Error: Image must be 800x600 pixels or smaller";
                }
                else {
                    String fileName = image.getOriginalFilename();
                    Path filePath = Paths.get(dotenv.get("UPLOAD_DIR"), fileName);
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
            return "Failed to upload image";
        }
    }    

    @GetMapping("/analysePlantImage")
    public String analysePlantImage(
        @RequestParam(value = "imageUrl") String imageUrl
    ) {
        String systemMessage = """
            When I send you an image please check if it is an image of a plant or a part of a plant. If not, reply exactly with 'ERROR: This image is no plant or part of a plant.' and nothing else.
            If the image is a plant or part of a plant, please give the name of the plant in the format 'plant name: [NAME OF THE PLANT]'. Answer 'plant name: unknown' if you cannot identify it.
            In addition, please identify if this plant is healthy or if it has any diseases. Answer with 'health status: healthy' or 'health status: unhealthy' according to the health status of the plant.
            If it has any diseases, please give an answer of the format 'diseases: [DISEASE_1, DISEASE_2, DISEASE_3, ..., DISEASE_N]'.
            Put each answer in its own line and separate them with a comma.
        """;
        String answer = connection.getAnswerByUrl(systemMessage, "empty", imageUrl, "300");
        System.out.println(answer);
        return answer;
    }

}