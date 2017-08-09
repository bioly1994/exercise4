package wdsr.exercise4.sender;

import java.math.BigDecimal;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wdsr.exercise4.Order;

public class JmsSender {
	private static final Logger log = LoggerFactory.getLogger(JmsSender.class);
	
	private final String queueName;
	private final String topicName;
	private Connection connection;
	private Destination destination;
	private Session session;
	private MessageProducer producer;
	

	public JmsSender(final String queueName, final String topicName) throws JMSException {
		this.queueName = queueName;
		this.topicName = topicName;
		
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:62616");		
		connection = connectionFactory.createConnection();
		session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);		
		
	}

	/**
	 * This method creates an Order message with the given parameters and sends it as an ObjectMessage to the queue.
	 * @param orderId ID of the product
	 * @param product Name of the product
	 * @param price Price of the product
	 * @throws JMSException 
	 */
	public void sendOrderToQueue(final int orderId, final String product, final BigDecimal price) throws JMSException {
		
		Order order = new Order(orderId,product,price);		
		this.destination = this.session.createQueue(this.queueName);
		this.producer = this.session.createProducer(this.destination);		
		ObjectMessage message = this.session.createObjectMessage();
		
		message.setObject(order);
		message.setJMSType("Order");
		message.setStringProperty("WDSR-System","OrderProcessor");
		
		this.producer.send(message);
		this.connection.close();
		this.session.close();
		
	}

	/**
	 * This method sends the given String to the queue as a TextMessage.
	 * @param text String to be sent
	 * @throws JMSException 
	 */
	public void sendTextToQueue(String text) throws JMSException {
		
		this.destination = this.session.createQueue(this.queueName);
		this.producer = this.session.createProducer(this.destination);
		TextMessage message = this.session.createTextMessage();
		
		message.setText(text);
		
		this.producer.send(message);
		this.session.close();
		this.connection.close();
	}

	/**
	 * Sends key-value pairs from the given map to the topic as a MapMessage.
	 * @param map Map of key-value pairs to be sent.
	 * @throws JMSException 
	 */
	public void sendMapToTopic(Map<String, String> map) throws JMSException {
		
		this.destination = this.session.createTopic(this.topicName);
		this.producer = this.session.createProducer(this.destination);
		MapMessage message = this.session.createMapMessage();
		
		for(Map.Entry<String,String> item : map.entrySet()){
			message.setString(item.getKey(),item.getValue());			
		}
		
		this.producer.send(message);
		this.connection.close();
		this.session.close();
		
	}
}
