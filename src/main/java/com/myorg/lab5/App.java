package com.myorg.lab5;

import java.io.IOException;
import java.util.Collection;

import com.myorg.lab5.commands.AddCommand;
import com.myorg.lab5.commands.AddIfMinCommand;
import com.myorg.lab5.commands.ClearCommand;
import com.myorg.lab5.commands.CommandManager;
import com.myorg.lab5.commands.CountByStudioCommand;
import com.myorg.lab5.commands.ExecuteScriptCommand;
import com.myorg.lab5.commands.ExitCommand;
import com.myorg.lab5.commands.FilterLessThenNumOfPart;
import com.myorg.lab5.commands.HelpCommand;
import com.myorg.lab5.commands.InfoCommand;
import com.myorg.lab5.commands.RemoveById;
import com.myorg.lab5.commands.PrintDescendingCommand;
import com.myorg.lab5.commands.RemoveGreaterCommand;
import com.myorg.lab5.commands.RemoveLowerCommand;
import com.myorg.lab5.commands.SaveCommand;
import com.myorg.lab5.commands.ShowCommand;
import com.myorg.lab5.commands.UpdateIdCommand;
import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.io.FileManager;
import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;


/**
 * Главный класс приложения.
 * Точка входа в программу управления коллекцией MusicBand.
 * 
 * <p>При запуске:
 * <ol>
 *   <li>Читает имя файла из переменной окружения DATA</li>
 *   <li>Загружает коллекцию из CSV файла (если существует)</li>
 *   <li>Регистрирует все доступные команды</li>
 *   <li>Запускает интерактивный режим ввода команд</li>
 * </ol>
 */
public class App {

    public static void main(String[] args) {

        String fileName = System.getenv("DATA");
        if(fileName == null){
            System.err.println("environment variable not found");
        }

        FileManager fileManager = new FileManager(fileName);
        CommandManager commandManager = new CommandManager();
        CollectionManager collectionManager = new CollectionManager();
        ConsoleManager consoleManager = new ConsoleManager();


        try{
            Collection<MusicBand> collection = fileManager.load();
            for (MusicBand band : collection) {
                collectionManager.add(band);
            }
        }catch (IOException e) {
            consoleManager.show("Could not load file: " + e.getMessage());
            consoleManager.show("Starting with empty collection");
        }

        //Регистрация команд
        commandManager.register("add", new AddCommand(collectionManager, consoleManager));
        commandManager.register("add_if_min", new AddIfMinCommand(collectionManager, consoleManager));
        commandManager.register("help", new HelpCommand(commandManager));
        commandManager.register("info", new InfoCommand(collectionManager, consoleManager));
        commandManager.register("show", new ShowCommand(collectionManager, consoleManager));
        commandManager.register("clear", new ClearCommand(collectionManager));
        commandManager.register("save", new SaveCommand(collectionManager));
        commandManager.register("exit", new ExitCommand(consoleManager));
        commandManager.register("count_by_studio", new CountByStudioCommand(collectionManager, consoleManager));
        commandManager.register("filter_less_than_number_of_participants", new FilterLessThenNumOfPart(collectionManager, consoleManager));
        commandManager.register("print_descending", new PrintDescendingCommand(collectionManager, consoleManager));
        commandManager.register("update", new UpdateIdCommand(collectionManager, consoleManager));
        commandManager.register("remove_greater", new RemoveGreaterCommand(collectionManager, consoleManager));
        commandManager.register("remove_lower", new RemoveLowerCommand(collectionManager, consoleManager));
        commandManager.register("remove_by_id", new RemoveById(collectionManager));
        commandManager.register("execute_script", new ExecuteScriptCommand(commandManager, consoleManager));
        
        while(true){
            String input = consoleManager.read();
            commandManager.execute(input);
        }
    }
}
