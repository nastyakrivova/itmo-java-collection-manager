package com.myorg.lab5.commands;


import com.myorg.lab5.utils.ScriptParser;

public class ExecuteScriptCommand implements Command{
    private ScriptParser scriptParser;

    public ExecuteScriptCommand(ScriptParser scriptParser){
        this.scriptParser = scriptParser;
    }
    
    @Override
    public void execute(String[] args){
        if (args.length < 1) {
            System.out.println("Error: Please specify script file name");
            return;
        }
        scriptParser.parse(args[0]);
    }

    @Override
    public String getDescription(){
        return "-Execute commands from script file (usage: execute_script filename)";
    }
}
