package com.phonepuppet.desktop;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.JsonObject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppLauncher extends Application {

    private TextArea logArea;

    static int counter=0;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Phone Puppet - Desktop");

        logArea = new TextArea();
        logArea.setEditable(false);

        Button connectButton = new Button("Connect to Phone");
        connectButton.setOnAction(event -> connectToPhone());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(connectButton, logArea);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void connectToPhone() {
        try {
            String phoneIp = "192.168.29.86"; // Replace with the actual phone IP
            int port = 5050; // Port the phone server listens on

            logArea.appendText("Connecting to phone at " + phoneIp + ":" + port + "...\n");

            // Establish the connection
            Socket socket = new Socket(phoneIp, port);

            // Send a basic command
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            
            JsonObject command = new JsonObject();
            command.addProperty("counter", ++counter);
            command.addProperty("action", "touch");
            command.addProperty("x", 100);
            command.addProperty("y", 200);

            writer.println(command.toString());
            logArea.appendText("Command sent: " + command.toString() + "\n");

            socket.close();
            logArea.appendText("Connection closed.\n");

        } catch (Exception e) {
            logArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }

    public static void launchApp(String[] args) {
        Application.launch(args);
    }
}
