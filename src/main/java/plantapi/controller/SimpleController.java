package plantapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.cdimascio.dotenv.Dotenv;

@RestController
public class SimpleController {

    private ChatGPTConnection connection;

    public SimpleController() {
        // create connection to chatGPT
        Dotenv dotenv = Dotenv.configure().load();
        this.connection = new ChatGPTConnection(dotenv.get("API_KEY"), dotenv.get("MODEL"));
    }

    @GetMapping("/describeImage")
    public String describeImage(
        // define necessary parameters for the endpoint
        @RequestParam(value = "userMessage") String userMessage,
        @RequestParam(value = "imageUrl") String imageUrl
    ) 
    {
        // get an answer from chat gpt
        String answer = connection.getChatGPTAnswer("empty", userMessage, imageUrl, "300");
        System.out.println(answer);
        return answer;
    }

    @GetMapping("/analysePlantImage")
    public String analysePlantImage(
        // define necessary parameters for the endpoint
        @RequestParam(value = "imageUrl") String imageUrl
    ) 
    {
        String systemMessage= """
            When I send you an image please check if it is an image of a plant or a part of a plant. If not, reply exactly with 'ERROR: This image is no plant or part of a plant.' and nothing else.
            If the image is a plant or part of a plant, please give the name of the plant in the format 'plant name: [NAME OF THE PLANT]'. Answer 'plant name: unklnown' if you cannot identify it.
            In addition, please identify if this plant is healthy or if it has any diseases. Answer with 'health status: healthy' or 'health status: unhealthy' according to the healh status of the plant.
            If it has any diseases, please give an answer of the format 'diseases: [DISEASE_1, DISEASE_2, DISEASE_3, ..., DISEASE_N]'.
            Put each answer in its own line and seperate them with a comma.
        """;
        // get an answer from chat gpt
        String answer = connection.getChatGPTAnswer(systemMessage, "empty", imageUrl, "300");
        System.out.println(answer);
        return answer;
    }

    @GetMapping("/")
    public String base() 
    {
        String answer = "Server is running";
        System.out.println(answer);
        return answer;
    }
}