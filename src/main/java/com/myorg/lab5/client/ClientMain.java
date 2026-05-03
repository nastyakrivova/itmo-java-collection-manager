package com.myorg.lab5.client;

import java.net.SocketTimeoutException;
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

        ConsoleManager consoleManager = new ConsoleManager();
        Validator validator = new Validator();
        CommandBuilder commandBuilder = new CommandBuilder(consoleManager, validator);
        ResponsePrinter responsePrinter = new ResponsePrinter(consoleManager);

        try (NetworkClient networkClient = new NetworkClient(SERVER_HOST, SERVER_PORT)) {
            consoleManager.show("Подключение установлено");
            
            // ========== АУТЕНТИФИКАЦИЯ (цикл до успеха) ==========
            while (!authenticateUser(consoleManager, networkClient)) {
                consoleManager.show("Повторитe попытку");
            }
            
            consoleManager.show("Авторизация успешна!");
            consoleManager.show("Введите help для списка команд");

            // ========== ОСНОВНОЙ ЦИКЛ КОМАНД ==========
            while (true) {
                try {
                    String input = consoleManager.read();

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

                    // Используем только один способ создания CommandRequest
                    CommandRequest request = commandBuilder.build(input, currentLogin, currentPassword);

                    if (request == null) {
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
        }
    }

    private static boolean authenticateUser(ConsoleManager consoleManager, NetworkClient networkClient) {
        try {
            String choice = consoleManager.read("1 - Вход, 2 - Регистрация: ");
            
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
            
            // ЕДИНСТВЕННЫЙ СПОСОБ создать CommandRequest
            CommandRequest authRequest = new CommandRequest(
                commandName, 
                new String[]{login, password}, 
                null,  // при регистрации/логине логин/пароль в args, а не в полях
                null
            );
            
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