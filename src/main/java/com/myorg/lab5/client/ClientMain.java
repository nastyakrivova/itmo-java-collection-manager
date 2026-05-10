package com.myorg.lab5.client;

import java.net.SocketTimeoutException;
import java.util.Scanner;

import com.myorg.lab5.commands.ExecuteScriptCommand;
import com.myorg.lab5.data_exchange.CommandRequest;
import com.myorg.lab5.data_exchange.CommandResponse;
import com.myorg.lab5.io.ConsoleManager;

public class ClientMain {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9807;

    private static String currentLogin;
    private static String currentPassword;
    
    public static void main(String[] args) {
        System.out.println("Клиент запущен...");

        Scanner scanner = new Scanner(System.in);
        
        ConsoleManager consoleManager = new ConsoleManager(scanner);
        Validator validator = new Validator();
        CommandBuilder commandBuilder = new CommandBuilder(consoleManager, validator, scanner);

        ResponsePrinter responsePrinter = new ResponsePrinter(consoleManager);

        try (NetworkClient networkClient = new NetworkClient(SERVER_HOST, SERVER_PORT)) {
            consoleManager.show("Подключение установлено");
            
            while (!authenticateUser(consoleManager, networkClient, scanner)) {
                String retry = consoleManager.read("Повторить попытку? (y/n): ");
                if (!"y".equalsIgnoreCase(retry)) {
                    consoleManager.show("Завершение работы...");
                    return;
                }
            }
            
            consoleManager.show("успешно!");
            consoleManager.show("Введите help для списка команд");

            while (true) {
                try {
                    String input = consoleManager.read();


                    if (input.trim().startsWith("execute_script")) {
                        String[] parts = input.trim().split("\\s+", 2);
                        if (parts.length < 2) {
                            consoleManager.show("Ошибка: укажите имя файла");
                            continue;
                        }
                        
                        ExecuteScriptCommand scriptCmd = new ExecuteScriptCommand(
                            networkClient, 
                            commandBuilder, 
                            currentLogin, 
                            currentPassword
                        );
                        scriptCmd.execute(parts[1]);
                        continue;
                    }

                    if (input == null || input.equals("exit")) {
                        consoleManager.show("Завершение работы клиента...");
                        break;
                    }

                    String commandName = input.trim().split("\\s+")[0].toLowerCase();
                    if (!validator.isValidCommand(commandName)) {
                        consoleManager.show("Неизвестная команда: " + commandName);
                        consoleManager.show("Введите 'help' для списка команд");
                        continue;
                    }

                    CommandRequest request = commandBuilder.build(input, currentLogin, currentPassword);
                    
                    if (request == null) {
                        continue;
                    }
                    
                    if (request.getCommandName().equals("check_update")) {
                        CommandResponse response = networkClient.sendCommand(request);
                        String msg = response.getMessage();
                        
                        if (msg == null) {
                            consoleManager.show("Error checking element");
                            continue;
                        }
                        
                        if (msg.startsWith("NOT_FOUND:")) {
                            int id = Integer.parseInt(msg.split(":")[1]);
                            consoleManager.show("Element with id " + id + " not found");
                            continue;
                        }
                        
                        if (msg.startsWith("NOT_OWNER:")) {
                            int id = Integer.parseInt(msg.split(":")[1]);
                            consoleManager.show("Access denied: You are not the owner of element " + id);
                            continue;
                        }
                        
                        if (msg.startsWith("OK:")) {
                            int id = Integer.parseInt(msg.split(":")[1]);
                            CommandRequest updateRequest = commandBuilder.buildUpdateWithData(id, currentLogin, currentPassword);
                            if (updateRequest != null) {
                                CommandResponse updateResponse = networkClient.sendCommand(updateRequest);
                                consoleManager.show(updateResponse.getMessage());
                            }
                            continue;
                        }
                        
                        consoleManager.show(msg);
                        continue;
                    }
                    
                    CommandResponse response = networkClient.sendCommand(request);
                    responsePrinter.print(response);

                } catch (SocketTimeoutException e) {
                    consoleManager.show("Ошибка: сервер временно недоступен. Повторите попытку.");
                } catch (Exception e) {
                    consoleManager.show("Ошибка: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            consoleManager.show("Не удалось подключиться к серверу: " + e.getMessage());
            System.exit(1);
        } finally {
            scanner.close();
        }
    }

    private static boolean authenticateUser(ConsoleManager consoleManager, NetworkClient networkClient, Scanner scanner) {
        try {
            consoleManager.show("\n АВТОРИЗАЦИЯ");
            String choice = null;
            while (choice == null) {
                choice = consoleManager.read("1 - Вход | 2 - Регистрация: ");
                if (!"1".equals(choice) && !"2".equals(choice)) {
                    consoleManager.show("Неверный выбор. Введите 1 или 2");
                    choice = null;
                }
            }
            String login = consoleManager.read("Логин: ");
            String password = consoleManager.read("Пароль: ");
            
            String commandName;
            if ("1".equals(choice)) {
                commandName = "login";
            } else if ("2".equals(choice)) {
                commandName = "register";
            } else {
                consoleManager.show("Неверный выбор");
                return false;
            }
            
            CommandRequest authRequest = new CommandRequest(commandName, new Object[]{login, password}, null, null);
            CommandResponse response = networkClient.sendCommand(authRequest);
            
            if (response.isSuccess()) {
                currentLogin = login;
                currentPassword = password;
                return true;
            } else {
                consoleManager.show("Ошибка: " + response.getMessage());
                return false;
            }
        } catch (Exception e) {
            consoleManager.show("Ошибка при авторизации: " + e.getMessage());
            return false;
        }
    }
}