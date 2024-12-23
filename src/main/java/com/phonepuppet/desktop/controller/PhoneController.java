package com.phonepuppet.desktop.controller;

import org.springframework.web.bind.annotation.*;
import com.google.gson.JsonObject;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

@RestController
@RequestMapping("/api/phone")
public class PhoneController {

    private int counter = 0;

    @PostMapping("/connect")
    public String connectToPhone(@RequestBody CommandRequest commandRequest) {
        String phoneIp = commandRequest.getPhoneIp();
        int port = commandRequest.getPort();

        try (Socket socket = new Socket(phoneIp, port)) {
            JsonObject command = new JsonObject();
            command.addProperty("counter", ++counter);
            command.addProperty("action", "touch");
            command.addProperty("x", commandRequest.getX());
            command.addProperty("y", commandRequest.getY());

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(command.toString());

            return "Command sent: " + command.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
