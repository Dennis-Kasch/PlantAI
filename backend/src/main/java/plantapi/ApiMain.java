package plantapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiMain {
    public static void main(String[] args) {
        // start server
        SpringApplication.run(ApiMain.class, args);
    }
}
