package com.myorg.lab5;

// import com.myorg.lab5.model.Coordinates;
// import com.myorg.lab5.model.MusicBand;
// import com.myorg.lab5.model.MusicGenre;

public class SerialTest {
    // public static void main(String[] args){
    //     try{
    //         MusicBand band = new MusicBand("Radiohead", new Coordinates(98, 87), 8, 1, MusicGenre.POP);
    //         CommandRequest originalRequest = new CommandRequest("add", band);
    //         System.out.println("Оригинальный запрос: " + originalRequest);

    //         byte[] serializedData = SerializationUtil.serialize(originalRequest);
    //         System.out.println("Сериализовано в " + serializedData.length + " байт");

    //         CommandRequest deserializedRequest = (CommandRequest) SerializationUtil.deserialize(serializedData);
    //         System.out.println("Десериализованный запрос: " + deserializedRequest);
            
    //         // 5. Проверяем, что данные сохранились
    //         System.out.println("Имя команды: " + deserializedRequest.getCommandName());
    //         MusicBand restoredBand = (MusicBand) deserializedRequest.getArgs()[0];
    //         System.out.println("Имя группы: " + restoredBand.getName());
            
    //         // 6. Тестируем CommandResponse
    //         CommandResponse successResponse = CommandResponse.success("Операция выполнена", restoredBand);
    //         System.out.println("Успешный ответ: " + successResponse);
            
    //         CommandResponse errorResponse = CommandResponse.error("Ошибка: группа не найдена");
    //         System.out.println("Ответ с ошибкой: " + errorResponse);
    //     }catch(Exception e){
    //         e.printStackTrace();
    //     }
    // }
}
