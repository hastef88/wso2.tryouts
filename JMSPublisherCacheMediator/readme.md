## JMS Publisher Cache Mediator for WSO2 ESB + WSO2 MB integration with JMS Topics

### Purpose

The default JMS transport in WSO2 ESB 4.8.1 does not support re-use of JMS sessions when publishing to the same destination multiple times. In the perspective of the message broker, this involves creation/disconnection of JMS sessions per each published message, introducing an unnecessary performance and memory overhead. 

There is a workaround to enable caching the session by configuring the ESB as per article [1]. However, this involves having a bulky configuration for each destination and requires the user to know all the destination names before using the application. 

This custom mediator enables re-use of JMS sessions specifically for JMS topics that are not known beforehand (in a use case where the topic name is inferred based on user's input). Test results of the 3 approaches in comparison can be found at [2]. In summary, re-using the JMS sessions proved to have about a 3-fold performance improvement as per the tests.  

### Usage

1. Build the source using "mvn clean install". 
2. Copy the "target/JMSPublisherCacheMediator-1.0-SNAPSHOT.jar" into "ESB_HOME/repository/components/dropins" directory.
3. Restart the ESB server.

You can use the mediator within a proxy as follows : 

`<?xml version="1.0" encoding="UTF-8"?>
<proxy xmlns="http://ws.apache.org/ns/synapse"
       name="JMSAPublisher"
       transports="https http"
       startOnLoad="true"
       trace="disable">
   <description/>
   <target>
      <inSequence>
         <property name="OUT_ONLY" value="true"/>
         <property name="FORCE_SC_ACCEPTED" value="true" scope="axis2"/>
         <iterate xmlns:xsd="http://services.samples/xsd"
                  xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope"
                  xmlns:ser="http://services.samples"
                  id="msgIterator"
                  expression="//xsd:symbol">
            <target>
               <sequence>
                  <property name="topicName"
                            expression="//xsd:symbol"
                            scope="default"
                            type="STRING"
                            description="GET STORE ID AKA TOPIC NAME"/>
                  <log level="custom" description="LOG STORE ID">
                     <property name="topicNameOfStore" expression="get-property('topicName')"/>
                  </log>
                  <class name="org.wso2.mediators.custom.JMSPublisherCacheMediator">
                     <property name="connectionFactoryName" value="TopicConnectionFactory"/>
		                 <property name="cacheExpirationInterval" value="3600"/>
                  </class>
               </sequence>
            </target>
         </iterate>
      </inSequence>
      <outSequence>
         <send/>
      </outSequence>
   </target>
</proxy>`

In the above configuration, the "connectionFactoryName" property points to the connectionFactory chosen to publish to topics from the ESB_HOME/repository/conf/jndi.properties file. And the "cacheExpirationInterval" points to the interval at which the cached JMS sessions would expire (in seconds).

*Both these properties are static and cannot be changed once the server is started.

[1] : http://waruapz.blogspot.com/2014/10/jms-performance-tuning-with-wso2-esb.html
[2] : 

