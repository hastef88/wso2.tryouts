package org.wso2.mediators.custom;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMOutputFormat;
import org.apache.axiom.om.OMText;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.MessageFormatter;
import org.apache.axis2.transport.base.BaseConstants;
import org.apache.axis2.transport.base.BaseUtils;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.jms.JMSConstants;
import org.apache.axis2.transport.jms.JMSUtils;
import org.apache.axis2.transport.jms.iowrappers.BytesMessageOutputStream;
import org.apache.axis2.util.MessageProcessorSelector;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.utils.ServerConstants;

import javax.activation.DataHandler;
import javax.jms.BytesMessage;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Properties;

public class JMSTopicContext {

    private static final Log log = LogFactory.getLog(JMSTopicContext.class);

    private static final String JNDI_FILE_PATH = System.getProperty(ServerConstants.CARBON_HOME) + File.separator +
            "repository" + File.separator + "conf" + File.separator + "jndi.properties";

    public static InitialContext initialJMSContext;
    public static TopicConnectionFactory topicConnectionFactory;

    private TopicConnection topicConnection;
    private TopicSession topicSession;
    private MessageProducer messageProducer;

    private String topicName;

    private static void initializeStaticContext(String topicName, String connectionFactoryName) throws NamingException, IOException {

        Properties jndiProperties = new Properties();

        jndiProperties.load(new FileInputStream(JNDI_FILE_PATH));

        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, ConfigurationManager.QPID_ICF);
        jndiProperties.put(ConfigurationManager.CF_NAME_PREFIX + ConfigurationManager.CF_NAME, ConfigurationManager.getTCPConnectionURL());
        jndiProperties.put(ConfigurationManager.TOPIC_NAME_PREFIX + topicName, topicName);

        initialJMSContext = new InitialContext(jndiProperties);

        if (null == topicConnectionFactory) {
            topicConnectionFactory = (TopicConnectionFactory) initialJMSContext.lookup(ConfigurationManager.CF_NAME);
        }
    }

    public JMSTopicContext(String topicName, String connectionFactoryName) throws NamingException, JMSException, IOException {

        if (null == initialJMSContext) {
            JMSTopicContext.initializeStaticContext(topicName, connectionFactoryName);
        }

        if (!initialJMSContext.getEnvironment().containsKey(ConfigurationManager.TOPIC_NAME_PREFIX + topicName)) {
            initialJMSContext.addToEnvironment(ConfigurationManager.TOPIC_NAME_PREFIX + topicName, topicName);
        }

        topicConnection = topicConnectionFactory.createTopicConnection();
        topicSession = topicConnection.createTopicSession(false,TopicSession.AUTO_ACKNOWLEDGE);

        Topic topic = (Topic) initialJMSContext.lookup(topicName);

        messageProducer = topicSession.createProducer(topic);

        this.topicName = topicName;
        log.info("Initialized Session for Topic : " + topicName);
    }

    public void publishMessage(MessageContext messageContext, String contentTypeProperty) throws AxisFault, JMSException {

        if (null != topicSession && null != messageProducer) {
            Message messageToPublish = createJMSMessage(messageContext,contentTypeProperty);

            synchronized (this.topicSession) {
                send(messageToPublish,messageContext);
            }
        }
    }

    /**
     * Create a JMS Message from the given MessageContext and using the given
     * session
     *
     * @param msgContext the MessageContext
     * @param contentTypeProperty the message property to be used to store the
     *                            content type
     * @return a JMS message from the context and session
     * @throws JMSException on exception
     * @throws org.apache.axis2.AxisFault on exception
     */
    private Message createJMSMessage(MessageContext msgContext,
                                     String contentTypeProperty) throws JMSException, AxisFault {

        Message message = null;
        String msgType = getProperty(msgContext, JMSConstants.JMS_MESSAGE_TYPE);

        // check the first element of the SOAP body, do we have content wrapped using the
        // default wrapper elements for binary (BaseConstants.DEFAULT_BINARY_WRAPPER) or
        // text (BaseConstants.DEFAULT_TEXT_WRAPPER) ? If so, do not create SOAP messages
        // for JMS but just get the payload in its native format
        String jmsPayloadType = guessMessageType(msgContext);

        if (jmsPayloadType == null) {

            OMOutputFormat format = BaseUtils.getOMOutputFormat(msgContext);
            MessageFormatter messageFormatter;
            try {
                messageFormatter = MessageProcessorSelector.getMessageFormatter(msgContext);
            } catch (AxisFault axisFault) {
                throw new JMSException("Unable to get the message formatter to use");
            }

            String contentType = messageFormatter.getContentType(
                    msgContext, format, msgContext.getSoapAction());

            boolean useBytesMessage =
                    msgType != null && JMSConstants.JMS_BYTE_MESSAGE.equals(msgType) ||
                            contentType.contains(HTTPConstants.HEADER_ACCEPT_MULTIPART_RELATED);

            OutputStream out;
            StringWriter sw;
            if (useBytesMessage) {
                BytesMessage bytesMsg = topicSession.createBytesMessage();
                sw = null;
                out = new BytesMessageOutputStream(bytesMsg);
                message = bytesMsg;
            } else {
                sw = new StringWriter();
                try {
                    out = new WriterOutputStream(sw, format.getCharSetEncoding());
                } catch (UnsupportedCharsetException ex) {
                    handleException("Unsupported encoding " + format.getCharSetEncoding(), ex);
                    return null;
                }
            }

            try {
                messageFormatter.writeTo(msgContext, format, out, true);
                out.close();
            } catch (IOException e) {
                handleException("IO Error while creating BytesMessage", e);
            }

            if (!useBytesMessage) {
                TextMessage txtMsg = topicSession.createTextMessage();
                txtMsg.setText(sw.toString());
                message = txtMsg;
            }

            if (contentTypeProperty != null) {
                message.setStringProperty(contentTypeProperty, contentType);
            }

        } else if (JMSConstants.JMS_BYTE_MESSAGE.equals(jmsPayloadType)) {
            message = topicSession.createBytesMessage();
            BytesMessage bytesMsg = (BytesMessage) message;
            OMElement wrapper = msgContext.getEnvelope().getBody().
                    getFirstChildWithName(BaseConstants.DEFAULT_BINARY_WRAPPER);
            OMNode omNode = wrapper.getFirstOMChild();
            if (omNode != null && omNode instanceof OMText) {
                Object dh = ((OMText) omNode).getDataHandler();
                if (dh != null && dh instanceof DataHandler) {
                    try {
                        ((DataHandler) dh).writeTo(new BytesMessageOutputStream(bytesMsg));
                    } catch (IOException e) {
                        handleException("Error serializing binary content of element : " +
                                BaseConstants.DEFAULT_BINARY_WRAPPER, e);
                    }
                }
            }

        } else if (JMSConstants.JMS_TEXT_MESSAGE.equals(jmsPayloadType)) {
            message = topicSession.createTextMessage();
            TextMessage txtMsg = (TextMessage) message;
            txtMsg.setText(msgContext.getEnvelope().getBody().
                    getFirstChildWithName(BaseConstants.DEFAULT_TEXT_WRAPPER).getText());
        } else if (JMSConstants.JMS_MAP_MESSAGE.equalsIgnoreCase(jmsPayloadType)){
            message = topicSession.createMapMessage();
            JMSUtils.convertXMLtoJMSMap(msgContext.getEnvelope().getBody().getFirstChildWithName(
                    JMSConstants.JMS_MAP_QNAME),(MapMessage)message);
        }

        // set the JMS correlation ID if specified
        String correlationId = getProperty(msgContext, JMSConstants.JMS_COORELATION_ID);
        if (correlationId == null && msgContext.getRelatesTo() != null) {
            correlationId = msgContext.getRelatesTo().getValue();
        }

        if (correlationId != null) {
            assert message != null;
            message.setJMSCorrelationID(correlationId);
        }

        if (msgContext.isServerSide()) {
            // set SOAP Action as a property on the JMS message
            setProperty(message, msgContext, BaseConstants.SOAPACTION);
        } else {
            String action = msgContext.getOptions().getAction();
            if (action != null) {
                assert message != null;
                message.setStringProperty(BaseConstants.SOAPACTION, action);
            }
        }

        JMSUtils.setTransportHeaders(msgContext, message);
        return message;
    }

    /**
     * Guess the message type to use for JMS looking at the message contexts' envelope
     * @param msgContext the message context
     * @return JMSConstants.JMS_BYTE_MESSAGE or JMSConstants.JMS_TEXT_MESSAGE or null
     */
    private String guessMessageType(MessageContext msgContext) {
        OMElement firstChild = msgContext.getEnvelope().getBody().getFirstElement();
        if (firstChild != null) {
            if (BaseConstants.DEFAULT_BINARY_WRAPPER.equals(firstChild.getQName())) {
                return JMSConstants.JMS_BYTE_MESSAGE;
            } else if (BaseConstants.DEFAULT_TEXT_WRAPPER.equals(firstChild.getQName())) {
                return JMSConstants.JMS_TEXT_MESSAGE;
            } else if (JMSConstants.JMS_MAP_QNAME.equals(firstChild.getQName())){
                return  JMSConstants.JMS_MAP_MESSAGE;
            }
        }
        return null;
    }

    private static void setProperty(Message message, MessageContext msgCtx, String key) {

        String value = getProperty(msgCtx, key);
        if (value != null) {
            try {
                message.setStringProperty(key, value);
            } catch (JMSException e) {
                log.warn("Couldn't set message property : " + key + " = " + value, e);
            }
        }
    }

    private static void handleException(String msg, Exception e) throws AxisFault {
        log.error(msg, e);
        throw new AxisFault(msg, e);
    }

    private static String getProperty(MessageContext mc, String key) {
        return (String) mc.getProperty(key);
    }

    private static Integer getIntegerProperty(MessageContext msgCtx, String name) {
        Object o = msgCtx.getProperty(name);
        if (o != null) {
            if (o instanceof Integer) {
                return (Integer) o;
            } else if (o instanceof String) {
                return Integer.parseInt((String) o);
            }
        }
        return null;
    }

    /**
     * Perform actual send of JMS message to the Destination selected
     *
     * @param message the JMS message
     * @param msgCtx the Axis2 MessageContext
     */
    private void send(Message message, MessageContext msgCtx) throws AxisFault {

        Boolean jtaCommit    = getBooleanProperty(msgCtx, BaseConstants.JTA_COMMIT_AFTER_SEND);
        Boolean rollbackOnly = getBooleanProperty(msgCtx, BaseConstants.SET_ROLLBACK_ONLY);
        Boolean persistent   = getBooleanProperty(msgCtx, JMSConstants.JMS_DELIVERY_MODE);
        Integer priority     = getIntegerProperty(msgCtx, JMSConstants.JMS_PRIORITY);
        Integer timeToLive   = getIntegerProperty(msgCtx, JMSConstants.JMS_TIME_TO_LIVE);

        // Do not commit, if message is marked for rollback
        if (rollbackOnly != null && rollbackOnly) {
            jtaCommit = Boolean.FALSE;
        }

        if (persistent != null) {
            try {
                messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            } catch (JMSException e) {
                handleException("Error setting JMS Producer for PERSISTENT delivery", e);
            }
        }
        if (priority != null) {
            try {
                messageProducer.setPriority(priority);
            } catch (JMSException e) {
                handleException("Error setting JMS Producer priority to : " + priority, e);
            }
        }
        if (timeToLive != null) {
            try {
                messageProducer.setTimeToLive(timeToLive);
            } catch (JMSException e) {
                handleException("Error setting JMS Producer TTL to : " + timeToLive, e);
            }
        }

        boolean sendingSuccessful = false;
        // perform actual message sending
        try {
//            if (jmsSpec11 || isQueue == null) {
                messageProducer.send(message);
                log.info("Published message to topic : " + topicName);

//            } else {
//                if (isQueue) {
//                    try {
//                        ((QueueSender) producer).send(message);
//                    } catch (JMSException e) {
//                        createTempQueueConsumer();
//                        ((QueueSender) producer).send(message);
//                    }
//
//                } else {
//                    try {
//                        ((TopicPublisher) producer).publish(message);
//                    } catch (JMSException e) {
//                        createTempTopicSubscriber();
//                        ((TopicPublisher) producer).publish(message);
//                    }
//                }
//            }

            // set the actual MessageID to the message context for use by any others down the line
            String msgId = null;
            try {
                msgId = message.getJMSMessageID();
                if (msgId != null) {
                    msgCtx.setProperty(JMSConstants.JMS_MESSAGE_ID, msgId);
                }
            } catch (JMSException ignore) {}

            sendingSuccessful = true;

            if (log.isDebugEnabled()) {
                log.debug("Sent Message Context ID : " + msgCtx.getMessageID() +
                        " with JMS Message ID : " + msgId +
                        " to destination : " + messageProducer.getDestination());
            }

        } catch (JMSException e) {
            handleException("Error sending message with MessageContext ID : " +
                    msgCtx.getMessageID() + " to destination : " + topicName , e);

        } finally {

            if (jtaCommit != null) {

                UserTransaction ut = (UserTransaction) msgCtx.getProperty(BaseConstants.USER_TRANSACTION);
                if (ut != null) {

                    try {
                        if (sendingSuccessful && jtaCommit) {
                            ut.commit();
                        } else {
                            ut.rollback();
                        }
                        msgCtx.removeProperty(BaseConstants.USER_TRANSACTION);

                        if (log.isDebugEnabled()) {
                            log.debug((sendingSuccessful ? "Committed" : "Rolled back") +
                                    " JTA Transaction");
                        }

                    } catch (Exception e) {
                        handleException("Error committing/rolling back JTA transaction after " +
                                "sending of message with MessageContext ID : " + msgCtx.getMessageID() +
                                " to destination : " + topicName, e);
                    }
                }

            } else {
                try {
                    if (topicSession.getTransacted()) {
                        if (sendingSuccessful && (rollbackOnly == null || !rollbackOnly)) {
                            topicSession.commit();
                        } else {
                            topicSession.rollback();
                        }
                    }

                    if (log.isDebugEnabled()) {
                        log.debug((sendingSuccessful ? "Committed" : "Rolled back") +
                                " local (JMS Session) Transaction");
                    }

                } catch (JMSException e) {
                    handleException("Error committing/rolling back local (i.e. session) " +
                            "transaction after sending of message with MessageContext ID : " +
                            msgCtx.getMessageID() + " to destination : " + topicName, e);
                }
            }
        }
    }

    private static Boolean getBooleanProperty(MessageContext msgCtx, String name) {
        Object o = msgCtx.getProperty(name);
        if (o != null) {
            if (o instanceof Boolean) {
                return (Boolean) o;
            } else if (o instanceof String) {
                return Boolean.valueOf((String) o);
            }
        }
        return null;
    }

    public void close() throws JMSException {

        if (null != messageProducer) {
            messageProducer.close();
        }
        if (null != topicSession) {
            topicSession.close();
        }
        if (null != topicConnection) {
            topicConnection.close();
        }

    }
}
