package plantapi.controller;

import sqlconnection.SQLConnector;

import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private SQLConnector dbConnector;

    public UserController() {
        try {
            this.dbConnector = new SQLConnector();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/user/")
    public ResponseEntity<String> createUser(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "firstname") String firstname,
            @RequestParam(value = "lastname") String lastname,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password) {
        try {
            boolean success = dbConnector.createUser(username, firstname, lastname, email, password);
            if (success) {
                return ResponseEntity.ok("User " + username + " has been added successfully.");
            } else {
                // Assuming failure is due to user already existing
                return ResponseEntity.badRequest().body("User " + username + " already exists.");
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            // Respond with an Internal Server Error (500) status code
            return ResponseEntity.internalServerError().body("An error occurred while creating the user.");
        }
    }
    
    @PostMapping("/user/image")
    public ResponseEntity<String> addImage(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "imageUrl") String imageUrl,
            @RequestParam(value = "imageName") String imageName,
            @RequestParam(value = "imageHash") String imageHash,
            @RequestParam(value = "result") String result) {
        try {
            boolean success = dbConnector.addImage(username, imageUrl, imageName, imageHash, result);
            if (success) {
                return ResponseEntity.ok("Image " + imageUrl + " has been added successfully.");
            } else {
                // Assuming failure is due to image already existing
                return ResponseEntity.badRequest().body("Image  with hash " + imageHash + " already exists.");
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            // Respond with an Internal Server Error (500) status code
            return ResponseEntity.internalServerError().body("An error occurred while adding an image.");
        }
    }

    @GetMapping("/user/history")
    public ResponseEntity<String> getUserHistory(@RequestParam(value = "username") String username) {
        try {
            // First, check if the user exists
            boolean userExists = dbConnector.doesUserExist(username);
            if (!userExists) {
                // User does not exist, return a 404 Not Found
                return ResponseEntity.notFound().build();
            }
            // If user exists, retrieve their history
            String historyJson = dbConnector.getUserHistory(username);
            if (historyJson == null || historyJson.isEmpty()) {
                // No history entries found for the user
                return ResponseEntity.ok().body("No history entries found for user.");
            }
            // Return the user's history as JSON with a 200 OK status
            return ResponseEntity.ok().body(historyJson);
        } catch (SQLException e) {
            e.printStackTrace();
            // Return an Internal Server Error (500) for any SQLException
            return ResponseEntity.internalServerError().body("An error occurred while retrieving user history.");
        }
    }    

}
