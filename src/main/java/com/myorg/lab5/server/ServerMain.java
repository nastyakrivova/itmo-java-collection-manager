package com.myorg.lab5.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Collection;

import com.myorg.lab5.commands.AddCommand;
import com.myorg.lab5.commands.AddIfMinCommand;
import com.myorg.lab5.commands.ClearCommand;
import com.myorg.lab5.commands.CommandManager;
import com.myorg.lab5.commands.CountByStudioCommand;
import com.myorg.lab5.commands.ExecuteScriptCommand;
import com.myorg.lab5.commands.HelpCommand;
import com.myorg.lab5.commands.InfoCommand;
import com.myorg.lab5.commands.PrintDescendingCommand;
import com.myorg.lab5.commands.FilterLessThenNumOfPart;
import com.myorg.lab5.commands.RemoveById;
import com.myorg.lab5.commands.RemoveGreaterCommand;
import com.myorg.lab5.commands.RemoveLowerCommand;
import com.myorg.lab5.commands.ShowCommand;
import com.myorg.lab5.commands.ServerSaveCommand;
import com.myorg.lab5.commands.UpdateIdCommand;
import com.myorg.lab5.io.FileManager;
import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.ScriptParser;


public class ServerMain {
    private static final int PORT = 9807;
    public static void main(String[] args){
        System.out.println("Инициализация сервера...\n");

        String fileName = System.getenv("DATA");
        if(fileName == null){
            System.err.println("Переменная окружения DATA не установлена");
        }

        try{
            FileManager fileManager = new FileManager(fileName);
            CollectionManager collectionManager = new CollectionManager();
            CommandManager commandManager = createCommandManager(collectionManager);

            Collection<MusicBand> collection = fileManager.load();
            for (MusicBand band : collection) {
                collectionManager.add(band);
            }

            RequestReader requestReader = new RequestReader();
            CommandExecutor commandExecutor = new CommandExecutor(commandManager, collectionManager);

            // DatagramSocket socket = new DatagramSocket(PORT);
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(PORT));
            ResponseSender responseSender = new ResponseSender(channel);

            ConnectionListener connectionListener = new ConnectionListener(channel, requestReader, commandExecutor, responseSender);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Сохранение коллекции...");
                collectionManager.save();
                try{
                    channel.close();
                } catch(Exception e){
                    System.out.println(e.getMessage());
                }
                
            }));

            connectionListener.start();
        }catch (IOException e) {
            System.out.println("Не получается загрузить файл: " + e.getMessage());
            System.out.println("Начало с пустой коллекцией");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static CommandManager createCommandManager(CollectionManager collectionManager){
        CommandManager commandManager = new CommandManager();

        commandManager.register("help", new HelpCommand(commandManager));
        commandManager.register("info", new InfoCommand(collectionManager));
        commandManager.register("show", new ShowCommand(collectionManager));
        commandManager.register("clear", new ClearCommand(collectionManager));
        commandManager.register("add", new AddCommand(collectionManager));
        commandManager.register("add_if_min", new AddIfMinCommand(collectionManager));
        commandManager.register("update", new UpdateIdCommand(collectionManager));
        commandManager.register("remove_by_id", new RemoveById(collectionManager));
        commandManager.register("remove_greater", new RemoveGreaterCommand(collectionManager));
        commandManager.register("remove_lower", new RemoveLowerCommand(collectionManager));
        commandManager.register("count_by_studio", new CountByStudioCommand(collectionManager));
        commandManager.register("filter_less_than_number_of_participants", new FilterLessThenNumOfPart(collectionManager));
        commandManager.register("print_descending", new PrintDescendingCommand(collectionManager));
        commandManager.register("execute_script", new ExecuteScriptCommand(new ScriptParser()));

        commandManager.register("server_save", new ServerSaveCommand(collectionManager));

        return commandManager;
    }
}
