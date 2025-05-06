package com.it_sakerhet_java23_reidar_thorsen.consoleview;

import com.it_sakerhet_java23_reidar_thorsen.model.AuthRequest;
import com.it_sakerhet_java23_reidar_thorsen.model.UserEntity;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;
import java.util.regex.Pattern;

@Component
public class ConsoleView {

    private RestTemplate restTemplate;
    private String jwtToken; // This will store the token after login

    public ConsoleView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    Scanner scanner = new Scanner(System.in);

    public void Menu() {
        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Create Time Capsule");
            System.out.println("4. View Time Capsules");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        register();
                        break;
                    case 2:
                        login();
                        break;
                    case 3:
                        createTimeCapsule();
                        break;
                    case 4:
                        viewTimeCapsules();
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        exitApplication(); // Call a method to handle a proper exit
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }



    public void login() {
        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(username);
        authRequest.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AuthRequest> request = new HttpEntity<>(authRequest, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/authenticate", request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                jwtToken = response.getBody();  // Store the JWT token
                System.out.println("Login successful. Token: " + jwtToken);
            } else {
                System.out.println("Login failed. Status: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            System.out.println("Login failed: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public void register() {
        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        String email = "";
        boolean isValid = false;

        while (!isValid) {
            System.out.println("Enter email: ");
            email = scanner.nextLine();

            if (isValidEmail(email)) {
                isValid = true;
            } else {
                System.out.println("Invalid email format. Please try again.");
            }
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserEntity> request = new HttpEntity<>(user, headers);

        String response = restTemplate.postForObject("http://localhost:8080/register", request, String.class);
        System.out.println(response);
    }

    public void createTimeCapsule() {
        if (jwtToken == null) {
            System.out.println("You need to log in first.");
            return;
        }

        System.out.println("Enter the message for your time capsule: ");
        String message = scanner.nextLine();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);  // Use the stored JWT token

        HttpEntity<String> request = new HttpEntity<>(message, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://localhost:8080/timecapsules", request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Time capsule created successfully.");
            } else {
                System.out.println("Failed to create time capsule. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public void viewTimeCapsules() {
        if (jwtToken == null) {
            System.out.println("You need to log in first.");
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);  // Use the stored JWT token

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String[]> response = restTemplate.exchange(
                    "http://localhost:8080/timecapsules", HttpMethod.GET, request, String[].class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String[] timeCapsules = response.getBody();
                if (timeCapsules != null && timeCapsules.length > 0) {
                    System.out.println("Your Time Capsules:");
                    for (String capsule : timeCapsules) {
                        System.out.println(capsule);
                    }
                } else {
                    System.out.println("No time capsules found.");
                }
            } else {
                System.out.println("Failed to retrieve time capsules. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void exitApplication() {
        System.out.println("Thank you for using the application. Goodbye!");
        System.exit(0); // Gracefully exit the program
    }
}
