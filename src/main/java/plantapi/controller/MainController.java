package plantapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String base() {
        String answer = "Server is running";
        System.out.println(answer);
        return answer;
    }
    
}
