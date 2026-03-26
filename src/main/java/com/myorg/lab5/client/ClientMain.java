package com.myorg.lab5.client;

import java.net.SocketTimeoutException;

import com.myorg.lab5.CommandRequest;
import com.myorg.lab5.CommandResponse;
import com.myorg.lab5.io.ConsoleManager;

public class ClientMain {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9807;
    
    public static void main(String[] args){
        System.out.println("Клиент запущен...");

        ConsoleManager consoleManager = new ConsoleManager();
        Validator validator = new Validator();
        CommandBuilder commandBuilder = new CommandBuilder(consoleManager, validator);

        ResponsePrinter responsePrinter = new ResponsePrinter(consoleManager);

        try(NetworkClient networkClient = new NetworkClient(SERVER_HOST, SERVER_PORT)){
            consoleManager.show("Подключение установлено");
            consoleManager.show("Введите help для списка команд");

            while(true){
                try{

                    String input = consoleManager.read();

                    if(input == null || input.equals("exit")){
                        consoleManager.show("Завершение работы клиента...");
                        break;
                    }

                    String commandName = input.trim().split("\\s+")[0].toLowerCase();
                    if(!validator.isValidCommand(commandName)){
                        consoleManager.show("Неизвестная команда: " + commandName);
                        consoleManager.show("Введите 'help' для списка команд");
                        continue;
                    }

                    CommandRequest request = commandBuilder.build(input);

                    if(request == null){
                        continue;
                    }

                    CommandResponse response = networkClient.sendCommand(request);
                    responsePrinter.print(response);

                }catch(SocketTimeoutException e){
                    consoleManager.show("Ошибка: сервер временно недоступен. Повторите попытку.");
                }catch(Exception e){
                    consoleManager.show("Ошибка: " + e.getMessage());
                }
            }   
        }catch(Exception e){
            consoleManager.show("Не удалось подключиться к серверу: " + e.getMessage());
            System.exit(1);
        }
    }
}
