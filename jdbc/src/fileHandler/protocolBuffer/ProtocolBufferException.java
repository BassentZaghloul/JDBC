package fileHandler.protocolBuffer;

public class ProtocolBufferException extends Exception {
    
    /**
    * 
    */
    private static final long serialVersionUID = 1L;
    private String errorMessage;
    
    public ProtocolBufferException(String error) {
        this.errorMessage = error;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
}
