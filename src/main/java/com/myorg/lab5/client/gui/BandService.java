package com.myorg.lab5.client.gui;

import com.myorg.lab5.client.NetworkClient;
import com.myorg.lab5.client.gui.MainApp;
import com.myorg.lab5.data_exchange.CommandRequest;
import com.myorg.lab5.data_exchange.CommandResponse;
import com.myorg.lab5.model.MusicBand;
import com.myorg.lab5.utils.MusicBandParser;

public class BandService {
    private final MainApp mainApp;
    private final MusicBandParser parser = new MusicBandParser();
    
    public BandService(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    private NetworkClient getClient() { return mainApp.getNetworkClient(); }
    private String getLogin() { return mainApp.getCurrentLogin(); }
    private String getPassword() { return mainApp.getCurrentPassword(); }
    
    private CommandResponse send(String commandName, Object[] args) throws Exception {
        CommandRequest request = new CommandRequest(commandName, args, getLogin(), getPassword());
        return getClient().sendCommand(request);
    }
    
    private CommandResponse sendSimple(String commandName) throws Exception {
        return send(commandName, new Object[0]);
    }
    
    private CommandResponse sendWithData(String commandName, MusicBand band) throws Exception {
        return send(commandName, new Object[]{parser.toCsv(band)});
    }
    
    private CommandResponse sendWithInt(String commandName, int value) throws Exception {
        return send(commandName, new Object[]{value});
    }
    
    private CommandResponse sendWithString(String commandName, String value) throws Exception {
        return send(commandName, new Object[]{value});
    }

    public CommandResponse show() throws Exception { return sendSimple("show"); }
    public CommandResponse info() throws Exception { return sendSimple("info"); }
    public CommandResponse clear() throws Exception { return sendSimple("clear"); }
    
    public CommandResponse add(MusicBand band) throws Exception { return sendWithData("add", band); }
    public CommandResponse addIfMin(MusicBand band) throws Exception { return sendWithData("add_if_min", band); }
    public CommandResponse removeGreater(MusicBand band) throws Exception { return sendWithData("remove_greater", band); }
    public CommandResponse removeLower(MusicBand band) throws Exception { return sendWithData("remove_lower", band); }
    
    public CommandResponse update(int id, MusicBand band) throws Exception {

            String bandData = parser.toCsv(band);
            
            return send("update", new Object[]{id, bandData});
    }
    
    public CommandResponse removeById(int id) throws Exception { return sendWithInt("remove_by_id", id); }
    public CommandResponse filterLessThanParticipants(int participants) throws Exception {
        return sendWithInt("filter_less_than_number_of_participants", participants);
    }
    
    public CommandResponse countByStudio(String studioName) throws Exception {
        return sendWithString("count_by_studio", studioName);
    }
}