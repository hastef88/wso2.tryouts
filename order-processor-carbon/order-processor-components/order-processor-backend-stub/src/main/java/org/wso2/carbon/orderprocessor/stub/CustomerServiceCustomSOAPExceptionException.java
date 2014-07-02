
/**
 * CustomerServiceCustomSOAPExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package org.wso2.carbon.orderprocessor.stub;

public class CustomerServiceCustomSOAPExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1400570082801L;
    
    private org.wso2.carbon.orderprocessor.stub.CustomerServiceStub.CustomerServiceCustomSOAPException faultMessage;

    
        public CustomerServiceCustomSOAPExceptionException() {
            super("CustomerServiceCustomSOAPExceptionException");
        }

        public CustomerServiceCustomSOAPExceptionException(java.lang.String s) {
           super(s);
        }

        public CustomerServiceCustomSOAPExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public CustomerServiceCustomSOAPExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(org.wso2.carbon.orderprocessor.stub.CustomerServiceStub.CustomerServiceCustomSOAPException msg){
       faultMessage = msg;
    }
    
    public org.wso2.carbon.orderprocessor.stub.CustomerServiceStub.CustomerServiceCustomSOAPException getFaultMessage(){
       return faultMessage;
    }
}
    