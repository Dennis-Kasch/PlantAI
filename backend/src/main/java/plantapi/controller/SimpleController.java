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
        @RequestParam(value = "userMessage") String userMessage,
        @RequestParam(value = "imageUrl") String imageUrl,
        @RequestParam(value = "tokenLimit") String tokenLimit
    ) 
    {
        // get an answer from chat gpt
        String answer = connection.getChatGPTAnswer(userMessage, imageUrl, tokenLimit);
        System.out.println(answer);
        return answer;
    }
}