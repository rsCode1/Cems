package logic;

import java.util.function.IntConsumer;

public class RequestTime {
    private int requestID;
    private String courseName;
    private String requestedBy;
    private String reason;
    private int extraTime;


    public int getId() { return  requestID; }   
    public void setId(int requestID) { this.requestID = requestID; }  
    public String getCourseName() { return  courseName; }       
    public void setCourseName(String courseName) { this.courseName = courseName; }  
    public String getRequestedBy() { return  requestedBy; } 
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }  
    public int getExtraTime() { return  extraTime; } 
    public void setExtraTime(int extraTime) { this.extraTime = extraTime; }  
    public String getReason() { return  reason; } 
    public void setReason(String reason) { this.reason = reason; }    
    
    
    
    public RequestTime(int requestID, String courseName, String requestedBy,int extraTime, String reason) {    
        this.requestID = requestID;
        this.courseName = courseName;
        this.requestedBy = requestedBy;
        this.extraTime = extraTime;
        this.reason = reason;
    


}
}