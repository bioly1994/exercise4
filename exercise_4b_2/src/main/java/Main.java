import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	private static Connection connection;
	private static Session session;
	private static Destination destination;
	private static MessageConsumer messageConsumer;
	private static int counter =0;
	
	public static void main(String[] args) {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("BIOLY.QUEUE");
			messageConsumer = session.createConsumer(destination);
			
			messageConsumer.setMessageListener(new MessageListener() {
				
				public void onMessage(Message message) {
				counter++;		
				String text;
					
				try {
					text = ((TextMessage) message).getText();
					log.info(text + "message number" +counter);
				} catch (JMSException e) {
					e.printStackTrace();
				}
				}
			});
			
		} catch (JMSException e) {
			
			e.printStackTrace();
		}
		
	}

}
