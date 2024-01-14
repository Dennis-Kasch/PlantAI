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
        //String tokenLimit = dotenv.get("TOKEN_LIMIT");
        this.connection = new ChatGPTConnection(dotenv.get("API_KEY"), dotenv.get("MODEL"));
        /*
        // send message + image and get answer
        String userMessage = "What is shown in this picture?";
        String imageUrl = "https://www.kaerchershop-schreiber.de/images/product_images/popup_images/Kaercher-Badeente-Quietscheente-Ente-gelb-gross_362275_0.jpg";
         */
    }

    @GetMapping("/describeImage")
    public String describeImage(
        @RequestParam(value = "userMessage") String userMessage,
        @RequestParam(value = "imageUrl") String imageUrl,
        @RequestParam(value = "tokenLimit") String tokenLimit
    ) 
    {
        String answer = connection.getChatGPTAnswer(userMessage, imageUrl, tokenLimit);
        System.out.println(answer);
        return answer;
    }
}