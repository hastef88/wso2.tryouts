General purpose MQTT client for Java
	
	- create multiple parallel subscribers/publishers to an mqtt topic
	- configure message count per a publishing client
	- configure qos options 

Kudos to PamodS for providing a great starting point

Further references : 
	http://www.slideshare.net/paolopat/mqtt-iot-protocols-comparison

	http://www.infoq.com/articles/practical-mqtt-with-paho

	http://public.dhe.ibm.com/software/dw/webservices/ws-mqtt/MQTT_V3.1_Protocol_Specific.pdf

	http://2lemetry.com/2013/08/22/mqtt-in-a-nutshell/

Usage :
	java -jar mqtt_client-1.0-SNAPSHOT-jar-with-dependencies.jar [publisherCount] [subscriberCount] [messagesPerPublisher] [topicName] [qosOption]

Sample : 
	java -jar mqtt_client-1.0-SNAPSHOT-jar-with-dependencies.jar 2 2 10 stocks 1