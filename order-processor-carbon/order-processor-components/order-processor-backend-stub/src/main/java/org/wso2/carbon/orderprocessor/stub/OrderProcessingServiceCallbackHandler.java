
/**
 * OrderProcessingServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package org.wso2.carbon.orderprocessor.stub;

    /**
     *  OrderProcessingServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class OrderProcessingServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public OrderProcessingServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public OrderProcessingServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for removeProducts method
            * override this method for handling normal response from removeProducts operation
            */
           public void receiveResultremoveProducts(
                    org.wso2.carbon.orderprocessor.stub.OrderProcessingServiceStub.RemoveProductsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from removeProducts operation
           */
            public void receiveErrorremoveProducts(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for get method
            * override this method for handling normal response from get operation
            */
           public void receiveResultget(
                    org.wso2.carbon.orderprocessor.stub.OrderProcessingServiceStub.GetResponse result
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
                    org.wso2.carbon.orderprocessor.stub.OrderProcessingServiceStub.CreateResponse result
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
                    org.wso2.carbon.orderprocessor.stub.OrderProcessingServiceStub.ListResponse result
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
                    org.wso2.carbon.orderprocessor.stub.OrderProcessingServiceStub.UpdateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from update operation
           */
            public void receiveErrorupdate(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addProducts method
            * override this method for handling normal response from addProducts operation
            */
           public void receiveResultaddProducts(
                    org.wso2.carbon.orderprocessor.stub.OrderProcessingServiceStub.AddProductsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addProducts operation
           */
            public void receiveErroraddProducts(java.lang.Exception e) {
            }
                


    }
    