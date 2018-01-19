package org.fran.front.common;

public class BaseResult {
    private String status;
    private String errorMsg;
    private String version;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatus() {
        return Integer.valueOf(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatus(int status){
        this.status = String.valueOf(status);
    }
}