package com.myorg.lab5.data_exchange;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Batch implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int sequenceNumber;
    private int totalBatches; 
    private List<CommandRequest> requests = new ArrayList<>();
    private List<CommandResponse> responses = new ArrayList<>();
    
    public int getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(int sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    
    public int getTotalBatches() { return totalBatches; }
    public void setTotalBatches(int totalBatches) { this.totalBatches = totalBatches; }
    
    public void addRequest(CommandRequest request) { requests.add(request); }
    public List<CommandRequest> getRequests() { return requests; }
    public void setRequests(List<CommandRequest> requests) { this.requests = requests; }
    
    public void addResponse(CommandResponse response) { responses.add(response); }
    public List<CommandResponse> getResponses() { return responses; }
    public void setResponses(List<CommandResponse> responses) { this.responses = responses; }
    
    public boolean isEmpty() { return requests.isEmpty() && responses.isEmpty(); }
}