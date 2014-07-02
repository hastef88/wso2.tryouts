package org.wso2.carbon.orderprocessor.backend.util;

import org.wso2.carbon.orderprocessor.backend.enumerations.SOAPStatusEnum;

/**
 * Created by hasithad on 4/10/14.
 */
public class CustomSOAPException extends Exception {
    private SOAPStatusEnum errorCode;
    private String message;

    public CustomSOAPException(SOAPStatusEnum errorCode,String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public SOAPStatusEnum getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(SOAPStatusEnum errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
