JMS Client for sending and receiving messages using Publish-Subscribe Model (Topics)

Usage :
	java -jar JMSTestForTopic-1.0-SNAPSHOT-jar-with-dependencies.jar [topicName] [msgCount] [BOTH/RUN_RECEIVER/RUN_SENDER] [waitInterval] [ttl]

For Example : 
	java -jar JMSTestForTopic-1.0-SNAPSHOT-jar-with-dependencies.jar testTopic 1000 BOTH 100 50