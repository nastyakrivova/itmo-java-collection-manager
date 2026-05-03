package com.myorg.lab5.data_exchange;

import java.util.ArrayList;
import java.util.List;

public class Batch {
    private List<CommandRequest> requests = new ArrayList<>();
    private List<CommandResponse> responses = new ArrayList<>();
    
    public void addRequest(CommandRequest request) {
        requests.add(request);
    }
    
    public void addResponse(CommandResponse response) {
        responses.add(response);
    }
    
    public List<CommandRequest> getRequests() { return requests; }
    public List<CommandResponse> getResponses() { return responses; }
    public void setResponses(List<CommandResponse> responses) { this.responses = responses; }
    public boolean isEmpty() { return requests.isEmpty() && responses.isEmpty(); }

}
