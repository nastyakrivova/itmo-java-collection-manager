package com.myorg.lab5.commands;


import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.ScriptParser;

/**
 * Команда обновления элемента по ID.
 * Заменяет элемент с указанным ID новыми данными.
 */
public class UpdateIdCommand implements Command{
    private final CollectionManager collectionManager;
    private final ScriptParser parser = new ScriptParser();

    public UpdateIdCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }
    
    /**
     * Обновляет элемент с указанным ID.
     * 
     * @param args массив аргументов, где args[0] - ID обновляемого элемента
     */
    @Override
    public void execute(String[] args, int userId){

        if (args.length == 0) {
            System.out.println("Ошибка: не указан ID. Использование: update <id>");
            return;
        }
    
        try {
            Integer id = Integer.parseInt(args[0]);
            
            if (collectionManager.containsId(id)){
                MusicBand updatedMusicBand = parser.parse(args[1]);
                collectionManager.updateId(id, updatedMusicBand, userId);
                System.out.println("Music band with id: " + id + " has been updated");
            } else {
                System.out.println("Id not found: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        }
    }

    @Override
    public String getDescription(){
        return "- Update an element by its ID";
    }
}
