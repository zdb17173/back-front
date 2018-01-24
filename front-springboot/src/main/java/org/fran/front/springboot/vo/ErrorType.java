package org.fran.front.springboot.vo;

import org.apache.http.HttpStatus;

/**
 * Created by fran on 2018/1/22.
 */
public class ErrorType {
    int status;
    String message;

    public ErrorType(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
