

import com.myorg.lab5.io.ConsoleManager;
import com.myorg.lab5.model.CollectionManager;
import com.myorg.lab5.model.MusicBand;

/**
 * Команда обновления элемента по ID.
 * Заменяет элемент с указанным ID новыми данными.
 */
public class UpdateIdCommand implements Command{
    private final CollectionManager collectionManager;
    private final ConsoleManager parser;

    public UpdateIdCommand(CollectionManager collectionManager, ConsoleManager parser){
        this.collectionManager = collectionManager;
        this.parser = parser;
    }
    
    /**
     * Обновляет элемент с указанным ID.
     * 
     * @param args массив аргументов, где args[0] - ID обновляемого элемента
     */
    @Override
    public void execute(String[] args){

        if (args.length == 0) {
            parser.show("Ошибка: не указан ID. Использование: update <id>");
            return;
        }
    
        try {
            Integer id = Integer.parseInt(args[0]);
            
            if (collectionManager.containsId(id)){
                MusicBand updatedMusicBand = parser.parse();
                collectionManager.updateId(id, updatedMusicBand);
                parser.show("Music band with id: " + id + " has been updated");
            } else {
                parser.show("Id not found: " + id);
            }
        } catch (NumberFormatException e) {
            parser.show("Ошибка: ID должен быть числом");
        }
    }


    @Override
    public String getDescription(){
        return "- Update an element by its ID";
    }
}
