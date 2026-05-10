package com.myorg.lab5.data_exchange;

import java.io.Serializable;
import java.util.Arrays;

public class CommandRequest implements Serializable{
    private static final long serialVersionUID = 1L;
    private int requestId;
    private final String commandName;
    private final Object[] args;
    private final String login;
    private final String password;
    
    public CommandRequest(String commandName, Object[] args, String login, String password){
        this.args = args != null ? args : new Object[0];
        this.commandName = commandName;
        this.login = login;
        this.password = password;
        this.requestId = -1;
    }

    public CommandRequest(String commandName, String login, String password) {
        this(commandName, new Object[0], login, password);
    }


    public String getCommandName(){
        return commandName;
    }

    public Object[] getArgs(){
        return args;
    }

    public String getLogin(){
        return login;
    }

    public String getPassword(){
        return password;
    }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    @Override
    public String toString() {
        return "CommandRequest{" +
               "commandName='" + commandName + '\'' +
               ", arguments=" + Arrays.toString(args) +
               '}';
    }
}