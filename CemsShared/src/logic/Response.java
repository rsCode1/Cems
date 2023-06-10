package logic;

import java.io.Serializable;

public class Response implements Serializable  {
    private String responseType;
    private Object responseParam;
    
    public Response(String responseType, Object responseParam) {
        this.responseType = responseType;
        this.responseParam = responseParam;
    }
    
    public String getResponseType() {
        return responseType;
    }
    
    public Object getResponseParam() {
        return responseParam;
    }
}
