package com.myorg.lab5;

import java.io.Serializable;
import java.util.Arrays;

public class CommandRequest implements Serializable{
    private static final long serialVersionUID = 1L;
    private final String commandName;
    private final Object[] args;
    
    public CommandRequest(String commandName, Object... args){
        this.args = args != null ? args : new Object[0];
        this.commandName = commandName;
    }

    public String getCommandName(){
        return commandName;
    }

    public Object[] getArgs(){
        return args;
    }

    @Override
    public String toString() {
        return "CommandRequest{" +
               "commandName='" + commandName + '\'' +
               ", arguments=" + Arrays.toString(args) +
               '}';
    }
}