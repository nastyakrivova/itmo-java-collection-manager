package com.myorg.lab5.client.gui.table;

import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.model.Studio;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class TableManager {
    private final TableView<MusicBand> table;
    private final ObservableList<MusicBand> data = FXCollections.observableArrayList();
    private final FilteredList<MusicBand> filteredData;
    private final SortedList<MusicBand> sortedData;
    
    public TableManager(TableView<MusicBand> table,
                        TableColumn<MusicBand, Integer> idCol,
                        TableColumn<MusicBand, String> nameCol,
                        TableColumn<MusicBand, Integer> participantsCol,
                        TableColumn<MusicBand, Integer> albumsCol,
                        TableColumn<MusicBand, String> genreCol,
                        TableColumn<MusicBand, Integer> ownerCol,
                        TableColumn<MusicBand, Integer> singlesCol,
                        TableColumn<MusicBand, String> studioCol,
                        TableColumn<MusicBand, LocalDate> creationDateCol,
                        TextField filterField) {
        
        this.table = table;
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        participantsCol.setCellValueFactory(new PropertyValueFactory<>("numberOfParticipants"));
        albumsCol.setCellValueFactory(new PropertyValueFactory<>("albumsCount"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        singlesCol.setCellValueFactory(new PropertyValueFactory<>("singlesCount"));
        creationDateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        studioCol.setCellValueFactory(cellData -> {
            Studio studio = cellData.getValue().getStudio();
            return new SimpleStringProperty(studio != null ? studio.getName() : "");
        });
        
        filteredData = new FilteredList<>(data, p -> true);
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
        
        filterField.textProperty().addListener((obs, old, newVal) -> {
            filteredData.setPredicate(band -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return band.getName().toLowerCase().contains(lower) ||
                       String.valueOf(band.getId()).contains(lower);
            });
        });
    }
    
    public void setData(List<MusicBand> bands) {
        data.setAll(bands);
    }
    
    public MusicBand getSelected() {
        return table.getSelectionModel().getSelectedItem();
    }
    
    public void refresh() {
        table.refresh();
    }
    
    public int getSize() {
        return data.size();
    }

    public List<MusicBand> getAllData() {
        return data;
    }
}