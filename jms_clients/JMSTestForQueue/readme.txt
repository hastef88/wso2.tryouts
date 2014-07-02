JMS Client for sending and receiving messages using Point-to-Point Model (Queues)

Usage :
	java -jar JMSTestForQueue-1.0-SNAPSHOT-jar-with-dependencies.jar [queueName] [msgCount] [BOTH/RUN_RECEIVER/RUN_SENDER] [waitInterval] [ttl]

For Example : 
	java -jar JMSTestForQueue-1.0-SNAPSHOT-jar-with-dependencies.jar testQueue 1000 BOTH 100 50