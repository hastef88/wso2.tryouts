
/**
 * ProductServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package org.wso2.carbon.orderprocessor.stub;

    /**
     *  ProductServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ProductServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ProductServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ProductServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for delete method
            * override this method for handling normal response from delete operation
            */
           public void receiveResultdelete(
                    org.wso2.carbon.orderprocessor.stub.ProductServiceStub.DeleteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from delete operation
           */
            public void receiveErrordelete(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for get method
            * override this method for handling normal response from get operation
            */
           public void receiveResultget(
                    org.wso2.carbon.orderprocessor.stub.ProductServiceStub.GetResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from get operation
           */
            public void receiveErrorget(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for create method
            * override this method for handling normal response from create operation
            */
           public void receiveResultcreate(
                    org.wso2.carbon.orderprocessor.stub.ProductServiceStub.CreateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from create operation
           */
            public void receiveErrorcreate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for list method
            * override this method for handling normal response from list operation
            */
           public void receiveResultlist(
                    org.wso2.carbon.orderprocessor.stub.ProductServiceStub.ListResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from list operation
           */
            public void receiveErrorlist(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for update method
            * override this method for handling normal response from update operation
            */
           public void receiveResultupdate(
                    org.wso2.carbon.orderprocessor.stub.ProductServiceStub.UpdateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from update operation
           */
            public void receiveErrorupdate(java.lang.Exception e) {
            }
                


    }
    