package com.myorg.lab5.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Collection;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.myorg.lab5.commands.AddCommand;
import com.myorg.lab5.commands.AddIfMinCommand;
import com.myorg.lab5.commands.CheckUpdateCommand;
import com.myorg.lab5.commands.ClearCommand;
import com.myorg.lab5.commands.CommandManager;
import com.myorg.lab5.commands.CountByStudioCommand;
import com.myorg.lab5.commands.ExecuteScriptCommand;
import com.myorg.lab5.commands.HelpCommand;
import com.myorg.lab5.commands.InfoCommand;
import com.myorg.lab5.commands.MyShowCommand;
import com.myorg.lab5.commands.PrintDescendingCommand;
import com.myorg.lab5.commands.FilterLessThenNumOfPart;
import com.myorg.lab5.commands.RemoveById;
import com.myorg.lab5.commands.RemoveGreaterCommand;
import com.myorg.lab5.commands.RemoveLowerCommand;
import com.myorg.lab5.commands.ShowCommand;
import com.myorg.lab5.commands.UpdateIdCommand;
import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.MusicBandParser;


public class ServerMain {
    private static final int PORT = 9807;
    private static final Logger logger = LogManager.getLogger(ServerMain.class);
    public static void main(String[] args){
        logger.info("Server initialization...\n");

        try{

            String dbUrl = "jdbc:postgresql://pg/studs";
            String dbUser = "s502501";
            String dbPassword = "vxTj72Ecz4qlSlv6";
            DBManager dbManager = new DBManager(dbUrl, dbUser, dbPassword);


            CollectionManager collectionManager = new CollectionManager(dbManager);
            CommandManager commandManager = createCommandManager(collectionManager);

            collectionManager.loadFromDB();
            logger.info("Loaded {} items", collectionManager.getList().size());

            RequestReader requestReader = new RequestReader();
            CommandExecutor commandExecutor = new CommandExecutor(commandManager, dbManager);

            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(PORT));
            logger.info("Server started on port {} in non-blocking mode", PORT);
            ResponseSender responseSender = new ResponseSender(channel);

            ConnectionListener connectionListener = new ConnectionListener(channel, requestReader, commandExecutor, responseSender);


            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Finishing...");
                connectionListener.stop();
            }));

            connectionListener.start();
        }catch (IOException e) {
            logger.warn("Can't upload the file: " + e.getMessage());
            logger.warn("Start with empty collection");
        } catch (Exception e) {
            logger.error("Unexpected error", e);
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
        commandManager.register("check_update", new CheckUpdateCommand(collectionManager));       
        commandManager.register("remove_by_id", new RemoveById(collectionManager));
        commandManager.register("remove_greater", new RemoveGreaterCommand(collectionManager));
        commandManager.register("remove_lower", new RemoveLowerCommand(collectionManager));
        commandManager.register("count_by_studio", new CountByStudioCommand(collectionManager));
        commandManager.register("filter_less_than_number_of_participants", new FilterLessThenNumOfPart(collectionManager));
        commandManager.register("print_descending", new PrintDescendingCommand(collectionManager));
        commandManager.register("execute_script", new ExecuteScriptCommand(commandManager));
        commandManager.register("my_show", new MyShowCommand(collectionManager));

        return commandManager;
    }
}
