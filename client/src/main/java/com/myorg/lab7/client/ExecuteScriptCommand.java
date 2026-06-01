package com.myorg.lab7.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.myorg.lab7.client.CommandBuilder;
import com.myorg.lab7.client.NetworkClient;
import com.myorg.lab7.data_exchange.CommandRequest;
import com.myorg.lab7.data_exchange.CommandResponse;

public class ExecuteScriptCommand {
    
    private final NetworkClient networkClient;
    private final CommandBuilder commandBuilder;
    private final String login;
    private final String password;
    
    public ExecuteScriptCommand(NetworkClient networkClient, 
                                CommandBuilder commandBuilder,
                                String login, 
                                String password) {
        this.networkClient = networkClient;
        this.commandBuilder = commandBuilder;
        this.login = login;
        this.password = password;
    }
    
    public void execute(String fileName) {
        List<CommandRequest> allRequests = new ArrayList<>();
        int lineNum = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                CommandRequest request = commandBuilder.build(line, login, password);
                if (request == null) {
                    System.err.println("Ошибка парсинга строки " + lineNum);
                    return;
                }
                
                allRequests.add(request);
            }
            
            if (allRequests.isEmpty()) {
                System.out.println("Скрипт не содержит команд");
                return;
            }
            
            System.out.println("\nВсего команд: " + allRequests.size());
            List<CommandResponse> allResponses = networkClient.sendScript(allRequests);
            
            System.out.println("\n Результаты:");
            for (int i = 0; i < allResponses.size(); i++) {
                CommandResponse resp = allResponses.get(i);
                CommandRequest req = allRequests.get(i);
                System.out.println("  " + (i+1) + ". " + req.getCommandName() + 
                                   ": " + resp.getMessage());
            }
            
            System.out.println("\nСкрипт выполнен!");
            
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}