package com.myorg.lab7.client;

import com.myorg.lab7.data_exchange.CommandResponse;
import com.myorg.lab7.io.ConsoleManager;

public class ResponsePrinter {
    private final ConsoleManager consoleManager;

    public ResponsePrinter(ConsoleManager consoleManager){
        this.consoleManager = consoleManager;
    }

    public void print(CommandResponse response){
        if(response == null){
            consoleManager.show("Пустой ответ от сервера\n");
            return;
        }

        if (response.isSuccess()){

            if (response.getMessage() != null && !response.getMessage().isEmpty()) {
                consoleManager.show(response.getMessage());
            }

            if (response.getData() != null) {
                consoleManager.show(response.getData().toString());
            }

            if (response.getMessage() == null && response.getData() == null) {
                consoleManager.show("Команда выполнена успешно");
            }
        }else {
            consoleManager.show("Ошибка: " + response.getMessage());
        }
    }
}
