package org.wso2.carbon.orderprocessor.backend.util;

import org.wso2.carbon.orderprocessor.backend.enumerations.SOAPStatusEnum;

/**
 * Created by hasithad on 4/9/14.
 */
public class SOAPStatus {

    private String message;
    private SOAPStatusEnum status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SOAPStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SOAPStatusEnum status) {
        this.status = status;
    }
}
