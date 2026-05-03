package com.myorg.lab5.server;

import com.myorg.lab5.commands.CommandManager;
import com.myorg.lab5.data_exchange.CommandRequest;
import com.myorg.lab5.data_exchange.CommandResponse;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.ScriptParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandExecutor {
    private static final Logger logger = LogManager.getLogger(CommandExecutor.class);
    private final CommandManager commandManager;
    private final DBManager dbManager;


    public CommandExecutor(CommandManager commandManager, DBManager dbManager){
        this.commandManager = commandManager;
        this.dbManager = dbManager;
    }

    public CommandResponse execute(CommandRequest request){
        String commandName = request.getCommandName();
        Object[] args = request.getArgs();
        String login = request.getLogin();
        String password = request.getPassword();

        if (commandName.equals("login")){
            return handleLogin(args);
        }
        if (commandName.equals("register")){
            return handleRegiser(args);
        }
        

        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            return CommandResponse.error("Not authenticated. Please login first.");
        }
        
        Integer userId;
        try {
            userId = dbManager.authentication(login, password);
            if (userId == null) {
                return CommandResponse.error("Invalid credentials. Please login again.");
            }
        } catch (Exception e) {
            logger.error("Authentication error: {}", e.getMessage());
            return CommandResponse.error("Authentication error: " + e.getMessage());
        }
        
        String[] stringArgs = toStringArgs(args);
        String result = commandManager.executeAndGetResult(commandName, stringArgs, userId);
        
        if (result == null) {
            return CommandResponse.error("Command execution failed");
        }
        if (result.isEmpty()) {
            return CommandResponse.success("Команда выполнена");
        }
        return CommandResponse.success(result);
        
    }

    private String[] toStringArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return new String[0];
        }
        String[] result = new String[args.length];
        for(int i = 0; i < args.length; i++) {
            if (args[i] instanceof MusicBand) {
                ScriptParser parser = new ScriptParser();
                result[i] = parser.toCsv((MusicBand) args[i]);
            } else if (args[i] != null) {
                result[i] = args[i].toString();
            } else {
                result[i] = "";
            }
        }
        return result;
    }

    private CommandResponse handleLogin(Object[] args){
        if(args == null || args.length < 2){
            return CommandResponse.error("Usage: login <username> <password>");
        }
        String login = args[0].toString();
        String password = args[1].toString();
        
        try {
            Integer userId = dbManager.authentication(login, password);
            if (userId != null) {
                return CommandResponse.success("Login successful. Welcome, " + login + "!");
            } else {
                return CommandResponse.error("Login failed: invalid username or password");
            }
        } catch (Exception e) {
            logger.error("Login error: {}", e.getMessage());
            return CommandResponse.error("Login error: " + e.getMessage());
        }
    }

    private CommandResponse handleRegiser(Object[] args){
        if (args == null || args.length < 2) {
            return CommandResponse.error("Usage: register <username> <password>");
        }
        
        String login = args[0].toString();
        String password = args[1].toString();
        
        try {
            boolean success = dbManager.registerUser(login, password);
            if (success) {
                return CommandResponse.success("Registration successful! You can now login.");
            } else {
                return CommandResponse.error("Registration failed: username already exists");
            }
        } catch (Exception e) {
            logger.error("Registration error: {}", e.getMessage());
            return CommandResponse.error("Registration error: " + e.getMessage());
        }
    }
}
